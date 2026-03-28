package com.bankernel.service.mapper;

import com.bankernel.domain.Escritorio;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.Pais;
import com.bankernel.domain.PessoaFisica;
import com.bankernel.domain.Plano;
import com.bankernel.domain.Profissao;
import com.bankernel.domain.User;
import com.bankernel.service.dto.EscritorioDTO;
import com.bankernel.service.dto.MoedaCarteiraDTO;
import com.bankernel.service.dto.PaisDTO;
import com.bankernel.service.dto.PessoaFisicaDTO;
import com.bankernel.service.dto.PlanoDTO;
import com.bankernel.service.dto.ProfissaoDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PessoaFisica} and its DTO {@link PessoaFisicaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PessoaFisicaMapper extends EntityMapper<PessoaFisicaDTO, PessoaFisica> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    @Mapping(target = "moedaPrincipal", source = "moedaPrincipal", qualifiedByName = "moedaCarteiraCodigo")
    @Mapping(target = "nacionalidade", source = "nacionalidade", qualifiedByName = "paisNome")
    @Mapping(target = "profissao", source = "profissao", qualifiedByName = "profissaoNome")
    @Mapping(target = "plano", source = "plano", qualifiedByName = "planoNome")
    @Mapping(target = "escritorio", source = "escritorio", qualifiedByName = "escritorioNome")
    PessoaFisicaDTO toDto(PessoaFisica s);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("moedaCarteiraCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    MoedaCarteiraDTO toDtoMoedaCarteiraCodigo(MoedaCarteira moedaCarteira);

    @Named("paisNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    PaisDTO toDtoPaisNome(Pais pais);

    @Named("profissaoNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    ProfissaoDTO toDtoProfissaoNome(Profissao profissao);

    @Named("planoNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    PlanoDTO toDtoPlanoNome(Plano plano);

    @Named("escritorioNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    EscritorioDTO toDtoEscritorioNome(Escritorio escritorio);
}
