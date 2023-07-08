package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_work")
@AllArgsConstructor
@NoArgsConstructor
public class Work {
    private String wID;
    private String wState;
    private String wProcessId;
    private String wOrderID;
    private Integer wProdNums;
    private String wCreateTime;
    private String WErrorTime;

    private String wNeedTime;
    private String wEquipmentID;
}
