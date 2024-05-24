package ru.gb.shopservice.service.rest;

import org.springframework.stereotype.Service;
import ru.gb.shopservice.dto.storage.GiveToBuyerRequest;
import ru.gb.shopservice.proxy.StorageServiceProxy;
import ru.gb.shopservice.service.LoggingService;

@Service
public class SellService extends AbstractService {

    private final StorageServiceProxy storageServiceProxy;

    private final ReserveService reserveService;

    private final PayService payService;

    public SellService(
            StorageServiceProxy storageServiceProxy,
            ReserveService reserveService,
            PayService payService,
            LoggingService loggingService
    ) {
        super(
                loggingService,
                "Запрос на выдачу товара с кодом \"%d\"",
                "Не удалось передать товар покупателю"
        );
        this.storageServiceProxy = storageServiceProxy;
        this.reserveService = reserveService;
        this.payService = payService;
    }

    @Override
    void work(long id, int count) {
        storageServiceProxy.giveToBuyer(new GiveToBuyerRequest(id));
    }

    @Override
    void rollback(long id, int count) {
        reserveService.process(id, -1 * count);
        payService.process(id, -1 * count);
    }

}
