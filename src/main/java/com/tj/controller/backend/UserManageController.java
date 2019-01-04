package com.tj.controller.backend;

import com.tj.common.Const;
import com.tj.common.ServerResponse;
import com.tj.pojo.UserInfo;
import com.tj.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * 后台用户控制器
 */
@RestController
@RequestMapping(value = "/manage/user")
public class UserManageController {
    @Autowired
    private IUserService iUserService;
    /**
     * 管理员登录
     */
    @RequestMapping(value = "/login.do")
    private ServerResponse login(HttpSession session, String username, String password){
        ServerResponse serverResponse = iUserService.login(username, password);
        //登录成功后将用户信息放入session中
        if(serverResponse.isSuccess()){
            UserInfo userInfo = (UserInfo)serverResponse.getData();
            if(userInfo.getRole()!=Const.RoleEnum.ROLE_Admin.getCode()){
                return ServerResponse.serverResponseByError("无权限登录");
            }
            session.setAttribute(Const.CURRENTUSER,userInfo);
        }
        return serverResponse;
    }


}
