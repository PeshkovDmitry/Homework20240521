package ru.gb.storageservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.storageservice.model.Item;
import ru.gb.storageservice.repository.ItemRepository;


import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StorageService {

    private final ItemRepository itemRepository;

    private final FileGatewayService fileGatewayService;

    public List<Item> getAll() {
        fileGatewayService.writeToFile(
                "log.txt",
                "Storage-service (" + LocalDateTime.now() + "): "
                        + "Выдача полных данных");
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
        fileGatewayService.writeToFile(
                "log.txt",
                "Storage-service (" + LocalDateTime.now() + "): "
                        + String.format(
                        "Резервирование товара с кодом \"%d\" в количестве %d единиц",
                        id,
                        count
                ));
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
        fileGatewayService.writeToFile(
                "log.txt",
                "Storage-service (" + LocalDateTime.now() + "): "
                        + String.format(
                        "Выдача покупателю товара с кодом \"%d\"",
                        id
                ));
        itemRepository.save(item);
    }

}
