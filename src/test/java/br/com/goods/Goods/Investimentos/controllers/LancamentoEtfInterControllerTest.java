package br.com.goods.Goods.Investimentos.controllers;

import br.com.goods.Goods.Investimentos.GoodsInvestimentosApplicationTests;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoEtfInterRequestDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoEtfInterEntity;
import br.com.goods.Goods.Investimentos.repositories.LancamentoEtfInterRepository;
import br.com.goods.Goods.Investimentos.services.LancamentoEtfInterService;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class LancamentoEtfInterControllerTest extends GoodsInvestimentosApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LancamentoEtfInterRepository repository;

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/etf-inter/findall/setup-comum.sql"})
    class FindById {

        @Test
        void sucesso() throws Exception {
            LocalDate time = LocalDate.of(2024, 1, 15);
            LancamentoEtfInterEntity entity = repository.findByTime(time);
            assertNotNull(entity, "Entity should exist in the database");
            Long id = entity.getId();

            mockMvc.perform(get("/api/lancamentos/etf-inter/{id}", id)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ativo", is("VTI")))
                    .andExpect(jsonPath("$.precoAtivo", is(235.78)))
                    .andExpect(jsonPath("$.quantidade", is(10.00000000)))
                    .andDo(print());
        }

        @Test
        void naoEncontrado() throws Exception {
            Long nonExistentId = 999L;

            mockMvc.perform(get("/api/lancamentos/etf-inter/{id}", nonExistentId)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/etf-inter/findall/setup-comum.sql"})
    class FindByIdUsuario {

        @Test
        void sucesso() throws Exception {
            mockMvc.perform(get("/api/lancamentos/etf-inter/usuario/{idUsuario}", 1)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andDo(print());
        }

        @Test
        void usuarioSemLancamentos() throws Exception {
            mockMvc.perform(get("/api/lancamentos/etf-inter/usuario/{idUsuario}", 999)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)))
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/etf-inter/findall/setup-comum.sql"})
    class FindByIdUsuarioAndTimeBetween {

        @Test
        void sucesso() throws Exception {
            LocalDate startDate = LocalDate.of(2024, 1, 15);
            LocalDate endDate = LocalDate.of(2024, 1, 16);

            mockMvc.perform(get("/api/lancamentos/etf-inter/usuario/{idUsuario}/periodo", 1)
                    .param("startDate", startDate.toString())
                    .param("endDate", endDate.toString())
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[*].ativo", org.hamcrest.Matchers.containsInAnyOrder("VTI", "VOO")))
                    .andDo(print());
        }

        @Test
        void periodoSemLancamentos() throws Exception {
            LocalDate startDate = LocalDate.of(2024, 2, 1);
            LocalDate endDate = LocalDate.of(2024, 2, 28);

            mockMvc.perform(get("/api/lancamentos/etf-inter/usuario/{idUsuario}/periodo", 1)
                    .param("startDate", startDate.toString())
                    .param("endDate", endDate.toString())
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)))
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/etf-inter/findall/setup-comum.sql"})
    class FindByIdUsuarioAndAtivo {

        @Test
        void sucesso() throws Exception {
            mockMvc.perform(get("/api/lancamentos/etf-inter/usuario/{idUsuario}/ativo/{ativo}", 1, "VTI")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].ativo", is("VTI")))
                    .andDo(print());
        }

        @Test
        void ativoNaoEncontrado() throws Exception {
            mockMvc.perform(get("/api/lancamentos/etf-inter/usuario/{idUsuario}/ativo/{ativo}", 1, "SPY")
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
            LocalDate testDate = LocalDate.of(2024, 1, 20);
            String jsonContent = """
                {
                    "ativo": "SPY",
                    "time": "2024-01-20",
                    "precoAtivo": 482.45,
                    "quantidade": 3.00000000,
                    "outrosCustos": 2.90,
                    "ativoFiat": "USD",
                    "idUsuario": 1,
                    "operacao": "COMPRA",
                    "origem": "AVENUE",
                    "precoMedioAntesOperacao": 0.00000000
                }
                """;

            mockMvc.perform(post("/api/lancamentos/etf-inter")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.ativo", is("SPY")))
                    .andExpect(jsonPath("$.precoAtivo", is(482.45)))
                    .andExpect(jsonPath("$.quantidade", is(3.00000000)))
                    .andDo(print());

            // Verify data was saved correctly in the database
            assertEquals(1, repository.count());

            LancamentoEtfInterEntity savedEntity = repository.findByTime(testDate);
            assertNotNull(savedEntity, "Entity should be saved in the database");
            assertEquals("SPY", savedEntity.getAtivo());
            assertEquals(0, new BigDecimal("482.45").compareTo(savedEntity.getPrecoAtivo()), 
                "PrecoAtivo should be 482.45, but was " + savedEntity.getPrecoAtivo());
            assertEquals(0, new BigDecimal("3.00000000").compareTo(savedEntity.getQuantidade()),
                "Quantidade should be 3.00000000, but was " + savedEntity.getQuantidade());
            assertEquals(0, new BigDecimal("2.90").compareTo(savedEntity.getOutrosCustos()),
                "OutrosCustos should be 2.90, but was " + savedEntity.getOutrosCustos());
            assertEquals("USD", savedEntity.getAtivoFiat());
            assertEquals(Integer.valueOf(1), savedEntity.getIdUsuario());
            assertEquals("COMPRA", savedEntity.getOperacao());
            assertEquals("AVENUE", savedEntity.getOrigem());
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

            mockMvc.perform(post("/api/lancamentos/etf-inter")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/etf-inter/findall/setup-comum.sql"})
    class Update {

        @Test
        void sucesso() throws Exception {
            LocalDate time = LocalDate.of(2024, 1, 15);

            LancamentoEtfInterEntity entity = repository.findByTime(time);
            assertNotNull(entity, "Entity should exist in the database");
            Long id = entity.getId();

            String jsonContent = """
                {
                    "ativo": "VTI",
                    "time": "2024-01-15",
                    "precoAtivo": 240.50,
                    "quantidade": 12.00000000,
                    "outrosCustos": 3.90,
                    "ativoFiat": "USD",
                    "idUsuario": 1,
                    "operacao": "COMPRA",
                    "origem": "AVENUE",
                    "precoMedioAntesOperacao": 0.00000000
                }
                """;

            mockMvc.perform(put("/api/lancamentos/etf-inter/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ativo", is("VTI")))
                    .andExpect(jsonPath("$.precoAtivo", is(240.50)))
                    .andExpect(jsonPath("$.quantidade", is(12.00000000)))
                    .andDo(print());
        }

        @Test
        void naoEncontrado() throws Exception {
            Long nonExistentId = 999L;

            String jsonContent = """
                {
                    "ativo": "VTI",
                    "time": "2024-02-01",
                    "precoAtivo": 240.50,
                    "quantidade": 12.00000000,
                    "outrosCustos": 3.90,
                    "ativoFiat": "USD",
                    "idUsuario": 1,
                    "operacao": "COMPRA",
                    "origem": "AVENUE",
                    "precoMedioAntesOperacao": 0.00000000
                }
                """;

            mockMvc.perform(put("/api/lancamentos/etf-inter/{id}", nonExistentId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/etf-inter/findall/setup-comum.sql"})
    class Delete {

        @Test
        void sucesso() throws Exception {
            LocalDate time = LocalDate.of(2024, 1, 15);
            LancamentoEtfInterEntity entity = repository.findByTime(time);
            assertNotNull(entity, "Entity should exist in the database");
            Long id = entity.getId();

            mockMvc.perform(delete("/api/lancamentos/etf-inter/{id}", id)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andDo(print());

            // Verify it's deleted directly from the database
            assertFalse(repository.existsById(id), "Entity should be removed from the database");
        }

        @Test
        void naoEncontrado() throws Exception {
            Long nonExistentId = 999L;

            mockMvc.perform(delete("/api/lancamentos/etf-inter/{id}", nonExistentId)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }
}