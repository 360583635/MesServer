package com.job.dispatchService.Work.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@TableName("t_work")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Work {
    @TableField("w_id")
    private String wId;

    @TableField("w_state")
    private String wState;

    @TableField("w_process_id")
    private String wProcessId;

    @TableField("w_order_id")
    private String wOrderId;

    @TableField("w_prod_nums")
    private Integer wProdNums;

    @TableField("w_create_time")
    private String wCreateTime;

    @TableField("w_error_time")
    private String wErrorTime;

    @TableField("w_need_time")
    private String wNeedTime;

    @TableField("w_equipment_id")
    private String wEquipmentId;
}
