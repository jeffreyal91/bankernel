package com.bankernel.service;

import com.bankernel.domain.Escritorio;
import com.bankernel.repository.EscritorioRepository;
import com.bankernel.service.dto.EscritorioDTO;
import com.bankernel.service.mapper.EscritorioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.Escritorio}.
 */
@Service
@Transactional
public class EscritorioService {

    private static final Logger LOG = LoggerFactory.getLogger(EscritorioService.class);

    private final EscritorioRepository escritorioRepository;

    private final EscritorioMapper escritorioMapper;

    public EscritorioService(EscritorioRepository escritorioRepository, EscritorioMapper escritorioMapper) {
        this.escritorioRepository = escritorioRepository;
        this.escritorioMapper = escritorioMapper;
    }

    /**
     * Save a escritorio.
     *
     * @param escritorioDTO the entity to save.
     * @return the persisted entity.
     */
    public EscritorioDTO save(EscritorioDTO escritorioDTO) {
        LOG.debug("Request to save Escritorio : {}", escritorioDTO);
        Escritorio escritorio = escritorioMapper.toEntity(escritorioDTO);
        escritorio = escritorioRepository.save(escritorio);
        return escritorioMapper.toDto(escritorio);
    }

    /**
     * Update a escritorio.
     *
     * @param escritorioDTO the entity to save.
     * @return the persisted entity.
     */
    public EscritorioDTO update(EscritorioDTO escritorioDTO) {
        LOG.debug("Request to update Escritorio : {}", escritorioDTO);
        Escritorio escritorio = escritorioMapper.toEntity(escritorioDTO);
        escritorio = escritorioRepository.save(escritorio);
        return escritorioMapper.toDto(escritorio);
    }

    /**
     * Partially update a escritorio.
     *
     * @param escritorioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EscritorioDTO> partialUpdate(EscritorioDTO escritorioDTO) {
        LOG.debug("Request to partially update Escritorio : {}", escritorioDTO);

        return escritorioRepository
            .findById(escritorioDTO.getId())
            .map(existingEscritorio -> {
                escritorioMapper.partialUpdate(existingEscritorio, escritorioDTO);

                return existingEscritorio;
            })
            .map(escritorioRepository::save)
            .map(escritorioMapper::toDto);
    }

    /**
     * Get all the escritorios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<EscritorioDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Escritorios");
        return escritorioRepository.findAll(pageable).map(escritorioMapper::toDto);
    }

    /**
     * Get one escritorio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EscritorioDTO> findOne(Long id) {
        LOG.debug("Request to get Escritorio : {}", id);
        return escritorioRepository.findById(id).map(escritorioMapper::toDto);
    }

    /**
     * Delete the escritorio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Escritorio : {}", id);
        escritorioRepository.deleteById(id);
    }
}
