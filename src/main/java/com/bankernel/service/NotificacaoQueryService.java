package com.bankernel.service;

import com.bankernel.domain.*; // for static metamodels
import com.bankernel.domain.Notificacao;
import com.bankernel.repository.NotificacaoRepository;
import com.bankernel.service.criteria.NotificacaoCriteria;
import com.bankernel.service.dto.NotificacaoDTO;
import com.bankernel.service.mapper.NotificacaoMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Notificacao} entities in the database.
 * The main input is a {@link NotificacaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link NotificacaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NotificacaoQueryService extends QueryService<Notificacao> {

    private static final Logger LOG = LoggerFactory.getLogger(NotificacaoQueryService.class);

    private final NotificacaoRepository notificacaoRepository;

    private final NotificacaoMapper notificacaoMapper;

    public NotificacaoQueryService(NotificacaoRepository notificacaoRepository, NotificacaoMapper notificacaoMapper) {
        this.notificacaoRepository = notificacaoRepository;
        this.notificacaoMapper = notificacaoMapper;
    }

    /**
     * Return a {@link Page} of {@link NotificacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NotificacaoDTO> findByCriteria(NotificacaoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Notificacao> specification = createSpecification(criteria);
        return notificacaoRepository.findAll(specification, page).map(notificacaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NotificacaoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Notificacao> specification = createSpecification(criteria);
        return notificacaoRepository.count(specification);
    }

    /**
     * Function to convert {@link NotificacaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Notificacao> createSpecification(NotificacaoCriteria criteria) {
        Specification<Notificacao> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Notificacao_.id),
                buildStringSpecification(criteria.getTitulo(), Notificacao_.titulo),
                buildStringSpecification(criteria.getMensagem(), Notificacao_.mensagem),
                buildSpecification(criteria.getTipo(), Notificacao_.tipo),
                buildSpecification(criteria.getSituacao(), Notificacao_.situacao),
                buildSpecification(criteria.getCanal(), Notificacao_.canal),
                buildSpecification(criteria.getLida(), Notificacao_.lida),
                buildSpecification(criteria.getUsuarioId(), root -> root.join(Notificacao_.usuario, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
