package com.example.shoppingcart.service;

import com.example.shoppingcart.entity.Order;
import com.example.shoppingcart.entity.OrderStatus;
import com.example.shoppingcart.entity.Product;
import com.example.shoppingcart.repository.OrderRepository;
import com.example.shoppingcart.repository.ProductRepository;
import com.example.shoppingcart.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Order Service
 * This service class contains all business logic that concern orders
 */
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;


    /**
     * method to find customer orders
     * @param currentUser -> current customer
     * @return -> list of current customer orders
     */
    public List<Order> findCurrentUserOrders(CurrentUser currentUser) {
        return orderRepository.findOrdersByUser_Id(currentUser.getUser().getId());
    }

    /**
     * method to save a new order
     * @param currentUser -> current customer
     * @param productId -> product, chosen by current customer
     * @param countOfProduct -> count of products in order
     * @return -> if product exists, creates the order, saves, returns 200 with the order, if not returns 404
     */
    public ResponseEntity<Order> saveOrder(CurrentUser currentUser, int productId, int countOfProduct) {
        Optional<Product> productById = productRepository.findById(productId);
        if(!productById.isPresent() || productById.get().getCountInStock() == 0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Product product = productById.get();
        Order order = Order.builder()
                .product(product)
                .user(currentUser.getUser())
                .status(OrderStatus.AWAITING_FOR_PAYMENT)
                .build();
        Order newOrder = orderRepository.save(order);
        product.setCountInStock(product.getCountInStock() - countOfProduct);
        productRepository.save(product);
        return ResponseEntity.ok(newOrder);

    }

    /**
     * method to change order status(ONLY FOR ADMIN)
     * @param orderId -> order id
     * @param status -> new status, chosen by ADMIN
     * @return if ok, returns 200, if not returns 404;
     */
    public ResponseEntity<String> changeOrderStatus(int orderId, String status) {
        Optional<Order> optOrder = orderRepository.findById(orderId);
        if(!optOrder.isPresent() || optOrder.get().getStatus().equals(OrderStatus.DELIVERED)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Order order = optOrder.get();
        order.setStatus(OrderStatus.valueOf(status));
        orderRepository.save(order);
        return ResponseEntity.ok().build();
    }

    /**
     * method to remove the order
     * @param orderId -> order id
     * @return -> if removed, returns 200, if not returns 404
     */
    public ResponseEntity<String> deleteOrderById(int orderId) {
        Optional<Order> optOrder = orderRepository.findById(orderId);
        if(!optOrder.isPresent()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        orderRepository.deleteById(orderId);
        return ResponseEntity.ok().build();
    }
}
