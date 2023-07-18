package com.job.dispatchService.lineManager.controller;

import com.job.common.pojo.Line;
import com.job.common.pojo.FlowProcessRelation;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.Order;
import com.job.common.pojo.Work;
import com.job.dispatchService.lineManager.service.FlowProcessRelationService;
import com.job.dispatchService.lineManager.service.LineService;
import com.job.dispatchService.lineManager.service.FlowService;
import com.job.dispatchService.lineManager.utils.RedisCache;
import com.job.dispatchService.work.controller.WorkController;
import com.job.dispatchService.work.service.WorkService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.job.common.result.Result;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-10-20:30
 * @description
 */
@Component
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


    @Async
    public void lineInstance(Line line) throws InterruptedException{
        // TODO: 2023/7/10 流水线实列执行流程
        FlowProcessRelation firstRelation;
        String firstProcessId;
        String flowId;
        String lineName = line.getLine();
        String lineId = line.getId();
        Thread.currentThread().setName(lineName+lineId);
        //订单队列
        List<Order> orderQueue = new LinkedList<>();
        while(true) {
            //判断订单列表是否有数据，流水线状态是否为 空闲或完成
            if ((orderQueue.isEmpty() == false && line.getLineStatus().equals("0")) || (orderQueue.isEmpty() == false && line.getLineStatus().equals("4")) ) {
                //从订单列表里获取订单
                Order order = orderQueue.get(0);
                orderQueue.remove(order);
                //查询该订单是否有工单关联
                LambdaQueryWrapper<Work> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper
                        .eq(Work::getWState,4)
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
                }
                FlowProcessRelation relation = firstRelation;
                //遍历工序开始生产
                while (!StringUtil.isNullOrEmpty(relation.getNextProcessId())) {
                    //调用工单处理方法
                    String currentProcessId = relation.getProcessId();
                    String workingStatus = String.valueOf(workController.working(currentProcessId, order.getOrderId()));
                    if ("error".equals(workingStatus)) {
                        //如果工单运行失败
                        order.setProductionStatus(3);
                        line.setLineStatus("3");
                        line.setExceptionCount(line.getExceptionCount()+1);
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
                        line.setLineStatus("3");
                    } else if ("ok".equals(workingStatus)) {
                        //如果工单运行成功，将订单和流水线状态设置为 完成
                        order.setProductionStatus(4);
                        line.setLineStatus("4");
                    }
                }
            }
        }
    }

    @Async
    @Scheduled(initialDelay = 0,fixedRate = 3000)
    public void queryOrders() throws InterruptedException {
        // TODO: 2023/7/10 每隔3秒执行一次查询订单

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
                return thread;
            }
        }
        return null;
    }

}
