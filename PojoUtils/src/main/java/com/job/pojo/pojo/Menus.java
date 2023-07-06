package com.job.pojo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_menus")
public class Menus {
    private String MenuID;
    private String Name;
    private String url;
    private Integer Type;
    private Integer Sort;
    private String Remark;
    private String ParentId;
    private String permission;
    private Date CreateTime;
    private String CreateUser;
    private Date UpdateTime;
    private String UpdateUser;
    private Integer Is_delete;
}
