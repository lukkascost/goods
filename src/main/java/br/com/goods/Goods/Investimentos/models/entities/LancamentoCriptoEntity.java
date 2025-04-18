package br.com.goods.Goods.Investimentos.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "lancamento_cripto", uniqueConstraints = {
    @UniqueConstraint(name = "lancamento_cripto_id_unique", columnNames = {"id", "time"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoCriptoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ativo", nullable = false, length = 10)
    private String ativo;

    @Column(name = "time", nullable = false)
    private ZonedDateTime time;

    @Column(name = "preco_ativo", nullable = false, precision = 20, scale = 8)
    private BigDecimal precoAtivo;

    @Column(name = "quantidade", nullable = false, precision = 20, scale = 8)
    private BigDecimal quantidade;

    @Column(name = "outros_custos", nullable = false, precision = 20, scale = 8)
    private BigDecimal outrosCustos;

    @Column(name = "ativo_fiat", nullable = false, length = 10)
    private String ativoFiat;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "operacao", nullable = false, length = 10)
    private String operacao;

    @Column(name = "origem", nullable = false, length = 10)
    private String origem;

    @Column(name = "preco_medio_antes_operacao", nullable = false, precision = 20, scale = 8)
    private BigDecimal precoMedioAntesOperacao;
}
