package com.example.ecommerce.service;

import java.util.List;

import com.example.ecommerce.dto.OrderDto;
import com.example.ecommerce.dto.OrderRequest;

public interface OrderService {
    OrderDto createOrder(OrderRequest orderRequest);
    OrderDto getOrderById(Long id);
    List<OrderDto> getAllOrders();
    List<OrderDto> getOrdersByUserId(Long userId);
    void deleteOrder(Long id);
}
