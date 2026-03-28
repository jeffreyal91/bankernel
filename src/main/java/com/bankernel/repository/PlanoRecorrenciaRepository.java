package com.bankernel.repository;

import com.bankernel.domain.PlanoRecorrencia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PlanoRecorrencia entity.
 */
@Repository
public interface PlanoRecorrenciaRepository extends JpaRepository<PlanoRecorrencia, Long> {
    @Query("select planoRecorrencia from PlanoRecorrencia planoRecorrencia where planoRecorrencia.usuario.login = ?#{authentication.name}")
    List<PlanoRecorrencia> findByUsuarioIsCurrentUser();

    default Optional<PlanoRecorrencia> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PlanoRecorrencia> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PlanoRecorrencia> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select planoRecorrencia from PlanoRecorrencia planoRecorrencia left join fetch planoRecorrencia.usuario left join fetch planoRecorrencia.linkCobranca",
        countQuery = "select count(planoRecorrencia) from PlanoRecorrencia planoRecorrencia"
    )
    Page<PlanoRecorrencia> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select planoRecorrencia from PlanoRecorrencia planoRecorrencia left join fetch planoRecorrencia.usuario left join fetch planoRecorrencia.linkCobranca"
    )
    List<PlanoRecorrencia> findAllWithToOneRelationships();

    @Query(
        "select planoRecorrencia from PlanoRecorrencia planoRecorrencia left join fetch planoRecorrencia.usuario left join fetch planoRecorrencia.linkCobranca where planoRecorrencia.id =:id"
    )
    Optional<PlanoRecorrencia> findOneWithToOneRelationships(@Param("id") Long id);
}
