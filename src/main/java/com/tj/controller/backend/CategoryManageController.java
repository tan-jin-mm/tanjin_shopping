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
    @RequestMapping(value = "/get_categoty/{categoryId}")
    public ServerResponse get_categoty(HttpSession session,
                                       @PathVariable("categoryId")Integer categoryId){
        return iCategoryService.get_categoty(categoryId);
    }
    /**
     * 增加节点
     */
    @RequestMapping(value = "/add_categoty/parentId/{parentId}/{categoryName}")
    public ServerResponse add_categotyByParentId(HttpSession session,
                                       @PathVariable("parentId") Integer parentId,
                                       @PathVariable("categoryName")String categoryName){
        return iCategoryService.add_categoty(parentId,categoryName);
    }
    @RequestMapping(value = "/add_categoty/{categoryName}")
    public ServerResponse add_categoty(HttpSession session,
                                       Integer parentId,
                                       @PathVariable("categoryName")String categoryName){
        return iCategoryService.add_categoty(parentId,categoryName);
    }
    /**
     * 修改品类名称
     */
    @RequestMapping(value = "/set_categoty_name/{categoryId}/{categoryName}")
    public ServerResponse set_categoty_name(HttpSession session,
                                            @PathVariable("categoryId") Integer categoryId,
                                            @PathVariable("categoryName") String categoryName){
        return iCategoryService.set_categoty_name(categoryId,categoryName);
    }
    /**
     * 获取当前分类ID和所有子节点categoryId
     */
    @RequestMapping(value = "/get_deep_category/{categoryId}")
    public ServerResponse get_deep_category(HttpSession session,
                                            @PathVariable("categoryId") Integer categoryId){
        return iCategoryService.get_deep_category(categoryId);
    }


}
