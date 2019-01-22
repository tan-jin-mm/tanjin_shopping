package com.tj.controller.postal;

import com.tj.common.Const;
import com.tj.common.ServerResponse;
import com.tj.pojo.Shipping;
import com.tj.pojo.UserInfo;
import com.tj.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.Serializable;

@RestController
@RequestMapping(value = "/portal/shipping")
public class AddressController {
    @Autowired
    private IAddressService iAddressService;
    /*
    * 添加地址
    * */
    @RequestMapping(value = "/add.do")
    public ServerResponse add(HttpSession session, Shipping shipping){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iAddressService.add(userInfo.getId(),shipping);
        return serverResponse;
    }
    /*
    * 删除地址
    * */
    @RequestMapping(value = "/del.do")
    public ServerResponse del(HttpSession session,Integer shippingId){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iAddressService.del(userInfo.getId(),shippingId);
        return serverResponse;
    }
    /*
     * 更新地址表
     * */
    @RequestMapping(value = "/update.do")
    public ServerResponse update(HttpSession session,Shipping shipping){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iAddressService.update(shipping);
        return serverResponse;
    }
    /*
     * 查看具体地址
     * */
    @RequestMapping(value = "/select.do")
    public ServerResponse select(HttpSession session,Integer shippingId){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iAddressService.select(shippingId);
        return serverResponse;
    }
    /*
     * 地址列表
     * */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(HttpSession session,
                               @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10")Integer pageSize){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return  ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        ServerResponse serverResponse = iAddressService.list(pageNum,pageSize);
        return serverResponse;
    }


}
