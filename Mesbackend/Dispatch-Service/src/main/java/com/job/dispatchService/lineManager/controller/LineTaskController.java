package com.job.dispatchService.lineManager.controller;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.pojo.Line;
import com.job.common.pojo.Order;
import com.job.common.pojo.Work;
import com.job.common.redis.RedisCache;
import com.job.dispatchService.lineManager.service.FlowProcessRelationService;
import com.job.dispatchService.lineManager.service.FlowService;
import com.job.dispatchService.lineManager.service.LineService;
import com.job.dispatchService.work.config.StateConfig;
import com.job.dispatchService.work.controller.WorkController;
import com.job.dispatchService.work.service.WorkService;
import com.job.feign.clients.OrderClient;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author 庸俗可耐
 * @create 2023-07-10-20:30
 * @description
 */
@Component
@Slf4j
public class LineTaskController {

    @Autowired
    private WorkController workController;

    @Autowired
    private LineService lineService;

    @Autowired
    private FlowService flowService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private FlowProcessRelationService flowProcessRelationService;
    @Autowired
    private WorkService workService;

    @Autowired
    private OrderClient orderClient;

    //1.实例化ReentrantLock
    private ReentrantLock lock = new ReentrantLock();


    @Async
    public void lineInstance(Line line) throws InterruptedException{

        boolean existsKey;

        // TODO: 2023/7/10 流水线实列执行流程
        FlowProcessRelation firstRelation;
        String firstProcessId;
        String flowId;
        String lineName = line.getLine();
        String lineId = line.getId();
        Thread.currentThread().setName(lineName+lineId);
        log.info(lineName+"线程创建成功，"+DateUtil.date());
        /*List<Order> orderQueue */
        Vector<Order> orderQueue = new Vector<Order>();
        if(lineName!=null){
                while (true) {
                    existsKey = redisCache.hasKey(lineName);
                    if (existsKey) {
                        if(redisCache.getCacheList(lineName).size()>0) {
                            log.info("redis中订单列表存在," + lineName + "流水线实体待机中，" + DateUtil.date());
                            //订单队列
                            orderQueue = redisCache.getCacheList(lineName).stream().map(item -> {
                                return (Order) item;
                            }).collect(Collectors.toCollection(Vector::new));

                            redisCache.deleteObject(lineName);
                            try {
                                lock.lock();
                                //判断订单列表是否有数据，流水线状态是否为 空闲或完成
                                if ((!orderQueue.isEmpty() && line.getLineStatus().equals("0")) || (!orderQueue.isEmpty() && line.getLineStatus().equals("4"))) {
                                    //从订单列表里获取订单
                                    Order order = orderQueue.get(0);
                                    orderQueue.remove(order);
                                    log.info(lineName + "流水线实体获取到" + order.getOrderId() + "订单，" + DateUtil.date());
                                    line.setOrderCount(line.getOrderCount() + 1);
                                    //查询该订单是否有工单关联
                                    LambdaQueryWrapper<Work> queryWrapper = new LambdaQueryWrapper<>();
                                    queryWrapper
                                            .eq(Work::getWState, StateConfig.EXCEPTION_STATE)
                                            .eq(Work::getWOrderId, order.getOrderId());
                                    List<Work> workList = workService.list(queryWrapper);

                                    flowId = line.getLineFlowId();
                                    if (workList.size() > 0) {
                                        //该订单曾经被执行过
                                        //接取该订单异常的那个工序
                                        firstProcessId = workList.get(0).getWProcessId();
                                    } else {
                                        //该订单未被执行
                                        LambdaQueryWrapper<FlowProcessRelation> queryWrapper1 = new LambdaQueryWrapper<>();
                                        queryWrapper1
                                                .eq(FlowProcessRelation::getIsDelete, 1)
                                                .eq(FlowProcessRelation::getFlowId, flowId)
                                                .eq(FlowProcessRelation::getSortNum, "1");
                                        FlowProcessRelation flowProcessRelation = flowProcessRelationService.getOne(queryWrapper1);
                                        firstProcessId = flowProcessRelation.getProcessId();
                                    }
                                    //获取头工序
                                    LambdaQueryWrapper<FlowProcessRelation> queryWrapper2 = new LambdaQueryWrapper<>();
                                    queryWrapper2
                                            .eq(FlowProcessRelation::getIsDelete, 1)
                                            .eq(FlowProcessRelation::getProcessId, firstProcessId)
                                            .eq(FlowProcessRelation::getFlowId, flowId);
                                    firstRelation = flowProcessRelationService.getOne(queryWrapper2);
                                    if (firstRelation != null) {
                                        log.info(lineName + "流水线实体开始执行" + order.getOrderId() + "订单，" + DateUtil.date());
                                        //将流水线状态修改为繁忙
                                        order.setProductionStatus(2);
                                        line.setLineStatus("2");
                                        orderClient.updateByOne(order);
                                        lineService.updateById(line);
                                        FlowProcessRelation relation = firstRelation;
                                        //遍历工序开始生产
                                        while (!StringUtil.isNullOrEmpty(relation.getNextProcessId())) {
                                            //调用工单处理方法
                                            String currentProcessId = relation.getProcessId();
                                            String workingStatus = String.valueOf(workController.working(currentProcessId, order.getOrderId()));
                                            if ("error".equals(workingStatus)) {
                                                //如果工单运行失败
                                                order.setProductionStatus(3);
                                                /*line.setLineStatus("3");*/
                                                line.setLineStatus("0");
                                                line.setExceptionCount(line.getExceptionCount() + 1);
                                                orderClient.updateByOne(order);
                                                lineService.updateById(line);
                                                log.error(lineName + "流水线异常，" + order.getOrderId() + "订单执行失败");
                                                // TODO: 2023/7/15 流水线状态为异常后，实时监控工单异常处理信息

                                            } else if ("ok".equals(workingStatus)) {
                                                //如果工单运行成功，获取下一个工序
                                                LambdaQueryWrapper<FlowProcessRelation> queryWrapper3 = new LambdaQueryWrapper<>();
                                                queryWrapper3
                                                        .eq(FlowProcessRelation::getIsDelete, 1)
                                                        .eq(FlowProcessRelation::getProcessId, relation.getNextProcessId())
                                                        .eq(FlowProcessRelation::getFlowId, flowId);
                                                relation = flowProcessRelationService.getOne(queryWrapper3);
                                            }
                                        }
                                        //判断此时的流程是否为最后的流程
                                        if ("lastProcess".equals(relation.getProcessType())) {
                                            //调用工单处理方法
                                            String currentProcessId = relation.getProcessId();
                                            String workingStatus = String.valueOf(workController.working(currentProcessId, order.getOrderId()));
                                            if ("error".equals(workingStatus)) {
                                                //如果工单运行失败，将订单和流水线状态设置为 异常
                                                order.setProductionStatus(3);
                                                /*line.setLineStatus("3");*/
                                                line.setLineStatus("0");
                                                line.setExceptionCount(line.getExceptionCount() + 1);
                                                orderClient.updateByOne(order);
                                                lineService.updateById(line);
                                                log.error(lineName + "流水线异常，" + order.getOrderId() + "订单执行失败");
                                            } else if ("ok".equals(workingStatus)) {
                                                //如果工单运行成功，将订单和流水线状态设置为 完成
                                                order.setProductionStatus(4);
                                                line.setLineStatus("4");
                                                line.setSuccessCount(line.getSuccessCount() + 1);
                                                orderClient.updateByOne(order);
                                                lineService.updateById(line);
                                                log.error(lineName + "流水线，" + order.getOrderId() + "订单执行成功");
                                            }
                                        }
                                    }

                                }
                            } finally {
                                lock.unlock();
                            }
                        }
                    }else{
                        log.error("redis中订单列表不存在，"+lineName+"流水线实体待机中，"+DateUtil.date());
                        Thread.sleep(6000);
                    }
                }
        }
    }

    @Async
    @Scheduled(initialDelay = 0,fixedRate = 1000*60)
    public void queryOrders() throws InterruptedException {
        // TODO: 2023/7/10 每隔3分钟执行一次查询订单
        boolean b = redisCache.hasKey("orderPQ");
        if(b==true&&redisCache.getCacheObject("orderPQ")!=null){
            Vector<Order> orderPQ = new Vector<>();
            JSONArray array = new JSONArray(redisCache.getCacheObject("orderPQ"));
            array.forEach(element ->{
                try{
                    orderPQ.add(JSON.parseObject(JSON.toJSONString(element),Order.class));
                }catch (JSONException e){
                    e.printStackTrace();
                }
            });
            if(orderPQ!=null&&orderPQ.size()>0) {
                for (Order order : orderPQ) {
                    String lineName = order.getProductLine();
                    if(!StringUtil.isNullOrEmpty(lineName)) {
                        log.info("派发给流水线实体"+lineName+"的订单"+order.getOrderId()+"开始存入到对应流水线的订单列表中"+ DateUtil.date());
                            Vector<Order> orderQueue = new Vector<>();
                            orderQueue.add(order);
                            redisCache.setCacheList(lineName, orderQueue);
                        log.info("派发给流水线实体"+lineName+"的订单"+order.getOrderId()+"存入成功，"+ DateUtil.date());
                    }else{
                        log.error("LineTaskController--订单列表中订单未匹配流水线，"+ DateUtil.date());
                    }
                }

                redisCache.setCacheObject("orderPQ",null);
            }else{
                log.error("LineTaskController--订单列表为空，"+ DateUtil.date());
            }
        }

    }
    
    @Scheduled(initialDelay = 0,fixedDelay = 5000)
    @Async
    public void lineTask() throws InterruptedException {
        LambdaQueryWrapper<Line> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(Line::getIsDelete,1)
                .eq(Line::getLineStatus,"0");
        List<Line> list = lineService.list(queryWrapper);
        if(list.isEmpty()==false&&list.size()>0){
            for(Line line : list){
                String lineName = line.getLine();
                String lineId = line.getId();
                Thread threadByName = findThreadByName(lineName+lineId);
                if(threadByName==null){
                    log.info(lineName+"线程不存在，开始创建，"+ DateUtil.date());
                    lineInstance(line);
                }else{
                    log.info(threadByName+"线程存在，"+ DateUtil.date());
                }
            }
        }else{
            log.error("暂时没有流水线实体可供使用，"+ DateUtil.date());
        }
    }

    /**
     * 根据线程名获取线程
     * @param name
     * @return
     */
    public static Thread findThreadByName(String name) {
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.getName().equals(name)) {
                return thread;
            }
        }
        return null;
    }

}
