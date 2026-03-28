package com.bankernel.service.mapper;

import com.bankernel.domain.RegistroIntegracao;
import com.bankernel.service.dto.RegistroIntegracaoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RegistroIntegracao} and its DTO {@link RegistroIntegracaoDTO}.
 */
@Mapper(componentModel = "spring")
public interface RegistroIntegracaoMapper extends EntityMapper<RegistroIntegracaoDTO, RegistroIntegracao> {}
