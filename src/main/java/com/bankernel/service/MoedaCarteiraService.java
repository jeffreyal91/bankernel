package com.bankernel.service;

import com.bankernel.domain.MoedaCarteira;
import com.bankernel.repository.MoedaCarteiraRepository;
import com.bankernel.service.dto.MoedaCarteiraDTO;
import com.bankernel.service.mapper.MoedaCarteiraMapper;
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
 * Service Implementation for managing {@link com.bankernel.domain.MoedaCarteira}.
 */
@Service
@Transactional
public class MoedaCarteiraService {

    private static final Logger LOG = LoggerFactory.getLogger(MoedaCarteiraService.class);

    private final MoedaCarteiraRepository moedaCarteiraRepository;

    private final MoedaCarteiraMapper moedaCarteiraMapper;

    public MoedaCarteiraService(MoedaCarteiraRepository moedaCarteiraRepository, MoedaCarteiraMapper moedaCarteiraMapper) {
        this.moedaCarteiraRepository = moedaCarteiraRepository;
        this.moedaCarteiraMapper = moedaCarteiraMapper;
    }

    /**
     * Save a moedaCarteira.
     *
     * @param moedaCarteiraDTO the entity to save.
     * @return the persisted entity.
     */
    public MoedaCarteiraDTO save(MoedaCarteiraDTO moedaCarteiraDTO) {
        LOG.debug("Request to save MoedaCarteira : {}", moedaCarteiraDTO);
        MoedaCarteira moedaCarteira = moedaCarteiraMapper.toEntity(moedaCarteiraDTO);
        moedaCarteira = moedaCarteiraRepository.save(moedaCarteira);
        return moedaCarteiraMapper.toDto(moedaCarteira);
    }

    /**
     * Update a moedaCarteira.
     *
     * @param moedaCarteiraDTO the entity to save.
     * @return the persisted entity.
     */
    public MoedaCarteiraDTO update(MoedaCarteiraDTO moedaCarteiraDTO) {
        LOG.debug("Request to update MoedaCarteira : {}", moedaCarteiraDTO);
        MoedaCarteira moedaCarteira = moedaCarteiraMapper.toEntity(moedaCarteiraDTO);
        moedaCarteira = moedaCarteiraRepository.save(moedaCarteira);
        return moedaCarteiraMapper.toDto(moedaCarteira);
    }

    /**
     * Partially update a moedaCarteira.
     *
     * @param moedaCarteiraDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<MoedaCarteiraDTO> partialUpdate(MoedaCarteiraDTO moedaCarteiraDTO) {
        LOG.debug("Request to partially update MoedaCarteira : {}", moedaCarteiraDTO);

        return moedaCarteiraRepository
            .findById(moedaCarteiraDTO.getId())
            .map(existingMoedaCarteira -> {
                moedaCarteiraMapper.partialUpdate(existingMoedaCarteira, moedaCarteiraDTO);

                return existingMoedaCarteira;
            })
            .map(moedaCarteiraRepository::save)
            .map(moedaCarteiraMapper::toDto);
    }

    /**
     * Get all the moedaCarteiras.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<MoedaCarteiraDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all MoedaCarteiras");
        return moedaCarteiraRepository.findAll(pageable).map(moedaCarteiraMapper::toDto);
    }

    /**
     * Get all the moedaCarteiras with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<MoedaCarteiraDTO> findAllWithEagerRelationships(Pageable pageable) {
        return moedaCarteiraRepository.findAllWithEagerRelationships(pageable).map(moedaCarteiraMapper::toDto);
    }

    /**
     *  Get all the moedaCarteiras where PessoaFisica is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MoedaCarteiraDTO> findAllWherePessoaFisicaIsNull() {
        LOG.debug("Request to get all moedaCarteiras where PessoaFisica is null");
        return StreamSupport.stream(moedaCarteiraRepository.findAll().spliterator(), false)
            .filter(moedaCarteira -> moedaCarteira.getPessoaFisica() == null)
            .map(moedaCarteiraMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the moedaCarteiras where PessoaJuridica is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MoedaCarteiraDTO> findAllWherePessoaJuridicaIsNull() {
        LOG.debug("Request to get all moedaCarteiras where PessoaJuridica is null");
        return StreamSupport.stream(moedaCarteiraRepository.findAll().spliterator(), false)
            .filter(moedaCarteira -> moedaCarteira.getPessoaJuridica() == null)
            .map(moedaCarteiraMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one moedaCarteira by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MoedaCarteiraDTO> findOne(Long id) {
        LOG.debug("Request to get MoedaCarteira : {}", id);
        return moedaCarteiraRepository.findOneWithEagerRelationships(id).map(moedaCarteiraMapper::toDto);
    }

    /**
     * Delete the moedaCarteira by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete MoedaCarteira : {}", id);
        moedaCarteiraRepository.deleteById(id);
    }
}
