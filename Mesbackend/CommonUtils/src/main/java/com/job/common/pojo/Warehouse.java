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
    private int warehouseId;
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
    private int warehouseType;
    /**
     * 仓库负责人
     */
    private String warehouseManager;
    /**
     * 是否配备温度监控设施（0为未装配，1为已装配）
     */
    private int temperatureControl;
    /**
     * 是否配备湿度监控设施（0为未装配，1为已装配）
     */
    private int humidityControl;
    /**
     * 是否配备防火措施
     */
     private int fireproofControl;
    /**
     * 装配了哪些安全设施
     */
    private String securityFacilities;
    /**
     * 是否装配自动化设备
     */
    private  int automationEquipment;
    /**
     * 仓库总面积
     */
    private float warehouseArea;
    /**
     * 可用面积
     */
    private float warehouseAvailable;
    /**
     *仓库层数
     */
    private int warehouseLayers;
    /**
     * 仓库总容量
     */
    private  int warehouseCapacity;
    /**
     * 逻辑删除
     */
    private  int isDelete;

    private int warehouseSave;


}
