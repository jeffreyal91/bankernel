package com.bankernel.repository;

import com.bankernel.domain.PessoaFisica;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PessoaFisica entity.
 */
@Repository
public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long>, JpaSpecificationExecutor<PessoaFisica> {
    default Optional<PessoaFisica> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<PessoaFisica> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<PessoaFisica> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select pessoaFisica from PessoaFisica pessoaFisica left join fetch pessoaFisica.usuario left join fetch pessoaFisica.moedaPrincipal left join fetch pessoaFisica.nacionalidade left join fetch pessoaFisica.profissao left join fetch pessoaFisica.plano left join fetch pessoaFisica.escritorio",
        countQuery = "select count(pessoaFisica) from PessoaFisica pessoaFisica"
    )
    Page<PessoaFisica> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select pessoaFisica from PessoaFisica pessoaFisica left join fetch pessoaFisica.usuario left join fetch pessoaFisica.moedaPrincipal left join fetch pessoaFisica.nacionalidade left join fetch pessoaFisica.profissao left join fetch pessoaFisica.plano left join fetch pessoaFisica.escritorio"
    )
    List<PessoaFisica> findAllWithToOneRelationships();

    @Query(
        "select pessoaFisica from PessoaFisica pessoaFisica left join fetch pessoaFisica.usuario left join fetch pessoaFisica.moedaPrincipal left join fetch pessoaFisica.nacionalidade left join fetch pessoaFisica.profissao left join fetch pessoaFisica.plano left join fetch pessoaFisica.escritorio where pessoaFisica.id =:id"
    )
    Optional<PessoaFisica> findOneWithToOneRelationships(@Param("id") Long id);
}
