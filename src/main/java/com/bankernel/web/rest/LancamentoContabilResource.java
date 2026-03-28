package com.bankernel.web.rest;

import com.bankernel.repository.LancamentoContabilRepository;
import com.bankernel.service.LancamentoContabilQueryService;
import com.bankernel.service.LancamentoContabilService;
import com.bankernel.service.criteria.LancamentoContabilCriteria;
import com.bankernel.service.dto.LancamentoContabilDTO;
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
 * REST controller for managing {@link com.bankernel.domain.LancamentoContabil}.
 */
@RestController
@RequestMapping("/api/lancamento-contabils")
public class LancamentoContabilResource {

    private static final Logger LOG = LoggerFactory.getLogger(LancamentoContabilResource.class);

    private static final String ENTITY_NAME = "lancamentoContabil";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LancamentoContabilService lancamentoContabilService;

    private final LancamentoContabilRepository lancamentoContabilRepository;

    private final LancamentoContabilQueryService lancamentoContabilQueryService;

    public LancamentoContabilResource(
        LancamentoContabilService lancamentoContabilService,
        LancamentoContabilRepository lancamentoContabilRepository,
        LancamentoContabilQueryService lancamentoContabilQueryService
    ) {
        this.lancamentoContabilService = lancamentoContabilService;
        this.lancamentoContabilRepository = lancamentoContabilRepository;
        this.lancamentoContabilQueryService = lancamentoContabilQueryService;
    }

    /**
     * {@code POST  /lancamento-contabils} : Create a new lancamentoContabil.
     *
     * @param lancamentoContabilDTO the lancamentoContabilDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new lancamentoContabilDTO, or with status {@code 400 (Bad Request)} if the lancamentoContabil has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LancamentoContabilDTO> createLancamentoContabil(@Valid @RequestBody LancamentoContabilDTO lancamentoContabilDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save LancamentoContabil : {}", lancamentoContabilDTO);
        if (lancamentoContabilDTO.getId() != null) {
            throw new BadRequestAlertException("A new lancamentoContabil cannot already have an ID", ENTITY_NAME, "idexists");
        }
        lancamentoContabilDTO = lancamentoContabilService.save(lancamentoContabilDTO);
        return ResponseEntity.created(new URI("/api/lancamento-contabils/" + lancamentoContabilDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, lancamentoContabilDTO.getId().toString()))
            .body(lancamentoContabilDTO);
    }

    /**
     * {@code PUT  /lancamento-contabils/:id} : Updates an existing lancamentoContabil.
     *
     * @param id the id of the lancamentoContabilDTO to save.
     * @param lancamentoContabilDTO the lancamentoContabilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lancamentoContabilDTO,
     * or with status {@code 400 (Bad Request)} if the lancamentoContabilDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the lancamentoContabilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LancamentoContabilDTO> updateLancamentoContabil(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LancamentoContabilDTO lancamentoContabilDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LancamentoContabil : {}, {}", id, lancamentoContabilDTO);
        if (lancamentoContabilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lancamentoContabilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lancamentoContabilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        lancamentoContabilDTO = lancamentoContabilService.update(lancamentoContabilDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lancamentoContabilDTO.getId().toString()))
            .body(lancamentoContabilDTO);
    }

    /**
     * {@code PATCH  /lancamento-contabils/:id} : Partial updates given fields of an existing lancamentoContabil, field will ignore if it is null
     *
     * @param id the id of the lancamentoContabilDTO to save.
     * @param lancamentoContabilDTO the lancamentoContabilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated lancamentoContabilDTO,
     * or with status {@code 400 (Bad Request)} if the lancamentoContabilDTO is not valid,
     * or with status {@code 404 (Not Found)} if the lancamentoContabilDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the lancamentoContabilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LancamentoContabilDTO> partialUpdateLancamentoContabil(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LancamentoContabilDTO lancamentoContabilDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LancamentoContabil partially : {}, {}", id, lancamentoContabilDTO);
        if (lancamentoContabilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, lancamentoContabilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!lancamentoContabilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LancamentoContabilDTO> result = lancamentoContabilService.partialUpdate(lancamentoContabilDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, lancamentoContabilDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lancamento-contabils} : get all the lancamentoContabils.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of lancamentoContabils in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LancamentoContabilDTO>> getAllLancamentoContabils(
        LancamentoContabilCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get LancamentoContabils by criteria: {}", criteria);

        Page<LancamentoContabilDTO> page = lancamentoContabilQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lancamento-contabils/count} : count all the lancamentoContabils.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countLancamentoContabils(LancamentoContabilCriteria criteria) {
        LOG.debug("REST request to count LancamentoContabils by criteria: {}", criteria);
        return ResponseEntity.ok().body(lancamentoContabilQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lancamento-contabils/:id} : get the "id" lancamentoContabil.
     *
     * @param id the id of the lancamentoContabilDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the lancamentoContabilDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LancamentoContabilDTO> getLancamentoContabil(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LancamentoContabil : {}", id);
        Optional<LancamentoContabilDTO> lancamentoContabilDTO = lancamentoContabilService.findOne(id);
        return ResponseUtil.wrapOrNotFound(lancamentoContabilDTO);
    }

    /**
     * {@code DELETE  /lancamento-contabils/:id} : delete the "id" lancamentoContabil.
     *
     * @param id the id of the lancamentoContabilDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLancamentoContabil(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LancamentoContabil : {}", id);
        lancamentoContabilService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
