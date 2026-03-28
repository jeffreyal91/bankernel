package com.bankernel.service.mapper;

import com.bankernel.domain.Plano;
import com.bankernel.service.dto.PlanoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Plano} and its DTO {@link PlanoDTO}.
 */
@Mapper(componentModel = "spring")
public interface PlanoMapper extends EntityMapper<PlanoDTO, Plano> {}
