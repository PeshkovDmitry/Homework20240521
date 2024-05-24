package ru.gb.shopservice.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.shopservice.dto.bank.TransferRequest;
import ru.gb.shopservice.model.Product;
import ru.gb.shopservice.proxy.BankServiceProxy;
import ru.gb.shopservice.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class PayService {

    private final FileGatewayService fileGatewayService;

    private final ProductRepository productRepository;

    private final BankServiceProxy bankServiceProxy;

    /*
     * Метод для оплаты товара
     */

    public boolean pay(long id, int count) {
        try {
            fileGatewayService.writeToFile(
                    "log.txt",
                    "Shop-service (" + LocalDateTime.now() + "): "
                            + String.format(
                            "Запрос на покупку товара с кодом \"%d\" в количестве %d единиц",
                            id,
                            count
                    ));
            BigDecimal price =
                    productRepository.findById(id).orElse(new Product()).getPrice();
            bankServiceProxy.pay(new TransferRequest(
                    2, 1, price.multiply(new BigDecimal(count))));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
