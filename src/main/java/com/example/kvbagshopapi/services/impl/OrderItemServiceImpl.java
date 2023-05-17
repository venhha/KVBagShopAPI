package com.example.kvbagshopapi.services.impl;

import com.example.kvbagshopapi.dtos.OrderItemDto;
import com.example.kvbagshopapi.entities.OrderItem;
import com.example.kvbagshopapi.repositories.OrderItemRepository;
import com.example.kvbagshopapi.services.IOrderItemService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderItemServiceImpl implements IOrderItemService {
    private OrderItemRepository orderItemRepository;
    private ModelMapper modelMapper;

    @Override
    public List<OrderItemDto> getALlOrderItemByOrderId(Long orderId){
        List<OrderItem> orderItems = orderItemRepository.findAllByOrderId(orderId);
        return orderItems.stream()
                .map((oderItem) -> modelMapper.map(oderItem, OrderItemDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemDto addOrderItem(OrderItem orderItem){
//        OrderItem orderItem = modelMapper.map(orderItemDto, OrderItem.class);
        OrderItem savedCategory = orderItemRepository.save(orderItem);
        OrderItemDto saveOrderItemDto = modelMapper.map(savedCategory, OrderItemDto.class);
        return saveOrderItemDto;
    }
}
