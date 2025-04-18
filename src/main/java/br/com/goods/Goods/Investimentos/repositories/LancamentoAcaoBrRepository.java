package br.com.goods.Goods.Investimentos.repositories;

import br.com.goods.Goods.Investimentos.models.entities.LancamentoAcaoBrEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface LancamentoAcaoBrRepository extends JpaRepository<LancamentoAcaoBrEntity, ZonedDateTime> {

    List<LancamentoAcaoBrEntity> findByIdUsuario(Integer idUsuario);

    List<LancamentoAcaoBrEntity> findByIdUsuarioAndTimeBetween(Integer idUsuario, ZonedDateTime startDate, ZonedDateTime endDate);

    List<LancamentoAcaoBrEntity> findByIdUsuarioAndAtivo(Integer idUsuario, String ativo);
}
