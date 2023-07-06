package com.job.dispatchService.LineManager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.dispatchService.LineManager.mapper.ProcessMapper;
import com.job.dispatchService.LineManager.service.ProcessService;
import com.job.dispatchService.LineManager.pojo.TProcess;
import org.springframework.stereotype.Service;


@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, TProcess> implements ProcessService {
}