package com.cognizant.hams.dto.Response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class PaymentResponseDTO {

    private Integer billId;

    private String method;

    private BigDecimal amount;

    private String transactionId;

    private LocalDateTime timestamp;

    private String paymentStatus;
}
