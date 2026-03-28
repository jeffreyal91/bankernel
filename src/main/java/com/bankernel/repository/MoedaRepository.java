package com.bankernel.repository;

import com.bankernel.domain.Moeda;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Moeda entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MoedaRepository extends JpaRepository<Moeda, Long> {}
