package com.job.dispatchService.LineManager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Process;
import com.job.dispatchService.LineManager.mapper.ProcessMapper;
import com.job.dispatchService.LineManager.service.ProcessService;
import org.springframework.stereotype.Service;


@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {
}