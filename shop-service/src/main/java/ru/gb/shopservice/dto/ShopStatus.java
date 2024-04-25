package ru.gb.shopservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShopStatus {
    private BigDecimal userAmount;
    private List<Purchase> purchases;
    private List<Purchase> inStorage;
}
