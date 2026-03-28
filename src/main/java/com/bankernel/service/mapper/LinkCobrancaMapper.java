package com.bankernel.service.mapper;

import com.bankernel.domain.LinkCobranca;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.User;
import com.bankernel.service.dto.LinkCobrancaDTO;
import com.bankernel.service.dto.MoedaCarteiraDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LinkCobranca} and its DTO {@link LinkCobrancaDTO}.
 */
@Mapper(componentModel = "spring")
public interface LinkCobrancaMapper extends EntityMapper<LinkCobrancaDTO, LinkCobranca> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    @Mapping(target = "moedaCarteira", source = "moedaCarteira", qualifiedByName = "moedaCarteiraCodigo")
    LinkCobrancaDTO toDto(LinkCobranca s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("moedaCarteiraCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    MoedaCarteiraDTO toDtoMoedaCarteiraCodigo(MoedaCarteira moedaCarteira);
}
