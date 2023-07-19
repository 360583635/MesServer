package com.job.productionManagementService.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.Material;
import com.job.productionManagementService.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-17-18:45
 * @description
 */
@RestController
@RequestMapping("/productionManagement")
public class MaterialController {

    @Autowired
    private MaterialService materialService;
    @GetMapping("/material/queryMaterials")
    List<Material> queryMaterials() {
        return materialService.queryMaterials();
    }

    /**
     * 根据id查询原材料
     * @param id
     * @return
     */
    @GetMapping("/queryMaterialsById/{id}")
    List<Material> queryMaterialsById(@PathVariable("id") String id){
        LambdaQueryWrapper<Material> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Material::getMaterialId,id);
        return materialService.list(queryWrapper);
    }
}
