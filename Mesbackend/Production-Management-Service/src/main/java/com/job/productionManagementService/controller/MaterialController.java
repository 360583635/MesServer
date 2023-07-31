package com.job.productionManagementService.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.job.common.pojo.Inventory;
import com.job.common.pojo.Material;
import com.job.common.result.Result;
import com.job.productionManagementService.service.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 猫
 * @create 2023-07-17-18:45
 * @description
 */
@RestController
@RequestMapping("/material")
@Component
public class MaterialController {

    @Resource
    private WarehouseService warehouseService;

    @Resource
    private InventoryService inventoryService;
    @Resource
    private MaterialService materialService;
    @Resource
    private EquipmentService equipmentService;
    @Resource
    private ProduceService produceService;

    @GetMapping("/queryMaterials")
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
     * 通过名称查询原材料信息
     * @param
     * @return
     */
    @PostMapping ("/queryMaterialsByName")
    List<Material> queryMaterialsByName(@RequestParam String materialName){
        LambdaQueryWrapper<Material> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Material::getMaterialId,materialName);
        return materialService.list(queryWrapper);
    }

    /**
     * 逻辑删除
     * @param materialName
     * @return
     */
    @PostMapping("/queryMaterialByName")
    public Result queryMaterialByName(@RequestParam String materialName){
        LambdaQueryWrapper<Material> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper
                .eq(Material::getIsDelete,1)
                .eq(Material::getMaterialName,materialName);
        Material material = materialService.getOne(lambdaQueryWrapper);
        if(material!=null){
            return Result.success(material,"查询成功");
        }
        return Result.error("查询失败");
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
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public Result saveMaterial(@RequestBody Material tmaterial){
        boolean save = materialService.save(tmaterial);
        if(save){
            return Result.success(null,"保存成功");
        }
        return Result.error("保存失败");
    }

    /**
     * 初始化原材料
     * @return
     */
    @PostMapping("/initializationMaterial")
    public Result initializationMaterial(@RequestParam String materialName,int warehouseId){
        Inventory inventory=new Inventory();
        inventory.setMaterialName(materialName);
        inventory.setWarehouseId(warehouseId);
        inventory.setWarehouseType(0);
        inventory.setIsDelete(1);
        inventory.setNumber(0);
        inventoryService.save(inventory);
        return null ;
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
