package com.imooc.repository;

import com.imooc.dataobject.OrderDetail;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDetailRepositoryTest {
    @Autowired
    private OrderDetailRepository repository;

    @Test
    public void save(){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setDetailId("123456-2");
        orderDetail.setOrderId("123456");
        orderDetail.setProductId("12346");
        orderDetail.setProductName("酸菜鱼");
        orderDetail.setProductPrice(new BigDecimal(20));
        orderDetail.setProductIcon("http://xxxx.jpg");
        orderDetail.setProductQuantity(2);
        OrderDetail result = repository.save(orderDetail);
        Assert.assertNotNull(result);
    }
    @Test
    public void findByOrderId() {
        String orderId = "123456";
        List<OrderDetail> result = repository.findByOrderId(orderId);
        Assert.assertNotEquals(0,result.size());
    }
}