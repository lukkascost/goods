package br.com.goods.Goods.Investimentos.repositories;

import br.com.goods.Goods.Investimentos.models.entities.LancamentoCriptoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Repository
public interface LancamentoCriptoRepository extends
        JpaRepository<LancamentoCriptoEntity, Long>,
        JpaSpecificationExecutor<LancamentoCriptoEntity> {

    LancamentoCriptoEntity findByTime(ZonedDateTime time);
}
