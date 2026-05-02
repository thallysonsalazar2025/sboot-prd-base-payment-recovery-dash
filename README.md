# ML Payment Recovery Dashboard
Projeto backend Java 21 + Spring Boot 3 para monitorar, consultar e reprocessar pagamentos.

## Rodar
```bash
mvn spring-boot:run
```

## Endpoints
- POST /api/payments/create
- GET /api/payments/{id}
- GET /api/payments/metrics
- POST /api/payments/{id}/retry
- GET /api/payments/methods

## Variáveis
- MP_TOKEN
- MP_BASE_URL
- MP_MOCK_ENABLED

## Testes
```bash
mvn test
```
