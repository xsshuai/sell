package com.imooc.controller;

import com.imooc.dto.OrderDTO;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/pay")
@Slf4j
public class PayerController {

    @Autowired
    private OrderService orderService;
    @RequestMapping("/create")
    public void create(@RequestParam("orderId") String orderId,
                       @RequestParam("returnUrl") String returnUrl){
        OrderDTO orderDTO = orderService.findOne(orderId);
        if (orderDTO == null){
            log.error("【订单支付】订单不存在，orderId={}",orderId);
            throw new SellException(ResultEnum.ORDER_NOT_EXIT);
        }

    }
}
