package com.bankernel.service.mapper;

import com.bankernel.domain.Administrador;
import com.bankernel.domain.Escritorio;
import com.bankernel.domain.User;
import com.bankernel.service.dto.AdministradorDTO;
import com.bankernel.service.dto.EscritorioDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Administrador} and its DTO {@link AdministradorDTO}.
 */
@Mapper(componentModel = "spring")
public interface AdministradorMapper extends EntityMapper<AdministradorDTO, Administrador> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    @Mapping(target = "escritorio", source = "escritorio", qualifiedByName = "escritorioNome")
    AdministradorDTO toDto(Administrador s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("escritorioNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    EscritorioDTO toDtoEscritorioNome(Escritorio escritorio);
}
