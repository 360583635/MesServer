package com.job.dispatchService.LineManager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.dispatchService.LineManager.mapper.FlowMapper;
import com.job.dispatchService.LineManager.service.FlowService;
import com.job.dispatchService.LineManager.pojo.TFlow;
import org.springframework.stereotype.Service;


@Service
public class FlowServiceImpl extends ServiceImpl<FlowMapper, TFlow> implements FlowService {
}