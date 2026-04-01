package com.bankernel.repository;

import com.bankernel.domain.ContaContabil;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContaContabil entity.
 */
@Repository
public interface ContaContabilRepository extends JpaRepository<ContaContabil, Long>, JpaSpecificationExecutor<ContaContabil> {
    default Optional<ContaContabil> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ContaContabil> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ContaContabil> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select contaContabil from ContaContabil contaContabil left join fetch contaContabil.moedaCarteira",
        countQuery = "select count(contaContabil) from ContaContabil contaContabil"
    )
    Page<ContaContabil> findAllWithToOneRelationships(Pageable pageable);

    @Query("select contaContabil from ContaContabil contaContabil left join fetch contaContabil.moedaCarteira")
    List<ContaContabil> findAllWithToOneRelationships();

    @Query("select contaContabil from ContaContabil contaContabil left join fetch contaContabil.moedaCarteira where contaContabil.id =:id")
    Optional<ContaContabil> findOneWithToOneRelationships(@Param("id") Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from ContaContabil c where c.id = :id")
    Optional<ContaContabil> findByIdComBloqueio(@Param("id") Long id);
}
