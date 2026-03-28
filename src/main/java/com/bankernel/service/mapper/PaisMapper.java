package com.bankernel.service.mapper;

import com.bankernel.domain.Pais;
import com.bankernel.service.dto.PaisDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pais} and its DTO {@link PaisDTO}.
 */
@Mapper(componentModel = "spring")
public interface PaisMapper extends EntityMapper<PaisDTO, Pais> {}
