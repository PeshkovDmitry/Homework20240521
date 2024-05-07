package ru.gb.shopservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.gb.shopservice.dto.storage.GiveToBuyerRequest;
import ru.gb.shopservice.dto.storage.Item;
import ru.gb.shopservice.dto.storage.ReserveRequest;

import java.util.List;

@FeignClient(name = "storage-service",url = "http://localhost:8080/storage-service")
public interface StorageServiceProxy {

    @GetMapping("/all")
    List<Item> getAllItems();

    @PostMapping("/reserve")
    ReserveRequest reserve(@RequestBody ReserveRequest request);

    @PostMapping("/sell")
    GiveToBuyerRequest giveToBuyer(@RequestBody GiveToBuyerRequest request);

}
