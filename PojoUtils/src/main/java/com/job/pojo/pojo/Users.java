package com.job.pojo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("t_users")
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
    private Date CreateTime;
    private String CreateUser;
    private Date UpdateTime;
    private String UpdateUser;
    private Integer State;
    private Date Birth;
    private Integer isDelete;
}
