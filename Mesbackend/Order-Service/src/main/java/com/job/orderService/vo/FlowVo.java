package com.job.orderService.vo;

import com.job.common.pojo.Material;
import lombok.Data;

import java.util.List;
@Data
public class FlowVo {
    private String title;
    private String value;
    private List<String>  material;
}
