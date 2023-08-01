package com.job.productionManagementService.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.job.common.pojo.Inventory;
import com.job.common.pojo.Material;
import com.job.productionManagementService.mapper.MaterialMapper;
import com.job.productionManagementService.service.EquipmentService;
import com.job.productionManagementService.service.InventoryService;
import com.job.productionManagementService.service.MaterialService;
import com.job.productionManagementService.service.ProduceService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
/**
 * @author 猫
 * @create 2023-07-18-15:14
 * @description
 */
@RestController
@RequestMapping("/productionManagement/inventory")
@Component
public class InventoryController {


    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private MaterialMapper materialMapper;

    @Autowired
    private EquipmentService equipmentService;

    @Resource
    private ProduceService produceService;

    /**
     * 根据原材料名称查询数量（模糊查询）
     * @param materialName
     *
     */
    @PostMapping("/queryMaterialNumberByMaterialName")
    List<Inventory> MaterialNumberByMaterialName(@RequestParam String materialName) {
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Inventory::getMaterialName,materialName);
        List<Inventory>inventoryList=inventoryService.list();
        return inventoryList;
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
     * 根据产品名称查询数量（模糊查询）
     * @param produceName
     * @return
     */
    @GetMapping("/queryProductNumberByName/{produceName}")
    List<Integer> queryProductNumberByName(@PathVariable("produceName") String produceName) {
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Inventory::getProduceName, produceName);
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
     * @return
     */
    @PostMapping ("/queryMaterialNameByWarehouseType")
    List<Inventory>queryMaterialNameByWarehouseType() {
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Inventory::getWarehouseType,0);
        List<Inventory> materiallist = inventoryService.list(queryWrapper);
        return materiallist;
    }
    /**
     * 根据库存类型查询设备库存信息
     * @return
     */
    @PostMapping ("/queryEquipmentNameByWarehouseType")
    List<Inventory>queryEquipmentByWarehouseType() {
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Inventory::getWarehouseType,1);
        List<Inventory> equipmentlist = inventoryService.list(queryWrapper);
        return equipmentlist;
    }

    /**
     * 根据库存类型查询成品库存信息

     * @return
     */
    @PostMapping ("/queryProductNameByWarehouseType/{warehouseType}")
    List<Inventory>queryProductNameByWarehouseType() {
        LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Inventory::getWarehouseType,2);
        List<Inventory> productlist = inventoryService.list(queryWrapper);
        return productlist;
    }

    /**
     * 根据原材料名称查询总个数
     */
    @GetMapping("/queryNumbersByMaterialName")
       Integer queryMaterialNumberByMaterialName(String materialName) {
        LambdaQueryWrapper<Inventory>queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Inventory::getMaterialName,materialName);
       List<Inventory>inventoryList = inventoryService.list(queryWrapper);
       int number =0 ;
       int size =inventoryList.size();
        for (int i=0 ;i<size;i++){
            number=number+inventoryList.get(i).getNumber();
        }
        return number;
    }
    /**
     * 查询所有原材料名称
     */
    @PostMapping("queryMaterialName")
    List<String> queryMaterialName(){
        QueryWrapper<Material>queryWrapper= Wrappers.query();
        queryWrapper.select("material_name");
        List<Material> materials = materialMapper.selectList(queryWrapper);

        List<String> nameList = new ArrayList<>();
        for (Material name : materials) {
            nameList.add(name.getMaterialName());
        }
        return nameList ;

    }

    /**
     * 根据原材料名称查询原材料名称无参数版
     * @return
     */
    @PostMapping("/queryMaterialNumbersByMaterialName")
    List<Integer> queryMaterialNumbersByMaterialName() {
        QueryWrapper<Material> materialQueryWrapper = Wrappers.query();
        materialQueryWrapper.select("material_name");
        List<Material> materials = materialMapper.selectList(materialQueryWrapper);
        List<Integer> numbers = new ArrayList<>();
        for (Material name : materials) {
            int number =0 ;
            LambdaQueryWrapper<Inventory> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Inventory::getMaterialName, name.getMaterialName());
            List<Inventory>materialList=inventoryService.list(queryWrapper);
            for (int i= 0 ;i<materialList.size();i++){
                int materialName=materialList.get(i).getNumber();
                number=number+materialName;
            }
            numbers.add(number);
        }
        return numbers;
    }

    }
