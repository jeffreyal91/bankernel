package com.bankernel.web.rest;

import com.bankernel.repository.ColaboradorPJRepository;
import com.bankernel.service.ColaboradorPJService;
import com.bankernel.service.dto.ColaboradorPJDTO;
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
 * REST controller for managing {@link com.bankernel.domain.ColaboradorPJ}.
 */
@RestController
@RequestMapping("/api/colaborador-pjs")
public class ColaboradorPJResource {

    private static final Logger LOG = LoggerFactory.getLogger(ColaboradorPJResource.class);

    private static final String ENTITY_NAME = "colaboradorPJ";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ColaboradorPJService colaboradorPJService;

    private final ColaboradorPJRepository colaboradorPJRepository;

    public ColaboradorPJResource(ColaboradorPJService colaboradorPJService, ColaboradorPJRepository colaboradorPJRepository) {
        this.colaboradorPJService = colaboradorPJService;
        this.colaboradorPJRepository = colaboradorPJRepository;
    }

    /**
     * {@code POST  /colaborador-pjs} : Create a new colaboradorPJ.
     *
     * @param colaboradorPJDTO the colaboradorPJDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new colaboradorPJDTO, or with status {@code 400 (Bad Request)} if the colaboradorPJ has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ColaboradorPJDTO> createColaboradorPJ(@Valid @RequestBody ColaboradorPJDTO colaboradorPJDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ColaboradorPJ : {}", colaboradorPJDTO);
        if (colaboradorPJDTO.getId() != null) {
            throw new BadRequestAlertException("A new colaboradorPJ cannot already have an ID", ENTITY_NAME, "idexists");
        }
        colaboradorPJDTO = colaboradorPJService.save(colaboradorPJDTO);
        return ResponseEntity.created(new URI("/api/colaborador-pjs/" + colaboradorPJDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, colaboradorPJDTO.getId().toString()))
            .body(colaboradorPJDTO);
    }

    /**
     * {@code PUT  /colaborador-pjs/:id} : Updates an existing colaboradorPJ.
     *
     * @param id the id of the colaboradorPJDTO to save.
     * @param colaboradorPJDTO the colaboradorPJDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated colaboradorPJDTO,
     * or with status {@code 400 (Bad Request)} if the colaboradorPJDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the colaboradorPJDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ColaboradorPJDTO> updateColaboradorPJ(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ColaboradorPJDTO colaboradorPJDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ColaboradorPJ : {}, {}", id, colaboradorPJDTO);
        if (colaboradorPJDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, colaboradorPJDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!colaboradorPJRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        colaboradorPJDTO = colaboradorPJService.update(colaboradorPJDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, colaboradorPJDTO.getId().toString()))
            .body(colaboradorPJDTO);
    }

    /**
     * {@code PATCH  /colaborador-pjs/:id} : Partial updates given fields of an existing colaboradorPJ, field will ignore if it is null
     *
     * @param id the id of the colaboradorPJDTO to save.
     * @param colaboradorPJDTO the colaboradorPJDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated colaboradorPJDTO,
     * or with status {@code 400 (Bad Request)} if the colaboradorPJDTO is not valid,
     * or with status {@code 404 (Not Found)} if the colaboradorPJDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the colaboradorPJDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ColaboradorPJDTO> partialUpdateColaboradorPJ(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ColaboradorPJDTO colaboradorPJDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ColaboradorPJ partially : {}, {}", id, colaboradorPJDTO);
        if (colaboradorPJDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, colaboradorPJDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!colaboradorPJRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ColaboradorPJDTO> result = colaboradorPJService.partialUpdate(colaboradorPJDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, colaboradorPJDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /colaborador-pjs} : get all the colaboradorPJS.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of colaboradorPJS in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ColaboradorPJDTO>> getAllColaboradorPJS(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of ColaboradorPJS");
        Page<ColaboradorPJDTO> page;
        if (eagerload) {
            page = colaboradorPJService.findAllWithEagerRelationships(pageable);
        } else {
            page = colaboradorPJService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /colaborador-pjs/:id} : get the "id" colaboradorPJ.
     *
     * @param id the id of the colaboradorPJDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the colaboradorPJDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ColaboradorPJDTO> getColaboradorPJ(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ColaboradorPJ : {}", id);
        Optional<ColaboradorPJDTO> colaboradorPJDTO = colaboradorPJService.findOne(id);
        return ResponseUtil.wrapOrNotFound(colaboradorPJDTO);
    }

    /**
     * {@code DELETE  /colaborador-pjs/:id} : delete the "id" colaboradorPJ.
     *
     * @param id the id of the colaboradorPJDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteColaboradorPJ(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ColaboradorPJ : {}", id);
        colaboradorPJService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
