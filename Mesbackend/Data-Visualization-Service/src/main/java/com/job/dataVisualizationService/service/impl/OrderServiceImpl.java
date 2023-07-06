package com.job.dataVisualizationService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.dataVisualizationService.mapper.OrderMapper;
import com.job.dataVisualizationService.pojo.Order;
import com.job.dataVisualizationService.service.OrderService;
import org.springframework.stereotype.Service;

/**
 * @Author 菜狗
 */
@Service
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {
    
}
