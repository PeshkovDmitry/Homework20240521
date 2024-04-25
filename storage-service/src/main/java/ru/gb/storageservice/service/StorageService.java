package ru.gb.storageservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.storageservice.model.Item;
import ru.gb.storageservice.repository.ItemRepository;


import java.util.List;

@Service
@AllArgsConstructor
public class StorageService {

    private final ItemRepository itemRepository;

    public List<Item> getAll() {
        return itemRepository.findAll();
    }

    @Transactional
    public void setReserved(long id, int count) {
        Item item = itemRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт не найден"));
        if (item.getInShop() < count) {
            throw new RuntimeException("Недостаточное количество");
        }
        item.setInReserve(
                item.getInReserve() + count
        );
        item.setInShop(
                item.getInShop() - count
        );
        itemRepository.save(item);
    }

    @Transactional
    public void setSelling(long id) {
        Item item = itemRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт не найден"));
        if (item.getInReserve() == 0) {
            throw new RuntimeException("Продукт не был зарезервирован");
        }
        item.setWithBuyer(item.getWithBuyer() + item.getInReserve());
        item.setInReserve(0);
        itemRepository.save(item);
    }

}
