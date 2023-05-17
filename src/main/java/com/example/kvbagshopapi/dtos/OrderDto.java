package com.example.kvbagshopapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private long id;

    private String address;

    private String phoneNumber;

    private int status;

    private double total;

    private UserDto user;

    private Date createAt;
    private List<OrderItemDto> orderItems;
}
