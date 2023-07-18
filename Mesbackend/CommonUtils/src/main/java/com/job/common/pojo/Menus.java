package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("t_menus")
@AllArgsConstructor
@NoArgsConstructor
public class Menus {
    @TableId("menu_id")
    private String MenuID;
    private String Name;
    private String url;
    private Integer Type;
    private Integer Sort;
    private String Remark;
    private String ParentId;
    private String permission;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date CreateTime;
    private String CreateUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date UpdateTime;
    private String UpdateUser;
    private Integer Is_delete;
}
