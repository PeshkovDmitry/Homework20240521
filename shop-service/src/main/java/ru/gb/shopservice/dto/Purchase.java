package ru.gb.shopservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Purchase {
    private long id;
    private String title;
    private int count;
    private BigDecimal price;
}
