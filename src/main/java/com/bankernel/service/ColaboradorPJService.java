package com.bankernel.service;

import com.bankernel.domain.ColaboradorPJ;
import com.bankernel.repository.ColaboradorPJRepository;
import com.bankernel.service.dto.ColaboradorPJDTO;
import com.bankernel.service.mapper.ColaboradorPJMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.ColaboradorPJ}.
 */
@Service
@Transactional
public class ColaboradorPJService {

    private static final Logger LOG = LoggerFactory.getLogger(ColaboradorPJService.class);

    private final ColaboradorPJRepository colaboradorPJRepository;

    private final ColaboradorPJMapper colaboradorPJMapper;

    public ColaboradorPJService(ColaboradorPJRepository colaboradorPJRepository, ColaboradorPJMapper colaboradorPJMapper) {
        this.colaboradorPJRepository = colaboradorPJRepository;
        this.colaboradorPJMapper = colaboradorPJMapper;
    }

    /**
     * Save a colaboradorPJ.
     *
     * @param colaboradorPJDTO the entity to save.
     * @return the persisted entity.
     */
    public ColaboradorPJDTO save(ColaboradorPJDTO colaboradorPJDTO) {
        LOG.debug("Request to save ColaboradorPJ : {}", colaboradorPJDTO);
        ColaboradorPJ colaboradorPJ = colaboradorPJMapper.toEntity(colaboradorPJDTO);
        colaboradorPJ = colaboradorPJRepository.save(colaboradorPJ);
        return colaboradorPJMapper.toDto(colaboradorPJ);
    }

    /**
     * Update a colaboradorPJ.
     *
     * @param colaboradorPJDTO the entity to save.
     * @return the persisted entity.
     */
    public ColaboradorPJDTO update(ColaboradorPJDTO colaboradorPJDTO) {
        LOG.debug("Request to update ColaboradorPJ : {}", colaboradorPJDTO);
        ColaboradorPJ colaboradorPJ = colaboradorPJMapper.toEntity(colaboradorPJDTO);
        colaboradorPJ = colaboradorPJRepository.save(colaboradorPJ);
        return colaboradorPJMapper.toDto(colaboradorPJ);
    }

    /**
     * Partially update a colaboradorPJ.
     *
     * @param colaboradorPJDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ColaboradorPJDTO> partialUpdate(ColaboradorPJDTO colaboradorPJDTO) {
        LOG.debug("Request to partially update ColaboradorPJ : {}", colaboradorPJDTO);

        return colaboradorPJRepository
            .findById(colaboradorPJDTO.getId())
            .map(existingColaboradorPJ -> {
                colaboradorPJMapper.partialUpdate(existingColaboradorPJ, colaboradorPJDTO);

                return existingColaboradorPJ;
            })
            .map(colaboradorPJRepository::save)
            .map(colaboradorPJMapper::toDto);
    }

    /**
     * Get all the colaboradorPJS.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ColaboradorPJDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ColaboradorPJS");
        return colaboradorPJRepository.findAll(pageable).map(colaboradorPJMapper::toDto);
    }

    /**
     * Get all the colaboradorPJS with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ColaboradorPJDTO> findAllWithEagerRelationships(Pageable pageable) {
        return colaboradorPJRepository.findAllWithEagerRelationships(pageable).map(colaboradorPJMapper::toDto);
    }

    /**
     * Get one colaboradorPJ by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ColaboradorPJDTO> findOne(Long id) {
        LOG.debug("Request to get ColaboradorPJ : {}", id);
        return colaboradorPJRepository.findOneWithEagerRelationships(id).map(colaboradorPJMapper::toDto);
    }

    /**
     * Delete the colaboradorPJ by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ColaboradorPJ : {}", id);
        colaboradorPJRepository.deleteById(id);
    }
}
