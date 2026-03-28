package com.bankernel.service;

import com.bankernel.domain.TipoOperacao;
import com.bankernel.repository.TipoOperacaoRepository;
import com.bankernel.service.dto.TipoOperacaoDTO;
import com.bankernel.service.mapper.TipoOperacaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.TipoOperacao}.
 */
@Service
@Transactional
public class TipoOperacaoService {

    private static final Logger LOG = LoggerFactory.getLogger(TipoOperacaoService.class);

    private final TipoOperacaoRepository tipoOperacaoRepository;

    private final TipoOperacaoMapper tipoOperacaoMapper;

    public TipoOperacaoService(TipoOperacaoRepository tipoOperacaoRepository, TipoOperacaoMapper tipoOperacaoMapper) {
        this.tipoOperacaoRepository = tipoOperacaoRepository;
        this.tipoOperacaoMapper = tipoOperacaoMapper;
    }

    /**
     * Save a tipoOperacao.
     *
     * @param tipoOperacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoOperacaoDTO save(TipoOperacaoDTO tipoOperacaoDTO) {
        LOG.debug("Request to save TipoOperacao : {}", tipoOperacaoDTO);
        TipoOperacao tipoOperacao = tipoOperacaoMapper.toEntity(tipoOperacaoDTO);
        tipoOperacao = tipoOperacaoRepository.save(tipoOperacao);
        return tipoOperacaoMapper.toDto(tipoOperacao);
    }

    /**
     * Update a tipoOperacao.
     *
     * @param tipoOperacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoOperacaoDTO update(TipoOperacaoDTO tipoOperacaoDTO) {
        LOG.debug("Request to update TipoOperacao : {}", tipoOperacaoDTO);
        TipoOperacao tipoOperacao = tipoOperacaoMapper.toEntity(tipoOperacaoDTO);
        tipoOperacao = tipoOperacaoRepository.save(tipoOperacao);
        return tipoOperacaoMapper.toDto(tipoOperacao);
    }

    /**
     * Partially update a tipoOperacao.
     *
     * @param tipoOperacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TipoOperacaoDTO> partialUpdate(TipoOperacaoDTO tipoOperacaoDTO) {
        LOG.debug("Request to partially update TipoOperacao : {}", tipoOperacaoDTO);

        return tipoOperacaoRepository
            .findById(tipoOperacaoDTO.getId())
            .map(existingTipoOperacao -> {
                tipoOperacaoMapper.partialUpdate(existingTipoOperacao, tipoOperacaoDTO);

                return existingTipoOperacao;
            })
            .map(tipoOperacaoRepository::save)
            .map(tipoOperacaoMapper::toDto);
    }

    /**
     * Get all the tipoOperacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoOperacaoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TipoOperacaos");
        return tipoOperacaoRepository.findAll(pageable).map(tipoOperacaoMapper::toDto);
    }

    /**
     * Get all the tipoOperacaos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TipoOperacaoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tipoOperacaoRepository.findAllWithEagerRelationships(pageable).map(tipoOperacaoMapper::toDto);
    }

    /**
     * Get one tipoOperacao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TipoOperacaoDTO> findOne(Long id) {
        LOG.debug("Request to get TipoOperacao : {}", id);
        return tipoOperacaoRepository.findOneWithEagerRelationships(id).map(tipoOperacaoMapper::toDto);
    }

    /**
     * Delete the tipoOperacao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TipoOperacao : {}", id);
        tipoOperacaoRepository.deleteById(id);
    }
}
