package com.bankernel.service.mapper;

import com.bankernel.domain.ColaboradorPJ;
import com.bankernel.domain.PessoaJuridica;
import com.bankernel.domain.User;
import com.bankernel.service.dto.ColaboradorPJDTO;
import com.bankernel.service.dto.PessoaJuridicaDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ColaboradorPJ} and its DTO {@link ColaboradorPJDTO}.
 */
@Mapper(componentModel = "spring")
public interface ColaboradorPJMapper extends EntityMapper<ColaboradorPJDTO, ColaboradorPJ> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    @Mapping(target = "pessoaJuridica", source = "pessoaJuridica", qualifiedByName = "pessoaJuridicaRazaoSocial")
    ColaboradorPJDTO toDto(ColaboradorPJ s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("pessoaJuridicaRazaoSocial")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "razaoSocial", source = "razaoSocial")
    PessoaJuridicaDTO toDtoPessoaJuridicaRazaoSocial(PessoaJuridica pessoaJuridica);
}
