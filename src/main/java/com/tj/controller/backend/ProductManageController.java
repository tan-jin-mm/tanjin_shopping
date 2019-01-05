package com.tj.controller.backend;

import com.tj.common.Const;
import com.tj.common.ServerResponse;
import com.tj.pojo.Product;
import com.tj.pojo.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/*
 *
 * 商品后台管理
 * */
@RestController
@RequestMapping(value = "/manage/product")
public class ProductManageController {

    @RequestMapping(value = "/saveOrUpdate")
    public ServerResponse saveOrUpdate(HttpSession session, Product product){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        if(userInfo.getRole()!=Const.RoleEnum.ROLE_Admin.getCode()){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        return null;
    }
}