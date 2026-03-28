package com.bankernel.service;

import com.bankernel.domain.*; // for static metamodels
import com.bankernel.domain.ContaBancaria;
import com.bankernel.repository.ContaBancariaRepository;
import com.bankernel.service.criteria.ContaBancariaCriteria;
import com.bankernel.service.dto.ContaBancariaDTO;
import com.bankernel.service.mapper.ContaBancariaMapper;
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
 * Service for executing complex queries for {@link ContaBancaria} entities in the database.
 * The main input is a {@link ContaBancariaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ContaBancariaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContaBancariaQueryService extends QueryService<ContaBancaria> {

    private static final Logger LOG = LoggerFactory.getLogger(ContaBancariaQueryService.class);

    private final ContaBancariaRepository contaBancariaRepository;

    private final ContaBancariaMapper contaBancariaMapper;

    public ContaBancariaQueryService(ContaBancariaRepository contaBancariaRepository, ContaBancariaMapper contaBancariaMapper) {
        this.contaBancariaRepository = contaBancariaRepository;
        this.contaBancariaMapper = contaBancariaMapper;
    }

    /**
     * Return a {@link Page} of {@link ContaBancariaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContaBancariaDTO> findByCriteria(ContaBancariaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContaBancaria> specification = createSpecification(criteria);
        return contaBancariaRepository.findAll(specification, page).map(contaBancariaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContaBancariaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ContaBancaria> specification = createSpecification(criteria);
        return contaBancariaRepository.count(specification);
    }

    /**
     * Function to convert {@link ContaBancariaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContaBancaria> createSpecification(ContaBancariaCriteria criteria) {
        Specification<ContaBancaria> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ContaBancaria_.id),
                buildStringSpecification(criteria.getNomeTitular(), ContaBancaria_.nomeTitular),
                buildStringSpecification(criteria.getNumeroConta(), ContaBancaria_.numeroConta),
                buildStringSpecification(criteria.getAgencia(), ContaBancaria_.agencia),
                buildStringSpecification(criteria.getNomeBanco(), ContaBancaria_.nomeBanco),
                buildStringSpecification(criteria.getCodigoBanco(), ContaBancaria_.codigoBanco),
                buildStringSpecification(criteria.getIspb(), ContaBancaria_.ispb),
                buildStringSpecification(criteria.getCodigoSwift(), ContaBancaria_.codigoSwift),
                buildSpecification(criteria.getTipoConta(), ContaBancaria_.tipoConta),
                buildSpecification(criteria.getAtiva(), ContaBancaria_.ativa),
                buildSpecification(criteria.getUsuarioId(), root -> root.join(ContaBancaria_.usuario, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getPaisId(), root -> root.join(ContaBancaria_.pais, JoinType.LEFT).get(Pais_.id)),
                buildSpecification(criteria.getMoedaId(), root -> root.join(ContaBancaria_.moeda, JoinType.LEFT).get(Moeda_.id))
            );
        }
        return specification;
    }
}
