package com.job.dataVisualizationService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.job.dataVisualizationService.pojo.OrderData;
import com.job.common.pojo.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;


/**
 * @Author 菜狗
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {


}
