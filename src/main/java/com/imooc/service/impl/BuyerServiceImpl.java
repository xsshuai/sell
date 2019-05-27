package com.imooc.service.impl;

import com.imooc.dataobject.OrderMaster;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.service.BuyerService;
import com.imooc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuyerServiceImpl implements BuyerService {

    @Autowired
    private OrderService orderService;

    @Override
    public OrderDTO findOneOrder(String openid, String orderId) {
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null){
            log.error("【查询订单】订单不存在");
            throw new SellException(ResultEnum.ORDER_NOT_EXIT);
        }
        if (!orderDTO.getBuyerOpenid().equals(openid)){
            log.error("【查询订单】订单ID和openid不一致，openid={}，orderId={}",openid,orderId);
            throw new SellException(ResultEnum.OPENID_UNEQUAL_ORDERID);
        }
        return orderDTO;

    }

    @Override
    public OrderDTO cancelOneOrder(String openid, String orderId) {
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null){
            log.error("【取消订单】订单不存在");
            throw new SellException(ResultEnum.ORDER_NOT_EXIT);
        }
        if (!orderDTO.getBuyerOpenid().equals(openid)){
            log.error("【取消订单】订单ID和openid不一致，openid={}，orderId={}",openid,orderId);
            throw new SellException(ResultEnum.OPENID_UNEQUAL_ORDERID);
        }
        return orderService.cancel(orderDTO);
    }
}
