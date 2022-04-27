package com.example.shoppingcart.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_orders")
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    private User user;
    @OneToOne
    private Product product;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    @DateTimeFormat
    private LocalDateTime createdDate;

}
