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
    private int materialId;
     /**
      * 原材料id
      * */
    private String materialName;
    /**
     * 原材料名称
     */
    private String materialSpecification;
    /**
     * 原材料规格
     */
    private String supplierId;
    /**
     * 供应商id
     */
    private int warehouseId ;
    /**
     *所存储的仓库id
     */
    private String materialUnit;
    /**
     * 原材料单位
     */
    private String materialArea;
    /**
     * * 100个所占面积
     */
     private String materialVolume;
    /**
     * 100个原材料所占体积
     */

    private Double materialCost;
    /**
     * 成本
     */

//    private String weight;


}
