package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@TableName("t_work")
@AllArgsConstructor
@NoArgsConstructor
public class Work {
    @TableId
    private String wId;
    private String wState;
    private String wProcessId;
    private String wOrderId;
    private Integer wProdNums;
    private String wCreateTime;
    private String WErrorTime;
    //完成的时间
    private Date successTime;
    private String wNeedTime;
    private String wEquipmentId;
}
