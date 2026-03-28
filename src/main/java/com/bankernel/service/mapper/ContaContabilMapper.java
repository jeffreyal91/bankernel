package com.bankernel.service.mapper;

import com.bankernel.domain.ContaContabil;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.service.dto.ContaContabilDTO;
import com.bankernel.service.dto.MoedaCarteiraDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContaContabil} and its DTO {@link ContaContabilDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContaContabilMapper extends EntityMapper<ContaContabilDTO, ContaContabil> {
    @Mapping(target = "moedaCarteira", source = "moedaCarteira", qualifiedByName = "moedaCarteiraCodigo")
    ContaContabilDTO toDto(ContaContabil s);

    @Named("moedaCarteiraCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    MoedaCarteiraDTO toDtoMoedaCarteiraCodigo(MoedaCarteira moedaCarteira);
}
