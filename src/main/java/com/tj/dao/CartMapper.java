package com.tj.dao;

import com.tj.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tj_cart
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tj_cart
     *
     * @mbg.generated
     */
    int insert(Cart record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tj_cart
     *
     * @mbg.generated
     */
    Cart selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tj_cart
     *
     * @mbg.generated
     */
    List<Cart> selectAll();

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tj_cart
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(Cart record);

    /*
     * 根据userId,productId查购物车里的商品信息
     * */
    Cart selectCartByUserIdAndProductId(@Param("userId") Integer userId,
                                              @Param("productId") Integer productId);

    /*
    * 根据userId查询List<Cart>
    * */
    List<Cart> selectCartByUserId(Integer userId);
    /*
    * 统计购物车是否全选
    * 返回值大于0，未全选
    * */
    int isCheckedAll(Integer userId);
    /*
    * 根据userId和productId删除购物车商品
    * */
    int deleteByUserIdAndProductIds(@Param("userId") Integer userId,
                                    @Param("productIds")List<Integer> productIdList);
    /*
     * 统计购物车是否被选中
     * */
    int selectOrUnselectProduct(@Param("userId") Integer userId,
                                @Param("productId") Integer productId,
                                @Param("check")Integer check);
    /*
    * 统计购物车的数量
    * */
    Integer get_cart_product_count(Integer userId);
    /*
    * 查询购物车中已选中的商品
    * */
    List<Cart> findCartListByUserIdAndCheck(Integer userId);
    /*
    * 批量删除购物车商品
    * */
    int batchDelete(List<Cart> cartList);

}