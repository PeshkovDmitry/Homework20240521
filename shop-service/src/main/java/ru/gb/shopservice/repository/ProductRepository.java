package ru.gb.shopservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.shopservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
