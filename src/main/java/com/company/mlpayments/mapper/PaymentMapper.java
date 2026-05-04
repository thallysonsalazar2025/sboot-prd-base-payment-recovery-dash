package com.company.mlpayments.mapper;

import com.company.mlpayments.domain.PaymentTransaction;
import com.company.mlpayments.dto.PaymentDtos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "method", source = "paymentMethod")
    PaymentDtos.PaymentStatusResponse toStatusResponse(PaymentTransaction entity);
}
