package com.bankernel.service;

import com.bankernel.domain.ContaBancaria;
import com.bankernel.repository.ContaBancariaRepository;
import com.bankernel.service.dto.ContaBancariaDTO;
import com.bankernel.service.mapper.ContaBancariaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.ContaBancaria}.
 */
@Service
@Transactional
public class ContaBancariaService {

    private static final Logger LOG = LoggerFactory.getLogger(ContaBancariaService.class);

    private final ContaBancariaRepository contaBancariaRepository;

    private final ContaBancariaMapper contaBancariaMapper;

    public ContaBancariaService(ContaBancariaRepository contaBancariaRepository, ContaBancariaMapper contaBancariaMapper) {
        this.contaBancariaRepository = contaBancariaRepository;
        this.contaBancariaMapper = contaBancariaMapper;
    }

    /**
     * Save a contaBancaria.
     *
     * @param contaBancariaDTO the entity to save.
     * @return the persisted entity.
     */
    public ContaBancariaDTO save(ContaBancariaDTO contaBancariaDTO) {
        LOG.debug("Request to save ContaBancaria : {}", contaBancariaDTO);
        ContaBancaria contaBancaria = contaBancariaMapper.toEntity(contaBancariaDTO);
        contaBancaria = contaBancariaRepository.save(contaBancaria);
        return contaBancariaMapper.toDto(contaBancaria);
    }

    /**
     * Update a contaBancaria.
     *
     * @param contaBancariaDTO the entity to save.
     * @return the persisted entity.
     */
    public ContaBancariaDTO update(ContaBancariaDTO contaBancariaDTO) {
        LOG.debug("Request to update ContaBancaria : {}", contaBancariaDTO);
        ContaBancaria contaBancaria = contaBancariaMapper.toEntity(contaBancariaDTO);
        contaBancaria = contaBancariaRepository.save(contaBancaria);
        return contaBancariaMapper.toDto(contaBancaria);
    }

    /**
     * Partially update a contaBancaria.
     *
     * @param contaBancariaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContaBancariaDTO> partialUpdate(ContaBancariaDTO contaBancariaDTO) {
        LOG.debug("Request to partially update ContaBancaria : {}", contaBancariaDTO);

        return contaBancariaRepository
            .findById(contaBancariaDTO.getId())
            .map(existingContaBancaria -> {
                contaBancariaMapper.partialUpdate(existingContaBancaria, contaBancariaDTO);

                return existingContaBancaria;
            })
            .map(contaBancariaRepository::save)
            .map(contaBancariaMapper::toDto);
    }

    /**
     * Get all the contaBancarias with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ContaBancariaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contaBancariaRepository.findAllWithEagerRelationships(pageable).map(contaBancariaMapper::toDto);
    }

    /**
     * Get one contaBancaria by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContaBancariaDTO> findOne(Long id) {
        LOG.debug("Request to get ContaBancaria : {}", id);
        return contaBancariaRepository.findOneWithEagerRelationships(id).map(contaBancariaMapper::toDto);
    }

    /**
     * Delete the contaBancaria by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ContaBancaria : {}", id);
        contaBancariaRepository.deleteById(id);
    }
}
