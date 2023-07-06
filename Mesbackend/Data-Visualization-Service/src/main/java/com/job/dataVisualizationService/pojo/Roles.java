package com.job.dataVisualizationService.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_roles")
public class Roles {
    private String RoleID;
    private String RoleName;
    private String CreateTime;
    private String CreateUser;
    private String UpdateTime;
    private String UpdateUser;
    private Integer IsDelete;
}
