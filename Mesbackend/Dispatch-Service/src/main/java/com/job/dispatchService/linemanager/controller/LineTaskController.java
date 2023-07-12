package com.job.dispatchservice.linemanager.controller;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.FlowProcessRelation;
import com.job.common.pojo.Line;
import com.job.common.pojo.Order;
import com.job.common.pojo.Work;
import com.job.dispatchservice.linemanager.service.FlowProcessRelationService;
import com.job.dispatchservice.linemanager.service.FlowService;
import com.job.dispatchservice.linemanager.service.LineService;
import com.job.dispatchservice.work.controller.WorkController;
import com.job.dispatchservice.work.service.WorkService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

import javax.sound.sampled.BooleanControl;
import java.time.LocalTime;
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
        if(orderQueue.isEmpty()==false&&line.getStatus().equals("0")){
            Order order = orderQueue.get(0);
            orderQueue.remove(order);
            //查询该订单是否有工单关联
            LambdaQueryWrapper<Work> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Work::getWOrderId,order.getOrderId());
            Work work = workService.getOne(queryWrapper);

            flowId = line.getLineFlowId();
            if(work!=null){
                //该订单曾经被执行过
                firstProcessId = work.getWProcessId();
            }else{
                //该订单未被执行
                LambdaQueryWrapper<FlowProcessRelation> queryWrapper1 = new LambdaQueryWrapper<>();
                queryWrapper1
                        .eq(FlowProcessRelation::getFlowId,flowId)
                        .eq(FlowProcessRelation::getSortNum,"0");
                FlowProcessRelation flowProcessRelation = flowProcessRelationService.getOne(queryWrapper1);
                firstProcessId = flowProcessRelation.getProcessId();
            }
            //获取头工序
            LambdaQueryWrapper<FlowProcessRelation> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2
                    .eq(FlowProcessRelation::getProcessId,firstProcessId)
                    .eq(FlowProcessRelation::getFlowId,flowId);
            firstRelation = flowProcessRelationService.getOne(queryWrapper2);
            if(firstRelation!=null){
                //将流水线状态修改为繁忙
                order.setProductionStatus(2);
                line.setStatus("2");
            }
            FlowProcessRelation relation = firstRelation;
            //遍历工序开始生产
            while(!StringUtil.isNullOrEmpty(relation.getNextProcessId())){
                //调用工单处理方法
                String currentProcessId = relation.getProcessId();
                String workingStatus = String.valueOf(workController.working(currentProcessId,order.getOrderId()));
                if("error".equals(workingStatus)){
                    //如果工单运行失败
                    order.setProductionStatus(3);
                    line.setStatus("3");
                }else if("ok".equals(workingStatus)){
                    //如果工单运行成功，获取下一个工序
                    LambdaQueryWrapper<FlowProcessRelation> queryWrapper3 = new LambdaQueryWrapper<>();
                    queryWrapper3.eq(FlowProcessRelation::getProcessId,relation.getNextProcessId()).eq(FlowProcessRelation::getFlowId,flowId);
                    relation = flowProcessRelationService.getOne(queryWrapper3);
                }
            }
            //判断此时的流程是否为最后的流程
           if("lastProcess".equals(relation.getProcessType())){
               //调用工单处理方法
               String currentProcessId = relation.getProcessId();
               String workingStatus = String.valueOf(workController.working(currentProcessId,order.getOrderId()));
               if("error".equals(workingStatus)){
                   //如果工单运行失败

               }else if("ok".equals(workingStatus)){
                   //如果工单运行成功
                   order.setProductionStatus(4);
                   line.setStatus("0");
               }
           }
        }
    }

    @Async
    @Scheduled(initialDelay = 0,fixedRate = 3000)
    public void queryOrders() throws InterruptedException {
        // TODO: 2023/7/10 每隔3秒执行一次查询订单

    }

    @Scheduled(initialDelay = 0,fixedDelay = 10000)
    public void lineTask() throws InterruptedException {
        LambdaQueryWrapper<Line> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Line::getStatus,"0");
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
    private static Thread findThreadByName(String name) {
        for (Thread thread : Thread.getAllStackTraces().keySet()) {
            if (thread.getName().equals(name)) {
                return thread;
            }
        }
        return null;
    }

}
