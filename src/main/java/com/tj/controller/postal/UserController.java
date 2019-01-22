package com.tj.controller.postal;

import com.tj.common.Const;
import com.tj.common.ServerResponse;
import com.tj.pojo.UserInfo;
import com.tj.service.IUserService;
import com.tj.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/portal/user")
public class UserController {
    @Autowired
    private IUserService iUserService;

    /**
     *登录接口
     */
    @RequestMapping(value = "/login.do")
    private ServerResponse login(HttpServletResponse response, HttpSession session, String username, String password){
        ServerResponse serverResponse = iUserService.login(username, password);
        //登录成功后将用户信息放入session中
        if(serverResponse.isSuccess()){
            UserInfo userInfo = (UserInfo)serverResponse.getData();
            session.setAttribute(Const.CURRENTUSER,userInfo);
            //生成token
            String token = MD5Utils.GetMD5Code(username)+MD5Utils.GetMD5Code(password);
            iUserService.updateTokenByUserId(userInfo.getId(), token);
            Cookie cookie = new Cookie(Const.AUTOLOGINTOKEN,token);
            cookie.setPath("/");
            cookie.setMaxAge(300);
            response.addCookie(cookie);
        }
        return serverResponse;
    }
    /**
     *注册接口
     */
    @RequestMapping(value = "/register.do")
    private ServerResponse register(UserInfo userInfo){
        ServerResponse serverResponse = iUserService.register(userInfo);
        return serverResponse;
    }
    /**
     * 校验用户，查询找回密码问题
     */
    @RequestMapping(value = "/forget_get_question.do")
    private ServerResponse forget_get_question(String username){
        ServerResponse serverResponse = iUserService.forget_get_question(username);
        return serverResponse;
    }

    /**
     * 根据问题答案确定用户，生成token
     */
    @RequestMapping(value = "/forget_check_answer.do")
    private ServerResponse forget_check_answer(String username,String question,String answer){
        ServerResponse serverResponse = iUserService.forget_check_answer(username,question,answer);
        return serverResponse;
    }

    /**
     * 忘记密码后重置密码
     */
    @RequestMapping(value = "/forget_reset_password.do")
    private ServerResponse forget_reset_password(String username,String passwordNew,String fogetToken){
        ServerResponse serverResponse = iUserService.forget_reset_password(username,passwordNew,fogetToken);
        return serverResponse;
    }
    /**
     * 检查用户名是否有效
     */
    @RequestMapping(value = "/check_valid.do")
    private ServerResponse check_valid(String str,String type){
        ServerResponse serverResponse = iUserService.check_valid(str,type);
        return serverResponse;
    }
    /**
     * 获取登录用户信息
     */
    @RequestMapping(value = "/get_user_info.do")
    private ServerResponse get_user_info(HttpSession session){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError("用户未登录");
        }
        userInfo.setPassword("");
        userInfo.setQuestion("");
        userInfo.setAnswer("");
        userInfo.setRole(null);
        return ServerResponse.serverResponseBySuccess(userInfo);
    }
    /**
     * 登录状态下重置密码,用旧密码防止横向越权
     */
    @RequestMapping(value = "/reset_password.do")
    private ServerResponse reset_password(HttpSession session,String passwordOld,String passwordNew){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError("用户未登录");
        }
        ServerResponse serverResponse = iUserService.reset_password(userInfo.getUsername(),passwordOld,passwordNew);
        return serverResponse;
    }
    /**
     *登录状态下更新个人信息
     */
    @RequestMapping(value = "/update_information.do")
    private ServerResponse update_information(HttpSession session,UserInfo user){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError("用户未登录");
        }
        user.setId(userInfo.getId());
        ServerResponse serverResponse = iUserService.update_information(user);
        if(serverResponse.isSuccess()){
            //更新session中的用户信息
            UserInfo userInfo1 = iUserService.findUserInfoByUserid(user.getId());
            session.setAttribute(Const.CURRENTUSER,userInfo1);
        }
        return serverResponse;
    }
    /**
     * 获取登录用户详细信息
     */
    @RequestMapping(value = "/get_information.do")
    private ServerResponse get_information(HttpSession session){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError("用户未登录");
        }
        UserInfo userInfo1 = iUserService.findUserInfoByUserid(userInfo.getId());
        userInfo1.setPassword("");
        return ServerResponse.serverResponseBySuccess(userInfo1);
    }
    /**
     * 退出登录
     */
    @RequestMapping(value = "/logout.do")
    private ServerResponse logout(HttpSession session){
       session.removeAttribute(Const.CURRENTUSER);
       return ServerResponse.serverResponseBySuccess();
    }
}
