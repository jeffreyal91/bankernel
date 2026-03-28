package com.bankernel.repository;

import com.bankernel.domain.SaquePix;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SaquePix entity.
 */
@Repository
public interface SaquePixRepository extends JpaRepository<SaquePix, Long> {
    @Query("select saquePix from SaquePix saquePix where saquePix.usuario.login = ?#{authentication.name}")
    List<SaquePix> findByUsuarioIsCurrentUser();

    default Optional<SaquePix> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<SaquePix> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<SaquePix> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select saquePix from SaquePix saquePix left join fetch saquePix.usuario",
        countQuery = "select count(saquePix) from SaquePix saquePix"
    )
    Page<SaquePix> findAllWithToOneRelationships(Pageable pageable);

    @Query("select saquePix from SaquePix saquePix left join fetch saquePix.usuario")
    List<SaquePix> findAllWithToOneRelationships();

    @Query("select saquePix from SaquePix saquePix left join fetch saquePix.usuario where saquePix.id =:id")
    Optional<SaquePix> findOneWithToOneRelationships(@Param("id") Long id);
}
