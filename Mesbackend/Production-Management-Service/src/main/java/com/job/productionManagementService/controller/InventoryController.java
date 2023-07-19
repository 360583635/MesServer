package com.job.productionManagementService.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.Inventory;
import com.job.dispatchService.lineManager.service.ProcessService;
import com.job.productionManagementService.service.EquipmentService;
import com.job.productionManagementService.service.InventoryService;
import com.job.productionManagementService.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Component
public class InventoryController {


    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private ProcessService processService;

    /**
     * 根据原材料名称查询数量
     */
    @GetMapping("/queryMaterialNumberByName/{materialName}")
    List<Inventory> queryMaterialNumberByName(@PathVariable("materialName") String materialName){
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Inventory::getMaterialName,materialName);
        List<Inventory> materiallist =inventoryService.list(queryWrapper);
        return materiallist;

    }
    @GetMapping("/queryEquipmentNumberByName/{equipmentName}")
    List<Inventory> queryEquipmentNumberByName(@PathVariable("equipmentName") String equipmentName){
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Inventory::getEquipmentName,equipmentName);
        List<Inventory> equipmentlist =inventoryService.list(queryWrapper);
        return equipmentlist;

    }
    @GetMapping("/queryProductNumberByName/{processName}")
    List<Integer> queryProductNumberByName(@PathVariable("processName") String processName) {
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Inventory::getProcessName, processName);
        List<Inventory> producelist = inventoryService.list(queryWrapper);
        List<Integer> numberList = new ArrayList<>();
        for (Inventory inventory : producelist) {
            Integer number = inventory.getNumber();
            numberList.add(number);
        }
        return numberList;
    }

    /**
     * 根据库存类型查询
     * @param warehouseType
     * @return
     */
    @GetMapping("/queryMaterialNameByWarehouseType/{warehouseType}")
    List<Inventory>queryMaterialNameByWarehouseType(@PathVariable("warehouseType") String warehouseType) {
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Inventory::getWarehouseType, warehouseType);
        List<Inventory> materiallist = inventoryService.list(queryWrapper);
        return materiallist;
    }

    }
