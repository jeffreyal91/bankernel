package com.bankernel.repository;

import com.bankernel.domain.PessoaJuridica;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PessoaJuridica entity.
 */
@Repository
public interface PessoaJuridicaRepository extends JpaRepository<PessoaJuridica, Long>, JpaSpecificationExecutor<PessoaJuridica> {
    default Optional<PessoaJuridica> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PessoaJuridica> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PessoaJuridica> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select pessoaJuridica from PessoaJuridica pessoaJuridica left join fetch pessoaJuridica.usuario left join fetch pessoaJuridica.moedaPrincipal left join fetch pessoaJuridica.contratoSocial left join fetch pessoaJuridica.nacionalidade left join fetch pessoaJuridica.tipoNegocio left join fetch pessoaJuridica.plano left join fetch pessoaJuridica.escritorio",
        countQuery = "select count(pessoaJuridica) from PessoaJuridica pessoaJuridica"
    )
    Page<PessoaJuridica> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select pessoaJuridica from PessoaJuridica pessoaJuridica left join fetch pessoaJuridica.usuario left join fetch pessoaJuridica.moedaPrincipal left join fetch pessoaJuridica.contratoSocial left join fetch pessoaJuridica.nacionalidade left join fetch pessoaJuridica.tipoNegocio left join fetch pessoaJuridica.plano left join fetch pessoaJuridica.escritorio"
    )
    List<PessoaJuridica> findAllWithToOneRelationships();

    @Query(
        "select pessoaJuridica from PessoaJuridica pessoaJuridica left join fetch pessoaJuridica.usuario left join fetch pessoaJuridica.moedaPrincipal left join fetch pessoaJuridica.contratoSocial left join fetch pessoaJuridica.nacionalidade left join fetch pessoaJuridica.tipoNegocio left join fetch pessoaJuridica.plano left join fetch pessoaJuridica.escritorio where pessoaJuridica.id =:id"
    )
    Optional<PessoaJuridica> findOneWithToOneRelationships(@Param("id") Long id);
}
