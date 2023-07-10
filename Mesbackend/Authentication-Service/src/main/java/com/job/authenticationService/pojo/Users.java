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

    private String name;
    private String salt;
    private String password;
    private String phone;
    private String email;
    private String address;
    private String enter_time;
    private String sex;
    private Date create_time;
    private String create_user;
    private Date update_time;
    private String update_user;
    private int state;
    private Date birth;
    private int Is_delete;
}

