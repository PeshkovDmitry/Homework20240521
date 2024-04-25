package ru.gb.storageservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.gb.storageservice.dto.BuyRequest;
import ru.gb.storageservice.dto.ReserveRequest;
import ru.gb.storageservice.model.Product;
import ru.gb.storageservice.service.StorageService;

import java.util.List;

@RestController
@AllArgsConstructor
public class StorageController {

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
    public ResponseEntity<BuyRequest> pay(@RequestBody BuyRequest request) {
        try {
            storageService.setSelling(request.getId());
            return new ResponseEntity<>(request, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(request, HttpStatus.CONFLICT);
        }
    }


}
