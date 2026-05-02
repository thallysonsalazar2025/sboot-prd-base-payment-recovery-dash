package com.company.mlpayments.service;

import com.company.mlpayments.domain.PaymentStatus;
import com.company.mlpayments.dto.PaymentDtos;
import com.company.mlpayments.repository.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service @RequiredArgsConstructor
public class MetricsService {
    private final PaymentTransactionRepository repository;
    public PaymentDtos.MetricsResponse metrics(){
        OffsetDateTime start = OffsetDateTime.now().toLocalDate().atStartOfDay().atOffset(OffsetDateTime.now().getOffset());
        OffsetDateTime end = start.plusDays(1);
        long a=repository.countByStatusAndCreatedAtBetween(PaymentStatus.APPROVED,start,end);
        long p=repository.countByStatusAndCreatedAtBetween(PaymentStatus.PENDING,start,end);
        long r=repository.countByStatusAndCreatedAtBetween(PaymentStatus.REJECTED,start,end);
        long e=repository.countByStatusAndCreatedAtBetween(PaymentStatus.EXPIRED,start,end);
        double c=(a+p+r)==0?0d: (double)a/(a+p+r)*100;
        return new PaymentDtos.MetricsResponse(a,p,r,e,c,r+e);
    }
}
