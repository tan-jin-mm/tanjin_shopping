package com.tj.controller.postal;

import com.tj.common.Const;
import com.tj.common.ServerResponse;
import com.tj.pojo.UserInfo;
import com.tj.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/order")
public class OrderController {
    @Autowired
    private IOrderService iOrderService;
    /*
    * 创建订单
    * */
    @RequestMapping(value = "/create.do")
    public ServerResponse create(HttpSession session,Integer shippingId){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iOrderService.create(userInfo.getId(), shippingId);
        return serverResponse;
    }
    /*
     * 获取订单的商品信息
     * */
    @RequestMapping(value = "/get_order_cart_product.do")
    public ServerResponse get_order_cart_product(HttpSession session,Integer shippingId){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iOrderService.get_order_cart_product(userInfo.getId(), shippingId);
        return serverResponse;
    }
    /*
     * 订单List
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,Integer shippingId){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        /*ServerResponse serverResponse = iOrderService.list(userInfo.getId(), shippingId);*/
        return null;
    }


}
