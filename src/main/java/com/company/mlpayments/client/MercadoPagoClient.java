package com.company.mlpayments.client;

import com.company.mlpayments.domain.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MercadoPagoClient {
    private final WebClient webClient;
    @Value("${mercadopago.mock-enabled:true}") private boolean mock;

    public ExternalPayment createPayment(String description, BigDecimal amount) {
        if (mock) return new ExternalPayment(UUID.randomUUID().toString(), "https://mock.checkout", "mock-qr", PaymentStatus.PENDING, "PIX");
        return webClient.post().uri("/v1/payments").retrieve().bodyToMono(ExternalPayment.class).block();
    }
    public PaymentStatus getPaymentStatus(String id){ return mock ? PaymentStatus.PENDING : PaymentStatus.APPROVED; }
    public List<String> getPaymentMethods(){ return List.of("PIX","CREDIT_CARD","BOLETO"); }
    public ExternalPayment retryPayment(String id){ return new ExternalPayment(UUID.randomUUID().toString(),"https://mock.checkout/retry","retry-qr",PaymentStatus.RETRY_CREATED,"PIX"); }

    public record ExternalPayment(String id,String checkoutUrl,String qrCode, PaymentStatus status, String method){}
}
