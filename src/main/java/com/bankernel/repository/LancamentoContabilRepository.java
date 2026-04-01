package com.bankernel.repository;

import com.bankernel.domain.LancamentoContabil;
import com.bankernel.domain.Transacao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LancamentoContabil entity.
 */
@Repository
public interface LancamentoContabilRepository
    extends JpaRepository<LancamentoContabil, Long>, JpaSpecificationExecutor<LancamentoContabil> {
    default Optional<LancamentoContabil> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<LancamentoContabil> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<LancamentoContabil> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select lancamentoContabil from LancamentoContabil lancamentoContabil left join fetch lancamentoContabil.contaContabil",
        countQuery = "select count(lancamentoContabil) from LancamentoContabil lancamentoContabil"
    )
    Page<LancamentoContabil> findAllWithToOneRelationships(Pageable pageable);

    @Query("select lancamentoContabil from LancamentoContabil lancamentoContabil left join fetch lancamentoContabil.contaContabil")
    List<LancamentoContabil> findAllWithToOneRelationships();

    @Query(
        "select lancamentoContabil from LancamentoContabil lancamentoContabil left join fetch lancamentoContabil.contaContabil where lancamentoContabil.id =:id"
    )
    Optional<LancamentoContabil> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select l from LancamentoContabil l left join fetch l.contaContabil where l.transacao = :transacao and l.ativo = true"
    )
    List<LancamentoContabil> findByTransacaoAndAtivoTrue(@Param("transacao") Transacao transacao);
}
