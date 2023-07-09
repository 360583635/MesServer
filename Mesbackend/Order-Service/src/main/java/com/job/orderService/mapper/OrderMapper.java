package com.job.orderService.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.job.common.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {

}
