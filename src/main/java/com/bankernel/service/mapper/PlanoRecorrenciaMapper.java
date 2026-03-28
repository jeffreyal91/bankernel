package com.bankernel.service.mapper;

import com.bankernel.domain.LinkCobranca;
import com.bankernel.domain.PlanoRecorrencia;
import com.bankernel.domain.User;
import com.bankernel.service.dto.LinkCobrancaDTO;
import com.bankernel.service.dto.PlanoRecorrenciaDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PlanoRecorrencia} and its DTO {@link PlanoRecorrenciaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlanoRecorrenciaMapper extends EntityMapper<PlanoRecorrenciaDTO, PlanoRecorrencia> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    @Mapping(target = "linkCobranca", source = "linkCobranca", qualifiedByName = "linkCobrancaNome")
    PlanoRecorrenciaDTO toDto(PlanoRecorrencia s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("linkCobrancaNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    LinkCobrancaDTO toDtoLinkCobrancaNome(LinkCobranca linkCobranca);
}
