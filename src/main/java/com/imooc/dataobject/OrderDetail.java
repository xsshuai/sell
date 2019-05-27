package com.imooc.dataobject;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class OrderDetail {
    //订单详情Id
    @Id
    private String detailId;
    //订单Id
    private String orderId;
    //商品Id
    private String productId;
    //商品名称
    private String productName;
    //商品单价
    private BigDecimal productPrice;
    //商品数量
    private Integer productQuantity;
    //商品小图
    private String productIcon;
    //创建时间
    private Date createTime;
    //修改时间
    private Date updateTime;
}
