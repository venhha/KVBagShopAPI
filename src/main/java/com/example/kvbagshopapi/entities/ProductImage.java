package com.example.kvbagshopapi.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "product_image")
public class ProductImage implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private long id;

    @Column(nullable = false)
    private String image;

    private Date createAt = new Date(new Date().getTime());

    @ManyToOne(fetch = FetchType.LAZY)
    // sử dụng FetchType.LAZY vì nếu không chúng ta sẽ quay lại việc tìm nạp EAGER
    private Product product;
}
