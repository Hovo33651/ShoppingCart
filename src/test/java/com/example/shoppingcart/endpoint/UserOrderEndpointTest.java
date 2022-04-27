package com.example.shoppingcart.endpoint;

import com.example.shoppingcart.service.UserOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserOrderEndpointTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    void showCustomerOrders() {

    }

    @Test
    void findCustomerOrderById() {
    }

    @Test
    void saveNewOrder() {
    }

    @Test
    void deleteOrderByOrderId() {
    }

    @Test
    void changeOrderStatus() {
    }
}