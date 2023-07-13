package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_users")
@AllArgsConstructor
@NoArgsConstructor
public class Users implements Serializable {
    private String Id;
    private String Password;
    private String Salt;
    private String Name;
    private String Phone;
    private String Email;
    private String Address;
    private Date EnterTime;
    private String Sex;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date CreateTime;
    private String CreateUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date UpdateTime;
    private String UpdateUser;
    private Integer State;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date Birth;
    private Integer isDelete;
    private Integer isBlack;
}
