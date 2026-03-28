package com.bankernel.service;

import com.bankernel.domain.*; // for static metamodels
import com.bankernel.domain.Transferencia;
import com.bankernel.repository.TransferenciaRepository;
import com.bankernel.service.criteria.TransferenciaCriteria;
import com.bankernel.service.dto.TransferenciaDTO;
import com.bankernel.service.mapper.TransferenciaMapper;
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
 * Service for executing complex queries for {@link Transferencia} entities in the database.
 * The main input is a {@link TransferenciaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TransferenciaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransferenciaQueryService extends QueryService<Transferencia> {

    private static final Logger LOG = LoggerFactory.getLogger(TransferenciaQueryService.class);

    private final TransferenciaRepository transferenciaRepository;

    private final TransferenciaMapper transferenciaMapper;

    public TransferenciaQueryService(TransferenciaRepository transferenciaRepository, TransferenciaMapper transferenciaMapper) {
        this.transferenciaRepository = transferenciaRepository;
        this.transferenciaMapper = transferenciaMapper;
    }

    /**
     * Return a {@link Page} of {@link TransferenciaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransferenciaDTO> findByCriteria(TransferenciaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Transferencia> specification = createSpecification(criteria);
        return transferenciaRepository.findAll(specification, page).map(transferenciaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransferenciaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Transferencia> specification = createSpecification(criteria);
        return transferenciaRepository.count(specification);
    }

    /**
     * Function to convert {@link TransferenciaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Transferencia> createSpecification(TransferenciaCriteria criteria) {
        Specification<Transferencia> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Transferencia_.id),
                buildRangeSpecification(criteria.getValor(), Transferencia_.valor),
                buildStringSpecification(criteria.getChaveInterna(), Transferencia_.chaveInterna),
                buildSpecification(criteria.getTipoChave(), Transferencia_.tipoChave),
                buildSpecification(criteria.getSituacao(), Transferencia_.situacao),
                buildStringSpecification(criteria.getDescricao(), Transferencia_.descricao),
                buildStringSpecification(criteria.getMotivoRejeicao(), Transferencia_.motivoRejeicao),
                buildStringSpecification(criteria.getNumeroReferencia(), Transferencia_.numeroReferencia),
                buildSpecification(criteria.getTransacaoId(), root -> root.join(Transferencia_.transacao, JoinType.LEFT).get(Transacao_.id)
                ),
                buildSpecification(criteria.getUsuarioOrigemId(), root ->
                    root.join(Transferencia_.usuarioOrigem, JoinType.LEFT).get(User_.id)
                ),
                buildSpecification(criteria.getUsuarioDestinoId(), root ->
                    root.join(Transferencia_.usuarioDestino, JoinType.LEFT).get(User_.id)
                ),
                buildSpecification(criteria.getCarteiraOrigemId(), root ->
                    root.join(Transferencia_.carteiraOrigem, JoinType.LEFT).get(Carteira_.id)
                ),
                buildSpecification(criteria.getCarteiraDestinoId(), root ->
                    root.join(Transferencia_.carteiraDestino, JoinType.LEFT).get(Carteira_.id)
                ),
                buildSpecification(criteria.getMoedaCarteiraId(), root ->
                    root.join(Transferencia_.moedaCarteira, JoinType.LEFT).get(MoedaCarteira_.id)
                )
            );
        }
        return specification;
    }
}
