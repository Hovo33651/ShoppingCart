package com.example.shoppingcart.endpoint;

import com.example.shoppingcart.dto.request.CreateUserRequestDto;
import com.example.shoppingcart.dto.request.UserLoginRequestDto;
import com.example.shoppingcart.dto.response.UserResponseDto;
import com.example.shoppingcart.entity.User;
import com.example.shoppingcart.service.UserService;
import com.example.shoppingcart.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * User controller which receives register and login requests
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserEndpoint {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;


    /**
     * endpoint to sign in
     * @param userLoginRequestDto -> user dto which contains user email and password
     * if email is not empty and user with that email exists, checks the password, if not returns 401
     * if passwords don't match, returns 401
     * else generates jw token and returns the user
     */
    @PostMapping("/auth")
    public ResponseEntity<UserResponseDto> userLogin(@RequestBody UserLoginRequestDto userLoginRequestDto) {
        if (Strings.isNotEmpty(userLoginRequestDto.getEmail())) {
            Optional<User> optUser = userService.findByEmail(userLoginRequestDto.getEmail());
            if (!optUser.isPresent() || !passwordEncoder.matches(userLoginRequestDto.getPassword(), optUser.get().getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
            User user = optUser.get();
            return ResponseEntity.ok(UserResponseDto.builder()
                    .name(user.getName())
                    .surname(user.getSurname())
                    .email(user.getEmail())
                    .token(jwtTokenUtil.generateToken(user.getEmail()))
                    .type(user.getType())
                    .build());
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    /**
     * endpoint to get registered
     * @param createUserRequestDto -> new user data
     * @return -> user response dto
     */

    @PostMapping("/")
    public @ResponseBody
    UserResponseDto saveUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        return userService.saveUserFromRequest(createUserRequestDto);
    }

}
