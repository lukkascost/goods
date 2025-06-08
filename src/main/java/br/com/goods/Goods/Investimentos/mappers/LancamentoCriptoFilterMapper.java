package br.com.goods.Goods.Investimentos.mappers;

import br.com.goods.Goods.Investimentos.models.dto.LancamentoCriptoFilterDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoCriptoEntity;
import br.com.goods.Goods.Investimentos.specifications.LancamentoCriptoSpecifications;
import org.mapstruct.Mapper;
import org.springframework.data.jpa.domain.Specification;

@Mapper(componentModel = "spring")
public interface LancamentoCriptoFilterMapper {

    default Specification<LancamentoCriptoEntity> toSpecification(LancamentoCriptoFilterDTO filterDTO) {
        Specification<LancamentoCriptoEntity> spec = Specification.where(null);
        if (filterDTO != null) {
            if (filterDTO.getIdUsuario() != null) {
                spec = spec.and(LancamentoCriptoSpecifications.withIdUsuario(filterDTO.getIdUsuario()));
            }
            if (filterDTO.getAtivo() != null && !filterDTO.getAtivo().isEmpty()) {
                spec = spec.and(LancamentoCriptoSpecifications.withAtivo(filterDTO.getAtivo()));
            }
            if (filterDTO.getStartDate() != null) {
                spec = spec.and(LancamentoCriptoSpecifications.withTimeAfter(filterDTO.getStartDate()));
            }
            if (filterDTO.getEndDate() != null) {
                spec = spec.and(LancamentoCriptoSpecifications.withTimeBefore(filterDTO.getEndDate()));
            }
            if (filterDTO.getOperacao() != null && !filterDTO.getOperacao().isEmpty()) {
                spec = spec.and(LancamentoCriptoSpecifications.withOperacao(filterDTO.getOperacao()));
            }
            if (filterDTO.getOrigem() != null && !filterDTO.getOrigem().isEmpty()) {
                spec = spec.and(LancamentoCriptoSpecifications.withOrigem(filterDTO.getOrigem()));
            }
        }
        return spec;
    }
}
