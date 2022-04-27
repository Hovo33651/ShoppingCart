package com.example.shoppingcart.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @WithMockUser(authorities = "ADMIN")
    @Test
    void saveProduct_OK() throws Exception {
        ObjectNode saveProductRequest = new ObjectMapper().createObjectNode();
        saveProductRequest.put("name", "telephone");
        saveProductRequest.put("description", "very good telephone");
        saveProductRequest.put("type", "ELECTRONICS");
        saveProductRequest.put("price", "15000");
        saveProductRequest.put("countInStock", "10");
        saveProductRequest.put("createdDate", "04.04.2020");

        mockMvc.perform(post("http://localhost:8080/product/")
                        .contentType(APPLICATION_JSON)
                        .content(saveProductRequest.toString()))
                .andExpect(status().isCreated());

    }

    @WithMockUser(authorities = "ADMIN")
    @Test
    void saveProduct_When_Filled_Incorrectly() throws Exception {
        ObjectNode saveProductRequest = new ObjectMapper().createObjectNode();
        saveProductRequest.put("name", "");
        saveProductRequest.put("description", "very good telephone");
        saveProductRequest.put("type", "ELECTRONICS");
        saveProductRequest.put("price", "15000");
        saveProductRequest.put("countInStock", "10");
        saveProductRequest.put("createdDate", "04.04.2020");

        mockMvc.perform(post("http://localhost:8080/product/")
                        .contentType(APPLICATION_JSON)
                        .content(saveProductRequest.toString()))
                .andExpect(status().isBadRequest());

    }

    @WithMockUser(authorities = "CUSTOMER")
    @Test
    void saveProduct_When_User_Is_Not_Admin() throws Exception {
        ObjectNode saveProductRequest = new ObjectMapper().createObjectNode();
        saveProductRequest.put("name", "telephone");
        saveProductRequest.put("description", "very good telephone");
        saveProductRequest.put("type", "ELECTRONICS");
        saveProductRequest.put("price", "15000");
        saveProductRequest.put("countInStock", "10");
        saveProductRequest.put("createdDate", "04.04.2020");

        mockMvc.perform(post("http://localhost:8080/product/")
                        .contentType(APPLICATION_JSON)
                        .content(saveProductRequest.toString()))
                .andExpect(status().isForbidden());
    }

}