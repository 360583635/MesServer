package com.job.orderService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Produce;
import com.job.orderService.mapper.ProduceMapper;
import com.job.orderService.service.ProduceService;
import org.springframework.stereotype.Service;

@Service
public class ProduceServiceImpl extends ServiceImpl<ProduceMapper, Produce> implements ProduceService {
}
