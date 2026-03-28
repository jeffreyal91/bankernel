package com.bankernel.service;

import com.bankernel.domain.Plano;
import com.bankernel.repository.PlanoRepository;
import com.bankernel.service.dto.PlanoDTO;
import com.bankernel.service.mapper.PlanoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.Plano}.
 */
@Service
@Transactional
public class PlanoService {

    private static final Logger LOG = LoggerFactory.getLogger(PlanoService.class);

    private final PlanoRepository planoRepository;

    private final PlanoMapper planoMapper;

    public PlanoService(PlanoRepository planoRepository, PlanoMapper planoMapper) {
        this.planoRepository = planoRepository;
        this.planoMapper = planoMapper;
    }

    /**
     * Save a plano.
     *
     * @param planoDTO the entity to save.
     * @return the persisted entity.
     */
    public PlanoDTO save(PlanoDTO planoDTO) {
        LOG.debug("Request to save Plano : {}", planoDTO);
        Plano plano = planoMapper.toEntity(planoDTO);
        plano = planoRepository.save(plano);
        return planoMapper.toDto(plano);
    }

    /**
     * Update a plano.
     *
     * @param planoDTO the entity to save.
     * @return the persisted entity.
     */
    public PlanoDTO update(PlanoDTO planoDTO) {
        LOG.debug("Request to update Plano : {}", planoDTO);
        Plano plano = planoMapper.toEntity(planoDTO);
        plano = planoRepository.save(plano);
        return planoMapper.toDto(plano);
    }

    /**
     * Partially update a plano.
     *
     * @param planoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PlanoDTO> partialUpdate(PlanoDTO planoDTO) {
        LOG.debug("Request to partially update Plano : {}", planoDTO);

        return planoRepository
            .findById(planoDTO.getId())
            .map(existingPlano -> {
                planoMapper.partialUpdate(existingPlano, planoDTO);

                return existingPlano;
            })
            .map(planoRepository::save)
            .map(planoMapper::toDto);
    }

    /**
     * Get all the planos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PlanoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Planos");
        return planoRepository.findAll(pageable).map(planoMapper::toDto);
    }

    /**
     * Get one plano by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PlanoDTO> findOne(Long id) {
        LOG.debug("Request to get Plano : {}", id);
        return planoRepository.findById(id).map(planoMapper::toDto);
    }

    /**
     * Delete the plano by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Plano : {}", id);
        planoRepository.deleteById(id);
    }
}
