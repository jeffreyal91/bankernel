package com.bankernel.service.mapper;

import com.bankernel.domain.Profissao;
import com.bankernel.service.dto.ProfissaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Profissao} and its DTO {@link ProfissaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProfissaoMapper extends EntityMapper<ProfissaoDTO, Profissao> {}
