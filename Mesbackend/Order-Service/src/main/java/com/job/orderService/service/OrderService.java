package com.job.orderService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Order;
import com.job.common.pojo.Users;
import com.job.orderService.common.result.Result;

import java.util.List;

public interface OrderService extends IService<Order> {
    Result<Order> addOrder(Order order);
    Result<Order> updateOrder(String orderId);
    Result<Order> saveUpdateOrder(Order order);
    Result<List<Order>> selectOrderByName(String typeName);
    Result<Order> showOrderDetail(String orderId);
    Result<Order> deleteOrder(String orderId);
//    Result<Order> handOrder(String orderId);




}
