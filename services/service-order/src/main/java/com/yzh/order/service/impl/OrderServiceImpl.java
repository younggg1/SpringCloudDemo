package com.yzh.order.service.impl;

import com.yzh.order.bean.Order;
import com.yzh.order.service.OrderService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public Order createOrder(Long productId,Long userId){
        Order order = new Order();
        order.setId(1L);
        //TODO 总金额
        order.setTotalAmount(new BigDecimal("0"));
        order.setUserId(userId);
        //TODO 远程调用
        order.setProductList(null);
        order.setAddress("北京");
        order.setNickName("张三");
        return null;
    }
}
