package com.bankernel.service.mapper;

import com.bankernel.domain.Carteira;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.User;
import com.bankernel.service.dto.CarteiraDTO;
import com.bankernel.service.dto.MoedaCarteiraDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Carteira} and its DTO {@link CarteiraDTO}.
 */
@Mapper(componentModel = "spring")
public interface CarteiraMapper extends EntityMapper<CarteiraDTO, Carteira> {
    @Mapping(target = "moedaCarteira", source = "moedaCarteira", qualifiedByName = "moedaCarteiraCodigo")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    CarteiraDTO toDto(Carteira s);

    @Named("moedaCarteiraCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    MoedaCarteiraDTO toDtoMoedaCarteiraCodigo(MoedaCarteira moedaCarteira);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
