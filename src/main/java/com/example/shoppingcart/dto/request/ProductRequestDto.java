package com.example.shoppingcart.dto.request;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    private String name;
    private String description;
    private String type;
    private double price;
    private int countInStock;
    private Date createdDate;

}
