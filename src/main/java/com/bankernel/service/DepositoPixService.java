package com.bankernel.service;

import com.bankernel.domain.DepositoPix;
import com.bankernel.repository.DepositoPixRepository;
import com.bankernel.service.dto.DepositoPixDTO;
import com.bankernel.service.mapper.DepositoPixMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.DepositoPix}.
 */
@Service
@Transactional
public class DepositoPixService {

    private static final Logger LOG = LoggerFactory.getLogger(DepositoPixService.class);

    private final DepositoPixRepository depositoPixRepository;

    private final DepositoPixMapper depositoPixMapper;

    public DepositoPixService(DepositoPixRepository depositoPixRepository, DepositoPixMapper depositoPixMapper) {
        this.depositoPixRepository = depositoPixRepository;
        this.depositoPixMapper = depositoPixMapper;
    }

    /**
     * Save a depositoPix.
     *
     * @param depositoPixDTO the entity to save.
     * @return the persisted entity.
     */
    public DepositoPixDTO save(DepositoPixDTO depositoPixDTO) {
        LOG.debug("Request to save DepositoPix : {}", depositoPixDTO);
        DepositoPix depositoPix = depositoPixMapper.toEntity(depositoPixDTO);
        depositoPix = depositoPixRepository.save(depositoPix);
        return depositoPixMapper.toDto(depositoPix);
    }

    /**
     * Update a depositoPix.
     *
     * @param depositoPixDTO the entity to save.
     * @return the persisted entity.
     */
    public DepositoPixDTO update(DepositoPixDTO depositoPixDTO) {
        LOG.debug("Request to update DepositoPix : {}", depositoPixDTO);
        DepositoPix depositoPix = depositoPixMapper.toEntity(depositoPixDTO);
        depositoPix = depositoPixRepository.save(depositoPix);
        return depositoPixMapper.toDto(depositoPix);
    }

    /**
     * Partially update a depositoPix.
     *
     * @param depositoPixDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DepositoPixDTO> partialUpdate(DepositoPixDTO depositoPixDTO) {
        LOG.debug("Request to partially update DepositoPix : {}", depositoPixDTO);

        return depositoPixRepository
            .findById(depositoPixDTO.getId())
            .map(existingDepositoPix -> {
                depositoPixMapper.partialUpdate(existingDepositoPix, depositoPixDTO);

                return existingDepositoPix;
            })
            .map(depositoPixRepository::save)
            .map(depositoPixMapper::toDto);
    }

    /**
     * Get all the depositoPixes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DepositoPixDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DepositoPixes");
        return depositoPixRepository.findAll(pageable).map(depositoPixMapper::toDto);
    }

    /**
     * Get all the depositoPixes with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DepositoPixDTO> findAllWithEagerRelationships(Pageable pageable) {
        return depositoPixRepository.findAllWithEagerRelationships(pageable).map(depositoPixMapper::toDto);
    }

    /**
     * Get one depositoPix by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepositoPixDTO> findOne(Long id) {
        LOG.debug("Request to get DepositoPix : {}", id);
        return depositoPixRepository.findOneWithEagerRelationships(id).map(depositoPixMapper::toDto);
    }

    /**
     * Delete the depositoPix by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DepositoPix : {}", id);
        depositoPixRepository.deleteById(id);
    }
}
