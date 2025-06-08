package br.com.goods.Goods.Investimentos.specifications;

import br.com.goods.Goods.Investimentos.models.entities.LancamentoCriptoEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.ZonedDateTime;

public class LancamentoCriptoSpecifications {

    public static Specification<LancamentoCriptoEntity> withIdUsuario(Integer idUsuario) {
        return (root, query, cb) -> idUsuario == null ? null : cb.equal(root.get("idUsuario"), idUsuario);
    }

    public static Specification<LancamentoCriptoEntity> withAtivo(String ativo) {
        return (root, query, cb) -> ativo == null ? null : cb.equal(root.get("ativo"), ativo);
    }

    public static Specification<LancamentoCriptoEntity> withTimeAfter(ZonedDateTime startDate) {
        return (root, query, cb) -> startDate == null ? null : cb.greaterThanOrEqualTo(root.get("time"), startDate);
    }

    public static Specification<LancamentoCriptoEntity> withTimeBefore(ZonedDateTime endDate) {
        return (root, query, cb) -> endDate == null ? null : cb.lessThanOrEqualTo(root.get("time"), endDate);
    }

    public static Specification<LancamentoCriptoEntity> withOperacao(String operacao) {
        return (root, query, cb) -> operacao == null ? null : cb.equal(root.get("operacao"), operacao);
    }

    public static Specification<LancamentoCriptoEntity> withOrigem(String origem) {
        return (root, query, cb) -> origem == null ? null : cb.equal(root.get("origem"), origem);
    }
}
