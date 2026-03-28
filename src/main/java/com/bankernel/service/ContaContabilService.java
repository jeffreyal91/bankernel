package com.bankernel.service;

import com.bankernel.domain.ContaContabil;
import com.bankernel.repository.ContaContabilRepository;
import com.bankernel.service.dto.ContaContabilDTO;
import com.bankernel.service.mapper.ContaContabilMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.ContaContabil}.
 */
@Service
@Transactional
public class ContaContabilService {

    private static final Logger LOG = LoggerFactory.getLogger(ContaContabilService.class);

    private final ContaContabilRepository contaContabilRepository;

    private final ContaContabilMapper contaContabilMapper;

    public ContaContabilService(ContaContabilRepository contaContabilRepository, ContaContabilMapper contaContabilMapper) {
        this.contaContabilRepository = contaContabilRepository;
        this.contaContabilMapper = contaContabilMapper;
    }

    /**
     * Save a contaContabil.
     *
     * @param contaContabilDTO the entity to save.
     * @return the persisted entity.
     */
    public ContaContabilDTO save(ContaContabilDTO contaContabilDTO) {
        LOG.debug("Request to save ContaContabil : {}", contaContabilDTO);
        ContaContabil contaContabil = contaContabilMapper.toEntity(contaContabilDTO);
        contaContabil = contaContabilRepository.save(contaContabil);
        return contaContabilMapper.toDto(contaContabil);
    }

    /**
     * Update a contaContabil.
     *
     * @param contaContabilDTO the entity to save.
     * @return the persisted entity.
     */
    public ContaContabilDTO update(ContaContabilDTO contaContabilDTO) {
        LOG.debug("Request to update ContaContabil : {}", contaContabilDTO);
        ContaContabil contaContabil = contaContabilMapper.toEntity(contaContabilDTO);
        contaContabil = contaContabilRepository.save(contaContabil);
        return contaContabilMapper.toDto(contaContabil);
    }

    /**
     * Partially update a contaContabil.
     *
     * @param contaContabilDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ContaContabilDTO> partialUpdate(ContaContabilDTO contaContabilDTO) {
        LOG.debug("Request to partially update ContaContabil : {}", contaContabilDTO);

        return contaContabilRepository
            .findById(contaContabilDTO.getId())
            .map(existingContaContabil -> {
                contaContabilMapper.partialUpdate(existingContaContabil, contaContabilDTO);

                return existingContaContabil;
            })
            .map(contaContabilRepository::save)
            .map(contaContabilMapper::toDto);
    }

    /**
     * Get all the contaContabils with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ContaContabilDTO> findAllWithEagerRelationships(Pageable pageable) {
        return contaContabilRepository.findAllWithEagerRelationships(pageable).map(contaContabilMapper::toDto);
    }

    /**
     * Get one contaContabil by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ContaContabilDTO> findOne(Long id) {
        LOG.debug("Request to get ContaContabil : {}", id);
        return contaContabilRepository.findOneWithEagerRelationships(id).map(contaContabilMapper::toDto);
    }

    /**
     * Delete the contaContabil by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ContaContabil : {}", id);
        contaContabilRepository.deleteById(id);
    }
}
