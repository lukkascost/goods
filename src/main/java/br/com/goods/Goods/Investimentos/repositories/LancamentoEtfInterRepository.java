package br.com.goods.Goods.Investimentos.repositories;

import br.com.goods.Goods.Investimentos.models.entities.LancamentoEtfInterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LancamentoEtfInterRepository extends JpaRepository<LancamentoEtfInterEntity, Long> {
    
    List<LancamentoEtfInterEntity> findByIdUsuario(Integer idUsuario);
    
    List<LancamentoEtfInterEntity> findByIdUsuarioAndTimeBetween(Integer idUsuario, LocalDate startDate, LocalDate endDate);
    
    List<LancamentoEtfInterEntity> findByIdUsuarioAndAtivo(Integer idUsuario, String ativo);
}