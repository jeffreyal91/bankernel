package com.bankernel.service;

import com.bankernel.domain.BloqueioOperacao;
import com.bankernel.repository.BloqueioOperacaoRepository;
import com.bankernel.service.dto.BloqueioOperacaoDTO;
import com.bankernel.service.mapper.BloqueioOperacaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.BloqueioOperacao}.
 */
@Service
@Transactional
public class BloqueioOperacaoService {

    private static final Logger LOG = LoggerFactory.getLogger(BloqueioOperacaoService.class);

    private final BloqueioOperacaoRepository bloqueioOperacaoRepository;

    private final BloqueioOperacaoMapper bloqueioOperacaoMapper;

    public BloqueioOperacaoService(BloqueioOperacaoRepository bloqueioOperacaoRepository, BloqueioOperacaoMapper bloqueioOperacaoMapper) {
        this.bloqueioOperacaoRepository = bloqueioOperacaoRepository;
        this.bloqueioOperacaoMapper = bloqueioOperacaoMapper;
    }

    /**
     * Save a bloqueioOperacao.
     *
     * @param bloqueioOperacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public BloqueioOperacaoDTO save(BloqueioOperacaoDTO bloqueioOperacaoDTO) {
        LOG.debug("Request to save BloqueioOperacao : {}", bloqueioOperacaoDTO);
        BloqueioOperacao bloqueioOperacao = bloqueioOperacaoMapper.toEntity(bloqueioOperacaoDTO);
        bloqueioOperacao = bloqueioOperacaoRepository.save(bloqueioOperacao);
        return bloqueioOperacaoMapper.toDto(bloqueioOperacao);
    }

    /**
     * Update a bloqueioOperacao.
     *
     * @param bloqueioOperacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public BloqueioOperacaoDTO update(BloqueioOperacaoDTO bloqueioOperacaoDTO) {
        LOG.debug("Request to update BloqueioOperacao : {}", bloqueioOperacaoDTO);
        BloqueioOperacao bloqueioOperacao = bloqueioOperacaoMapper.toEntity(bloqueioOperacaoDTO);
        bloqueioOperacao = bloqueioOperacaoRepository.save(bloqueioOperacao);
        return bloqueioOperacaoMapper.toDto(bloqueioOperacao);
    }

    /**
     * Partially update a bloqueioOperacao.
     *
     * @param bloqueioOperacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BloqueioOperacaoDTO> partialUpdate(BloqueioOperacaoDTO bloqueioOperacaoDTO) {
        LOG.debug("Request to partially update BloqueioOperacao : {}", bloqueioOperacaoDTO);

        return bloqueioOperacaoRepository
            .findById(bloqueioOperacaoDTO.getId())
            .map(existingBloqueioOperacao -> {
                bloqueioOperacaoMapper.partialUpdate(existingBloqueioOperacao, bloqueioOperacaoDTO);

                return existingBloqueioOperacao;
            })
            .map(bloqueioOperacaoRepository::save)
            .map(bloqueioOperacaoMapper::toDto);
    }

    /**
     * Get all the bloqueioOperacaos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BloqueioOperacaoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BloqueioOperacaos");
        return bloqueioOperacaoRepository.findAll(pageable).map(bloqueioOperacaoMapper::toDto);
    }

    /**
     * Get one bloqueioOperacao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BloqueioOperacaoDTO> findOne(Long id) {
        LOG.debug("Request to get BloqueioOperacao : {}", id);
        return bloqueioOperacaoRepository.findById(id).map(bloqueioOperacaoMapper::toDto);
    }

    /**
     * Delete the bloqueioOperacao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BloqueioOperacao : {}", id);
        bloqueioOperacaoRepository.deleteById(id);
    }
}
