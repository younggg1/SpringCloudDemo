package com.yzh.order.feign.fallback;

import java.math.BigDecimal;

import com.yzh.order.feign.ProductFeignClient;
import com.yzh.product.bean.Product;
import org.springframework.stereotype.Component;


@Component
public class ProductFeignClientFallback implements ProductFeignClient {
    @Override
    public Product getProductById(Long id) {
        System.out.println("兜底回调....");
        Product product = new Product();
        product.setId(id);
        product.setPrice(new BigDecimal("0"));
        product.setProductName("未知商品");
        product.setNum(0);

        return product;
    }
}
