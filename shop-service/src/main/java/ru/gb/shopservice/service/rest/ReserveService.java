package ru.gb.shopservice.service.rest;

import org.springframework.stereotype.Service;
import ru.gb.shopservice.dto.storage.ReserveRequest;
import ru.gb.shopservice.proxy.StorageServiceProxy;
import ru.gb.shopservice.service.LoggingService;

@Service
public class ReserveService extends AbstractService {

    private final StorageServiceProxy storageServiceProxy;

    public ReserveService(StorageServiceProxy storageServiceProxy, LoggingService loggingService) {
        super(
                loggingService,
                "Запрос на резервирование товара с кодом \"%d\" в количестве %d единиц",
                "Не удалось зарезервировать товар"
        );
        this.storageServiceProxy = storageServiceProxy;
    }

    @Override
    void work(long id, int count) {
        storageServiceProxy.reserve(new ReserveRequest(id, count));
    }

    @Override
    void rollback(long id, int count) {
    }

}
