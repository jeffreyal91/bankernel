package com.bankernel.repository;

import com.bankernel.domain.Administrador;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Administrador entity.
 */
@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    default Optional<Administrador> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Administrador> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Administrador> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select administrador from Administrador administrador left join fetch administrador.usuario left join fetch administrador.escritorio",
        countQuery = "select count(administrador) from Administrador administrador"
    )
    Page<Administrador> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select administrador from Administrador administrador left join fetch administrador.usuario left join fetch administrador.escritorio"
    )
    List<Administrador> findAllWithToOneRelationships();

    @Query(
        "select administrador from Administrador administrador left join fetch administrador.usuario left join fetch administrador.escritorio where administrador.id =:id"
    )
    Optional<Administrador> findOneWithToOneRelationships(@Param("id") Long id);
}
