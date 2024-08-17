package tech.springboot.productQueryService.repository;

import java.math.BigInteger;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.springboot.productQueryService.model.Product;

public interface ProductRepository extends JpaRepository<Product, BigInteger> {

}
