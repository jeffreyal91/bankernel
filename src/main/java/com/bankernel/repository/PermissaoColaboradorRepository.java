package com.bankernel.repository;

import com.bankernel.domain.PermissaoColaborador;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PermissaoColaborador entity.
 */
@Repository
public interface PermissaoColaboradorRepository extends JpaRepository<PermissaoColaborador, Long> {
    default Optional<PermissaoColaborador> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PermissaoColaborador> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PermissaoColaborador> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select permissaoColaborador from PermissaoColaborador permissaoColaborador left join fetch permissaoColaborador.carteira",
        countQuery = "select count(permissaoColaborador) from PermissaoColaborador permissaoColaborador"
    )
    Page<PermissaoColaborador> findAllWithToOneRelationships(Pageable pageable);

    @Query("select permissaoColaborador from PermissaoColaborador permissaoColaborador left join fetch permissaoColaborador.carteira")
    List<PermissaoColaborador> findAllWithToOneRelationships();

    @Query(
        "select permissaoColaborador from PermissaoColaborador permissaoColaborador left join fetch permissaoColaborador.carteira where permissaoColaborador.id =:id"
    )
    Optional<PermissaoColaborador> findOneWithToOneRelationships(@Param("id") Long id);
}
