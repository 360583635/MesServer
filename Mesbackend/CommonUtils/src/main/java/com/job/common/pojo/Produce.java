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
    private int produceId;
    /**
     * 产品名称
     */
    private String produceName ;

    private int producePrice;
    /**
     * 所占面积
     */
    private int produceArea;

    private int isDelete;


}

