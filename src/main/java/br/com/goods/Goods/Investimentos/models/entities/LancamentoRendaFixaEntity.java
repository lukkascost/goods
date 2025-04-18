package br.com.goods.Goods.Investimentos.models.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "lancamento_renda_fixa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LancamentoRendaFixaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_de_titulo", nullable = false, length = 10)
    private String tipoDeTitulo;

    @Column(name = "data_de_compra", nullable = false)
    private LocalDate dataDeCompra;

    @Column(name = "data_de_vencimento", nullable = false)
    private LocalDate dataDeVencimento;

    @Column(name = "emissor", nullable = false, length = 100)
    private String emissor;

    @Column(name = "indexador", length = 10)
    private String indexador;

    @Column(name = "valor_investido", nullable = false, precision = 20, scale = 8)
    private BigDecimal valorInvestido;

    @Column(name = "rentabilidade", nullable = false, precision = 20, scale = 8)
    private BigDecimal rentabilidade;

    @Column(name = "forma_de_rendimento", nullable = false, length = 10)
    private String formaDeRendimento;

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