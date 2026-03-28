package com.bankernel.service;

import com.bankernel.domain.*; // for static metamodels
import com.bankernel.domain.Saque;
import com.bankernel.repository.SaqueRepository;
import com.bankernel.service.criteria.SaqueCriteria;
import com.bankernel.service.dto.SaqueDTO;
import com.bankernel.service.mapper.SaqueMapper;
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
 * Service for executing complex queries for {@link Saque} entities in the database.
 * The main input is a {@link SaqueCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SaqueDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SaqueQueryService extends QueryService<Saque> {

    private static final Logger LOG = LoggerFactory.getLogger(SaqueQueryService.class);

    private final SaqueRepository saqueRepository;

    private final SaqueMapper saqueMapper;

    public SaqueQueryService(SaqueRepository saqueRepository, SaqueMapper saqueMapper) {
        this.saqueRepository = saqueRepository;
        this.saqueMapper = saqueMapper;
    }

    /**
     * Return a {@link Page} of {@link SaqueDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SaqueDTO> findByCriteria(SaqueCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Saque> specification = createSpecification(criteria);
        return saqueRepository.findAll(specification, page).map(saqueMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SaqueCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Saque> specification = createSpecification(criteria);
        return saqueRepository.count(specification);
    }

    /**
     * Function to convert {@link SaqueCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Saque> createSpecification(SaqueCriteria criteria) {
        Specification<Saque> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Saque_.id),
                buildRangeSpecification(criteria.getValorSaque(), Saque_.valorSaque),
                buildRangeSpecification(criteria.getValorEnviado(), Saque_.valorEnviado),
                buildStringSpecification(criteria.getDescricao(), Saque_.descricao),
                buildSpecification(criteria.getTipoSaque(), Saque_.tipoSaque),
                buildSpecification(criteria.getSituacaoSaque(), Saque_.situacaoSaque),
                buildStringSpecification(criteria.getNumeroReferencia(), Saque_.numeroReferencia),
                buildStringSpecification(criteria.getMotivoRejeicao(), Saque_.motivoRejeicao),
                buildSpecification(criteria.getContabilizado(), Saque_.contabilizado),
                buildStringSpecification(criteria.getNomeUsuarioFixo(), Saque_.nomeUsuarioFixo),
                buildSpecification(criteria.getTransacaoId(), root -> root.join(Saque_.transacao, JoinType.LEFT).get(Transacao_.id)),
                buildSpecification(criteria.getTransacaoEstornoId(), root ->
                    root.join(Saque_.transacaoEstorno, JoinType.LEFT).get(Transacao_.id)
                ),
                buildSpecification(criteria.getCarteiraId(), root -> root.join(Saque_.carteira, JoinType.LEFT).get(Carteira_.id)),
                buildSpecification(criteria.getMoedaCarteiraId(), root ->
                    root.join(Saque_.moedaCarteira, JoinType.LEFT).get(MoedaCarteira_.id)
                ),
                buildSpecification(criteria.getContaBancariaDestinoId(), root ->
                    root.join(Saque_.contaBancariaDestino, JoinType.LEFT).get(ContaBancaria_.id)
                ),
                buildSpecification(criteria.getUsuarioId(), root -> root.join(Saque_.usuario, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getEscritorioId(), root -> root.join(Saque_.escritorio, JoinType.LEFT).get(Escritorio_.id)),
                buildSpecification(criteria.getSaquePixId(), root -> root.join(Saque_.saquePix, JoinType.LEFT).get(SaquePix_.id)),
                buildSpecification(criteria.getSaqueBoletoId(), root -> root.join(Saque_.saqueBoleto, JoinType.LEFT).get(SaqueBoleto_.id))
            );
        }
        return specification;
    }
}
