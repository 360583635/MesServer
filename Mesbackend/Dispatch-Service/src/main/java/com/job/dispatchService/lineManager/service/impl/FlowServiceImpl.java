package com.job.dispatchService.lineManager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Flow;
import com.job.common.result.Result;
import com.job.dispatchService.lineManager.mapper.FlowMapper;
import com.job.dispatchService.lineManager.request.FlowPageReq;
import com.job.dispatchService.lineManager.service.FlowService;
import org.springframework.stereotype.Service;


@Service
public class FlowServiceImpl extends ServiceImpl<FlowMapper, Flow> implements FlowService {

}