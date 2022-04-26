package com.example.shoppingcart.endpoint;

import com.example.shoppingcart.dto.request.ProductRequestDto;
import com.example.shoppingcart.dto.response.ProductResponseDto;
import com.example.shoppingcart.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product controller which receives all product requests and sends necessary responses
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductEndpoint {

    private final ProductService productService;


    /**
     * endpoint to add a new product in stock(ONLY FOR ADMIN)
     * @param createProductRequestDto -> new product data
     * @return -> saved product dto
     */
    @PostMapping("/")
    public @ResponseBody
    ProductResponseDto saveProduct(@RequestBody ProductRequestDto createProductRequestDto) {
        return productService.saveProductFromRequest(createProductRequestDto);
    }

    /**
     * endpoint to update the product(ONLY FOR ADMIN)
     * @param productId -> product id
     * @param updateProductRequestDto -> product new data
     * @return -> if updated, returns product dto, if not returns 404;
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable("id") int productId,
                                                            @RequestBody ProductRequestDto updateProductRequestDto) {
        return productService.updateProductByIdAndProductRequestIfExists(productId, updateProductRequestDto);
    }


    /**
     * endpoint to remove product
     * @param productId -> product id
     * @return -> if removed, returns 200, if not returns 404
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeProduct(@PathVariable("id") int productId) {
        return productService.removeProductByIdIfExists(productId);
    }

    /**
     * endpoint to show all products
     * @return -> list of existing products
     */
    @GetMapping("/view")
    public List<ProductResponseDto> seeAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * endpoint to show all products by product type
     * @param type -> type, chosen by customer
     * @return -> list of existing products of chosen type
     */
    @GetMapping("/view/{type}")
    public List<ProductResponseDto> seeProductsByType(@PathVariable("type") String type) {
        return productService.getProductsByType(type);
    }

    /**
     * endpoint to show sorted products by product type
     * @param type -> type, chosen by customer
     * @param sort -> sorting(name,price,createdDate)
     * @param dir -> direction of sorting(ASC, DESC)
     * @return -> sorted list of existing products of chosen type
     */
    @GetMapping("/view/{type}/{sort}/{dir}")
    public List<ProductResponseDto> seeSortedProductsByType(@PathVariable("type") String type,
                                                            @PathVariable("sort") String sort,
                                                            @PathVariable("dir") String dir) {
        return productService.getProductsSortedByType(type,sort,dir);
    }


    /**
     * endpoint to search a product by keyword
     * @param keyword -> keyword written by customer
     * @return -> if found, returns list of products, if not returns 404
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponseDto>> searchProducts(@RequestParam("q") String keyword) {
        return productService.getProductsByKeyword(keyword);
    }

}
