package ru.gb.bankservice.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.bankservice.dto.TransferRequest;
import ru.gb.bankservice.model.Account;
import ru.gb.bankservice.repository.AccountRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class BankService {

    private final AccountRepository accountRepository;

    private final FileGatewayService fileGatewayService;

    public List<Account> getAll() {
        fileGatewayService.writeToFile(
                "log.txt",
                "Bank-service (" + LocalDateTime.now() + "): "
                + "Выдача полных данных");
        return accountRepository.findAll();
    }

    @Transactional
    public void transferMoney(TransferRequest transferRequest) throws Exception {
        Account sender = accountRepository
                .findById(transferRequest.getSenderAccountId())
                .orElseThrow(() -> new Exception("Отправитель не найден"));
        Account receiver = accountRepository
                .findById(transferRequest.getReceiverAccountId())
                .orElseThrow(() -> new Exception("Получатель не найден"));
        if (sender.getAmount().compareTo(transferRequest.getAmount()) < 0) {
            throw new Exception("У отправителя недостаточно средств");
        }
        fileGatewayService.writeToFile(
                "log.txt",
                "Bank-service (" + LocalDateTime.now() + "): "
                        + String.format(
                                "Перевод от %s к %s %f у.е.",
                                sender.getName(),
                                receiver.getName(),
                                transferRequest.getAmount()
                        ));
        sender.setAmount(
                sender.getAmount().subtract(transferRequest.getAmount()));
        receiver.setAmount(
                receiver.getAmount().add(transferRequest.getAmount()));
        accountRepository.save(sender);
        accountRepository.save(receiver);

    }
}
