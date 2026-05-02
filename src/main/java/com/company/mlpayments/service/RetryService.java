package com.company.mlpayments.service;

import com.company.mlpayments.dto.PaymentDtos;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RetryService {
    private final PaymentService paymentService;
    public PaymentDtos.CreatePaymentResponse retry(Long id){ return paymentService.retry(id); }
}
