package com.job.orderService.controller;

import com.job.orderService.common.result.Result;
import com.job.orderService.pojo.Order;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {
    /**
     * 创建订单
     * @return
     */
    @PostMapping("/addOrder")
    public Result<Order> addOrder(){
        return Result.success(new Order(),"success");
    }
}
