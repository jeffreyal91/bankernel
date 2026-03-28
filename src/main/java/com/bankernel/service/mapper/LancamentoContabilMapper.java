package com.bankernel.service.mapper;

import com.bankernel.domain.ContaContabil;
import com.bankernel.domain.LancamentoContabil;
import com.bankernel.domain.Transacao;
import com.bankernel.service.dto.ContaContabilDTO;
import com.bankernel.service.dto.LancamentoContabilDTO;
import com.bankernel.service.dto.TransacaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LancamentoContabil} and its DTO {@link LancamentoContabilDTO}.
 */
@Mapper(componentModel = "spring")
public interface LancamentoContabilMapper extends EntityMapper<LancamentoContabilDTO, LancamentoContabil> {
    @Mapping(target = "transacao", source = "transacao", qualifiedByName = "transacaoId")
    @Mapping(target = "contaContabil", source = "contaContabil", qualifiedByName = "contaContabilCodigo")
    LancamentoContabilDTO toDto(LancamentoContabil s);

    @Named("transacaoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TransacaoDTO toDtoTransacaoId(Transacao transacao);

    @Named("contaContabilCodigo")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "codigo", source = "codigo")
    ContaContabilDTO toDtoContaContabilCodigo(ContaContabil contaContabil);
}
