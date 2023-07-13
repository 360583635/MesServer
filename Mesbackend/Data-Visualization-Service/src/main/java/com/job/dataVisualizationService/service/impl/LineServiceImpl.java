package com.job.dataVisualizationService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.dataVisualizationService.mapper.LineMapper;
import com.job.common.pojo.Line;
import com.job.dataVisualizationService.service.LineService;
import org.springframework.stereotype.Service;

/**
 * @Auther:Liang
 */
@Service
public class LineServiceImpl extends ServiceImpl<LineMapper, Line> implements LineService {
}
