package ru.gb.shopservice.service.rest;

import org.springframework.stereotype.Service;
import ru.gb.shopservice.dto.bank.TransferRequest;
import ru.gb.shopservice.model.Product;
import ru.gb.shopservice.proxy.BankServiceProxy;
import ru.gb.shopservice.repository.ProductRepository;
import ru.gb.shopservice.service.LoggingService;

import java.math.BigDecimal;

@Service
public class PayService extends AbstractService {

    private final ProductRepository productRepository;

    private final BankServiceProxy bankServiceProxy;

    private final ReserveService reserveService;

    public PayService(
            ProductRepository productRepository,
            BankServiceProxy bankServiceProxy,
            ReserveService reserveService,
            LoggingService loggingService) {
        super(
                loggingService,
                "Запрос на покупку товара с кодом \"%d\" в количестве %d единиц",
                "Не удалось оплатить товар"
        );
        this.productRepository = productRepository;
        this.bankServiceProxy = bankServiceProxy;
        this.reserveService = reserveService;
    }


    @Override
    void work(long id, int count) {
        BigDecimal price =
                productRepository.findById(id).orElse(new Product()).getPrice();
        bankServiceProxy.pay(new TransferRequest(
                2, 1, price.multiply(new BigDecimal(count))));
    }

    @Override
    void rollback(long id, int count) {
        reserveService.process(id, -1 * count);
    }

}
