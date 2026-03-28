package com.bankernel.service.mapper;

import com.bankernel.domain.TipoNegocio;
import com.bankernel.service.dto.TipoNegocioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TipoNegocio} and its DTO {@link TipoNegocioDTO}.
 */
@Mapper(componentModel = "spring")
public interface TipoNegocioMapper extends EntityMapper<TipoNegocioDTO, TipoNegocio> {}
