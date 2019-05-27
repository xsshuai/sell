package com.imooc.enums;

import lombok.Getter;

@Getter
public enum PayStatusEnum implements CodeEnum{
    UNPAYED(0,"未支付"),
    PAYED(1,"已支付");

    private Integer code;
    private  String message;

    PayStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
