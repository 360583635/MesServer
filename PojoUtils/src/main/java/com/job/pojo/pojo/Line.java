package com.job.pojo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_line")
public class Line {
    private String id;
    private String line;
    private String lineDesc;
    private String lineFlow;
    private String status;
    private String orderCount;
}
