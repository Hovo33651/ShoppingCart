package com.example.shoppingcart.repository;

import com.example.shoppingcart.entity.Product;
import com.example.shoppingcart.entity.ProductType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {


    List<Product> findProductsByType(ProductType type);
    List<Product> findProductsByType(ProductType type, Sort sort);

}

