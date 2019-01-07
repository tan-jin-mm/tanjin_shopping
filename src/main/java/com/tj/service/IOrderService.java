package com.tj.service;

import com.tj.common.ServerResponse;


public interface IOrderService {
    /*
     * 创建订单
     * */
    ServerResponse create(Integer userId,Integer shippingId);
    /*
    * 获取订单的商品信息
    * */
    ServerResponse get_order_cart_product(Integer userId, Integer shippingId);
    /*
    * 订单List
    * */
    ServerResponse list(Integer shippingId);

}
