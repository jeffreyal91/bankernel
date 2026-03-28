package com.bankernel.repository;

import com.bankernel.domain.Transferencia;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Transferencia entity.
 */
@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long>, JpaSpecificationExecutor<Transferencia> {
    @Query("select transferencia from Transferencia transferencia where transferencia.usuarioOrigem.login = ?#{authentication.name}")
    List<Transferencia> findByUsuarioOrigemIsCurrentUser();

    @Query("select transferencia from Transferencia transferencia where transferencia.usuarioDestino.login = ?#{authentication.name}")
    List<Transferencia> findByUsuarioDestinoIsCurrentUser();

    default Optional<Transferencia> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Transferencia> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Transferencia> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select transferencia from Transferencia transferencia left join fetch transferencia.usuarioOrigem left join fetch transferencia.usuarioDestino left join fetch transferencia.carteiraOrigem left join fetch transferencia.carteiraDestino left join fetch transferencia.moedaCarteira",
        countQuery = "select count(transferencia) from Transferencia transferencia"
    )
    Page<Transferencia> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select transferencia from Transferencia transferencia left join fetch transferencia.usuarioOrigem left join fetch transferencia.usuarioDestino left join fetch transferencia.carteiraOrigem left join fetch transferencia.carteiraDestino left join fetch transferencia.moedaCarteira"
    )
    List<Transferencia> findAllWithToOneRelationships();

    @Query(
        "select transferencia from Transferencia transferencia left join fetch transferencia.usuarioOrigem left join fetch transferencia.usuarioDestino left join fetch transferencia.carteiraOrigem left join fetch transferencia.carteiraDestino left join fetch transferencia.moedaCarteira where transferencia.id =:id"
    )
    Optional<Transferencia> findOneWithToOneRelationships(@Param("id") Long id);
}
