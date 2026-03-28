package com.bankernel.web.rest;

import com.bankernel.repository.TipoNegocioRepository;
import com.bankernel.service.TipoNegocioService;
import com.bankernel.service.dto.TipoNegocioDTO;
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
 * REST controller for managing {@link com.bankernel.domain.TipoNegocio}.
 */
@RestController
@RequestMapping("/api/tipo-negocios")
public class TipoNegocioResource {

    private static final Logger LOG = LoggerFactory.getLogger(TipoNegocioResource.class);

    private static final String ENTITY_NAME = "tipoNegocio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoNegocioService tipoNegocioService;

    private final TipoNegocioRepository tipoNegocioRepository;

    public TipoNegocioResource(TipoNegocioService tipoNegocioService, TipoNegocioRepository tipoNegocioRepository) {
        this.tipoNegocioService = tipoNegocioService;
        this.tipoNegocioRepository = tipoNegocioRepository;
    }

    /**
     * {@code POST  /tipo-negocios} : Create a new tipoNegocio.
     *
     * @param tipoNegocioDTO the tipoNegocioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoNegocioDTO, or with status {@code 400 (Bad Request)} if the tipoNegocio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TipoNegocioDTO> createTipoNegocio(@Valid @RequestBody TipoNegocioDTO tipoNegocioDTO) throws URISyntaxException {
        LOG.debug("REST request to save TipoNegocio : {}", tipoNegocioDTO);
        if (tipoNegocioDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoNegocio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tipoNegocioDTO = tipoNegocioService.save(tipoNegocioDTO);
        return ResponseEntity.created(new URI("/api/tipo-negocios/" + tipoNegocioDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tipoNegocioDTO.getId().toString()))
            .body(tipoNegocioDTO);
    }

    /**
     * {@code PUT  /tipo-negocios/:id} : Updates an existing tipoNegocio.
     *
     * @param id the id of the tipoNegocioDTO to save.
     * @param tipoNegocioDTO the tipoNegocioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoNegocioDTO,
     * or with status {@code 400 (Bad Request)} if the tipoNegocioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoNegocioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoNegocioDTO> updateTipoNegocio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoNegocioDTO tipoNegocioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TipoNegocio : {}, {}", id, tipoNegocioDTO);
        if (tipoNegocioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoNegocioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoNegocioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tipoNegocioDTO = tipoNegocioService.update(tipoNegocioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoNegocioDTO.getId().toString()))
            .body(tipoNegocioDTO);
    }

    /**
     * {@code PATCH  /tipo-negocios/:id} : Partial updates given fields of an existing tipoNegocio, field will ignore if it is null
     *
     * @param id the id of the tipoNegocioDTO to save.
     * @param tipoNegocioDTO the tipoNegocioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoNegocioDTO,
     * or with status {@code 400 (Bad Request)} if the tipoNegocioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoNegocioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoNegocioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoNegocioDTO> partialUpdateTipoNegocio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoNegocioDTO tipoNegocioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TipoNegocio partially : {}, {}", id, tipoNegocioDTO);
        if (tipoNegocioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoNegocioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoNegocioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoNegocioDTO> result = tipoNegocioService.partialUpdate(tipoNegocioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoNegocioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-negocios} : get all the tipoNegocios.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoNegocios in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TipoNegocioDTO>> getAllTipoNegocios(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of TipoNegocios");
        Page<TipoNegocioDTO> page = tipoNegocioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-negocios/:id} : get the "id" tipoNegocio.
     *
     * @param id the id of the tipoNegocioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoNegocioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoNegocioDTO> getTipoNegocio(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TipoNegocio : {}", id);
        Optional<TipoNegocioDTO> tipoNegocioDTO = tipoNegocioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoNegocioDTO);
    }

    /**
     * {@code DELETE  /tipo-negocios/:id} : delete the "id" tipoNegocio.
     *
     * @param id the id of the tipoNegocioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoNegocio(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TipoNegocio : {}", id);
        tipoNegocioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
