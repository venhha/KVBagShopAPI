package com.example.kvbagshopapi.services;

import com.example.kvbagshopapi.dtos.OrderItemDto;
import com.example.kvbagshopapi.entities.OrderItem;

import java.util.List;

public interface IOrderItemService {
    List<OrderItemDto> getALlOrderItemByOrderId(Long orderId);


    OrderItemDto addOrderItem(OrderItem orderItem);
}
