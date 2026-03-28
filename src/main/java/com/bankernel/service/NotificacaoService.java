package com.bankernel.service;

import com.bankernel.domain.Notificacao;
import com.bankernel.repository.NotificacaoRepository;
import com.bankernel.service.dto.NotificacaoDTO;
import com.bankernel.service.mapper.NotificacaoMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.bankernel.domain.Notificacao}.
 */
@Service
@Transactional
public class NotificacaoService {

    private static final Logger LOG = LoggerFactory.getLogger(NotificacaoService.class);

    private final NotificacaoRepository notificacaoRepository;

    private final NotificacaoMapper notificacaoMapper;

    public NotificacaoService(NotificacaoRepository notificacaoRepository, NotificacaoMapper notificacaoMapper) {
        this.notificacaoRepository = notificacaoRepository;
        this.notificacaoMapper = notificacaoMapper;
    }

    /**
     * Save a notificacao.
     *
     * @param notificacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificacaoDTO save(NotificacaoDTO notificacaoDTO) {
        LOG.debug("Request to save Notificacao : {}", notificacaoDTO);
        Notificacao notificacao = notificacaoMapper.toEntity(notificacaoDTO);
        notificacao = notificacaoRepository.save(notificacao);
        return notificacaoMapper.toDto(notificacao);
    }

    /**
     * Update a notificacao.
     *
     * @param notificacaoDTO the entity to save.
     * @return the persisted entity.
     */
    public NotificacaoDTO update(NotificacaoDTO notificacaoDTO) {
        LOG.debug("Request to update Notificacao : {}", notificacaoDTO);
        Notificacao notificacao = notificacaoMapper.toEntity(notificacaoDTO);
        notificacao = notificacaoRepository.save(notificacao);
        return notificacaoMapper.toDto(notificacao);
    }

    /**
     * Partially update a notificacao.
     *
     * @param notificacaoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NotificacaoDTO> partialUpdate(NotificacaoDTO notificacaoDTO) {
        LOG.debug("Request to partially update Notificacao : {}", notificacaoDTO);

        return notificacaoRepository
            .findById(notificacaoDTO.getId())
            .map(existingNotificacao -> {
                notificacaoMapper.partialUpdate(existingNotificacao, notificacaoDTO);

                return existingNotificacao;
            })
            .map(notificacaoRepository::save)
            .map(notificacaoMapper::toDto);
    }

    /**
     * Get all the notificacaos with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<NotificacaoDTO> findAllWithEagerRelationships(Pageable pageable) {
        return notificacaoRepository.findAllWithEagerRelationships(pageable).map(notificacaoMapper::toDto);
    }

    /**
     * Get one notificacao by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NotificacaoDTO> findOne(Long id) {
        LOG.debug("Request to get Notificacao : {}", id);
        return notificacaoRepository.findOneWithEagerRelationships(id).map(notificacaoMapper::toDto);
    }

    /**
     * Delete the notificacao by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Notificacao : {}", id);
        notificacaoRepository.deleteById(id);
    }
}
