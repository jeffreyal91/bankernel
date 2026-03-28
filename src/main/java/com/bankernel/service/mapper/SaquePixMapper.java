package com.bankernel.service.mapper;

import com.bankernel.domain.Saque;
import com.bankernel.domain.SaquePix;
import com.bankernel.domain.User;
import com.bankernel.service.dto.SaqueDTO;
import com.bankernel.service.dto.SaquePixDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SaquePix} and its DTO {@link SaquePixDTO}.
 */
@Mapper(componentModel = "spring")
public interface SaquePixMapper extends EntityMapper<SaquePixDTO, SaquePix> {
    @Mapping(target = "saque", source = "saque", qualifiedByName = "saqueId")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    SaquePixDTO toDto(SaquePix s);

    @Named("saqueId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SaqueDTO toDtoSaqueId(Saque saque);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
