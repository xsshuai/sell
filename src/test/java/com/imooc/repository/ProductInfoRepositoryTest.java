package com.imooc.repository;

import com.imooc.dataobject.ProductInfo;
import com.imooc.enums.ProductCategoryEnum;
import com.imooc.enums.ProductStatusEnum;
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
public class ProductInfoRepositoryTest {

    @Autowired
    private ProductInfoRepository repository;
    @Test
    public void findByProductStatus() {

        List<ProductInfo> list = repository.findByProductStatus(0);
        Assert.assertNotEquals(0,list.size());
    }
    @Test
    public void findOne(){
        ProductInfo result = repository.findById("12345").get();
        Assert.assertNotNull(result);

    }
    @Test
    public void findAll(){

    }
    @Test
    public void save(){
        ProductInfo product = new ProductInfo();
        product.setProductId("12346");
        product.setProductName("酸菜鱼");
        product.setProductStock(200);
        product.setProductPrice(new BigDecimal(20));
        product.setProductStatus(ProductStatusEnum.UP.getCode());
        product.setProductIcon("http://xxxx.jpg");
        product.setProductDescription("酸菜鱼");
        product.setCategoryType(ProductCategoryEnum.MAN_ONLY.getCode());
        ProductInfo result = repository.save(product);
    }
}