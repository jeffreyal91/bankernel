package com.bankernel.repository;

import com.bankernel.domain.Profissao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Profissao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfissaoRepository extends JpaRepository<Profissao, Long> {}
