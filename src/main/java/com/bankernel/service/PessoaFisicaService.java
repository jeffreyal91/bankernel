package com.bankernel.service;

import com.bankernel.domain.PessoaFisica;
import com.bankernel.repository.PessoaFisicaRepository;
import com.bankernel.service.dto.PessoaFisicaDTO;
import com.bankernel.service.mapper.PessoaFisicaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.PessoaFisica}.
 */
@Service
@Transactional
public class PessoaFisicaService {

    private static final Logger LOG = LoggerFactory.getLogger(PessoaFisicaService.class);

    private final PessoaFisicaRepository pessoaFisicaRepository;

    private final PessoaFisicaMapper pessoaFisicaMapper;

    public PessoaFisicaService(PessoaFisicaRepository pessoaFisicaRepository, PessoaFisicaMapper pessoaFisicaMapper) {
        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.pessoaFisicaMapper = pessoaFisicaMapper;
    }

    /**
     * Save a pessoaFisica.
     *
     * @param pessoaFisicaDTO the entity to save.
     * @return the persisted entity.
     */
    public PessoaFisicaDTO save(PessoaFisicaDTO pessoaFisicaDTO) {
        LOG.debug("Request to save PessoaFisica : {}", pessoaFisicaDTO);
        PessoaFisica pessoaFisica = pessoaFisicaMapper.toEntity(pessoaFisicaDTO);
        pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);
        return pessoaFisicaMapper.toDto(pessoaFisica);
    }

    /**
     * Update a pessoaFisica.
     *
     * @param pessoaFisicaDTO the entity to save.
     * @return the persisted entity.
     */
    public PessoaFisicaDTO update(PessoaFisicaDTO pessoaFisicaDTO) {
        LOG.debug("Request to update PessoaFisica : {}", pessoaFisicaDTO);
        PessoaFisica pessoaFisica = pessoaFisicaMapper.toEntity(pessoaFisicaDTO);
        pessoaFisica = pessoaFisicaRepository.save(pessoaFisica);
        return pessoaFisicaMapper.toDto(pessoaFisica);
    }

    /**
     * Partially update a pessoaFisica.
     *
     * @param pessoaFisicaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PessoaFisicaDTO> partialUpdate(PessoaFisicaDTO pessoaFisicaDTO) {
        LOG.debug("Request to partially update PessoaFisica : {}", pessoaFisicaDTO);

        return pessoaFisicaRepository
            .findById(pessoaFisicaDTO.getId())
            .map(existingPessoaFisica -> {
                pessoaFisicaMapper.partialUpdate(existingPessoaFisica, pessoaFisicaDTO);

                return existingPessoaFisica;
            })
            .map(pessoaFisicaRepository::save)
            .map(pessoaFisicaMapper::toDto);
    }

    /**
     * Get all the pessoaFisicas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PessoaFisicaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pessoaFisicaRepository.findAllWithEagerRelationships(pageable).map(pessoaFisicaMapper::toDto);
    }

    /**
     * Get one pessoaFisica by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PessoaFisicaDTO> findOne(Long id) {
        LOG.debug("Request to get PessoaFisica : {}", id);
        return pessoaFisicaRepository.findOneWithEagerRelationships(id).map(pessoaFisicaMapper::toDto);
    }

    /**
     * Delete the pessoaFisica by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PessoaFisica : {}", id);
        pessoaFisicaRepository.deleteById(id);
    }
}
