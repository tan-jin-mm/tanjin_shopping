package com.tj.service;

import com.tj.common.ServerResponse;

import javax.servlet.http.HttpSession;

public interface ICartService {
    /*
     * 添加或者更新购物车
     * */
    ServerResponse add(Integer userId, Integer productId, Integer count);
    /*
    * 购物车列表
    * */
    ServerResponse list(Integer userId);
    /*
     * 更新某个产品的数量
     * */
    ServerResponse update(Integer userId,Integer productId,Integer count);
    /*
    * 删除购物车中一件或多件商品
    * */
    ServerResponse delete_product(Integer userId,String productIds);
    /*
    * 购物车选中某个商品
    * */
   ServerResponse select(Integer userId,Integer productId,Integer check);
   /*
   * 统计购物车里里商品的数量
   * */
   ServerResponse get_cart_product_count(Integer userId);

}
