package com.job.pojo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_roles")
public class Roles {
    private String RoleID;
    private String RoleName;
    private Date CreateTime;
    private String CreateUser;
    private Date UpdateTime;
    private String UpdateUser;
    private Integer IsDelete;
}
