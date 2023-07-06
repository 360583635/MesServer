package com.job.pojo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_users_roles")
public class UsersRoles {
    private String UserId;
    private String RoleId;
    private Date CreateTime;
    private String CreateUser;
    private Date UpdateTime;
    private String UpdateUser;
    private Integer IsDelete;
}
