package com.job.dataVisualizationService.controller;

import com.job.common.result.Result;
import com.job.dataVisualizationService.mapper.EquipmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("data/equipment")
public class EquipmentController {
    @Autowired
    private EquipmentMapper equipmentMapper;

    @GetMapping("")
    public Result<Object> getEquipment(){
        return null;
    }

}
