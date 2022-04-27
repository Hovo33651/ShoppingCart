package com.example.shoppingcart.service;

import com.example.shoppingcart.dto.request.ProductRequestDto;
import com.example.shoppingcart.dto.response.ProductResponseDto;
import com.example.shoppingcart.entity.Product;
import com.example.shoppingcart.entity.ProductType;
import com.example.shoppingcart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Product Service
 * This service class contains all business logic that concern product
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");


    /**
     * method to add a new product in stock(ONLY FOR ADMIN)
     *
     * @param createProductRequestDto -> new product data
     * @return -> saved product dto
     */
    @SneakyThrows
    public ProductResponseDto saveProductFromRequest(ProductRequestDto createProductRequestDto) {
        Product newProduct = Product.builder()
                .name(createProductRequestDto.getName())
                .description(createProductRequestDto.getDescription())
                .createdDate(sdf.parse(createProductRequestDto.getCreatedDate()))
                .countInStock(createProductRequestDto.getCountInStock())
                .type(ProductType.valueOf(createProductRequestDto.getType()))
                .price(createProductRequestDto.getPrice())
                .build();
        Product savedProduct = productRepository.save(newProduct);
        return modelMapper.map(savedProduct, ProductResponseDto.class);
    }


    /**
     * method to update the product(ONLY FOR ADMIN)
     *
     * @param product                 -> product which will be updated
     * @param updateProductRequestDto -> product new data
     * @return -> updates and returns ProductRepsonseDto
     */
    @SneakyThrows
    public ProductResponseDto updateProductByIdAndProductRequestIfExists(Product product, ProductRequestDto updateProductRequestDto) {
        product.setName(updateProductRequestDto.getName());
        product.setDescription(updateProductRequestDto.getDescription());
        product.setCountInStock(updateProductRequestDto.getCountInStock());
        product.setCreatedDate(sdf.parse(updateProductRequestDto.getCreatedDate()));
        product.setUpdatedDate(LocalDate.now());
        Product updatedProduct = productRepository.save(product);
        return modelMapper.map(updatedProduct, ProductResponseDto.class);
    }


    /**
     * method to remove product(ONLY FOR ADMIN)
     *
     * @param product -> product which will be removed
     */
    public void removeProduct(Product product) {
        productRepository.delete(product);
    }


    /**
     * method to show all products
     *
     * @return -> list of existing products
     */
    public List<ProductResponseDto> findAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .collect(Collectors.toList());
    }



    /**
     * method to search a product by keyword
     *
     * @param keyword -> keyword written by customer
     * @return ->  list of products found by keyword
     */
    public List<ProductResponseDto> findProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findAll(keyword);
        return products.stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .collect(Collectors.toList());
    }


    /**
     * method to show all products by product type
     *
     * @param type -> type, chosen by customer
     * @return -> list of existing products of chosen type
     */
    public List<ProductResponseDto> findProductsByType(String type) {
        ProductType productType = ProductType.valueOf(type);
        List<Product> productsByType = productRepository.findProductsByType(productType);
        return productsByType.stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .collect(Collectors.toList());
    }


    /**
     * method to show sorted products by product type
     *
     * @param type -> type, chosen by customer
     * @param sort -> sorting(name,price,createdDate, asc or desc)
     * @return -> sorted list of existing products of chosen type
     */
    public List<ProductResponseDto> findProductsSortedByType(String type, Sort sort) {
        ProductType productType = ProductType.valueOf(type);
        List<Product> productsByType = productRepository.findProductsByType(productType, sort);
        return productsByType.stream()
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .collect(Collectors.toList());
    }


    /**
     * method to find Optional Product by id
     *
     * @param productId -> product id
     * @return -> Optional Product
     */
    public Optional<Product> findById(int productId) {
        return productRepository.findById(productId);
    }

}
