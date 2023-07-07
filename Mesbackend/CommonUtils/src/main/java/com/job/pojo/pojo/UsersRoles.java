package com.job.pojo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("t_users_roles")
@AllArgsConstructor
@NoArgsConstructor
public class UsersRoles {
    private String UserId;
    private String RoleId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date CreateTime;
    private String CreateUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date UpdateTime;
    private String UpdateUser;
    private Integer IsDelete;
}
