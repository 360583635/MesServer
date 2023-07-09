package com.job.common.pojo;

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
    private String lineFlow;
    /**
     * 状态
     */
    private String status;
    /**
     *
     */
    private String orderCount;
}
