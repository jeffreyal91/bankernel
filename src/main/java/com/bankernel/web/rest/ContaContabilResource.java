package com.bankernel.web.rest;

import com.bankernel.repository.ContaContabilRepository;
import com.bankernel.service.ContaContabilQueryService;
import com.bankernel.service.ContaContabilService;
import com.bankernel.service.criteria.ContaContabilCriteria;
import com.bankernel.service.dto.ContaContabilDTO;
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
 * REST controller for managing {@link com.bankernel.domain.ContaContabil}.
 */
@RestController
@RequestMapping("/api/conta-contabils")
public class ContaContabilResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContaContabilResource.class);

    private static final String ENTITY_NAME = "contaContabil";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContaContabilService contaContabilService;

    private final ContaContabilRepository contaContabilRepository;

    private final ContaContabilQueryService contaContabilQueryService;

    public ContaContabilResource(
        ContaContabilService contaContabilService,
        ContaContabilRepository contaContabilRepository,
        ContaContabilQueryService contaContabilQueryService
    ) {
        this.contaContabilService = contaContabilService;
        this.contaContabilRepository = contaContabilRepository;
        this.contaContabilQueryService = contaContabilQueryService;
    }

    /**
     * {@code POST  /conta-contabils} : Create a new contaContabil.
     *
     * @param contaContabilDTO the contaContabilDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contaContabilDTO, or with status {@code 400 (Bad Request)} if the contaContabil has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContaContabilDTO> createContaContabil(@Valid @RequestBody ContaContabilDTO contaContabilDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ContaContabil : {}", contaContabilDTO);
        if (contaContabilDTO.getId() != null) {
            throw new BadRequestAlertException("A new contaContabil cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contaContabilDTO = contaContabilService.save(contaContabilDTO);
        return ResponseEntity.created(new URI("/api/conta-contabils/" + contaContabilDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contaContabilDTO.getId().toString()))
            .body(contaContabilDTO);
    }

    /**
     * {@code PUT  /conta-contabils/:id} : Updates an existing contaContabil.
     *
     * @param id the id of the contaContabilDTO to save.
     * @param contaContabilDTO the contaContabilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contaContabilDTO,
     * or with status {@code 400 (Bad Request)} if the contaContabilDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contaContabilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContaContabilDTO> updateContaContabil(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContaContabilDTO contaContabilDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ContaContabil : {}, {}", id, contaContabilDTO);
        if (contaContabilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contaContabilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contaContabilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contaContabilDTO = contaContabilService.update(contaContabilDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contaContabilDTO.getId().toString()))
            .body(contaContabilDTO);
    }

    /**
     * {@code PATCH  /conta-contabils/:id} : Partial updates given fields of an existing contaContabil, field will ignore if it is null
     *
     * @param id the id of the contaContabilDTO to save.
     * @param contaContabilDTO the contaContabilDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contaContabilDTO,
     * or with status {@code 400 (Bad Request)} if the contaContabilDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contaContabilDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contaContabilDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContaContabilDTO> partialUpdateContaContabil(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContaContabilDTO contaContabilDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ContaContabil partially : {}, {}", id, contaContabilDTO);
        if (contaContabilDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contaContabilDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contaContabilRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContaContabilDTO> result = contaContabilService.partialUpdate(contaContabilDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contaContabilDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /conta-contabils} : get all the contaContabils.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contaContabils in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContaContabilDTO>> getAllContaContabils(
        ContaContabilCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ContaContabils by criteria: {}", criteria);

        Page<ContaContabilDTO> page = contaContabilQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /conta-contabils/count} : count all the contaContabils.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countContaContabils(ContaContabilCriteria criteria) {
        LOG.debug("REST request to count ContaContabils by criteria: {}", criteria);
        return ResponseEntity.ok().body(contaContabilQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /conta-contabils/:id} : get the "id" contaContabil.
     *
     * @param id the id of the contaContabilDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contaContabilDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContaContabilDTO> getContaContabil(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ContaContabil : {}", id);
        Optional<ContaContabilDTO> contaContabilDTO = contaContabilService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contaContabilDTO);
    }

    /**
     * {@code DELETE  /conta-contabils/:id} : delete the "id" contaContabil.
     *
     * @param id the id of the contaContabilDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContaContabil(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ContaContabil : {}", id);
        contaContabilService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
