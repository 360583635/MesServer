package com.job.dispatchService.Work.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringAndNumberUtil {
    public static boolean StingAndNumberTest(String s){
        Pattern pattern = Pattern.compile("[0-9a-zA-Z]{1,}");
        Matcher matcher = pattern.matcher(s);
        return matcher.matches();
    }

    public static boolean StingAndNumberTest(String... s){
        Pattern pattern = Pattern.compile("[0-9a-zA-Z]{1,}");
        boolean flag = true;
        Matcher matcher;
        for (int i = 0; i < s.length; i++) {
            matcher = pattern.matcher(s[i]);
            flag = matcher.matches();
            if(!flag){
                return false;
            }
        }
        return true;
    }
}
