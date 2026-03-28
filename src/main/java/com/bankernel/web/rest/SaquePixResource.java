package com.bankernel.web.rest;

import com.bankernel.repository.SaquePixRepository;
import com.bankernel.service.SaquePixService;
import com.bankernel.service.dto.SaquePixDTO;
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
 * REST controller for managing {@link com.bankernel.domain.SaquePix}.
 */
@RestController
@RequestMapping("/api/saque-pixes")
public class SaquePixResource {

    private static final Logger LOG = LoggerFactory.getLogger(SaquePixResource.class);

    private static final String ENTITY_NAME = "saquePix";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SaquePixService saquePixService;

    private final SaquePixRepository saquePixRepository;

    public SaquePixResource(SaquePixService saquePixService, SaquePixRepository saquePixRepository) {
        this.saquePixService = saquePixService;
        this.saquePixRepository = saquePixRepository;
    }

    /**
     * {@code POST  /saque-pixes} : Create a new saquePix.
     *
     * @param saquePixDTO the saquePixDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new saquePixDTO, or with status {@code 400 (Bad Request)} if the saquePix has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SaquePixDTO> createSaquePix(@Valid @RequestBody SaquePixDTO saquePixDTO) throws URISyntaxException {
        LOG.debug("REST request to save SaquePix : {}", saquePixDTO);
        if (saquePixDTO.getId() != null) {
            throw new BadRequestAlertException("A new saquePix cannot already have an ID", ENTITY_NAME, "idexists");
        }
        saquePixDTO = saquePixService.save(saquePixDTO);
        return ResponseEntity.created(new URI("/api/saque-pixes/" + saquePixDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, saquePixDTO.getId().toString()))
            .body(saquePixDTO);
    }

    /**
     * {@code PUT  /saque-pixes/:id} : Updates an existing saquePix.
     *
     * @param id the id of the saquePixDTO to save.
     * @param saquePixDTO the saquePixDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saquePixDTO,
     * or with status {@code 400 (Bad Request)} if the saquePixDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the saquePixDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SaquePixDTO> updateSaquePix(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SaquePixDTO saquePixDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SaquePix : {}, {}", id, saquePixDTO);
        if (saquePixDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saquePixDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saquePixRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        saquePixDTO = saquePixService.update(saquePixDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saquePixDTO.getId().toString()))
            .body(saquePixDTO);
    }

    /**
     * {@code PATCH  /saque-pixes/:id} : Partial updates given fields of an existing saquePix, field will ignore if it is null
     *
     * @param id the id of the saquePixDTO to save.
     * @param saquePixDTO the saquePixDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saquePixDTO,
     * or with status {@code 400 (Bad Request)} if the saquePixDTO is not valid,
     * or with status {@code 404 (Not Found)} if the saquePixDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the saquePixDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SaquePixDTO> partialUpdateSaquePix(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SaquePixDTO saquePixDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SaquePix partially : {}, {}", id, saquePixDTO);
        if (saquePixDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saquePixDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saquePixRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SaquePixDTO> result = saquePixService.partialUpdate(saquePixDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saquePixDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /saque-pixes} : get all the saquePixes.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of saquePixes in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SaquePixDTO>> getAllSaquePixes(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of SaquePixes");
        Page<SaquePixDTO> page;
        if (eagerload) {
            page = saquePixService.findAllWithEagerRelationships(pageable);
        } else {
            page = saquePixService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /saque-pixes/:id} : get the "id" saquePix.
     *
     * @param id the id of the saquePixDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the saquePixDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SaquePixDTO> getSaquePix(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SaquePix : {}", id);
        Optional<SaquePixDTO> saquePixDTO = saquePixService.findOne(id);
        return ResponseUtil.wrapOrNotFound(saquePixDTO);
    }

    /**
     * {@code DELETE  /saque-pixes/:id} : delete the "id" saquePix.
     *
     * @param id the id of the saquePixDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaquePix(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SaquePix : {}", id);
        saquePixService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
