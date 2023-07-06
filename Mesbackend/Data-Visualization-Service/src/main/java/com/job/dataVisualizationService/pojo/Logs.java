package com.job.dataVisualizationService.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.bouncycastle.asn1.cms.TimeStampedData;

@Data
@TableName("t_logs")
public class Logs {
    private String LogId;
    private String UserId;
    private String Method;
    private String Args;
    private String Operation;
    //char
    private String Ip;
    //timestamp
    private TimeStampedData OperationTime;

    private Integer Result;
    private String Detail;


}
