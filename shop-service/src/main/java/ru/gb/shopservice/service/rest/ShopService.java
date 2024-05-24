package ru.gb.shopservice.service.rest;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.shopservice.dto.*;
import ru.gb.shopservice.dto.storage.Item;
import ru.gb.shopservice.model.Product;
import ru.gb.shopservice.proxy.BankServiceProxy;
import ru.gb.shopservice.proxy.StorageServiceProxy;
import ru.gb.shopservice.repository.ProductRepository;
import ru.gb.shopservice.service.LoggingService;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@AllArgsConstructor
public class ShopService {

    private final ProductRepository productRepository;

    private final BankServiceProxy bankServiceProxy;

    private final StorageServiceProxy storageServiceProxy;

    private final ReserveService reserveService;

    private final PayService payService;

    private final SellService sellService;

    private final LoggingService loggingService;


    /*
     * Метод опрашивает микросервис банка и микросервис склада
     * для формирования общего состояния магазина
     */
    public ShopStatus getStatus() {
        ShopStatus status = new ShopStatus();
        status.setPurchases(new ArrayList<>());
        status.setInStorage(new ArrayList<>());
        status.setUserAmount(getUserAmount());
        getItemsInStorageAndSellingItems(status);
        loggingService.log("Выдача данных о состоянии магазина");
        return status;
    }

    /*
     * Метод для покупки товара
     */

    public void buy(long id, int count) {
        loggingService.log(String.format("Запрос на покупку товара с кодом \"%d\" в количестве %d единиц", id, count));
        reserveService.process(id, count);
        payService.process(id, count);
        sellService.process(id, count);
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

    private void getItemsInStorageAndSellingItems(ShopStatus status) {
        for (Item item: storageServiceProxy.getAllItems()) {
            Product product = productRepository.findById(item.getId()).get();
            getInStorage(status, item, product);
            getWithBuyer(status, item, product);
        }
        loggingService.log("Выдача данных о купленных и доступных к покупке товарах");
    }

    /*
     * Метод определения количества данного продукта на складе
     */

    private void getInStorage(ShopStatus status, Item item, Product product) {
        if (item.getInShop() > 0) {
            status.getInStorage().add(
                    new Purchase(item.getId(), product.getTitle(), item.getInShop(), product.getPrice())
            );
        }
    }

    /*
     * Метод определения количества данного продукта у покупателя
     */

    private void getWithBuyer(ShopStatus status, Item item, Product product) {
        if (item.getWithBuyer() > 0) {
            status.getPurchases().add(
            new Purchase(item.getId(), product.getTitle(), item.getWithBuyer(), product.getPrice())
            );
        }
    }

}
