package com.job.dataVisualizationService.controller;

import com.job.dataVisualizationService.common.result.Result;
import com.job.dataVisualizationService.pojo.Order;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author 菜狗
 */
@RestController
@RequestMapping("/data/order")
public class OrderController {

    @GetMapping("/classification")    //分类占比
    public Result<Order> getClassification(){
        return Result.success(new Order(),"success");
    }

}
