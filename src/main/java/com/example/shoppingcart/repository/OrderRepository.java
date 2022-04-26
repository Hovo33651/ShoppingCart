package com.example.shoppingcart.repository;

import com.example.shoppingcart.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Integer> {

    List<Order> findOrdersByUser_Id(int userId);

}
