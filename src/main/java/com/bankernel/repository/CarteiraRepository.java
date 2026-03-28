package com.bankernel.repository;

import com.bankernel.domain.Carteira;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Carteira entity.
 */
@Repository
public interface CarteiraRepository extends JpaRepository<Carteira, Long>, JpaSpecificationExecutor<Carteira> {
    @Query("select carteira from Carteira carteira where carteira.usuario.login = ?#{authentication.name}")
    List<Carteira> findByUsuarioIsCurrentUser();

    default Optional<Carteira> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Carteira> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Carteira> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select carteira from Carteira carteira left join fetch carteira.moedaCarteira left join fetch carteira.usuario",
        countQuery = "select count(carteira) from Carteira carteira"
    )
    Page<Carteira> findAllWithToOneRelationships(Pageable pageable);

    @Query("select carteira from Carteira carteira left join fetch carteira.moedaCarteira left join fetch carteira.usuario")
    List<Carteira> findAllWithToOneRelationships();

    @Query(
        "select carteira from Carteira carteira left join fetch carteira.moedaCarteira left join fetch carteira.usuario where carteira.id =:id"
    )
    Optional<Carteira> findOneWithToOneRelationships(@Param("id") Long id);
}
