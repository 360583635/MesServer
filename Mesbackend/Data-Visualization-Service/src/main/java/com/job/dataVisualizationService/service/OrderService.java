package com.job.dataVisualizationService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.dataVisualizationService.pojo.OrderData;
import com.job.common.pojo.Order;

import java.util.Map;

/**
 * @Author 菜狗
 */
public interface OrderService extends IService<Order> {
 OrderData preData();
 Map<Object,Object> countData(OrderData order);

 Map<Object,Object> classification(OrderData order);

 Map<Object,Object> countOneData(OrderData order);
}
