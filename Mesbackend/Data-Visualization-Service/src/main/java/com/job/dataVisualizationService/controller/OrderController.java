package com.job.dataVisualizationService.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.Order;
import com.job.common.result.Result;
import com.job.dataVisualizationService.pojo.OrderData;
import com.job.dataVisualizationService.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Author 菜狗
 */
@RestController
@RequestMapping("/data/order")
@Slf4j
public class OrderController {
    @Autowired
    private OrderService orderService;


    @GetMapping("/classification")    //分类占比
    public Result<Object> getClassification( OrderData order){
        Map<Object,Object> map = orderService.classification(order);
        return Result.success(map,"success");
    }

    @GetMapping ("/count")    //全部数量金额统计
    public Result<Object> getCount(OrderData order){
        Map<Object,Object> map = orderService.countData(order);
        return Result.success(map,"success");
    }

    @GetMapping("/countone")    //单个数量金额统计
    public Result<Object> getCountOne( OrderData order){
        Map<Object,Object> map = orderService.countOneData(order);
        return Result.success(map,"success");
    }

    @GetMapping("/prediction")    //金额预测和数量
    public Result<OrderData> getPAmount(){
        OrderData orderData = orderService.preData();
        log.info(orderData.toString());
        return Result.success(orderData,"success");
    }
}
