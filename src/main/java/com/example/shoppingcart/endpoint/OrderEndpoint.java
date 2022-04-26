package com.example.shoppingcart.endpoint;

import com.example.shoppingcart.entity.Order;
import com.example.shoppingcart.security.CurrentUser;
import com.example.shoppingcart.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Order controller which receives all order requests and sends necessary responses
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderEndpoint {

    private final OrderService orderService;


    /**
     * endpoint to show customer his orders
     * @param currentUser -> current customer
     * @return -> list of customer orders
     */
    @GetMapping("/view")
    public List<Order> showCustomerOrders(@AuthenticationPrincipal CurrentUser currentUser) {
        return orderService.findCurrentUserOrders(currentUser);
    }


    /**
     * endpoint to save a new order, if product exists in stock
     * @param currentUser -> current customer
     * @param productId -> order product id
     * @param countOfProduct -> count of products in order
     * @return -> if saved, returns the order, if not returns 404
     */
    @PostMapping("/")
    public ResponseEntity<Order> saveNewOrder(@AuthenticationPrincipal CurrentUser currentUser,
                                           @RequestParam("id") int productId,
                                           @RequestParam("count") int countOfProduct) {
       return orderService.saveOrder(currentUser,productId,countOfProduct);
    }

    /**
     * endpoint to remove the order
     * @param orderId -> order id
     * @return -> if removed, returns 200, if not returns 404
     */

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomerOrder(@PathVariable("id") int orderId){
        return orderService.deleteOrderById(orderId);
    }


    /**
     * endpoint only for ADMIN, to change order status(ONLY FOR ADMIN)
     * @param orderId -> order id
     * @param status -> new status, chosen by ADMIN
     * @return if ok, returns 200, if not returns 404;
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> changeOrderStatus(@PathVariable("id") int orderId,
                                                    @RequestParam("status") String status){
        return orderService.changeOrderStatus(orderId,status);

    }


}
