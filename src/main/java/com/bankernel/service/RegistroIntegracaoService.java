package com.bankernel.service;

import com.bankernel.domain.RegistroIntegracao;
import com.bankernel.repository.RegistroIntegracaoRepository;
import com.bankernel.service.dto.RegistroIntegracaoDTO;
import com.bankernel.service.mapper.RegistroIntegracaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.RegistroIntegracao}.
 */
@Service
@Transactional
public class RegistroIntegracaoService {

    private static final Logger LOG = LoggerFactory.getLogger(RegistroIntegracaoService.class);

    private final RegistroIntegracaoRepository registroIntegracaoRepository;

    private final RegistroIntegracaoMapper registroIntegracaoMapper;

    public RegistroIntegracaoService(
        RegistroIntegracaoRepository registroIntegracaoRepository,
        RegistroIntegracaoMapper registroIntegracaoMapper
    ) {
        this.registroIntegracaoRepository = registroIntegracaoRepository;
        this.registroIntegracaoMapper = registroIntegracaoMapper;
    }

    /**
     * Save a registroIntegracao.
     *
     * @param registroIntegracaoDTO the entity to save.
     * @return the persisted entity.
     */
    public RegistroIntegracaoDTO save(RegistroIntegracaoDTO registroIntegracaoDTO) {
        LOG.debug("Request to save RegistroIntegracao : {}", registroIntegracaoDTO);
        RegistroIntegracao registroIntegracao = registroIntegracaoMapper.toEntity(registroIntegracaoDTO);
        registroIntegracao = registroIntegracaoRepository.save(registroIntegracao);
        return registroIntegracaoMapper.toDto(registroIntegracao);
    }

    /**
     * Update a registroIntegracao.
     *
     * @param registroIntegracaoDTO the entity to save.
     * @return the persisted entity.
     */
    public RegistroIntegracaoDTO update(RegistroIntegracaoDTO registroIntegracaoDTO) {
        LOG.debug("Request to update RegistroIntegracao : {}", registroIntegracaoDTO);
        RegistroIntegracao registroIntegracao = registroIntegracaoMapper.toEntity(registroIntegracaoDTO);
        registroIntegracao = registroIntegracaoRepository.save(registroIntegracao);
        return registroIntegracaoMapper.toDto(registroIntegracao);
    }

    /**
     * Partially update a registroIntegracao.
     *
     * @param registroIntegracaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RegistroIntegracaoDTO> partialUpdate(RegistroIntegracaoDTO registroIntegracaoDTO) {
        LOG.debug("Request to partially update RegistroIntegracao : {}", registroIntegracaoDTO);

        return registroIntegracaoRepository
            .findById(registroIntegracaoDTO.getId())
            .map(existingRegistroIntegracao -> {
                registroIntegracaoMapper.partialUpdate(existingRegistroIntegracao, registroIntegracaoDTO);

                return existingRegistroIntegracao;
            })
            .map(registroIntegracaoRepository::save)
            .map(registroIntegracaoMapper::toDto);
    }

    /**
     * Get one registroIntegracao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RegistroIntegracaoDTO> findOne(Long id) {
        LOG.debug("Request to get RegistroIntegracao : {}", id);
        return registroIntegracaoRepository.findById(id).map(registroIntegracaoMapper::toDto);
    }

    /**
     * Delete the registroIntegracao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete RegistroIntegracao : {}", id);
        registroIntegracaoRepository.deleteById(id);
    }
}
