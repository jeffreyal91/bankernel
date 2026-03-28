package com.bankernel.service;

import com.bankernel.domain.LancamentoContabil;
import com.bankernel.repository.LancamentoContabilRepository;
import com.bankernel.service.dto.LancamentoContabilDTO;
import com.bankernel.service.mapper.LancamentoContabilMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.LancamentoContabil}.
 */
@Service
@Transactional
public class LancamentoContabilService {

    private static final Logger LOG = LoggerFactory.getLogger(LancamentoContabilService.class);

    private final LancamentoContabilRepository lancamentoContabilRepository;

    private final LancamentoContabilMapper lancamentoContabilMapper;

    public LancamentoContabilService(
        LancamentoContabilRepository lancamentoContabilRepository,
        LancamentoContabilMapper lancamentoContabilMapper
    ) {
        this.lancamentoContabilRepository = lancamentoContabilRepository;
        this.lancamentoContabilMapper = lancamentoContabilMapper;
    }

    /**
     * Save a lancamentoContabil.
     *
     * @param lancamentoContabilDTO the entity to save.
     * @return the persisted entity.
     */
    public LancamentoContabilDTO save(LancamentoContabilDTO lancamentoContabilDTO) {
        LOG.debug("Request to save LancamentoContabil : {}", lancamentoContabilDTO);
        LancamentoContabil lancamentoContabil = lancamentoContabilMapper.toEntity(lancamentoContabilDTO);
        lancamentoContabil = lancamentoContabilRepository.save(lancamentoContabil);
        return lancamentoContabilMapper.toDto(lancamentoContabil);
    }

    /**
     * Update a lancamentoContabil.
     *
     * @param lancamentoContabilDTO the entity to save.
     * @return the persisted entity.
     */
    public LancamentoContabilDTO update(LancamentoContabilDTO lancamentoContabilDTO) {
        LOG.debug("Request to update LancamentoContabil : {}", lancamentoContabilDTO);
        LancamentoContabil lancamentoContabil = lancamentoContabilMapper.toEntity(lancamentoContabilDTO);
        lancamentoContabil = lancamentoContabilRepository.save(lancamentoContabil);
        return lancamentoContabilMapper.toDto(lancamentoContabil);
    }

    /**
     * Partially update a lancamentoContabil.
     *
     * @param lancamentoContabilDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LancamentoContabilDTO> partialUpdate(LancamentoContabilDTO lancamentoContabilDTO) {
        LOG.debug("Request to partially update LancamentoContabil : {}", lancamentoContabilDTO);

        return lancamentoContabilRepository
            .findById(lancamentoContabilDTO.getId())
            .map(existingLancamentoContabil -> {
                lancamentoContabilMapper.partialUpdate(existingLancamentoContabil, lancamentoContabilDTO);

                return existingLancamentoContabil;
            })
            .map(lancamentoContabilRepository::save)
            .map(lancamentoContabilMapper::toDto);
    }

    /**
     * Get all the lancamentoContabils with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<LancamentoContabilDTO> findAllWithEagerRelationships(Pageable pageable) {
        return lancamentoContabilRepository.findAllWithEagerRelationships(pageable).map(lancamentoContabilMapper::toDto);
    }

    /**
     * Get one lancamentoContabil by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LancamentoContabilDTO> findOne(Long id) {
        LOG.debug("Request to get LancamentoContabil : {}", id);
        return lancamentoContabilRepository.findOneWithEagerRelationships(id).map(lancamentoContabilMapper::toDto);
    }

    /**
     * Delete the lancamentoContabil by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete LancamentoContabil : {}", id);
        lancamentoContabilRepository.deleteById(id);
    }
}
