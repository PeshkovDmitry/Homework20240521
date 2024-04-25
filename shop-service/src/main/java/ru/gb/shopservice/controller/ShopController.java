package ru.gb.shopservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.gb.shopservice.dto.storage.ReserveRequest;
import ru.gb.shopservice.dto.ShopStatus;
import ru.gb.shopservice.service.ShopService;

@RestController
@AllArgsConstructor
@RequestMapping("/")
public class ShopController {

    private final ShopService shopService;

    @GetMapping
    public ResponseEntity<ShopStatus> getAllAccounts() {
        return new ResponseEntity<>(shopService.getStatus(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReserveRequest> pay(@RequestBody ReserveRequest request) {
        try {
            shopService.buy(request.getId(), request.getCount());
            return new ResponseEntity<>(request, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(request, HttpStatus.CONFLICT);
        }
    }




}
