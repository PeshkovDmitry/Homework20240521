package ru.gb.shopservice.service;

import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.gb.shopservice.dto.*;
import ru.gb.shopservice.dto.bank.TransferRequest;
import ru.gb.shopservice.dto.storage.GiveToBuyerRequest;
import ru.gb.shopservice.dto.storage.Item;
import ru.gb.shopservice.dto.storage.ReserveRequest;
import ru.gb.shopservice.model.Product;
import ru.gb.shopservice.proxy.BankServiceProxy;
import ru.gb.shopservice.proxy.StorageServiceProxy;
import ru.gb.shopservice.repository.ProductRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ShopService {

    private final ProductRepository productRepository;

    private final BankServiceProxy bankServiceProxy;

    private final StorageServiceProxy storageServiceProxy;

    private final FileGatewayService fileGatewayService;

    private final ReserveService reserveService;

    private final PayService payService;

    private final SellService sellService;


    /*
     * Метод опрашивает микросервис банка и микросервис склада
     * для формирования общего состояния магазина
     */
    public ShopStatus getStatus() {
        ShopStatus status = new ShopStatus();
        status.setPurchases(new ArrayList<>());
        status.setInStorage(new ArrayList<>());
        status.setUserAmount(getUserAmount());
        status = getItemsInStorageAndSellingItems(status);
        log("Выдача данных о состоянии магазина");
        return status;
    }

    /*
     * Метод опрашивает микросервис магазина для получения
     * суммы на счету покупателя
     */

    private BigDecimal getUserAmount() {
        return bankServiceProxy.getAllAccounts().get(1).getAmount();
    }
    /*
     * Метод опрашивает микросервис склада и, сопоставляя полученные
     * данные (id, количество товара на складе, количество купленного товара)
     * с данными из репозитория магазина (id, название и цена),
     * формирует данные о сделанных покупках и товаре в магазине
     */

    private ShopStatus getItemsInStorageAndSellingItems(ShopStatus status) {
        for (Item item: storageServiceProxy.getAllItems()) {
            Product product = productRepository.findById(item.getId()).get();
            if (item.getInShop() > 0) {
                status.getInStorage().add(
                        new Purchase(item.getId(), product.getTitle(), item.getInShop(), product.getPrice())
                );
            }
            if (item.getWithBuyer() > 0) {
                status.getPurchases().add(
                        new Purchase(item.getId(), product.getTitle(), item.getWithBuyer(), product.getPrice())
                );
            }
        }
        log("Выдача данных о купленных и доступных к покупке товарах");
        return status;
    }

    /*
     * Метод для покупки товара
     */

    public boolean buy(long id, int count) throws Exception {
        log(String.format("Запрос на покупку товара с кодом \"%d\" в количестве %d единиц", id, count));
        if (!reserveService.reserve(id, count)) {
            throw new Exception("Не удалось зарезервировать товар");
        }
        if (!payService.pay(id, count)) {
            reserveService.reserve(id, -1 * count);
            throw new Exception("Не удалось оплатить товар");
        }
        if (!sellService.sell(id, count)) {
            reserveService.reserve(id, -1 * count);
            payService.pay(id, -1 * count);
            throw new Exception("Не удалось передать товар покупателю");
        }
        return true;
    }

    /*
     * Метод для логирования сообщений
     */
    private void log(String message) {
        fileGatewayService.writeToFile(
                "log.txt",
                "Shop-service (" + LocalDateTime.now() + "): " + message);
    }

}
