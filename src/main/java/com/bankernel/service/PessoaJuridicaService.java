package com.bankernel.service;

import com.bankernel.domain.PessoaJuridica;
import com.bankernel.repository.PessoaJuridicaRepository;
import com.bankernel.service.dto.PessoaJuridicaDTO;
import com.bankernel.service.mapper.PessoaJuridicaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.PessoaJuridica}.
 */
@Service
@Transactional
public class PessoaJuridicaService {

    private static final Logger LOG = LoggerFactory.getLogger(PessoaJuridicaService.class);

    private final PessoaJuridicaRepository pessoaJuridicaRepository;

    private final PessoaJuridicaMapper pessoaJuridicaMapper;

    public PessoaJuridicaService(PessoaJuridicaRepository pessoaJuridicaRepository, PessoaJuridicaMapper pessoaJuridicaMapper) {
        this.pessoaJuridicaRepository = pessoaJuridicaRepository;
        this.pessoaJuridicaMapper = pessoaJuridicaMapper;
    }

    /**
     * Save a pessoaJuridica.
     *
     * @param pessoaJuridicaDTO the entity to save.
     * @return the persisted entity.
     */
    public PessoaJuridicaDTO save(PessoaJuridicaDTO pessoaJuridicaDTO) {
        LOG.debug("Request to save PessoaJuridica : {}", pessoaJuridicaDTO);
        PessoaJuridica pessoaJuridica = pessoaJuridicaMapper.toEntity(pessoaJuridicaDTO);
        pessoaJuridica = pessoaJuridicaRepository.save(pessoaJuridica);
        return pessoaJuridicaMapper.toDto(pessoaJuridica);
    }

    /**
     * Update a pessoaJuridica.
     *
     * @param pessoaJuridicaDTO the entity to save.
     * @return the persisted entity.
     */
    public PessoaJuridicaDTO update(PessoaJuridicaDTO pessoaJuridicaDTO) {
        LOG.debug("Request to update PessoaJuridica : {}", pessoaJuridicaDTO);
        PessoaJuridica pessoaJuridica = pessoaJuridicaMapper.toEntity(pessoaJuridicaDTO);
        pessoaJuridica = pessoaJuridicaRepository.save(pessoaJuridica);
        return pessoaJuridicaMapper.toDto(pessoaJuridica);
    }

    /**
     * Partially update a pessoaJuridica.
     *
     * @param pessoaJuridicaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PessoaJuridicaDTO> partialUpdate(PessoaJuridicaDTO pessoaJuridicaDTO) {
        LOG.debug("Request to partially update PessoaJuridica : {}", pessoaJuridicaDTO);

        return pessoaJuridicaRepository
            .findById(pessoaJuridicaDTO.getId())
            .map(existingPessoaJuridica -> {
                pessoaJuridicaMapper.partialUpdate(existingPessoaJuridica, pessoaJuridicaDTO);

                return existingPessoaJuridica;
            })
            .map(pessoaJuridicaRepository::save)
            .map(pessoaJuridicaMapper::toDto);
    }

    /**
     * Get all the pessoaJuridicas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<PessoaJuridicaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return pessoaJuridicaRepository.findAllWithEagerRelationships(pageable).map(pessoaJuridicaMapper::toDto);
    }

    /**
     * Get one pessoaJuridica by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PessoaJuridicaDTO> findOne(Long id) {
        LOG.debug("Request to get PessoaJuridica : {}", id);
        return pessoaJuridicaRepository.findOneWithEagerRelationships(id).map(pessoaJuridicaMapper::toDto);
    }

    /**
     * Delete the pessoaJuridica by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete PessoaJuridica : {}", id);
        pessoaJuridicaRepository.deleteById(id);
    }
}
