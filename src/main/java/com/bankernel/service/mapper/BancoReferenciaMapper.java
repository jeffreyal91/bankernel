package com.bankernel.service.mapper;

import com.bankernel.domain.BancoReferencia;
import com.bankernel.service.dto.BancoReferenciaDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BancoReferencia} and its DTO {@link BancoReferenciaDTO}.
 */
@Mapper(componentModel = "spring")
public interface BancoReferenciaMapper extends EntityMapper<BancoReferenciaDTO, BancoReferencia> {}
