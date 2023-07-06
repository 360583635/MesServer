package com.job.dataVisualizationService.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.util.Date;

@Data
@TableName("t_flow")
public class Flow {
    private String Id;
    private String flow;
    private String flowDesc;
    private String process;
    private Date createTime;
    private String createUsername;
    private Date updateTime;
    private String updateUsername;
}
