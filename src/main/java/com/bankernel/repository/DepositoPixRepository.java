package com.bankernel.repository;

import com.bankernel.domain.DepositoPix;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DepositoPix entity.
 */
@Repository
public interface DepositoPixRepository extends JpaRepository<DepositoPix, Long> {
    @Query("select depositoPix from DepositoPix depositoPix where depositoPix.usuario.login = ?#{authentication.name}")
    List<DepositoPix> findByUsuarioIsCurrentUser();

    default Optional<DepositoPix> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<DepositoPix> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<DepositoPix> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select depositoPix from DepositoPix depositoPix left join fetch depositoPix.carteira left join fetch depositoPix.usuario",
        countQuery = "select count(depositoPix) from DepositoPix depositoPix"
    )
    Page<DepositoPix> findAllWithToOneRelationships(Pageable pageable);

    @Query("select depositoPix from DepositoPix depositoPix left join fetch depositoPix.carteira left join fetch depositoPix.usuario")
    List<DepositoPix> findAllWithToOneRelationships();

    @Query(
        "select depositoPix from DepositoPix depositoPix left join fetch depositoPix.carteira left join fetch depositoPix.usuario where depositoPix.id =:id"
    )
    Optional<DepositoPix> findOneWithToOneRelationships(@Param("id") Long id);
}
