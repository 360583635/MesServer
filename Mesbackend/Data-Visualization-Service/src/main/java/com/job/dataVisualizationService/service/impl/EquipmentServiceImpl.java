package com.job.dataVisualizationService.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Equipment;
import com.job.common.pojo.Flow;
import com.job.dataVisualizationService.mapper.EquipmentMapper;
import com.job.dataVisualizationService.mapper.FlowMapper;
import com.job.dataVisualizationService.service.EquipmentService;
import com.job.dataVisualizationService.service.FlowService;
import org.springframework.stereotype.Service;

@Service
public class EquipmentServiceImpl extends ServiceImpl<EquipmentMapper, Equipment> implements EquipmentService {
}
