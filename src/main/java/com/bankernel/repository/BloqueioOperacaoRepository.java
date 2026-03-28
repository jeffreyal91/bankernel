package com.bankernel.repository;

import com.bankernel.domain.BloqueioOperacao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BloqueioOperacao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BloqueioOperacaoRepository extends JpaRepository<BloqueioOperacao, Long> {}
