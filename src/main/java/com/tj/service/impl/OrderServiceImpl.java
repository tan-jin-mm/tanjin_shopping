package com.tj.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tj.common.Const;
import com.tj.common.ServerResponse;
import com.tj.dao.*;
import com.tj.pojo.*;
import com.tj.service.IOrderService;
import com.tj.utils.BigDecimalUtils;
import com.tj.utils.DateUtils;
import com.tj.utils.PropertiesUtils;
import com.tj.vo.CartOrderItemVO;
import com.tj.vo.OrderItemVO;
import com.tj.vo.OrderVO;
import com.tj.vo.ShippingVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Service
public class OrderServiceImpl implements IOrderService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private ShippingMapper shippingMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public ServerResponse create(Integer userId, Integer shippingId) {
        if(shippingId==null){
            return ServerResponse.serverResponseByError("地址参数不能为空");
        }
        //将购物车中该用户选中的商品查询出来，转为OrderItemList
        List<Cart> cartList = cartMapper.findCartListByUserIdAndCheck(userId);
        ServerResponse serverResponse = getOrderItem(cartList);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        //计算订单总价格
        BigDecimal orderTotalPrice = new BigDecimal("0");
        List<OrderItem> orderItemList = (List<OrderItem>)serverResponse.getData();
        if(orderItemList==null||orderItemList.size()==0){
            return ServerResponse.serverResponseByError("购物车为空");
        }
        orderTotalPrice = getOrderTotalPrice(orderItemList);
        //创建订单Order并把它保存到数据库
        Order order = createOrder(userId, shippingId, orderTotalPrice);
        if(order==null){
            return ServerResponse.serverResponseByError("订单创建失败");
        }
        for(OrderItem orderItem:orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
        }
        int i = orderItemMapper.insertBatch(orderItemList);
        //减库存
        reduceProductStock(orderItemList);
        //购物车中清空已下单的商品
        cleanCart(cartList);
        //返回orderVO
        OrderVO orderVO = assembleOrderVO(order, orderItemList, shippingId);
        return ServerResponse.serverResponseBySuccess(orderVO);
    }

    /*
    * 构建OrderVO
    * */
    private OrderVO assembleOrderVO(Order order,List<OrderItem> orderItemList,Integer shippingId){
        OrderVO orderVO = new OrderVO();
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        for(OrderItem orderItem:orderItemList){
            OrderItemVO orderItemVO = assembleOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVoList(orderItemVOList);
        orderVO.setImageHost(PropertiesUtils.IMAGE_HOST);
        Shipping shipping = shippingMapper.selectByPrimaryKey(shippingId);
        if(shipping!=null){
            orderVO.setReceiverName(shipping.getReceiverName());
            orderVO.setShippingId(shippingId);
            ShippingVO shippingVO = assembleShippingVO(shipping);
            orderVO.setShippingVo(shippingVO);
        }
        orderVO.setStatus(order.getStatus());
        Const.OrderStatusEnum orderStatusEnum = Const.OrderStatusEnum.codeOf(order.getStatus());
        if(orderStatusEnum!=null){
            orderVO.setStatusDesc(orderStatusEnum.getDesc());
        }
        orderVO.setPostage(0);
        orderVO.setPayment(order.getPayment());
        orderVO.setPaymentType(order.getPaymentType());
        Const.PaymentEnum paymentEnum = Const.PaymentEnum.codeOf(order.getPaymentType());
        if(paymentEnum!=null){
            orderVO.setPaymentTypeDesc(paymentEnum.getDesc());
        }
        orderVO.setOrderNo(order.getOrderNo());
        return orderVO;
    }
    /*
    * shipping----->shippingVO
    * */
    private ShippingVO assembleShippingVO(Shipping shipping){
        ShippingVO shippingVO = new ShippingVO();
        if(shipping!=null){
            shippingVO.setReceiverAddress(shipping.getReceiverAddress());
            shippingVO.setReceiverCity(shipping.getReceiverCity());
            shippingVO.setReceiverDistrict(shipping.getReceiverDistrict());
            shippingVO.setReceiverMobile(shipping.getReceiverMobile());
            shippingVO.setReceiverName(shipping.getReceiverName());
            shippingVO.setReceiverProvice(shipping.getReceiverProvice());
            shippingVO.setReceiverPhone(shipping.getReceiverPhone());
            shippingVO.setReceiverZip(shipping.getReceiverZip());
        }
       return shippingVO;
    }



    /*
    * List<OrderItem>----->List<OrderItemVO>
    * */
    private OrderItemVO assembleOrderItemVO(OrderItem orderItem){
        OrderItemVO orderItemVO = new OrderItemVO();
        if(orderItem!=null){
            orderItemVO.setOrderNo(orderItem.getOrderNo());
            orderItemVO.setProductId(orderItem.getProductId());
            orderItemVO.setCreateTime(DateUtils.dateToStr(orderItem.getCreateTime()));
            orderItemVO.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
            orderItemVO.setProductImage(orderItem.getProductImage());
            orderItemVO.setProductName(orderItem.getProductName());
            orderItemVO.setQuantity(orderItem.getQuantity());
            orderItemVO.setTotalPrice(orderItem.getTotlePrice());
        }
        return orderItemVO;
    }
    /*
    * 购物车中清空已下单的商品
    * */
    private void cleanCart(List<Cart> cartList){
        if(cartList!=null&&cartList.size()>0){
            cartMapper.batchDelete(cartList);
        }
    }

    /*
    * 扣库存
    * */
    private void reduceProductStock(List<OrderItem> orderItemList){
        if(orderItemList!=null||orderItemList.size()>0){
            for(OrderItem orderItem:orderItemList){
                Integer productId = orderItem.getProductId();
                Integer quantity = orderItem.getQuantity();
                Product product = productMapper.selectByPrimaryKey(productId);
                product.setStock(product.getStock()-quantity);
                productMapper.updateByPrimaryKey(product);
            }
        }
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
        //保存订单
        int insert = orderMapper.insert(order);
        if(insert>0){
            return order;
        }
        return null;
    }
    /*
    * 定义一个生成订单编号的规则
    * */
    private Long genetateOrderNO(){
        return System.currentTimeMillis()+new Random().nextInt(100);
    }


    @Override
    public ServerResponse cancel(Integer userId, Long orderNo) {
        if(orderNo==null){
            return ServerResponse.serverResponseByError("订单编号不能为空");
        }
        Order order = orderMapper.findOrderByUserIdAndOrderNo(userId, orderNo);
        if(order==null){
            return ServerResponse.serverResponseByError("订单不存在");
        }
        if(order.getStatus()!=Const.OrderStatusEnum.ORDER__UN_PAY.getCode()){
            return ServerResponse.serverResponseByError("订单不可取消");
        }
        order.setStatus(Const.OrderStatusEnum.ORDER__CLOSED.getCode());
        int i = orderMapper.updateByPrimaryKey(order);
        if(i>0){
            return ServerResponse.serverResponseBySuccess();
        }
        return ServerResponse.serverResponseByError("订单取消失败");
    }

    @Override
    public ServerResponse get_order_cart_product(Integer userId) {
        List<Cart> cartList = cartMapper.findCartListByUserIdAndCheck(userId);
        ServerResponse serverResponse = getOrderItem(cartList);
        if(!serverResponse.isSuccess()){
            return serverResponse;
        }
        List<OrderItem> orderItemList = (List<OrderItem>)serverResponse.getData();
        if(orderItemList==null||orderItemList.size()==0){
            return ServerResponse.serverResponseByError("购物车为空");
        }
        //List<OrderItem>---->CartOrderItemVO
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        for(OrderItem orderItem:orderItemList){
            OrderItemVO orderItemVO = assembleOrderItemVO(orderItem);
            orderItemVOList.add(orderItemVO);
        }
        CartOrderItemVO cartOrderItemVO = new CartOrderItemVO();
        cartOrderItemVO.setOrderItemVOList(orderItemVOList);
        cartOrderItemVO.setImageHost(PropertiesUtils.IMAGE_HOST);
        cartOrderItemVO.setTotalPrice(getOrderTotalPrice(orderItemList));
        return ServerResponse.serverResponseBySuccess(cartOrderItemVO);
    }

    @Override
    public ServerResponse list(Integer userId,Integer pageNum,Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = new ArrayList<>();
        if(userId==null){
            orderList = orderMapper.selectAll();
        }else {
            orderList = orderMapper.findOrderByUserId(userId);
        }
        //orderList转orderVO
        if(orderList==null){
            return ServerResponse.serverResponseByError("未查询到订单信息");
        }
        List<OrderVO> orderVOList = new ArrayList<>();
        for(Order order:orderList){
            List<OrderItem> orderItems = orderItemMapper.fintOrderItemByOrderNo(order.getOrderNo());
            OrderVO orderVO = assembleOrderVO(order, orderItems, order.getShippingId());
            orderVOList.add(orderVO);
        }
        PageInfo pageInfo = new PageInfo(orderVOList);
        return ServerResponse.serverResponseBySuccess(pageInfo);
    }
    @Override
    public ServerResponse detail(Long orderNo) {
        if(orderNo==null){
            return ServerResponse.serverResponseByError("订单编号不能为空");
        }
        Order order = orderMapper.findOrderByOrderNo(orderNo);
        if(order==null){
            return ServerResponse.serverResponseByError("订单不存在");
        }
        List<OrderItem> orderItems = orderItemMapper.fintOrderItemByOrderNo(order.getOrderNo());
        OrderVO orderVO = assembleOrderVO(order, orderItems, order.getShippingId());
        return ServerResponse.serverResponseBySuccess(orderVO);
    }

}
