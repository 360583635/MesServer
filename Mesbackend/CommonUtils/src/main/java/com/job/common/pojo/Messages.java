package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("t_messages")
@AllArgsConstructor
@NoArgsConstructor
public class Messages {
    private String MessageId;
    private String Title;
    private String Content;
    private String SendUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date CreateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date UpdateTime;
    private Integer State;
    private Integer Level;
    private Integer IsDelete;

}
