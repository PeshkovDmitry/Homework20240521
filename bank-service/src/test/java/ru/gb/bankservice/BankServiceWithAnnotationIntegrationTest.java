package ru.gb.bankservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.gb.bankservice.dto.TransferRequest;
import ru.gb.bankservice.model.Account;
import ru.gb.bankservice.repository.AccountRepository;
import ru.gb.bankservice.service.BankService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class BankServiceWithAnnotationIntegrationTest {

    @Autowired
    private BankService service;

    @MockBean
    private AccountRepository repository;

    @Test
    public void transferMoneyCorrectFlow() throws Exception {

        //Блок предусловия
        Account source = new Account(1L, "Ivan", new BigDecimal(1000));
        Account destination = new Account(2L, "Olga", new BigDecimal(1000));

        TransferRequest request = new TransferRequest();
        request.setSenderAccountId(1L);
        request.setReceiverAccountId(2L);
        request.setAmount(new BigDecimal(100));

        when(repository.findById(source.getId())).thenReturn(Optional.of(source));
        when(repository.findById(destination.getId())).thenReturn(Optional.of(destination));

        //Блок действия (вызова метода)
        service.transferMoney(request);

        //Блок проверки действия
        verify(repository).save(new Account(1L, "Ivan",new BigDecimal(900)));
        verify(repository).save(new Account(2L, "Olga",new BigDecimal(1100)));

    }

}
