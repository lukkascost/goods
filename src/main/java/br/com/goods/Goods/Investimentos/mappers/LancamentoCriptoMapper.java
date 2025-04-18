package br.com.goods.Goods.Investimentos.mappers;

import br.com.goods.Goods.Investimentos.models.dto.LancamentoCriptoRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoCriptoResponseDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoCriptoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LancamentoCriptoMapper {

    LancamentoCriptoMapper INSTANCE = Mappers.getMapper(LancamentoCriptoMapper.class);

    LancamentoCriptoEntity toEntity(LancamentoCriptoRequestDTO requestDTO);

    @Mapping(target = "valorTotal", ignore = true) // This is a calculated field
    LancamentoCriptoResponseDTO toResponseDTO(LancamentoCriptoEntity entity);
}