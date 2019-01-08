package com.tj.service;

import com.tj.common.ServerResponse;


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

}
