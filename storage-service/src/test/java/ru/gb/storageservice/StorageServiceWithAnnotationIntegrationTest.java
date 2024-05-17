package ru.gb.storageservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.gb.storageservice.model.Item;
import ru.gb.storageservice.repository.ItemRepository;
import ru.gb.storageservice.service.StorageService;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class StorageServiceWithAnnotationIntegrationTest {

    @Autowired
    private StorageService service;

    @MockBean
    private ItemRepository repository;

    @Test
    public void setReservedCorrectFlow() {

        //Блок предусловия
        Item item = new Item(1, 10, 1, 0);
        when(repository.findById(item.getId())).thenReturn(Optional.of(item));

        //Блок действия
        service.setReserved(item.getId(), 2);

        //Блок проверки действия
        verify(repository).save(new Item(1, 8, 3, 0));

    }
}
