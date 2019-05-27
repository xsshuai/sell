package com.imooc.service.impl;

import com.imooc.convertor.OrderMasterToOrderDTOConverter;
import com.imooc.dataobject.OrderDetail;
import com.imooc.dataobject.OrderMaster;
import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDTO;
import com.imooc.dto.OrderDTO;
import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.repository.OrderDetailRepository;
import com.imooc.repository.OrderMasterRepository;
import com.imooc.service.OrderService;
import com.imooc.service.ProductService;
import com.imooc.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private ProductService productService;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private OrderMasterRepository orderMasterRepository;

    @Override
    @Transactional
    public OrderDTO create(OrderDTO orderDTO) {
        orderDTO.setOrderId(KeyUtil.getUniqueKey());
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);
        List<CartDTO> cartDTOList = new ArrayList<>();
        //1、查询商品
        for (OrderDetail orderDetail:orderDTO.getOrderDetailList()){
            CartDTO cartDTO = new CartDTO(orderDetail.getProductId(),orderDetail.getProductQuantity());
            cartDTOList.add(cartDTO);
            ProductInfo productInfo = productService.findOne(orderDetail.getProductId());
            if (productInfo == null){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIT);
            }
            if (productInfo.getProductStock()<orderDetail.getProductQuantity()){
                throw new SellException(ResultEnum.PRODUCT_UNDER_STOCK);
            }
            //计算总价
            orderAmount = productInfo.getProductPrice()
                    .multiply(new BigDecimal(orderDetail.getProductQuantity()))
                    .add(orderAmount);
            orderDTO.setOrderAmount(orderAmount);
            //写入订单详情数据库
            orderDetail.setOrderId(orderDTO.getOrderId());
            orderDetail.setDetailId(KeyUtil.getUniqueKey());
            BeanUtils.copyProperties(productInfo,orderDetail);
            orderDetailRepository.save(orderDetail);
        }
        //写入订单主表数据库
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId(orderDTO.getOrderId());
        orderMaster.setOrderAmount(orderDTO.getOrderAmount());
        orderMaster.setBuyerPhone(orderDTO.getBuyerPhone());
        orderMaster.setBuyerName(orderDTO.getBuyerName());
        orderMaster.setBuyerAddress(orderDTO.getBuyerAddress());
        orderMaster.setBuyerOpenid(orderDTO.getBuyerOpenid());
        orderMasterRepository.save(orderMaster);

        //减库存
//        cartDTOList = orderDTO.getOrderDetailList().stream()
//                .map(e -> new CartDTO(e.getProductId(),e.getProductQuantity()))
//                .collect(Collectors.toList());
        productService.reduceStock(cartDTOList);
        System.out.println(orderDTO.getOrderId());
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO findOne(String orderId) {
        OrderDTO orderDTO = new OrderDTO();
        OrderMaster orderMaster = orderMasterRepository.findById(orderId).get();
        if (orderMaster == null){
            throw new SellException(ResultEnum.ORDER_NOT_EXIT);
        }
        List<OrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (orderDetailList.size() == 0){
            throw new SellException(ResultEnum.ORDER_NO_PRODUCT);
        }
        orderDTO.setOrderId(orderMaster.getOrderId());
        orderDTO.setBuyerName(orderMaster.getBuyerName());
        orderDTO.setBuyerPhone(orderMaster.getBuyerPhone());
        orderDTO.setBuyerAddress(orderMaster.getBuyerAddress());
        orderDTO.setBuyerOpenid(orderMaster.getBuyerOpenid());
        orderDTO.setOrderStatus(orderMaster.getOrderStatus());
        orderDTO.setPayStatus(orderMaster.getPayStatus());
        orderDTO.setOrderAmount(orderMaster.getOrderAmount());
        orderDTO.setCreateTime(orderMaster.getCreateTime());
        orderDTO.setUpdateTime(orderMaster.getUpdateTime());
        orderDTO.setOrderDetailList(orderDetailList);
        return orderDTO;
    }

    @Override
    @Transactional
    public Page<OrderDTO> findList(String buyerOpenid, Pageable pageable) {
        Page<OrderMaster> pageOrderMaster = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
        List<OrderMaster> orderMasterList = pageOrderMaster.getContent();
        List<OrderDTO> orderDTOList = OrderMasterToOrderDTOConverter.convert(orderMasterList);
        Page<OrderDTO> orderDTOPage = new PageImpl<OrderDTO>(orderDTOList,pageable,pageOrderMaster.getTotalElements());
        return orderDTOPage;
    }

    @Override
    public Page<OrderDTO> findByOrderStatus(Integer orderStatus, Pageable pageable) {
        Page<OrderMaster> pageOrderMaster = orderMasterRepository.findByOrderStatus(orderStatus,pageable);
        List<OrderMaster> orderMasterList = pageOrderMaster.getContent();
        List<OrderDTO> orderDTOList = OrderMasterToOrderDTOConverter.convert(orderMasterList);
        Page<OrderDTO> orderDTOPage = new PageImpl<OrderDTO>(orderDTOList,pageable,pageOrderMaster.getTotalElements());
        return orderDTOPage;
    }

    @Override
    @Transactional
    public OrderDTO cancel(OrderDTO orderDTO) {
        //判断订单状态
        OrderMaster orderMaster = orderMasterRepository.findById(orderDTO.getOrderId()).get();
        if (orderMaster == null){
            log.error("【取消订单】订单不存在，不能取消，orderId={},orderStatus={}",
                    orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_NOT_EXIT);
        }
        if (orderMaster.getOrderStatus().equals(OrderStatusEnum.CANCEL.getCode())){
            log.error("【取消订单】订单已取消，不能重复取消，orderId={},orderStatus={}",
                    orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_HAS_CANCELED);
        }
        if (orderMaster.getOrderStatus().equals(OrderStatusEnum.FINISH.getCode())){
            log.error("【取消订单】订单已完成，不能取消订单，orderId={},orderStatus={}",
                    orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_HAS_FINISHED);
        }
        //订单取消
        orderMaster.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if (result == null){
            log.error("【取消订单】订单取消失败，orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_CANCEL_FAIL);
        }
        //返回库存
        List<OrderDetail> orderDetailList = orderDTO.getOrderDetailList();
        if (orderDetailList.size() ==0){
            log.error("【取消订单】订单中无商品信息，orderDTO={}",orderDTO);
            throw new SellException(ResultEnum.ORDER_NO_PRODUCT);
        }
        List<CartDTO> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e ->new CartDTO(e.getProductId(),e.getProductQuantity()))
                .collect(Collectors.toList());
        productService.addStock(cartDTOList);
        //退款
        if (orderDTO.getPayStatus().equals(PayStatusEnum.PAYED)){
            //TODO
        }
        log.debug("【取消订单】订单取消成功，orderMaster={}",orderMaster);
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO finish(OrderDTO orderDTO) {
        //判断订单状态
        OrderMaster orderMaster = orderMasterRepository.findById(orderDTO.getOrderId()).get();
        if (orderMaster == null){
            log.error("【订单完结】订单不存在，不能完结，orderId={},orderStatus={}",
                    orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_NOT_EXIT);
        }
        if (orderMaster.getOrderStatus().equals(OrderStatusEnum.CANCEL)){
            log.error("【订单完结】订单已被取消，不能完结，orderId={},orderStatus={}",
                    orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_HAS_CANCELED);
        }
        if (orderMaster.getOrderStatus().equals(OrderStatusEnum.FINISH)){
            log.error("【订单完结】订单已被完结，不能重复完结，orderId={},orderStatus={}",
                    orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_HAS_FINISHED);
        }
        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISH.getCode());
        orderMaster.setOrderStatus(OrderStatusEnum.FINISH.getCode());
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if (result == null){
            log.error("【订单完结】订单完结失败，orderMaster={},",orderMaster);
            throw new SellException(ResultEnum.ORDER_FINISH_FAIL);
        }
        log.debug("【订单完结】订单完结成功，orderMaster={},",orderMaster);
        return orderDTO;
    }

    @Override
    @Transactional
    public OrderDTO paid(OrderDTO orderDTO) {
        //判断订单状态
        OrderMaster orderMaster = orderMasterRepository.findById(orderDTO.getOrderId()).get();
        if (orderMaster == null){
            log.error("【订单支付】订单不存在，不能支付，orderId={},orderStatus={}",
                    orderDTO.getOrderId(),orderDTO.getOrderStatus());
            throw new SellException(ResultEnum.ORDER_NOT_EXIT);
        }
        if (orderMaster.getPayStatus().equals(PayStatusEnum.PAYED)){
            log.error("【订单支付】订单已支付，不能重复支付，orderId={},payStatus={}",
                    orderDTO.getOrderId(),orderDTO.getPayStatus());
            throw new SellException(ResultEnum.ORDER_HAS_PAID);
        }
        //修改订单状态
        orderDTO.setPayStatus(PayStatusEnum.PAYED.getCode());
        orderMaster.setPayStatus(PayStatusEnum.PAYED.getCode());
        OrderMaster result = orderMasterRepository.save(orderMaster);
        if (result == null){
            log.error("【订单支付】订单支付失败，orderMaster={}",orderMaster);
            throw new SellException(ResultEnum.ORDER_PAID_FAIL);
        }
        log.debug("【订单支付】订单支付成功，orderMaster={}",orderMaster);
        return orderDTO;
    }

    //卖家端
    //查询订单列表

    @Override
    public Page<OrderDTO> findList(Pageable pageable) {
        Page<OrderMaster> pageOrderMaster = orderMasterRepository.findAll(pageable);
        List<OrderMaster> orderMasterList = pageOrderMaster.getContent();
        List<OrderDTO> orderDTOList = OrderMasterToOrderDTOConverter.convert(orderMasterList);
        Page<OrderDTO> orderDTOPage = new PageImpl<OrderDTO>(orderDTOList,pageable,pageOrderMaster.getTotalElements());
        return orderDTOPage;
    }
}
