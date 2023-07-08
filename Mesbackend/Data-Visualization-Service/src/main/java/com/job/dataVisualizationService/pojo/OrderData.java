package com.job.dataVisualizationService.pojo;

import com.job.pojo.pojo.Order;
import lombok.Data;

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

}
