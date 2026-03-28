package com.bankernel.service;

import com.bankernel.domain.PermissaoColaborador;
import com.bankernel.repository.PermissaoColaboradorRepository;
import com.bankernel.service.dto.PermissaoColaboradorDTO;
import com.bankernel.service.mapper.PermissaoColaboradorMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.PermissaoColaborador}.
 */
@Service
@Transactional
public class PermissaoColaboradorService {

    private static final Logger LOG = LoggerFactory.getLogger(PermissaoColaboradorService.class);

    private final PermissaoColaboradorRepository permissaoColaboradorRepository;

    private final PermissaoColaboradorMapper permissaoColaboradorMapper;

    public PermissaoColaboradorService(
        PermissaoColaboradorRepository permissaoColaboradorRepository,
        PermissaoColaboradorMapper permissaoColaboradorMapper
    ) {
        this.permissaoColaboradorRepository = permissaoColaboradorRepository;
        this.permissaoColaboradorMapper = permissaoColaboradorMapper;
    }

    /**
     * Save a permissaoColaborador.
     *
     * @param permissaoColaboradorDTO the entity to save.
     * @return the persisted entity.
     */
    public PermissaoColaboradorDTO save(PermissaoColaboradorDTO permissaoColaboradorDTO) {
        LOG.debug("Request to save PermissaoColaborador : {}", permissaoColaboradorDTO);
        PermissaoColaborador permissaoColaborador = permissaoColaboradorMapper.toEntity(permissaoColaboradorDTO);
        permissaoColaborador = permissaoColaboradorRepository.save(permissaoColaborador);
        return permissaoColaboradorMapper.toDto(permissaoColaborador);
    }

    /**
     * Update a permissaoColaborador.
     *
     * @param permissaoColaboradorDTO the entity to save.
     * @return the persisted entity.
     */
    public PermissaoColaboradorDTO update(PermissaoColaboradorDTO permissaoColaboradorDTO) {
        LOG.debug("Request to update PermissaoColaborador : {}", permissaoColaboradorDTO);
        PermissaoColaborador permissaoColaborador = permissaoColaboradorMapper.toEntity(permissaoColaboradorDTO);
        permissaoColaborador = permissaoColaboradorRepository.save(permissaoColaborador);
        return permissaoColaboradorMapper.toDto(permissaoColaborador);
    }

    /**
     * Partially update a permissaoColaborador.
     *
     * @param permissaoColaboradorDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PermissaoColaboradorDTO> partialUpdate(PermissaoColaboradorDTO permissaoColaboradorDTO) {
        LOG.debug("Request to partially update PermissaoColaborador : {}", permissaoColaboradorDTO);

        return permissaoColaboradorRepository
            .findById(permissaoColaboradorDTO.getId())
            .map(existingPermissaoColaborador -> {
                permissaoColaboradorMapper.partialUpdate(existingPermissaoColaborador, permissaoColaboradorDTO);

                return existingPermissaoColaborador;
            })
            .map(permissaoColaboradorRepository::save)
            .map(permissaoColaboradorMapper::toDto);
    }

    /**
     * Get all the permissaoColaboradors.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PermissaoColaboradorDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all PermissaoColaboradors");
        return permissaoColaboradorRepository.findAll(pageable).map(permissaoColaboradorMapper::toDto);
    }

    /**
     * Get all the permissaoColaboradors with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PermissaoColaboradorDTO> findAllWithEagerRelationships(Pageable pageable) {
        return permissaoColaboradorRepository.findAllWithEagerRelationships(pageable).map(permissaoColaboradorMapper::toDto);
    }

    /**
     * Get one permissaoColaborador by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PermissaoColaboradorDTO> findOne(Long id) {
        LOG.debug("Request to get PermissaoColaborador : {}", id);
        return permissaoColaboradorRepository.findOneWithEagerRelationships(id).map(permissaoColaboradorMapper::toDto);
    }

    /**
     * Delete the permissaoColaborador by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PermissaoColaborador : {}", id);
        permissaoColaboradorRepository.deleteById(id);
    }
}
