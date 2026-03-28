package com.bankernel.repository;

import com.bankernel.domain.Saque;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Saque entity.
 */
@Repository
public interface SaqueRepository extends JpaRepository<Saque, Long>, JpaSpecificationExecutor<Saque> {
    @Query("select saque from Saque saque where saque.usuario.login = ?#{authentication.name}")
    List<Saque> findByUsuarioIsCurrentUser();

    default Optional<Saque> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Saque> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Saque> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select saque from Saque saque left join fetch saque.carteira left join fetch saque.moedaCarteira left join fetch saque.usuario left join fetch saque.escritorio",
        countQuery = "select count(saque) from Saque saque"
    )
    Page<Saque> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select saque from Saque saque left join fetch saque.carteira left join fetch saque.moedaCarteira left join fetch saque.usuario left join fetch saque.escritorio"
    )
    List<Saque> findAllWithToOneRelationships();

    @Query(
        "select saque from Saque saque left join fetch saque.carteira left join fetch saque.moedaCarteira left join fetch saque.usuario left join fetch saque.escritorio where saque.id =:id"
    )
    Optional<Saque> findOneWithToOneRelationships(@Param("id") Long id);
}
