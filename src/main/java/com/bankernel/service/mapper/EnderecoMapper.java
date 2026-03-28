package com.bankernel.service.mapper;

import com.bankernel.domain.Endereco;
import com.bankernel.domain.User;
import com.bankernel.service.dto.EnderecoDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Endereco} and its DTO {@link EnderecoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EnderecoMapper extends EntityMapper<EnderecoDTO, Endereco> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    EnderecoDTO toDto(Endereco s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);
}
