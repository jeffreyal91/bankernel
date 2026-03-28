package com.bankernel.service;

import com.bankernel.domain.PlanoRecorrencia;
import com.bankernel.repository.PlanoRecorrenciaRepository;
import com.bankernel.service.dto.PlanoRecorrenciaDTO;
import com.bankernel.service.mapper.PlanoRecorrenciaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.PlanoRecorrencia}.
 */
@Service
@Transactional
public class PlanoRecorrenciaService {

    private static final Logger LOG = LoggerFactory.getLogger(PlanoRecorrenciaService.class);

    private final PlanoRecorrenciaRepository planoRecorrenciaRepository;

    private final PlanoRecorrenciaMapper planoRecorrenciaMapper;

    public PlanoRecorrenciaService(PlanoRecorrenciaRepository planoRecorrenciaRepository, PlanoRecorrenciaMapper planoRecorrenciaMapper) {
        this.planoRecorrenciaRepository = planoRecorrenciaRepository;
        this.planoRecorrenciaMapper = planoRecorrenciaMapper;
    }

    /**
     * Save a planoRecorrencia.
     *
     * @param planoRecorrenciaDTO the entity to save.
     * @return the persisted entity.
     */
    public PlanoRecorrenciaDTO save(PlanoRecorrenciaDTO planoRecorrenciaDTO) {
        LOG.debug("Request to save PlanoRecorrencia : {}", planoRecorrenciaDTO);
        PlanoRecorrencia planoRecorrencia = planoRecorrenciaMapper.toEntity(planoRecorrenciaDTO);
        planoRecorrencia = planoRecorrenciaRepository.save(planoRecorrencia);
        return planoRecorrenciaMapper.toDto(planoRecorrencia);
    }

    /**
     * Update a planoRecorrencia.
     *
     * @param planoRecorrenciaDTO the entity to save.
     * @return the persisted entity.
     */
    public PlanoRecorrenciaDTO update(PlanoRecorrenciaDTO planoRecorrenciaDTO) {
        LOG.debug("Request to update PlanoRecorrencia : {}", planoRecorrenciaDTO);
        PlanoRecorrencia planoRecorrencia = planoRecorrenciaMapper.toEntity(planoRecorrenciaDTO);
        planoRecorrencia = planoRecorrenciaRepository.save(planoRecorrencia);
        return planoRecorrenciaMapper.toDto(planoRecorrencia);
    }

    /**
     * Partially update a planoRecorrencia.
     *
     * @param planoRecorrenciaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PlanoRecorrenciaDTO> partialUpdate(PlanoRecorrenciaDTO planoRecorrenciaDTO) {
        LOG.debug("Request to partially update PlanoRecorrencia : {}", planoRecorrenciaDTO);

        return planoRecorrenciaRepository
            .findById(planoRecorrenciaDTO.getId())
            .map(existingPlanoRecorrencia -> {
                planoRecorrenciaMapper.partialUpdate(existingPlanoRecorrencia, planoRecorrenciaDTO);

                return existingPlanoRecorrencia;
            })
            .map(planoRecorrenciaRepository::save)
            .map(planoRecorrenciaMapper::toDto);
    }

    /**
     * Get all the planoRecorrencias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanoRecorrenciaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PlanoRecorrencias");
        return planoRecorrenciaRepository.findAll(pageable).map(planoRecorrenciaMapper::toDto);
    }

    /**
     * Get all the planoRecorrencias with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PlanoRecorrenciaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return planoRecorrenciaRepository.findAllWithEagerRelationships(pageable).map(planoRecorrenciaMapper::toDto);
    }

    /**
     * Get one planoRecorrencia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlanoRecorrenciaDTO> findOne(Long id) {
        LOG.debug("Request to get PlanoRecorrencia : {}", id);
        return planoRecorrenciaRepository.findOneWithEagerRelationships(id).map(planoRecorrenciaMapper::toDto);
    }

    /**
     * Delete the planoRecorrencia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PlanoRecorrencia : {}", id);
        planoRecorrenciaRepository.deleteById(id);
    }
}
