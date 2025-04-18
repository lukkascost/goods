package br.com.goods.Goods.Investimentos.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoCriptoRequestDTO {

    @NotBlank(message = "Ativo é obrigatório")
    @Size(max = 10, message = "Ativo deve ter no máximo 10 caracteres")
    private String ativo;

    @NotNull(message = "Data é obrigatória")
    private ZonedDateTime time;

    @NotNull(message = "Preço do ativo é obrigatório")
    @Positive(message = "Preço do ativo deve ser maior que zero")
    private BigDecimal precoAtivo;

    @NotNull(message = "Quantidade é obrigatória")
    @Positive(message = "Quantidade deve ser maior que zero")
    private BigDecimal quantidade;

    @NotNull(message = "Outros custos é obrigatório")
    private BigDecimal outrosCustos;

    @NotBlank(message = "Ativo fiat é obrigatório")
    @Size(max = 10, message = "Ativo fiat deve ter no máximo 10 caracteres")
    private String ativoFiat = "BRL";

    @NotNull(message = "ID do usuário é obrigatório")
    private Integer idUsuario;

    @NotBlank(message = "Operação é obrigatória")
    @Size(max = 10, message = "Operação deve ter no máximo 10 caracteres")
    private String operacao;

    @NotBlank(message = "Origem é obrigatória")
    @Size(max = 10, message = "Origem deve ter no máximo 10 caracteres")
    private String origem;

    @NotNull(message = "Preço médio antes da operação é obrigatório")
    private BigDecimal precoMedioAntesOperacao;
}
