package ru.gb.storageservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.gb.storageservice.model.Item;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
