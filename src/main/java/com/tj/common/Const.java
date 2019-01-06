package com.tj.common;

public class Const {
    //成功的状态码
    public static final int RESPONSECODE_SUCCESS=0;
    //失败状态码
    public static final int RESPONSECODE_ERROR=100;
    //定义一个当前用户的常量
    public static final String CURRENTUSER="current_user";
    //定以一个状态码
    public enum ResponseCodeEnum{
        NEED_LOGIN(2,"需要登录"),
        NO_PRIVILEGE(3,"无权限操作")
        ;
        private int code;
        private String desc;

        ResponseCodeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    //为用户角色定义一个枚举类
    public enum RoleEnum{

        ROLE_Admin(0,"管理员"),
        ROLE_CUSTOMER(1,"普通用户")
        ;
        private int code;
        private String desc;

        RoleEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    //定义一个商品状态的枚举类
    public enum ProductEnum{
        PRODUCT_ONLINT(1,"在售"),
        PRODUCT_OFFLINT(2,"下架"),
        PRODUCT_DELETE(3,"删除")
        ;
        private int code;
        private String desc;

        ProductEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
}
