package com.bankernel.repository;

import com.bankernel.domain.Cobranca;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Cobranca entity.
 */
@Repository
public interface CobrancaRepository extends JpaRepository<Cobranca, Long>, JpaSpecificationExecutor<Cobranca> {
    @Query("select cobranca from Cobranca cobranca where cobranca.usuario.login = ?#{authentication.name}")
    List<Cobranca> findByUsuarioIsCurrentUser();

    default Optional<Cobranca> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Cobranca> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Cobranca> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select cobranca from Cobranca cobranca left join fetch cobranca.usuario left join fetch cobranca.carteira left join fetch cobranca.carteiraCreditada left join fetch cobranca.moedaCarteira left join fetch cobranca.linkCobranca",
        countQuery = "select count(cobranca) from Cobranca cobranca"
    )
    Page<Cobranca> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select cobranca from Cobranca cobranca left join fetch cobranca.usuario left join fetch cobranca.carteira left join fetch cobranca.carteiraCreditada left join fetch cobranca.moedaCarteira left join fetch cobranca.linkCobranca"
    )
    List<Cobranca> findAllWithToOneRelationships();

    @Query(
        "select cobranca from Cobranca cobranca left join fetch cobranca.usuario left join fetch cobranca.carteira left join fetch cobranca.carteiraCreditada left join fetch cobranca.moedaCarteira left join fetch cobranca.linkCobranca where cobranca.id =:id"
    )
    Optional<Cobranca> findOneWithToOneRelationships(@Param("id") Long id);
}
