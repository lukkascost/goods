package br.com.goods.Goods.Investimentos.controllers;

import br.com.goods.Goods.Investimentos.GoodsInvestimentosApplicationTests;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoAcaoBrRequestDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoAcaoBrEntity;
import br.com.goods.Goods.Investimentos.repositories.LancamentoAcaoBrRepository;
import br.com.goods.Goods.Investimentos.services.LancamentoAcaoBrService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


public class LancamentoAcaoBrControllerTest extends GoodsInvestimentosApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/acoes-br/findall/setup-comum.sql"})
    class FindAll {
        //Caso seja encontrado um bug no endpoint findAll futuro, o teste que previne que o bug retorne deve ser criado nesta subclasse;

        @Test
        void sucesso() throws Exception {
            RequestBuilder requestBuilder = MockMvcRequestBuilders
                    .get("/api/lancamentos/acoes-br")
                    .accept(MediaType.APPLICATION_JSON);

            mockMvc.perform(requestBuilder)
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(3)))
                    .andDo(print())
                    .andReturn();
        }
    }

    @Nested
    @Sql(scripts = {"/scripts/clear-all.sql", "/scripts/acoes-br/findall/setup-comum.sql"})
    class FindById {

        @Test
        void sucesso() throws Exception {
            LocalDate time = LocalDate.of(2024, 1, 15);

            mockMvc.perform(get("/api/lancamentos/acoes-br/{time}", time)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ativo", is("PETR4")))
                    .andExpect(jsonPath("$.precoAtivo", is(35.78)))
                    .andExpect(jsonPath("$.quantidade", is(100.00000000)))
                    .andDo(print());
        }

        @Test
        void naoEncontrado() throws Exception {
            LocalDate time = LocalDate.of(2024, 2, 1);

            mockMvc.perform(get("/api/lancamentos/acoes-br/{time}", time)
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
            LocalDate startDate = LocalDate.of(2024, 1, 15);
            LocalDate endDate = LocalDate.of(2024, 1, 16);

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
            LocalDate startDate = LocalDate.of(2024, 2, 1);
            LocalDate endDate = LocalDate.of(2024, 2, 28);

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
            LancamentoAcaoBrRequestDTO requestDTO = new LancamentoAcaoBrRequestDTO();
            requestDTO.setAtivo("ITUB4");
            requestDTO.setTime(LocalDate.of(2024, 1, 20));
            requestDTO.setPrecoAtivo(new BigDecimal("32.45"));
            requestDTO.setQuantidade(new BigDecimal("200.00000000"));
            requestDTO.setOutrosCustos(new BigDecimal("5.90"));
            requestDTO.setAtivoFiat("BRL");
            requestDTO.setIdUsuario(1);
            requestDTO.setOperacao("COMPRA");
            requestDTO.setOrigem("CLEAR");
            requestDTO.setPrecoMedioAntesOperacao(new BigDecimal("0.00000000"));

            mockMvc.perform(post("/api/lancamentos/acoes-br")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDTO))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.ativo", is("ITUB4")))
                    .andExpect(jsonPath("$.precoAtivo", is(32.45)))
                    .andExpect(jsonPath("$.quantidade", is(200.00000000)))
                    .andDo(print());
        }

        @Test
        void validacaoFalha() throws Exception {
            LancamentoAcaoBrRequestDTO requestDTO = new LancamentoAcaoBrRequestDTO();
            // Missing required fields

            mockMvc.perform(post("/api/lancamentos/acoes-br")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDTO))
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
            LocalDate time = LocalDate.of(2024, 1, 15);

            LancamentoAcaoBrRequestDTO requestDTO = new LancamentoAcaoBrRequestDTO();
            requestDTO.setAtivo("PETR4");
            requestDTO.setTime(time);
            requestDTO.setPrecoAtivo(new BigDecimal("36.50"));
            requestDTO.setQuantidade(new BigDecimal("120.00000000"));
            requestDTO.setOutrosCustos(new BigDecimal("5.90"));
            requestDTO.setAtivoFiat("BRL");
            requestDTO.setIdUsuario(1);
            requestDTO.setOperacao("COMPRA");
            requestDTO.setOrigem("CLEAR");
            requestDTO.setPrecoMedioAntesOperacao(new BigDecimal("0.00000000"));

            mockMvc.perform(put("/api/lancamentos/acoes-br/{time}", time)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDTO))
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.ativo", is("PETR4")))
                    .andExpect(jsonPath("$.precoAtivo", is(36.50)))
                    .andExpect(jsonPath("$.quantidade", is(120.00000000)))
                    .andDo(print());
        }

        @Test
        void naoEncontrado() throws Exception {
            LocalDate time = LocalDate.of(2024, 2, 1);

            LancamentoAcaoBrRequestDTO requestDTO = new LancamentoAcaoBrRequestDTO();
            requestDTO.setAtivo("PETR4");
            requestDTO.setTime(time);
            requestDTO.setPrecoAtivo(new BigDecimal("36.50"));
            requestDTO.setQuantidade(new BigDecimal("120.00000000"));
            requestDTO.setOutrosCustos(new BigDecimal("5.90"));
            requestDTO.setAtivoFiat("BRL");
            requestDTO.setIdUsuario(1);
            requestDTO.setOperacao("COMPRA");
            requestDTO.setOrigem("CLEAR");
            requestDTO.setPrecoMedioAntesOperacao(new BigDecimal("0.00000000"));

            mockMvc.perform(put("/api/lancamentos/acoes-br/{time}", time)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(requestDTO))
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
            LocalDate time = LocalDate.of(2024, 1, 15);

            mockMvc.perform(delete("/api/lancamentos/acoes-br/{time}", time)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent())
                    .andDo(print());

            // Verify it's deleted
            mockMvc.perform(get("/api/lancamentos/acoes-br/{time}", time)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }

        @Test
        void naoEncontrado() throws Exception {
            LocalDate time = LocalDate.of(2024, 2, 1);

            mockMvc.perform(delete("/api/lancamentos/acoes-br/{time}", time)
                    .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound())
                    .andDo(print());
        }
    }
}
