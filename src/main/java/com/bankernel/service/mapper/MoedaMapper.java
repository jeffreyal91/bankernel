package com.bankernel.service.mapper;

import com.bankernel.domain.Moeda;
import com.bankernel.service.dto.MoedaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Moeda} and its DTO {@link MoedaDTO}.
 */
@Mapper(componentModel = "spring")
public interface MoedaMapper extends EntityMapper<MoedaDTO, Moeda> {}
