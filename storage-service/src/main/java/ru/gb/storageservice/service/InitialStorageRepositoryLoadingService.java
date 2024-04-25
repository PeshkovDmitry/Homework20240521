package ru.gb.storageservice.service;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.gb.storageservice.model.Product;
import ru.gb.storageservice.repository.ProductRepository;

@Service
@AllArgsConstructor
public class InitialStorageRepositoryLoadingService implements CommandLineRunner {

    private final ProductRepository productRepository;

    @Override
    public void run(String... args) throws Exception {

        productRepository.save(
                new Product(1L, "Ботинки", 4, 0,0));

        productRepository.save(
                new Product(2L, "Кроссовки", 6, 0,0));

    }
}
