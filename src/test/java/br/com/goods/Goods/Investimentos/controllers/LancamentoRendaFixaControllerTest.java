package br.com.goods.Goods.Investimentos.controllers;

import br.com.goods.Goods.Investimentos.GoodsInvestimentosApplicationTests;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoRendaFixaRequestDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoRendaFixaEntity;
import br.com.goods.Goods.Investimentos.repositories.LancamentoRendaFixaRepository;
import br.com.goods.Goods.Investimentos.services.LancamentoRendaFixaService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LancamentoRendaFixaControllerTest extends GoodsInvestimentosApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LancamentoRendaFixaRepository repository;

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/renda-fixa/findall/setup-comum.sql"})
    class FindById {

        @Test
        void sucesso() throws Exception {
            LocalDate dataDeCompra = LocalDate.of(2024, 1, 15);
            LancamentoRendaFixaEntity entity = repository.findByDataDeCompra(dataDeCompra);
            assertNotNull(entity, "Entity should exist in the database");
            Long id = entity.getId();

            mockMvc.perform(get("/api/lancamentos/renda-fixa/{id}", id)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.tipoDeTitulo", is("CDB")))
                    .andExpect(jsonPath("$.valorInvestido", is(5000.00000000)))
                    .andExpect(jsonPath("$.rentabilidade", is(110.00000000)))
                    .andDo(print());
        }

        @Test
        void naoEncontrado() throws Exception {
            Long nonExistentId = 999L;

            mockMvc.perform(get("/api/lancamentos/renda-fixa/{id}", nonExistentId)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/renda-fixa/findall/setup-comum.sql"})
    class FindByIdUsuario {

        @Test
        void sucesso() throws Exception {
            mockMvc.perform(get("/api/lancamentos/renda-fixa/usuario/{idUsuario}", 1)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andDo(print());
        }

        @Test
        void usuarioSemLancamentos() throws Exception {
            mockMvc.perform(get("/api/lancamentos/renda-fixa/usuario/{idUsuario}", 999)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)))
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/renda-fixa/findall/setup-comum.sql"})
    class FindByIdUsuarioAndDataDeCompraBetween {

        @Test
        void sucesso() throws Exception {
            LocalDate startDate = LocalDate.of(2024, 1, 15);
            LocalDate endDate = LocalDate.of(2024, 1, 16);

            mockMvc.perform(get("/api/lancamentos/renda-fixa/usuario/{idUsuario}/periodo", 1)
                    .param("startDate", startDate.toString())
                    .param("endDate", endDate.toString())
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[*].tipoDeTitulo", org.hamcrest.Matchers.containsInAnyOrder("CDB", "LCI")))
                    .andDo(print());
        }

        @Test
        void periodoSemLancamentos() throws Exception {
            LocalDate startDate = LocalDate.of(2024, 2, 1);
            LocalDate endDate = LocalDate.of(2024, 2, 28);

            mockMvc.perform(get("/api/lancamentos/renda-fixa/usuario/{idUsuario}/periodo", 1)
                    .param("startDate", startDate.toString())
                    .param("endDate", endDate.toString())
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)))
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/renda-fixa/findall/setup-comum.sql"})
    class FindByIdUsuarioAndTipoDeTitulo {

        @Test
        void sucesso() throws Exception {
            mockMvc.perform(get("/api/lancamentos/renda-fixa/usuario/{idUsuario}/tipo/{tipoDeTitulo}", 1, "CDB")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].tipoDeTitulo", is("CDB")))
                    .andDo(print());
        }

        @Test
        void tipoNaoEncontrado() throws Exception {
            mockMvc.perform(get("/api/lancamentos/renda-fixa/usuario/{idUsuario}/tipo/{tipoDeTitulo}", 1, "LCA")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)))
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/renda-fixa/findall/setup-comum.sql"})
    class FindByIdUsuarioAndEmissor {

        @Test
        void sucesso() throws Exception {
            mockMvc.perform(get("/api/lancamentos/renda-fixa/usuario/{idUsuario}/emissor/{emissor}", 1, "Banco XYZ")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].emissor", is("Banco XYZ")))
                    .andDo(print());
        }

        @Test
        void emissorNaoEncontrado() throws Exception {
            mockMvc.perform(get("/api/lancamentos/renda-fixa/usuario/{idUsuario}/emissor/{emissor}", 1, "Banco DEF")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)))
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql"})
    class Create {

        @Test
        void sucesso() throws Exception {
            LocalDate dataDeCompra = LocalDate.of(2024, 1, 20);
            LocalDate dataDeVencimento = LocalDate.of(2025, 1, 20);
            String jsonContent = """
                {
                    "tipoDeTitulo": "LCA",
                    "dataDeCompra": "2024-01-20",
                    "dataDeVencimento": "2025-01-20",
                    "emissor": "Banco DEF",
                    "indexador": "CDI",
                    "valorInvestido": 8000.00000000,
                    "rentabilidade": 108.00000000,
                    "formaDeRendimento": "PREFIXADO",
                    "ativoFiat": "BRL",
                    "idUsuario": 1,
                    "operacao": "COMPRA",
                    "origem": "CLEAR",
                    "precoMedioAntesOperacao": 0.00000000
                }
                """;

            mockMvc.perform(post("/api/lancamentos/renda-fixa")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.tipoDeTitulo", is("LCA")))
                    .andExpect(jsonPath("$.valorInvestido", is(8000.00000000)))
                    .andExpect(jsonPath("$.rentabilidade", is(108.00000000)))
                    .andDo(print());

            // Verify data was saved correctly in the database
            assertEquals(1, repository.count());

            LancamentoRendaFixaEntity savedEntity = repository.findByDataDeCompra(dataDeCompra);
            assertNotNull(savedEntity, "Entity should be saved in the database");
            assertEquals("LCA", savedEntity.getTipoDeTitulo());
            assertEquals(0, new BigDecimal("8000.00000000").compareTo(savedEntity.getValorInvestido()), 
                "ValorInvestido should be 8000.00000000, but was " + savedEntity.getValorInvestido());
            assertEquals(0, new BigDecimal("108.00000000").compareTo(savedEntity.getRentabilidade()),
                "Rentabilidade should be 108.00000000, but was " + savedEntity.getRentabilidade());
            assertEquals("Banco DEF", savedEntity.getEmissor());
            assertEquals("CDI", savedEntity.getIndexador());
            assertEquals("PREFIXADO", savedEntity.getFormaDeRendimento());
            assertEquals("BRL", savedEntity.getAtivoFiat());
            assertEquals(Integer.valueOf(1), savedEntity.getIdUsuario());
            assertEquals("COMPRA", savedEntity.getOperacao());
            assertEquals("CLEAR", savedEntity.getOrigem());
            assertEquals(0, new BigDecimal("0.00000000").compareTo(savedEntity.getPrecoMedioAntesOperacao()),
                "PrecoMedioAntesOperacao should be 0.00000000, but was " + savedEntity.getPrecoMedioAntesOperacao());
        }

        @Test
        void validacaoFalha() throws Exception {
            // JSON with missing required fields
            String jsonContent = """
                {
                }
                """;

            mockMvc.perform(post("/api/lancamentos/renda-fixa")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/renda-fixa/findall/setup-comum.sql"})
    class Update {

        @Test
        void sucesso() throws Exception {
            LocalDate dataDeCompra = LocalDate.of(2024, 1, 15);

            LancamentoRendaFixaEntity entity = repository.findByDataDeCompra(dataDeCompra);
            assertNotNull(entity, "Entity should exist in the database");
            Long id = entity.getId();

            String jsonContent = """
                {
                    "tipoDeTitulo": "CDB",
                    "dataDeCompra": "2024-01-15",
                    "dataDeVencimento": "2025-01-15",
                    "emissor": "Banco XYZ",
                    "indexador": "CDI",
                    "valorInvestido": 6000.00000000,
                    "rentabilidade": 112.00000000,
                    "formaDeRendimento": "PREFIXADO",
                    "ativoFiat": "BRL",
                    "idUsuario": 1,
                    "operacao": "COMPRA",
                    "origem": "CLEAR",
                    "precoMedioAntesOperacao": 0.00000000
                }
                """;

            mockMvc.perform(put("/api/lancamentos/renda-fixa/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.tipoDeTitulo", is("CDB")))
                    .andExpect(jsonPath("$.valorInvestido", is(6000.00000000)))
                    .andExpect(jsonPath("$.rentabilidade", is(112.00000000)))
                    .andDo(print());
        }

        @Test
        void naoEncontrado() throws Exception {
            Long nonExistentId = 999L;

            String jsonContent = """
                {
                    "tipoDeTitulo": "CDB",
                    "dataDeCompra": "2024-02-01",
                    "dataDeVencimento": "2025-02-01",
                    "emissor": "Banco XYZ",
                    "indexador": "CDI",
                    "valorInvestido": 6000.00000000,
                    "rentabilidade": 112.00000000,
                    "formaDeRendimento": "PREFIXADO",
                    "ativoFiat": "BRL",
                    "idUsuario": 1,
                    "operacao": "COMPRA",
                    "origem": "CLEAR",
                    "precoMedioAntesOperacao": 0.00000000
                }
                """;

            mockMvc.perform(put("/api/lancamentos/renda-fixa/{id}", nonExistentId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/renda-fixa/findall/setup-comum.sql"})
    class Delete {

        @Test
        void sucesso() throws Exception {
            LocalDate dataDeCompra = LocalDate.of(2024, 1, 15);
            LancamentoRendaFixaEntity entity = repository.findByDataDeCompra(dataDeCompra);
            assertNotNull(entity, "Entity should exist in the database");
            Long id = entity.getId();

            mockMvc.perform(delete("/api/lancamentos/renda-fixa/{id}", id)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andDo(print());

            // Verify it's deleted directly from the database
            assertFalse(repository.existsById(id), "Entity should be removed from the database");
        }

        @Test
        void naoEncontrado() throws Exception {
            Long nonExistentId = 999L;

            mockMvc.perform(delete("/api/lancamentos/renda-fixa/{id}", nonExistentId)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }
}