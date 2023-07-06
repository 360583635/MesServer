package com.job.dataVisualizationService.controller;

import com.job.dataVisualizationService.common.result.Result;
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
//        Order order = orderService.getClassification();
        return Result.success(new Order(),"success");
    }

    @GetMapping("/amount")
    public Result<Order> getAmount(){
        return Result.success(new Order(),"success");
    }


}
