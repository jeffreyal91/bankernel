package com.bankernel.repository;

import com.bankernel.domain.Transacao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Transacao entity.
 */
@Repository
public interface TransacaoRepository extends JpaRepository<Transacao, Long>, JpaSpecificationExecutor<Transacao> {
    default Optional<Transacao> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Transacao> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Transacao> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select transacao from Transacao transacao left join fetch transacao.carteiraOrigem left join fetch transacao.carteiraDestino left join fetch transacao.moedaOrigem left join fetch transacao.moedaDestino",
        countQuery = "select count(transacao) from Transacao transacao"
    )
    Page<Transacao> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select transacao from Transacao transacao left join fetch transacao.carteiraOrigem left join fetch transacao.carteiraDestino left join fetch transacao.moedaOrigem left join fetch transacao.moedaDestino"
    )
    List<Transacao> findAllWithToOneRelationships();

    @Query(
        "select transacao from Transacao transacao left join fetch transacao.carteiraOrigem left join fetch transacao.carteiraDestino left join fetch transacao.moedaOrigem left join fetch transacao.moedaDestino where transacao.id =:id"
    )
    Optional<Transacao> findOneWithToOneRelationships(@Param("id") Long id);
}
