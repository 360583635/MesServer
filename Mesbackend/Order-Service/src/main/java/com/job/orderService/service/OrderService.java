package com.job.orderService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Order;
import com.job.orderService.common.result.Result;
import com.job.orderService.vo.FlowVo;

import java.util.List;

public interface OrderService extends IService<Order> {
    Result<Object> addOrder(Order order,String token);
    Result<Order> updateOrder(String orderId);
    Result<Order> saveUpdateOrder(Order order);
    Result<List<Order>> selectOrderByName(String typeName);
    Result<Order> showOrderDetail(String orderId);
    Result<Order> deleteOrder(String orderId);
//    Result<Order> handOrder(String orderId);
    Result<List<FlowVo>> toAddOrder(String token);






}
