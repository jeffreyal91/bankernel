package com.bankernel.service;

import com.bankernel.domain.*; // for static metamodels
import com.bankernel.domain.Deposito;
import com.bankernel.repository.DepositoRepository;
import com.bankernel.service.criteria.DepositoCriteria;
import com.bankernel.service.dto.DepositoDTO;
import com.bankernel.service.mapper.DepositoMapper;
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
 * Service for executing complex queries for {@link Deposito} entities in the database.
 * The main input is a {@link DepositoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DepositoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepositoQueryService extends QueryService<Deposito> {

    private static final Logger LOG = LoggerFactory.getLogger(DepositoQueryService.class);

    private final DepositoRepository depositoRepository;

    private final DepositoMapper depositoMapper;

    public DepositoQueryService(DepositoRepository depositoRepository, DepositoMapper depositoMapper) {
        this.depositoRepository = depositoRepository;
        this.depositoMapper = depositoMapper;
    }

    /**
     * Return a {@link Page} of {@link DepositoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepositoDTO> findByCriteria(DepositoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Deposito> specification = createSpecification(criteria);
        return depositoRepository.findAll(specification, page).map(depositoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepositoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Deposito> specification = createSpecification(criteria);
        return depositoRepository.count(specification);
    }

    /**
     * Function to convert {@link DepositoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Deposito> createSpecification(DepositoCriteria criteria) {
        Specification<Deposito> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Deposito_.id),
                buildRangeSpecification(criteria.getValor(), Deposito_.valor),
                buildRangeSpecification(criteria.getValorCreditado(), Deposito_.valorCreditado),
                buildRangeSpecification(criteria.getValorSaldoCarteira(), Deposito_.valorSaldoCarteira),
                buildSpecification(criteria.getTipoDeposito(), Deposito_.tipoDeposito),
                buildSpecification(criteria.getSituacaoDeposito(), Deposito_.situacaoDeposito),
                buildStringSpecification(criteria.getNumeroReferencia(), Deposito_.numeroReferencia),
                buildStringSpecification(criteria.getReferenciaExterna(), Deposito_.referenciaExterna),
                buildStringSpecification(criteria.getDescricao(), Deposito_.descricao),
                buildStringSpecification(criteria.getMotivoRejeicao(), Deposito_.motivoRejeicao),
                buildSpecification(criteria.getContabilizado(), Deposito_.contabilizado),
                buildStringSpecification(criteria.getNomeUsuarioFixo(), Deposito_.nomeUsuarioFixo),
                buildRangeSpecification(criteria.getNumeroParcela(), Deposito_.numeroParcela),
                buildSpecification(criteria.getTransacaoId(), root -> root.join(Deposito_.transacao, JoinType.LEFT).get(Transacao_.id)),
                buildSpecification(criteria.getCarteiraId(), root -> root.join(Deposito_.carteira, JoinType.LEFT).get(Carteira_.id)),
                buildSpecification(criteria.getMoedaCarteiraId(), root ->
                    root.join(Deposito_.moedaCarteira, JoinType.LEFT).get(MoedaCarteira_.id)
                ),
                buildSpecification(criteria.getUsuarioId(), root -> root.join(Deposito_.usuario, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getContaBancariaId(), root ->
                    root.join(Deposito_.contaBancaria, JoinType.LEFT).get(ContaBancaria_.id)
                ),
                buildSpecification(criteria.getDepositoPixId(), root -> root.join(Deposito_.depositoPix, JoinType.LEFT).get(DepositoPix_.id)
                ),
                buildSpecification(criteria.getDepositoBoletoId(), root ->
                    root.join(Deposito_.depositoBoleto, JoinType.LEFT).get(DepositoBoleto_.id)
                )
            );
        }
        return specification;
    }
}
