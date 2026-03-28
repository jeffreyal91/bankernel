package com.bankernel.service;

import com.bankernel.domain.*; // for static metamodels
import com.bankernel.domain.Cobranca;
import com.bankernel.repository.CobrancaRepository;
import com.bankernel.service.criteria.CobrancaCriteria;
import com.bankernel.service.dto.CobrancaDTO;
import com.bankernel.service.mapper.CobrancaMapper;
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
 * Service for executing complex queries for {@link Cobranca} entities in the database.
 * The main input is a {@link CobrancaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link CobrancaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CobrancaQueryService extends QueryService<Cobranca> {

    private static final Logger LOG = LoggerFactory.getLogger(CobrancaQueryService.class);

    private final CobrancaRepository cobrancaRepository;

    private final CobrancaMapper cobrancaMapper;

    public CobrancaQueryService(CobrancaRepository cobrancaRepository, CobrancaMapper cobrancaMapper) {
        this.cobrancaRepository = cobrancaRepository;
        this.cobrancaMapper = cobrancaMapper;
    }

    /**
     * Return a {@link Page} of {@link CobrancaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CobrancaDTO> findByCriteria(CobrancaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Cobranca> specification = createSpecification(criteria);
        return cobrancaRepository.findAll(specification, page).map(cobrancaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CobrancaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Cobranca> specification = createSpecification(criteria);
        return cobrancaRepository.count(specification);
    }

    /**
     * Function to convert {@link CobrancaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Cobranca> createSpecification(CobrancaCriteria criteria) {
        Specification<Cobranca> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Cobranca_.id),
                buildRangeSpecification(criteria.getValor(), Cobranca_.valor),
                buildRangeSpecification(criteria.getValorCreditado(), Cobranca_.valorCreditado),
                buildRangeSpecification(criteria.getValorCreditadoCarteira(), Cobranca_.valorCreditadoCarteira),
                buildStringSpecification(criteria.getIdProdutoExterno(), Cobranca_.idProdutoExterno),
                buildStringSpecification(criteria.getNomeProdutoExterno(), Cobranca_.nomeProdutoExterno),
                buildSpecification(criteria.getSituacao(), Cobranca_.situacao),
                buildSpecification(criteria.getTipo(), Cobranca_.tipo),
                buildRangeSpecification(criteria.getDescontoGeral(), Cobranca_.descontoGeral),
                buildSpecification(criteria.getTipoDesconto(), Cobranca_.tipoDesconto),
                buildSpecification(criteria.getContabilizado(), Cobranca_.contabilizado),
                buildStringSpecification(criteria.getNomeUsuarioFixo(), Cobranca_.nomeUsuarioFixo),
                buildStringSpecification(criteria.getChaveCobranca(), Cobranca_.chaveCobranca),
                buildStringSpecification(criteria.getIdentificadorExterno(), Cobranca_.identificadorExterno),
                buildSpecification(criteria.getRetornoNotificado(), Cobranca_.retornoNotificado),
                buildSpecification(criteria.getTransacaoId(), root -> root.join(Cobranca_.transacao, JoinType.LEFT).get(Transacao_.id)),
                buildSpecification(criteria.getUsuarioId(), root -> root.join(Cobranca_.usuario, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getCarteiraId(), root -> root.join(Cobranca_.carteira, JoinType.LEFT).get(Carteira_.id)),
                buildSpecification(criteria.getCarteiraCreditadaId(), root ->
                    root.join(Cobranca_.carteiraCreditada, JoinType.LEFT).get(Carteira_.id)
                ),
                buildSpecification(criteria.getMoedaCarteiraId(), root ->
                    root.join(Cobranca_.moedaCarteira, JoinType.LEFT).get(MoedaCarteira_.id)
                ),
                buildSpecification(criteria.getLinkCobrancaId(), root ->
                    root.join(Cobranca_.linkCobranca, JoinType.LEFT).get(LinkCobranca_.id)
                )
            );
        }
        return specification;
    }
}
