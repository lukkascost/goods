package br.com.goods.Goods.Investimentos.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoRendaFixaResponseDTO {

    private Long id;
    private String tipoDeTitulo;
    private LocalDate dataDeCompra;
    private LocalDate dataDeVencimento;
    private String emissor;
    private String indexador;
    private BigDecimal valorInvestido;
    private BigDecimal rentabilidade;
    private String formaDeRendimento;
    private String ativoFiat;
    private Integer idUsuario;
    private String operacao;
    private String origem;
    private BigDecimal precoMedioAntesOperacao;
    
    // Campos calculados
    private Long diasAteVencimento;
    private BigDecimal valorEstimadoNoVencimento;
    
    public Long getDiasAteVencimento() {
        if (dataDeVencimento != null) {
            LocalDate hoje = LocalDate.now();
            if (dataDeVencimento.isAfter(hoje)) {
                return ChronoUnit.DAYS.between(hoje, dataDeVencimento);
            } else {
                return 0L;
            }
        }
        return null;
    }
    
    public BigDecimal getValorEstimadoNoVencimento() {
        if (valorInvestido != null && rentabilidade != null) {
            // Cálculo simplificado para estimativa
            // Para um cálculo mais preciso, seria necessário considerar o tipo de rendimento,
            // indexador, e outros fatores específicos de cada tipo de título
            return valorInvestido.multiply(BigDecimal.ONE.add(rentabilidade.divide(new BigDecimal("100"))));
        }
        return BigDecimal.ZERO;
    }
}