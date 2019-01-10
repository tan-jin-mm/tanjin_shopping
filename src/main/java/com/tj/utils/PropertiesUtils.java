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
    static {

        InputStream inStream = PropertiesUtils.class.getResourceAsStream("/db.properties");
        try {
            prop.load(inStream);//需要传一个字节输入流
            imageHost = prop.getProperty("imageHost");
            ftpIp = prop.getProperty("ftp.server.ip");
            ftpUser = prop.getProperty("ftp.server.user");
            ftpPassword = prop.getProperty("ftp.server.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static final String IMAGE_HOST = imageHost;
    public static final String FTPIP = ftpIp;
    public static final String FTPUSER = ftpUser;
    public static final String FTPPASSWORD = ftpPassword;

    /*public static void main(String[] args) {
        System.out.println(IMAGE_HOST);
    }*/

}
