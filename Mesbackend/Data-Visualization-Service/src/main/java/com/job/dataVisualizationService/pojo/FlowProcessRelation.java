package com.job.dataVisualizationService.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_flow_process_relation")
public class FlowProcessRelation {
    private String Id;
    private String flowId;
    private String Flow;
    private String perOper;
    private String perOperId;
    private String oper;
    private String operId;
    private String nextOper;
    private String nextOperId;
    private String sortNum;
    private String operType;

}
