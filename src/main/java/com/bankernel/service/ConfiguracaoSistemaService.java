package com.bankernel.service;

import com.bankernel.domain.ConfiguracaoSistema;
import com.bankernel.repository.ConfiguracaoSistemaRepository;
import com.bankernel.service.dto.ConfiguracaoSistemaDTO;
import com.bankernel.service.mapper.ConfiguracaoSistemaMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.ConfiguracaoSistema}.
 */
@Service
@Transactional
public class ConfiguracaoSistemaService {

    private static final Logger LOG = LoggerFactory.getLogger(ConfiguracaoSistemaService.class);

    private final ConfiguracaoSistemaRepository configuracaoSistemaRepository;

    private final ConfiguracaoSistemaMapper configuracaoSistemaMapper;

    public ConfiguracaoSistemaService(
        ConfiguracaoSistemaRepository configuracaoSistemaRepository,
        ConfiguracaoSistemaMapper configuracaoSistemaMapper
    ) {
        this.configuracaoSistemaRepository = configuracaoSistemaRepository;
        this.configuracaoSistemaMapper = configuracaoSistemaMapper;
    }

    /**
     * Save a configuracaoSistema.
     *
     * @param configuracaoSistemaDTO the entity to save.
     * @return the persisted entity.
     */
    public ConfiguracaoSistemaDTO save(ConfiguracaoSistemaDTO configuracaoSistemaDTO) {
        LOG.debug("Request to save ConfiguracaoSistema : {}", configuracaoSistemaDTO);
        ConfiguracaoSistema configuracaoSistema = configuracaoSistemaMapper.toEntity(configuracaoSistemaDTO);
        configuracaoSistema = configuracaoSistemaRepository.save(configuracaoSistema);
        return configuracaoSistemaMapper.toDto(configuracaoSistema);
    }

    /**
     * Update a configuracaoSistema.
     *
     * @param configuracaoSistemaDTO the entity to save.
     * @return the persisted entity.
     */
    public ConfiguracaoSistemaDTO update(ConfiguracaoSistemaDTO configuracaoSistemaDTO) {
        LOG.debug("Request to update ConfiguracaoSistema : {}", configuracaoSistemaDTO);
        ConfiguracaoSistema configuracaoSistema = configuracaoSistemaMapper.toEntity(configuracaoSistemaDTO);
        configuracaoSistema = configuracaoSistemaRepository.save(configuracaoSistema);
        return configuracaoSistemaMapper.toDto(configuracaoSistema);
    }

    /**
     * Partially update a configuracaoSistema.
     *
     * @param configuracaoSistemaDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ConfiguracaoSistemaDTO> partialUpdate(ConfiguracaoSistemaDTO configuracaoSistemaDTO) {
        LOG.debug("Request to partially update ConfiguracaoSistema : {}", configuracaoSistemaDTO);

        return configuracaoSistemaRepository
            .findById(configuracaoSistemaDTO.getId())
            .map(existingConfiguracaoSistema -> {
                configuracaoSistemaMapper.partialUpdate(existingConfiguracaoSistema, configuracaoSistemaDTO);

                return existingConfiguracaoSistema;
            })
            .map(configuracaoSistemaRepository::save)
            .map(configuracaoSistemaMapper::toDto);
    }

    /**
     * Get all the configuracaoSistemas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ConfiguracaoSistemaDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ConfiguracaoSistemas");
        return configuracaoSistemaRepository.findAll(pageable).map(configuracaoSistemaMapper::toDto);
    }

    /**
     * Get one configuracaoSistema by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ConfiguracaoSistemaDTO> findOne(Long id) {
        LOG.debug("Request to get ConfiguracaoSistema : {}", id);
        return configuracaoSistemaRepository.findById(id).map(configuracaoSistemaMapper::toDto);
    }

    /**
     * Delete the configuracaoSistema by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ConfiguracaoSistema : {}", id);
        configuracaoSistemaRepository.deleteById(id);
    }
}
