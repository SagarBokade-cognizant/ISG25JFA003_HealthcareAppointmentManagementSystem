package com.cognizant.hams.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentCallBackDTO {
    private Long billId;
    private String type;
    private String identifier;
    private Integer pin;
    private Float amount;
}