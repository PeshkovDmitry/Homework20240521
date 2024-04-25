package ru.gb.storageservice.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BuyRequest {

  private long senderAccountId;
  private long receiverAccountId;
  private BigDecimal amount;

}
