package com.job.dispatchService.LineManager.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.dispatchService.LineManager.mapper.LineMapper;
import com.job.dispatchService.LineManager.service.LineService;
import com.job.dispatchService.LineManager.pojo.TLine;
import org.springframework.stereotype.Service;


@Service
public class LineServiceImpl extends ServiceImpl<LineMapper, TLine> implements LineService {
}