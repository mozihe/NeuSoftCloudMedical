package com.moxin.java.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class GeneralRecordNumberUtil {
    public static String generate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String timestamp = sdf.format(new Date());
        int randomNum = new Random().nextInt(9000000) + 1000000;
        return timestamp + randomNum;
    }
}
