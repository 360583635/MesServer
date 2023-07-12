package com.job.dataVisualizationService.pojo;

import com.job.common.pojo.Order;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author 菜狗
 */
@Data
public class OrderData extends Order {

    private int[] preCount = new int[6];
    private int[] preAmount = new int[6];
    private int[] preTime = new int[6];

    //
    private int[] count = new int[6];
    private int[] amount = new int[6];
    private int[] time = new int[6];
    //几个月为间隔
    private Integer separate;
    //几个柱形图
    private Integer dataNumber;

    private Date startTime;
    private Date endTime;

    private String[] productType = new String[6];
    private int[] total    = new int[6];

}
