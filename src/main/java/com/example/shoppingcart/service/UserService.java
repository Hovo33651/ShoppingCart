package com.example.shoppingcart.service;

import com.example.shoppingcart.dto.request.CreateUserRequestDto;
import com.example.shoppingcart.dto.response.UserResponseDto;
import com.example.shoppingcart.entity.User;
import com.example.shoppingcart.entity.UserType;
import com.example.shoppingcart.repository.UserRepository;
import com.example.shoppingcart.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * User Controller
 * This service class contains all business logic that concern user
 */

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * method to find an optional user by email
     * @param email -> email, from UserLoginRequestDto
     * @return -> optional user
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * method to save a new user
     * @param createUserRequestDto -> new user data
     * saves the new user as customer
     * generates a jw token for the customer
     * @return -> UserResponseDto
     */
    public UserResponseDto saveUserFromRequest(CreateUserRequestDto createUserRequestDto) {
        User user = modelMapper.map(createUserRequestDto, User.class);
        user.setType(UserType.CUSTOMER);
        user.setPassword(passwordEncoder.encode(createUserRequestDto.getPassword()));
        userRepository.save(user);
        UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
        userResponseDto.setToken(jwtTokenUtil.generateToken(userResponseDto.getEmail()));
        return userResponseDto;

    }
}
