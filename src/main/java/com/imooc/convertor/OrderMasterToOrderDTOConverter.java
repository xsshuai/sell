package com.imooc.convertor;

import com.imooc.dataobject.OrderMaster;
import com.imooc.dto.OrderDTO;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMasterToOrderDTOConverter {
    public static OrderDTO convert(OrderMaster orderMaster){
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(orderMaster.getOrderId());
        orderDTO.setBuyerName(orderMaster.getBuyerName());
        orderDTO.setBuyerPhone(orderMaster.getBuyerPhone());
        orderDTO.setBuyerAddress(orderMaster.getBuyerAddress());
        orderDTO.setBuyerOpenid(orderMaster.getBuyerOpenid());
        orderDTO.setOrderAmount(orderMaster.getOrderAmount());
        orderDTO.setOrderStatus(orderMaster.getOrderStatus());
        orderDTO.setPayStatus(orderMaster.getPayStatus());
        orderDTO.setCreateTime(orderMaster.getCreateTime());
        orderDTO.setUpdateTime(orderMaster.getUpdateTime());
        return orderDTO;
    }
    public static List<OrderDTO> convert(List<OrderMaster>orderMasterList){
        return orderMasterList.stream().map(e ->convert(e)).collect(Collectors.toList());
    }
}
