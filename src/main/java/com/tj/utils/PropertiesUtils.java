package com.tj.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
    public static Properties prop = new Properties();
    static String imageHost = null;
    static String ftpIp = null;
    static String ftpUser = null;
    static String ftpPassword = null;
    static Integer hour = null;
    static Integer maxTotal =null;
    static Integer maxIdle = null;
    static Integer minIdle = null;
    static String redisIp = null;
    static Integer redisPort = null;
    static String redisPassword = null;
    static boolean testBorrow;
    static boolean testReturn;

    static {

        InputStream inStream = PropertiesUtils.class.getResourceAsStream("/db.properties");
        try {
            prop.load(inStream);//需要传一个字节输入流
            imageHost = prop.getProperty("imageHost");
            ftpIp = prop.getProperty("ftp.server.ip");
            ftpUser = prop.getProperty("ftp.server.user");
            ftpPassword = prop.getProperty("ftp.server.password");
            hour =Integer.parseInt(prop.getProperty("close.order.time"));
            maxTotal = Integer.parseInt(prop.getProperty("redis.max.total"));
            maxIdle = Integer.parseInt(prop.getProperty("redis.max.idle"));
            minIdle = Integer.parseInt(prop.getProperty("redis.min.idle"));
            redisIp = prop.getProperty("redis.ip");
            redisPort = Integer.parseInt(prop.getProperty("redis.port"));
            testBorrow = Boolean.parseBoolean(prop.getProperty("redis.test.borrow"));
            testReturn = Boolean.parseBoolean(prop.getProperty("redis.test.return"));
            redisPassword = prop.getProperty("redis.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static final String IMAGE_HOST = imageHost;
    public static final String FTPIP = ftpIp;
    public static final String FTPUSER = ftpUser;
    public static final String FTPPASSWORD = ftpPassword;
    public static final Integer HOUR = hour;
    public static final Integer MAXTOTAL = maxTotal;
    public static final Integer MAXIDLE = maxIdle;
    public static final Integer MINIDLE = minIdle;
    public static final String REDISIP = redisIp;
    public static final Integer REDISPORT = redisPort;
    public static final Boolean TESTBORROW = testBorrow;
    public static final Boolean TESTRETURN = testReturn;
    public static final String REDISPASSWORD = redisPassword;


}
