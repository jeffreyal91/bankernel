package com.bankernel.service.mapper;

import com.bankernel.domain.Carteira;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.Transacao;
import com.bankernel.service.dto.CarteiraDTO;
import com.bankernel.service.dto.MoedaCarteiraDTO;
import com.bankernel.service.dto.TransacaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Transacao} and its DTO {@link TransacaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransacaoMapper extends EntityMapper<TransacaoDTO, Transacao> {
    @Mapping(target = "carteiraOrigem", source = "carteiraOrigem", qualifiedByName = "carteiraNumeroConta")
    @Mapping(target = "carteiraDestino", source = "carteiraDestino", qualifiedByName = "carteiraNumeroConta")
    @Mapping(target = "moedaOrigem", source = "moedaOrigem", qualifiedByName = "moedaCarteiraCodigo")
    @Mapping(target = "moedaDestino", source = "moedaDestino", qualifiedByName = "moedaCarteiraCodigo")
    TransacaoDTO toDto(Transacao s);

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
