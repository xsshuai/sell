package com.imooc.repository;

import com.imooc.dataobject.OrderMaster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OderMasterRepositoryTest {

    @Autowired
    private OrderMasterRepository repository;

    @Test
    public void save(){
        OrderMaster orderMaster = new OrderMaster();
        orderMaster.setOrderId("123456");
        orderMaster.setBuyerName("黑黑小帅");
        orderMaster.setBuyerOpenid("XSS0710");
        orderMaster.setBuyerPhone("15626416540");
        orderMaster.setBuyerAddress("联航路1158号万达信息股份有限公司");
        orderMaster.setOrderAmount(new BigDecimal(3200));
        OrderMaster result = repository.save(orderMaster);
        Assert.assertNotNull(result);
    }
    @Test
    public void findByBuyerOpenid() {
        String buyerOpenid = "XSS0710";
        Pageable pageable = new PageRequest(0,2);
        Page<OrderMaster> result = repository.findByBuyerOpenid(buyerOpenid,pageable);
        Assert.assertNotNull(result);
    }
}