package com.bankernel.repository;

import com.bankernel.domain.ContaBancaria;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContaBancaria entity.
 */
@Repository
public interface ContaBancariaRepository extends JpaRepository<ContaBancaria, Long>, JpaSpecificationExecutor<ContaBancaria> {
    @Query("select contaBancaria from ContaBancaria contaBancaria where contaBancaria.usuario.login = ?#{authentication.name}")
    List<ContaBancaria> findByUsuarioIsCurrentUser();

    default Optional<ContaBancaria> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ContaBancaria> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ContaBancaria> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select contaBancaria from ContaBancaria contaBancaria left join fetch contaBancaria.usuario left join fetch contaBancaria.pais left join fetch contaBancaria.moeda",
        countQuery = "select count(contaBancaria) from ContaBancaria contaBancaria"
    )
    Page<ContaBancaria> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select contaBancaria from ContaBancaria contaBancaria left join fetch contaBancaria.usuario left join fetch contaBancaria.pais left join fetch contaBancaria.moeda"
    )
    List<ContaBancaria> findAllWithToOneRelationships();

    @Query(
        "select contaBancaria from ContaBancaria contaBancaria left join fetch contaBancaria.usuario left join fetch contaBancaria.pais left join fetch contaBancaria.moeda where contaBancaria.id =:id"
    )
    Optional<ContaBancaria> findOneWithToOneRelationships(@Param("id") Long id);
}
