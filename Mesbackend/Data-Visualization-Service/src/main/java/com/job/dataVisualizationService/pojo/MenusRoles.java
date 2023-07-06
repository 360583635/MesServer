package com.job.dataVisualizationService.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_menus_roles")
public class MenusRoles {
    private String MenuId;
    private String RoleId;
    private String CreateTime;
    private String CreateUser;
    private String UpdateTime;
    private String UpdateUser;
    private Integer IsDelete;
}
