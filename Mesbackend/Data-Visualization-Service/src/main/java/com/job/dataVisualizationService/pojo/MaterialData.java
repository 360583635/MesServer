package com.job.dataVisualizationService.pojo;

import com.job.common.pojo.Material;
import com.job.common.pojo.Warehouse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaterialData extends Material {
    //原材料id
    private int[] id ;
    //原材料名称
    private String[] name ;
    //占地面积
    private int[] area ;
    //占仓库体积
    private int[] volume ;
    //占货款
    private double[] total ;
    //产品数量
    private int[] number ;


}
