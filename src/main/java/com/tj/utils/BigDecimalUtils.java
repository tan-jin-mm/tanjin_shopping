package com.tj.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {

    //加
    public static BigDecimal add(double d1,double d2){
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(d2));
        return bigDecimal.add(bigDecimal1);
    }
    //减
    public static BigDecimal sub(double d1,double d2){
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(d2));
        return bigDecimal.subtract(bigDecimal1);
    }
    //乘
    public static BigDecimal mul(double d1,double d2){
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(d2));
        return bigDecimal.multiply(bigDecimal1);
    }
    //除，保留两位小数，四舍五入
    public static BigDecimal div(double d1,double d2){
        BigDecimal bigDecimal = new BigDecimal(String.valueOf(d1));
        BigDecimal bigDecimal1 = new BigDecimal(String.valueOf(d2));
        return bigDecimal.divide(bigDecimal1,2,BigDecimal.ROUND_HALF_UP);
    }
}
