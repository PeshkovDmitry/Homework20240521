package ru.gb.bankservice.service;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import ru.gb.bankservice.model.Account;
import ru.gb.bankservice.repository.AccountRepository;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class InitialAccountRepositoryLoadingService implements CommandLineRunner {

    private final AccountRepository accountRepository;

    @Override
    public void run(String... args) throws Exception {

        accountRepository.save(
                new Account(1L, "Магазин", new BigDecimal(1000)));

        accountRepository.save(
                new Account(2L, "Иван", new BigDecimal(1000)));

    }
}
