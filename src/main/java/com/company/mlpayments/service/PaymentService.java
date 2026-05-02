package com.company.mlpayments.service;

import com.company.mlpayments.client.MercadoPagoClient;
import com.company.mlpayments.domain.PaymentStatus;
import com.company.mlpayments.domain.PaymentTransaction;
import com.company.mlpayments.dto.PaymentDtos;
import com.company.mlpayments.exception.NotFoundException;
import com.company.mlpayments.mapper.PaymentMapper;
import com.company.mlpayments.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor @Slf4j
public class PaymentService {
    private final PaymentTransactionRepository repository;
    private final MercadoPagoClient client;
    private final PaymentMapper mapper;

    public PaymentDtos.CreatePaymentResponse create(PaymentDtos.CreatePaymentRequest req){
        var ext = client.createPayment(req.description(), req.amount());
        var tx = repository.save(PaymentTransaction.builder().externalId(ext.id()).customerName(req.customerName()).email(req.email()).description(req.description()).amount(req.amount()).status(ext.status()).paymentMethod(ext.method()).build());
        log.info("Payment created id={} amount={}", tx.getId(), tx.getAmount());
        return new PaymentDtos.CreatePaymentResponse(ext.id(), ext.checkoutUrl(), ext.qrCode(), ext.status());
    }
    public PaymentDtos.PaymentStatusResponse get(Long id){ return mapper.toStatusResponse(repository.findById(id).orElseThrow(()->new NotFoundException("Payment not found"))); }
    public PaymentDtos.PaymentMethodsResponse methods(){ return new PaymentDtos.PaymentMethodsResponse(client.getPaymentMethods()); }
    public PaymentDtos.CreatePaymentResponse retry(Long id){
        var tx = repository.findById(id).orElseThrow(()->new NotFoundException("Payment not found"));
        if (tx.getStatus()!= PaymentStatus.REJECTED) throw new IllegalStateException("Retry allowed only for rejected");
        var ext = client.retryPayment(tx.getExternalId());
        tx.setStatus(ext.status()); repository.save(tx);
        return new PaymentDtos.CreatePaymentResponse(ext.id(), ext.checkoutUrl(), ext.qrCode(), ext.status());
    }
}
