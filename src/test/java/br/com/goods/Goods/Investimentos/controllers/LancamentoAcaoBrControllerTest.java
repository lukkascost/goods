package br.com.goods.Goods.Investimentos.controllers;

import br.com.goods.Goods.Investimentos.GoodsInvestimentosApplicationTests;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoAcaoBrRequestDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoAcaoBrEntity;
import br.com.goods.Goods.Investimentos.repositories.LancamentoAcaoBrRepository;
import br.com.goods.Goods.Investimentos.services.LancamentoAcaoBrService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class LancamentoAcaoBrControllerTest extends GoodsInvestimentosApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LancamentoAcaoBrRepository repository;

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/acoes-br/findall/setup-comum.sql"})
    class FindById {

        @Test
        void sucesso() throws Exception {
            ZonedDateTime time = ZonedDateTime.of(2024, 1, 15, 12, 0, 0, 0, ZoneOffset.UTC);
            LancamentoAcaoBrEntity entity = repository.findByTime(time);
            assertNotNull(entity, "Entity should exist in the database");
            Long id = entity.getId();

            mockMvc.perform(get("/api/lancamentos/acoes-br/{id}", id)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ativo", is("PETR4")))
                    .andExpect(jsonPath("$.precoAtivo", is(35.78)))
                    .andExpect(jsonPath("$.quantidade", is(100.00000000)))
                    .andDo(print());
        }

        @Test
        void naoEncontrado() throws Exception {
            Long nonExistentId = 999L;

            mockMvc.perform(get("/api/lancamentos/acoes-br/{id}", nonExistentId)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/acoes-br/findall/setup-comum.sql"})
    class FindByIdUsuario {

        @Test
        void sucesso() throws Exception {
            mockMvc.perform(get("/api/lancamentos/acoes-br/usuario/{idUsuario}", 1)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andDo(print());
        }

        @Test
        void usuarioSemLancamentos() throws Exception {
            mockMvc.perform(get("/api/lancamentos/acoes-br/usuario/{idUsuario}", 999)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)))
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/acoes-br/findall/setup-comum.sql"})
    class FindByIdUsuarioAndTimeBetween {

        @Test
        void sucesso() throws Exception {
            ZonedDateTime startDate = ZonedDateTime.of(2024, 1, 15, 12, 0, 0, 0, ZoneOffset.UTC);
            ZonedDateTime endDate = ZonedDateTime.of(2024, 1, 16, 12, 0, 0, 0, ZoneOffset.UTC);

            mockMvc.perform(get("/api/lancamentos/acoes-br/usuario/{idUsuario}/periodo", 1)
                    .param("startDate", startDate.toString())
                    .param("endDate", endDate.toString())
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[*].ativo", org.hamcrest.Matchers.containsInAnyOrder("PETR4", "VALE3")))
                    .andDo(print());
        }

        @Test
        void periodoSemLancamentos() throws Exception {
            ZonedDateTime startDate = ZonedDateTime.of(2024, 2, 1, 12, 0, 0, 0, ZoneOffset.UTC);
            ZonedDateTime endDate = ZonedDateTime.of(2024, 2, 28, 12, 0, 0, 0, ZoneOffset.UTC);

            mockMvc.perform(get("/api/lancamentos/acoes-br/usuario/{idUsuario}/periodo", 1)
                    .param("startDate", startDate.toString())
                    .param("endDate", endDate.toString())
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)))
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/acoes-br/findall/setup-comum.sql"})
    class FindByIdUsuarioAndAtivo {

        @Test
        void sucesso() throws Exception {
            mockMvc.perform(get("/api/lancamentos/acoes-br/usuario/{idUsuario}/ativo/{ativo}", 1, "PETR4")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].ativo", is("PETR4")))
                    .andDo(print());
        }

        @Test
        void ativoNaoEncontrado() throws Exception {
            mockMvc.perform(get("/api/lancamentos/acoes-br/usuario/{idUsuario}/ativo/{ativo}", 1, "ITUB4")
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
            ZonedDateTime testDate = ZonedDateTime.of(2024, 1, 20, 12, 0, 0, 0, ZoneOffset.UTC);
            String jsonContent = """
                {
                    "ativo": "ITUB4",
                    "time": "2024-01-20T12:00:00+00:00",
                    "precoAtivo": 32.45,
                    "quantidade": 200.00000000,
                    "outrosCustos": 5.90,
                    "ativoFiat": "BRL",
                    "idUsuario": 1,
                    "operacao": "COMPRA",
                    "origem": "CLEAR",
                    "precoMedioAntesOperacao": 0.00000000
                }
                """;

            mockMvc.perform(post("/api/lancamentos/acoes-br")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.ativo", is("ITUB4")))
                    .andExpect(jsonPath("$.precoAtivo", is(32.45)))
                    .andExpect(jsonPath("$.quantidade", is(200.00000000)))
                    .andDo(print());

            // Verify data was saved correctly in the database
            assertEquals(1, repository.count());

            LancamentoAcaoBrEntity savedEntity = repository.findByTime(testDate);
            assertNotNull(savedEntity, "Entity should be saved in the database");
            assertEquals("ITUB4", savedEntity.getAtivo());
            assertEquals(0, new BigDecimal("32.45").compareTo(savedEntity.getPrecoAtivo()), 
                "PrecoAtivo should be 32.45, but was " + savedEntity.getPrecoAtivo());
            assertEquals(0, new BigDecimal("200.00000000").compareTo(savedEntity.getQuantidade()),
                "Quantidade should be 200.00000000, but was " + savedEntity.getQuantidade());
            assertEquals(0, new BigDecimal("5.90").compareTo(savedEntity.getOutrosCustos()),
                "OutrosCustos should be 5.90, but was " + savedEntity.getOutrosCustos());
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

            mockMvc.perform(post("/api/lancamentos/acoes-br")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/acoes-br/findall/setup-comum.sql"})
    class Update {

        @Test
        void sucesso() throws Exception {
            ZonedDateTime time = ZonedDateTime.of(2024, 1, 15, 12, 0, 0, 0, ZoneOffset.UTC);

            LancamentoAcaoBrEntity entity = repository.findByTime(time);
            assertNotNull(entity, "Entity should exist in the database");
            Long id = entity.getId();

            String jsonContent = """
                {
                    "ativo": "PETR4",
                    "time": "2024-01-15T12:00:00+00:00",
                    "precoAtivo": 36.50,
                    "quantidade": 120.00000000,
                    "outrosCustos": 5.90,
                    "ativoFiat": "BRL",
                    "idUsuario": 1,
                    "operacao": "COMPRA",
                    "origem": "CLEAR",
                    "precoMedioAntesOperacao": 0.00000000
                }
                """;

            mockMvc.perform(put("/api/lancamentos/acoes-br/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ativo", is("PETR4")))
                    .andExpect(jsonPath("$.precoAtivo", is(36.50)))
                    .andExpect(jsonPath("$.quantidade", is(120.00000000)))
                    .andDo(print());
        }

        @Test
        void naoEncontrado() throws Exception {
            Long nonExistentId = 999L;

            String jsonContent = """
                {
                    "ativo": "PETR4",
                    "time": "2024-02-01T12:00:00+00:00",
                    "precoAtivo": 36.50,
                    "quantidade": 120.00000000,
                    "outrosCustos": 5.90,
                    "ativoFiat": "BRL",
                    "idUsuario": 1,
                    "operacao": "COMPRA",
                    "origem": "CLEAR",
                    "precoMedioAntesOperacao": 0.00000000
                }
                """;

            mockMvc.perform(put("/api/lancamentos/acoes-br/{id}", nonExistentId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/acoes-br/findall/setup-comum.sql"})
    class Delete {

        @Test
        void sucesso() throws Exception {
            ZonedDateTime time = ZonedDateTime.of(2024, 1, 15, 12, 0, 0, 0, ZoneOffset.UTC);
            LancamentoAcaoBrEntity entity = repository.findByTime(time);
            assertNotNull(entity, "Entity should exist in the database");
            Long id = entity.getId();

            mockMvc.perform(delete("/api/lancamentos/acoes-br/{id}", id)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andDo(print());

            // Verify it's deleted
            mockMvc.perform(get("/api/lancamentos/acoes-br/{id}", id)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }

        @Test
        void naoEncontrado() throws Exception {
            Long nonExistentId = 999L;

            mockMvc.perform(delete("/api/lancamentos/acoes-br/{id}", nonExistentId)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }
}
