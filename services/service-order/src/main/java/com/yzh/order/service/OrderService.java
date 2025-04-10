package com.yzh.order.service;

import com.yzh.order.bean.Order;

public interface OrderService {
    Order createOrder(Long userId, Long productId);
}
