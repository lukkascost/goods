package br.com.goods.Goods.Investimentos.mappers;

import br.com.goods.Goods.Investimentos.models.dto.LancamentoAcaoBrRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoAcaoBrResponseDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoAcaoBrEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LancamentoAcaoBrMapper {

    LancamentoAcaoBrMapper INSTANCE = Mappers.getMapper(LancamentoAcaoBrMapper.class);

    LancamentoAcaoBrEntity toEntity(LancamentoAcaoBrRequestDTO requestDTO);

    @Mapping(target = "valorTotal", ignore = true) // This is a calculated field
    LancamentoAcaoBrResponseDTO toResponseDTO(LancamentoAcaoBrEntity entity);
}