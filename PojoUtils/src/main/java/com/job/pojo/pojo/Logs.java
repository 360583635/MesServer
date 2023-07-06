package com.job.pojo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_logs")
public class Logs {
    private String LogId;
    private String UserId;
    private String Method;
    private String Args;
    private String Operation;
    private String Ip;
    private Date OperationTime;
    private Integer Result;
    private String Detail;


}
