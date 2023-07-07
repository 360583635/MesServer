package com.job.dispatchService.LineManager.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_line")
public class TLine {
    private String id;
    private String line;
    private String lineDesc;
    private String lineFlow;
    private String status;
    private String orderCount;


}