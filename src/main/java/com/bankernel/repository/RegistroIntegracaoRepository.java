package com.bankernel.repository;

import com.bankernel.domain.RegistroIntegracao;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RegistroIntegracao entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RegistroIntegracaoRepository
    extends JpaRepository<RegistroIntegracao, Long>, JpaSpecificationExecutor<RegistroIntegracao> {}
