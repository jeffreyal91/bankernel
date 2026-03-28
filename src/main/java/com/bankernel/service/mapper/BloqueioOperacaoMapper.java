package com.bankernel.service.mapper;

import com.bankernel.domain.BloqueioOperacao;
import com.bankernel.service.dto.BloqueioOperacaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BloqueioOperacao} and its DTO {@link BloqueioOperacaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface BloqueioOperacaoMapper extends EntityMapper<BloqueioOperacaoDTO, BloqueioOperacao> {}
