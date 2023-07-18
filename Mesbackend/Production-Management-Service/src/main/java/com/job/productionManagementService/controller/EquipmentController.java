package com.job.productionManagementService.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.job.common.pojo.Equipment;
import com.job.productionManagementService.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 庸俗可耐
 * @create 2023-07-18-15:14
 * @description
 */
@Controller
@RequestMapping("/productionManagement/equipment")
@CrossOrigin
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    /**
     * 查询所有设备功能类型
     * @return
     */
    @GetMapping("/queryEquipmentTypes")
    List<String> queryEquipmentTypes(){
        LambdaQueryWrapper<Equipment> queryWrapper = new LambdaQueryWrapper<>();
        List<Equipment> list = equipmentService.list();
        List<String> functionNames = new ArrayList<>();
        for(Equipment equipment:list){
            String functionName = equipment.getFunctionName();
            functionNames.add(functionName);
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
}
