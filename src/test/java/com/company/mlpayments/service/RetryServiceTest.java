package com.company.mlpayments.service;

import com.company.mlpayments.dto.PaymentDtos;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RetryServiceTest {
    @Mock PaymentService paymentService;
    @InjectMocks RetryService service;
    @Test void shouldRetry(){
        when(paymentService.retry(1L)).thenReturn(new PaymentDtos.CreatePaymentResponse("id","u","q",null));
        assertEquals("id", service.retry(1L).paymentId());
    }
}
