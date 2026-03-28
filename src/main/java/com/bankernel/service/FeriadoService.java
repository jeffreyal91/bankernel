package com.bankernel.service;

import com.bankernel.domain.Feriado;
import com.bankernel.repository.FeriadoRepository;
import com.bankernel.service.dto.FeriadoDTO;
import com.bankernel.service.mapper.FeriadoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.Feriado}.
 */
@Service
@Transactional
public class FeriadoService {

    private static final Logger LOG = LoggerFactory.getLogger(FeriadoService.class);

    private final FeriadoRepository feriadoRepository;

    private final FeriadoMapper feriadoMapper;

    public FeriadoService(FeriadoRepository feriadoRepository, FeriadoMapper feriadoMapper) {
        this.feriadoRepository = feriadoRepository;
        this.feriadoMapper = feriadoMapper;
    }

    /**
     * Save a feriado.
     *
     * @param feriadoDTO the entity to save.
     * @return the persisted entity.
     */
    public FeriadoDTO save(FeriadoDTO feriadoDTO) {
        LOG.debug("Request to save Feriado : {}", feriadoDTO);
        Feriado feriado = feriadoMapper.toEntity(feriadoDTO);
        feriado = feriadoRepository.save(feriado);
        return feriadoMapper.toDto(feriado);
    }

    /**
     * Update a feriado.
     *
     * @param feriadoDTO the entity to save.
     * @return the persisted entity.
     */
    public FeriadoDTO update(FeriadoDTO feriadoDTO) {
        LOG.debug("Request to update Feriado : {}", feriadoDTO);
        Feriado feriado = feriadoMapper.toEntity(feriadoDTO);
        feriado = feriadoRepository.save(feriado);
        return feriadoMapper.toDto(feriado);
    }

    /**
     * Partially update a feriado.
     *
     * @param feriadoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FeriadoDTO> partialUpdate(FeriadoDTO feriadoDTO) {
        LOG.debug("Request to partially update Feriado : {}", feriadoDTO);

        return feriadoRepository
            .findById(feriadoDTO.getId())
            .map(existingFeriado -> {
                feriadoMapper.partialUpdate(existingFeriado, feriadoDTO);

                return existingFeriado;
            })
            .map(feriadoRepository::save)
            .map(feriadoMapper::toDto);
    }

    /**
     * Get all the feriados.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<FeriadoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Feriados");
        return feriadoRepository.findAll(pageable).map(feriadoMapper::toDto);
    }

    /**
     * Get one feriado by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FeriadoDTO> findOne(Long id) {
        LOG.debug("Request to get Feriado : {}", id);
        return feriadoRepository.findById(id).map(feriadoMapper::toDto);
    }

    /**
     * Delete the feriado by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Feriado : {}", id);
        feriadoRepository.deleteById(id);
    }
}
