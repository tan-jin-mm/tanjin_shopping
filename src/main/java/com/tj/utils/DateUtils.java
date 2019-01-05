package com.tj.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtils {
    //定义一个默认时间格式
    private static final String STANDARD_FORMAT="yyyy-MM-dd HH:mm:ss";
    /**
    * Date-->String
    * */
    public static String dateToStr(Date date){
        //用到时间转换的工具包joda-time
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT);
    }
    /**
     * string-->Date
    * */
    public static Date strToDate(String str){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }

    /*public static void main(String[] args) {
        System.out.println(dateToStr(new Date()));
        System.out.println(strToDate("2019-01-02 11:16:18"));
    }*/
}
