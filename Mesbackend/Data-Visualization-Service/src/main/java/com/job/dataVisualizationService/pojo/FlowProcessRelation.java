package com.job.dataVisualizationService.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_flow_process_relation")
public class FlowProcessRelation {
    private String Id;
    private String flowId;
    private String Flow;
    private String perProcess;
    private String perProcessId;
    private String process;
    private String processId;
    private String nextProcess;
    private String nextProcessId;
    private String sortNum;
    private String processType;

}
