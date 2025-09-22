package com.cognizant.hams.dto.Request;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Long appointmentId;
    private String paymentMethod;
}
