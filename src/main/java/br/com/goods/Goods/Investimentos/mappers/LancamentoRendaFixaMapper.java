package br.com.goods.Goods.Investimentos.mappers;

import br.com.goods.Goods.Investimentos.models.dto.LancamentoRendaFixaRequestDTO;
import br.com.goods.Goods.Investimentos.models.dto.LancamentoRendaFixaResponseDTO;
import br.com.goods.Goods.Investimentos.models.entities.LancamentoRendaFixaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LancamentoRendaFixaMapper {

    LancamentoRendaFixaMapper INSTANCE = Mappers.getMapper(LancamentoRendaFixaMapper.class);

    LancamentoRendaFixaEntity toEntity(LancamentoRendaFixaRequestDTO requestDTO);

    @Mapping(target = "diasAteVencimento", ignore = true) // This is a calculated field
    @Mapping(target = "valorEstimadoNoVencimento", ignore = true) // This is a calculated field
    LancamentoRendaFixaResponseDTO toResponseDTO(LancamentoRendaFixaEntity entity);
}