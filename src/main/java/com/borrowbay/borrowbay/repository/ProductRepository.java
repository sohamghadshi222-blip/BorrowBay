package com.borrowbay.borrowbay.repository;

import com.borrowbay.borrowbay.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByOwnerEmail(String ownerEmail);

}