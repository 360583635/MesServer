package com.job.dataVisualizationService.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author 菜狗
 */
public class NowDM {

    public static int[] getNowDM() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String dateString = sdf.format(date);
        String dayString = dateString.substring(8,8+2);
        String mmString = dateString.substring(5,5+2);
        if(dateString.charAt(0) == '0')dayString = dateString.substring(1);
        int day = Integer.parseInt(dayString);
        if(mmString.charAt(0) == '0')mmString = mmString.substring(1);
        int mm = Integer.parseInt(mmString);
        return new int[]{day,mm};
    }
}
