package com.bankernel.service.mapper;

import com.bankernel.domain.Moeda;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.service.dto.MoedaCarteiraDTO;
import com.bankernel.service.dto.MoedaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MoedaCarteira} and its DTO {@link MoedaCarteiraDTO}.
 */
@Mapper(componentModel = "spring")
public interface MoedaCarteiraMapper extends EntityMapper<MoedaCarteiraDTO, MoedaCarteira> {
    @Mapping(target = "moeda", source = "moeda", qualifiedByName = "moedaNome")
    MoedaCarteiraDTO toDto(MoedaCarteira s);

    @Named("moedaNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    MoedaDTO toDtoMoedaNome(Moeda moeda);
}
