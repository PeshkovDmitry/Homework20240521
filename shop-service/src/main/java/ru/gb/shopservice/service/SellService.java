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

    private final ReserveService reserveService;

    private final PayService payService;

    private final LoggingService loggingService;

    /*
     * Метод для передачи оплаченного товара покупателю
     */

    public void sell(long id, int count) {
        try {
            loggingService.log(String.format("Запрос на выдачу товара с кодом \"%d\"", id));
            storageServiceProxy.giveToBuyer(new GiveToBuyerRequest(id));
        } catch (Exception e) {
            reserveService.reserve(id, -1 * count);
            payService.pay(id, -1 * count);
            throw new RuntimeException("Не удалось передать товар покупателю");
        }
    }

}
