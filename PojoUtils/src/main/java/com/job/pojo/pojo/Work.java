package com.job.pojo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_work")
public class Work {
    private String wID;
    private String wState;
    private String wProcessId;
    private String wOrderID;
    private String wProdNums;
    private String wCreateTime;
    private String WErrorTime;
    private String wNeedTime;
    private String wEquipmentID;
}
