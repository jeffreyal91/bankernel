package com.bankernel.service.mapper;

import com.bankernel.domain.Carteira;
import com.bankernel.domain.ContaBancaria;
import com.bankernel.domain.Escritorio;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.Saque;
import com.bankernel.domain.Transacao;
import com.bankernel.domain.User;
import com.bankernel.service.dto.CarteiraDTO;
import com.bankernel.service.dto.ContaBancariaDTO;
import com.bankernel.service.dto.EscritorioDTO;
import com.bankernel.service.dto.MoedaCarteiraDTO;
import com.bankernel.service.dto.SaqueDTO;
import com.bankernel.service.dto.TransacaoDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Saque} and its DTO {@link SaqueDTO}.
 */
@Mapper(componentModel = "spring")
public interface SaqueMapper extends EntityMapper<SaqueDTO, Saque> {
    @Mapping(target = "transacao", source = "transacao", qualifiedByName = "transacaoId")
    @Mapping(target = "transacaoEstorno", source = "transacaoEstorno", qualifiedByName = "transacaoId")
    @Mapping(target = "carteira", source = "carteira", qualifiedByName = "carteiraNumeroConta")
    @Mapping(target = "moedaCarteira", source = "moedaCarteira", qualifiedByName = "moedaCarteiraCodigo")
    @Mapping(target = "contaBancariaDestino", source = "contaBancariaDestino", qualifiedByName = "contaBancariaId")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    @Mapping(target = "escritorio", source = "escritorio", qualifiedByName = "escritorioNome")
    SaqueDTO toDto(Saque s);

    @Named("transacaoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransacaoDTO toDtoTransacaoId(Transacao transacao);

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

    @Named("contaBancariaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContaBancariaDTO toDtoContaBancariaId(ContaBancaria contaBancaria);

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("escritorioNome")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "nome", source = "nome")
    EscritorioDTO toDtoEscritorioNome(Escritorio escritorio);
}
