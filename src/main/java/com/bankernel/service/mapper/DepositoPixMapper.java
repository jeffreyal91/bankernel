package com.bankernel.service.mapper;

import com.bankernel.domain.Carteira;
import com.bankernel.domain.Deposito;
import com.bankernel.domain.DepositoPix;
import com.bankernel.domain.User;
import com.bankernel.service.dto.CarteiraDTO;
import com.bankernel.service.dto.DepositoDTO;
import com.bankernel.service.dto.DepositoPixDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DepositoPix} and its DTO {@link DepositoPixDTO}.
 */
@Mapper(componentModel = "spring")
public interface DepositoPixMapper extends EntityMapper<DepositoPixDTO, DepositoPix> {
    @Mapping(target = "deposito", source = "deposito", qualifiedByName = "depositoId")
    @Mapping(target = "carteira", source = "carteira", qualifiedByName = "carteiraNumeroConta")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    DepositoPixDTO toDto(DepositoPix s);

    @Named("depositoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepositoDTO toDtoDepositoId(Deposito deposito);

    @Named("carteiraNumeroConta")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numeroConta", source = "numeroConta")
    CarteiraDTO toDtoCarteiraNumeroConta(Carteira carteira);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
