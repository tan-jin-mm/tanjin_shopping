package com.tj.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tj.common.ServerResponse;
import com.tj.dao.CategoryMapper;
import com.tj.dao.ProductMapper;
import com.tj.pojo.Category;
import com.tj.pojo.Product;
import com.tj.service.IProductService;
import com.tj.utils.DateUtils;
import com.tj.utils.PropertiesUtils;
import com.tj.vo.ProductDetailVO;
import com.tj.vo.ProductListVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;


@Service
public class ProductServiceImpl implements IProductService {
    @Autowired
    protected ProductMapper productMapper;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse saveOrUpdate(Product product) {
        if(product==null){
            return ServerResponse.serverResponseByError("参数不能为空");
        }
        //设置商品主图，主图为子图的第一张，子图格式，逗号分隔的一个字符串
        if(product.getSubImage()!=null&&!"".equals(product.getSubImage())){
            String[] subImageArray = product.getSubImage().split(",");
            if(subImageArray.length>0){
                product.setMainImage(subImageArray[0]);
            }
        }
        //通过id判断是添加还是更新
        if(product.getId()==null){
            //没有id就是添加新商品
            int result = productMapper.insert(product);
            if(result>0){
                return ServerResponse.serverResponseBySuccess();
            }else {
                return ServerResponse.serverResponseByError("添加失败");
            }
        }else {
            //有id就是修改商品信息
            int result = productMapper.updateByPrimaryKey(product);
            if(result>0){
                return ServerResponse.serverResponseBySuccess();
            }else {
                return ServerResponse.serverResponseByError("更新失败");
            }
        }
    }

    @Override
    public ServerResponse set_sale_status(Integer productId,Integer status) {
        if(productId==null){
            return ServerResponse.serverResponseByError("商品id参数不能为空");
        }
        if(status==null){
            return ServerResponse.serverResponseByError("商品状态参数不能为空");
        }
        //自己写的dao接口，传对象
        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        int result = productMapper.updateProductKeySelective(product);
        if(result>0){
            return ServerResponse.serverResponseBySuccess();
        }else {
            return ServerResponse.serverResponseByError("更新失败");
        }
    }

    @Override
    public ServerResponse detail(Integer productId) {
        if(productId==null){
            return ServerResponse.serverResponseByError("商品id参数不能为空");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.serverResponseByError("商品不存在");
        }
        ProductDetailVO productDetailVO = assembleProductDetailVO(product);
        return ServerResponse.serverResponseBySuccess(productDetailVO);
    }
    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        //使用mybatis-pageHelper插件进行分页,在查询数据之前使用，实现原理是springAOP
        PageHelper.startPage(pageNum, pageSize);
        List<Product> products = productMapper.selectAll();
        ArrayList<ProductListVO> productListVOS = new ArrayList<>();
        if(products!=null&&products.size()>0){
            for(Product product:products){
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOS.add(productListVO);
            }
        }
        //使用分页插件里的对象，返回前台分页格式的数据
        PageInfo pageInfo = new PageInfo(productListVOS);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }

    @Override
    public ServerResponse search(Integer productId,String produtcName, Integer pageNum, Integer pageSize) {
        //使用mybatis-pageHelper插件进行分页
        PageHelper.startPage(pageNum,pageSize);
        List<Product> products = productMapper.findProductByProductIdAndProductName(productId, produtcName);
        ArrayList<ProductListVO> productListVOS = new ArrayList<>();
        if(products!=null&&products.size()>0){
            for(Product product:products){
                ProductListVO productListVO = assembleProductListVO(product);
                productListVOS.add(productListVO);
            }
        }
        PageInfo pageInfo = new PageInfo(productListVOS);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }

    @Override
    public ServerResponse upload(String path, MultipartFile multipartFile) {
        if(multipartFile==null){
            return ServerResponse.serverResponseByError("没有选择文件");
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String exName = originalFilename.substring(originalFilename.indexOf("."));//扩展名
        String newName = UUID.randomUUID().toString()+exName;
        File file = new File(path);
        //若文件实例不存在，说明无此路径，一个该路径的文件夹
        if(!file.exists()){
            file.setWritable(true);
            file.mkdir();
        }
        File file1 = new File(path, newName);
        try {
            //将multipartFile转为file
            multipartFile.transferTo(file1);
            //上传到ftp服务器。。。
            //map<uri,url>,uri是文件名称，url是图片完整路径
            Map<String,String> map = new HashMap<>();
            map.put("uri",newName);
            map.put("url", PropertiesUtils.IMAGE_HOST  + newName);
            return ServerResponse.serverResponseBySuccess(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //定义一个方法，将product转为productDetalVO
    private ProductDetailVO assembleProductDetailVO(Product product){
        ProductDetailVO productDetailVO = new ProductDetailVO();
        productDetailVO.setCategoryId(product.getCategoryId());
        productDetailVO.setId(product.getId());
        productDetailVO.setName(product.getName());
        productDetailVO.setPrice(product.getPrice());
        productDetailVO.setStatus(product.getStatus());
        productDetailVO.setStock(product.getStock());
        productDetailVO.setSubImage(product.getSubImage());
        productDetailVO.setSubtitle(product.getSubtitle());
        productDetailVO.setMainImage(product.getMainImage());
        productDetailVO.setDetail(product.getDetail());
        //product中的属性能转为productDetailVO直接转，日期需要用到工具类
        productDetailVO.setCreateTime(DateUtils.dateToStr(product.getCreateTime()));
        productDetailVO.setUpdateTime(DateUtils.dateToStr(product.getUpdateTime()));
        //图片服务器的名称写入配置文件，所以还要封装一个读配置文件的工具类
        productDetailVO.setImageHost(PropertiesUtils.IMAGE_HOST);
        //通过id获得category，再getParentId
        Category category = categoryMapper.selectByPrimaryKey(product.getId());
        if(category!=null){
            productDetailVO.setParentCategoryId(category.getParentId());
        }else {
            productDetailVO.setParentCategoryId(0);
        }
        return productDetailVO;
    }

    //定义一个方法，将product转为productListVO
    private ProductListVO assembleProductListVO(Product product){
        ProductListVO productListVO = new ProductListVO();
        productListVO.setId(product.getId());
        productListVO.setCategoryId(product.getCategoryId());
        productListVO.setMainImage(product.getMainImage());
        productListVO.setName(product.getName());
        productListVO.setPrice(product.getPrice());
        productListVO.setStatus(product.getStatus());
        productListVO.setSubtitle(product.getSubtitle());
        return productListVO;
    }
}