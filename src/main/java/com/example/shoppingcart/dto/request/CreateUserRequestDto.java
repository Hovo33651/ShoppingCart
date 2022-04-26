package com.example.shoppingcart.dto.request;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequestDto {

    private String name;
    private String surname;
    private Date birthday;
    private String email;
    private String password;

}
