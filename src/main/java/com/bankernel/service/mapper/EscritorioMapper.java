package com.bankernel.service.mapper;

import com.bankernel.domain.Escritorio;
import com.bankernel.service.dto.EscritorioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Escritorio} and its DTO {@link EscritorioDTO}.
 */
@Mapper(componentModel = "spring")
public interface EscritorioMapper extends EntityMapper<EscritorioDTO, Escritorio> {}
