package io.lending.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class LoanDTO {
    private Long id;
    private Long subscriberId;
    private BigDecimal amount;
    private String currency;
}
