# Shopping Cart
![](https://partyponty.hu/public/uploads/contents/resized/front/081e84054e5cec4f95be5f9c36ee9212Onlineshoppingwithcart_Tevarak_iStock_GettyImagesPlus-5dac33d3b517456f9a0955b2836ebe07.jpg)
### An e-commerce web application to make the shopping easier. 
___
>## Description
### Shopping Cart is a simple Restful API.
#### Project has two types of users: Customer and Admin. Admin is able to add, update or remove products from the store. Customer is able to register, providing name, surname, birthday, email, password. After registration or login Customer receives a JW Token, gets authenticated, and then Customer can see all the products in the store, can search a product, can filter the products by type and sort them by name and price by lower and by higher. Also Customer can make orders, see his orders and remove the order. Admin can change the order status. 
___
>## Technology Stack
 + ### Java 8
 + ### Spring Framework
 + ### Design Patterns
 + ### Java Docs
 + ### Mysql
 + ### Swagger
 + ### Maven
 + ### GitHub
 ___
>## Libraries
+ ### Lombok
+ ### SpringFox Swagger 2
+ ### ModelMapper
+ ### JWT
+ ### Hibernate Validator
+ ### MySql Connector Java
+ ### Spring Security
+ ### Spring Data Jpa
+ ### JUnit
+ ### Mockito Core
+ ### H2 Database
___
>## Few Code Examples
### Swagger Configuration

```java
@Configuration
public class SpringFoxConfig {

@Bean
public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2).select()
          .apis(RequestHandlerSelectors.basePackage("com.exampleshoppingcartendpoint"))
          .paths(PathSelectors.any())
          .build();   

    }
}
```

### JWT Authentication Entry Point
```java
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable {

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Unauthorized");
    }

}
```

### JWT Authentication Token Filter
```java
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil tokenUtil;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String requestHeader = request.getHeader("Authorization");
        String username = null;
        String authToken = null;
        if(requestHeader != null && requestHeader.startsWith("Bearer ")){
            authToken = requestHeader.substring(7);
            try{
                username = tokenUtil.getUsernameFromToken(authToken);
            }catch (Exception e){
                logger.error(e);
            }
        }
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            if(tokenUtil.validateToken(authToken,userDetails.getUsername())){
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request,response);

    }
}
```
### Endpoint to save new product

```java
/**
     * endpoint to add a new product in stock(ONLY FOR ADMIN)
     *
     * @param createProductRequestDto -> new product data
     * @param bindingResult           -> checks if there are any errors about filling the fields
     * @return -> saved product dto
     */
    @PostMapping("/")
    public ResponseEntity<?> saveProduct(@RequestBody @Valid ProductRequestDto createProductRequestDto,
                                         BindingResult bindingResult,
                                         @AuthenticationPrincipal CurrentUser currentUser) {
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
```
___
>## Developer
### Hovhannes Gevorgyan
>## Contacts
>+ e-mail: hovosse888@gmail.com
>+ telephone: +(374)93-77-66-94

