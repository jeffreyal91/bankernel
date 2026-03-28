package com.bankernel.service;

import com.bankernel.domain.Transferencia;
import com.bankernel.repository.TransferenciaRepository;
import com.bankernel.service.dto.TransferenciaDTO;
import com.bankernel.service.mapper.TransferenciaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.Transferencia}.
 */
@Service
@Transactional
public class TransferenciaService {

    private static final Logger LOG = LoggerFactory.getLogger(TransferenciaService.class);

    private final TransferenciaRepository transferenciaRepository;

    private final TransferenciaMapper transferenciaMapper;

    public TransferenciaService(TransferenciaRepository transferenciaRepository, TransferenciaMapper transferenciaMapper) {
        this.transferenciaRepository = transferenciaRepository;
        this.transferenciaMapper = transferenciaMapper;
    }

    /**
     * Save a transferencia.
     *
     * @param transferenciaDTO the entity to save.
     * @return the persisted entity.
     */
    public TransferenciaDTO save(TransferenciaDTO transferenciaDTO) {
        LOG.debug("Request to save Transferencia : {}", transferenciaDTO);
        Transferencia transferencia = transferenciaMapper.toEntity(transferenciaDTO);
        transferencia = transferenciaRepository.save(transferencia);
        return transferenciaMapper.toDto(transferencia);
    }

    /**
     * Update a transferencia.
     *
     * @param transferenciaDTO the entity to save.
     * @return the persisted entity.
     */
    public TransferenciaDTO update(TransferenciaDTO transferenciaDTO) {
        LOG.debug("Request to update Transferencia : {}", transferenciaDTO);
        Transferencia transferencia = transferenciaMapper.toEntity(transferenciaDTO);
        transferencia = transferenciaRepository.save(transferencia);
        return transferenciaMapper.toDto(transferencia);
    }

    /**
     * Partially update a transferencia.
     *
     * @param transferenciaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TransferenciaDTO> partialUpdate(TransferenciaDTO transferenciaDTO) {
        LOG.debug("Request to partially update Transferencia : {}", transferenciaDTO);

        return transferenciaRepository
            .findById(transferenciaDTO.getId())
            .map(existingTransferencia -> {
                transferenciaMapper.partialUpdate(existingTransferencia, transferenciaDTO);

                return existingTransferencia;
            })
            .map(transferenciaRepository::save)
            .map(transferenciaMapper::toDto);
    }

    /**
     * Get all the transferencias with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TransferenciaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return transferenciaRepository.findAllWithEagerRelationships(pageable).map(transferenciaMapper::toDto);
    }

    /**
     * Get one transferencia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TransferenciaDTO> findOne(Long id) {
        LOG.debug("Request to get Transferencia : {}", id);
        return transferenciaRepository.findOneWithEagerRelationships(id).map(transferenciaMapper::toDto);
    }

    /**
     * Delete the transferencia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Transferencia : {}", id);
        transferenciaRepository.deleteById(id);
    }
}
