package com.company.mlpayments.integration;

import com.company.mlpayments.domain.PaymentStatus;
import com.company.mlpayments.domain.PaymentTransaction;
import com.company.mlpayments.repository.PaymentTransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PaymentIntegrationTest {
    @LocalServerPort int port;
    @Autowired TestRestTemplate rest;
    @Autowired PaymentTransactionRepository repository;

    @Test void createAndMetrics(){
        var body = Map.of("customerName","A","email","a@a.com","description","d","amount",10);
        ResponseEntity<String> create = rest.postForEntity("http://localhost:"+port+"/api/payments/create", body, String.class);
        assertEquals(HttpStatus.OK, create.getStatusCode());
        assertEquals(HttpStatus.OK, rest.getForEntity("http://localhost:"+port+"/api/payments/metrics", String.class).getStatusCode());
    }

    @Test void retry(){
        var tx = repository.save(PaymentTransaction.builder().externalId("x").customerName("a").email("a@a.com").description("d").amount(BigDecimal.ONE).status(PaymentStatus.REJECTED).paymentMethod("PIX").build());
        assertEquals(HttpStatus.OK, rest.postForEntity("http://localhost:"+port+"/api/payments/"+tx.getId()+"/retry", HttpEntity.EMPTY, String.class).getStatusCode());
    }
}
