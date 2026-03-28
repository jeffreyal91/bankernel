package com.bankernel.service;

import com.bankernel.domain.Carteira;
import com.bankernel.repository.CarteiraRepository;
import com.bankernel.service.dto.CarteiraDTO;
import com.bankernel.service.mapper.CarteiraMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.Carteira}.
 */
@Service
@Transactional
public class CarteiraService {

    private static final Logger LOG = LoggerFactory.getLogger(CarteiraService.class);

    private final CarteiraRepository carteiraRepository;

    private final CarteiraMapper carteiraMapper;

    public CarteiraService(CarteiraRepository carteiraRepository, CarteiraMapper carteiraMapper) {
        this.carteiraRepository = carteiraRepository;
        this.carteiraMapper = carteiraMapper;
    }

    /**
     * Save a carteira.
     *
     * @param carteiraDTO the entity to save.
     * @return the persisted entity.
     */
    public CarteiraDTO save(CarteiraDTO carteiraDTO) {
        LOG.debug("Request to save Carteira : {}", carteiraDTO);
        Carteira carteira = carteiraMapper.toEntity(carteiraDTO);
        carteira = carteiraRepository.save(carteira);
        return carteiraMapper.toDto(carteira);
    }

    /**
     * Update a carteira.
     *
     * @param carteiraDTO the entity to save.
     * @return the persisted entity.
     */
    public CarteiraDTO update(CarteiraDTO carteiraDTO) {
        LOG.debug("Request to update Carteira : {}", carteiraDTO);
        Carteira carteira = carteiraMapper.toEntity(carteiraDTO);
        carteira = carteiraRepository.save(carteira);
        return carteiraMapper.toDto(carteira);
    }

    /**
     * Partially update a carteira.
     *
     * @param carteiraDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CarteiraDTO> partialUpdate(CarteiraDTO carteiraDTO) {
        LOG.debug("Request to partially update Carteira : {}", carteiraDTO);

        return carteiraRepository
            .findById(carteiraDTO.getId())
            .map(existingCarteira -> {
                carteiraMapper.partialUpdate(existingCarteira, carteiraDTO);

                return existingCarteira;
            })
            .map(carteiraRepository::save)
            .map(carteiraMapper::toDto);
    }

    /**
     * Get all the carteiras with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CarteiraDTO> findAllWithEagerRelationships(Pageable pageable) {
        return carteiraRepository.findAllWithEagerRelationships(pageable).map(carteiraMapper::toDto);
    }

    /**
     * Get one carteira by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CarteiraDTO> findOne(Long id) {
        LOG.debug("Request to get Carteira : {}", id);
        return carteiraRepository.findOneWithEagerRelationships(id).map(carteiraMapper::toDto);
    }

    /**
     * Delete the carteira by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Carteira : {}", id);
        carteiraRepository.deleteById(id);
    }
}
