package com.bankernel.service;

import com.bankernel.domain.*; // for static metamodels
import com.bankernel.domain.ContaContabil;
import com.bankernel.repository.ContaContabilRepository;
import com.bankernel.service.criteria.ContaContabilCriteria;
import com.bankernel.service.dto.ContaContabilDTO;
import com.bankernel.service.mapper.ContaContabilMapper;
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
 * Service for executing complex queries for {@link ContaContabil} entities in the database.
 * The main input is a {@link ContaContabilCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ContaContabilDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContaContabilQueryService extends QueryService<ContaContabil> {

    private static final Logger LOG = LoggerFactory.getLogger(ContaContabilQueryService.class);

    private final ContaContabilRepository contaContabilRepository;

    private final ContaContabilMapper contaContabilMapper;

    public ContaContabilQueryService(ContaContabilRepository contaContabilRepository, ContaContabilMapper contaContabilMapper) {
        this.contaContabilRepository = contaContabilRepository;
        this.contaContabilMapper = contaContabilMapper;
    }

    /**
     * Return a {@link Page} of {@link ContaContabilDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContaContabilDTO> findByCriteria(ContaContabilCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContaContabil> specification = createSpecification(criteria);
        return contaContabilRepository.findAll(specification, page).map(contaContabilMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContaContabilCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ContaContabil> specification = createSpecification(criteria);
        return contaContabilRepository.count(specification);
    }

    /**
     * Function to convert {@link ContaContabilCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContaContabil> createSpecification(ContaContabilCriteria criteria) {
        Specification<ContaContabil> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ContaContabil_.id),
                buildStringSpecification(criteria.getCodigo(), ContaContabil_.codigo),
                buildStringSpecification(criteria.getNome(), ContaContabil_.nome),
                buildRangeSpecification(criteria.getSaldo(), ContaContabil_.saldo),
                buildStringSpecification(criteria.getDescricao(), ContaContabil_.descricao),
                buildSpecification(criteria.getTipoContaContabil(), ContaContabil_.tipoContaContabil),
                buildSpecification(criteria.getCategoriaContaContabil(), ContaContabil_.categoriaContaContabil),
                buildSpecification(criteria.getAtiva(), ContaContabil_.ativa),
                buildSpecification(criteria.getMoedaCarteiraId(), root ->
                    root.join(ContaContabil_.moedaCarteira, JoinType.LEFT).get(MoedaCarteira_.id)
                )
            );
        }
        return specification;
    }
}
