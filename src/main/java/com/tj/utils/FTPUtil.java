package com.tj.utils;

import com.tj.common.Const;
import org.apache.commons.net.ftp.FTPClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

public class FTPUtil {

    private String ftpIp;
    private String ftpUser;
    private String ftpPassword;
    private Integer port;

    public FTPUtil(String ftpIp, String ftpUser, String ftpPassword, int port) {
        this.ftpIp = ftpIp;
        this.ftpUser = ftpUser;
        this.ftpPassword = ftpPassword;
        this.port = port;
    }

    /*
    *图片上传到FTP
    * */
    public static boolean onloadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil(PropertiesUtils.FTPIP, PropertiesUtils.FTPUSER, PropertiesUtils.FTPPASSWORD, 21);
        System.out.println(PropertiesUtils.FTPIP+PropertiesUtils.FTPUSER+PropertiesUtils.FTPPASSWORD);
        System.out.println("开始连接FTP服务器....");
        ftpUtil.onloadFile("img",fileList);
        return false;
    }
    private  boolean onloadFile(String remotePath,List<File> fileList) throws IOException {
        FileInputStream fileInputStream = null;
        //连接ftp服务器
        if(connectFTPServer(ftpIp,ftpUser,ftpPassword)){
            System.out.println("正在上传图片。。。");
            try {
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();//打开被动传输模式
                for(File file:fileList){
                    fileInputStream = new FileInputStream(file);
                    ftpClient.storeFile(file.getName(),fileInputStream);
                }
                System.out.println("=====文件上传成功========");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("文件上传出错。。。");
            }finally {
                fileInputStream.close();
                ftpClient.disconnect();
            }
        }
        return false;
    }
    //连接ftp服务器
    FTPClient ftpClient = null;
    private boolean connectFTPServer(String ftpIp,String ftpUser,String ftpPassword){
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ftpIp);
            System.out.println("正在连接服务器");
            return ftpClient.login(ftpUser, ftpPassword);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("连接ftp服务器异常");
        }
        return false;
    }

    public String getFtpIp() {
        return ftpIp;
    }

    public void setFtpIp(String ftpIp) {
        this.ftpIp = ftpIp;
    }

    public String getFtpUser() {
        return ftpUser;
    }

    public void setFtpUser(String ftpUser) {
        this.ftpUser = ftpUser;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public void setFtpPassword(String ftpPassword) {
        this.ftpPassword = ftpPassword;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
