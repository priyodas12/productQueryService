package tech.springboot.productQueryService.service;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;
import tech.springboot.productQueryService.model.Product;
import tech.springboot.productQueryService.model.SqsEvent;
import tech.springboot.productQueryService.repository.ProductRepository;
import tech.springboot.productQueryService.repository.SqsEventRepository;

@Service
@Log4j2
public class ProductService {


  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private SqsEventRepository sqsEventRepository;

  public Optional<Product> fetchProductById (BigInteger productId) {
    log.info ("Fetching product : {} ", productId);
    return productRepository.findById (productId);
  }

  public Product saveProduct (Product product) {
    log.info ("Saving product : {} ", product);
    return productRepository.save (product);
  }

  public void deleteProduct (Product product) {
    log.info ("Deleting product : {} ", product);
    productRepository.delete (product);
  }

  public SqsEvent saveProductSqsEvent (SqsEvent sqsEvent) {
    log.info ("Saving sqs event : {} ", sqsEvent);
    return sqsEventRepository.save (sqsEvent);
  }
}

