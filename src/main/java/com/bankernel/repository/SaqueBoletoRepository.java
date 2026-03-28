package com.bankernel.repository;

import com.bankernel.domain.SaqueBoleto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SaqueBoleto entity.
 */
@Repository
public interface SaqueBoletoRepository extends JpaRepository<SaqueBoleto, Long> {
    @Query("select saqueBoleto from SaqueBoleto saqueBoleto where saqueBoleto.usuario.login = ?#{authentication.name}")
    List<SaqueBoleto> findByUsuarioIsCurrentUser();

    default Optional<SaqueBoleto> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SaqueBoleto> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SaqueBoleto> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select saqueBoleto from SaqueBoleto saqueBoleto left join fetch saqueBoleto.usuario",
        countQuery = "select count(saqueBoleto) from SaqueBoleto saqueBoleto"
    )
    Page<SaqueBoleto> findAllWithToOneRelationships(Pageable pageable);

    @Query("select saqueBoleto from SaqueBoleto saqueBoleto left join fetch saqueBoleto.usuario")
    List<SaqueBoleto> findAllWithToOneRelationships();

    @Query("select saqueBoleto from SaqueBoleto saqueBoleto left join fetch saqueBoleto.usuario where saqueBoleto.id =:id")
    Optional<SaqueBoleto> findOneWithToOneRelationships(@Param("id") Long id);
}
