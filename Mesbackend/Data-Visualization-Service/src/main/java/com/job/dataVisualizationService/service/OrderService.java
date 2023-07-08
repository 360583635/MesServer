package com.job.dataVisualizationService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.dataVisualizationService.pojo.OrderData;
import com.job.common.pojo.Order;

/**
 * @Author 菜狗
 */
public interface OrderService extends IService<Order> {
 OrderData preData();
 OrderData countData();

 OrderData classification();
}
