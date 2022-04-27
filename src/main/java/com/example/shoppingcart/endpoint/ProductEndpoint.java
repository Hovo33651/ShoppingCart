package com.example.shoppingcart.endpoint;

import com.example.shoppingcart.dto.request.ProductRequestDto;
import com.example.shoppingcart.dto.response.ProductResponseDto;
import com.example.shoppingcart.entity.Product;
import com.example.shoppingcart.security.CurrentUser;
import com.example.shoppingcart.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Product controller which receives all product requests and sends necessary responses
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Slf4j
public class ProductEndpoint {

    private final ProductService productService;


    /**
     * endpoint to add a new product in stock(ONLY FOR ADMIN)
     *
     * @param createProductRequestDto -> new product data
     * @param bindingResult           -> checks if there are any errors about filling the fields
     * @return -> saved product dto
     */
    @PostMapping("/")
    public ResponseEntity<?> saveProduct(@RequestBody @Valid ProductRequestDto createProductRequestDto,
                                         BindingResult bindingResult,@AuthenticationPrincipal CurrentUser currentUser) {
        log.info("New request to save a product called {}.", createProductRequestDto.getName());
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        ProductResponseDto savedProductResponseDto = productService.saveProductFromRequest(createProductRequestDto);
        log.info("Product {} has been saved.", savedProductResponseDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductResponseDto);
    }


    /**
     * endpoint to update the product(ONLY FOR ADMIN)
     *
     * @param productId               -> product id
     * @param updateProductRequestDto -> product new data
     * @param bindingResult           -> checks if there are any errors about filling the fields
     * @return -> if updated, returns product dto, if not returns 404;
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable("id") int productId,
                                           @RequestBody @Valid ProductRequestDto updateProductRequestDto,
                                           BindingResult bindingResult,
                                           @AuthenticationPrincipal CurrentUser currentUser) {
        log.info("Admin {} wants to update product", currentUser.getUser().getEmail());
        if (bindingResult.hasErrors()) {
            List<String> errors = new ArrayList<>();
            for (ObjectError error : bindingResult.getAllErrors()) {
                errors.add(error.getDefaultMessage());
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
        }
        Optional<Product> optProduct = productService.findById(productId);
        if (!optProduct.isPresent()) {
            log.warn("Product with id {} doesn't exist.", productId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        ProductResponseDto productResponseDto = productService.updateProductByIdAndProductRequestIfExists(optProduct.get(), updateProductRequestDto);
        log.info("Product {} has been updated on {}, by Admin {}", productResponseDto.getName(), productResponseDto.getUpdatedDate(), currentUser.getUser().getEmail());
        return ResponseEntity.ok(productResponseDto);
    }


    /**
     * endpoint to remove product
     *
     * @param productId -> product id
     * @return -> if removed, returns 200, if not returns 404
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeProductIfExists(@PathVariable("id") int productId,
                                                        @AuthenticationPrincipal CurrentUser currentUser) {
        log.info("ADMIN {} wants to remove a product", currentUser.getUser().getEmail());
        Optional<Product> optProduct = productService.findById(productId);
        if (!optProduct.isPresent()) {
            log.warn("Product with id {} doesn't exist", productId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        productService.removeProduct(optProduct.get());
        log.info("Product has been removed on {} by Admin {}", LocalDateTime.now(), currentUser.getUser().getEmail());
        return ResponseEntity.ok().build();
    }


    /**
     * endpoint to show all products
     *
     * @return -> list of existing products
     */
    @GetMapping("/view")
    public List<ProductResponseDto> getAllProducts(@AuthenticationPrincipal CurrentUser currentUser) {
        log.info("User {}: request to see all products", currentUser.getUser().getEmail());
        return productService.findAllProducts();
    }

    /**
     * endpoint to show all products by product type
     *
     * @param type -> type, chosen by customer
     * @return -> list of existing products of chosen type
     */
    @GetMapping("/type")
    public ResponseEntity<List<ProductResponseDto>> getProductsByType(@RequestParam("t") String type,
                                                                      @AuthenticationPrincipal CurrentUser currentUser) {
        try {
            log.info("User {}: request to see products of type /{}/", currentUser.getUser().getEmail(), type);
            List<ProductResponseDto> productsByType = productService.findProductsByType(type);
            if (productsByType.isEmpty()) {
                log.warn("There is no product of type /{}/", type);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            log.info("{} products of type /{}/ have been found", productsByType.size(), type);
            return ResponseEntity.ok(productsByType);
        } catch (Exception e) {
            log.error("Wrong type: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    /**
     * endpoint to show sorted products by product type
     *
     * @param type -> type, chosen by customer
     * @param sortStr -> sorting(name,price,createdDate)
     * @param dir  -> direction of sorting(ASC, DESC)
     * @return -> sorted list of existing products of chosen type
     */
    @GetMapping("/sort")
    public ResponseEntity<List<ProductResponseDto>> seeSortedProductsByType(@RequestParam("t") String type,
                                                                            @RequestParam("s") String sortStr,
                                                                            @RequestParam("d") String dir,
                                                                            @AuthenticationPrincipal CurrentUser currentUser) {
        log.info("User {}: request to see products of type {}, sorted by {}, {}", currentUser.getUser().getEmail(), type, sortStr, dir);
        Sort sort;
        if (dir.equals("asc")) {
            sort = Sort.by(sortStr).ascending();
        } else if (dir.equals("desc")) {
            sort = Sort.by(sortStr).descending();
        } else {
            log.warn("Wrong sorting direction: {}", dir);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        List<ProductResponseDto> productsByType = productService.findProductsSortedByType(type, sort);
        if (productsByType.isEmpty()) {
            log.warn("No products of type {} found", type);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("{} products of type {} found", productsByType.size(), type);
        return ResponseEntity.ok(productsByType);

    }


    /**
     * endpoint to search a product by keyword
     *
     * @param keyword -> keyword written by customer
     * @return -> if found, returns list of products, if not returns 404
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDto>> searchProducts(@RequestParam("q") String keyword,
                                                                   @AuthenticationPrincipal CurrentUser currentUser) {
        log.info("User {}: request to find product by keyword /{}/", currentUser.getUser().getEmail(), keyword);
        List<ProductResponseDto> products = productService.findProductsByKeyword(keyword);
        if (products.isEmpty()) {
            log.warn("No products found with keyword /{}/", keyword);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        log.info("{} products found by keyword /{}/", products.size(), keyword);

        return ResponseEntity.ok(products);

    }

}
