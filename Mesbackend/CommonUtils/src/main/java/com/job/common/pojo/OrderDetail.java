package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("t_order_detail")
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    private String detailId;
    private String orderId;
    private Integer type;
    private String productId;
    private String rawName;
    private Integer rawNum;
}
