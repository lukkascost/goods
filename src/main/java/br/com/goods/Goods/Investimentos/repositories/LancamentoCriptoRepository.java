package br.com.goods.Goods.Investimentos.repositories;

import br.com.goods.Goods.Investimentos.models.entities.LancamentoCriptoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface LancamentoCriptoRepository extends JpaRepository<LancamentoCriptoEntity, Long> {
    
    List<LancamentoCriptoEntity> findByIdUsuario(Integer idUsuario);
    
    List<LancamentoCriptoEntity> findByIdUsuarioAndTimeBetween(Integer idUsuario, LocalDate startDate, LocalDate endDate);
    
    List<LancamentoCriptoEntity> findByIdUsuarioAndAtivo(Integer idUsuario, String ativo);
}