package com.bankernel.service.mapper;

import com.bankernel.domain.Feriado;
import com.bankernel.service.dto.FeriadoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Feriado} and its DTO {@link FeriadoDTO}.
 */
@Mapper(componentModel = "spring")
public interface FeriadoMapper extends EntityMapper<FeriadoDTO, Feriado> {}
