package com.job.dataVisualizationService.controller;

import com.job.common.result.Result;
import com.job.dataVisualizationService.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/data/equipment")
public class EquipmentController {
    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/equipment")
    public Result<Object> getEquipment(){

        Map<Object,Object> map = equipmentService.getEqument();

        return  Result.success(map,"success");
    }

}
