package com.imooc.service.impl;

import com.imooc.dataobject.ProductInfo;
import com.imooc.dto.CartDTO;
import com.imooc.enums.ProductStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.repository.ProductInfoRepository;
import com.imooc.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductInfoRepository repository;
    @Override
    public ProductInfo findOne(String productId) {
        Optional<ProductInfo> optionalProductInfo = repository.findById(productId);
        ProductInfo product = optionalProductInfo.isPresent()?optionalProductInfo.get():null;
        return product;
    }

    @Override
    public List<ProductInfo> findUpAll() {
        List<ProductInfo> list = repository.findByProductStatus(ProductStatusEnum.UP.getCode());
        return list;
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return repository.save(productInfo);
    }

    @Override
    @Transactional
    public void addStock(List<CartDTO> list) {

        for (CartDTO cartDTO:list){
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).get();
            if (productInfo == null){

                throw new SellException(ResultEnum.PRODUCT_NOT_EXIT);
            }
            Integer stock = productInfo.getProductStock()+cartDTO.getProductQuantity();
            productInfo.setProductStock(stock);
            repository.save(productInfo);
        }
    }

    @Override
    @Transactional
    public void reduceStock(List<CartDTO> list) {

        for (CartDTO cartDTO:list){
            ProductInfo productInfo = repository.findById(cartDTO.getProductId()).get();
            if (productInfo == null){

                throw new SellException(ResultEnum.PRODUCT_NOT_EXIT);
            }
            Integer stock = productInfo.getProductStock()-cartDTO.getProductQuantity();
            if (stock < 0){

                throw new SellException(ResultEnum.PRODUCT_UNDER_STOCK);
            }
            productInfo.setProductStock(stock);
            repository.save(productInfo);
        }
    }

    @Override
    public ProductInfo onSale(String productId) {
        Optional<ProductInfo> optionalProductInfo = repository.findById(productId);
        ProductInfo product = optionalProductInfo.isPresent()?optionalProductInfo.get():null;
        if (product == null){
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIT);
        }
        if (product.getProductStatus() == ProductStatusEnum.UP.getCode()){
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        product.setProductStatus(ProductStatusEnum.UP.getCode());
        return repository.save(product);
    }

    @Override
    public ProductInfo offSale(String productId) {
        Optional<ProductInfo> optionalProductInfo = repository.findById(productId);
        ProductInfo product = optionalProductInfo.isPresent()?optionalProductInfo.get():null;
        if (product == null){
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIT);
        }
        if (product.getProductStatus() == ProductStatusEnum.DOWN.getCode()){
            throw new SellException(ResultEnum.PRODUCT_STATUS_ERROR);
        }
        product.setProductStatus(ProductStatusEnum.DOWN.getCode());
        return repository.save(product);
    }

}
