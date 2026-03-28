package com.bankernel.service;

import com.bankernel.domain.SaqueBoleto;
import com.bankernel.repository.SaqueBoletoRepository;
import com.bankernel.service.dto.SaqueBoletoDTO;
import com.bankernel.service.mapper.SaqueBoletoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.SaqueBoleto}.
 */
@Service
@Transactional
public class SaqueBoletoService {

    private static final Logger LOG = LoggerFactory.getLogger(SaqueBoletoService.class);

    private final SaqueBoletoRepository saqueBoletoRepository;

    private final SaqueBoletoMapper saqueBoletoMapper;

    public SaqueBoletoService(SaqueBoletoRepository saqueBoletoRepository, SaqueBoletoMapper saqueBoletoMapper) {
        this.saqueBoletoRepository = saqueBoletoRepository;
        this.saqueBoletoMapper = saqueBoletoMapper;
    }

    /**
     * Save a saqueBoleto.
     *
     * @param saqueBoletoDTO the entity to save.
     * @return the persisted entity.
     */
    public SaqueBoletoDTO save(SaqueBoletoDTO saqueBoletoDTO) {
        LOG.debug("Request to save SaqueBoleto : {}", saqueBoletoDTO);
        SaqueBoleto saqueBoleto = saqueBoletoMapper.toEntity(saqueBoletoDTO);
        saqueBoleto = saqueBoletoRepository.save(saqueBoleto);
        return saqueBoletoMapper.toDto(saqueBoleto);
    }

    /**
     * Update a saqueBoleto.
     *
     * @param saqueBoletoDTO the entity to save.
     * @return the persisted entity.
     */
    public SaqueBoletoDTO update(SaqueBoletoDTO saqueBoletoDTO) {
        LOG.debug("Request to update SaqueBoleto : {}", saqueBoletoDTO);
        SaqueBoleto saqueBoleto = saqueBoletoMapper.toEntity(saqueBoletoDTO);
        saqueBoleto = saqueBoletoRepository.save(saqueBoleto);
        return saqueBoletoMapper.toDto(saqueBoleto);
    }

    /**
     * Partially update a saqueBoleto.
     *
     * @param saqueBoletoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SaqueBoletoDTO> partialUpdate(SaqueBoletoDTO saqueBoletoDTO) {
        LOG.debug("Request to partially update SaqueBoleto : {}", saqueBoletoDTO);

        return saqueBoletoRepository
            .findById(saqueBoletoDTO.getId())
            .map(existingSaqueBoleto -> {
                saqueBoletoMapper.partialUpdate(existingSaqueBoleto, saqueBoletoDTO);

                return existingSaqueBoleto;
            })
            .map(saqueBoletoRepository::save)
            .map(saqueBoletoMapper::toDto);
    }

    /**
     * Get all the saqueBoletos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SaqueBoletoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SaqueBoletos");
        return saqueBoletoRepository.findAll(pageable).map(saqueBoletoMapper::toDto);
    }

    /**
     * Get all the saqueBoletos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SaqueBoletoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return saqueBoletoRepository.findAllWithEagerRelationships(pageable).map(saqueBoletoMapper::toDto);
    }

    /**
     * Get one saqueBoleto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SaqueBoletoDTO> findOne(Long id) {
        LOG.debug("Request to get SaqueBoleto : {}", id);
        return saqueBoletoRepository.findOneWithEagerRelationships(id).map(saqueBoletoMapper::toDto);
    }

    /**
     * Delete the saqueBoleto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SaqueBoleto : {}", id);
        saqueBoletoRepository.deleteById(id);
    }
}
