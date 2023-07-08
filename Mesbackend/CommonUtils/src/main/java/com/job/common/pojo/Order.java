package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author 菜狗
 */
@Data
@TableName("t_order")
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private String orderId;
    private String customId;
    private Integer type;
    private String productId;
    private Integer orderNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date orderDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
    private Date expectDate;
    private String auditor;
    private Integer priority;
    private Integer productLine;
    private Integer status;
    private Integer orderPrice;
}
