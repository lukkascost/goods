package br.com.goods.Goods.Investimentos.controllers;

import br.com.goods.Goods.Investimentos.GoodsInvestimentosApplicationTests;
import br.com.goods.Goods.Investimentos.repositories.LancamentoCriptoRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertFalse;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class LancamentoCriptoControllerTest extends GoodsInvestimentosApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LancamentoCriptoRepository repository;

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql"})
    class Create {

        @Test
        void sucesso() throws Exception {
            String jsonContent = """
                {
                    "ativo": "BTC",
                    "time": "2024-01-20T12:00:00+00:00",
                    "precoAtivo": 50000.00,
                    "quantidade": 0.5,
                    "outrosCustos": 10.00,
                    "ativoFiat": "BRL",
                    "idUsuario": 1,
                    "operacao": "COMPRA",
                    "origem": "BINANCE",
                    "precoMedioAntesOperacao": 0.00000000
                }
                """;

            mockMvc.perform(post("/api/lancamentos/cripto")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.ativo", is("BTC")))
                    .andExpect(jsonPath("$.precoAtivo", is(50000.00)))
                    .andExpect(jsonPath("$.quantidade", is(0.5)))
                    .andDo(print());
        }

        @Test
        void validacaoFalha() throws Exception {
            String jsonContent = """
                {
                }
                """;

            mockMvc.perform(post("/api/lancamentos/cripto")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isBadRequest())
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/cripto/findall/setup-comum.sql"})
    class Update {

        @Test
        void sucesso() throws Exception {
            // Get the ID of the first record (BTC)
            Long id = repository.findAll().stream()
                    .filter(entity -> "BTC".equals(entity.getAtivo()))
                    .findFirst()
                    .orElseThrow()
                    .getId();

            String jsonContent = """
                {
                    "ativo": "BTC",
                    "time": "2024-01-15T12:00:00+00:00",
                    "precoAtivo": 48000.00,
                    "quantidade": 0.6,
                    "outrosCustos": 12.00,
                    "ativoFiat": "BRL",
                    "idUsuario": 1,
                    "operacao": "COMPRA",
                    "origem": "BINANCE",
                    "precoMedioAntesOperacao": 0.00000000
                }
                """;

            mockMvc.perform(put("/api/lancamentos/cripto/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ativo", is("BTC")))
                    .andExpect(jsonPath("$.precoAtivo", is(48000.00)))
                    .andExpect(jsonPath("$.quantidade", is(0.6)))
                    .andDo(print());
        }

        @Test
        void naoEncontrado() throws Exception {
            Long nonExistentId = 999L;

            String jsonContent = """
                {
                    "ativo": "BTC",
                    "time": "2024-02-01T12:00:00+00:00",
                    "precoAtivo": 48000.00,
                    "quantidade": 0.6,
                    "outrosCustos": 12.00,
                    "ativoFiat": "BRL",
                    "idUsuario": 1,
                    "operacao": "COMPRA",
                    "origem": "BINANCE",
                    "precoMedioAntesOperacao": 0.00000000
                }
                """;

            mockMvc.perform(put("/api/lancamentos/cripto/{id}", nonExistentId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(jsonContent)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/cripto/findall/setup-comum.sql"})
    class Delete {

        @Test
        void sucesso() throws Exception {
            // Get the ID of the first record (BTC)
            Long id = repository.findAll().stream()
                    .filter(entity -> "BTC".equals(entity.getAtivo()))
                    .findFirst()
                    .orElseThrow()
                    .getId();

            mockMvc.perform(delete("/api/lancamentos/cripto/{id}", id)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andDo(print());

            // Verify it's deleted directly from the database
            assertFalse(repository.existsById(id), "Entity should be removed from the database");
        }

        @Test
        void naoEncontrado() throws Exception {
            Long nonExistentId = 999L;

            mockMvc.perform(delete("/api/lancamentos/cripto/{id}", nonExistentId)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/cripto/findall/setup-comum.sql"})
    class FindById {

        @Test
        void sucesso() throws Exception {
            // Get the ID of the first record (BTC)
            Long id = repository.findAll().stream()
                    .filter(entity -> "BTC".equals(entity.getAtivo()))
                    .findFirst()
                    .orElseThrow()
                    .getId();

            mockMvc.perform(get("/api/lancamentos/cripto/{id}", id)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ativo", is("BTC")))
                    .andExpect(jsonPath("$.precoAtivo", is(45000.00)))
                    .andExpect(jsonPath("$.quantidade", is(0.5)))
                    .andDo(print());
        }

        @Test
        void naoEncontrado() throws Exception {
            Long nonExistentId = 999L;

            mockMvc.perform(get("/api/lancamentos/cripto/{id}", nonExistentId)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/cripto/findall/setup-comum.sql"})
    class FindByIdUsuario {

        @Test
        void sucesso() throws Exception {
            mockMvc.perform(get("/api/lancamentos/cripto/usuario/{idUsuario}", 1)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andDo(print());
        }

        @Test
        void usuarioSemLancamentos() throws Exception {
            mockMvc.perform(get("/api/lancamentos/cripto/usuario/{idUsuario}", 999)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(0)))
                    .andDo(print());
        }
    }
}