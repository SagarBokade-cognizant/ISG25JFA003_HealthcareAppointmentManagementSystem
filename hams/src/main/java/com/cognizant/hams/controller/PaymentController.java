package com.cognizant.hams.controller;

import com.cognizant.hams.dto.Request.PaymentCallBackDTO;
import com.cognizant.hams.dto.Request.PaymentRequestDTO;
import com.cognizant.hams.dto.Response.PaymentResponseDTO;
import com.cognizant.hams.dto.Response.PaymentStatusDTO;
import com.cognizant.hams.service.Impl.PaymentServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentServiceImpl paymentServiceImpl;

    /**
     * Initiates a payment for a specific appointment bill.
     * Maps to a POST request at /payments/initiate.
     * @param request The PaymentRequestDTO containing the appointment ID and payment method.
     * @return A ResponseEntity with the PaymentResponseDTO and HTTP status.
     */
    @PostMapping("/initiate")
    public ResponseEntity<PaymentResponseDTO> initiatePayment(@RequestBody PaymentRequestDTO request) {
        return new ResponseEntity<>(paymentServiceImpl.initiatePayment(request.getAppointmentId(), request), HttpStatus.CREATED);
    }

    /**
     * Handles a dummy callback for payment processing.
     * This endpoint simulates a response from a payment gateway.
     * @param callback The PaymentCallBackDTO containing payment details.
     * @param billId The ID of the bill to update.
     * @return A ResponseEntity with the transaction ID and HTTP status.
     */
    @PostMapping("/dummy-callback/{billId}")
    public ResponseEntity<String> dummyCallback(@PathVariable("billId") Long billId, @RequestBody PaymentCallBackDTO callback) {
        String transactionId = paymentServiceImpl.processPaymentResponse(billId, callback);
        return new ResponseEntity<>("Callback processed payment's transaction ID : " + transactionId, HttpStatus.ACCEPTED);
    }

    /**
     * Retrieves the payment status for all bills associated with an appointment.
     * Maps to a GET request at /payments/status/appointment/{appointmentId}.
     * @param appointmentId The ID of the appointment.
     * @return A ResponseEntity with a list of PaymentStatusDTOs and HTTP status.
     */
    @GetMapping("/status/appointment/{appointmentId}")
    public ResponseEntity<List<PaymentStatusDTO>> getPaymentStatus(@PathVariable("appointmentId") Long appointmentId) {
        List<PaymentStatusDTO> list = paymentServiceImpl.getPaymentStatus(appointmentId);
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}