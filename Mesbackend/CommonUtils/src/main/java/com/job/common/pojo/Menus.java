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
    @TableId(value ="menu_id")
    private String menuID;
    private String name;
    private String url;
    private Integer type;
    private Integer sort;
    private String remark;
    private String parentId;
    private String permission;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;
    private String createUser;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date updateTime;
    private String updateUser;
    private Integer is_delete;
}
