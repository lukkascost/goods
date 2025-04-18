# Goods Investimentos - Project Guidelines

## Project Overview
Goods Investimentos is a Spring Boot application for managing various types of investments including Brazilian stocks (Ações BR), cryptocurrencies (Cripto), fixed income (Renda Fixa), Brazilian real estate funds (FII BR), and international ETFs.

## Technology Stack
- Java 21
- Spring Boot 3.4.2
- Spring Data JPA
- PostgreSQL Database
- Flyway for database migrations
- Spring Cloud with Resilience4j and OpenFeign
- Lombok for reducing boilerplate code
- TestContainers for integration testing

## Project Structure
The project follows a standard layered architecture:

```
src/
├── main/
│   ├── java/
│   │   └── br/com/goods/Goods/Investimentos/
│   │       ├── controllers/    # REST API endpoints
│   │       ├── models/
│   │       │   ├── dto/        # Data Transfer Objects
│   │       │   └── entities/   # JPA Entities
│   │       ├── repositories/   # Spring Data JPA repositories
│   │       └── services/       # Business logic
│   └── resources/
│       ├── application.properties  # Application configuration
│       └── db/migration/           # Flyway database migrations
└── test/
    ├── java/                       # Test classes
    └── resources/
        └── application.properties  # Test configuration
```

## Coding Standards

### Controllers
- Use `@RestController` and `@RequestMapping` annotations
- Implement constructor injection for dependencies
- Use DTOs for request and response
- Return `ResponseEntity` with appropriate HTTP status codes
- Handle exceptions properly
- Use validation annotations (`@Valid`) for request DTOs
- Follow RESTful API design principles

Example:
```java
@RestController
@RequestMapping("/api/lancamentos/acoes-br")
public class LancamentoAcaoBrController {
    private final LancamentoAcaoBrService service;

    @Autowired
    public LancamentoAcaoBrController(LancamentoAcaoBrService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<LancamentoAcaoBrResponseDTO>> findAll() {
        return ResponseEntity.ok(service.findAll());
    }

    @PostMapping
    public ResponseEntity<LancamentoAcaoBrResponseDTO> create(@Valid @RequestBody LancamentoAcaoBrRequestDTO requestDTO) {
        LancamentoAcaoBrResponseDTO responseDTO = service.create(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
}
```

### Services
- Use `@Service` annotation
- Implement constructor injection for dependencies
- Use `@Transactional` annotations (with `readOnly = true` for queries)
- Implement entity-to-DTO and DTO-to-entity conversion methods
- Handle exceptions properly
- Use clear method naming conventions

Example:
```java
@Service
public class LancamentoCriptoService {
    private final LancamentoCriptoRepository repository;

    @Autowired
    public LancamentoCriptoService(LancamentoCriptoRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<LancamentoCriptoResponseDTO> findAll() {
        return repository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private LancamentoCriptoResponseDTO convertToResponseDTO(LancamentoCriptoEntity entity) {
        LancamentoCriptoResponseDTO responseDTO = new LancamentoCriptoResponseDTO();
        BeanUtils.copyProperties(entity, responseDTO);
        return responseDTO;
    }
}
```

### Entities
- Use JPA annotations (`@Entity`, `@Table`, `@Column`, etc.)
- Use Lombok annotations (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`)
- Define appropriate column constraints (nullable, length, precision, scale)
- Use appropriate data types (BigDecimal for financial values)

Example:
```java
@Entity
@Table(name = "lancamento_cripto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoCriptoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ativo", nullable = false, length = 10)
    private String ativo;

    @Column(name = "preco_ativo", nullable = false, precision = 20, scale = 8)
    private BigDecimal precoAtivo;
}
```

### DTOs
- Use Lombok annotations (`@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`)
- Use validation annotations (`@NotNull`, `@NotBlank`, `@Size`, etc.) with custom error messages
- Match field types with corresponding entity fields

Example:
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoCriptoRequestDTO {
    @NotBlank(message = "Ativo é obrigatório")
    @Size(max = 10, message = "Ativo deve ter no máximo 10 caracteres")
    private String ativo;

    @NotNull(message = "Preço do ativo é obrigatório")
    @Positive(message = "Preço do ativo deve ser maior que zero")
    private BigDecimal precoAtivo;
}
```

### Repositories
- Extend Spring Data JPA interfaces
- Define custom query methods following Spring Data naming conventions

Example:
```java
public interface LancamentoCriptoRepository extends JpaRepository<LancamentoCriptoEntity, Long> {
    List<LancamentoCriptoEntity> findByIdUsuario(Integer idUsuario);
    List<LancamentoCriptoEntity> findByIdUsuarioAndTimeBetween(Integer idUsuario, LocalDate startDate, LocalDate endDate);
}
```

## Testing
- Use TestContainers for integration tests with a real PostgreSQL database
- Create separate test configuration classes
- Use Spring Boot Test annotations
- Follow the Arrange-Act-Assert pattern

Example:
```java
@TestConfiguration
public class PostgreSQLTestContainer {
    private static final String POSTGRES_IMAGE = "timescale/timescaledb-ha:pg17";
    private static final PostgreSQLContainer<?> postgreSQLContainer;

    static {
        postgreSQLContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE)
                .withDatabaseName("goods_db_test")
                .withUsername("postgres")
                .withPassword("postgres")
                .withReuse(true);

        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
    }
}
```

## Database Migrations
- Use Flyway for database schema migrations
- Place migration scripts in `src/main/resources/db/migration/dados/`
- Follow the naming convention `V{version}__{description}.sql`

## Build and Deployment
- Use Maven for building the project
- Use Docker for containerization
- Configure the application for different environments using Spring profiles

## Best Practices
1. Always use constructor injection instead of field injection
2. Use DTOs to separate API contracts from internal entities
3. Validate input data using Jakarta validation annotations
4. Handle exceptions properly and return appropriate HTTP status codes
5. Use transactions with appropriate isolation levels
6. Write comprehensive tests for all components
7. Use BigDecimal for financial calculations
8. Follow consistent naming conventions
9. Document your code and APIs
10. Keep controllers thin and move business logic to services