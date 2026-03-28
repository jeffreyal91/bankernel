package com.bankernel.service;

import com.bankernel.domain.HistoricoOperacao;
import com.bankernel.repository.HistoricoOperacaoRepository;
import com.bankernel.service.dto.HistoricoOperacaoDTO;
import com.bankernel.service.mapper.HistoricoOperacaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.HistoricoOperacao}.
 */
@Service
@Transactional
public class HistoricoOperacaoService {

    private static final Logger LOG = LoggerFactory.getLogger(HistoricoOperacaoService.class);

    private final HistoricoOperacaoRepository historicoOperacaoRepository;

    private final HistoricoOperacaoMapper historicoOperacaoMapper;

    public HistoricoOperacaoService(
        HistoricoOperacaoRepository historicoOperacaoRepository,
        HistoricoOperacaoMapper historicoOperacaoMapper
    ) {
        this.historicoOperacaoRepository = historicoOperacaoRepository;
        this.historicoOperacaoMapper = historicoOperacaoMapper;
    }

    /**
     * Save a historicoOperacao.
     *
     * @param historicoOperacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public HistoricoOperacaoDTO save(HistoricoOperacaoDTO historicoOperacaoDTO) {
        LOG.debug("Request to save HistoricoOperacao : {}", historicoOperacaoDTO);
        HistoricoOperacao historicoOperacao = historicoOperacaoMapper.toEntity(historicoOperacaoDTO);
        historicoOperacao = historicoOperacaoRepository.save(historicoOperacao);
        return historicoOperacaoMapper.toDto(historicoOperacao);
    }

    /**
     * Update a historicoOperacao.
     *
     * @param historicoOperacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public HistoricoOperacaoDTO update(HistoricoOperacaoDTO historicoOperacaoDTO) {
        LOG.debug("Request to update HistoricoOperacao : {}", historicoOperacaoDTO);
        HistoricoOperacao historicoOperacao = historicoOperacaoMapper.toEntity(historicoOperacaoDTO);
        historicoOperacao = historicoOperacaoRepository.save(historicoOperacao);
        return historicoOperacaoMapper.toDto(historicoOperacao);
    }

    /**
     * Partially update a historicoOperacao.
     *
     * @param historicoOperacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<HistoricoOperacaoDTO> partialUpdate(HistoricoOperacaoDTO historicoOperacaoDTO) {
        LOG.debug("Request to partially update HistoricoOperacao : {}", historicoOperacaoDTO);

        return historicoOperacaoRepository
            .findById(historicoOperacaoDTO.getId())
            .map(existingHistoricoOperacao -> {
                historicoOperacaoMapper.partialUpdate(existingHistoricoOperacao, historicoOperacaoDTO);

                return existingHistoricoOperacao;
            })
            .map(historicoOperacaoRepository::save)
            .map(historicoOperacaoMapper::toDto);
    }

    /**
     * Get all the historicoOperacaos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<HistoricoOperacaoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return historicoOperacaoRepository.findAllWithEagerRelationships(pageable).map(historicoOperacaoMapper::toDto);
    }

    /**
     * Get one historicoOperacao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HistoricoOperacaoDTO> findOne(Long id) {
        LOG.debug("Request to get HistoricoOperacao : {}", id);
        return historicoOperacaoRepository.findOneWithEagerRelationships(id).map(historicoOperacaoMapper::toDto);
    }

    /**
     * Delete the historicoOperacao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete HistoricoOperacao : {}", id);
        historicoOperacaoRepository.deleteById(id);
    }
}
