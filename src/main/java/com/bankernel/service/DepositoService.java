package com.bankernel.service;

import com.bankernel.domain.Deposito;
import com.bankernel.repository.DepositoRepository;
import com.bankernel.service.dto.DepositoDTO;
import com.bankernel.service.mapper.DepositoMapper;
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
 * Service Implementation for managing {@link com.bankernel.domain.Deposito}.
 */
@Service
@Transactional
public class DepositoService {

    private static final Logger LOG = LoggerFactory.getLogger(DepositoService.class);

    private final DepositoRepository depositoRepository;

    private final DepositoMapper depositoMapper;

    public DepositoService(DepositoRepository depositoRepository, DepositoMapper depositoMapper) {
        this.depositoRepository = depositoRepository;
        this.depositoMapper = depositoMapper;
    }

    /**
     * Save a deposito.
     *
     * @param depositoDTO the entity to save.
     * @return the persisted entity.
     */
    public DepositoDTO save(DepositoDTO depositoDTO) {
        LOG.debug("Request to save Deposito : {}", depositoDTO);
        Deposito deposito = depositoMapper.toEntity(depositoDTO);
        deposito = depositoRepository.save(deposito);
        return depositoMapper.toDto(deposito);
    }

    /**
     * Update a deposito.
     *
     * @param depositoDTO the entity to save.
     * @return the persisted entity.
     */
    public DepositoDTO update(DepositoDTO depositoDTO) {
        LOG.debug("Request to update Deposito : {}", depositoDTO);
        Deposito deposito = depositoMapper.toEntity(depositoDTO);
        deposito = depositoRepository.save(deposito);
        return depositoMapper.toDto(deposito);
    }

    /**
     * Partially update a deposito.
     *
     * @param depositoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DepositoDTO> partialUpdate(DepositoDTO depositoDTO) {
        LOG.debug("Request to partially update Deposito : {}", depositoDTO);

        return depositoRepository
            .findById(depositoDTO.getId())
            .map(existingDeposito -> {
                depositoMapper.partialUpdate(existingDeposito, depositoDTO);

                return existingDeposito;
            })
            .map(depositoRepository::save)
            .map(depositoMapper::toDto);
    }

    /**
     * Get all the depositos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<DepositoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return depositoRepository.findAllWithEagerRelationships(pageable).map(depositoMapper::toDto);
    }

    /**
     *  Get all the depositos where DepositoPix is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DepositoDTO> findAllWhereDepositoPixIsNull() {
        LOG.debug("Request to get all depositos where DepositoPix is null");
        return StreamSupport.stream(depositoRepository.findAll().spliterator(), false)
            .filter(deposito -> deposito.getDepositoPix() == null)
            .map(depositoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the depositos where DepositoBoleto is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DepositoDTO> findAllWhereDepositoBoletoIsNull() {
        LOG.debug("Request to get all depositos where DepositoBoleto is null");
        return StreamSupport.stream(depositoRepository.findAll().spliterator(), false)
            .filter(deposito -> deposito.getDepositoBoleto() == null)
            .map(depositoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one deposito by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DepositoDTO> findOne(Long id) {
        LOG.debug("Request to get Deposito : {}", id);
        return depositoRepository.findOneWithEagerRelationships(id).map(depositoMapper::toDto);
    }

    /**
     * Delete the deposito by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Deposito : {}", id);
        depositoRepository.deleteById(id);
    }
}
