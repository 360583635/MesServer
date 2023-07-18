package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_inventory")
public class Inventory {
    private String warehouseId;
    private Integer warehouseType;
    private String materialName;
    private String equipmentName;
    private String flow;
    private Integer number;
}
