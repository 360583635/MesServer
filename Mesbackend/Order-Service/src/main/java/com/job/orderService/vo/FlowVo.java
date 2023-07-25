package com.job.orderService.vo;

import com.job.common.pojo.Material;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class FlowVo {
    private String text;
    private String value;
    //private Map<String,Integer> material;
    private List<Map<String,String>> material;
}
