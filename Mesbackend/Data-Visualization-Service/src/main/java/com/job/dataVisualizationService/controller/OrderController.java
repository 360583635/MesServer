package com.job.dataVisualizationService.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.dataVisualizationService.common.result.Result;
import com.job.dataVisualizationService.pojo.OrderData;
import com.job.dataVisualizationService.service.OrderService;
import com.job.pojo.pojo.Order;
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
        OrderData orderData = orderService.classification();
        return Result.success(order,"success");
    }

    @GetMapping("/count")    //数量金额统计
    public Result<OrderData> getCount(){
        OrderData orderData = orderService.countData();
        return Result.success(orderData,"success");
    }

    @GetMapping("/prediction")    //金额预测和数量
    public Result<OrderData> getPAmount(){
        OrderData orderData = orderService.preData();
        return Result.success(orderData,"success");

    }



}
