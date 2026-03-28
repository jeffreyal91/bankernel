package com.bankernel.service.mapper;

import com.bankernel.domain.Documento;
import com.bankernel.service.dto.DocumentoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Documento} and its DTO {@link DocumentoDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentoMapper extends EntityMapper<DocumentoDTO, Documento> {}
