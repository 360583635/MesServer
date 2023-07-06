package com.job.authenticationService.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "t_users")
public class Users implements Serializable {
    private static final long serialVersionUID = -40356785423868312L;

    @TableId
    private Long Id;

    private String Name;
    private String Salt;
    private String Password;
    private String Phone;
    private String Email;
    private String Address;
    private String Enter_time;
    private String Sex;
    private Date Create_time;
    private String Create_user;
    private Date Update_time;
    private String Update_user;
    private int State;
    private Date Birth;
    private int Is_delete;
}

