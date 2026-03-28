package com.bankernel.service;

import com.bankernel.domain.Transacao;
import com.bankernel.repository.TransacaoRepository;
import com.bankernel.service.dto.TransacaoDTO;
import com.bankernel.service.mapper.TransacaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.Transacao}.
 */
@Service
@Transactional
public class TransacaoService {

    private static final Logger LOG = LoggerFactory.getLogger(TransacaoService.class);

    private final TransacaoRepository transacaoRepository;

    private final TransacaoMapper transacaoMapper;

    public TransacaoService(TransacaoRepository transacaoRepository, TransacaoMapper transacaoMapper) {
        this.transacaoRepository = transacaoRepository;
        this.transacaoMapper = transacaoMapper;
    }

    /**
     * Save a transacao.
     *
     * @param transacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public TransacaoDTO save(TransacaoDTO transacaoDTO) {
        LOG.debug("Request to save Transacao : {}", transacaoDTO);
        Transacao transacao = transacaoMapper.toEntity(transacaoDTO);
        transacao = transacaoRepository.save(transacao);
        return transacaoMapper.toDto(transacao);
    }

    /**
     * Update a transacao.
     *
     * @param transacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public TransacaoDTO update(TransacaoDTO transacaoDTO) {
        LOG.debug("Request to update Transacao : {}", transacaoDTO);
        Transacao transacao = transacaoMapper.toEntity(transacaoDTO);
        transacao = transacaoRepository.save(transacao);
        return transacaoMapper.toDto(transacao);
    }

    /**
     * Partially update a transacao.
     *
     * @param transacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransacaoDTO> partialUpdate(TransacaoDTO transacaoDTO) {
        LOG.debug("Request to partially update Transacao : {}", transacaoDTO);

        return transacaoRepository
            .findById(transacaoDTO.getId())
            .map(existingTransacao -> {
                transacaoMapper.partialUpdate(existingTransacao, transacaoDTO);

                return existingTransacao;
            })
            .map(transacaoRepository::save)
            .map(transacaoMapper::toDto);
    }

    /**
     * Get all the transacaos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TransacaoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return transacaoRepository.findAllWithEagerRelationships(pageable).map(transacaoMapper::toDto);
    }

    /**
     * Get one transacao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransacaoDTO> findOne(Long id) {
        LOG.debug("Request to get Transacao : {}", id);
        return transacaoRepository.findOneWithEagerRelationships(id).map(transacaoMapper::toDto);
    }

    /**
     * Delete the transacao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Transacao : {}", id);
        transacaoRepository.deleteById(id);
    }
}
