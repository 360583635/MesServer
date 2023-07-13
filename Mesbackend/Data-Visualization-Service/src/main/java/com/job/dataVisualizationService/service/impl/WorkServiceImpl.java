package com.job.dataVisualizationService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.dataVisualizationService.mapper.WorkMapper;
import com.job.common.pojo.Work;
import com.job.dataVisualizationService.service.WorkService;
import org.springframework.stereotype.Service;

/**
 * @Auther:Liang
 */
@Service
public class WorkServiceImpl extends ServiceImpl<WorkMapper, Work> implements WorkService {
}
