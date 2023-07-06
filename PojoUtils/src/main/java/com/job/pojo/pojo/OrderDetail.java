package com.job.pojo.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_order_detail")
public class OrderDetail {
    private String detailId;
    private String orderId;
    private Integer type;
    private String productId;
    private String rawName;
    private Integer rawNum;
}
