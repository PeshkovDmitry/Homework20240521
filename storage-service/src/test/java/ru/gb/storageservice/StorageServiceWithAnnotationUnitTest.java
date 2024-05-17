package ru.gb.storageservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gb.storageservice.model.Item;
import ru.gb.storageservice.repository.ItemRepository;
import ru.gb.storageservice.service.StorageService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class StorageServiceWithAnnotationUnitTest {

    @InjectMocks
    private StorageService service;

    @Mock
    private ItemRepository repository;

    @Test
    public void setReservedCorrectFlow() {

        //Блок предусловия
        Item item = new Item(1, 10, 1, 0);
        given(repository.findById(item.getId())).willReturn(Optional.of(item));

        //Блок действия
        service.setReserved(item.getId(), 2);

        //Блок проверки действия
        verify(repository).save(new Item(1, 8, 3, 0));

    }

    @Test
    public void setReservedAccountNotFound() {

        //Блок предусловия
        given(repository.findById(1L)).willReturn(Optional.empty());

        //Блок действия
        assertThrows(RuntimeException.class, () -> {service.setReserved(1L, 2);});

        //Блок проверки действия
        verify(repository, never()).save(any());

    }


}
