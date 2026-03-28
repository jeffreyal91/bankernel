package com.bankernel.service;

import com.bankernel.domain.*; // for static metamodels
import com.bankernel.domain.HistoricoOperacao;
import com.bankernel.repository.HistoricoOperacaoRepository;
import com.bankernel.service.criteria.HistoricoOperacaoCriteria;
import com.bankernel.service.dto.HistoricoOperacaoDTO;
import com.bankernel.service.mapper.HistoricoOperacaoMapper;
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
 * Service for executing complex queries for {@link HistoricoOperacao} entities in the database.
 * The main input is a {@link HistoricoOperacaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HistoricoOperacaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HistoricoOperacaoQueryService extends QueryService<HistoricoOperacao> {

    private static final Logger LOG = LoggerFactory.getLogger(HistoricoOperacaoQueryService.class);

    private final HistoricoOperacaoRepository historicoOperacaoRepository;

    private final HistoricoOperacaoMapper historicoOperacaoMapper;

    public HistoricoOperacaoQueryService(
        HistoricoOperacaoRepository historicoOperacaoRepository,
        HistoricoOperacaoMapper historicoOperacaoMapper
    ) {
        this.historicoOperacaoRepository = historicoOperacaoRepository;
        this.historicoOperacaoMapper = historicoOperacaoMapper;
    }

    /**
     * Return a {@link Page} of {@link HistoricoOperacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HistoricoOperacaoDTO> findByCriteria(HistoricoOperacaoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HistoricoOperacao> specification = createSpecification(criteria);
        return historicoOperacaoRepository.findAll(specification, page).map(historicoOperacaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HistoricoOperacaoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HistoricoOperacao> specification = createSpecification(criteria);
        return historicoOperacaoRepository.count(specification);
    }

    /**
     * Function to convert {@link HistoricoOperacaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HistoricoOperacao> createSpecification(HistoricoOperacaoCriteria criteria) {
        Specification<HistoricoOperacao> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), HistoricoOperacao_.id),
                buildRangeSpecification(criteria.getValor(), HistoricoOperacao_.valor),
                buildRangeSpecification(criteria.getSaldoApos(), HistoricoOperacao_.saldoApos),
                buildStringSpecification(criteria.getDescricao(), HistoricoOperacao_.descricao),
                buildSpecification(criteria.getTipoSimbolo(), HistoricoOperacao_.tipoSimbolo),
                buildStringSpecification(criteria.getNumeroReferencia(), HistoricoOperacao_.numeroReferencia),
                buildSpecification(criteria.getTipoHistorico(), HistoricoOperacao_.tipoHistorico),
                buildSpecification(criteria.getSituacaoHistorico(), HistoricoOperacao_.situacaoHistorico),
                buildStringSpecification(criteria.getTipoEntidadeOrigem(), HistoricoOperacao_.tipoEntidadeOrigem),
                buildRangeSpecification(criteria.getIdEntidadeOrigem(), HistoricoOperacao_.idEntidadeOrigem),
                buildSpecification(criteria.getTransacaoId(), root ->
                    root.join(HistoricoOperacao_.transacao, JoinType.LEFT).get(Transacao_.id)
                ),
                buildSpecification(criteria.getUsuarioId(), root -> root.join(HistoricoOperacao_.usuario, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getCarteiraId(), root -> root.join(HistoricoOperacao_.carteira, JoinType.LEFT).get(Carteira_.id)
                )
            );
        }
        return specification;
    }
}
