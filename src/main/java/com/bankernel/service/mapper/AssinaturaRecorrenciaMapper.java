package com.bankernel.service.mapper;

import com.bankernel.domain.AssinaturaRecorrencia;
import com.bankernel.domain.LinkCobranca;
import com.bankernel.domain.PlanoRecorrencia;
import com.bankernel.domain.User;
import com.bankernel.service.dto.AssinaturaRecorrenciaDTO;
import com.bankernel.service.dto.LinkCobrancaDTO;
import com.bankernel.service.dto.PlanoRecorrenciaDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AssinaturaRecorrencia} and its DTO {@link AssinaturaRecorrenciaDTO}.
 */
@Mapper(componentModel = "spring")
public interface AssinaturaRecorrenciaMapper extends EntityMapper<AssinaturaRecorrenciaDTO, AssinaturaRecorrencia> {
    @Mapping(target = "planoRecorrencia", source = "planoRecorrencia", qualifiedByName = "planoRecorrenciaNome")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    @Mapping(target = "linkCobranca", source = "linkCobranca", qualifiedByName = "linkCobrancaNome")
    AssinaturaRecorrenciaDTO toDto(AssinaturaRecorrencia s);

    @Named("planoRecorrenciaNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    PlanoRecorrenciaDTO toDtoPlanoRecorrenciaNome(PlanoRecorrencia planoRecorrencia);

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
