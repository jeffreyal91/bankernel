package com.bankernel.service;

import com.bankernel.domain.LinkCobranca;
import com.bankernel.repository.LinkCobrancaRepository;
import com.bankernel.service.dto.LinkCobrancaDTO;
import com.bankernel.service.mapper.LinkCobrancaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.LinkCobranca}.
 */
@Service
@Transactional
public class LinkCobrancaService {

    private static final Logger LOG = LoggerFactory.getLogger(LinkCobrancaService.class);

    private final LinkCobrancaRepository linkCobrancaRepository;

    private final LinkCobrancaMapper linkCobrancaMapper;

    public LinkCobrancaService(LinkCobrancaRepository linkCobrancaRepository, LinkCobrancaMapper linkCobrancaMapper) {
        this.linkCobrancaRepository = linkCobrancaRepository;
        this.linkCobrancaMapper = linkCobrancaMapper;
    }

    /**
     * Save a linkCobranca.
     *
     * @param linkCobrancaDTO the entity to save.
     * @return the persisted entity.
     */
    public LinkCobrancaDTO save(LinkCobrancaDTO linkCobrancaDTO) {
        LOG.debug("Request to save LinkCobranca : {}", linkCobrancaDTO);
        LinkCobranca linkCobranca = linkCobrancaMapper.toEntity(linkCobrancaDTO);
        linkCobranca = linkCobrancaRepository.save(linkCobranca);
        return linkCobrancaMapper.toDto(linkCobranca);
    }

    /**
     * Update a linkCobranca.
     *
     * @param linkCobrancaDTO the entity to save.
     * @return the persisted entity.
     */
    public LinkCobrancaDTO update(LinkCobrancaDTO linkCobrancaDTO) {
        LOG.debug("Request to update LinkCobranca : {}", linkCobrancaDTO);
        LinkCobranca linkCobranca = linkCobrancaMapper.toEntity(linkCobrancaDTO);
        linkCobranca = linkCobrancaRepository.save(linkCobranca);
        return linkCobrancaMapper.toDto(linkCobranca);
    }

    /**
     * Partially update a linkCobranca.
     *
     * @param linkCobrancaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<LinkCobrancaDTO> partialUpdate(LinkCobrancaDTO linkCobrancaDTO) {
        LOG.debug("Request to partially update LinkCobranca : {}", linkCobrancaDTO);

        return linkCobrancaRepository
            .findById(linkCobrancaDTO.getId())
            .map(existingLinkCobranca -> {
                linkCobrancaMapper.partialUpdate(existingLinkCobranca, linkCobrancaDTO);

                return existingLinkCobranca;
            })
            .map(linkCobrancaRepository::save)
            .map(linkCobrancaMapper::toDto);
    }

    /**
     * Get all the linkCobrancas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<LinkCobrancaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all LinkCobrancas");
        return linkCobrancaRepository.findAll(pageable).map(linkCobrancaMapper::toDto);
    }

    /**
     * Get all the linkCobrancas with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<LinkCobrancaDTO> findAllWithEagerRelationships(Pageable pageable) {
        return linkCobrancaRepository.findAllWithEagerRelationships(pageable).map(linkCobrancaMapper::toDto);
    }

    /**
     * Get one linkCobranca by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<LinkCobrancaDTO> findOne(Long id) {
        LOG.debug("Request to get LinkCobranca : {}", id);
        return linkCobrancaRepository.findOneWithEagerRelationships(id).map(linkCobrancaMapper::toDto);
    }

    /**
     * Delete the linkCobranca by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete LinkCobranca : {}", id);
        linkCobrancaRepository.deleteById(id);
    }
}
