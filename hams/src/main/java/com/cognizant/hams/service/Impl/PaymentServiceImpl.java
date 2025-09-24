package com.cognizant.hams.service.Impl;

import com.cognizant.hams.constant.PaymentConstants;
import com.cognizant.hams.dto.Request.PaymentCallBackDTO;
import com.cognizant.hams.dto.Request.PaymentRequestDTO;
import com.cognizant.hams.dto.Response.PaymentResponseDTO;
import com.cognizant.hams.dto.Response.PaymentStatusDTO;
import com.cognizant.hams.entity.Appointment;
import com.cognizant.hams.entity.AppointmentStatus;
import com.cognizant.hams.entity.Bill;
import com.cognizant.hams.exception.PaymentException;
import com.cognizant.hams.exception.ResourceNotFoundException;
import com.cognizant.hams.repository.BillRepository;
import com.cognizant.hams.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl {

    private final BillRepository billRepository;
    private final AppointmentRepository appointmentRepository;

    private final ModelMapper modelMapper;

    private static final String SUCCESSFUL_STATUS = "Successful";
    private static final String PENDING_STATUS = "Pending";
    private static final String FAILED_STATUS = "Failed";

    /**
     * Initiates a payment for a specific bill by creating a payment record with PENDING status.
     *
     * @param request The payment request containing appointment ID and payment method.
     * @return A DTO representing the newly created payment record.
     */
    @Transactional
    public PaymentResponseDTO initiatePayment(Long appointmentId, PaymentRequestDTO request) {
        // Find the bill using the correct repository method that returns an Optional.
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "Appointment ID", appointmentId));


        Set<Bill> bills = appointment.getBill();



        // Check for existing payments to prevent double-payment.
        if (bills.stream().anyMatch(b -> b.getPaymentStatus().equalsIgnoreCase(SUCCESSFUL_STATUS))) {
            throw new PaymentException("Payment for this bill is already successful.");
        }
        if (bills.stream().anyMatch(b -> b.getPaymentStatus().equalsIgnoreCase(SUCCESSFUL_STATUS))) {
            throw new PaymentException("Payment for this bill is Pending.");
        }

        Bill bill = new Bill();
        bill.setPaymentStatus(PENDING_STATUS);
        bill.setPaymentMethod(request.getPaymentMethod());
        bill.setAmount(BigDecimal.valueOf(1500L));
        bill.setTimestamp(LocalDateTime.now());
        bill.setAppointment(appointment);

        // Save the updated bill record.
        Bill savedBill = billRepository.save(bill);

        bill.setBillId(savedBill.getBillId());


        // Map and return the response.
        return modelMapper.map(bill, PaymentResponseDTO.class);
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------
    /**
     * Processes a payment response from a callback. Validates the payment and updates the bill status.
     *
     * @param callback The payment callback DTO with payment details.
     * @return The transaction ID generated for the payment.
     */
    @Transactional
    public String processPaymentResponse(Long billId, PaymentCallBackDTO callback) {
        // 1. Find the bill record using the billId from the path.
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill", "ID", billId));

        String transactionId = "txn_" + UUID.randomUUID();

        if(PaymentConstants.validatePayment(
                callback.getType(),
                callback.getIdentifier(),
                callback.getPin(),
                callback.getAmount().floatValue())) {

            bill.setPaymentStatus(SUCCESSFUL_STATUS);
            bill.setTransactionId(transactionId);

        }
        else{
            // 4. Handle failed payment and set the status accordingly.
            bill.setPaymentStatus(FAILED_STATUS);
            bill.setTransactionId(transactionId);

        }
        // 5. Save the updated bill record.
        Bill savedBill = billRepository.save(bill);

        return transactionId;
    }

    //----------------------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * Retrieves the payment status for all bills associated with an appointment.
     *
     * @param appointmentId The ID of the appointment.
     * @return A list of DTOs containing payment status and transaction ID for each bill.
     */
    public List<PaymentStatusDTO> getPaymentStatus(Long appointmentId) {
        List<Bill> billList = billRepository.findAllByAppointment_AppointmentId(appointmentId);

        return billList.stream()
                .map(bill -> new PaymentStatusDTO(bill.getAppointment().getAppointmentId(), bill.getPaymentStatus(), bill.getTransactionId()))
                .collect(Collectors.toList());
    }
}