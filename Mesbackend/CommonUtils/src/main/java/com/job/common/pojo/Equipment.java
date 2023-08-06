package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import   com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_equipment")
@AllArgsConstructor
@NoArgsConstructor
public class Equipment {
    @TableId("equipment_id")
    private Integer equipmentId;
    /**
     * 设备id
     */
    private String equipmentName;
    /**
     * 设备名称
     */
    private String functionName;
    /**
     * 设备功能
     */
    private  Integer equipmentState;
    /**
     * 设备状态
     */
    private  String productionCapacity;
    /**
     * 单位时间内可处理的数量
     */
    private  String  maximumLoad;
    /**
     * 工序Id
     */
    private String processId;

    //逻辑删除
    private int isDelete;

    //缺少该字段 修改人：梁晋豪
    private int OperatingSpeed;
    /**
     * 设备状态数
     */
    @TableField(exist = false)
    private Integer count;
    /**
     * 设备所占面积
     */
    private Float equipmentArea;
}
