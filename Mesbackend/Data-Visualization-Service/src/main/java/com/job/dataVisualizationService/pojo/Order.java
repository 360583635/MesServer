package com.job.dataVisualizationService.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author 菜狗
 */
@Data
@TableName("t_order")
public class Order implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private String orderId;
    private String customId;
    private Integer type;
    private String productId;
    private Integer orderNumber;
    private Date orderDate;
    private Date expectDate;
    private String auditor;
    private Integer priority;
    private Integer productLine;
    private Integer status;
    private Float orderPrice;


}
