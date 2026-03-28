package com.bankernel.repository;

import com.bankernel.domain.Endereco;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Endereco entity.
 */
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    @Query("select endereco from Endereco endereco where endereco.usuario.login = ?#{authentication.name}")
    List<Endereco> findByUsuarioIsCurrentUser();

    default Optional<Endereco> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Endereco> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Endereco> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select endereco from Endereco endereco left join fetch endereco.usuario",
        countQuery = "select count(endereco) from Endereco endereco"
    )
    Page<Endereco> findAllWithToOneRelationships(Pageable pageable);

    @Query("select endereco from Endereco endereco left join fetch endereco.usuario")
    List<Endereco> findAllWithToOneRelationships();

    @Query("select endereco from Endereco endereco left join fetch endereco.usuario where endereco.id =:id")
    Optional<Endereco> findOneWithToOneRelationships(@Param("id") Long id);
}
