package com.bankernel.repository;

import com.bankernel.domain.BancoReferencia;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BancoReferencia entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BancoReferenciaRepository extends JpaRepository<BancoReferencia, Long> {}
