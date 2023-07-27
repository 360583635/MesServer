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
    /**
     * 仓库id
     */
    private int warehouseId;
    /**
     * 仓库类型
     */
    private Integer warehouseType;
    /**
     * 原材料名称
     */
    private String materialName;
    /**
     * 设备名称
     */
    private String equipmentName;
    /**
     *产品名称
     */
    private String produceName;
    /**
     * 数量
     */
    private Integer number;
    /**
     * 逻辑删除
     */
    private int isDelete;

}

