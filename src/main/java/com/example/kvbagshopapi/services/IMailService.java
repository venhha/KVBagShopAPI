package com.example.kvbagshopapi.services;

import com.example.kvbagshopapi.Model.Mail;
import com.example.kvbagshopapi.dtos.OrderDto;
import com.example.kvbagshopapi.dtos.OrderItemDto;
import com.example.kvbagshopapi.dtos.UserDto;

import java.util.List;

public interface IMailService {
    public void sendEmail(Mail mail);
    public void sendOrderMail(UserDto userDto, OrderDto orderDto, List<OrderItemDto> orderItemDtos);
}
