package com.bankernel.repository;

import com.bankernel.domain.Deposito;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Deposito entity.
 */
@Repository
public interface DepositoRepository extends JpaRepository<Deposito, Long>, JpaSpecificationExecutor<Deposito> {
    @Query("select deposito from Deposito deposito where deposito.usuario.login = ?#{authentication.name}")
    List<Deposito> findByUsuarioIsCurrentUser();

    default Optional<Deposito> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Deposito> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Deposito> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select deposito from Deposito deposito left join fetch deposito.carteira left join fetch deposito.moedaCarteira left join fetch deposito.usuario",
        countQuery = "select count(deposito) from Deposito deposito"
    )
    Page<Deposito> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select deposito from Deposito deposito left join fetch deposito.carteira left join fetch deposito.moedaCarteira left join fetch deposito.usuario"
    )
    List<Deposito> findAllWithToOneRelationships();

    @Query(
        "select deposito from Deposito deposito left join fetch deposito.carteira left join fetch deposito.moedaCarteira left join fetch deposito.usuario where deposito.id =:id"
    )
    Optional<Deposito> findOneWithToOneRelationships(@Param("id") Long id);
}
