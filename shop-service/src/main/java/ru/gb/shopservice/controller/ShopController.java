package ru.gb.shopservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.gb.shopservice.dto.GiveToBuyerRequest;
import ru.gb.shopservice.dto.ReserveRequest;
import ru.gb.shopservice.model.Product;
import ru.gb.shopservice.service.StorageService;

import java.util.List;

@RestController
@AllArgsConstructor
public class ShopController {

    private final StorageService storageService;

    @GetMapping("/all")
    public ResponseEntity<List<Product>> getAllAccounts() {
        return new ResponseEntity<>(storageService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/reserve")
    public ResponseEntity<ReserveRequest> pay(@RequestBody ReserveRequest request) {
        try {
            storageService.setReserved(request.getId(), request.getCount());
            return new ResponseEntity<>(request, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(request, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/sell")
    public ResponseEntity<GiveToBuyerRequest> pay(@RequestBody GiveToBuyerRequest request) {
        try {
            storageService.setSelling(request.getId());
            return new ResponseEntity<>(request, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(request, HttpStatus.CONFLICT);
        }
    }


}
