package com.bankernel.service.mapper;

import com.bankernel.domain.Carteira;
import com.bankernel.domain.ContaBancaria;
import com.bankernel.domain.Deposito;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.Transacao;
import com.bankernel.domain.User;
import com.bankernel.service.dto.CarteiraDTO;
import com.bankernel.service.dto.ContaBancariaDTO;
import com.bankernel.service.dto.DepositoDTO;
import com.bankernel.service.dto.MoedaCarteiraDTO;
import com.bankernel.service.dto.TransacaoDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Deposito} and its DTO {@link DepositoDTO}.
 */
@Mapper(componentModel = "spring")
public interface DepositoMapper extends EntityMapper<DepositoDTO, Deposito> {
    @Mapping(target = "transacao", source = "transacao", qualifiedByName = "transacaoId")
    @Mapping(target = "carteira", source = "carteira", qualifiedByName = "carteiraNumeroConta")
    @Mapping(target = "moedaCarteira", source = "moedaCarteira", qualifiedByName = "moedaCarteiraCodigo")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    @Mapping(target = "contaBancaria", source = "contaBancaria", qualifiedByName = "contaBancariaId")
    DepositoDTO toDto(Deposito s);

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

    @Named("userLogin")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "login", source = "login")
    UserDTO toDtoUserLogin(User user);

    @Named("contaBancariaId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContaBancariaDTO toDtoContaBancariaId(ContaBancaria contaBancaria);
}
