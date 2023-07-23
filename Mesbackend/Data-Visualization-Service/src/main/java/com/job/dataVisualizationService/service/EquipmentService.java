package com.job.dataVisualizationService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Equipment;

import java.util.Map;

public interface EquipmentService extends IService<Equipment> {
    Map<Object, Object> getEqument();
}
