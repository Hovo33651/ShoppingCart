package com.example.shoppingcart.service;

import com.example.shoppingcart.entity.OrderStatus;
import com.example.shoppingcart.entity.Product;
import com.example.shoppingcart.entity.User;
import com.example.shoppingcart.entity.UserOrder;
import com.example.shoppingcart.repository.UserOrderRepository;
import com.example.shoppingcart.repository.ProductRepository;
import com.example.shoppingcart.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Order Service
 * This service class contains all business logic that concern orders
 */
@Service
@RequiredArgsConstructor
public class UserOrderService {

    private final UserOrderRepository orderRepository;
    private final ProductRepository productRepository;



    /**
     * method to save a new order
     *
     * @param user    -> current customer
     * @param product        -> product, chosen by current customer
     * @param countOfProduct -> count of products in order
     * @return -> if product exists, creates the order, saves, returns 200 with the order, if not returns 404
     */
    public UserOrder saveOrder(User user, Product product, int countOfProduct) {
        UserOrder order = UserOrder.builder()
                .product(product)
                .user(user)
                .status(OrderStatus.AWAITING_FOR_PAYMENT)
                .createdDate(LocalDateTime.now())
                .build();
        UserOrder newOrder = orderRepository.save(order);
        product.setCountInStock(product.getCountInStock() - countOfProduct);
        productRepository.save(product);
        return newOrder;
    }



    /**
     * method to find customer orders
     *
     * @param user -> current customer
     * @return -> list of current customer orders
     */
    public List<UserOrder> findCurrentUserOrders(User user) {
        return orderRepository.findOrdersByUser_Id(user.getId());
    }



    /**
     * method to change order status(ONLY FOR ADMIN)
     *
     * @param order     -> order
     * @param newStatus -> new status, chosen by ADMIN
     * @return if ok, returns 200, if not returns 404;
     */
    public UserOrder changeOrderStatus(UserOrder order, String newStatus) {
        order.setStatus(OrderStatus.valueOf(newStatus));
        return orderRepository.save(order);

    }


    /**
     * method to remove the order
     *
     * @param order -> order that will be deleted
     */
    public void deleteOrder(UserOrder order) {
        orderRepository.delete(order);
    }


    public Optional<UserOrder> findById(int orderId) {
        return orderRepository.findById(orderId);
    }
}
