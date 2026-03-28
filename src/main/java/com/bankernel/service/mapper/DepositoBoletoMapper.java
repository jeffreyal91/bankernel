package com.bankernel.service.mapper;

import com.bankernel.domain.Carteira;
import com.bankernel.domain.Deposito;
import com.bankernel.domain.DepositoBoleto;
import com.bankernel.domain.User;
import com.bankernel.service.dto.CarteiraDTO;
import com.bankernel.service.dto.DepositoBoletoDTO;
import com.bankernel.service.dto.DepositoDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DepositoBoleto} and its DTO {@link DepositoBoletoDTO}.
 */
@Mapper(componentModel = "spring")
public interface DepositoBoletoMapper extends EntityMapper<DepositoBoletoDTO, DepositoBoleto> {
    @Mapping(target = "deposito", source = "deposito", qualifiedByName = "depositoId")
    @Mapping(target = "carteira", source = "carteira", qualifiedByName = "carteiraNumeroConta")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    DepositoBoletoDTO toDto(DepositoBoleto s);

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
