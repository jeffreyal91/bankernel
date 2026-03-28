package com.bankernel.service.mapper;

import com.bankernel.domain.Carteira;
import com.bankernel.domain.Cobranca;
import com.bankernel.domain.LinkCobranca;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.Transacao;
import com.bankernel.domain.User;
import com.bankernel.service.dto.CarteiraDTO;
import com.bankernel.service.dto.CobrancaDTO;
import com.bankernel.service.dto.LinkCobrancaDTO;
import com.bankernel.service.dto.MoedaCarteiraDTO;
import com.bankernel.service.dto.TransacaoDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cobranca} and its DTO {@link CobrancaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CobrancaMapper extends EntityMapper<CobrancaDTO, Cobranca> {
    @Mapping(target = "transacao", source = "transacao", qualifiedByName = "transacaoId")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    @Mapping(target = "carteira", source = "carteira", qualifiedByName = "carteiraNumeroConta")
    @Mapping(target = "carteiraCreditada", source = "carteiraCreditada", qualifiedByName = "carteiraNumeroConta")
    @Mapping(target = "moedaCarteira", source = "moedaCarteira", qualifiedByName = "moedaCarteiraCodigo")
    @Mapping(target = "linkCobranca", source = "linkCobranca", qualifiedByName = "linkCobrancaNome")
    CobrancaDTO toDto(Cobranca s);

    @Named("transacaoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransacaoDTO toDtoTransacaoId(Transacao transacao);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("carteiraNumeroConta")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "numeroConta", source = "numeroConta")
    CarteiraDTO toDtoCarteiraNumeroConta(Carteira carteira);

    @Named("moedaCarteiraCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    MoedaCarteiraDTO toDtoMoedaCarteiraCodigo(MoedaCarteira moedaCarteira);

    @Named("linkCobrancaNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    LinkCobrancaDTO toDtoLinkCobrancaNome(LinkCobranca linkCobranca);
}
