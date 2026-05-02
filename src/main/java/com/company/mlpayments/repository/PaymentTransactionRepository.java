package com.company.mlpayments.repository;

import com.company.mlpayments.domain.PaymentStatus;
import com.company.mlpayments.domain.PaymentTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    long countByStatusAndCreatedAtBetween(PaymentStatus status, OffsetDateTime start, OffsetDateTime end);
}
