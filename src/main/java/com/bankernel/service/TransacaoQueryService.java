package com.bankernel.service;

import com.bankernel.domain.*; // for static metamodels
import com.bankernel.domain.Transacao;
import com.bankernel.repository.TransacaoRepository;
import com.bankernel.service.criteria.TransacaoCriteria;
import com.bankernel.service.dto.TransacaoDTO;
import com.bankernel.service.mapper.TransacaoMapper;
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
 * Service for executing complex queries for {@link Transacao} entities in the database.
 * The main input is a {@link TransacaoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TransacaoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransacaoQueryService extends QueryService<Transacao> {

    private static final Logger LOG = LoggerFactory.getLogger(TransacaoQueryService.class);

    private final TransacaoRepository transacaoRepository;

    private final TransacaoMapper transacaoMapper;

    public TransacaoQueryService(TransacaoRepository transacaoRepository, TransacaoMapper transacaoMapper) {
        this.transacaoRepository = transacaoRepository;
        this.transacaoMapper = transacaoMapper;
    }

    /**
     * Return a {@link Page} of {@link TransacaoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransacaoDTO> findByCriteria(TransacaoCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Transacao> specification = createSpecification(criteria);
        return transacaoRepository.findAll(specification, page).map(transacaoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransacaoCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Transacao> specification = createSpecification(criteria);
        return transacaoRepository.count(specification);
    }

    /**
     * Function to convert {@link TransacaoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Transacao> createSpecification(TransacaoCriteria criteria) {
        Specification<Transacao> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Transacao_.id),
                buildRangeSpecification(criteria.getValorEnviado(), Transacao_.valorEnviado),
                buildRangeSpecification(criteria.getValorRecebido(), Transacao_.valorRecebido),
                buildStringSpecification(criteria.getDescricao(), Transacao_.descricao),
                buildSpecification(criteria.getEstornada(), Transacao_.estornada),
                buildSpecification(criteria.getTipoTransacao(), Transacao_.tipoTransacao),
                buildSpecification(criteria.getTipoPagamento(), Transacao_.tipoPagamento),
                buildSpecification(criteria.getSituacao(), Transacao_.situacao),
                buildSpecification(criteria.getAtiva(), Transacao_.ativa),
                buildStringSpecification(criteria.getTipoEntidadeOrigem(), Transacao_.tipoEntidadeOrigem),
                buildRangeSpecification(criteria.getIdEntidadeOrigem(), Transacao_.idEntidadeOrigem),
                buildSpecification(criteria.getCarteiraOrigemId(), root ->
                    root.join(Transacao_.carteiraOrigem, JoinType.LEFT).get(Carteira_.id)
                ),
                buildSpecification(criteria.getCarteiraDestinoId(), root ->
                    root.join(Transacao_.carteiraDestino, JoinType.LEFT).get(Carteira_.id)
                ),
                buildSpecification(criteria.getMoedaOrigemId(), root ->
                    root.join(Transacao_.moedaOrigem, JoinType.LEFT).get(MoedaCarteira_.id)
                ),
                buildSpecification(criteria.getMoedaDestinoId(), root ->
                    root.join(Transacao_.moedaDestino, JoinType.LEFT).get(MoedaCarteira_.id)
                )
            );
        }
        return specification;
    }
}
