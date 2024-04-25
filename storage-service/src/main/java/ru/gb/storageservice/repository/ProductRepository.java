package ru.gb.storageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.storageservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
