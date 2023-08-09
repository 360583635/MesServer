package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
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
    @TableId("produce_id")
    private Integer produceId;
    /**
     * 产品名称
     */
    private String produceName ;

    private Integer producePrice;
    /**
     * 所占面积
     */
    private Integer produceArea;

    private Integer isDelete;


}

