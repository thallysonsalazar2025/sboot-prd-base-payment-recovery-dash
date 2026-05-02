package com.company.mlpayments.service;

import com.company.mlpayments.domain.PaymentStatus;
import com.company.mlpayments.repository.PaymentTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MetricsServiceTest {
    @Mock PaymentTransactionRepository repository;
    @InjectMocks MetricsService service;
    @Test void shouldBuildMetrics(){
        when(repository.countByStatusAndCreatedAtBetween(org.mockito.ArgumentMatchers.eq(PaymentStatus.APPROVED), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).thenReturn(2L);
        when(repository.countByStatusAndCreatedAtBetween(org.mockito.ArgumentMatchers.eq(PaymentStatus.PENDING), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).thenReturn(1L);
        when(repository.countByStatusAndCreatedAtBetween(org.mockito.ArgumentMatchers.eq(PaymentStatus.REJECTED), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).thenReturn(1L);
        when(repository.countByStatusAndCreatedAtBetween(org.mockito.ArgumentMatchers.eq(PaymentStatus.EXPIRED), org.mockito.ArgumentMatchers.any(), org.mockito.ArgumentMatchers.any())).thenReturn(0L);
        assertNotNull(service.metrics());
    }
}
