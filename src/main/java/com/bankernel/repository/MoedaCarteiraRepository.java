package com.bankernel.repository;

import com.bankernel.domain.MoedaCarteira;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MoedaCarteira entity.
 */
@Repository
public interface MoedaCarteiraRepository extends JpaRepository<MoedaCarteira, Long> {
    default Optional<MoedaCarteira> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<MoedaCarteira> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<MoedaCarteira> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select moedaCarteira from MoedaCarteira moedaCarteira left join fetch moedaCarteira.moeda",
        countQuery = "select count(moedaCarteira) from MoedaCarteira moedaCarteira"
    )
    Page<MoedaCarteira> findAllWithToOneRelationships(Pageable pageable);

    @Query("select moedaCarteira from MoedaCarteira moedaCarteira left join fetch moedaCarteira.moeda")
    List<MoedaCarteira> findAllWithToOneRelationships();

    @Query("select moedaCarteira from MoedaCarteira moedaCarteira left join fetch moedaCarteira.moeda where moedaCarteira.id =:id")
    Optional<MoedaCarteira> findOneWithToOneRelationships(@Param("id") Long id);
}
