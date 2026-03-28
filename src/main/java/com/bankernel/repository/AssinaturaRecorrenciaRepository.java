package com.bankernel.repository;

import com.bankernel.domain.AssinaturaRecorrencia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AssinaturaRecorrencia entity.
 */
@Repository
public interface AssinaturaRecorrenciaRepository extends JpaRepository<AssinaturaRecorrencia, Long> {
    @Query(
        "select assinaturaRecorrencia from AssinaturaRecorrencia assinaturaRecorrencia where assinaturaRecorrencia.usuario.login = ?#{authentication.name}"
    )
    List<AssinaturaRecorrencia> findByUsuarioIsCurrentUser();

    default Optional<AssinaturaRecorrencia> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<AssinaturaRecorrencia> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<AssinaturaRecorrencia> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select assinaturaRecorrencia from AssinaturaRecorrencia assinaturaRecorrencia left join fetch assinaturaRecorrencia.planoRecorrencia left join fetch assinaturaRecorrencia.usuario left join fetch assinaturaRecorrencia.linkCobranca",
        countQuery = "select count(assinaturaRecorrencia) from AssinaturaRecorrencia assinaturaRecorrencia"
    )
    Page<AssinaturaRecorrencia> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select assinaturaRecorrencia from AssinaturaRecorrencia assinaturaRecorrencia left join fetch assinaturaRecorrencia.planoRecorrencia left join fetch assinaturaRecorrencia.usuario left join fetch assinaturaRecorrencia.linkCobranca"
    )
    List<AssinaturaRecorrencia> findAllWithToOneRelationships();

    @Query(
        "select assinaturaRecorrencia from AssinaturaRecorrencia assinaturaRecorrencia left join fetch assinaturaRecorrencia.planoRecorrencia left join fetch assinaturaRecorrencia.usuario left join fetch assinaturaRecorrencia.linkCobranca where assinaturaRecorrencia.id =:id"
    )
    Optional<AssinaturaRecorrencia> findOneWithToOneRelationships(@Param("id") Long id);
}
