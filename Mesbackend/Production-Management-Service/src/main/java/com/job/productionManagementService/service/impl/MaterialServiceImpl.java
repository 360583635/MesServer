package com.job.productionManagementService.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.job.common.pojo.Material;
import com.job.productionManagementService.mapper.MaterialMapper;
import com.job.productionManagementService.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 猫
 * @create 2023-07-17-18:43
 * @description
 */
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {

    @Autowired
    private MaterialMapper materialMapper;

    @Override
    public List<Material> queryMaterials() {
        LambdaQueryWrapper<Material> queryWrapper = new LambdaQueryWrapper<>();
        List<Material> materials = materialMapper.selectList(queryWrapper);
        return materials;
    }
}
