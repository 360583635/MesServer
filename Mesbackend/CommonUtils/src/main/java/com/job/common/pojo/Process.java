package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_process")
public class Process {
    /**
     * 工序ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 工序
     */
    private String process;

    /**
     * 工序描述
     */
    private String processDesc;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;

    /**
     * 创建人
     */
    private String createUsername;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date updateTime;

    /**
     * 修改人
     */
    private String updateUsername;

    //完成工序数量
    private Integer successCount;

    //异常次数
    private Integer exceptionCount;

    private Integer isDelete;

    private String equipmentId;

    @TableField(exist = false)
    private Equipment equipment;
}
