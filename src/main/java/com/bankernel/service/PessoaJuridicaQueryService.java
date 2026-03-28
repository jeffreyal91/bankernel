package com.bankernel.service;

import com.bankernel.domain.*; // for static metamodels
import com.bankernel.domain.PessoaJuridica;
import com.bankernel.repository.PessoaJuridicaRepository;
import com.bankernel.service.criteria.PessoaJuridicaCriteria;
import com.bankernel.service.dto.PessoaJuridicaDTO;
import com.bankernel.service.mapper.PessoaJuridicaMapper;
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
 * Service for executing complex queries for {@link PessoaJuridica} entities in the database.
 * The main input is a {@link PessoaJuridicaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PessoaJuridicaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PessoaJuridicaQueryService extends QueryService<PessoaJuridica> {

    private static final Logger LOG = LoggerFactory.getLogger(PessoaJuridicaQueryService.class);

    private final PessoaJuridicaRepository pessoaJuridicaRepository;

    private final PessoaJuridicaMapper pessoaJuridicaMapper;

    public PessoaJuridicaQueryService(PessoaJuridicaRepository pessoaJuridicaRepository, PessoaJuridicaMapper pessoaJuridicaMapper) {
        this.pessoaJuridicaRepository = pessoaJuridicaRepository;
        this.pessoaJuridicaMapper = pessoaJuridicaMapper;
    }

    /**
     * Return a {@link Page} of {@link PessoaJuridicaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PessoaJuridicaDTO> findByCriteria(PessoaJuridicaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PessoaJuridica> specification = createSpecification(criteria);
        return pessoaJuridicaRepository.findAll(specification, page).map(pessoaJuridicaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PessoaJuridicaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PessoaJuridica> specification = createSpecification(criteria);
        return pessoaJuridicaRepository.count(specification);
    }

    /**
     * Function to convert {@link PessoaJuridicaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PessoaJuridica> createSpecification(PessoaJuridicaCriteria criteria) {
        Specification<PessoaJuridica> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), PessoaJuridica_.id),
                buildStringSpecification(criteria.getCnpj(), PessoaJuridica_.cnpj),
                buildStringSpecification(criteria.getRazaoSocial(), PessoaJuridica_.razaoSocial),
                buildStringSpecification(criteria.getNomeFantasia(), PessoaJuridica_.nomeFantasia),
                buildStringSpecification(criteria.getTelefone(), PessoaJuridica_.telefone),
                buildStringSpecification(criteria.getSitioWeb(), PessoaJuridica_.sitioWeb),
                buildStringSpecification(criteria.getDescricao(), PessoaJuridica_.descricao),
                buildRangeSpecification(criteria.getDataFundacao(), PessoaJuridica_.dataFundacao),
                buildRangeSpecification(criteria.getCapitalSocial(), PessoaJuridica_.capitalSocial),
                buildRangeSpecification(criteria.getFaturamentoAnual(), PessoaJuridica_.faturamentoAnual),
                buildRangeSpecification(criteria.getMediaMovimentacaoMensal(), PessoaJuridica_.mediaMovimentacaoMensal),
                buildSpecification(criteria.getTipoDocumento(), PessoaJuridica_.tipoDocumento),
                buildStringSpecification(criteria.getRegimeTributario(), PessoaJuridica_.regimeTributario),
                buildStringSpecification(criteria.getCodigoNaturezaJuridica(), PessoaJuridica_.codigoNaturezaJuridica),
                buildStringSpecification(criteria.getAtividadePrincipal(), PessoaJuridica_.atividadePrincipal),
                buildSpecification(criteria.getEmpresaAtiva(), PessoaJuridica_.empresaAtiva),
                buildSpecification(criteria.getNivelRisco(), PessoaJuridica_.nivelRisco),
                buildSpecification(criteria.getSituacao(), PessoaJuridica_.situacao),
                buildSpecification(criteria.getBloqueioSaque(), PessoaJuridica_.bloqueioSaque),
                buildStringSpecification(criteria.getCpfRepresentanteLegal(), PessoaJuridica_.cpfRepresentanteLegal),
                buildStringSpecification(criteria.getNumeroRegistro(), PessoaJuridica_.numeroRegistro),
                buildSpecification(criteria.getUsuarioId(), root -> root.join(PessoaJuridica_.usuario, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getMoedaPrincipalId(), root ->
                    root.join(PessoaJuridica_.moedaPrincipal, JoinType.LEFT).get(MoedaCarteira_.id)
                ),
                buildSpecification(criteria.getContratoSocialId(), root ->
                    root.join(PessoaJuridica_.contratoSocial, JoinType.LEFT).get(Documento_.id)
                ),
                buildSpecification(criteria.getNacionalidadeId(), root ->
                    root.join(PessoaJuridica_.nacionalidade, JoinType.LEFT).get(Pais_.id)
                ),
                buildSpecification(criteria.getTipoNegocioId(), root ->
                    root.join(PessoaJuridica_.tipoNegocio, JoinType.LEFT).get(TipoNegocio_.id)
                ),
                buildSpecification(criteria.getPlanoId(), root -> root.join(PessoaJuridica_.plano, JoinType.LEFT).get(Plano_.id)),
                buildSpecification(criteria.getEscritorioId(), root ->
                    root.join(PessoaJuridica_.escritorio, JoinType.LEFT).get(Escritorio_.id)
                )
            );
        }
        return specification;
    }
}
