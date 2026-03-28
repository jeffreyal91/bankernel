package com.bankernel.service.mapper;

import com.bankernel.domain.Carteira;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.Transacao;
import com.bankernel.domain.Transferencia;
import com.bankernel.domain.User;
import com.bankernel.service.dto.CarteiraDTO;
import com.bankernel.service.dto.MoedaCarteiraDTO;
import com.bankernel.service.dto.TransacaoDTO;
import com.bankernel.service.dto.TransferenciaDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transferencia} and its DTO {@link TransferenciaDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransferenciaMapper extends EntityMapper<TransferenciaDTO, Transferencia> {
    @Mapping(target = "transacao", source = "transacao", qualifiedByName = "transacaoId")
    @Mapping(target = "usuarioOrigem", source = "usuarioOrigem", qualifiedByName = "userLogin")
    @Mapping(target = "usuarioDestino", source = "usuarioDestino", qualifiedByName = "userLogin")
    @Mapping(target = "carteiraOrigem", source = "carteiraOrigem", qualifiedByName = "carteiraNumeroConta")
    @Mapping(target = "carteiraDestino", source = "carteiraDestino", qualifiedByName = "carteiraNumeroConta")
    @Mapping(target = "moedaCarteira", source = "moedaCarteira", qualifiedByName = "moedaCarteiraCodigo")
    TransferenciaDTO toDto(Transferencia s);

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
}
