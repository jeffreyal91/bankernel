package com.bankernel.web.rest;

import com.bankernel.repository.CobrancaRepository;
import com.bankernel.service.CobrancaQueryService;
import com.bankernel.service.CobrancaService;
import com.bankernel.service.criteria.CobrancaCriteria;
import com.bankernel.service.dto.CobrancaDTO;
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
 * REST controller for managing {@link com.bankernel.domain.Cobranca}.
 */
@RestController
@RequestMapping("/api/cobrancas")
public class CobrancaResource {

    private static final Logger LOG = LoggerFactory.getLogger(CobrancaResource.class);

    private static final String ENTITY_NAME = "cobranca";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CobrancaService cobrancaService;

    private final CobrancaRepository cobrancaRepository;

    private final CobrancaQueryService cobrancaQueryService;

    public CobrancaResource(
        CobrancaService cobrancaService,
        CobrancaRepository cobrancaRepository,
        CobrancaQueryService cobrancaQueryService
    ) {
        this.cobrancaService = cobrancaService;
        this.cobrancaRepository = cobrancaRepository;
        this.cobrancaQueryService = cobrancaQueryService;
    }

    /**
     * {@code POST  /cobrancas} : Create a new cobranca.
     *
     * @param cobrancaDTO the cobrancaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cobrancaDTO, or with status {@code 400 (Bad Request)} if the cobranca has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CobrancaDTO> createCobranca(@Valid @RequestBody CobrancaDTO cobrancaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Cobranca : {}", cobrancaDTO);
        if (cobrancaDTO.getId() != null) {
            throw new BadRequestAlertException("A new cobranca cannot already have an ID", ENTITY_NAME, "idexists");
        }
        cobrancaDTO = cobrancaService.save(cobrancaDTO);
        return ResponseEntity.created(new URI("/api/cobrancas/" + cobrancaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, cobrancaDTO.getId().toString()))
            .body(cobrancaDTO);
    }

    /**
     * {@code PUT  /cobrancas/:id} : Updates an existing cobranca.
     *
     * @param id the id of the cobrancaDTO to save.
     * @param cobrancaDTO the cobrancaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cobrancaDTO,
     * or with status {@code 400 (Bad Request)} if the cobrancaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cobrancaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CobrancaDTO> updateCobranca(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CobrancaDTO cobrancaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Cobranca : {}, {}", id, cobrancaDTO);
        if (cobrancaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cobrancaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cobrancaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        cobrancaDTO = cobrancaService.update(cobrancaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cobrancaDTO.getId().toString()))
            .body(cobrancaDTO);
    }

    /**
     * {@code PATCH  /cobrancas/:id} : Partial updates given fields of an existing cobranca, field will ignore if it is null
     *
     * @param id the id of the cobrancaDTO to save.
     * @param cobrancaDTO the cobrancaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cobrancaDTO,
     * or with status {@code 400 (Bad Request)} if the cobrancaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cobrancaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cobrancaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CobrancaDTO> partialUpdateCobranca(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CobrancaDTO cobrancaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Cobranca partially : {}, {}", id, cobrancaDTO);
        if (cobrancaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cobrancaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cobrancaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CobrancaDTO> result = cobrancaService.partialUpdate(cobrancaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cobrancaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /cobrancas} : get all the cobrancas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cobrancas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CobrancaDTO>> getAllCobrancas(
        CobrancaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Cobrancas by criteria: {}", criteria);

        Page<CobrancaDTO> page = cobrancaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cobrancas/count} : count all the cobrancas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCobrancas(CobrancaCriteria criteria) {
        LOG.debug("REST request to count Cobrancas by criteria: {}", criteria);
        return ResponseEntity.ok().body(cobrancaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /cobrancas/:id} : get the "id" cobranca.
     *
     * @param id the id of the cobrancaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cobrancaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CobrancaDTO> getCobranca(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Cobranca : {}", id);
        Optional<CobrancaDTO> cobrancaDTO = cobrancaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cobrancaDTO);
    }

    /**
     * {@code DELETE  /cobrancas/:id} : delete the "id" cobranca.
     *
     * @param id the id of the cobrancaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCobranca(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Cobranca : {}", id);
        cobrancaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
