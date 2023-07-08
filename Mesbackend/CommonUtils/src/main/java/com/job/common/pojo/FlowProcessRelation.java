package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_flow_process_relation")
@AllArgsConstructor
@NoArgsConstructor
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
    private int sortNum;
    private String processType;

}
