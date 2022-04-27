package com.example.shoppingcart.repository;

import com.example.shoppingcart.entity.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserOrderRepository extends JpaRepository<UserOrder,Integer> {

    List<UserOrder> findOrdersByUser_Id(int userId);

}
