package com.bankernel.repository;

import com.bankernel.domain.LinkCobranca;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LinkCobranca entity.
 */
@Repository
public interface LinkCobrancaRepository extends JpaRepository<LinkCobranca, Long> {
    @Query("select linkCobranca from LinkCobranca linkCobranca where linkCobranca.usuario.login = ?#{authentication.name}")
    List<LinkCobranca> findByUsuarioIsCurrentUser();

    default Optional<LinkCobranca> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<LinkCobranca> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<LinkCobranca> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select linkCobranca from LinkCobranca linkCobranca left join fetch linkCobranca.usuario left join fetch linkCobranca.moedaCarteira",
        countQuery = "select count(linkCobranca) from LinkCobranca linkCobranca"
    )
    Page<LinkCobranca> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select linkCobranca from LinkCobranca linkCobranca left join fetch linkCobranca.usuario left join fetch linkCobranca.moedaCarteira"
    )
    List<LinkCobranca> findAllWithToOneRelationships();

    @Query(
        "select linkCobranca from LinkCobranca linkCobranca left join fetch linkCobranca.usuario left join fetch linkCobranca.moedaCarteira where linkCobranca.id =:id"
    )
    Optional<LinkCobranca> findOneWithToOneRelationships(@Param("id") Long id);
}
