# Goods Investimentos

Aplicativo para gerenciamento de investimentos incluindo ações brasileiras, criptomoedas, renda fixa, fundos imobiliários brasileiros e ETFs internacionais.

## Tecnologias

- Java 21
- Spring Boot 3.4.2
- TimescaleDB (extensão do PostgreSQL)
- Spring Data JPA
- Flyway
- Spring Cloud OpenFeign
- Lombok
- Embedded PostgreSQL Database (para testes)
- Docker

## Estrutura do Projeto

O projeto segue uma arquitetura em camadas padrão:

```
src/
├── main/
│   ├── java/
│   │   └── br/com/goods/Goods/Investimentos/
│   │       ├── controllers/    # Endpoints da API REST
│   │       ├── models/
│   │       │   ├── dto/        # Data Transfer Objects
│   │       │   └── entities/   # Entidades JPA
│   │       ├── repositories/   # Repositórios Spring Data JPA
│   │       └── services/       # Lógica de negócios
│   └── resources/
│       ├── application.properties  # Configuração da aplicação
│       └── db/migration/           # Migrações de banco de dados Flyway
└── test/
    ├── java/                       # Classes de teste
    └── resources/
        ├── application.properties  # Configuração de teste
        └── scripts/                # Scripts SQL para testes
```

## Configuração e Execução

### Pré-requisitos
- Java 21
- Maven
- Docker (opcional, para execução em contêiner)

### Executando localmente
1. Clone o repositório
2. Configure o banco de dados TimescaleDB (ou PostgreSQL)
3. Execute a aplicação:
   ```
   mvn spring-boot:run
   ```

### Executando com Docker
1. Construa a imagem Docker:
   ```
   docker build -t goods-investimentos .
   ```
2. Execute o contêiner:
   ```
   docker-compose up
   ```

## Documentação

Para mais informações sobre a estrutura do projeto e padrões de codificação, consulte o arquivo [GUIDELINES.md](GUIDELINES.md).
