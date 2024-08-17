package tech.springboot.productQueryService.controller;


import java.math.BigInteger;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;
import tech.springboot.productQueryService.model.Product;
import tech.springboot.productQueryService.model.ProductResponse;
import tech.springboot.productQueryService.service.ProductService;

@Log4j2
@RestController
@RequestMapping ("/api/v2")
public class ProductController {

  @Autowired
  private ProductService productService;

  @GetMapping ("/products/{productId}")
  public ResponseEntity<ProductResponse> getProduct (
      @Validated @PathVariable ("productId") BigInteger productId) {
    log.info ("Fetching product Id: {} ", productId);
    Optional<Product> product = productService.fetchProductById (productId);
    ProductResponse productResponse = new ProductResponse ();
    productResponse.setProduct (product.get ());
    productResponse.setStatusCode (HttpStatusCode.valueOf (200));
    return ResponseEntity.ok (productResponse);
  }


}
