package com.example.shoppingcart.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserEndpointTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void saveUser_When_Filled_Correctly() throws Exception {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("name", "poxos");
        objectNode.put("surname", "poxosyan");
        objectNode.put("birthday", "04.04.1994");
        objectNode.put("email", "poxos@mail.com");
        objectNode.put("password", "poxos1234");

        mockMvc.perform(post("http://localhost:8080/user/")
                        .contentType(APPLICATION_JSON)
                        .content(objectNode.toString()))
                .andExpect(status().isCreated());
    }

    @Test
    void saveUser_When_Filled_Incorrectly() throws Exception {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("name", "");
        objectNode.put("surname", "");
        objectNode.put("birthday", "04.04.1994");
        objectNode.put("email", "poxos@mail.com");
        objectNode.put("password", "poxos1234");

        mockMvc.perform(post("http://localhost:8080/user/")
                        .contentType(APPLICATION_JSON)
                        .content(objectNode.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void saveUser_When_Email_Already_Exists() throws Exception {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("name", "poxos");
        objectNode.put("surname", "poxosyan");
        objectNode.put("birthday", "04.04.1994");
        objectNode.put("email", "poxos@mail.com");
        objectNode.put("password", "poxos1234");

        mockMvc.perform(post("http://localhost:8080/user/")
                        .contentType(APPLICATION_JSON)
                        .content(objectNode.toString()))
                .andExpect(status().isCreated());

        mockMvc.perform(post("http://localhost:8080/user/")
                        .contentType(APPLICATION_JSON)
                        .content(objectNode.toString()))
                .andExpect(status().isConflict());
    }

    @Test
    void userLogin_When_Filled_Correctly_And_User_Exists() throws Exception {
        ObjectNode createUserRequest = new ObjectMapper().createObjectNode();
        createUserRequest.put("name", "poxos");
        createUserRequest.put("surname", "poxosyan");
        createUserRequest.put("birthday", "04.04.1994");
        createUserRequest.put("email", "poxos@mail.com");
        createUserRequest.put("password", "poxos1234");

        mockMvc.perform(post("http://localhost:8080/user/")
                        .contentType(APPLICATION_JSON)
                        .content(createUserRequest.toString()))
                .andExpect(status().isCreated());

        ObjectNode loginUserRequest = new ObjectMapper().createObjectNode();
        loginUserRequest.put("email", "poxos@mail.com");
        loginUserRequest.put("password", "poxos1234");

        mockMvc.perform(post("http://localhost:8080/user/auth")
                        .contentType(APPLICATION_JSON)
                        .content(loginUserRequest.toString()))
                .andExpect(status().isOk());
    }

    @Test
    void userLogin_When_Empty_Fields() throws Exception {
        ObjectNode createUserRequest = new ObjectMapper().createObjectNode();
        createUserRequest.put("name", "poxos");
        createUserRequest.put("surname", "poxosyan");
        createUserRequest.put("birthday", "04.04.1994");
        createUserRequest.put("email", "poxos@mail.com");
        createUserRequest.put("password", "poxos1234");

        mockMvc.perform(post("http://localhost:8080/user/")
                        .contentType(APPLICATION_JSON)
                        .content(createUserRequest.toString()))
                .andExpect(status().isCreated());

        ObjectNode loginUserRequest = new ObjectMapper().createObjectNode();
        loginUserRequest.put("email", "");
        loginUserRequest.put("password", "poxos1234");

        mockMvc.perform(post("http://localhost:8080/user/auth")
                        .contentType(APPLICATION_JSON)
                        .content(loginUserRequest.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void userLogin_When_Wrong_Data() throws Exception {
        ObjectNode createUserRequest = new ObjectMapper().createObjectNode();
        createUserRequest.put("name", "poxos");
        createUserRequest.put("surname", "poxosyan");
        createUserRequest.put("birthday", "04.04.1994");
        createUserRequest.put("email", "poxos@mail.com");
        createUserRequest.put("password", "poxos1234");

        mockMvc.perform(post("http://localhost:8080/user/")
                        .contentType(APPLICATION_JSON)
                        .content(createUserRequest.toString()))
                .andExpect(status().isCreated());

        ObjectNode loginUserRequest = new ObjectMapper().createObjectNode();
        loginUserRequest.put("email", "armen@mail.com");
        loginUserRequest.put("password", "poxos1234");

        mockMvc.perform(post("http://localhost:8080/user/auth")
                        .contentType(APPLICATION_JSON)
                        .content(loginUserRequest.toString()))
                .andExpect(status().isUnauthorized());
    }
}