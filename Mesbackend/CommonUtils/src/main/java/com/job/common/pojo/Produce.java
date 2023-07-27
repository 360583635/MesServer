package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_produce")
@AllArgsConstructor
@NoArgsConstructor
public class Produce {
    /**
     * 产品id
     */
    private String produceId;
    /**
     * 产品名称
     */
    private String ProduceName ;
    /**
     * 所属仓库名称
     */
    private String warehouseId;
    /**
     * 产品单价
     */
    private int producePrice;
    /**
     * 所占面积
     */
    private int produceArea;
    /**
     * 产品高度
     */
    private int produceHeight;
    /**
     * 逻辑删除
     */
    private int is_delete;

}
