package com.bankernel.repository;

import com.bankernel.domain.Documento;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Documento entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {}
