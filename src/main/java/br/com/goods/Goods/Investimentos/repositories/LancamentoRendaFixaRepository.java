package br.com.goods.Goods.Investimentos.repositories;

import br.com.goods.Goods.Investimentos.models.entities.LancamentoRendaFixaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LancamentoRendaFixaRepository extends JpaRepository<LancamentoRendaFixaEntity, Long> {

    List<LancamentoRendaFixaEntity> findByIdUsuario(Integer idUsuario);

    List<LancamentoRendaFixaEntity> findByIdUsuarioAndDataDeCompraBetween(Integer idUsuario, LocalDate startDate, LocalDate endDate);

    List<LancamentoRendaFixaEntity> findByIdUsuarioAndTipoDeTitulo(Integer idUsuario, String tipoDeTitulo);

    List<LancamentoRendaFixaEntity> findByIdUsuarioAndEmissor(Integer idUsuario, String emissor);

    LancamentoRendaFixaEntity findByDataDeCompra(LocalDate dataDeCompra);
}
