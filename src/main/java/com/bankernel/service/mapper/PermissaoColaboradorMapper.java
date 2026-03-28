package com.bankernel.service.mapper;

import com.bankernel.domain.Carteira;
import com.bankernel.domain.ColaboradorPJ;
import com.bankernel.domain.PermissaoColaborador;
import com.bankernel.service.dto.CarteiraDTO;
import com.bankernel.service.dto.ColaboradorPJDTO;
import com.bankernel.service.dto.PermissaoColaboradorDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PermissaoColaborador} and its DTO {@link PermissaoColaboradorDTO}.
 */
@Mapper(componentModel = "spring")
public interface PermissaoColaboradorMapper extends EntityMapper<PermissaoColaboradorDTO, PermissaoColaborador> {
    @Mapping(target = "colaborador", source = "colaborador", qualifiedByName = "colaboradorPJId")
    @Mapping(target = "carteira", source = "carteira", qualifiedByName = "carteiraNumeroConta")
    PermissaoColaboradorDTO toDto(PermissaoColaborador s);

    @Named("colaboradorPJId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ColaboradorPJDTO toDtoColaboradorPJId(ColaboradorPJ colaboradorPJ);

    @Named("carteiraNumeroConta")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numeroConta", source = "numeroConta")
    CarteiraDTO toDtoCarteiraNumeroConta(Carteira carteira);
}
