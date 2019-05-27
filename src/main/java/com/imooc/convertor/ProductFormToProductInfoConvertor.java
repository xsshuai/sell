package com.imooc.convertor;

import com.imooc.dataobject.ProductInfo;
import com.imooc.form.ProductForm;
import org.springframework.beans.BeanUtils;

public class ProductFormToProductInfoConvertor {

    public static ProductInfo convert(ProductForm productForm){

        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(productForm,productInfo);
        return productInfo;
    }
}
