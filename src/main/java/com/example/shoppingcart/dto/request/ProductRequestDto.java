package com.example.shoppingcart.dto.request;

import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "What is your product name")
    @Size(min = 3,max = 15, message = "Check your product name")
    private String name;
    @NotBlank(message = "What is your product name")
    @Size(min = 10,max = 250, message = "Check your product description. Should be at least 50 characters and maximum 250.")
    private String description;
    private String type;
    @NotNull(message = "What is your product price")
    private double price;
    @Min(value = 1,message = "You can't add a product you don't have")
    private int countInStock;
    private String createdDate;

}
