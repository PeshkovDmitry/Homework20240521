package ru.gb.storageservice.service;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.gb.storageservice.model.Item;
import ru.gb.storageservice.repository.ItemRepository;

@Service
@AllArgsConstructor
public class InitialStorageRepositoryLoadingService implements CommandLineRunner {

    private final ItemRepository itemRepository;

    @Override
    public void run(String... args) throws Exception {

        itemRepository.save(
                new Item(1L, 4, 0,0));

        itemRepository.save(
                new Item(2L, 6, 0,0));

    }
}
