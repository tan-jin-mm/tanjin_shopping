package com.tj.service;

import com.tj.common.ServerResponse;

import java.util.Map;


public interface IOrderService {
    /*
     * 创建订单
     * */
    ServerResponse create(Integer userId,Integer shippingId);
    /*
    * 获取购物车的商品信息
    * */
    ServerResponse get_order_cart_product(Integer userId);
    /*
    * 订单List
    * */
    ServerResponse list(Integer userId,Integer pageNum,Integer pageSize);
    /*
    * 取消订单
    * */
    ServerResponse cancel(Integer userId, Long orderNo);
    /*
    * 订单详情
    * */
    ServerResponse detail(Long orderNo);
    /*
    * 支付接口
    * */
    ServerResponse pay(Integer userId,Long orderNo);
    /*
    * 支付宝回调
    * */
    ServerResponse alipay_callback(Map<String,String> map);
    /*
    * 查询订单支付状态
    * */
    ServerResponse query_order_pay_status(Long orderNo);
    /*
    * 关闭订单
    * */
    void closeOrderByScheduled(String date);
}
