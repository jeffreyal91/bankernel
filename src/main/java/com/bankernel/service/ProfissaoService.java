package com.bankernel.service;

import com.bankernel.domain.Profissao;
import com.bankernel.repository.ProfissaoRepository;
import com.bankernel.service.dto.ProfissaoDTO;
import com.bankernel.service.mapper.ProfissaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.Profissao}.
 */
@Service
@Transactional
public class ProfissaoService {

    private static final Logger LOG = LoggerFactory.getLogger(ProfissaoService.class);

    private final ProfissaoRepository profissaoRepository;

    private final ProfissaoMapper profissaoMapper;

    public ProfissaoService(ProfissaoRepository profissaoRepository, ProfissaoMapper profissaoMapper) {
        this.profissaoRepository = profissaoRepository;
        this.profissaoMapper = profissaoMapper;
    }

    /**
     * Save a profissao.
     *
     * @param profissaoDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfissaoDTO save(ProfissaoDTO profissaoDTO) {
        LOG.debug("Request to save Profissao : {}", profissaoDTO);
        Profissao profissao = profissaoMapper.toEntity(profissaoDTO);
        profissao = profissaoRepository.save(profissao);
        return profissaoMapper.toDto(profissao);
    }

    /**
     * Update a profissao.
     *
     * @param profissaoDTO the entity to save.
     * @return the persisted entity.
     */
    public ProfissaoDTO update(ProfissaoDTO profissaoDTO) {
        LOG.debug("Request to update Profissao : {}", profissaoDTO);
        Profissao profissao = profissaoMapper.toEntity(profissaoDTO);
        profissao = profissaoRepository.save(profissao);
        return profissaoMapper.toDto(profissao);
    }

    /**
     * Partially update a profissao.
     *
     * @param profissaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProfissaoDTO> partialUpdate(ProfissaoDTO profissaoDTO) {
        LOG.debug("Request to partially update Profissao : {}", profissaoDTO);

        return profissaoRepository
            .findById(profissaoDTO.getId())
            .map(existingProfissao -> {
                profissaoMapper.partialUpdate(existingProfissao, profissaoDTO);

                return existingProfissao;
            })
            .map(profissaoRepository::save)
            .map(profissaoMapper::toDto);
    }

    /**
     * Get all the profissaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProfissaoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Profissaos");
        return profissaoRepository.findAll(pageable).map(profissaoMapper::toDto);
    }

    /**
     * Get one profissao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProfissaoDTO> findOne(Long id) {
        LOG.debug("Request to get Profissao : {}", id);
        return profissaoRepository.findById(id).map(profissaoMapper::toDto);
    }

    /**
     * Delete the profissao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Profissao : {}", id);
        profissaoRepository.deleteById(id);
    }
}
