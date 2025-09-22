package com.cognizant.hams.service;

import com.cognizant.hams.dto.Request.PaymentCallBackDTO;
import com.cognizant.hams.dto.Request.PaymentRequestDTO;
import com.cognizant.hams.dto.Response.PaymentResponseDTO;
import com.cognizant.hams.dto.Response.PaymentStatusDTO;

import java.util.List;

public interface PaymentService {

    PaymentResponseDTO initiatePayment(Long appointmentId, PaymentRequestDTO request);
    String processPaymentResponse(Long billId, PaymentCallBackDTO callback);
    List<PaymentStatusDTO> getPaymentStatus(Long appointmentId);
}
