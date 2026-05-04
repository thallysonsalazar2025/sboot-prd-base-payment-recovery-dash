package com.company.mlpayments.integration;

import com.company.mlpayments.domain.PaymentStatus;
import com.company.mlpayments.domain.PaymentTransaction;
import com.company.mlpayments.dto.PaymentDtos;
import com.company.mlpayments.repository.PaymentTransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
    "mercadopago.mock-enabled=true"
})
class PaymentProcessIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PaymentTransactionRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    private String getBaseUrl() {
        return "http://localhost:" + port + "/api/payments";
    }

    @Test
    @DisplayName("Deve criar um pagamento e validar as métricas")
    void shouldCreatePaymentAndVerifyMetrics() {
        // Arrange
        var request = Map.of(
            "customerName", "John Doe",
            "email", "john@example.com",
            "description", "Test Payment",
            "amount", 100.00
        );

        // Act - Create
        ResponseEntity<PaymentDtos.CreatePaymentResponse> createResponse = restTemplate.postForEntity(
            getBaseUrl() + "/create", request, PaymentDtos.CreatePaymentResponse.class);

        // Assert - Create
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        assertNotNull(createResponse.getBody().paymentId());

        // Act - Metrics
        ResponseEntity<PaymentDtos.MetricsResponse> metricsResponse = restTemplate.getForEntity(
            getBaseUrl() + "/metrics", PaymentDtos.MetricsResponse.class);

        // Assert - Metrics
        assertEquals(HttpStatus.OK, metricsResponse.getStatusCode());
        assertNotNull(metricsResponse.getBody());
    }

    @Test
    @DisplayName("Deve realizar o retry de um pagamento rejeitado")
    void shouldRetryRejectedPayment() {
        // Arrange
        PaymentTransaction tx = PaymentTransaction.builder()
            .externalId("ext-123")
            .customerName("Jane Doe")
            .email("jane@example.com")
            .description("Rejected Payment")
            .amount(BigDecimal.TEN)
            .status(PaymentStatus.REJECTED)
            .paymentMethod("PIX")
            .build();
        tx = repository.saveAndFlush(tx);

        // Act
        ResponseEntity<PaymentDtos.CreatePaymentResponse> retryResponse = restTemplate.postForEntity(
            getBaseUrl() + "/" + tx.getId() + "/retry",
            HttpEntity.EMPTY,
            PaymentDtos.CreatePaymentResponse.class
        );

        // Assert
        assertEquals(HttpStatus.OK, retryResponse.getStatusCode());
        assertNotNull(retryResponse.getBody());
        assertEquals(PaymentStatus.RETRY_CREATED, retryResponse.getBody().status());

        // Verify database update
        PaymentTransaction updatedTx = repository.findById(tx.getId()).orElseThrow();
        assertEquals(PaymentStatus.RETRY_CREATED, updatedTx.getStatus());
    }

    @Test
    @DisplayName("Deve falhar ao tentar retry de um pagamento que não está REJECTED")
    void shouldFailRetryWhenNotRejected() {
        // Arrange
        PaymentTransaction tx = PaymentTransaction.builder()
            .externalId("ext-456")
            .customerName("Bob")
            .email("bob@example.com")
            .description("Approved Payment")
            .amount(BigDecimal.TEN)
            .status(PaymentStatus.APPROVED)
            .paymentMethod("PIX")
            .build();
        tx = repository.saveAndFlush(tx);

        // Act
        ResponseEntity<String> retryResponse = restTemplate.postForEntity(
            getBaseUrl() + "/" + tx.getId() + "/retry",
            HttpEntity.EMPTY,
            String.class
        );

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, retryResponse.getStatusCode());
    }
}
