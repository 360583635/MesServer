package com.job.dataVisualizationService.controller;

import com.job.dataVisualizationService.common.result.Result;
import com.job.dataVisualizationService.mapper.OrderMapper;
import com.job.dataVisualizationService.pojo.Order;
import com.job.dataVisualizationService.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 菜狗
 */
@RestController
@RequestMapping("/data/order")
public class OrderController {
    @Autowired
    private OrderService orderService;


    @GetMapping("/classification")    //分类占比
    public Result<Order> getClassification(){
        Order order = new Order();
        order.setAuditor("zx");
        System.out.println(1);
        orderService.save(order);
        return Result.success(order,"success");
    }

    @GetMapping("/amount")   //金额统计
    public Result<Order> getAmount(){
        return Result.success(new Order(),"success");
    }

    @GetMapping("/count")    //数量统计
    public Result<Order> getCount(){
        return null;
    }

    @GetMapping("/prediction/amount")    //金额预测
    public Result<Order> getPAmount(){
        return null;
    }

    @GetMapping("/prediction/count")      //数量预测
    public Result<Order> getPCount(){
        return null;
    }
}
