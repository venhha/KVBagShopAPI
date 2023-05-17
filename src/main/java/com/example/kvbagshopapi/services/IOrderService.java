package com.example.kvbagshopapi.services;

import com.example.kvbagshopapi.dtos.OrderDto;
import com.example.kvbagshopapi.entities.Order;

import java.util.List;

public interface IOrderService {
    List<OrderDto> getAllOrder(Long userId);

    OrderDto newOrder(OrderDto orderDto);

//    void deleteOrder(Long orderId);

    void deleteOrder(Order order);
}
