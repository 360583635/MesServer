package com.job.dataVisualizationService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Process;
import com.job.dataVisualizationService.mapper.ProcessMapper;
import com.job.dataVisualizationService.service.ProcessService;
import org.springframework.stereotype.Service;


/**
 * @Auther:Liang
 */
@Service
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {
}
