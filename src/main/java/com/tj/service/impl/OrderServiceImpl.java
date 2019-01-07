package com.tj.service.impl;

import com.tj.common.Const;
import com.tj.common.ServerResponse;
import com.tj.dao.CartMapper;
import com.tj.dao.ProductMapper;
import com.tj.pojo.Cart;
import com.tj.pojo.Order;
import com.tj.pojo.OrderItem;
import com.tj.pojo.Product;
import com.tj.service.IOrderService;
import com.tj.utils.BigDecimalUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public ServerResponse create(Integer userId, Integer shippingId) {
        if(shippingId==null){
            return ServerResponse.serverResponseByError("地址参数不能为空");
        }
        //将购物车中该用户选中的商品查询出来，转为OrderItemList
        List<Cart> cartList = cartMapper.findCartListByUserIdAndCheck(userId);
        ServerResponse serverResponse = getOrderItem(cartList);
        if(serverResponse.isSuccess()){
            return serverResponse;
        }
        //计算订单总价格
        BigDecimal orderTotalPrice = new BigDecimal("0");
        //创建订单Order并把它保存到数据库
        createOrder(userId,shippingId,orderTotalPrice);
        return null;
    }
    /*
    * 计算订单总价格
    * */
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItems){
        BigDecimal orderTotalPrice = new BigDecimal("0");
       for(OrderItem orderItem:orderItems){
           orderTotalPrice = BigDecimalUtils.add(orderTotalPrice.doubleValue(),orderItem.getTotlePrice().doubleValue());
       }
       return orderTotalPrice;
    }

    //List<Cart>---->List<OrderItem>
    private ServerResponse getOrderItem(List<Cart> cartList){
        List<OrderItem> orderItems = new ArrayList<>();
        if(cartList==null||cartList.size()==0) {
            return ServerResponse.serverResponseByError("购物车为空");
        }
        for(Cart cart:cartList){
            OrderItem orderItem = new OrderItem();
            orderItem.setUserId(cart.getUserId());
            Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            if(product==null){
                return ServerResponse.serverResponseByError("id为"+cart.getProductId()+"的商品不存在");
            }
            if(product.getStatus()!= Const.ProductEnum.PRODUCT_ONLINT.getCode()){
                return ServerResponse.serverResponseByError("id为"+cart.getProductId()+"的商品已经下架");
            }
            if(cart.getQuantity()>product.getStock()){
                return ServerResponse.serverResponseByError("id为"+cart.getProductId()+"的商品库存不足");
            }
            orderItem.setProductId(cart.getProductId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setTotlePrice(BigDecimalUtils.mul(product.getPrice().doubleValue(),cart.getQuantity().doubleValue()));
            orderItems.add(orderItem);
        }
        return ServerResponse.serverResponseBySuccess(orderItems);
    }
    //创建订单
    private Order createOrder(Integer userId, Integer shippingId, BigDecimal orderTotalPrice){
        Order order = new Order();
        order.setOrderNo(genetateOrderNO());
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setStatus(Const.OrderStatusEnum.ORDER__UN_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(Const.PaymentEnum.PAYMENT_ONLINE.getCode());
        return order;
    }
    /*
    * 定义一个生成订单编号的规则
    * */
    private Long genetateOrderNO(){
        return System.currentTimeMillis()+new Random().nextInt(100);
    }
    @Override
    public ServerResponse get_order_cart_product(Integer userId, Integer shippingId) {
        return null;
    }

    @Override
    public ServerResponse list(Integer shippingId) {
        return null;
    }
}
