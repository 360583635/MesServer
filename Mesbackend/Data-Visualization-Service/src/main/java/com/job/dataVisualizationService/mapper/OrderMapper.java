package com.job.dataVisualizationService.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.job.common.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @Author 菜狗
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
