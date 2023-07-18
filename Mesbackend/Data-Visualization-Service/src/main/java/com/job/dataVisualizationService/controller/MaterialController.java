package com.job.dataVisualizationService.controller;

import com.job.common.pojo.Order;
import com.job.common.result.Result;
import com.job.dataVisualizationService.pojo.MaterialData;
import com.job.dataVisualizationService.service.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/data/material")
@Slf4j
public class MaterialController {
    @Autowired
    private MaterialService materialService;
    @GetMapping("/classification")    //分类占比   金额 体积 面积
    public Result<MaterialData> getClassification(){
        MaterialData materialData = materialService.classification();
        log.info(materialData.toString());
        return Result.success(materialData,"success");
    }
}
