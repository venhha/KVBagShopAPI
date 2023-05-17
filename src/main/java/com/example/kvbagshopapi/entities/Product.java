package com.example.kvbagshopapi.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "product")
public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private int sold = 0;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Column(nullable = false, updatable = false)
    private Date createAt = new Date(new Date().getTime());

    private Date updateAt= new Date(new Date().getTime());

    @ManyToOne(fetch = FetchType.LAZY)
    // sử dụng FetchType.LAZY vì nếu không chúng ta sẽ quay lại việc tìm nạp EAGER
    private Category category;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL
    )
    private List<ProductImage> productImages;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL
    )
    private List<Cart> carts;

    @OneToMany(
            mappedBy = "product",
            cascade = CascadeType.ALL
    )
    private List<OrderItem> orderItems;

}
