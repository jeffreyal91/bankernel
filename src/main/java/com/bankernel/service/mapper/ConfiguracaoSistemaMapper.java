package com.bankernel.service.mapper;

import com.bankernel.domain.ConfiguracaoSistema;
import com.bankernel.service.dto.ConfiguracaoSistemaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConfiguracaoSistema} and its DTO {@link ConfiguracaoSistemaDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConfiguracaoSistemaMapper extends EntityMapper<ConfiguracaoSistemaDTO, ConfiguracaoSistema> {}
