package com.job.productionManagementService.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.job.common.pojo.Equipment;
import com.job.common.pojo.Inventory;
import com.job.common.result.Result;
import com.job.productionManagementService.service.*;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 猫
 * @create 2023-07-18-15:14
 * @description
 */
@Controller
@RequestMapping("/productionManagement/equipment")
@Component
public class EquipmentController {

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


    /**
     * 初始化设备入库
     * @param equipmentName
     * @param warehouseId
     * @return
     */
    @PostMapping("/initializationEquipment")
    public Result initializationEquipment(@RequestParam String equipmentName,int warehouseId){
        Inventory inventory=new Inventory();
        inventory.setProduceName(equipmentName);
        inventory.setWarehouseId(warehouseId);
        inventory.setWarehouseType(1);
        inventory.setIsDelete(1);
        inventory.setNumber(0);
        inventoryService.save(inventory);
        return null ;
    }
    /**
     * 查询所有设备功能类型
     * @return
     */
    @GetMapping("/queryEquipmentTypes")
    @ResponseBody
    List<String> queryEquipmentTypes(){
        LambdaQueryWrapper<Equipment> queryWrapper = new LambdaQueryWrapper<>();
        List<Equipment> list = equipmentService.list();
        List<String> functionNames = new ArrayList<>();
        Set<String> uniqueFunctionNames = new HashSet<>();
        for (Equipment equipment : list) {
            String functionName = equipment.getFunctionName();
            if (!uniqueFunctionNames.contains(functionName)) {
                functionNames.add(functionName);
                uniqueFunctionNames.add(functionName);
            }
        }
        return functionNames;
    }

    /**
     * 根据设备功能类型查询设备
     */
    @PostMapping("/queryEquipmentsByType")
    @ResponseBody
    List<Equipment> queryEquipmentsByType(@RequestParam String functionName){
        LambdaQueryWrapper<Equipment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Equipment::getFunctionName,functionName);
        List<Equipment> equipmentList = equipmentService.list(queryWrapper);
        return equipmentList;
    }


    /**
     * 根据id逻辑删除
     * @param equipmentId
     * @return
     */
    @PostMapping("/delete")
    @ResponseBody
    public Result removeById(@RequestParam long equipmentId){
        LambdaUpdateWrapper<Equipment> lambdaUpdateWrapper=new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.set(Equipment::getIsDelete,0);
        boolean update = equipmentService.update(lambdaUpdateWrapper);
        if(update){
            return Result.success(null,"成功删除");
        }else {
            return Result.error("删除失败");
        }
    }

    /**
     *
     * @param equipment
     * @return
     */
    @PostMapping("/save")
    @ResponseBody
    public Result saveEquipment(@RequestBody Equipment equipment){

        boolean save = equipmentService.save(equipment);
        if(save){
            return Result.success(null,"保存成功");
        }
        return Result.error("保存失败");
    }

    /**
     * 修改设备信息
     * @param equipment
     * @return
     */
    @PostMapping("/update")
    @ResponseBody
    public Result updateEquipment(@RequestBody Equipment equipment){

        boolean b = equipmentService.updateById(equipment);
        if(b){
            return Result.success(null,"保存成功");
        }
        return Result.error("保存失败");
    }






}
