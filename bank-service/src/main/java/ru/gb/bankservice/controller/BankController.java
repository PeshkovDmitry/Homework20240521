package ru.gb.bankservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.gb.bankservice.dto.TransferRequest;
import ru.gb.bankservice.model.Account;
import ru.gb.bankservice.service.BankService;
import ru.gb.bankservice.service.FileGatewayService;

import java.util.List;

@RestController
@AllArgsConstructor
public class BankController {

    private final BankService bankService;

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        return new ResponseEntity<>(bankService.getAll(), HttpStatus.OK);
    }

    @PostMapping("/pay")
    public ResponseEntity<TransferRequest> pay(@RequestBody TransferRequest request) {
        try {
            bankService.transferMoney(request);
            return new ResponseEntity<>(request, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(request, HttpStatus.CONFLICT);
        }
    }

}
