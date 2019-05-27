package com.imooc.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    SUCCESS(0,"成功"),
    PARAM_ERROR(1,"参数不正确"),
    OPENID_UNEQUAL_ORDERID(7,"openid与orderId不一致"),
    OPENID_NOT_EXIT(8,"openid不存在"),
    CART_EMPTY(9,"购物车为空"),
    PRODUCT_NOT_EXIT(10,"商品不存在"),
    PRODUCT_UNDER_STOCK(11,"商品库存不足"),
    ORDER_NOT_EXIT(12,"订单不存在"),
    ORDER_NO_PRODUCT(13,"订单下没有商品"),
    ORDER_HAS_CANCELED(14,"订单已被取消，不能执行此操作"),
    ORDER_HAS_FINISHED(15,"订单已完成，不能执行此操作"),
    ORDER_CANCEL_FAIL(16,"订单取消失败"),
    ORDER_FINISH_FAIL(17,"订单完结失败"),
    ORDER_CANCEL_SUCCESS(18,"订单取消成功"),
    ORDER_FINISH_SUCCESS(19,"订单完结成功"),
    ORDER_HAS_PAID(20,"订单已被支付，不能执行此操作"),
    ORDER_PAID_SUCCESS(21,"订单支付成功"),
    ORDER_PAID_FAIL(22,"订单支付失败"),
    WECHAT_ERROR(23,"微信方面错误"),
    PRODUCT_STATUS_ERROR(24,"商品状态错误"),
    PRODUCT_UP_SUCCESS(25,"商品上架成功"),
    PRODUCT_DOWN_SUCCESS(26,"商品下架成功"),
    PRODUCT_DAA_SUCCESS(27,"商品新增成功"),
    PRODUCT_UPDATE_SUCCESS(28,"商品更新成功"),
    CATEGORY_ADD_SUCCESS(29,"商品类目新增成功"),
    CATEGORY_UPDATE_SUCCESS(29,"商品类目更新成功"),
    ;

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
