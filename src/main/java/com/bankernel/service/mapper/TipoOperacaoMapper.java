package com.bankernel.service.mapper;

import com.bankernel.domain.ContaContabil;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.TipoOperacao;
import com.bankernel.service.dto.ContaContabilDTO;
import com.bankernel.service.dto.MoedaCarteiraDTO;
import com.bankernel.service.dto.TipoOperacaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoOperacao} and its DTO {@link TipoOperacaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoOperacaoMapper extends EntityMapper<TipoOperacaoDTO, TipoOperacao> {
    @Mapping(target = "contaCredito", source = "contaCredito", qualifiedByName = "contaContabilCodigo")
    @Mapping(target = "contaDebito", source = "contaDebito", qualifiedByName = "contaContabilCodigo")
    @Mapping(target = "moedaCarteira", source = "moedaCarteira", qualifiedByName = "moedaCarteiraCodigo")
    TipoOperacaoDTO toDto(TipoOperacao s);

    @Named("contaContabilCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    ContaContabilDTO toDtoContaContabilCodigo(ContaContabil contaContabil);

    @Named("moedaCarteiraCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    MoedaCarteiraDTO toDtoMoedaCarteiraCodigo(MoedaCarteira moedaCarteira);
}
