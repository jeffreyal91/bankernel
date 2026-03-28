package com.bankernel.repository;

import com.bankernel.domain.HistoricoOperacao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HistoricoOperacao entity.
 */
@Repository
public interface HistoricoOperacaoRepository extends JpaRepository<HistoricoOperacao, Long>, JpaSpecificationExecutor<HistoricoOperacao> {
    @Query(
        "select historicoOperacao from HistoricoOperacao historicoOperacao where historicoOperacao.usuario.login = ?#{authentication.name}"
    )
    List<HistoricoOperacao> findByUsuarioIsCurrentUser();

    default Optional<HistoricoOperacao> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<HistoricoOperacao> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<HistoricoOperacao> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select historicoOperacao from HistoricoOperacao historicoOperacao left join fetch historicoOperacao.usuario left join fetch historicoOperacao.carteira",
        countQuery = "select count(historicoOperacao) from HistoricoOperacao historicoOperacao"
    )
    Page<HistoricoOperacao> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select historicoOperacao from HistoricoOperacao historicoOperacao left join fetch historicoOperacao.usuario left join fetch historicoOperacao.carteira"
    )
    List<HistoricoOperacao> findAllWithToOneRelationships();

    @Query(
        "select historicoOperacao from HistoricoOperacao historicoOperacao left join fetch historicoOperacao.usuario left join fetch historicoOperacao.carteira where historicoOperacao.id =:id"
    )
    Optional<HistoricoOperacao> findOneWithToOneRelationships(@Param("id") Long id);
}
