package com.bankernel.service;

import com.bankernel.domain.Saque;
import com.bankernel.repository.SaqueRepository;
import com.bankernel.service.dto.SaqueDTO;
import com.bankernel.service.mapper.SaqueMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.Saque}.
 */
@Service
@Transactional
public class SaqueService {

    private static final Logger LOG = LoggerFactory.getLogger(SaqueService.class);

    private final SaqueRepository saqueRepository;

    private final SaqueMapper saqueMapper;

    public SaqueService(SaqueRepository saqueRepository, SaqueMapper saqueMapper) {
        this.saqueRepository = saqueRepository;
        this.saqueMapper = saqueMapper;
    }

    /**
     * Save a saque.
     *
     * @param saqueDTO the entity to save.
     * @return the persisted entity.
     */
    public SaqueDTO save(SaqueDTO saqueDTO) {
        LOG.debug("Request to save Saque : {}", saqueDTO);
        Saque saque = saqueMapper.toEntity(saqueDTO);
        saque = saqueRepository.save(saque);
        return saqueMapper.toDto(saque);
    }

    /**
     * Update a saque.
     *
     * @param saqueDTO the entity to save.
     * @return the persisted entity.
     */
    public SaqueDTO update(SaqueDTO saqueDTO) {
        LOG.debug("Request to update Saque : {}", saqueDTO);
        Saque saque = saqueMapper.toEntity(saqueDTO);
        saque = saqueRepository.save(saque);
        return saqueMapper.toDto(saque);
    }

    /**
     * Partially update a saque.
     *
     * @param saqueDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SaqueDTO> partialUpdate(SaqueDTO saqueDTO) {
        LOG.debug("Request to partially update Saque : {}", saqueDTO);

        return saqueRepository
            .findById(saqueDTO.getId())
            .map(existingSaque -> {
                saqueMapper.partialUpdate(existingSaque, saqueDTO);

                return existingSaque;
            })
            .map(saqueRepository::save)
            .map(saqueMapper::toDto);
    }

    /**
     * Get all the saques with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<SaqueDTO> findAllWithEagerRelationships(Pageable pageable) {
        return saqueRepository.findAllWithEagerRelationships(pageable).map(saqueMapper::toDto);
    }

    /**
     *  Get all the saques where SaquePix is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SaqueDTO> findAllWhereSaquePixIsNull() {
        LOG.debug("Request to get all saques where SaquePix is null");
        return StreamSupport.stream(saqueRepository.findAll().spliterator(), false)
            .filter(saque -> saque.getSaquePix() == null)
            .map(saqueMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the saques where SaqueBoleto is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SaqueDTO> findAllWhereSaqueBoletoIsNull() {
        LOG.debug("Request to get all saques where SaqueBoleto is null");
        return StreamSupport.stream(saqueRepository.findAll().spliterator(), false)
            .filter(saque -> saque.getSaqueBoleto() == null)
            .map(saqueMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one saque by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SaqueDTO> findOne(Long id) {
        LOG.debug("Request to get Saque : {}", id);
        return saqueRepository.findOneWithEagerRelationships(id).map(saqueMapper::toDto);
    }

    /**
     * Delete the saque by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Saque : {}", id);
        saqueRepository.deleteById(id);
    }
}
