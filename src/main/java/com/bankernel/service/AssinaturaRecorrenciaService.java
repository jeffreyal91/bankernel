package com.bankernel.service;

import com.bankernel.domain.AssinaturaRecorrencia;
import com.bankernel.repository.AssinaturaRecorrenciaRepository;
import com.bankernel.service.dto.AssinaturaRecorrenciaDTO;
import com.bankernel.service.mapper.AssinaturaRecorrenciaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.AssinaturaRecorrencia}.
 */
@Service
@Transactional
public class AssinaturaRecorrenciaService {

    private static final Logger LOG = LoggerFactory.getLogger(AssinaturaRecorrenciaService.class);

    private final AssinaturaRecorrenciaRepository assinaturaRecorrenciaRepository;

    private final AssinaturaRecorrenciaMapper assinaturaRecorrenciaMapper;

    public AssinaturaRecorrenciaService(
        AssinaturaRecorrenciaRepository assinaturaRecorrenciaRepository,
        AssinaturaRecorrenciaMapper assinaturaRecorrenciaMapper
    ) {
        this.assinaturaRecorrenciaRepository = assinaturaRecorrenciaRepository;
        this.assinaturaRecorrenciaMapper = assinaturaRecorrenciaMapper;
    }

    /**
     * Save a assinaturaRecorrencia.
     *
     * @param assinaturaRecorrenciaDTO the entity to save.
     * @return the persisted entity.
     */
    public AssinaturaRecorrenciaDTO save(AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO) {
        LOG.debug("Request to save AssinaturaRecorrencia : {}", assinaturaRecorrenciaDTO);
        AssinaturaRecorrencia assinaturaRecorrencia = assinaturaRecorrenciaMapper.toEntity(assinaturaRecorrenciaDTO);
        assinaturaRecorrencia = assinaturaRecorrenciaRepository.save(assinaturaRecorrencia);
        return assinaturaRecorrenciaMapper.toDto(assinaturaRecorrencia);
    }

    /**
     * Update a assinaturaRecorrencia.
     *
     * @param assinaturaRecorrenciaDTO the entity to save.
     * @return the persisted entity.
     */
    public AssinaturaRecorrenciaDTO update(AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO) {
        LOG.debug("Request to update AssinaturaRecorrencia : {}", assinaturaRecorrenciaDTO);
        AssinaturaRecorrencia assinaturaRecorrencia = assinaturaRecorrenciaMapper.toEntity(assinaturaRecorrenciaDTO);
        assinaturaRecorrencia = assinaturaRecorrenciaRepository.save(assinaturaRecorrencia);
        return assinaturaRecorrenciaMapper.toDto(assinaturaRecorrencia);
    }

    /**
     * Partially update a assinaturaRecorrencia.
     *
     * @param assinaturaRecorrenciaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AssinaturaRecorrenciaDTO> partialUpdate(AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO) {
        LOG.debug("Request to partially update AssinaturaRecorrencia : {}", assinaturaRecorrenciaDTO);

        return assinaturaRecorrenciaRepository
            .findById(assinaturaRecorrenciaDTO.getId())
            .map(existingAssinaturaRecorrencia -> {
                assinaturaRecorrenciaMapper.partialUpdate(existingAssinaturaRecorrencia, assinaturaRecorrenciaDTO);

                return existingAssinaturaRecorrencia;
            })
            .map(assinaturaRecorrenciaRepository::save)
            .map(assinaturaRecorrenciaMapper::toDto);
    }

    /**
     * Get all the assinaturaRecorrencias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AssinaturaRecorrenciaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AssinaturaRecorrencias");
        return assinaturaRecorrenciaRepository.findAll(pageable).map(assinaturaRecorrenciaMapper::toDto);
    }

    /**
     * Get all the assinaturaRecorrencias with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<AssinaturaRecorrenciaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return assinaturaRecorrenciaRepository.findAllWithEagerRelationships(pageable).map(assinaturaRecorrenciaMapper::toDto);
    }

    /**
     * Get one assinaturaRecorrencia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AssinaturaRecorrenciaDTO> findOne(Long id) {
        LOG.debug("Request to get AssinaturaRecorrencia : {}", id);
        return assinaturaRecorrenciaRepository.findOneWithEagerRelationships(id).map(assinaturaRecorrenciaMapper::toDto);
    }

    /**
     * Delete the assinaturaRecorrencia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AssinaturaRecorrencia : {}", id);
        assinaturaRecorrenciaRepository.deleteById(id);
    }
}
