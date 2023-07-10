package com.job.dispatchservice.linemanager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Process;
import com.job.dispatchservice.linemanager.mapper.ProcessMapper;
import com.job.dispatchservice.linemanager.service.ProcessService;
import org.springframework.stereotype.Service;


@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {
}