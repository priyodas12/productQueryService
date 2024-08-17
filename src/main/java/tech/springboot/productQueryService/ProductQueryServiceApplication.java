package tech.springboot.productQueryService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProductQueryServiceApplication {

  public static void main (String[] args) {
    SpringApplication.run (ProductQueryServiceApplication.class, args);
  }

}
