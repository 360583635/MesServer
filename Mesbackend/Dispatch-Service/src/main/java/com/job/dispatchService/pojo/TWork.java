package com.job.dispatchService.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;

@TableName
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TWork {
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
