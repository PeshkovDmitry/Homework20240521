package ru.gb.shopservice.proxy;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.gb.shopservice.dto.bank.Account;
import ru.gb.shopservice.dto.bank.TransferRequest;

import java.util.List;


@FeignClient(name = "bank-service",url = "http://localhost:8080/bank-service")
public interface BankServiceProxy {

    @GetMapping("/all")
    List<Account> getAllAccounts();

    @PostMapping("/pay")
    TransferRequest pay(@RequestBody TransferRequest request);
}
