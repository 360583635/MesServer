package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author 庸俗可耐
 * @create 2023-07-16-19:49
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_process_material_relation")
public class ProcessMaterialRelation {

    /**
     * id
     */
    private String id;

    /**
     * 工序Id
     */
    private String processId;

    /**
     * 工序名称
     */
    private String processName;

    /**
     * 原材料Id
     */
    private String materialId;

    /**
     * 原材料名称
     */
    private String materialName;

    /**
     * 原材料数量
     */
    private String number;

    /**
     * 逻辑删除
     */
    private Integer isDelete;
}