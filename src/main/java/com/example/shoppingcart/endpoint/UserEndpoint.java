package com.example.shoppingcart.endpoint;

import com.example.shoppingcart.dto.request.CreateUserRequestDto;
import com.example.shoppingcart.dto.request.UserLoginRequestDto;
import com.example.shoppingcart.dto.response.UserResponseDto;
import com.example.shoppingcart.entity.User;
import com.example.shoppingcart.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * User controller which receives register and login requests
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Slf4j
public class UserEndpoint {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    /**
     * endpoint to sign in
     *
     * @param userLoginRequestDto -> user dto which contains user email and password
     * @param bindingResult       -> checks if there are any errors about filling the fields
     *                            if email is not empty and user with that email exists, checks the password, if not returns 401
     *                            if passwords don't match, returns 401
     *                            else generates jw token and returns the user
     */
    @PostMapping("/auth")
    public ResponseEntity<?> login(@RequestBody @Valid UserLoginRequestDto userLoginRequestDto, BindingResult bindingResult) {
        log.info("Request from {} to sign in", userLoginRequestDto.getEmail());
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errors.add(error.getDefaultMessage());
            }
            log.warn("No user found with email {} and request password", userLoginRequestDto.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Optional<User> optUser = userService.findByEmail(userLoginRequestDto.getEmail());
        if (!optUser.isPresent() || !passwordEncoder.matches(userLoginRequestDto.getPassword(), optUser.get().getPassword())) {
            log.warn("No user found with email {} and request password", userLoginRequestDto.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        log.warn("User {} got a token and signed in", userLoginRequestDto.getEmail());
        return ResponseEntity.ok(userService.login(optUser.get()));


    }


    /**
     * endpoint to get registered
     *
     * @param bindingResult        -> checks if there are any errors about filling the fields
     * @param createUserRequestDto -> new user data
     * @return -> if there are errors while filling fields, returns 400
     *            if email already exists, returns 409
     *            else returns 200 and user response dto
     */
    @PostMapping("/")
    public ResponseEntity<?> register(@RequestBody @Valid CreateUserRequestDto createUserRequestDto, BindingResult bindingResult) throws ParseException {
        log.info("Request to get registered: email {}", createUserRequestDto.getEmail());
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Optional<User> optUser = userService.findByEmail(createUserRequestDto.getEmail());
        if (optUser.isPresent()) {
            log.warn("User with email {} already exists", createUserRequestDto.getEmail());
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        UserResponseDto userResponseDto = userService.save(createUserRequestDto);
        log.info("User {} has been registered successfully", userResponseDto.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDto);

    }

}
