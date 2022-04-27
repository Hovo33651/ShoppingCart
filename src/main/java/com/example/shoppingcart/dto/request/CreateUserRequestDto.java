package com.example.shoppingcart.dto.request;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequestDto {

    @NotBlank(message = "What is your name")
    @Size(min = 3,max = 10, message = "Check your name")
    private String name;
    @NotBlank(message = "What is your name")
    @Size(min = 5,max = 12, message = "Check your surname")
    private String surname;
    @NotBlank(message = "You forgot to fill the birthday field")
    private String birthday;
    @NotBlank(message = "What is your email address")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$",
            message = "Please input valid email address")
    private String email;
    @NotBlank(message = "You forgot to create a password")
    @Size(min = 8, message = "Please input valid password. It should be minimum 8 characters")
    private String password;

}
