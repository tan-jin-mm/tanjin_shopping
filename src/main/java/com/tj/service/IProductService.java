package com.tj.service;

import com.tj.common.ServerResponse;
import com.tj.pojo.Product;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;

public interface IProductService {
    /*
     * 添加和更新商品的接口
     * */
    ServerResponse saveOrUpdate( Product product);
    /*
     * 商品上下架
     * */
    ServerResponse set_sale_status(Integer productId,Integer status);
    /*
     * 商品详情
     * */
    ServerResponse detail(Integer productId);
    /*
    * 商品分页列表
    * */
    ServerResponse list(Integer pageNum,Integer pageSize);
    /*
     * 后台商品搜索，可以通过id精确查询，也可通过名称模糊查询，返回的还是分页对象
     * */
    ServerResponse search(Integer productId,String produtcName,Integer pageNum,Integer pageSize);
    /**
     * 图片上传
     */
    ServerResponse upload(String path,MultipartFile multipartFile);
    /*
    * 前台-商品详情
    * */
    ServerResponse detail_postal(Integer productId);
    /*
    *前台搜索商品并排序，按照类别id,或者关键字查询，按照orderBy排序
    * orderBy命名规则字段_order
    * */
    ServerResponse list_postal(Integer categoryId, String keyword, Integer pageNum, Integer pageSize, String orderBy);
}
