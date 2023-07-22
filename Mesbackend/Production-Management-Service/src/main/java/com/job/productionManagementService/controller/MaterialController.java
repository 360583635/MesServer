package com.job.productionManagementService.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.job.common.pojo.Material;
import com.job.common.result.Result;
import com.job.productionManagementService.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 根据id逻辑删除
     * @param materialId
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public Result removeById(@RequestParam long materialId){
        LambdaUpdateWrapper<Material> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(Material::getMaterialId,materialId);
        lambdaUpdateWrapper.set(Material::getIsDelete,0);
        boolean update = materialService.update(lambdaUpdateWrapper);
        if(update){
            return Result.success(null,"成功删除");
        }else {
            return Result.error("删除失败");
        }
    }

    /**
     * 保存材料
     * @param material
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public Result saveMaterial(@RequestBody Material material){
        boolean save = materialService.save(material);
        if(save){
            return Result.success(null,"保存成功");
        }
        return Result.error("保存失败");
    }
    @PostMapping("/update")
    @ResponseBody
    public Result updateMaterial(@RequestBody Material material){

        boolean b = materialService.updateById(material);
        if(b){
            return Result.success(null,"保存成功");
        }
        return Result.error("保存失败");
    }






}
