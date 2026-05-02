package com.company.mlpayments.dto;

import com.company.mlpayments.domain.PaymentStatus;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public class PaymentDtos {
    public record CreatePaymentRequest(@NotBlank String customerName, @Email String email, @NotBlank String description, @DecimalMin("0.01") BigDecimal amount) {}
    public record CreatePaymentResponse(String paymentId, String checkoutUrl, String qrCode, PaymentStatus status) {}
    public record PaymentStatusResponse(Long id, PaymentStatus status, String method, BigDecimal amount, OffsetDateTime createdAt) {}
    public record PaymentMethodsResponse(List<String> methods) {}
    public record MetricsResponse(long approvedToday,long pendingToday,long rejectedToday,long expiredPixToday,double conversionRate,long recoveryOpportunities) {}
    public record ApiResponse<T>(String correlationId, T data) {}
}
