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
/**
 * @author 猫
 * @create 2023-07-18-15:14
 * @description
 */
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
     * 根据原材料名称查询数量（模糊查询）
     * @param materialName
     *
     */
    @GetMapping("/queryMaterialNumberByName/{materialName}")
    List<Integer> queryMaterialNumberByName(@PathVariable("materialName") String materialName){
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Inventory::getMaterialName,materialName);
        List<Inventory> materiallist =inventoryService.list(queryWrapper);
        List<Integer> materialNumberlist = new ArrayList<>();
        for (Inventory inventory : materiallist){
           Integer materialNumber =inventory.getNumber();
           materialNumberlist.add(materialNumber);
        }
        return materialNumberlist;

    }

    /**
     * 根据设备名称查询数量(模糊查询)
     * @param equipmentName
     *
     */
    @GetMapping("/queryEquipmentNumberByName/{equipmentName}")
    List<Integer> queryEquipmentNumberByName(@PathVariable("equipmentName") String equipmentName){
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Inventory::getEquipmentName,equipmentName);
        List<Inventory> equipmentlist =inventoryService.list(queryWrapper);
        List<Integer>  equipmentNumberlist = new ArrayList<>();
        for (Inventory inventory : equipmentlist){
            Integer equipmentNumber = inventory.getNumber();
            equipmentNumberlist.add(equipmentNumber);
        }
        return equipmentNumberlist;

    }

    /**
     * 根据产品名称插叙数量（模糊查询）
     * @param produceName
     * @return
     */
    @GetMapping("/queryProductNumberByName/{produceName}")
    List<Integer> queryProductNumberByName(@PathVariable("produceName") String produceName) {
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Inventory::getProcessName, produceName);
        List<Inventory> producelist = inventoryService.list(queryWrapper);
        List<Integer> produceNumberList = new ArrayList<>();
        for (Inventory inventory : producelist) {
            Integer produceNumber = inventory.getNumber();
            produceNumberList.add(produceNumber);
        }
        return produceNumberList;
    }

    /**
     * 根据库存类型查询原材料库存信息
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
    /**
     * 根据库存类型查询设备库存信息
     * @param warehouseType
     * @return
     */
    @GetMapping("/queryEquipmentNameByWarehouseType/{warehouseType}")
    List<Inventory>queryEquipmentByWarehouseType(@PathVariable("warehouseType") String warehouseType) {
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Inventory::getWarehouseType, warehouseType);
        List<Inventory> equipmentlist = inventoryService.list(queryWrapper);
        return equipmentlist;
    }

    /**
     * 根据库存类型查询成品库存信息
     * @param warehouseType
     * @return
     */
    @GetMapping("/queryProductNameByWarehouseType/{warehouseType}")
    List<Inventory>queryProductNameByWarehouseType(@PathVariable("warehouseType") String warehouseType) {
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Inventory::getWarehouseType, warehouseType);
        List<Inventory> productlist = inventoryService.list(queryWrapper);
        return productlist;
    }

    }
