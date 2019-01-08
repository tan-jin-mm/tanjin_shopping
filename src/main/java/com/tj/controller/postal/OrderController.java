package com.tj.controller.postal;

import com.tj.common.Const;
import com.tj.common.ServerResponse;
import com.tj.pojo.UserInfo;
import com.tj.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
     * 获取购物车的商品信息
     * */
    @RequestMapping(value = "/get_order_cart_product.do")
    public ServerResponse get_order_cart_product(HttpSession session){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iOrderService.get_order_cart_product(userInfo.getId());
        return serverResponse;
    }
    /*
     * 订单List(兼容前台和后台，普通用户只能查看自己的订单，管理员可以查看所有订单)
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(required = false,defaultValue = "1")Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10") Integer pageSize){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iOrderService.list(userInfo.getId(), pageNum,pageSize);
        return serverResponse;
    }
    /*
    * 取消订单
    * */
    @RequestMapping(value = "/cancel.do")
    public ServerResponse cancel(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iOrderService.cancel(userInfo.getId(), orderNo);
        return serverResponse;
    }
    /*
    * 订单详情detail(根据orderNo获得OrderVO
    * */
    @RequestMapping(value="/detail.do")
    public ServerResponse detail(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iOrderService.detail(orderNo);
        return serverResponse;
    }

}
