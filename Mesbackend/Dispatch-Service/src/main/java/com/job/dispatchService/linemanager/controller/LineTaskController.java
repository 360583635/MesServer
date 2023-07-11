package com.job.dispatchservice.linemanager.controller;

import com.job.dispatchservice.linemanager.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.time.LocalTime;

/**
 * @author 庸俗可耐
 * @create 2023-07-10-20:30
 * @description
 */
@Component
public class LineTaskController {

    @Autowired
    private LineService lineService;

    @Async
    public void lineTask(){
        // TODO: 2023/7/10 流水线实列执行流程
    }

    @Async
    @Scheduled(initialDelay = 0,fixedDelay = 3000)
    public void queryOrders() throws InterruptedException {
        // TODO: 2023/7/10 每隔3秒执行一次查询订单
        System.out.println("线程:"+Thread.currentThread().getName()+",当前执行时间:"+ LocalTime.now());
        Thread.sleep(5000);
    }

    public void test() throws InterruptedException {
        for(int i=0;i<5;i++){
            queryOrders();
        }
    }
}
