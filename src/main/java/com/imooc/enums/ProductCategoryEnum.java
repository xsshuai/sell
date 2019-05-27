package com.imooc.enums;

import lombok.Getter;

/*商品类目*/
@Getter
public enum ProductCategoryEnum implements CodeEnum{
    DIGITAL(3,"数码商品"),
    BEST_SELLERS(4,"热销商品"),
    MAN_ONLY(5,"男士专享");
    private Integer code;
    private String message;

    ProductCategoryEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
