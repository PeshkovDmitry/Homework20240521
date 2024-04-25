package ru.gb.bankservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.bankservice.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
