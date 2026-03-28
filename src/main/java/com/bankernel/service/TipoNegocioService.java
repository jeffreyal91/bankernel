package com.bankernel.service;

import com.bankernel.domain.TipoNegocio;
import com.bankernel.repository.TipoNegocioRepository;
import com.bankernel.service.dto.TipoNegocioDTO;
import com.bankernel.service.mapper.TipoNegocioMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.TipoNegocio}.
 */
@Service
@Transactional
public class TipoNegocioService {

    private static final Logger LOG = LoggerFactory.getLogger(TipoNegocioService.class);

    private final TipoNegocioRepository tipoNegocioRepository;

    private final TipoNegocioMapper tipoNegocioMapper;

    public TipoNegocioService(TipoNegocioRepository tipoNegocioRepository, TipoNegocioMapper tipoNegocioMapper) {
        this.tipoNegocioRepository = tipoNegocioRepository;
        this.tipoNegocioMapper = tipoNegocioMapper;
    }

    /**
     * Save a tipoNegocio.
     *
     * @param tipoNegocioDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoNegocioDTO save(TipoNegocioDTO tipoNegocioDTO) {
        LOG.debug("Request to save TipoNegocio : {}", tipoNegocioDTO);
        TipoNegocio tipoNegocio = tipoNegocioMapper.toEntity(tipoNegocioDTO);
        tipoNegocio = tipoNegocioRepository.save(tipoNegocio);
        return tipoNegocioMapper.toDto(tipoNegocio);
    }

    /**
     * Update a tipoNegocio.
     *
     * @param tipoNegocioDTO the entity to save.
     * @return the persisted entity.
     */
    public TipoNegocioDTO update(TipoNegocioDTO tipoNegocioDTO) {
        LOG.debug("Request to update TipoNegocio : {}", tipoNegocioDTO);
        TipoNegocio tipoNegocio = tipoNegocioMapper.toEntity(tipoNegocioDTO);
        tipoNegocio = tipoNegocioRepository.save(tipoNegocio);
        return tipoNegocioMapper.toDto(tipoNegocio);
    }

    /**
     * Partially update a tipoNegocio.
     *
     * @param tipoNegocioDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TipoNegocioDTO> partialUpdate(TipoNegocioDTO tipoNegocioDTO) {
        LOG.debug("Request to partially update TipoNegocio : {}", tipoNegocioDTO);

        return tipoNegocioRepository
            .findById(tipoNegocioDTO.getId())
            .map(existingTipoNegocio -> {
                tipoNegocioMapper.partialUpdate(existingTipoNegocio, tipoNegocioDTO);

                return existingTipoNegocio;
            })
            .map(tipoNegocioRepository::save)
            .map(tipoNegocioMapper::toDto);
    }

    /**
     * Get all the tipoNegocios.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<TipoNegocioDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all TipoNegocios");
        return tipoNegocioRepository.findAll(pageable).map(tipoNegocioMapper::toDto);
    }

    /**
     * Get one tipoNegocio by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TipoNegocioDTO> findOne(Long id) {
        LOG.debug("Request to get TipoNegocio : {}", id);
        return tipoNegocioRepository.findById(id).map(tipoNegocioMapper::toDto);
    }

    /**
     * Delete the tipoNegocio by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TipoNegocio : {}", id);
        tipoNegocioRepository.deleteById(id);
    }
}
