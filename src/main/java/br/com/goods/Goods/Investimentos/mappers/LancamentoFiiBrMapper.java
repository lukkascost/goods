package br.com.goods.Goods.Investimentos.mappers;

import br.com.goods.Goods.Investimentos.models.dto.LancamentoFiiBrRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoFiiBrResponseDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoFiiBrEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LancamentoFiiBrMapper {

    LancamentoFiiBrMapper INSTANCE = Mappers.getMapper(LancamentoFiiBrMapper.class);

    LancamentoFiiBrEntity toEntity(LancamentoFiiBrRequestDTO requestDTO);

    @Mapping(target = "valorTotal", ignore = true) // This is a calculated field
    LancamentoFiiBrResponseDTO toResponseDTO(LancamentoFiiBrEntity entity);
}