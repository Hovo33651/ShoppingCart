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

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");


    /**
     * method to save a new user
     *
     * @param createUserRequestDto -> new user data
     *                             saves the new user as customer
     *                             generates a jw token for the customer
     * @return -> if email exists, returns 409, if did save returns UserResponseDto
     */
    public UserResponseDto save(CreateUserRequestDto createUserRequestDto) throws ParseException {
        User user = User.builder()
                .name(createUserRequestDto.getName())
                .surname(createUserRequestDto.getSurname())
                .email(createUserRequestDto.getEmail())
                .birthday(sdf.parse(createUserRequestDto.getBirthday()))
                .build();
        user.setType(UserType.CUSTOMER);
        user.setPassword(passwordEncoder.encode(createUserRequestDto.getPassword()));
        userRepository.save(user);
        UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
        userResponseDto.setToken(jwtTokenUtil.generateToken(userResponseDto.getEmail()));
        return userResponseDto;

    }

    /**
     * endpoint to sign in
     *
     * @param user -> customer
     *             creates UserResponseDto, generates token
     * @return -> UserResponseDto
     */
    public UserResponseDto login(User user) {
        return UserResponseDto.builder()
                .name(user.getName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .token(jwtTokenUtil.generateToken(user.getEmail()))
                .type(user.getType())
                .build();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
