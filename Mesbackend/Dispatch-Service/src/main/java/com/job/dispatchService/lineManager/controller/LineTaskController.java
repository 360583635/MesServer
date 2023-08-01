package com.job.dispatchService.lineManager.controller;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.nacos.shaded.io.grpc.internal.JsonUtil;
import com.job.common.pojo.Line;
import com.job.common.pojo.FlowProcessRelation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.Order;
import com.job.common.pojo.Work;
import com.job.common.redis.RedisCache;
import com.job.dispatchService.lineManager.service.FlowProcessRelationService;
import com.job.dispatchService.lineManager.service.LineService;
import com.job.dispatchService.lineManager.service.FlowService;
import com.job.dispatchService.work.controller.WorkController;
import com.job.dispatchService.work.service.WorkService;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.job.common.result.Result;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Stream;

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

    //1.实例化ReentrantLock
    private ReentrantLock lock = new ReentrantLock();


    @Async
    public void lineInstance(Line line) throws InterruptedException{

        // TODO: 2023/7/10 流水线实列执行流程
        FlowProcessRelation firstRelation;
        String firstProcessId;
        String flowId;
        String lineName = line.getLine();
        String lineId = line.getId();
        Thread.currentThread().setName(lineName+lineId);
        /*List<Order> orderQueue */
        Vector<Order> orderQueue = new Vector<>();
        if(lineName!=null){
            boolean existsKey = redisCache.hasKey(lineName);
            if(existsKey==true){
                //订单队列
                orderQueue = new Vector<Order>(redisCache.getCacheList(lineName));
            }
                while (true) {

                    try {
                        lock.lock();
                        //判断订单列表是否有数据，流水线状态是否为 空闲或完成
                        if ((orderQueue.isEmpty() == false && line.getLineStatus().equals("0")) || (orderQueue.isEmpty() == false && line.getLineStatus().equals("4"))) {
                            //从订单列表里获取订单
                            Order order = orderQueue.get(0);
                            orderQueue.remove(order);
                            //查询该订单是否有工单关联
                            LambdaQueryWrapper<Work> queryWrapper = new LambdaQueryWrapper<>();
                            queryWrapper
                                    .eq(Work::getWState, 4)
                                    .eq(Work::getWOrderId, order.getOrderId());
                            Work work = workService.getOne(queryWrapper);

                            flowId = line.getLineFlowId();
                            if (work != null) {
                                //该订单曾经被执行过
                                //接取该订单异常的那个工序
                                firstProcessId = work.getWProcessId();
                            } else {
                                //该订单未被执行
                                LambdaQueryWrapper<FlowProcessRelation> queryWrapper1 = new LambdaQueryWrapper<>();
                                queryWrapper1
                                        .eq(FlowProcessRelation::getIsDelete, 1)
                                        .eq(FlowProcessRelation::getFlowId, flowId)
                                        .eq(FlowProcessRelation::getSortNum, "0");
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
                                //将流水线状态修改为繁忙
                                order.setProductionStatus(2);
                                line.setLineStatus("2");
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
                                        line.setExceptionCount(line.getExceptionCount() + 1);
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
                                        line.setExceptionCount(line.getExceptionCount() + 1);
                                    } else if ("ok".equals(workingStatus)) {
                                        //如果工单运行成功，将订单和流水线状态设置为 完成
                                        order.setProductionStatus(4);
                                        line.setLineStatus("4");
                                        line.setSuccessCount(line.getSuccessCount() + 1);
                                    }
                                }
                            }

                        }
                    }finally {
                        lock.unlock();
                    }
                }
        }
    }

    @Async
    @Scheduled(initialDelay = 0,fixedRate = 3000*60)
    public void queryOrders() throws InterruptedException {
        // TODO: 2023/7/10 每隔3分钟执行一次查询订单
        boolean b = redisCache.hasKey("orderPQ");
        if(b==true){
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
                        if (redisCache.getCacheList(lineName) == null && StringUtil.isNullOrEmpty(lineName) == false) {
                            Vector<Order> orderQueue = new Vector<>();
                            orderQueue.add(order);
                            redisCache.setCacheList(lineName, orderQueue);
                        } else {
                            redisCache.getCacheList(lineName).add(order);
                        }
                    }else{
                        log.error("LineTaskController--订单列表中订单未匹配流水线");
                    }
                }
            }else{
                log.error("LineTaskController--订单列表为空");
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
                    lineInstance(line);
                }
            }
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
                log.info("查询到的线程名:"+name);
                return thread;
            }
        }
        return null;
    }

}
