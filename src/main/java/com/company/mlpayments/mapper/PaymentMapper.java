package com.company.mlpayments.mapper;

import com.company.mlpayments.domain.PaymentTransaction;
import com.company.mlpayments.dto.PaymentDtos;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentDtos.PaymentStatusResponse toStatusResponse(PaymentTransaction entity);
}
