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

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_line")
public class Line {
    /**
     * 流水线ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 流水线名称
     */
    private String line;
    /**
     * 流水线描述
     */
    private String lineDesc;
    /**
     * 流水线所属流程Id
     */
    private String lineFlowId;
    /**
     * 状态
     * 0 待生产；1 停机；2 生产中；3 生产异常；4 生产完成
     */
    private String lineStatus;
    /**
     *订单数量
     */
    private Integer orderCount;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date createTime;


    //创建人
    private String createUsername;


    //修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date updateTime;

    //修改人
    private String updateUsername;


    //异常次数
    private Integer exceptionCount = 0;
    //完成次数
    private Integer successCount = 0;

    private Integer isDelete;
    @TableField(exist = false)
    private Integer totalExceptionCount;
    @TableField(exist = false)
    private Integer totalSuccessCount;
    @TableField(exist = false)
    private Integer totalLineStatus;
}
