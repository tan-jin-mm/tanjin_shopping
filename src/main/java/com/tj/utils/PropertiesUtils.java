package com.tj.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {
    public static Properties prop = new Properties();
    static String imageHost = null;
    static {

        InputStream inStream = PropertiesUtils.class.getResourceAsStream("/db.properties");
        try {
            prop.load(inStream);//需要传一个字节输入流
            imageHost = prop.getProperty("imageHost");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static final String IMAGE_HOST = imageHost;

    /*public static void main(String[] args) {
        System.out.println(IMAGE_HOST);
    }*/

}
