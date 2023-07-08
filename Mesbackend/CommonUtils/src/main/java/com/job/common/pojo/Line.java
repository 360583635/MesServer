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
    private String id;
    private String line;
    private String lineDesc;
    private String lineFlow;
    private String status;
    private String orderCount;
}
