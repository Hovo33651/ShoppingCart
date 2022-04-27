package com.example.shoppingcart.endpoint;

import com.example.shoppingcart.entity.OrderStatus;
import com.example.shoppingcart.entity.Product;
import com.example.shoppingcart.entity.UserOrder;
import com.example.shoppingcart.security.CurrentUser;
import com.example.shoppingcart.service.ProductService;
import com.example.shoppingcart.service.UserOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Order controller which receives all order requests and sends necessary responses
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
@Slf4j
public class UserOrderEndpoint {

    private final UserOrderService orderService;
    private final ProductService productService;


    /**
     * endpoint to show customer his orders
     *
     * @param currentUser -> current customer
     * @return -> list of customer orders
     */
    @GetMapping("")
    public List<UserOrder> showCustomerOrders(@AuthenticationPrincipal CurrentUser currentUser) {
        log.info("User {}: request to see customer orders.", currentUser.getUser().getEmail());
        return orderService.findUserOrders(currentUser.getUser());
    }

    /**
     * @param orderId     -> customer order id
     * @param currentUser -> customer as principal
     * @return -> if order doesn't exist returns 404, if found return 200 with the order
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserOrder> findById(@PathVariable("id") int orderId,
                                              @AuthenticationPrincipal CurrentUser currentUser) {
        log.info("User {}: request to see a single order", currentUser.getUser().getEmail());
        Optional<UserOrder> optOrder = orderService.findById(orderId);
        return optOrder.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }


    /**
     * endpoint to save a new order, if product exists in stock
     *
     * @param currentUser    -> current customer
     * @param productId      -> order product id
     * @param countOfProduct -> count of products in order
     * @return -> if saved, returns the order, if not returns 404
     */
    @PostMapping("")
    public ResponseEntity<UserOrder> save(@AuthenticationPrincipal CurrentUser currentUser,
                                          @RequestParam("id") int productId,
                                          @RequestParam("count") int countOfProduct) {
        log.info("User {} wants to create a new order", currentUser.getUser().getEmail());
        Optional<Product> productById = productService.findById(productId);
        if (!productById.isPresent() ||
                productById.get().getCountInStock() == 0 ||
                productById.get().getCountInStock() < countOfProduct) {
            log.warn("Product doesn't exist or the count of product in order is more than count in stock");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Product product = productById.get();
        UserOrder newOrder = orderService.save(currentUser.getUser(), product, countOfProduct);
        log.info("New order has been created for user {}", currentUser.getUser().getEmail());
        return ResponseEntity.ok(newOrder);
    }


    /**
     * endpoint to remove the order
     *
     * @param orderId -> order id
     * @return -> if removed, returns 200, if not returns 404
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserOrder> delete(@PathVariable("id") int orderId,
                                            @AuthenticationPrincipal CurrentUser currentUser) {
        log.info("User {} wants to remove an order", currentUser.getUser().getEmail());
        Optional<UserOrder> optOrder = orderService.findById(orderId);
        if (!optOrder.isPresent()) {
            log.warn("No order found by id {}", orderId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (!optOrder.get().getUser().equals(currentUser.getUser())) {
            log.warn("User {} wanted to remove user {} order", currentUser.getUser().getEmail(), optOrder.get().getUser().getEmail());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        orderService.delete(optOrder.get());
        log.info("User {} removed the order", currentUser.getUser().getEmail());
        return ResponseEntity.ok().build();
    }


    /**
     * endpoint only for ADMIN, to change order status(ONLY FOR ADMIN)
     *
     * @param orderId   -> order id
     * @param newStatus -> new status, chosen by ADMIN
     * @return if ok, returns 200, if not returns 404;
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserOrder> changeStatus(@PathVariable("id") int orderId,
                                                  @RequestParam("status") String newStatus,
                                                  @AuthenticationPrincipal CurrentUser currentUser) {
        log.info("Admin {} wants to change order status to {}", currentUser.getUser().getEmail(), newStatus);
        Optional<UserOrder> optOrder = orderService.findById(orderId);
        if (!optOrder.isPresent() || optOrder.get().getStatus().equals(OrderStatus.DELIVERED)) {
            log.warn("No active order found by id {}", orderId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        UserOrder order = optOrder.get();
        String oldStatus = order.getStatus().name();
        UserOrder userOrder = orderService.changeStatus(order, newStatus);
        log.info("Order status has been changed from {} to {}", oldStatus, newStatus);
        return ResponseEntity.ok(userOrder);

    }


}
