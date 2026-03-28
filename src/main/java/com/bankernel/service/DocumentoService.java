package com.bankernel.service;

import com.bankernel.domain.Documento;
import com.bankernel.repository.DocumentoRepository;
import com.bankernel.service.dto.DocumentoDTO;
import com.bankernel.service.mapper.DocumentoMapper;
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
 * Service Implementation for managing {@link com.bankernel.domain.Documento}.
 */
@Service
@Transactional
public class DocumentoService {

    private static final Logger LOG = LoggerFactory.getLogger(DocumentoService.class);

    private final DocumentoRepository documentoRepository;

    private final DocumentoMapper documentoMapper;

    public DocumentoService(DocumentoRepository documentoRepository, DocumentoMapper documentoMapper) {
        this.documentoRepository = documentoRepository;
        this.documentoMapper = documentoMapper;
    }

    /**
     * Save a documento.
     *
     * @param documentoDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentoDTO save(DocumentoDTO documentoDTO) {
        LOG.debug("Request to save Documento : {}", documentoDTO);
        Documento documento = documentoMapper.toEntity(documentoDTO);
        documento = documentoRepository.save(documento);
        return documentoMapper.toDto(documento);
    }

    /**
     * Update a documento.
     *
     * @param documentoDTO the entity to save.
     * @return the persisted entity.
     */
    public DocumentoDTO update(DocumentoDTO documentoDTO) {
        LOG.debug("Request to update Documento : {}", documentoDTO);
        Documento documento = documentoMapper.toEntity(documentoDTO);
        documento = documentoRepository.save(documento);
        return documentoMapper.toDto(documento);
    }

    /**
     * Partially update a documento.
     *
     * @param documentoDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<DocumentoDTO> partialUpdate(DocumentoDTO documentoDTO) {
        LOG.debug("Request to partially update Documento : {}", documentoDTO);

        return documentoRepository
            .findById(documentoDTO.getId())
            .map(existingDocumento -> {
                documentoMapper.partialUpdate(existingDocumento, documentoDTO);

                return existingDocumento;
            })
            .map(documentoRepository::save)
            .map(documentoMapper::toDto);
    }

    /**
     * Get all the documentos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<DocumentoDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Documentos");
        return documentoRepository.findAll(pageable).map(documentoMapper::toDto);
    }

    /**
     *  Get all the documentos where PessoaJuridica is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<DocumentoDTO> findAllWherePessoaJuridicaIsNull() {
        LOG.debug("Request to get all documentos where PessoaJuridica is null");
        return StreamSupport.stream(documentoRepository.findAll().spliterator(), false)
            .filter(documento -> documento.getPessoaJuridica() == null)
            .map(documentoMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one documento by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<DocumentoDTO> findOne(Long id) {
        LOG.debug("Request to get Documento : {}", id);
        return documentoRepository.findById(id).map(documentoMapper::toDto);
    }

    /**
     * Delete the documento by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Documento : {}", id);
        documentoRepository.deleteById(id);
    }
}
