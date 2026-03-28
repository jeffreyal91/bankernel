package com.bankernel.service;

import com.bankernel.domain.Cobranca;
import com.bankernel.repository.CobrancaRepository;
import com.bankernel.service.dto.CobrancaDTO;
import com.bankernel.service.mapper.CobrancaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.Cobranca}.
 */
@Service
@Transactional
public class CobrancaService {

    private static final Logger LOG = LoggerFactory.getLogger(CobrancaService.class);

    private final CobrancaRepository cobrancaRepository;

    private final CobrancaMapper cobrancaMapper;

    public CobrancaService(CobrancaRepository cobrancaRepository, CobrancaMapper cobrancaMapper) {
        this.cobrancaRepository = cobrancaRepository;
        this.cobrancaMapper = cobrancaMapper;
    }

    /**
     * Save a cobranca.
     *
     * @param cobrancaDTO the entity to save.
     * @return the persisted entity.
     */
    public CobrancaDTO save(CobrancaDTO cobrancaDTO) {
        LOG.debug("Request to save Cobranca : {}", cobrancaDTO);
        Cobranca cobranca = cobrancaMapper.toEntity(cobrancaDTO);
        cobranca = cobrancaRepository.save(cobranca);
        return cobrancaMapper.toDto(cobranca);
    }

    /**
     * Update a cobranca.
     *
     * @param cobrancaDTO the entity to save.
     * @return the persisted entity.
     */
    public CobrancaDTO update(CobrancaDTO cobrancaDTO) {
        LOG.debug("Request to update Cobranca : {}", cobrancaDTO);
        Cobranca cobranca = cobrancaMapper.toEntity(cobrancaDTO);
        cobranca = cobrancaRepository.save(cobranca);
        return cobrancaMapper.toDto(cobranca);
    }

    /**
     * Partially update a cobranca.
     *
     * @param cobrancaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CobrancaDTO> partialUpdate(CobrancaDTO cobrancaDTO) {
        LOG.debug("Request to partially update Cobranca : {}", cobrancaDTO);

        return cobrancaRepository
            .findById(cobrancaDTO.getId())
            .map(existingCobranca -> {
                cobrancaMapper.partialUpdate(existingCobranca, cobrancaDTO);

                return existingCobranca;
            })
            .map(cobrancaRepository::save)
            .map(cobrancaMapper::toDto);
    }

    /**
     * Get all the cobrancas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CobrancaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return cobrancaRepository.findAllWithEagerRelationships(pageable).map(cobrancaMapper::toDto);
    }

    /**
     * Get one cobranca by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CobrancaDTO> findOne(Long id) {
        LOG.debug("Request to get Cobranca : {}", id);
        return cobrancaRepository.findOneWithEagerRelationships(id).map(cobrancaMapper::toDto);
    }

    /**
     * Delete the cobranca by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Cobranca : {}", id);
        cobrancaRepository.deleteById(id);
    }
}
