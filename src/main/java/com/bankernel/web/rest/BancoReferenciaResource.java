package com.bankernel.web.rest;

import com.bankernel.repository.BancoReferenciaRepository;
import com.bankernel.service.BancoReferenciaService;
import com.bankernel.service.dto.BancoReferenciaDTO;
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
 * REST controller for managing {@link com.bankernel.domain.BancoReferencia}.
 */
@RestController
@RequestMapping("/api/banco-referencias")
public class BancoReferenciaResource {

    private static final Logger LOG = LoggerFactory.getLogger(BancoReferenciaResource.class);

    private static final String ENTITY_NAME = "bancoReferencia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BancoReferenciaService bancoReferenciaService;

    private final BancoReferenciaRepository bancoReferenciaRepository;

    public BancoReferenciaResource(BancoReferenciaService bancoReferenciaService, BancoReferenciaRepository bancoReferenciaRepository) {
        this.bancoReferenciaService = bancoReferenciaService;
        this.bancoReferenciaRepository = bancoReferenciaRepository;
    }

    /**
     * {@code POST  /banco-referencias} : Create a new bancoReferencia.
     *
     * @param bancoReferenciaDTO the bancoReferenciaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bancoReferenciaDTO, or with status {@code 400 (Bad Request)} if the bancoReferencia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BancoReferenciaDTO> createBancoReferencia(@Valid @RequestBody BancoReferenciaDTO bancoReferenciaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BancoReferencia : {}", bancoReferenciaDTO);
        if (bancoReferenciaDTO.getId() != null) {
            throw new BadRequestAlertException("A new bancoReferencia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bancoReferenciaDTO = bancoReferenciaService.save(bancoReferenciaDTO);
        return ResponseEntity.created(new URI("/api/banco-referencias/" + bancoReferenciaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bancoReferenciaDTO.getId().toString()))
            .body(bancoReferenciaDTO);
    }

    /**
     * {@code PUT  /banco-referencias/:id} : Updates an existing bancoReferencia.
     *
     * @param id the id of the bancoReferenciaDTO to save.
     * @param bancoReferenciaDTO the bancoReferenciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bancoReferenciaDTO,
     * or with status {@code 400 (Bad Request)} if the bancoReferenciaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bancoReferenciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BancoReferenciaDTO> updateBancoReferencia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BancoReferenciaDTO bancoReferenciaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BancoReferencia : {}, {}", id, bancoReferenciaDTO);
        if (bancoReferenciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bancoReferenciaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bancoReferenciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bancoReferenciaDTO = bancoReferenciaService.update(bancoReferenciaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bancoReferenciaDTO.getId().toString()))
            .body(bancoReferenciaDTO);
    }

    /**
     * {@code PATCH  /banco-referencias/:id} : Partial updates given fields of an existing bancoReferencia, field will ignore if it is null
     *
     * @param id the id of the bancoReferenciaDTO to save.
     * @param bancoReferenciaDTO the bancoReferenciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bancoReferenciaDTO,
     * or with status {@code 400 (Bad Request)} if the bancoReferenciaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bancoReferenciaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bancoReferenciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BancoReferenciaDTO> partialUpdateBancoReferencia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BancoReferenciaDTO bancoReferenciaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BancoReferencia partially : {}, {}", id, bancoReferenciaDTO);
        if (bancoReferenciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bancoReferenciaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bancoReferenciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BancoReferenciaDTO> result = bancoReferenciaService.partialUpdate(bancoReferenciaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bancoReferenciaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /banco-referencias} : get all the bancoReferencias.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bancoReferencias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BancoReferenciaDTO>> getAllBancoReferencias(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of BancoReferencias");
        Page<BancoReferenciaDTO> page = bancoReferenciaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /banco-referencias/:id} : get the "id" bancoReferencia.
     *
     * @param id the id of the bancoReferenciaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bancoReferenciaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BancoReferenciaDTO> getBancoReferencia(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BancoReferencia : {}", id);
        Optional<BancoReferenciaDTO> bancoReferenciaDTO = bancoReferenciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bancoReferenciaDTO);
    }

    /**
     * {@code DELETE  /banco-referencias/:id} : delete the "id" bancoReferencia.
     *
     * @param id the id of the bancoReferenciaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBancoReferencia(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BancoReferencia : {}", id);
        bancoReferenciaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
