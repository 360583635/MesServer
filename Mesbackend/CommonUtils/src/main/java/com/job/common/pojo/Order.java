package com.job.common.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author 菜狗
 */
@Data
@TableName("t_order")
@AllArgsConstructor
@NoArgsConstructor
public class Order{
    @TableId(type = IdType.ASSIGN_ID)
    private String orderId;
    private String customName;
    private String typeName;
    private String productId;
    private Integer orderNumber;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date orderDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expectDate;
    private String auditor;
    private Integer priority;// 0 无优先级；1 中等优先级； 2 高优先级
    private String productLine;
    private Integer productionStatus;//0 未生产；1待生产；2 生产中；3 生产异常；4 生产完成
    private Integer orderPrice;
    private String rawName;
    private String rawNum;
    private String customTel;
    private Integer isDelete;// 0 未删除；1 删除
    private String productName;
    //统计订单总金额
    @TableField(exist = false)
    private Integer orderTotal;
    @TableField(exist = false)
    private Date startTime;
    @TableField(exist = false)
    private Date endTime;
}
