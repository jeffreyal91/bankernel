package com.bankernel.service;

import com.bankernel.domain.*; // for static metamodels
import com.bankernel.domain.Carteira;
import com.bankernel.repository.CarteiraRepository;
import com.bankernel.service.criteria.CarteiraCriteria;
import com.bankernel.service.dto.CarteiraDTO;
import com.bankernel.service.mapper.CarteiraMapper;
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
 * Service for executing complex queries for {@link Carteira} entities in the database.
 * The main input is a {@link CarteiraCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CarteiraDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CarteiraQueryService extends QueryService<Carteira> {

    private static final Logger LOG = LoggerFactory.getLogger(CarteiraQueryService.class);

    private final CarteiraRepository carteiraRepository;

    private final CarteiraMapper carteiraMapper;

    public CarteiraQueryService(CarteiraRepository carteiraRepository, CarteiraMapper carteiraMapper) {
        this.carteiraRepository = carteiraRepository;
        this.carteiraMapper = carteiraMapper;
    }

    /**
     * Return a {@link Page} of {@link CarteiraDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CarteiraDTO> findByCriteria(CarteiraCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Carteira> specification = createSpecification(criteria);
        return carteiraRepository.findAll(specification, page).map(carteiraMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CarteiraCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Carteira> specification = createSpecification(criteria);
        return carteiraRepository.count(specification);
    }

    /**
     * Function to convert {@link CarteiraCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Carteira> createSpecification(CarteiraCriteria criteria) {
        Specification<Carteira> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Carteira_.id),
                buildRangeSpecification(criteria.getSaldo(), Carteira_.saldo),
                buildRangeSpecification(criteria.getLimiteNegativo(), Carteira_.limiteNegativo),
                buildRangeSpecification(criteria.getValorCongelado(), Carteira_.valorCongelado),
                buildStringSpecification(criteria.getNumeroConta(), Carteira_.numeroConta),
                buildSpecification(criteria.getAtiva(), Carteira_.ativa),
                buildSpecification(criteria.getMoedaCarteiraId(), root ->
                    root.join(Carteira_.moedaCarteira, JoinType.LEFT).get(MoedaCarteira_.id)
                ),
                buildSpecification(criteria.getUsuarioId(), root -> root.join(Carteira_.usuario, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
