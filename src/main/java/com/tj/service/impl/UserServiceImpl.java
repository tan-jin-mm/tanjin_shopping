package com.tj.service.impl;

import com.tj.common.Const;

import com.tj.common.ServerResponse;
import com.tj.dao.UserInfoMapper;
import com.tj.pojo.UserInfo;
import com.tj.service.IUserService;
import com.tj.utils.MD5Utils;
import com.tj.utils.TokenCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Override
    public ServerResponse login(String username, String password) {
        //判断参数是否为空
        if(username==null||"".equals(username)){
            return ServerResponse.serverResponseByError("用户名不能为空");
        }
        if(password==null||"".equals(password)){
            return ServerResponse.serverResponseByError("密码不能为空");
        }
        //检查用户名是否存在\
        int checkUsername = userInfoMapper.checkUsername(username);
        if(checkUsername==0){
            return ServerResponse.serverResponseByError("用户名不存在");
        }
        //根据用户名密码查找用户信息
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsernameAndPassword(username, MD5Utils.GetMD5Code(password));
        if(userInfo==null){
            return ServerResponse.serverResponseByError("密码错误");
        }
        userInfo.setPassword("");

        //返回结果
        return ServerResponse.serverResponseBySuccess(userInfo);
    }

    @Override
    public ServerResponse register(UserInfo userInfo) {
        //判断参数非空
        if(userInfo==null){
            return ServerResponse.serverResponseByError("参数必须");
        }
        //校验用户名
        int checkUsername = userInfoMapper.checkUsername(userInfo.getUsername());
        if(checkUsername>0){
            return ServerResponse.serverResponseByError("用户名已存在");
        }
        //校验密码
        int checkEmail = userInfoMapper.checkEmail(userInfo.getEmail());
        if(checkEmail>0){
            return ServerResponse.serverResponseByError("邮箱已存在");
        }
        //注册
        userInfo.setRole(Const.RoleEnum.ROLE_CUSTOMER.getCode());
        userInfo.setPassword(MD5Utils.GetMD5Code(userInfo.getPassword()));
        int insert = userInfoMapper.insert(userInfo);
        if(insert>0){
            return ServerResponse.serverResponseBySuccess("注册成功");
        }
        //返回结果
        return ServerResponse.serverResponseByError("注册失败");
    }

    @Override
    public ServerResponse forget_get_question(String username) {
        //判断参数是否为空
        if(username==null||"".equals(username)){
            return ServerResponse.serverResponseByError("用户名不能为空");
        }
        //校验用户名
        int checkUsername = userInfoMapper.checkUsername(username);
        if(checkUsername==0){
            return ServerResponse.serverResponseByError("用户名不存在");
        }
        String questionByUsername = userInfoMapper.selectQuestionByUsername(username);
        if(questionByUsername==null||"".equals(questionByUsername)){
            return  ServerResponse.serverResponseByError("密保问题为空");
        }
        return ServerResponse.serverResponseBySuccess(questionByUsername);
    }

    @Override
    public ServerResponse forget_check_answer(String username, String question, String answer) {
        //判断参数是否为空
        if(username==null||"".equals(username)){
            return ServerResponse.serverResponseByError("用户名不能为空");
        }
        if(question==null||"".equals(question)){
            return ServerResponse.serverResponseByError("密保问题不能为空");
        }
        if(answer==null||"".equals(answer)){
            return ServerResponse.serverResponseByError("密保答案不能为空");
        }
        int result = userInfoMapper.selectByUsernameAndQuestionAndAnswer(username, question, answer);
        if(result==0){
            return ServerResponse.serverResponseByError("答案错误");
        }
        //服务端生成一个随机唯一的token保存并发送给客户端
        String fogetToken = UUID.randomUUID().toString();
        //使用google的guava cache进行缓存
        TokenCache.set(username,fogetToken);
        return ServerResponse.serverResponseBySuccess(fogetToken);
    }

    @Override
    public ServerResponse forget_reset_password(String username, String passwordNew, String fogetToken) {
        //判断参数是否为空
        if(username==null||"".equals(username)){
            return ServerResponse.serverResponseByError("用户名不能为空");
        }
        if(passwordNew==null||"".equals(passwordNew)){
            return ServerResponse.serverResponseByError("密码不能为空");
        }
        if(fogetToken==null||"".equals(fogetToken)){
            return ServerResponse.serverResponseByError("Token不能为空");
        }
        //校验token
        String token = TokenCache.get(username);
        if(token==null){
            return ServerResponse.serverResponseByError("token过期");
        }
        if(!token.equals(fogetToken)){
            return ServerResponse.serverResponseByError("无效的token");
        }
        //修改密码
        int result = userInfoMapper.updateUserPassword(username, MD5Utils.GetMD5Code(passwordNew));
        if(result>0){
            return ServerResponse.serverResponseBySuccess();
        }
        return ServerResponse.serverResponseByError("修改密码失败");
    }

    @Override
    public ServerResponse check_valid(String str, String type) {
        //判断参数是否为空
        if(str==null||"".equals(str)){
            return ServerResponse.serverResponseByError("用户名或者邮箱不能为空");
        }
        if(type==null||"".equals(type)){
            return ServerResponse.serverResponseByError("校验类型不能为空");
        }
        //判断type类型
        if("username".equals(type)){
            int i = userInfoMapper.checkUsername(str);
            if(i>0){
                return ServerResponse.serverResponseByError("用户名已存在");
            }else {
                return ServerResponse.serverResponseBySuccess();
            }
        }else if("email".equals(type)){
            int i = userInfoMapper.checkEmail(str);
            if(i>0){
                return ServerResponse.serverResponseByError("邮箱已存在");
            }else {
                return ServerResponse.serverResponseBySuccess();
            }
        }else {
            return ServerResponse.serverResponseByError("校验类型错误");
        }

    }

    @Override
    public ServerResponse reset_password(String username,String passwordOld, String passwordNew) {
        //判断参数是否为空
        if(username==null||"".equals(username)){
            return ServerResponse.serverResponseByError("用户名不能为空");
        }
        if(passwordOld==null||"".equals(passwordOld)){
            return ServerResponse.serverResponseByError("旧密码不能为空");
        }
        if(passwordNew==null||"".equals(passwordNew)){
            return ServerResponse.serverResponseByError("新密码不能为空");
        }
        UserInfo userInfo = userInfoMapper.selectUserInfoByUsernameAndPassword(username, MD5Utils.GetMD5Code(passwordOld));
        if(userInfo==null){
            return ServerResponse.serverResponseByError("旧密码错误");
        }
        userInfo.setPassword(MD5Utils.GetMD5Code(passwordNew));
        int result = userInfoMapper.updateByPrimaryKey(userInfo);
        if(result>0){
            return ServerResponse.serverResponseBySuccess();
        }
        return ServerResponse.serverResponseByError("修改密码失败");
    }

    @Override
    public ServerResponse update_information(UserInfo userInfo) {
        //判断参数是否为空
        if(userInfo==null) {
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        int i = userInfoMapper.updateUserBySelectActive(userInfo);
        if(i>0){
            return ServerResponse.serverResponseBySuccess();
        }

        return ServerResponse.serverResponseByError("更新个人信息失败");
    }

    @Override
    public UserInfo findUserInfoByUserid(Integer userId) {
        return userInfoMapper.selectByPrimaryKey(userId);
    }


}
