package com.bankernel.service.mapper;

import com.bankernel.domain.Documento;
import com.bankernel.domain.Escritorio;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.Pais;
import com.bankernel.domain.PessoaJuridica;
import com.bankernel.domain.Plano;
import com.bankernel.domain.TipoNegocio;
import com.bankernel.domain.User;
import com.bankernel.service.dto.DocumentoDTO;
import com.bankernel.service.dto.EscritorioDTO;
import com.bankernel.service.dto.MoedaCarteiraDTO;
import com.bankernel.service.dto.PaisDTO;
import com.bankernel.service.dto.PessoaJuridicaDTO;
import com.bankernel.service.dto.PlanoDTO;
import com.bankernel.service.dto.TipoNegocioDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PessoaJuridica} and its DTO {@link PessoaJuridicaDTO}.
 */
@Mapper(componentModel = "spring")
public interface PessoaJuridicaMapper extends EntityMapper<PessoaJuridicaDTO, PessoaJuridica> {
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    @Mapping(target = "moedaPrincipal", source = "moedaPrincipal", qualifiedByName = "moedaCarteiraCodigo")
    @Mapping(target = "contratoSocial", source = "contratoSocial", qualifiedByName = "documentoNome")
    @Mapping(target = "nacionalidade", source = "nacionalidade", qualifiedByName = "paisNome")
    @Mapping(target = "tipoNegocio", source = "tipoNegocio", qualifiedByName = "tipoNegocioNome")
    @Mapping(target = "plano", source = "plano", qualifiedByName = "planoNome")
    @Mapping(target = "escritorio", source = "escritorio", qualifiedByName = "escritorioNome")
    PessoaJuridicaDTO toDto(PessoaJuridica s);

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

    @Named("documentoNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    DocumentoDTO toDtoDocumentoNome(Documento documento);

    @Named("paisNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    PaisDTO toDtoPaisNome(Pais pais);

    @Named("tipoNegocioNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    TipoNegocioDTO toDtoTipoNegocioNome(TipoNegocio tipoNegocio);

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
