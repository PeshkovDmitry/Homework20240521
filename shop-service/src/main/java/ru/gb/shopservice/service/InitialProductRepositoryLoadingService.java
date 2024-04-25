package ru.gb.shopservice.service;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.gb.shopservice.model.Product;
import ru.gb.shopservice.repository.ProductRepository;

@Service
@AllArgsConstructor
public class InitialProductRepositoryLoadingService implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {

        productRepository.save(
                new Product(1L, "Ботинки", 150));

        productRepository.save(
                new Product(2L, "Кроссовки", 200));

    }
}
