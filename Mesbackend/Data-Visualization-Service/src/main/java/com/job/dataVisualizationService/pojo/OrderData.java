package com.job.dataVisualizationService.pojo;

import com.job.common.pojo.Order;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * @Author 菜狗
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderData extends Order {

    private int[] preCount;
    private int[] preAmount;
    private int[] preTime;

    //
    private int[] count;
    private int[] amount;
    private int[] time;
    //几个月为间隔
    private Integer separate;
    //几个柱形图
    private Integer dataNumber;

    private Date startTime;
    private Date endTime;

    private String[] productType;
    private int[] total;
}
