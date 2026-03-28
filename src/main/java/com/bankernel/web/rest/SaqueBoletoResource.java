package com.bankernel.web.rest;

import com.bankernel.repository.SaqueBoletoRepository;
import com.bankernel.service.SaqueBoletoService;
import com.bankernel.service.dto.SaqueBoletoDTO;
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
 * REST controller for managing {@link com.bankernel.domain.SaqueBoleto}.
 */
@RestController
@RequestMapping("/api/saque-boletos")
public class SaqueBoletoResource {

    private static final Logger LOG = LoggerFactory.getLogger(SaqueBoletoResource.class);

    private static final String ENTITY_NAME = "saqueBoleto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SaqueBoletoService saqueBoletoService;

    private final SaqueBoletoRepository saqueBoletoRepository;

    public SaqueBoletoResource(SaqueBoletoService saqueBoletoService, SaqueBoletoRepository saqueBoletoRepository) {
        this.saqueBoletoService = saqueBoletoService;
        this.saqueBoletoRepository = saqueBoletoRepository;
    }

    /**
     * {@code POST  /saque-boletos} : Create a new saqueBoleto.
     *
     * @param saqueBoletoDTO the saqueBoletoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new saqueBoletoDTO, or with status {@code 400 (Bad Request)} if the saqueBoleto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SaqueBoletoDTO> createSaqueBoleto(@Valid @RequestBody SaqueBoletoDTO saqueBoletoDTO) throws URISyntaxException {
        LOG.debug("REST request to save SaqueBoleto : {}", saqueBoletoDTO);
        if (saqueBoletoDTO.getId() != null) {
            throw new BadRequestAlertException("A new saqueBoleto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        saqueBoletoDTO = saqueBoletoService.save(saqueBoletoDTO);
        return ResponseEntity.created(new URI("/api/saque-boletos/" + saqueBoletoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, saqueBoletoDTO.getId().toString()))
            .body(saqueBoletoDTO);
    }

    /**
     * {@code PUT  /saque-boletos/:id} : Updates an existing saqueBoleto.
     *
     * @param id the id of the saqueBoletoDTO to save.
     * @param saqueBoletoDTO the saqueBoletoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saqueBoletoDTO,
     * or with status {@code 400 (Bad Request)} if the saqueBoletoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the saqueBoletoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SaqueBoletoDTO> updateSaqueBoleto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SaqueBoletoDTO saqueBoletoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SaqueBoleto : {}, {}", id, saqueBoletoDTO);
        if (saqueBoletoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saqueBoletoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saqueBoletoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        saqueBoletoDTO = saqueBoletoService.update(saqueBoletoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saqueBoletoDTO.getId().toString()))
            .body(saqueBoletoDTO);
    }

    /**
     * {@code PATCH  /saque-boletos/:id} : Partial updates given fields of an existing saqueBoleto, field will ignore if it is null
     *
     * @param id the id of the saqueBoletoDTO to save.
     * @param saqueBoletoDTO the saqueBoletoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated saqueBoletoDTO,
     * or with status {@code 400 (Bad Request)} if the saqueBoletoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the saqueBoletoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the saqueBoletoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SaqueBoletoDTO> partialUpdateSaqueBoleto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SaqueBoletoDTO saqueBoletoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SaqueBoleto partially : {}, {}", id, saqueBoletoDTO);
        if (saqueBoletoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, saqueBoletoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!saqueBoletoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SaqueBoletoDTO> result = saqueBoletoService.partialUpdate(saqueBoletoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, saqueBoletoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /saque-boletos} : get all the saqueBoletos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of saqueBoletos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SaqueBoletoDTO>> getAllSaqueBoletos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of SaqueBoletos");
        Page<SaqueBoletoDTO> page;
        if (eagerload) {
            page = saqueBoletoService.findAllWithEagerRelationships(pageable);
        } else {
            page = saqueBoletoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /saque-boletos/:id} : get the "id" saqueBoleto.
     *
     * @param id the id of the saqueBoletoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the saqueBoletoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SaqueBoletoDTO> getSaqueBoleto(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SaqueBoleto : {}", id);
        Optional<SaqueBoletoDTO> saqueBoletoDTO = saqueBoletoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(saqueBoletoDTO);
    }

    /**
     * {@code DELETE  /saque-boletos/:id} : delete the "id" saqueBoleto.
     *
     * @param id the id of the saqueBoletoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSaqueBoleto(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SaqueBoleto : {}", id);
        saqueBoletoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
