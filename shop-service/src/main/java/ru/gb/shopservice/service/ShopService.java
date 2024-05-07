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
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ShopService {

    private final RestTemplate template;

    private final HttpHeaders headers;

    private final ProductRepository productRepository;

    private final BankServiceProxy bankServiceProxy;

    private final StorageServiceProxy storageServiceProxy;

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
        return status;
    }

    /*
     * Метод для покупки товара
     */

    public boolean buy(long id, int count) throws Exception {
        if (!reserve(id, count)) {
            throw new Exception("Не удалось зарезервировать товар");
        }
        if (!pay(id, count)) {
            reserve(id, -1 * count);
            throw new Exception("Не удалось оплатить товар");
        }
        if (!sell(id)) {
            reserve(id, -1 * count);
            pay(id, -1 * count);
            throw new Exception("Не удалось передать товар покупателю");
        }
        return true;
    }

    /*
     * Метод для резервирования товара на складе
     */
    private boolean reserve(long id, int count) {
        try {
            storageServiceProxy.reserve(new ReserveRequest(id, count));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * Метод для оплаты товара
     */

    private boolean pay(long id, int count) {
        try {
            BigDecimal price =
                    productRepository.findById(id).orElse(new Product()).getPrice();
            bankServiceProxy.pay(new TransferRequest(
                    2, 1, price.multiply(new BigDecimal(count))));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /*
     * Метод для передачи оплаченного товара покупателю
     */

    private boolean sell(long id) {
        try {
            storageServiceProxy.giveToBuyer(new GiveToBuyerRequest(id));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
