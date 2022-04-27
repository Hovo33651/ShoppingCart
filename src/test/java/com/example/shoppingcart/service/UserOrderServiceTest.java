package com.example.shoppingcart.service;

import com.example.shoppingcart.entity.*;
import com.example.shoppingcart.repository.ProductRepository;
import com.example.shoppingcart.repository.UserOrderRepository;
import com.example.shoppingcart.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserOrderServiceTest {

    @Autowired
    private UserOrderService userOrderService;
    @Autowired
    private UserOrderRepository userOrderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    private User user;
    private Product product;


    @BeforeEach
    void beforeEach() throws ParseException {
        createUser();
        createProduct();
    }



    @Test
    void saveOrder() {
        int count = product.getCountInStock();
        UserOrder order = userOrderService.save(user, product, 5);
        assertEquals(1,userOrderRepository.count());
        assertEquals(1,order.getId());
        assertEquals(user,order.getUser());
        assertEquals(count-5,product.getCountInStock());
    }

    @Test
    void findCurrentUserOrders() {
        userOrderService.save(user, product, 1);
        List<UserOrder> currentUserOrders = userOrderService.findUserOrders(user);
        assertFalse(currentUserOrders.isEmpty());
        assertEquals(1,currentUserOrders.size());
    }

    @Test
    void changeOrderStatus() {
        UserOrder order = userOrderService.save(user, product, 1);
        assertEquals(OrderStatus.AWAITING_FOR_PAYMENT,order.getStatus());
        UserOrder updatedOrder = userOrderService.changeStatus(order, "DELIVERED");
        assertEquals(OrderStatus.DELIVERED,updatedOrder.getStatus());

    }

    @Test
    void deleteOrder() {
        UserOrder order = userOrderService.save(user, product, 1);
        assertEquals(1,userOrderRepository.count());
        userOrderService.delete(order);
        assertEquals(0,userOrderRepository.count());


    }



    private void createProduct() throws ParseException {
        product = Product.builder()
                .id(1)
                .name("asd")
                .description("asd")
                .createdDate(sdf.parse("04.04.1994"))
                .type(ProductType.ELECTRONICS)
                .countInStock(15)
                .build();
        productRepository.save(product);
    }

    private void createUser() throws ParseException {
        user = User.builder()
                .id(1)
                .name("John")
                .surname("Lennon")
                .email("lennon@gmail.com")
                .password("12345678")
                .birthday(sdf.parse("04.04.1994"))
                .build();
        userRepository.save(user);
    }
}