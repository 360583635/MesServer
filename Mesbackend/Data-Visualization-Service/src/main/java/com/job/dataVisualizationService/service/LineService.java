package com.job.dataVisualizationService.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.job.common.pojo.Line;

import java.util.Map;

/**
 * @Auther:Liang
 */
public interface LineService extends IService<Line> {
    Map<Object,Object> getall();

    Map<Object,Object> getone(Line line);

}
