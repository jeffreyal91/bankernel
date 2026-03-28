package com.bankernel.repository;

import com.bankernel.domain.ConfiguracaoSistema;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ConfiguracaoSistema entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConfiguracaoSistemaRepository extends JpaRepository<ConfiguracaoSistema, Long> {}
