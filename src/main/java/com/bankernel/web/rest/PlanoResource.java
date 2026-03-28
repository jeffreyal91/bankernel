package com.bankernel.web.rest;

import com.bankernel.repository.PlanoRepository;
import com.bankernel.service.PlanoService;
import com.bankernel.service.dto.PlanoDTO;
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
 * REST controller for managing {@link com.bankernel.domain.Plano}.
 */
@RestController
@RequestMapping("/api/planos")
public class PlanoResource {

    private static final Logger LOG = LoggerFactory.getLogger(PlanoResource.class);

    private static final String ENTITY_NAME = "plano";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlanoService planoService;

    private final PlanoRepository planoRepository;

    public PlanoResource(PlanoService planoService, PlanoRepository planoRepository) {
        this.planoService = planoService;
        this.planoRepository = planoRepository;
    }

    /**
     * {@code POST  /planos} : Create a new plano.
     *
     * @param planoDTO the planoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new planoDTO, or with status {@code 400 (Bad Request)} if the plano has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PlanoDTO> createPlano(@Valid @RequestBody PlanoDTO planoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Plano : {}", planoDTO);
        if (planoDTO.getId() != null) {
            throw new BadRequestAlertException("A new plano cannot already have an ID", ENTITY_NAME, "idexists");
        }
        planoDTO = planoService.save(planoDTO);
        return ResponseEntity.created(new URI("/api/planos/" + planoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, planoDTO.getId().toString()))
            .body(planoDTO);
    }

    /**
     * {@code PUT  /planos/:id} : Updates an existing plano.
     *
     * @param id the id of the planoDTO to save.
     * @param planoDTO the planoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planoDTO,
     * or with status {@code 400 (Bad Request)} if the planoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the planoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlanoDTO> updatePlano(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlanoDTO planoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Plano : {}, {}", id, planoDTO);
        if (planoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        planoDTO = planoService.update(planoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planoDTO.getId().toString()))
            .body(planoDTO);
    }

    /**
     * {@code PATCH  /planos/:id} : Partial updates given fields of an existing plano, field will ignore if it is null
     *
     * @param id the id of the planoDTO to save.
     * @param planoDTO the planoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated planoDTO,
     * or with status {@code 400 (Bad Request)} if the planoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the planoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the planoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlanoDTO> partialUpdatePlano(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlanoDTO planoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Plano partially : {}, {}", id, planoDTO);
        if (planoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, planoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!planoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlanoDTO> result = planoService.partialUpdate(planoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, planoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /planos} : get all the planos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of planos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PlanoDTO>> getAllPlanos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Planos");
        Page<PlanoDTO> page = planoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /planos/:id} : get the "id" plano.
     *
     * @param id the id of the planoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the planoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PlanoDTO> getPlano(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Plano : {}", id);
        Optional<PlanoDTO> planoDTO = planoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(planoDTO);
    }

    /**
     * {@code DELETE  /planos/:id} : delete the "id" plano.
     *
     * @param id the id of the planoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlano(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Plano : {}", id);
        planoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
