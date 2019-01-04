package com.tj.service.impl;

import com.tj.common.ServerResponse;
import com.tj.dao.CategoryMapper;
import com.tj.pojo.Category;
import com.tj.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public ServerResponse get_categoty(Integer categoryId) {
        if(categoryId==null){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category==null){
            return ServerResponse.serverResponseByError("该类别不存在");
        }
        List<Category> categorieList = categoryMapper.findChildCategory(categoryId);
        return ServerResponse.serverResponseBySuccess(categorieList);
    }

    @Override
    public ServerResponse add_categoty(Integer parentId, String categoryName) {
        if(categoryName==null||"".equals(categoryName)){
            return ServerResponse.serverResponseByError("类别名称不能为空");
        }
        Category category = new Category();
        category.setName(categoryName);
        category.setStatus(1);
        category.setParentId(parentId);
        int i = categoryMapper.insert(category);
        if(i>0){
            return ServerResponse.serverResponseBySuccess();
        }
        return ServerResponse.serverResponseByError("添加失败");
    }

    @Override
    public ServerResponse set_categoty_name(Integer categoryId, String categoryName) {
        if(categoryId==null){
            return ServerResponse.serverResponseByError("类别id不能为空");
        }
        if(categoryName==null||"".equals(categoryName)){
            return ServerResponse.serverResponseByError("类别名称不能为空");
        }
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(category==null){
            return ServerResponse.serverResponseByError("要修改的类别不存在");
        }
        category.setName(categoryName);
        int i = categoryMapper.updateByPrimaryKey(category);
        if(i>0){
            return ServerResponse.serverResponseBySuccess();
        }
        return ServerResponse.serverResponseByError("修改失败");
    }

    @Override
    public ServerResponse get_deep_category(Integer categoryId) {
        if(categoryId==null){
            return ServerResponse.serverResponseByError("类别id不能为空");
        }
        //需要进行递归查询
        return null;
    }
    //定义一个递归查询的方法
    public List<Category> findAllChildCategory(List<Category> categories,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(categories!=null){
            categories.add(category);
        }
        return null;

    }

}
