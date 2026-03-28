package com.bankernel.service;

import com.bankernel.domain.*; // for static metamodels
import com.bankernel.domain.PessoaFisica;
import com.bankernel.repository.PessoaFisicaRepository;
import com.bankernel.service.criteria.PessoaFisicaCriteria;
import com.bankernel.service.dto.PessoaFisicaDTO;
import com.bankernel.service.mapper.PessoaFisicaMapper;
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
 * Service for executing complex queries for {@link PessoaFisica} entities in the database.
 * The main input is a {@link PessoaFisicaCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PessoaFisicaDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PessoaFisicaQueryService extends QueryService<PessoaFisica> {

    private static final Logger LOG = LoggerFactory.getLogger(PessoaFisicaQueryService.class);

    private final PessoaFisicaRepository pessoaFisicaRepository;

    private final PessoaFisicaMapper pessoaFisicaMapper;

    public PessoaFisicaQueryService(PessoaFisicaRepository pessoaFisicaRepository, PessoaFisicaMapper pessoaFisicaMapper) {
        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.pessoaFisicaMapper = pessoaFisicaMapper;
    }

    /**
     * Return a {@link Page} of {@link PessoaFisicaDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PessoaFisicaDTO> findByCriteria(PessoaFisicaCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PessoaFisica> specification = createSpecification(criteria);
        return pessoaFisicaRepository.findAll(specification, page).map(pessoaFisicaMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PessoaFisicaCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PessoaFisica> specification = createSpecification(criteria);
        return pessoaFisicaRepository.count(specification);
    }

    /**
     * Function to convert {@link PessoaFisicaCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PessoaFisica> createSpecification(PessoaFisicaCriteria criteria) {
        Specification<PessoaFisica> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), PessoaFisica_.id),
                buildStringSpecification(criteria.getCpf(), PessoaFisica_.cpf),
                buildStringSpecification(criteria.getNomeCompleto(), PessoaFisica_.nomeCompleto),
                buildStringSpecification(criteria.getNomeSocial(), PessoaFisica_.nomeSocial),
                buildRangeSpecification(criteria.getDataNascimento(), PessoaFisica_.dataNascimento),
                buildSpecification(criteria.getGenero(), PessoaFisica_.genero),
                buildStringSpecification(criteria.getNomeMae(), PessoaFisica_.nomeMae),
                buildStringSpecification(criteria.getTelefone(), PessoaFisica_.telefone),
                buildSpecification(criteria.getTelefoneVerificado(), PessoaFisica_.telefoneVerificado),
                buildSpecification(criteria.getNivelRisco(), PessoaFisica_.nivelRisco),
                buildSpecification(criteria.getSituacao(), PessoaFisica_.situacao),
                buildSpecification(criteria.getBloqueioSaque(), PessoaFisica_.bloqueioSaque),
                buildSpecification(criteria.getUsuarioId(), root -> root.join(PessoaFisica_.usuario, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getMoedaPrincipalId(), root ->
                    root.join(PessoaFisica_.moedaPrincipal, JoinType.LEFT).get(MoedaCarteira_.id)
                ),
                buildSpecification(criteria.getNacionalidadeId(), root ->
                    root.join(PessoaFisica_.nacionalidade, JoinType.LEFT).get(Pais_.id)
                ),
                buildSpecification(criteria.getProfissaoId(), root -> root.join(PessoaFisica_.profissao, JoinType.LEFT).get(Profissao_.id)),
                buildSpecification(criteria.getPlanoId(), root -> root.join(PessoaFisica_.plano, JoinType.LEFT).get(Plano_.id)),
                buildSpecification(criteria.getEscritorioId(), root ->
                    root.join(PessoaFisica_.escritorio, JoinType.LEFT).get(Escritorio_.id)
                )
            );
        }
        return specification;
    }
}
