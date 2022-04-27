package com.example.shoppingcart.service;

import com.example.shoppingcart.dto.request.CreateUserRequestDto;
import com.example.shoppingcart.dto.request.UserLoginRequestDto;
import com.example.shoppingcart.dto.response.UserResponseDto;
import com.example.shoppingcart.entity.User;
import com.example.shoppingcart.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    private CreateUserRequestDto createUserRequestDto;

    @BeforeEach
    void beforeEach() {
        createUserRequestDto = CreateUserRequestDto.builder()
                .name("John")
                .surname("Lennon")
                .email("lennon@gmail.com")
                .password("12345678")
                .birthday("04.04.1994")
                .build();
    }

    @Test
    void save_User_From_Request() throws ParseException {
        userService.saveUserFromRequest(createUserRequestDto);
        Optional<User> optUser = userRepository.findByEmail(createUserRequestDto.getEmail());
        assertTrue(optUser.isPresent());
        assertEquals(createUserRequestDto.getEmail(),optUser.get().getEmail());
        assertEquals(1,userRepository.count());
    }


    @Test
    void login() throws ParseException {
        userService.saveUserFromRequest(createUserRequestDto);
        Optional<User> optUser = userRepository.findByEmail(createUserRequestDto.getEmail());
        assertTrue(optUser.isPresent());
        User user = optUser.get();
        UserResponseDto userResponseDto = userService.login(user);
        assertNotNull(userResponseDto.getToken());
    }
}