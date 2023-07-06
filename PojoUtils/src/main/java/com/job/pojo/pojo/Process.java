package com.job.pojo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_process")
public class Process {
    private String id;
    private String process;
    private String processDesc;
    private Date CreateTime;
    private String CreateUsername;
    private Date UpdateTime;
    private String UpdateUsername;
}
