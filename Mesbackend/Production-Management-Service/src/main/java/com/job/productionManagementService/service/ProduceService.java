package com.job.productionManagementService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Produce;

import java.util.List;

public interface ProduceService extends IService<Produce> {

    public List<Produce> queryMaterials();
}
