package com.example.shoppingcart.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    @Enumerated(EnumType.STRING)
    private ProductType type;
    private double price;
    private int countInStock;
    private Date createdDate;
    private LocalDate updatedDate;
}
