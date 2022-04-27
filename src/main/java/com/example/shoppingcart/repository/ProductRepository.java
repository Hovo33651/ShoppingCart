package com.example.shoppingcart.repository;

import com.example.shoppingcart.entity.Product;
import com.example.shoppingcart.entity.ProductType;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {


    List<Product> findProductsByType(ProductType type);
    List<Product> findProductsByType(ProductType type, Sort sort);

    @Query(value = "SELECT * FROM product p WHERE p.name LIKE %?1% OR p.description LIKE %?1%",
    nativeQuery = true)
    List<Product> findAll(String keyword);

}

