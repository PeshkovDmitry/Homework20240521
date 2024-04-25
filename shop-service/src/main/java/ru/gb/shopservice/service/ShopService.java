package ru.gb.shopservice.service;

import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.gb.shopservice.dto.*;
import ru.gb.shopservice.dto.bank.Account;
import ru.gb.shopservice.dto.bank.TransferRequest;
import ru.gb.shopservice.dto.storage.GiveToBuyerRequest;
import ru.gb.shopservice.dto.storage.Item;
import ru.gb.shopservice.dto.storage.ReserveRequest;
import ru.gb.shopservice.model.Product;
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
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Account[]> entity = new HttpEntity<>(headers);
        ResponseEntity<Account[]> response = template.exchange(
                "http://localhost:8080/bank-service/all",
                HttpMethod.GET,
                entity,
                Account[].class);
        return response.getBody()[1].getAmount();
    }

    /*
     * Метод опрашивает микросервис склада и, сопоставляя полученные
     * данные (id, количество товара на складе, количество купленного товара)
     * с данными из репозитория магазина (id, название и цена),
     * формирует данные о сделанных покупках и товаре в магазине
     */
    private ShopStatus getItemsInStorageAndSellingItems(ShopStatus status) {
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Item[]> entity = new HttpEntity<>(headers);
        ResponseEntity<Item[]> response = template.exchange(
                "http://localhost:8080/storage-service/all",
                HttpMethod.GET,
                entity,
                Item[].class);
        for (Item item: response.getBody()) {
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
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<ReserveRequest> entity = new HttpEntity<>(
                new ReserveRequest(id, count),
                headers);
        ResponseEntity<ReserveRequest> response = template.exchange(
                "http://localhost:8080/storage-service/reserve",
                HttpMethod.POST,
                entity,
                ReserveRequest.class);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return true;
        }
        return false;
    }

    /*
     * Метод для оплаты товара
     */

    private boolean pay(long id, int count) {
        BigDecimal price =
            productRepository.findById(id).orElse(new Product()).getPrice();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<TransferRequest> entity = new HttpEntity<>(
                new TransferRequest(
                        2, 1, price.multiply(new BigDecimal(count))),
                headers);
        ResponseEntity<TransferRequest> response = template.exchange(
                "http://localhost:8080/bank-service/pay",
                HttpMethod.POST,
                entity,
                TransferRequest.class);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return true;
        }
        return false;
    }

    /*
     * Метод для передачи оплаченного товара покупателю
     */

    private boolean sell(long id) {
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<GiveToBuyerRequest> entity = new HttpEntity<>(
                new GiveToBuyerRequest(id),
                headers);
        ResponseEntity<GiveToBuyerRequest> response = template.exchange(
                "http://localhost:8080/storage-service/sell",
                HttpMethod.POST,
                entity,
                GiveToBuyerRequest.class);
        if (response.getStatusCode().equals(HttpStatus.OK)) {
            return true;
        }
        return false;
    }

}
