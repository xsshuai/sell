package com.imooc.service.impl;

import com.imooc.dataobject.ProductInfo;
import com.imooc.enums.ProductCategoryEnum;
import com.imooc.enums.ProductStatusEnum;
import com.imooc.service.ProductService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {

    @Autowired
    private ProductService productService;
    @Test
    public void findOne() {
        ProductInfo result = productService.findOne("12345");
        Assert.assertNotNull(result);
    }

    @Test
    public void findUpAll() {
        List<ProductInfo> result = productService.findUpAll();
        Assert.assertNotEquals(0,result.size());
    }

    @Test
    public void findAll() {
        PageRequest request = new PageRequest(0,2);
        Page<ProductInfo> result = productService.findAll(request);
        Assert.assertNotNull(request);
    }

    @Test
    public void save() {
        ProductInfo product = new ProductInfo();
        product.setProductId("12346");
        product.setProductName("酸菜鱼");
        product.setProductStock(200);
        product.setProductPrice(new BigDecimal(20.0));
        product.setProductStatus(ProductStatusEnum.UP.getCode());
        product.setProductIcon("http://xxxx.jpg");
        product.setProductDescription("酸菜鱼");
        product.setCategoryType(ProductCategoryEnum.MAN_ONLY.getCode());
        ProductInfo result = productService.save(product);
        Assert.assertNotNull(result);
    }
}