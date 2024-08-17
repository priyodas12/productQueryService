package tech.springboot.productQueryService.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tech.springboot.productQueryService.model.SqsEvent;

public interface SqsEventRepository extends JpaRepository<SqsEvent, UUID> {

}
