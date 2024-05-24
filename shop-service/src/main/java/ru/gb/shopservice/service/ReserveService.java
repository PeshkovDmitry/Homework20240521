package ru.gb.shopservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.shopservice.dto.storage.ReserveRequest;
import ru.gb.shopservice.proxy.StorageServiceProxy;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class ReserveService {

    private final FileGatewayService fileGatewayService;

    private final StorageServiceProxy storageServiceProxy;

    public void process(long id, int count) throws Exception {
        if (reserve(id, count)) {
            throw new Exception("Не удалось зарезервировать товар");
        }
    }

    /*
     * Метод для резервирования товара на складе
     */

    public boolean reserve(long id, int count) {
        try {
            fileGatewayService.writeToFile(
                    "log.txt",
                    "Shop-service (" + LocalDateTime.now() + "): "
                            + String.format(
                            "Запрос на резервирование товара с кодом \"%d\" в количестве %d единиц",
                            id,
                            count
                    ));
            storageServiceProxy.reserve(new ReserveRequest(id, count));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
