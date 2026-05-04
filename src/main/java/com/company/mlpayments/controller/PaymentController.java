package com.company.mlpayments.controller;

import com.company.mlpayments.dto.PaymentDtos;
import com.company.mlpayments.service.MetricsService;
import com.company.mlpayments.service.PaymentService;
import com.company.mlpayments.service.RetryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final MetricsService metricsService;
    private final RetryService retryService;

    @PostMapping("/create")
    public PaymentDtos.CreatePaymentResponse create(@Valid @RequestBody PaymentDtos.CreatePaymentRequest request) {
        return paymentService.create(request);
    }

    @GetMapping("/{id}")
    public PaymentDtos.PaymentStatusResponse get(@PathVariable("id") Long id) {
        return paymentService.get(id);
    }

    @GetMapping("/metrics")
    public PaymentDtos.MetricsResponse metrics() {
        return metricsService.metrics();
    }

    @PostMapping("/{id}/retry")
    public PaymentDtos.CreatePaymentResponse retry(@PathVariable("id") Long id) {
        return retryService.retry(id);
    }

    @GetMapping("/methods")
    public PaymentDtos.PaymentMethodsResponse methods() {
        return paymentService.methods();
    }
}
