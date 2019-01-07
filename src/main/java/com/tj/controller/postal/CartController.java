package com.tj.controller.postal;

import com.tj.common.Const;
import com.tj.common.ServerResponse;
import com.tj.pojo.UserInfo;
import com.tj.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/cart")
public class CartController {

    @Autowired
    private ICartService iCartService;
    /*
    * 添加或者更新购物车
    * */
    @RequestMapping(value = "/add.do")
    public ServerResponse add(HttpSession session,Integer productId,Integer count){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iCartService.add(userInfo.getId(),productId,count);
        return serverResponse;
    }
    /*
     * 查看购物车列表
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iCartService.list(userInfo.getId());
        return serverResponse;
    }
    /*
     * 更新某个产品的数量
     * */
    @RequestMapping(value = "/update.do")
    public ServerResponse update(HttpSession session,Integer productId,Integer count){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iCartService.update(userInfo.getId(),productId,count);
        return serverResponse;
    }
    /*
     * 移除购物车某个商品
     * */
    @RequestMapping(value = "/delete_product.do")
    public ServerResponse delete_product(HttpSession session,String productIds){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iCartService.delete_product(userInfo.getId(),productIds);
        return serverResponse;
    }
    /*
     * 修改购物车选中某个商品的状态
     * */
    @RequestMapping(value = "/select.do")
    public ServerResponse select(HttpSession session,Integer productId){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iCartService.select(userInfo.getId(),productId,Const.CartCheckEnum.CART_CHECK_ENUM.getCode());
        return serverResponse;
    }
    /*
     * 取消购物车选中的状态
     * */
    @RequestMapping(value = "/un_select.do")
    public ServerResponse un_select(HttpSession session,Integer productId){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iCartService.select(userInfo.getId(),productId,Const.CartCheckEnum.CART_UNCHECK_ENUM.getCode());
        return serverResponse;
    }
    /*
     * 全选
     * */
    @RequestMapping(value = "/select_all.do")
    public ServerResponse select_all(HttpSession session){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iCartService.select(userInfo.getId(),null,Const.CartCheckEnum.CART_CHECK_ENUM.getCode());
        return serverResponse;
    }
    /*
     * 取消全选
     * */
    @RequestMapping(value = "/un_select_all.do")
    public ServerResponse un_select_all(HttpSession session){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iCartService.select(userInfo.getId(),null,Const.CartCheckEnum.CART_UNCHECK_ENUM.getCode());
        return serverResponse;
    }
    /*
     * 购物车中商品的总数
     * */
    @RequestMapping(value = "/get_cart_product_count.do")
    public ServerResponse get_cart_product_count(HttpSession session){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iCartService.get_cart_product_count(userInfo.getId());
        return serverResponse;
    }


}
