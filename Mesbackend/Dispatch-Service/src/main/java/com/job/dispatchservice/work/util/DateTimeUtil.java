package com.job.dispatchservice.work.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateTimeUtil{
    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss";

    public static boolean isValid(String dateStr) {
        final DateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

}
