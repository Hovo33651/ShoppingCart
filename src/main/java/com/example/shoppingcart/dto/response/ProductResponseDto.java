package com.example.shoppingcart.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class ProductResponseDto {

    private int id;
    private String name;
    private String description;
    private String type;
    private double price;
    private int countInStock;
    private Date createdDate;
    private LocalDate updatedDate;
}
