package com.bankernel.service.mapper;

import com.bankernel.domain.Notificacao;
import com.bankernel.domain.User;
import com.bankernel.service.dto.NotificacaoDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notificacao} and its DTO {@link NotificacaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificacaoMapper extends EntityMapper<NotificacaoDTO, Notificacao> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    NotificacaoDTO toDto(Notificacao s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
