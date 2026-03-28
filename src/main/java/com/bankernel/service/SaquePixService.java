package com.bankernel.service;

import com.bankernel.domain.SaquePix;
import com.bankernel.repository.SaquePixRepository;
import com.bankernel.service.dto.SaquePixDTO;
import com.bankernel.service.mapper.SaquePixMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.SaquePix}.
 */
@Service
@Transactional
public class SaquePixService {

    private static final Logger LOG = LoggerFactory.getLogger(SaquePixService.class);

    private final SaquePixRepository saquePixRepository;

    private final SaquePixMapper saquePixMapper;

    public SaquePixService(SaquePixRepository saquePixRepository, SaquePixMapper saquePixMapper) {
        this.saquePixRepository = saquePixRepository;
        this.saquePixMapper = saquePixMapper;
    }

    /**
     * Save a saquePix.
     *
     * @param saquePixDTO the entity to save.
     * @return the persisted entity.
     */
    public SaquePixDTO save(SaquePixDTO saquePixDTO) {
        LOG.debug("Request to save SaquePix : {}", saquePixDTO);
        SaquePix saquePix = saquePixMapper.toEntity(saquePixDTO);
        saquePix = saquePixRepository.save(saquePix);
        return saquePixMapper.toDto(saquePix);
    }

    /**
     * Update a saquePix.
     *
     * @param saquePixDTO the entity to save.
     * @return the persisted entity.
     */
    public SaquePixDTO update(SaquePixDTO saquePixDTO) {
        LOG.debug("Request to update SaquePix : {}", saquePixDTO);
        SaquePix saquePix = saquePixMapper.toEntity(saquePixDTO);
        saquePix = saquePixRepository.save(saquePix);
        return saquePixMapper.toDto(saquePix);
    }

    /**
     * Partially update a saquePix.
     *
     * @param saquePixDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SaquePixDTO> partialUpdate(SaquePixDTO saquePixDTO) {
        LOG.debug("Request to partially update SaquePix : {}", saquePixDTO);

        return saquePixRepository
            .findById(saquePixDTO.getId())
            .map(existingSaquePix -> {
                saquePixMapper.partialUpdate(existingSaquePix, saquePixDTO);

                return existingSaquePix;
            })
            .map(saquePixRepository::save)
            .map(saquePixMapper::toDto);
    }

    /**
     * Get all the saquePixes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SaquePixDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SaquePixes");
        return saquePixRepository.findAll(pageable).map(saquePixMapper::toDto);
    }

    /**
     * Get all the saquePixes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SaquePixDTO> findAllWithEagerRelationships(Pageable pageable) {
        return saquePixRepository.findAllWithEagerRelationships(pageable).map(saquePixMapper::toDto);
    }

    /**
     * Get one saquePix by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SaquePixDTO> findOne(Long id) {
        LOG.debug("Request to get SaquePix : {}", id);
        return saquePixRepository.findOneWithEagerRelationships(id).map(saquePixMapper::toDto);
    }

    /**
     * Delete the saquePix by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SaquePix : {}", id);
        saquePixRepository.deleteById(id);
    }
}
