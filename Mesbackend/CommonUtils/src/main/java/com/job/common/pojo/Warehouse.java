package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_warehouse")
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {
    /**
     * 仓库id
     */
    @TableId("warehouse_id")
    private Integer warehouseId;
    /**
     *仓库地址
     */
    private String warehouseAddress;
    /**
     * 仓库名称
     */
    private String warehouseName;
    /**
     * 仓库类型
     */
    private Integer warehouseType;
    /**
     * 仓库负责人
     */
    private String warehouseManager;
    /**
     * 是否配备温度监控设施（0为未装配，1为已装配）
     */
    private Integer temperatureControl;
    /**
     * 是否配备湿度监控设施（0为未装配，1为已装配）
     */
    private Integer humidityControl;
    /**
     * 是否配备防火措施
     */
     private Integer fireproofControl;
    /**
     * 装配了哪些安全设施
     */
    private String securityFacilities;
    /**
     * 是否装配自动化设备
     */
    private  Integer automationEquipment;
    /**
     * 仓库总面积
     */
    private Float warehouseArea;
    /**
     * 可用面积
     */
    private Float warehouseAvailable;
    /**
     *仓库层数
     */
    private Integer warehouseLayers;
    /**
     * 仓库总容量
     */
    private  Integer warehouseCapacity;
    /**
     * 逻辑删除
     */
    private  Integer isDelete;
    /**
     *仓库级别
     */
    private Integer warehouseSave;


}
