package com.tj.service;

import com.tj.common.ServerResponse;
import com.tj.pojo.UserInfo;

import javax.servlet.http.HttpSession;

public interface IUserService {

    /**
     * 登录接口
     */
    ServerResponse login(String username,String password);

    /**
     * 注册接口
     */
    ServerResponse register(UserInfo userInfo);
    /**
     * 查找回密码问题
     */
    ServerResponse forget_get_question(String username);
    /**
     * 校验答案
     */
    ServerResponse forget_check_answer(String username,String question,String answer);

    /**
     * 忘记密码修改密码
     */
    ServerResponse forget_reset_password(String username,String passwordNew,String fogetToken);
    /**
     * 检查用户名是否有效
     */
    ServerResponse check_valid(String str,String type);
    /**
     * 登录状态下重置密码,用旧密码防止横向越权
     */
    ServerResponse reset_password(String username,String passwordOld, String passwordNew);
    /**
     *登录状态下更新个人信息
     */
    ServerResponse update_information(UserInfo userInfo);
    /**
     * 根据用户id查询最新用户信息
     */
    UserInfo findUserInfoByUserid(Integer userId);

    /*
     * 根据tocken查询用户信息
     * */
    UserInfo findUserInforByToken(String token);
    /*
    * 根据用户id修改token字段
    * */
    int updateTokenByUserId(Integer userId, String token);
}
