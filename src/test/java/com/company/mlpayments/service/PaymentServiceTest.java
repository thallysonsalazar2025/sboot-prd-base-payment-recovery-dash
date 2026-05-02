package com.company.mlpayments.service;

import com.company.mlpayments.client.MercadoPagoClient;
import com.company.mlpayments.domain.PaymentStatus;
import com.company.mlpayments.domain.PaymentTransaction;
import com.company.mlpayments.dto.PaymentDtos;
import com.company.mlpayments.mapper.PaymentMapper;
import com.company.mlpayments.repository.PaymentTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {
    @Mock PaymentTransactionRepository repository;
    @Mock MercadoPagoClient client;
    @Mock PaymentMapper mapper;
    @InjectMocks PaymentService service;

    @Test void shouldCreate() {
        when(client.createPayment(any(), any())).thenReturn(new MercadoPagoClient.ExternalPayment("e1","u","q", PaymentStatus.PENDING,"PIX"));
        when(repository.save(any())).thenAnswer(i-> { var p=i.getArgument(0, PaymentTransaction.class); p.setId(1L); return p; });
        var resp = service.create(new PaymentDtos.CreatePaymentRequest("A","a@a.com","desc", BigDecimal.TEN));
        assertEquals(PaymentStatus.PENDING, resp.status());
    }
}
