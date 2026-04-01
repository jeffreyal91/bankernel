package com.bankernel.repository;

import com.bankernel.domain.TipoOperacao;
import com.bankernel.domain.enumeration.EnumTipoOperacao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TipoOperacao entity.
 */
@Repository
public interface TipoOperacaoRepository extends JpaRepository<TipoOperacao, Long> {
    default Optional<TipoOperacao> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<TipoOperacao> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<TipoOperacao> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select tipoOperacao from TipoOperacao tipoOperacao left join fetch tipoOperacao.contaCredito left join fetch tipoOperacao.contaDebito left join fetch tipoOperacao.moedaCarteira",
        countQuery = "select count(tipoOperacao) from TipoOperacao tipoOperacao"
    )
    Page<TipoOperacao> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select tipoOperacao from TipoOperacao tipoOperacao left join fetch tipoOperacao.contaCredito left join fetch tipoOperacao.contaDebito left join fetch tipoOperacao.moedaCarteira"
    )
    List<TipoOperacao> findAllWithToOneRelationships();

    @Query(
        "select tipoOperacao from TipoOperacao tipoOperacao left join fetch tipoOperacao.contaCredito left join fetch tipoOperacao.contaDebito left join fetch tipoOperacao.moedaCarteira where tipoOperacao.id =:id"
    )
    Optional<TipoOperacao> findOneWithToOneRelationships(@Param("id") Long id);

    @Query(
        "select t from TipoOperacao t left join fetch t.contaCredito left join fetch t.contaDebito where t.codigo = :codigo"
    )
    Optional<TipoOperacao> findByCodigoComContas(@Param("codigo") EnumTipoOperacao codigo);
}
