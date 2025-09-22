package com.cognizant.hams.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bills")
@Data
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long billId;

    //    Relation mapping --------------
    @OneToOne
    @JoinColumn(name = "appointmentId")
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "patientId")
    private Patient patient;

    // variables ----------
    @Column(name = "Method", length = 50)
    private String paymentMethod;
    @Column(name = "payment_status")
    private String paymentStatus;
    @Column(name = "TransactionID", length = 100)
    private String transactionId;


    @Column(name = "Amount", precision = 10, scale = 2)
    private BigDecimal amount;
    @Column(name = "Timestamp")
    private LocalDateTime timestamp;
    @Column(name = "total")
    private BigDecimal total;

}