package dev.semkoksharov.vibeshub2.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static String getFormattedTimestamp() {
        return dateFormat.format(new Date());
    }
}
