package com.job.dataVisualizationService.pojo;

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
    private String CreateTime;
    private String CreateUser;
    private String UpdateTime;
    private String UpdateUser;
    private Integer State;
    private Date Birth;
    private Integer isDelete;
}
