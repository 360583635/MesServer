package com.job.dataVisualizationService.controller;

import com.alibaba.fastjson.JSON;
import com.job.common.pojo.Order;
import com.job.common.result.Result;
import com.job.dataVisualizationService.pojo.MaterialData;
import com.job.dataVisualizationService.service.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/data/material")
@Slf4j
public class MaterialController {
    @Autowired
    private MaterialService materialService;
    @GetMapping("/classification")    //分类占比   金额 体积 面积
    @ResponseBody
    public Result<Object> getClassification(){
        Map<Object,Object> map = materialService.classification();
        return Result.success(map,"success");
    }
    @GetMapping("/warehouse")    //分类占比   金额 体积 面积
    public Result<Object> getWarehouse(){
        Map<Object,Object> map = materialService.getWarehouse();
        return Result.success(map,"success");
    }
}
