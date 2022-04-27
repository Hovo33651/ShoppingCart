package com.example.shoppingcart.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginRequestDto {

    @NotBlank(message = "What is your email address")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "Please input valid email address")
    private String email;
    @NotBlank(message = "You forgot to create a password")
    @Size(min = 8, message = "Please input valid password. It should be minimum 8 characters")
    private String password;


}
