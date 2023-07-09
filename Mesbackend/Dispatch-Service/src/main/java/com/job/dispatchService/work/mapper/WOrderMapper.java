package com.job.dispatchService.work.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.job.common.pojo.Order;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

public interface WOrderMapper extends BaseMapper<Order> {
}
