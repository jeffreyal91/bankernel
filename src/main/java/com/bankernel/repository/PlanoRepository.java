package com.bankernel.repository;

import com.bankernel.domain.Plano;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Plano entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PlanoRepository extends JpaRepository<Plano, Long> {}
