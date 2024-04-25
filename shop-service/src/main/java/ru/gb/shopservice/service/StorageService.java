package ru.gb.shopservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.shopservice.model.Product;
import ru.gb.shopservice.repository.ProductRepository;


import java.util.List;

@Service
@AllArgsConstructor
public class StorageService {

    private final ProductRepository productRepository;

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Transactional
    public void setReserved(long id, int count) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт не найден"));
        if (product.getInShop() < count) {
            throw new RuntimeException("Недостаточное количество");
        }
        product.setInReserve(
                product.getInReserve() + count
        );
        product.setInShop(
                product.getInShop() - count
        );
        productRepository.save(product);
    }

    @Transactional
    public void setSelling(long id) {
        Product product = productRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт не найден"));
        if (product.getInReserve() == 0) {
            throw new RuntimeException("Продукт не был зарезервирован");
        }
        product.setWithBuyer(product.getInReserve());
        product.setInReserve(0);
        productRepository.save(product);
    }

}
