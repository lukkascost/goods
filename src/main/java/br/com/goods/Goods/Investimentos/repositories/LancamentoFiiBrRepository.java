package br.com.goods.Goods.Investimentos.repositories;

import br.com.goods.Goods.Investimentos.models.entities.LancamentoFiiBrEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LancamentoFiiBrRepository extends JpaRepository<LancamentoFiiBrEntity, Long> {

    List<LancamentoFiiBrEntity> findByIdUsuario(Integer idUsuario);

    List<LancamentoFiiBrEntity> findByIdUsuarioAndTimeBetween(Integer idUsuario, LocalDate startDate, LocalDate endDate);

    List<LancamentoFiiBrEntity> findByIdUsuarioAndAtivo(Integer idUsuario, String ativo);

    LancamentoFiiBrEntity findByTime(LocalDate time);
}
