package br.com.goods.Goods.Investimentos.repositories;

import br.com.goods.Goods.Investimentos.models.entities.LancamentoAcaoBrEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LancamentoAcaoBrRepository extends JpaRepository<LancamentoAcaoBrEntity, LocalDate> {

    List<LancamentoAcaoBrEntity> findByIdUsuario(Integer idUsuario);

    List<LancamentoAcaoBrEntity> findByIdUsuarioAndTimeBetween(Integer idUsuario, LocalDate startDate, LocalDate endDate);

    List<LancamentoAcaoBrEntity> findByIdUsuarioAndAtivo(Integer idUsuario, String ativo);
}
