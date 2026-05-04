# ML Payment Recovery Dashboard

Este projeto é um acelerador de monitoramento e recuperação de pagamentos integrados com a API do Mercado Pago. Ele permite a criação de transações, consulta de status, visualização de métricas de conversão e reprocessamento automático de pagamentos rejeitados.

O sistema foi desenhado para ser resiliente e escalável, utilizando Spring Boot 3 e Java 21, com suporte nativo a observabilidade e rastreabilidade (Correlation ID).

## 🚀 Tecnologias
- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA**
- **Spring WebFlux (WebClient para chamadas externas)**
- **H2 Database (Memória para desenvolvimento)**
- **Flyway (Migração de banco de dados)**
- **Lombok & MapStruct**

## 🛠️ Como Executar
```bash
mvn spring-boot:run
```
O servidor iniciará por padrão em `http://localhost:8080`.

## 🐳 Docker (Ambiente de Produção/Dev)
Para subir o ambiente completo (API + Banco de Dados PostgreSQL) utilizando Docker:

```bash
docker-compose up --build -d
```
- **API**: `http://localhost:8080`
- **PostgreSQL**: `localhost:5433` (externo) / `db:5432` (interno)

## 📌 Endpoints da API

### 1. Criar Pagamento
Cria uma nova intenção de pagamento no Mercado Pago e registra localmente.
- **POST** `/api/payments/create`
- **Body**:
```json
{
  "customerName": "Nome do Cliente",
  "email": "cliente@email.com",
  "description": "Descrição do Produto",
  "amount": 100.00
}
```

### 2. Consultar Pagamento
Retorna os detalhes e o status atual de uma transação específica.
- **GET** `/api/payments/{id}`

### 3. Métricas de Recuperação
Exibe estatísticas consolidadas do dia para análise de funil e conversão.
- **GET** `/api/payments/metrics`
- **Retorno**: Quantidade de aprovados, pendentes, rejeitados, taxa de conversão e oportunidades de recuperação.

### 4. Reprocessar Pagamento (Retry)
Permite tentar novamente um pagamento que foi previamente rejeitado.
- **POST** `/api/payments/{id}/retry`
- **Regra**: Só é permitido para transações com status `REJECTED`.

### 5. Métodos de Pagamento
Lista os métodos de pagamento aceitos (ex: PIX, CREDIT_CARD).
- **GET** `/api/payments/methods`

## ⚙️ Configurações (Variáveis de Ambiente)
As seguintes variáveis podem ser configuradas no `application.yml` ou passadas como variáveis de ambiente:

| Variável | Descrição | Valor Padrão |
|----------|-----------|--------------|
| `MP_TOKEN` | Token de acesso do Mercado Pago | - |
| `MP_BASE_URL` | URL base da API do Mercado Pago | `https://api.mercadopago.com` |
| `MP_MOCK_ENABLED` | Habilita modo simulação (sem chamadas reais) | `true` |
| `DB_URL` | URL do banco de dados (Perfil Prod) | - |

## 🧪 Testes
Para rodar os testes unitários e de integração:
```bash
mvn test
```
