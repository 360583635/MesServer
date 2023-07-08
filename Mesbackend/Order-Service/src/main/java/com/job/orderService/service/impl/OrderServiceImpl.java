package com.job.orderService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.orderService.mapper.OrderMapper;
import com.job.orderService.service.OrderService;
import com.job.pojo.pojo.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
}
