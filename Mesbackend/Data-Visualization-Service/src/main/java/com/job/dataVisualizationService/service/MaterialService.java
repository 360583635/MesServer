package com.job.dataVisualizationService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Material;
import com.job.dataVisualizationService.pojo.MaterialData;

import java.util.Map;

public interface MaterialService extends IService<Material> {
    Map<Object,Object> classification();

    Map<Object,Object> getWarehouse();
}
