package com.job.dataVisualizationService.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_messages")
public class Messages {
    private String MessageId;
    private String Title;
    private String Content;
    private String SendUser;
    private String CreateTime;
    private String UpdateTime;
    private Integer State;
    private Integer Level;
    private Integer IsDelete;

}
