package br.com.goods.Goods.Investimentos.controllers;

import br.com.goods.Goods.Investimentos.GoodsInvestimentosApplicationTests;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoFiiBrRequestDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoFiiBrEntity;
import br.com.goods.Goods.Investimentos.repositories.LancamentoFiiBrRepository;
import br.com.goods.Goods.Investimentos.services.LancamentoFiiBrService;
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

public class LancamentoFiiBrControllerTest extends GoodsInvestimentosApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LancamentoFiiBrRepository repository;

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/fii-br/findall/setup-comum.sql"})
    class FindById {

        @Test
        void sucesso() throws Exception {
            LocalDate time = LocalDate.of(2024, 1, 15);
            LancamentoFiiBrEntity entity = repository.findByTime(time);
            assertNotNull(entity, "Entity should exist in the database");
            Long id = entity.getId();

            mockMvc.perform(get("/api/lancamentos/fii-br/{id}", id)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ativo", is("HGLG11")))
                    .andExpect(jsonPath("$.precoAtivo", is(175.50)))
                    .andExpect(jsonPath("$.quantidade", is(10.00000000)))
                    .andDo(print());
        }

        @Test
        void naoEncontrado() throws Exception {
            Long nonExistentId = 999L;

            mockMvc.perform(get("/api/lancamentos/fii-br/{id}", nonExistentId)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/fii-br/findall/setup-comum.sql"})
    class FindByIdUsuario {

        @Test
        void sucesso() throws Exception {
            mockMvc.perform(get("/api/lancamentos/fii-br/usuario/{idUsuario}", 1)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andDo(print());
        }

        @Test
        void usuarioSemLancamentos() throws Exception {
            mockMvc.perform(get("/api/lancamentos/fii-br/usuario/{idUsuario}", 999)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)))
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/fii-br/findall/setup-comum.sql"})
    class FindByIdUsuarioAndTimeBetween {

        @Test
        void sucesso() throws Exception {
            LocalDate startDate = LocalDate.of(2024, 1, 15);
            LocalDate endDate = LocalDate.of(2024, 1, 16);

            mockMvc.perform(get("/api/lancamentos/fii-br/usuario/{idUsuario}/periodo", 1)
                    .param("startDate", startDate.toString())
                    .param("endDate", endDate.toString())
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andExpect(jsonPath("$[*].ativo", org.hamcrest.Matchers.containsInAnyOrder("HGLG11", "KNRI11")))
                    .andDo(print());
        }

        @Test
        void periodoSemLancamentos() throws Exception {
            LocalDate startDate = LocalDate.of(2024, 2, 1);
            LocalDate endDate = LocalDate.of(2024, 2, 28);

            mockMvc.perform(get("/api/lancamentos/fii-br/usuario/{idUsuario}/periodo", 1)
                    .param("startDate", startDate.toString())
                    .param("endDate", endDate.toString())
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)))
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/fii-br/findall/setup-comum.sql"})
    class FindByIdUsuarioAndAtivo {

        @Test
        void sucesso() throws Exception {
            mockMvc.perform(get("/api/lancamentos/fii-br/usuario/{idUsuario}/ativo/{ativo}", 1, "HGLG11")
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(1)))
                    .andExpect(jsonPath("$[0].ativo", is("HGLG11")))
                    .andDo(print());
        }

        @Test
        void ativoNaoEncontrado() throws Exception {
            mockMvc.perform(get("/api/lancamentos/fii-br/usuario/{idUsuario}/ativo/{ativo}", 1, "XPLG11")
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
                    "ativo": "XPLG11",
                    "time": "2024-01-20",
                    "precoAtivo": 112.45,
                    "quantidade": 20.00000000,
                    "outrosCustos": 5.90,
                    "ativoFiat": "BRL",
                    "idUsuario": 1,
                    "operacao": "COMPRA",
                    "origem": "CLEAR",
                    "precoMedioAntesOperacao": 0.00000000
                }
                """;

            mockMvc.perform(post("/api/lancamentos/fii-br")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.ativo", is("XPLG11")))
                    .andExpect(jsonPath("$.precoAtivo", is(112.45)))
                    .andExpect(jsonPath("$.quantidade", is(20.00000000)))
                    .andDo(print());

            // Verify data was saved correctly in the database
            assertEquals(1, repository.count());

            LancamentoFiiBrEntity savedEntity = repository.findByTime(testDate);
            assertNotNull(savedEntity, "Entity should be saved in the database");
            assertEquals("XPLG11", savedEntity.getAtivo());
            assertEquals(0, new BigDecimal("112.45").compareTo(savedEntity.getPrecoAtivo()), 
                "PrecoAtivo should be 112.45, but was " + savedEntity.getPrecoAtivo());
            assertEquals(0, new BigDecimal("20.00000000").compareTo(savedEntity.getQuantidade()),
                "Quantidade should be 20.00000000, but was " + savedEntity.getQuantidade());
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

            mockMvc.perform(post("/api/lancamentos/fii-br")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/fii-br/findall/setup-comum.sql"})
    class Update {

        @Test
        void sucesso() throws Exception {
            LocalDate time = LocalDate.of(2024, 1, 15);

            LancamentoFiiBrEntity entity = repository.findByTime(time);
            assertNotNull(entity, "Entity should exist in the database");
            Long id = entity.getId();

            String jsonContent = """
                {
                    "ativo": "HGLG11",
                    "time": "2024-01-15",
                    "precoAtivo": 180.50,
                    "quantidade": 12.00000000,
                    "outrosCustos": 5.90,
                    "ativoFiat": "BRL",
                    "idUsuario": 1,
                    "operacao": "COMPRA",
                    "origem": "CLEAR",
                    "precoMedioAntesOperacao": 0.00000000
                }
                """;

            mockMvc.perform(put("/api/lancamentos/fii-br/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ativo", is("HGLG11")))
                    .andExpect(jsonPath("$.precoAtivo", is(180.50)))
                    .andExpect(jsonPath("$.quantidade", is(12.00000000)))
                    .andDo(print());
        }

        @Test
        void naoEncontrado() throws Exception {
            Long nonExistentId = 999L;

            String jsonContent = """
                {
                    "ativo": "HGLG11",
                    "time": "2024-02-01",
                    "precoAtivo": 180.50,
                    "quantidade": 12.00000000,
                    "outrosCustos": 5.90,
                    "ativoFiat": "BRL",
                    "idUsuario": 1,
                    "operacao": "COMPRA",
                    "origem": "CLEAR",
                    "precoMedioAntesOperacao": 0.00000000
                }
                """;

            mockMvc.perform(put("/api/lancamentos/fii-br/{id}", nonExistentId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/fii-br/findall/setup-comum.sql"})
    class Delete {

        @Test
        void sucesso() throws Exception {
            LocalDate time = LocalDate.of(2024, 1, 15);
            LancamentoFiiBrEntity entity = repository.findByTime(time);
            assertNotNull(entity, "Entity should exist in the database");
            Long id = entity.getId();

            mockMvc.perform(delete("/api/lancamentos/fii-br/{id}", id)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andDo(print());

            // Verify it's deleted directly from the database
            assertFalse(repository.existsById(id), "Entity should be removed from the database");
        }

        @Test
        void naoEncontrado() throws Exception {
            Long nonExistentId = 999L;

            mockMvc.perform(delete("/api/lancamentos/fii-br/{id}", nonExistentId)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }
}