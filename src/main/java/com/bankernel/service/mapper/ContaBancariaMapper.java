package com.bankernel.service.mapper;

import com.bankernel.domain.ContaBancaria;
import com.bankernel.domain.Moeda;
import com.bankernel.domain.Pais;
import com.bankernel.domain.User;
import com.bankernel.service.dto.ContaBancariaDTO;
import com.bankernel.service.dto.MoedaDTO;
import com.bankernel.service.dto.PaisDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContaBancaria} and its DTO {@link ContaBancariaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContaBancariaMapper extends EntityMapper<ContaBancariaDTO, ContaBancaria> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    @Mapping(target = "pais", source = "pais", qualifiedByName = "paisNome")
    @Mapping(target = "moeda", source = "moeda", qualifiedByName = "moedaNome")
    ContaBancariaDTO toDto(ContaBancaria s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("paisNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    PaisDTO toDtoPaisNome(Pais pais);

    @Named("moedaNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    MoedaDTO toDtoMoedaNome(Moeda moeda);
}
