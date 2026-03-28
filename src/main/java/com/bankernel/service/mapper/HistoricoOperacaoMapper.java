package com.bankernel.service.mapper;

import com.bankernel.domain.Carteira;
import com.bankernel.domain.HistoricoOperacao;
import com.bankernel.domain.Transacao;
import com.bankernel.domain.User;
import com.bankernel.service.dto.CarteiraDTO;
import com.bankernel.service.dto.HistoricoOperacaoDTO;
import com.bankernel.service.dto.TransacaoDTO;
import com.bankernel.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link HistoricoOperacao} and its DTO {@link HistoricoOperacaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface HistoricoOperacaoMapper extends EntityMapper<HistoricoOperacaoDTO, HistoricoOperacao> {
    @Mapping(target = "transacao", source = "transacao", qualifiedByName = "transacaoId")
    @Mapping(target = "usuario", source = "usuario", qualifiedByName = "userLogin")
    @Mapping(target = "carteira", source = "carteira", qualifiedByName = "carteiraNumeroConta")
    HistoricoOperacaoDTO toDto(HistoricoOperacao s);

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
}
