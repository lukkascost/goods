package br.com.goods.Goods.Investimentos.mappers;

import br.com.goods.Goods.Investimentos.models.dto.LancamentoEtfInterRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoEtfInterResponseDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoEtfInterEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LancamentoEtfInterMapper {

    LancamentoEtfInterMapper INSTANCE = Mappers.getMapper(LancamentoEtfInterMapper.class);

    LancamentoEtfInterEntity toEntity(LancamentoEtfInterRequestDTO requestDTO);

    @Mapping(target = "valorTotal", ignore = true) // This is a calculated field
    LancamentoEtfInterResponseDTO toResponseDTO(LancamentoEtfInterEntity entity);
}