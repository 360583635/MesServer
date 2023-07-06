package com.job.dataVisualizationService.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_process")
public class Process {
    private String id;
    private String process;
    private String processDesc;
    private String CreateTime;
    private String CreateUsername;
    private String UpdateTime;
    private String UpdateUsername;
}
