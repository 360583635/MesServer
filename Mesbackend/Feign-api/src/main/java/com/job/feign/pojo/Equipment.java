package com.job.feign.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_equipment")
@AllArgsConstructor
@NoArgsConstructor
public class Equipment {

    private int equipmentID;
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
    private int equipmentCode;
    /**
     *设备唯一标识码
     */
    private  String equipmentStatus;
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
}
