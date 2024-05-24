package ru.gb.shopservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.shopservice.dto.storage.ReserveRequest;
import ru.gb.shopservice.proxy.StorageServiceProxy;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ReserveService {

    private final StorageServiceProxy storageServiceProxy;

    private final LoggingService loggingService;

    /*
     * Метод для резервирования товара на складе
     */

    public void reserve(long id, int count) {
        try {
            loggingService.log(String.format(
                    "Запрос на резервирование товара с кодом \"%d\" в количестве %d единиц", id, count));
            storageServiceProxy.reserve(new ReserveRequest(id, count));
        } catch (Exception e) {
            throw new RuntimeException("Не удалось зарезервировать товар");
        }
    }

}
