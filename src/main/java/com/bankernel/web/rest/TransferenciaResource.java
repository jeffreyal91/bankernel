package com.bankernel.web.rest;

import com.bankernel.repository.TransferenciaRepository;
import com.bankernel.service.TransferenciaQueryService;
import com.bankernel.service.TransferenciaService;
import com.bankernel.service.criteria.TransferenciaCriteria;
import com.bankernel.service.dto.TransferenciaDTO;
import com.bankernel.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bankernel.domain.Transferencia}.
 */
@RestController
@RequestMapping("/api/transferencias")
public class TransferenciaResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransferenciaResource.class);

    private static final String ENTITY_NAME = "transferencia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransferenciaService transferenciaService;

    private final TransferenciaRepository transferenciaRepository;

    private final TransferenciaQueryService transferenciaQueryService;

    public TransferenciaResource(
        TransferenciaService transferenciaService,
        TransferenciaRepository transferenciaRepository,
        TransferenciaQueryService transferenciaQueryService
    ) {
        this.transferenciaService = transferenciaService;
        this.transferenciaRepository = transferenciaRepository;
        this.transferenciaQueryService = transferenciaQueryService;
    }

    /**
     * {@code POST  /transferencias} : Create a new transferencia.
     *
     * @param transferenciaDTO the transferenciaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transferenciaDTO, or with status {@code 400 (Bad Request)} if the transferencia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransferenciaDTO> createTransferencia(@Valid @RequestBody TransferenciaDTO transferenciaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save Transferencia : {}", transferenciaDTO);
        if (transferenciaDTO.getId() != null) {
            throw new BadRequestAlertException("A new transferencia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transferenciaDTO = transferenciaService.save(transferenciaDTO);
        return ResponseEntity.created(new URI("/api/transferencias/" + transferenciaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transferenciaDTO.getId().toString()))
            .body(transferenciaDTO);
    }

    /**
     * {@code PUT  /transferencias/:id} : Updates an existing transferencia.
     *
     * @param id the id of the transferenciaDTO to save.
     * @param transferenciaDTO the transferenciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transferenciaDTO,
     * or with status {@code 400 (Bad Request)} if the transferenciaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transferenciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransferenciaDTO> updateTransferencia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransferenciaDTO transferenciaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Transferencia : {}, {}", id, transferenciaDTO);
        if (transferenciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transferenciaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transferenciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transferenciaDTO = transferenciaService.update(transferenciaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transferenciaDTO.getId().toString()))
            .body(transferenciaDTO);
    }

    /**
     * {@code PATCH  /transferencias/:id} : Partial updates given fields of an existing transferencia, field will ignore if it is null
     *
     * @param id the id of the transferenciaDTO to save.
     * @param transferenciaDTO the transferenciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transferenciaDTO,
     * or with status {@code 400 (Bad Request)} if the transferenciaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transferenciaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transferenciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransferenciaDTO> partialUpdateTransferencia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransferenciaDTO transferenciaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Transferencia partially : {}, {}", id, transferenciaDTO);
        if (transferenciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transferenciaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transferenciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransferenciaDTO> result = transferenciaService.partialUpdate(transferenciaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transferenciaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transferencias} : get all the transferencias.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transferencias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransferenciaDTO>> getAllTransferencias(
        TransferenciaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Transferencias by criteria: {}", criteria);

        Page<TransferenciaDTO> page = transferenciaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transferencias/count} : count all the transferencias.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTransferencias(TransferenciaCriteria criteria) {
        LOG.debug("REST request to count Transferencias by criteria: {}", criteria);
        return ResponseEntity.ok().body(transferenciaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transferencias/:id} : get the "id" transferencia.
     *
     * @param id the id of the transferenciaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transferenciaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransferenciaDTO> getTransferencia(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Transferencia : {}", id);
        Optional<TransferenciaDTO> transferenciaDTO = transferenciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transferenciaDTO);
    }

    /**
     * {@code DELETE  /transferencias/:id} : delete the "id" transferencia.
     *
     * @param id the id of the transferenciaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransferencia(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Transferencia : {}", id);
        transferenciaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
