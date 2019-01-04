package com.tj.controller;

import com.tj.pojo.UserInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class TestController {
    @RequestMapping(value = "/loginTest.do")
    public UserInfo getUserInfor(){
       UserInfo userInfo = new UserInfo();
       userInfo.setUsername("zhangsan");
       return userInfo;
    }
}
