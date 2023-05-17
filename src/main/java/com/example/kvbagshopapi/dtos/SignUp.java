package com.example.kvbagshopapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SignUp {
    private long id;

    private String name;

    private String userName;

    private String password;

    private String phoneNumber;

    private String email;
}
