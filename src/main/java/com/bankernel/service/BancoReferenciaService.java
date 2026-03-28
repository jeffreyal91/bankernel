package com.bankernel.service;

import com.bankernel.domain.BancoReferencia;
import com.bankernel.repository.BancoReferenciaRepository;
import com.bankernel.service.dto.BancoReferenciaDTO;
import com.bankernel.service.mapper.BancoReferenciaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.BancoReferencia}.
 */
@Service
@Transactional
public class BancoReferenciaService {

    private static final Logger LOG = LoggerFactory.getLogger(BancoReferenciaService.class);

    private final BancoReferenciaRepository bancoReferenciaRepository;

    private final BancoReferenciaMapper bancoReferenciaMapper;

    public BancoReferenciaService(BancoReferenciaRepository bancoReferenciaRepository, BancoReferenciaMapper bancoReferenciaMapper) {
        this.bancoReferenciaRepository = bancoReferenciaRepository;
        this.bancoReferenciaMapper = bancoReferenciaMapper;
    }

    /**
     * Save a bancoReferencia.
     *
     * @param bancoReferenciaDTO the entity to save.
     * @return the persisted entity.
     */
    public BancoReferenciaDTO save(BancoReferenciaDTO bancoReferenciaDTO) {
        LOG.debug("Request to save BancoReferencia : {}", bancoReferenciaDTO);
        BancoReferencia bancoReferencia = bancoReferenciaMapper.toEntity(bancoReferenciaDTO);
        bancoReferencia = bancoReferenciaRepository.save(bancoReferencia);
        return bancoReferenciaMapper.toDto(bancoReferencia);
    }

    /**
     * Update a bancoReferencia.
     *
     * @param bancoReferenciaDTO the entity to save.
     * @return the persisted entity.
     */
    public BancoReferenciaDTO update(BancoReferenciaDTO bancoReferenciaDTO) {
        LOG.debug("Request to update BancoReferencia : {}", bancoReferenciaDTO);
        BancoReferencia bancoReferencia = bancoReferenciaMapper.toEntity(bancoReferenciaDTO);
        bancoReferencia = bancoReferenciaRepository.save(bancoReferencia);
        return bancoReferenciaMapper.toDto(bancoReferencia);
    }

    /**
     * Partially update a bancoReferencia.
     *
     * @param bancoReferenciaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<BancoReferenciaDTO> partialUpdate(BancoReferenciaDTO bancoReferenciaDTO) {
        LOG.debug("Request to partially update BancoReferencia : {}", bancoReferenciaDTO);

        return bancoReferenciaRepository
            .findById(bancoReferenciaDTO.getId())
            .map(existingBancoReferencia -> {
                bancoReferenciaMapper.partialUpdate(existingBancoReferencia, bancoReferenciaDTO);

                return existingBancoReferencia;
            })
            .map(bancoReferenciaRepository::save)
            .map(bancoReferenciaMapper::toDto);
    }

    /**
     * Get all the bancoReferencias.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<BancoReferenciaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all BancoReferencias");
        return bancoReferenciaRepository.findAll(pageable).map(bancoReferenciaMapper::toDto);
    }

    /**
     * Get one bancoReferencia by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<BancoReferenciaDTO> findOne(Long id) {
        LOG.debug("Request to get BancoReferencia : {}", id);
        return bancoReferenciaRepository.findById(id).map(bancoReferenciaMapper::toDto);
    }

    /**
     * Delete the bancoReferencia by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete BancoReferencia : {}", id);
        bancoReferenciaRepository.deleteById(id);
    }
}
