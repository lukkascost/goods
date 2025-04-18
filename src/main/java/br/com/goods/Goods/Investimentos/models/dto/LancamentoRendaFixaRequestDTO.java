package br.com.goods.Goods.Investimentos.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoRendaFixaRequestDTO {

    @NotBlank(message = "Tipo de título é obrigatório")
    @Size(max = 10, message = "Tipo de título deve ter no máximo 10 caracteres")
    private String tipoDeTitulo;

    @NotNull(message = "Data de compra é obrigatória")
    private LocalDate dataDeCompra;

    @NotNull(message = "Data de vencimento é obrigatória")
    private LocalDate dataDeVencimento;

    @NotBlank(message = "Emissor é obrigatório")
    @Size(max = 100, message = "Emissor deve ter no máximo 100 caracteres")
    private String emissor;

    @Size(max = 10, message = "Indexador deve ter no máximo 10 caracteres")
    private String indexador;

    @NotNull(message = "Valor investido é obrigatório")
    @Positive(message = "Valor investido deve ser maior que zero")
    private BigDecimal valorInvestido;

    @NotNull(message = "Rentabilidade é obrigatória")
    @Positive(message = "Rentabilidade deve ser maior que zero")
    private BigDecimal rentabilidade;

    @NotBlank(message = "Forma de rendimento é obrigatória")
    @Size(max = 10, message = "Forma de rendimento deve ter no máximo 10 caracteres")
    private String formaDeRendimento;

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