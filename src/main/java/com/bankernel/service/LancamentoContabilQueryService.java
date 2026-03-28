package com.bankernel.service;

import com.bankernel.domain.*; // for static metamodels
import com.bankernel.domain.LancamentoContabil;
import com.bankernel.repository.LancamentoContabilRepository;
import com.bankernel.service.criteria.LancamentoContabilCriteria;
import com.bankernel.service.dto.LancamentoContabilDTO;
import com.bankernel.service.mapper.LancamentoContabilMapper;
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
 * Service for executing complex queries for {@link LancamentoContabil} entities in the database.
 * The main input is a {@link LancamentoContabilCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link LancamentoContabilDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LancamentoContabilQueryService extends QueryService<LancamentoContabil> {

    private static final Logger LOG = LoggerFactory.getLogger(LancamentoContabilQueryService.class);

    private final LancamentoContabilRepository lancamentoContabilRepository;

    private final LancamentoContabilMapper lancamentoContabilMapper;

    public LancamentoContabilQueryService(
        LancamentoContabilRepository lancamentoContabilRepository,
        LancamentoContabilMapper lancamentoContabilMapper
    ) {
        this.lancamentoContabilRepository = lancamentoContabilRepository;
        this.lancamentoContabilMapper = lancamentoContabilMapper;
    }

    /**
     * Return a {@link Page} of {@link LancamentoContabilDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LancamentoContabilDTO> findByCriteria(LancamentoContabilCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LancamentoContabil> specification = createSpecification(criteria);
        return lancamentoContabilRepository.findAll(specification, page).map(lancamentoContabilMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LancamentoContabilCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<LancamentoContabil> specification = createSpecification(criteria);
        return lancamentoContabilRepository.count(specification);
    }

    /**
     * Function to convert {@link LancamentoContabilCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LancamentoContabil> createSpecification(LancamentoContabilCriteria criteria) {
        Specification<LancamentoContabil> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), LancamentoContabil_.id),
                buildRangeSpecification(criteria.getValor(), LancamentoContabil_.valor),
                buildSpecification(criteria.getTipoLancamento(), LancamentoContabil_.tipoLancamento),
                buildSpecification(criteria.getSinalLancamento(), LancamentoContabil_.sinalLancamento),
                buildSpecification(criteria.getAtivo(), LancamentoContabil_.ativo),
                buildSpecification(criteria.getTransacaoId(), root ->
                    root.join(LancamentoContabil_.transacao, JoinType.LEFT).get(Transacao_.id)
                ),
                buildSpecification(criteria.getContaContabilId(), root ->
                    root.join(LancamentoContabil_.contaContabil, JoinType.LEFT).get(ContaContabil_.id)
                )
            );
        }
        return specification;
    }
}
