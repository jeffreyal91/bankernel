package com.bankernel.web.rest;

import com.bankernel.repository.PlanoRecorrenciaRepository;
import com.bankernel.service.PlanoRecorrenciaService;
import com.bankernel.service.dto.PlanoRecorrenciaDTO;
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
 * REST controller for managing {@link com.bankernel.domain.PlanoRecorrencia}.
 */
@RestController
@RequestMapping("/api/plano-recorrencias")
public class PlanoRecorrenciaResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlanoRecorrenciaResource.class);

    private static final String ENTITY_NAME = "planoRecorrencia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanoRecorrenciaService planoRecorrenciaService;

    private final PlanoRecorrenciaRepository planoRecorrenciaRepository;

    public PlanoRecorrenciaResource(
        PlanoRecorrenciaService planoRecorrenciaService,
        PlanoRecorrenciaRepository planoRecorrenciaRepository
    ) {
        this.planoRecorrenciaService = planoRecorrenciaService;
        this.planoRecorrenciaRepository = planoRecorrenciaRepository;
    }

    /**
     * {@code POST  /plano-recorrencias} : Create a new planoRecorrencia.
     *
     * @param planoRecorrenciaDTO the planoRecorrenciaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planoRecorrenciaDTO, or with status {@code 400 (Bad Request)} if the planoRecorrencia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PlanoRecorrenciaDTO> createPlanoRecorrencia(@Valid @RequestBody PlanoRecorrenciaDTO planoRecorrenciaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PlanoRecorrencia : {}", planoRecorrenciaDTO);
        if (planoRecorrenciaDTO.getId() != null) {
            throw new BadRequestAlertException("A new planoRecorrencia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        planoRecorrenciaDTO = planoRecorrenciaService.save(planoRecorrenciaDTO);
        return ResponseEntity.created(new URI("/api/plano-recorrencias/" + planoRecorrenciaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, planoRecorrenciaDTO.getId().toString()))
            .body(planoRecorrenciaDTO);
    }

    /**
     * {@code PUT  /plano-recorrencias/:id} : Updates an existing planoRecorrencia.
     *
     * @param id the id of the planoRecorrenciaDTO to save.
     * @param planoRecorrenciaDTO the planoRecorrenciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planoRecorrenciaDTO,
     * or with status {@code 400 (Bad Request)} if the planoRecorrenciaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planoRecorrenciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlanoRecorrenciaDTO> updatePlanoRecorrencia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlanoRecorrenciaDTO planoRecorrenciaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PlanoRecorrencia : {}, {}", id, planoRecorrenciaDTO);
        if (planoRecorrenciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planoRecorrenciaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planoRecorrenciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        planoRecorrenciaDTO = planoRecorrenciaService.update(planoRecorrenciaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planoRecorrenciaDTO.getId().toString()))
            .body(planoRecorrenciaDTO);
    }

    /**
     * {@code PATCH  /plano-recorrencias/:id} : Partial updates given fields of an existing planoRecorrencia, field will ignore if it is null
     *
     * @param id the id of the planoRecorrenciaDTO to save.
     * @param planoRecorrenciaDTO the planoRecorrenciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planoRecorrenciaDTO,
     * or with status {@code 400 (Bad Request)} if the planoRecorrenciaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the planoRecorrenciaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the planoRecorrenciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlanoRecorrenciaDTO> partialUpdatePlanoRecorrencia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlanoRecorrenciaDTO planoRecorrenciaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PlanoRecorrencia partially : {}, {}", id, planoRecorrenciaDTO);
        if (planoRecorrenciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planoRecorrenciaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planoRecorrenciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlanoRecorrenciaDTO> result = planoRecorrenciaService.partialUpdate(planoRecorrenciaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planoRecorrenciaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /plano-recorrencias} : get all the planoRecorrencias.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planoRecorrencias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PlanoRecorrenciaDTO>> getAllPlanoRecorrencias(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of PlanoRecorrencias");
        Page<PlanoRecorrenciaDTO> page;
        if (eagerload) {
            page = planoRecorrenciaService.findAllWithEagerRelationships(pageable);
        } else {
            page = planoRecorrenciaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /plano-recorrencias/:id} : get the "id" planoRecorrencia.
     *
     * @param id the id of the planoRecorrenciaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planoRecorrenciaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlanoRecorrenciaDTO> getPlanoRecorrencia(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PlanoRecorrencia : {}", id);
        Optional<PlanoRecorrenciaDTO> planoRecorrenciaDTO = planoRecorrenciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planoRecorrenciaDTO);
    }

    /**
     * {@code DELETE  /plano-recorrencias/:id} : delete the "id" planoRecorrencia.
     *
     * @param id the id of the planoRecorrenciaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlanoRecorrencia(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PlanoRecorrencia : {}", id);
        planoRecorrenciaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
