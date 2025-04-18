package br.com.goods.Goods.Investimentos.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoAcaoBrResponseDTO {

    private String ativo;
    private ZonedDateTime time;
    private BigDecimal precoAtivo;
    private BigDecimal quantidade;
    private BigDecimal outrosCustos;
    private String ativoFiat;
    private Integer idUsuario;
    private String operacao;
    private String origem;
    private BigDecimal precoMedioAntesOperacao;

    // Campos calculados
    private BigDecimal valorTotal;

    public BigDecimal getValorTotal() {
        if (precoAtivo != null && quantidade != null) {
            return precoAtivo.multiply(quantidade).add(outrosCustos != null ? outrosCustos : BigDecimal.ZERO);
        }
        return BigDecimal.ZERO;
    }
}
