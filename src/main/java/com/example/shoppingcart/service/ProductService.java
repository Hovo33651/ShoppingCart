package com.example.shoppingcart.service;

import com.example.shoppingcart.dto.request.ProductRequestDto;
import com.example.shoppingcart.dto.response.ProductResponseDto;
import com.example.shoppingcart.entity.Product;
import com.example.shoppingcart.entity.ProductType;
import com.example.shoppingcart.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    /**
     * endpoint to add a new product in stock(ONLY FOR ADMIN)
     * @param createProductRequestDto -> new product data
     * @return -> saved product dto
     */
    public ProductResponseDto saveProductFromRequest(ProductRequestDto createProductRequestDto) {
        Product newProduct = modelMapper.map(createProductRequestDto, Product.class);
        Product savedProduct = productRepository.save(newProduct);
        return modelMapper.map(savedProduct, ProductResponseDto.class);
    }

    /**
     * endpoint to update the product(ONLY FOR ADMIN)
     * @param productId -> product id
     * @param updateProductRequestDto -> product new data
     * @return -> if updated, returns product dto, if not returns 404;
     */
    public ResponseEntity<ProductResponseDto> updateProductByIdAndProductRequestIfExists(int productId, ProductRequestDto updateProductRequestDto) {
        Optional<Product> optProduct = productRepository.findById(productId);
        if (!optProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Product product = optProduct.get();
        product.setName(updateProductRequestDto.getName());
        product.setDescription(updateProductRequestDto.getDescription());
        product.setCountInStock(updateProductRequestDto.getCountInStock());
        product.setCreatedDate(updateProductRequestDto.getCreatedDate());
        product.setUpdatedDate(LocalDate.now());
        Product updatedProduct = productRepository.save(product);
        return ResponseEntity.ok(modelMapper.map(updatedProduct, ProductResponseDto.class));

    }


    /**
     * endpoint to remove product
     * @param productId -> product id
     * @return -> if removed, returns 200, if not returns 404
     */
    public ResponseEntity<String> removeProductByIdIfExists(int productId) {
        Optional<Product> product = productRepository.findById(productId);
        if (!product.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        productRepository.delete(product.get());
        return ResponseEntity.ok().build();
    }

    /**
     * endpoint to show all products
     * @return -> list of existing products
     */
    public List<ProductResponseDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> modelMapper.map(product,ProductResponseDto.class))
                .collect(Collectors.toList());
    }

    /**
     * endpoint to show all products by product type
     * @param type -> type, chosen by customer
     * @return -> list of existing products of chosen type
     */
    public List<ProductResponseDto> getProductsByType(String type) {
        List<Product> productsByType = productRepository.findProductsByType(ProductType.valueOf(type));
        return productsByType.stream()
                .map(product -> modelMapper.map(product,ProductResponseDto.class))
                .collect(Collectors.toList());
    }

    /**
     * endpoint to search a product by keyword
     * @param keyword -> keyword written by customer
     * @return -> if found, returns list of products, if not returns 404
     */
    public ResponseEntity<List<ProductResponseDto>> getProductsByKeyword(String keyword) {
        List<Product> products = productRepository.findAll();
        List<ProductResponseDto> responseDtoList = products.stream()
                .filter(product -> product.getName().contains(keyword)
                        || product.getDescription().contains(keyword))
                .map(product -> modelMapper.map(product, ProductResponseDto.class))
                .collect(Collectors.toList());
        if(responseDtoList.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(responseDtoList);
    }

    /**
     * endpoint to show sorted products by product type
     * @param type -> type, chosen by customer
     * @param sortStr -> sorting(name,price,createdDate)
     * @param dir -> direction of sorting(ASC, DESC)
     * @return -> sorted list of existing products of chosen type
     */
    public List<ProductResponseDto> getProductsSortedByType(String type, String sortStr, String dir){
        Sort sort;
        if(dir.equals("asc")){
            sort = Sort.by(sortStr).ascending();
        }
        else{
            sort = Sort.by(sortStr).descending();
        }
        List<Product> productsByType = productRepository.findProductsByType(ProductType.valueOf(type), sort);
        return productsByType.stream()
                .map(product -> modelMapper.map(product,ProductResponseDto.class))
                .collect(Collectors.toList());

    }
}
