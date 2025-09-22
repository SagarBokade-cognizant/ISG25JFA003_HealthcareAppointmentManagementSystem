package com.cognizant.hams.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentStatusDTO {

    private Long appointmentId;
    private String paymentStatus;
    private String transactionId;
}