package com.tj.service;

import com.tj.common.ServerResponse;
import com.tj.pojo.Shipping;

import javax.servlet.http.HttpSession;


public interface IAddressService {
    /*
    * 添加地址
    * */
    ServerResponse add(Integer userId,Shipping shipping);
    /*
    * 删除地址（防止横向越权，用userId和shippingId组合）
    * */
    ServerResponse del(Integer userId, Integer shippingId);
    /*
    * 更新地址表
    * */
    ServerResponse update(Shipping shipping);
    /*
    * 查看具体地址
    * */
    ServerResponse select(Integer shippingId);
    /*
    * 分页查询地址列表
    * */
    ServerResponse list(Integer pageNum,Integer pageSize);
}
