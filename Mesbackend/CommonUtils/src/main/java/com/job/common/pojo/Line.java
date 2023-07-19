package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_line")
@AllArgsConstructor
@NoArgsConstructor
public class Line {
    /**
     * 流水线ID
     */
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
     * 流水线所属流程
     */
    @TableField("line_flow")
    private String lineFlowId;
    /**
     * 状态
     * 0 待生产；1 停机；2 生产中；3 生产异常；4 生产完成
     */
    private String lineStatus;
    /**
     *
     */
    private Integer orderCount;

    //异常次数
    private Integer exceptionCount;
    //完成次数
    private Integer successCount;

    private Integer isDelete;
}
