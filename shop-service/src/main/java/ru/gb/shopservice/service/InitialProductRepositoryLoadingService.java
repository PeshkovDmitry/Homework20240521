package ru.gb.shopservice.service;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.gb.shopservice.model.Product;
import ru.gb.shopservice.repository.ProductRepository;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class InitialProductRepositoryLoadingService implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {

        productRepository.save(
                new Product(1L, "Ботинки", new BigDecimal(150)));

        productRepository.save(
                new Product(2L, "Кроссовки", new BigDecimal(200)));

    }
}
