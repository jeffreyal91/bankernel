package com.bankernel.service.mapper;

import com.bankernel.domain.Saque;
import com.bankernel.domain.SaqueBoleto;
import com.bankernel.domain.User;
import com.bankernel.service.dto.SaqueBoletoDTO;
import com.bankernel.service.dto.SaqueDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SaqueBoleto} and its DTO {@link SaqueBoletoDTO}.
 */
@Mapper(componentModel = "spring")
public interface SaqueBoletoMapper extends EntityMapper<SaqueBoletoDTO, SaqueBoleto> {
    @Mapping(target = "saque", source = "saque", qualifiedByName = "saqueId")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    SaqueBoletoDTO toDto(SaqueBoleto s);

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
