package com.yzh.product.controller;


import com.yzh.product.bean.Product;
import com.yzh.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    //查询商品
    @GetMapping("/product/{id}")
    public Product getProduct(@PathVariable("id") Long productId){
    Product product = productService.getProductById(productId);
    return product;
    }
}
