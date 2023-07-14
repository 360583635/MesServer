package com.job.dataVisualizationService.controller;

import com.job.common.pojo.Line;
import com.job.common.result.Result;
import com.job.dataVisualizationService.pojo.LineData;
import com.job.dataVisualizationService.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author 菜狗
 */
@RestController
@RequestMapping("/data/line")
public class LineController {
    @Autowired
    private LineService lineService;
    @GetMapping("/now")
    public Result<List<Line>> getNow(){
        List<Line> list = lineService.list();
        return Result.success(list,"success");
    }
}
