package com.example.kvbagshopapi.services.impl;

import com.example.kvbagshopapi.dtos.OrderDto;
import com.example.kvbagshopapi.entities.Order;
import com.example.kvbagshopapi.repositories.OrderRepository;
import com.example.kvbagshopapi.services.ICartService;
import com.example.kvbagshopapi.services.IOrderService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements IOrderService {
    private final OrderRepository orderRepository;
    private final ICartService iCartService;
    private ModelMapper modelMapper;

    @Override
    public List<OrderDto> getAllOrder(Long userId){
        List<Order> orders = orderRepository.findAllByUserId(userId);
        return orders.stream()
                .map((order) -> (modelMapper.map(order, OrderDto.class)))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDto newOrder(OrderDto orderDto){
        Order order = modelMapper.map(orderDto, Order.class);
        order.setCreateAt(new Date(new Date().getTime()));
        Order savedOrder = orderRepository.save(order);
        OrderDto saveCategoryDto = modelMapper.map(savedOrder, OrderDto.class);
        return saveCategoryDto;

    }

    @Override
    public void deleteOrder(Order order) {
        System.out.println(order.getId());
        orderRepository.delete(order);
    }
}
