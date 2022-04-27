package com.example.shoppingcart.service;

import com.example.shoppingcart.dto.request.ProductRequestDto;
import com.example.shoppingcart.dto.response.ProductResponseDto;
import com.example.shoppingcart.entity.Product;
import com.example.shoppingcart.entity.ProductType;
import com.example.shoppingcart.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;
    private ProductRequestDto productRequestDto;

    @BeforeEach
    void beforeEach() {
        productRequestDto = ProductRequestDto.builder()
                .name("telephone")
                .description("very good telephone")
                .type("ELECTRONICS")
                .countInStock(15)
                .createdDate("04.04.2020")
                .price(15000)
                .build();
    }

    @Test
    void saveProductFromRequest() {
        productService.saveProductFromRequest(productRequestDto);
        Optional<Product> byId = productRepository.findById(1);
        assertTrue(byId.isPresent());
        assertEquals("very good telephone",byId.get().getDescription());
        assertEquals(1,productRepository.count());
    }

    @Test
    void updateProductByIdAndProductRequestIfExists() {
        productService.saveProductFromRequest(productRequestDto);
        ProductRequestDto updatedRequest = ProductRequestDto.builder()
                .name("telephone")
                .description("very bad telephone")
                .type("ELECTRONICS")
                .countInStock(15)
                .createdDate("04.04.2020")
                .price(15000)
                .build();
        Optional<Product> optProduct = productRepository.findById(1);
        assertTrue(optProduct.isPresent());
        assertEquals(1,optProduct.get().getId());
        productService.updateProductByIdAndProductRequestIfExists(optProduct.get(),updatedRequest);
        Optional<Product> updatedProduct = productRepository.findById(1);
        assertTrue(updatedProduct.isPresent());
        assertEquals("very bad telephone",updatedProduct.get().getDescription());
        assertNotNull(updatedProduct.get().getUpdatedDate());



    }

    @Test
    void removeProduct() {
        productService.saveProductFromRequest(productRequestDto);
        Optional<Product> opdProduct = productRepository.findById(1);
        assertTrue(opdProduct.isPresent());
        productService.removeProduct(opdProduct.get());
        assertEquals(0,productRepository.count());
    }

    @Test
    void findAllProducts() {
        productService.saveProductFromRequest(productRequestDto);
        productService.saveProductFromRequest(productRequestDto);
        productService.saveProductFromRequest(productRequestDto);
        productService.saveProductFromRequest(productRequestDto);
        List<ProductResponseDto> allProducts = productService.findAllProducts();
        assertFalse(allProducts.isEmpty());
        assertEquals(4,allProducts.size());


    }

    @Test
    void findProductsByType() {
        productService.saveProductFromRequest(productRequestDto);
        productService.saveProductFromRequest(productRequestDto);
        productService.saveProductFromRequest(productRequestDto);
        List<ProductResponseDto> electronicProducts = productService.findProductsByType("ELECTRONICS");
        List<ProductResponseDto> careProducts = productService.findProductsByType("CARE");
        assertFalse(electronicProducts.isEmpty());
        assertEquals(3,electronicProducts.size());
        assertTrue(careProducts.isEmpty());

    }

    @Test
    void findProductsByKeyword() {
        productService.saveProductFromRequest(productRequestDto);
        List<ProductResponseDto> productsByCorrectKeyword = productService.findProductsByKeyword("tel");
        List<ProductResponseDto> productsByWrongKeyword = productService.findProductsByKeyword("bla");
        assertFalse(productsByCorrectKeyword.isEmpty());
        assertEquals(1,productsByCorrectKeyword.size());
        assertTrue(productsByWrongKeyword.isEmpty());

    }

    @Test
    void findProductsSortedByType() {
        productService.saveProductFromRequest(productRequestDto);
        Sort sort = Sort.by("name").ascending();
        List<ProductResponseDto> products = productService.findProductsSortedByType("ELECTRONICS", sort);
        assertFalse(products.isEmpty());
    }
}