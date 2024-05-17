package ru.gb.bankservice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.gb.bankservice.dto.TransferRequest;
import ru.gb.bankservice.model.Account;
import ru.gb.bankservice.repository.AccountRepository;
import ru.gb.bankservice.service.BankService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BankServiceWithAnnotationUnitTest {

    @InjectMocks
    private BankService service;

    @Mock
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

        given(repository.findById(source.getId())).willReturn(Optional.of(source));
        given(repository.findById(destination.getId())).willReturn(Optional.of(destination));

        //Блок действия (вызова метода)
        service.transferMoney(request);

        //Блок проверки действия
        verify(repository).save(new Account(1L, "Ivan",new BigDecimal(900)));
        verify(repository).save(new Account(2L, "Olga",new BigDecimal(1100)));

    }

}
