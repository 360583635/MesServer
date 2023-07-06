package com.job.dataVisualizationService.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.bouncycastle.asn1.cms.TimeStampedData;

@Data
@TableName("t_messages")
public class Messages {
    private String MessageId;
    private String Title;
    private String Content;
    private String SendUser;
    private TimeStampedData CreateTime;
    private TimeStampedData UpdateTime;
    private Integer State;
    private Integer Level;
    private Integer IsDelete;

}
