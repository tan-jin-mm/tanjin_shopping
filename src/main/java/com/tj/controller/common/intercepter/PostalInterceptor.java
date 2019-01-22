package com.tj.controller.common.intercepter;

import com.google.gson.Gson;
import com.tj.common.Const;
import com.tj.common.ServerResponse;
import com.tj.pojo.UserInfo;
import com.tj.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
@Component
public class PostalInterceptor implements HandlerInterceptor {
    @Autowired
    private IUserService iUserService;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        System.out.println("========preHandle=========");
        Cookie[] cookies = httpServletRequest.getCookies();
        HttpSession session = httpServletRequest.getSession();
        UserInfo userInfo = (UserInfo) session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            if(cookies!=null&&cookies.length>=1){
                for (Cookie cookie:cookies){
                    if (Const.AUTOLOGINTOKEN.equals(cookie.getName())){
                        String token = cookie.getValue();
                        userInfo = iUserService.findUserInforByToken(token);
                        if(userInfo!=null){
                            session.setAttribute(Const.CURRENTUSER,userInfo);
                            return true;
                        }
                    }
                }
                ServerResponse serverResponse = ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
                Gson gson = new Gson();
                //重构HttpServletResponse
                httpServletResponse.reset();
                httpServletResponse.setCharacterEncoding("UTF-8");
                httpServletResponse.setContentType("application/json;charset=UTF-8");
                PrintWriter printWriter = httpServletResponse.getWriter();
                String json = gson.toJson(serverResponse);
                printWriter.write(json);
                printWriter.flush();
                printWriter.close();
                return false;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
