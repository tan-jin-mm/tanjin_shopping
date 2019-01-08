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
    //为购物车的选择状态设置
    public enum CartCheckEnum{

        CART_CHECK_ENUM(1,"已勾选"),
        CART_UNCHECK_ENUM(0,"未勾选")
        ;
        private int code;
        private String desc;

        CartCheckEnum(int code, String desc) {
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
    //设置订单状态
    public enum OrderStatusEnum{

        ORDER__CANCLELED(0,"已取消"),
        ORDER__UN_PAY(10,"未付款"),
        ORDER__PAYED(20,"已付款"),
        ORDER__SEND(40,"已发货"),
        ORDER__SUCCESS(50,"交易成功"),
        ORDER__CLOSED(60,"已取消")
        ;
        private int code;
        private String desc;

        OrderStatusEnum(int code, String desc) {
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
        //遍历枚举类
        public static OrderStatusEnum codeOf(Integer code){
            for(OrderStatusEnum orderStatusEnum:values()){
                if(code == orderStatusEnum.getCode()){
                    return orderStatusEnum;
                }
            }
            return null;
        }
    }
    //为购物车的选择状态设置
    public enum PaymentEnum{

        PAYMENT_ONLINE(1,"线上支付")
        ;
        private int code;
        private String desc;

        PaymentEnum(int code, String desc) {
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
        //遍历枚举类
        public static PaymentEnum codeOf(Integer code){
            for(PaymentEnum paymentEnum:values()){
                if(code == paymentEnum.getCode()){
                    return paymentEnum;
                }
            }
            return null;
        }
    }
}
