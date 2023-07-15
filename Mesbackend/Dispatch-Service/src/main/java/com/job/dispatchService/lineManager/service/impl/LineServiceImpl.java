package com.job.dispatchService.lineManager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Line;
import com.job.dispatchService.lineManager.service.LineService;
import com.job.dispatchservice.linemanager.mapper.LineMapper;
import org.springframework.stereotype.Service;


@Service
public class LineServiceImpl extends ServiceImpl<LineMapper, Line> implements LineService {
}