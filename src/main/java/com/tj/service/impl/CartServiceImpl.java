package com.tj.service.impl;

import com.tj.common.Const;
import com.tj.common.ServerResponse;
import com.tj.dao.CartMapper;
import com.tj.dao.ProductMapper;
import com.tj.pojo.Cart;
import com.tj.pojo.Product;
import com.tj.service.ICartService;
import com.tj.utils.BigDecimalUtils;
import com.tj.vo.CartProductVO;
import com.tj.vo.CartVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {
    
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;
    @Override
    public ServerResponse add(Integer userId, Integer productId, Integer count) {
        if(productId==null||count==null){
            return ServerResponse.serverResponseByError("参数错误");
        }
        Product product = productMapper.selectByPrimaryKey(productId);
        if(product==null){
            return ServerResponse.serverResponseByError("要添加的商品不存在");
        }

        //根据userId,productId查购物车里的商品信息
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        //如果cart不存在，说明是添加，如果存在，说明是更新
        if(cart==null){
            //添加
            Cart cart1 = new Cart();
            cart1.setUserId(userId);
            cart1.setProductId(productId);
            cart1.setQuantity(count);
            cart1.setChecked(Const.CartCheckEnum.CART_CHECK_ENUM.getCode());
            cartMapper.insert(cart1);
        }else {
            //更新
            cart.setQuantity(cart.getQuantity()+count);
            cartMapper.updateByPrimaryKey(cart);
        }
        CartVO cartVO = getCartVO(userId);
        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    @Override
    public ServerResponse list(Integer userId) {
        CartVO cartVO = getCartVO(userId);
        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    @Override
    public ServerResponse update(Integer userId, Integer productId, Integer count) {
        if(productId==null||count==null){
            return ServerResponse.serverResponseByError("参数错误");
        }
        //根据userId,productId查购物车里的商品信息
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if(cart!=null){
            cart.setQuantity(count);
            cartMapper.updateByPrimaryKey(cart);
        }
        CartVO cartVO = getCartVO(userId);
        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    @Override
    public ServerResponse delete_product(Integer userId, String productIds) {
        if(productIds==null){
            return ServerResponse.serverResponseByError("参数错误");
        }
        List<Integer> productIdList = new ArrayList<>();
        String[] split = productIds.split(",");
        if(split!=null&&split.length>0){
            for(String s:split){
                Integer productId = Integer.parseInt(s);
                productIdList.add(productId);
            }
        }
        int i = cartMapper.deleteByUserIdAndProductIds(userId, productIdList);
        CartVO cartVO = getCartVO(userId);
        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    @Override
    public ServerResponse select(Integer userId, Integer productId,Integer check) {
        cartMapper.selectOrUnselectProduct(userId,productId,check);
        CartVO cartVO = getCartVO(userId);
        return ServerResponse.serverResponseBySuccess(cartVO);
    }

    @Override
    public ServerResponse get_cart_product_count(Integer userId) {
        Integer quatity = cartMapper.get_cart_product_count(userId);
        return ServerResponse.serverResponseBySuccess(quatity);
    }

    //cart转cartVO
    public CartVO getCartVO(Integer userId){
        CartVO cartVO = new CartVO();
        //1.根据userId查询List<cart>
        List<Cart> carts = cartMapper.selectCartByUserId(userId);
        //2、List<cart>--->List<CartVO>
        List<CartProductVO> cartProductVOList  = new ArrayList<>();
        //购物车总价格
        BigDecimal cartTotalPrice = new BigDecimal("0");
        if(carts!=null&&carts.size()>0){
            for(Cart cart:carts){
                CartProductVO cartProductVO = new CartProductVO();
                cartProductVO.setId(cart.getId());
                cartProductVO.setUserId(cart.getUserId());
                cartProductVO.setQuantity(cart.getQuantity());
                cartProductVO.setProductChecked(cart.getChecked());
                //通过productId查商品信息
                Product product = productMapper.selectByPrimaryKey(cart.getProductId());
                if(product!=null){
                    cartProductVO.setProductId(product.getId());
                    cartProductVO.setProductMainImage(product.getMainImage());
                    cartProductVO.setProductName(product.getName());
                    cartProductVO.setProductPrice(product.getPrice());
                    cartProductVO.setProductStatus(product.getStatus());
                    cartProductVO.setProductStock(product.getStock());
                    cartProductVO.setProductSubtitle(product.getSubtitle());
                    //判断商品库存
                    int stock = product.getStock();
                    int limitProductCount=0;
                    if(stock>cart.getQuantity()){
                        limitProductCount=cart.getQuantity();
                        cartProductVO.setLimitQuantity("LIMIT_NUM_SUCCESS");
                    }else {
                        //库存不足，更新购物车中的数量
                        limitProductCount=stock;
                        Cart cart1 = new Cart();
                        cart1.setQuantity(stock);
                        cart1.setId(cart.getId());
                        cart1.setProductId(cart.getProductId());
                        cart1.setChecked(cart.getChecked());
                        cart1.setUserId(userId);
                        cartMapper.updateByPrimaryKey(cart1);
                        cartProductVO.setLimitQuantity("LIMIT_NUM_FAIL");
                    }
                    cartProductVO.setQuantity(limitProductCount);
                    //计算商品总价=单价*数量
                    cartProductVO.setProductTotalPrice(BigDecimalUtils.mul(product.getPrice().doubleValue(), Double.valueOf(cartProductVO.getQuantity())));
                }
                if(cartProductVO.getProductChecked()==Const.CartCheckEnum.CART_CHECK_ENUM.getCode()){
                    cartTotalPrice = BigDecimalUtils.add(cartTotalPrice.doubleValue(),cartProductVO.getProductTotalPrice().doubleValue());
                }
                cartProductVOList.add(cartProductVO);
            }
        }
        //3、计算购物车总价格
        cartVO.setCartProductVOList(cartProductVOList);
        cartVO.setCartTotalPrice(cartTotalPrice);
        //4、判断购物车是否全选
        int i = cartMapper.isCheckedAll(userId);
        if(i>0){
            cartVO.setAllChecked(false);
        }else {
            cartVO.setAllChecked(true);
        }
        //5、返回结果
        return cartVO;
    }

}
