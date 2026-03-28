package com.bankernel.repository;

import com.bankernel.domain.TipoNegocio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TipoNegocio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TipoNegocioRepository extends JpaRepository<TipoNegocio, Long> {}
