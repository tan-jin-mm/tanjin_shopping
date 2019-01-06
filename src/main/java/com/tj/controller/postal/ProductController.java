package com.tj.controller.postal;

import com.mysql.fabric.Server;
import com.tj.common.ServerResponse;
import com.tj.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;


@RestController
@RequestMapping(value = "/product")
public class ProductController {
    @Autowired
    private IProductService iProductService;
    /**
     * 商品详情
     */
    @RequestMapping(value = "/detail.do")
    public ServerResponse detail(Integer productId){
        ServerResponse serverResponse = iProductService.detail_postal(productId);
        return serverResponse;
    }
    /**
     * 前台搜索商品并排序，按照类别id,或者关键字查询，按照orderBy排序
     * orderBy命名规则:字段_desc,字段_asc
     */
    @RequestMapping(value = "/list.do")
    public ServerResponse list(@RequestParam(required = false) Integer categoryId,
                               @RequestParam(required = false) String keyword,
                               @RequestParam(required = false,defaultValue = "1") Integer pageNum,
                               @RequestParam(required = false,defaultValue = "10") Integer pageSize,
                               @RequestParam(required = false,defaultValue = "") String orderBy){
        ServerResponse serverResponse = iProductService.list_postal(categoryId,keyword,pageNum,pageSize,orderBy);
        return serverResponse;
    }
}
