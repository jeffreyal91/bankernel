package com.bankernel.repository;

import com.bankernel.domain.ColaboradorPJ;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ColaboradorPJ entity.
 */
@Repository
public interface ColaboradorPJRepository extends JpaRepository<ColaboradorPJ, Long> {
    default Optional<ColaboradorPJ> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ColaboradorPJ> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ColaboradorPJ> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select colaboradorPJ from ColaboradorPJ colaboradorPJ left join fetch colaboradorPJ.usuario left join fetch colaboradorPJ.pessoaJuridica",
        countQuery = "select count(colaboradorPJ) from ColaboradorPJ colaboradorPJ"
    )
    Page<ColaboradorPJ> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select colaboradorPJ from ColaboradorPJ colaboradorPJ left join fetch colaboradorPJ.usuario left join fetch colaboradorPJ.pessoaJuridica"
    )
    List<ColaboradorPJ> findAllWithToOneRelationships();

    @Query(
        "select colaboradorPJ from ColaboradorPJ colaboradorPJ left join fetch colaboradorPJ.usuario left join fetch colaboradorPJ.pessoaJuridica where colaboradorPJ.id =:id"
    )
    Optional<ColaboradorPJ> findOneWithToOneRelationships(@Param("id") Long id);
}
