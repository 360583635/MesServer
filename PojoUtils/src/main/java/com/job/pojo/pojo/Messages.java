package com.job.pojo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_messages")
public class Messages {
    private String MessageId;
    private String Title;
    private String Content;
    private String SendUser;
    private Date CreateTime;
    private Date UpdateTime;
    private Integer State;
    private Integer Level;
    private Integer IsDelete;

}