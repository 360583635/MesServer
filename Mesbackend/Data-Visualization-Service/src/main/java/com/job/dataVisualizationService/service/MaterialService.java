package com.job.dataVisualizationService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Material;
import com.job.dataVisualizationService.pojo.MaterialData;

public interface MaterialService extends IService<Material> {
    MaterialData classification();
}
