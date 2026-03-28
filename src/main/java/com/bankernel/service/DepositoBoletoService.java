package com.bankernel.service;

import com.bankernel.domain.DepositoBoleto;
import com.bankernel.repository.DepositoBoletoRepository;
import com.bankernel.service.dto.DepositoBoletoDTO;
import com.bankernel.service.mapper.DepositoBoletoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.DepositoBoleto}.
 */
@Service
@Transactional
public class DepositoBoletoService {

    private static final Logger LOG = LoggerFactory.getLogger(DepositoBoletoService.class);

    private final DepositoBoletoRepository depositoBoletoRepository;

    private final DepositoBoletoMapper depositoBoletoMapper;

    public DepositoBoletoService(DepositoBoletoRepository depositoBoletoRepository, DepositoBoletoMapper depositoBoletoMapper) {
        this.depositoBoletoRepository = depositoBoletoRepository;
        this.depositoBoletoMapper = depositoBoletoMapper;
    }

    /**
     * Save a depositoBoleto.
     *
     * @param depositoBoletoDTO the entity to save.
     * @return the persisted entity.
     */
    public DepositoBoletoDTO save(DepositoBoletoDTO depositoBoletoDTO) {
        LOG.debug("Request to save DepositoBoleto : {}", depositoBoletoDTO);
        DepositoBoleto depositoBoleto = depositoBoletoMapper.toEntity(depositoBoletoDTO);
        depositoBoleto = depositoBoletoRepository.save(depositoBoleto);
        return depositoBoletoMapper.toDto(depositoBoleto);
    }

    /**
     * Update a depositoBoleto.
     *
     * @param depositoBoletoDTO the entity to save.
     * @return the persisted entity.
     */
    public DepositoBoletoDTO update(DepositoBoletoDTO depositoBoletoDTO) {
        LOG.debug("Request to update DepositoBoleto : {}", depositoBoletoDTO);
        DepositoBoleto depositoBoleto = depositoBoletoMapper.toEntity(depositoBoletoDTO);
        depositoBoleto = depositoBoletoRepository.save(depositoBoleto);
        return depositoBoletoMapper.toDto(depositoBoleto);
    }

    /**
     * Partially update a depositoBoleto.
     *
     * @param depositoBoletoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DepositoBoletoDTO> partialUpdate(DepositoBoletoDTO depositoBoletoDTO) {
        LOG.debug("Request to partially update DepositoBoleto : {}", depositoBoletoDTO);

        return depositoBoletoRepository
            .findById(depositoBoletoDTO.getId())
            .map(existingDepositoBoleto -> {
                depositoBoletoMapper.partialUpdate(existingDepositoBoleto, depositoBoletoDTO);

                return existingDepositoBoleto;
            })
            .map(depositoBoletoRepository::save)
            .map(depositoBoletoMapper::toDto);
    }

    /**
     * Get all the depositoBoletos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DepositoBoletoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all DepositoBoletos");
        return depositoBoletoRepository.findAll(pageable).map(depositoBoletoMapper::toDto);
    }

    /**
     * Get all the depositoBoletos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DepositoBoletoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return depositoBoletoRepository.findAllWithEagerRelationships(pageable).map(depositoBoletoMapper::toDto);
    }

    /**
     * Get one depositoBoleto by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepositoBoletoDTO> findOne(Long id) {
        LOG.debug("Request to get DepositoBoleto : {}", id);
        return depositoBoletoRepository.findOneWithEagerRelationships(id).map(depositoBoletoMapper::toDto);
    }

    /**
     * Delete the depositoBoleto by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete DepositoBoleto : {}", id);
        depositoBoletoRepository.deleteById(id);
    }
}
