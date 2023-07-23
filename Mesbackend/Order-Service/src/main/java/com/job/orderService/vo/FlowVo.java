package com.job.orderService.vo;

import com.job.common.pojo.Material;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FlowVo {
    private String title;
    private String value;
    private Map<String,Integer> material;
}
