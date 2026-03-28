package com.bankernel.repository;

import com.bankernel.domain.DepositoBoleto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DepositoBoleto entity.
 */
@Repository
public interface DepositoBoletoRepository extends JpaRepository<DepositoBoleto, Long> {
    @Query("select depositoBoleto from DepositoBoleto depositoBoleto where depositoBoleto.usuario.login = ?#{authentication.name}")
    List<DepositoBoleto> findByUsuarioIsCurrentUser();

    default Optional<DepositoBoleto> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DepositoBoleto> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DepositoBoleto> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select depositoBoleto from DepositoBoleto depositoBoleto left join fetch depositoBoleto.carteira left join fetch depositoBoleto.usuario",
        countQuery = "select count(depositoBoleto) from DepositoBoleto depositoBoleto"
    )
    Page<DepositoBoleto> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select depositoBoleto from DepositoBoleto depositoBoleto left join fetch depositoBoleto.carteira left join fetch depositoBoleto.usuario"
    )
    List<DepositoBoleto> findAllWithToOneRelationships();

    @Query(
        "select depositoBoleto from DepositoBoleto depositoBoleto left join fetch depositoBoleto.carteira left join fetch depositoBoleto.usuario where depositoBoleto.id =:id"
    )
    Optional<DepositoBoleto> findOneWithToOneRelationships(@Param("id") Long id);
}
