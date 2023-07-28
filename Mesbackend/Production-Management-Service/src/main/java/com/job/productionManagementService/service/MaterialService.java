package com.job.productionManagementService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Material;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MaterialService extends IService<Material> {

    public List<Material> queryMaterials();

}
