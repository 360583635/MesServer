package com.job.dispatchservice.linemanager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Flow;
import com.job.dispatchservice.linemanager.mapper.FlowMapper;
import com.job.dispatchservice.linemanager.service.FlowService;
import org.springframework.stereotype.Service;


@Service
public class FlowServiceImpl extends ServiceImpl<FlowMapper, Flow> implements FlowService {
}