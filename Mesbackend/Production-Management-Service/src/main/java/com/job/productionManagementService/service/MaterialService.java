package com.job.productionManagementService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Material;

import java.util.List;

public interface MaterialService extends IService<Material> {

    public List<Material> queryMaterials();
}
