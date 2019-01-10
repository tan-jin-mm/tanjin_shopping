package com.tj.controller.postal;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.tj.common.Const;
import com.tj.common.ServerResponse;
import com.tj.pojo.UserInfo;
import com.tj.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    /*
     * 支付接口
     * */
    @RequestMapping(value="/pay.do")
    public ServerResponse pay(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iOrderService.pay(userInfo.getId(),orderNo);
        return serverResponse;
    }
    /*
    * 支付宝回调应用服务器接口
    * */
    @RequestMapping(value="/alipay_callback.do")
    public ServerResponse callback(HttpServletRequest request){
        System.out.println("支付宝回调应用服务器接口");
        //拿到支付宝返回的参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String,String> requestMap = new HashMap<>();
        Iterator<String> iterator = parameterMap.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            String[] strings = parameterMap.get(key);
            String value = "";
            for(int i=0;i<strings.length;i++){
                value= (i==strings.length-1)? value+strings[i]:value+strings[i]+",";
            }
            requestMap.put(key,value);
        }
        //支付宝验签
        try {
            requestMap.remove("sign_type");
            boolean result = AlipaySignature.rsaCheckV2(requestMap, Configs.getAlipayPublicKey(), "utf-8", Configs.getSignType());
            if(!result){
                return ServerResponse.serverResponseByError("非法请求，验证不通过");
            }

        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //处理业务逻辑
        return iOrderService.alipay_callback(requestMap);
    }
    /*
     * 查询订单支付状态
     * */
    @RequestMapping(value="/query_order_pay_status.do")
    public ServerResponse query_order_pay_status(HttpSession session,Long orderNo){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iOrderService.query_order_pay_status(orderNo);
        return serverResponse;
    }

}
