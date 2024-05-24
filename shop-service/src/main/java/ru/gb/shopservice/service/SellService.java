package ru.gb.shopservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.shopservice.dto.storage.GiveToBuyerRequest;
import ru.gb.shopservice.proxy.StorageServiceProxy;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SellService {

    private final StorageServiceProxy storageServiceProxy;

    private final FileGatewayService fileGatewayService;

    /*
     * Метод для передачи оплаченного товара покупателю
     */

    public boolean sell(long id, int count) {
        try {
            fileGatewayService.writeToFile(
                    "log.txt",
                    "Shop-service (" + LocalDateTime.now() + "): "
                            + String.format(
                            "Запрос на выдачу товара с кодом \"%d\"",
                            id
                    ));
            storageServiceProxy.giveToBuyer(new GiveToBuyerRequest(id));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
