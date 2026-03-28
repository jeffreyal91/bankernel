package com.bankernel.repository;

import com.bankernel.domain.Notificacao;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Notificacao entity.
 */
@Repository
public interface NotificacaoRepository extends JpaRepository<Notificacao, Long>, JpaSpecificationExecutor<Notificacao> {
    @Query("select notificacao from Notificacao notificacao where notificacao.usuario.login = ?#{authentication.name}")
    List<Notificacao> findByUsuarioIsCurrentUser();

    default Optional<Notificacao> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<Notificacao> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<Notificacao> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select notificacao from Notificacao notificacao left join fetch notificacao.usuario",
        countQuery = "select count(notificacao) from Notificacao notificacao"
    )
    Page<Notificacao> findAllWithToOneRelationships(Pageable pageable);

    @Query("select notificacao from Notificacao notificacao left join fetch notificacao.usuario")
    List<Notificacao> findAllWithToOneRelationships();

    @Query("select notificacao from Notificacao notificacao left join fetch notificacao.usuario where notificacao.id =:id")
    Optional<Notificacao> findOneWithToOneRelationships(@Param("id") Long id);
}
