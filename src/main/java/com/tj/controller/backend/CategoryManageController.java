package com.tj.controller.backend;

import com.tj.common.Const;
import com.tj.common.ServerResponse;
import com.tj.pojo.UserInfo;
import com.tj.service.ICategoryService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/manage/category")
public class CategoryManageController {
    @Autowired
    private ICategoryService iCategoryService;

    /**
     * 获取品类子节点（平级）
     */
    @RequestMapping(value = "/get_categoty.do")
    public ServerResponse get_categoty(HttpSession session,Integer categoryId){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        if(userInfo.getRole()!=Const.RoleEnum.ROLE_Admin.getCode()){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        ServerResponse serverResponse = iCategoryService.get_categoty(categoryId);
        return serverResponse;
    }
    /**
     * 增加节点
     */
    @RequestMapping(value = "/add_categoty.do")
    public ServerResponse add_categoty(HttpSession session,
                                       @RequestParam(required = false,defaultValue = "0")Integer parentId,
                                        String categoryName){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        if(userInfo.getRole()!=Const.RoleEnum.ROLE_Admin.getCode()){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        ServerResponse serverResponse = iCategoryService.add_categoty(parentId,categoryName);
        return serverResponse;
    }
    /**
     * 修改品类名称
     */
    @RequestMapping(value = "/set_categoty_name.do")
    public ServerResponse set_categoty_name(HttpSession session, Integer categoryId, String categoryName){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        if(userInfo.getRole()!=Const.RoleEnum.ROLE_Admin.getCode()){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        ServerResponse serverResponse = iCategoryService.set_categoty_name(categoryId,categoryName);
        return serverResponse;
    }
    /**
     * 获取当前分类ID和所有子节点categoryId
     */
    @RequestMapping(value = "/get_deep_category.do")
    public ServerResponse get_deep_category(HttpSession session, Integer categoryId){
        UserInfo userInfo = (UserInfo)session.getAttribute(Const.CURRENTUSER);
        if(userInfo==null){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NEED_LOGIN.getCode(),Const.ResponseCodeEnum.NEED_LOGIN.getDesc());
        }
        if(userInfo.getRole()!=Const.RoleEnum.ROLE_Admin.getCode()){
            return ServerResponse.serverResponseByError(Const.ResponseCodeEnum.NO_PRIVILEGE.getCode(),Const.ResponseCodeEnum.NO_PRIVILEGE.getDesc());
        }
        ServerResponse serverResponse = iCategoryService.get_deep_category(categoryId);
        return serverResponse;
    }


}
