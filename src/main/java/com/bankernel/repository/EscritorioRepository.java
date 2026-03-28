package com.bankernel.repository;

import com.bankernel.domain.Escritorio;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Escritorio entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EscritorioRepository extends JpaRepository<Escritorio, Long> {}
