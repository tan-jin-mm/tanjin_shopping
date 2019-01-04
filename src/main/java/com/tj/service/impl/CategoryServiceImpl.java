package com.tj.service.impl;

import com.google.common.collect.Sets;
import com.tj.common.ServerResponse;
import com.tj.dao.CategoryMapper;
import com.tj.pojo.Category;
import com.tj.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

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
        Set<Category> categories = new HashSet<>();
        //使用set集合可以自动去重
        Set<Category> allChildCategory = findAllChildCategory(categories, categoryId);
        //遍历这个集合，将id取出来放入一个新的set集合中，返回id的集合
        HashSet<Integer> categoryIds = new HashSet<>();
        //用迭代器进行遍历
        Iterator<Category> iterator = categories.iterator();
        while (iterator.hasNext()){
            Category category = iterator.next();
            categoryIds.add(category.getId());
        }
        return ServerResponse.serverResponseBySuccess(categoryIds);
    }


    //定义一个递归查询的方法
    public Set<Category> findAllChildCategory(Set<Category> categories,Integer categoryId){
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        if(categories!=null){
            categories.add(category);//通过id判断是否相等，需要重写hash和equals
        }
        //查找categoryId下的子节点（平级）
        List<Category> childCategory = categoryMapper.findChildCategory(categoryId);
        if(childCategory!=null&&childCategory.size()!=0){
            for(Category category1:childCategory){
               findAllChildCategory(categories,category1.getId());
            }
        }

        return categories;

    }

}
