package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_material")
@AllArgsConstructor
@NoArgsConstructor
public class Material {
    /**
     * 原材料id
     * */
    private int materialId;
    /**
     * 原材料名称
     */
    private String materialName;
    /**
     * 原材料规格
     */
    private String materialSpecification;
    /**
     * 供应商id
     */
    private String supplierId;
    /**
     *所存储的仓库id
     */
    private int warehouseId ;
    /**
     * 原材料单位
     */
    private String materialUnit;
    /**
     * 所占面积
     */
    private Float materialArea;
    /**
     *原材料所占体积
     */
    private String materialVolume;
    /**
     * 成本
     */
    private Double materialCost;
    /**
     * 逻辑删除
     */
    private int isDelete;


}
