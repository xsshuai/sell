package com.imooc.controller;

import com.imooc.convertor.ProductFormToProductInfoConvertor;
import com.imooc.dataobject.ProductCategory;
import com.imooc.dataobject.ProductInfo;
import com.imooc.enums.ProductStatusEnum;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.SellException;
import com.imooc.form.ProductForm;
import com.imooc.service.CategoryService;
import com.imooc.service.ProductService;
import com.imooc.utils.KeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seller/product")
@Slf4j
public class SellerProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 商品列表
     * @param page 第几页 第1页开始
     * @param size 每页有多少条数据
     * @return
     */
    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page",defaultValue = "1" )Integer page,
                             @RequestParam(value = "size",defaultValue = "4")Integer size,
                             Map<String,Object> map){
        PageRequest pageRequest = new PageRequest(page-1,size);
        Page<ProductInfo> productInfoPage = productService.findAll(pageRequest);
        List<ProductCategory> categoryList = categoryService.findAll();
        map.put("categoryList",categoryList);
        map.put("productInfoPage",productInfoPage);
        map.put("currentPage",page);
        map.put("size",size);
        return new ModelAndView("product/list",map);
    }

    /**
     * 商品上架
     * @param productId
     * @return
     */
    @GetMapping("/on_sale")
    public ModelAndView onSale(@RequestParam("productId")String productId,
                               Map<String,Object> map){
        try {
            productService.onSale(productId);
        }catch (SellException e){
            log.error("【商品上架】商品上架异常，{}",e);
            map.put("msg", e.getMessage());
            map.put("url","/sell/seller/product/list");
            return new ModelAndView("common/error",map);
        }
        map.put("msg", ResultEnum.PRODUCT_UP_SUCCESS.getMessage());
        map.put("url","/sell/seller/product/list");
        return new ModelAndView("common/success");
    }

    /**
     * 商品下架
     * @param productId
     * @return
     */
    @GetMapping("/off_sale")
    public ModelAndView offSale(@RequestParam("productId")String productId,
                                Map<String,Object> map){
        try {
            productService.offSale(productId);
        }catch (SellException e){
            log.error("【商品下架】商品下架异常，{}",e);
            map.put("msg", e.getMessage());
            map.put("url","/sell/seller/product/list");
            return new ModelAndView("common/error",map);
        }
        map.put("msg", ResultEnum.PRODUCT_DOWN_SUCCESS.getMessage());
        map.put("url","/sell/seller/product/list");
        return new ModelAndView("common/success");
    }

    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "productId",required = false) String productId,
                              Map<String,Object> map){
        if (!StringUtils.isEmpty(productId)){
            ProductInfo productInfo = productService.findOne(productId);
            map.put("productInfo",productInfo);
        }
        //查询所有的类目
        List<ProductCategory> categoryList = categoryService.findAll();
        map.put("categoryList", categoryList);
        return new ModelAndView("product/index");
    }

    /**
     * 商品新增/更新
     * @param productForm
     * @param bindingResult
     * @return
     */
    @PostMapping("/save")
    public ModelAndView save(@Valid ProductForm productForm,
                             BindingResult bindingResult,
                             Map<String,Object> map){
        if (bindingResult.hasErrors()){
            log.error("【商品添加/更新】参数不正确，orderForm={}",productForm);
            map.put("msg",bindingResult.getFieldError().getDefaultMessage());
            map.put("url","/sell/seller/product/index");
            return new ModelAndView("common/error",map);
        }
        ProductInfo productInfo = ProductFormToProductInfoConvertor.convert(productForm);
        ProductInfo product = new ProductInfo();
        if (StringUtils.isEmpty(productInfo.getProductId())){
            BeanUtils.copyProperties(productInfo,product);
            product.setProductId(KeyUtil.getUniqueKey());
            product.setProductStatus(ProductStatusEnum.UP.getCode());
            map.put("msg", ResultEnum.PRODUCT_DAA_SUCCESS.getMessage());
        }else {
            product = productService.findOne(productInfo.getProductId());
            product.setProductName(productInfo.getProductName());
            product.setProductPrice(productInfo.getProductPrice());
            product.setProductStock(productInfo.getProductStock());
            product.setProductDescription(productInfo.getProductDescription());
            product.setProductIcon(productInfo.getProductIcon());
            product.setCategoryType(productInfo.getCategoryType());
            product.setUpdateTime(new Date());
            map.put("msg", ResultEnum.PRODUCT_UPDATE_SUCCESS.getMessage());
        }
        try {
            productService.save(product);
        }catch (SellException e){
            log.error("【商品新增/更新】商品新增/更新异常，{}",e);
            map.put("msg",e.getMessage());
            map.put("url","/sell/seller/product/index");
            return new ModelAndView("common/error",map);
        }
        map.put("url","/sell/seller/product/list");
        return new ModelAndView("common/success");
    }
}
