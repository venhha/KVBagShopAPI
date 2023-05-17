package com.example.kvbagshopapi.entities;

import com.example.kvbagshopapi.utils.Role;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "user")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String password;

    private String avatar;

    @Column(length = 10)
    private String phoneNumber;

    @Email
    private String email;

    @Column
    private Boolean isActive = true;

    @Column
    private Date createAt= new Date(new Date().getTime());

    @Column
    private Date updateAt= new Date(new Date().getTime());

    @ElementCollection(fetch = FetchType.EAGER)
    List<Role> roles;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<Cart> carts;

    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL
    )
    private List<Order> orders;

}
