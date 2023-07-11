package com.job.dispatchservice.linemanager.controller;

import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.Line;
import com.job.common.pojo.Order;
import com.job.common.pojo.Work;
import com.job.dispatchservice.linemanager.service.LineService;
import com.job.dispatchservice.work.service.WorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;

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
    private LineService lineService;

    @Autowired
    private WorkService workService;

    @Async
    @Scheduled(initialDelay = 0,fixedDelay = 0)
    public void lineInstance(Line line) throws InterruptedException{
        // TODO: 2023/7/10 流水线实列执行流程
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

            if(work!=null){
                //该订单曾经被执行过

            }else{
                //该订单未被执行

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
