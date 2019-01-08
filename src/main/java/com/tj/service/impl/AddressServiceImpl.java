package com.tj.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tj.common.ServerResponse;
import com.tj.dao.ShippingMapper;
import com.tj.pojo.Shipping;
import com.tj.service.IAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements IAddressService {
    @Autowired
    private ShippingMapper shippingMapper;
    @Override
    public ServerResponse add(Integer userId,Shipping shipping) {
        if(shipping==null){
            return ServerResponse.serverResponseByError("参数错误");
        }
        //添加到数据库，返回主键
        shipping.setUserId(userId);
        shippingMapper.insert(shipping);
        Map<String,Integer> map = new HashMap<>();
        map.put("shippingId",shipping.getId());
        return ServerResponse.serverResponseBySuccess(map);
    }

    @Override
    public ServerResponse del(Integer userId, Integer shippingId) {
        if(shippingId==null){
            return ServerResponse.serverResponseByError("参数错误");
        }
        int i = shippingMapper.deleteByUserIdAndShippingId(userId, shippingId);
        if(i>0){
            return ServerResponse.serverResponseBySuccess();
        }
        return ServerResponse.serverResponseByError("删除失败");
    }

    @Override
    public ServerResponse update( Shipping shipping) {
        if(shipping==null){
            return ServerResponse.serverResponseByError("参数错误");
        }
        int i = shippingMapper.updateBySelectiveKey(shipping);
        if(i>0){
            return ServerResponse.serverResponseBySuccess();
        }
        return ServerResponse.serverResponseByError("更新失败");
    }

    @Override
    public ServerResponse select(Integer shippingId) {
        if(shippingId==null){
            return ServerResponse.serverResponseByError("参数错误");
        }
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        return ServerResponse.serverResponseBySuccess(shipping);
    }

    @Override
    public ServerResponse list(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippings = shippingMapper.selectAll();
        PageInfo pageInfo = new PageInfo(shippings);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }
}
