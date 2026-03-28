package com.bankernel.web.rest;

import com.bankernel.repository.PessoaFisicaRepository;
import com.bankernel.service.PessoaFisicaQueryService;
import com.bankernel.service.PessoaFisicaService;
import com.bankernel.service.criteria.PessoaFisicaCriteria;
import com.bankernel.service.dto.PessoaFisicaDTO;
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
 * REST controller for managing {@link com.bankernel.domain.PessoaFisica}.
 */
@RestController
@RequestMapping("/api/pessoa-fisicas")
public class PessoaFisicaResource {

    private static final Logger LOG = LoggerFactory.getLogger(PessoaFisicaResource.class);

    private static final String ENTITY_NAME = "pessoaFisica";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PessoaFisicaService pessoaFisicaService;

    private final PessoaFisicaRepository pessoaFisicaRepository;

    private final PessoaFisicaQueryService pessoaFisicaQueryService;

    public PessoaFisicaResource(
        PessoaFisicaService pessoaFisicaService,
        PessoaFisicaRepository pessoaFisicaRepository,
        PessoaFisicaQueryService pessoaFisicaQueryService
    ) {
        this.pessoaFisicaService = pessoaFisicaService;
        this.pessoaFisicaRepository = pessoaFisicaRepository;
        this.pessoaFisicaQueryService = pessoaFisicaQueryService;
    }

    /**
     * {@code POST  /pessoa-fisicas} : Create a new pessoaFisica.
     *
     * @param pessoaFisicaDTO the pessoaFisicaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pessoaFisicaDTO, or with status {@code 400 (Bad Request)} if the pessoaFisica has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PessoaFisicaDTO> createPessoaFisica(@Valid @RequestBody PessoaFisicaDTO pessoaFisicaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PessoaFisica : {}", pessoaFisicaDTO);
        if (pessoaFisicaDTO.getId() != null) {
            throw new BadRequestAlertException("A new pessoaFisica cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pessoaFisicaDTO = pessoaFisicaService.save(pessoaFisicaDTO);
        return ResponseEntity.created(new URI("/api/pessoa-fisicas/" + pessoaFisicaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pessoaFisicaDTO.getId().toString()))
            .body(pessoaFisicaDTO);
    }

    /**
     * {@code PUT  /pessoa-fisicas/:id} : Updates an existing pessoaFisica.
     *
     * @param id the id of the pessoaFisicaDTO to save.
     * @param pessoaFisicaDTO the pessoaFisicaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pessoaFisicaDTO,
     * or with status {@code 400 (Bad Request)} if the pessoaFisicaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pessoaFisicaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PessoaFisicaDTO> updatePessoaFisica(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PessoaFisicaDTO pessoaFisicaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PessoaFisica : {}, {}", id, pessoaFisicaDTO);
        if (pessoaFisicaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pessoaFisicaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pessoaFisicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pessoaFisicaDTO = pessoaFisicaService.update(pessoaFisicaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pessoaFisicaDTO.getId().toString()))
            .body(pessoaFisicaDTO);
    }

    /**
     * {@code PATCH  /pessoa-fisicas/:id} : Partial updates given fields of an existing pessoaFisica, field will ignore if it is null
     *
     * @param id the id of the pessoaFisicaDTO to save.
     * @param pessoaFisicaDTO the pessoaFisicaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pessoaFisicaDTO,
     * or with status {@code 400 (Bad Request)} if the pessoaFisicaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pessoaFisicaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pessoaFisicaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PessoaFisicaDTO> partialUpdatePessoaFisica(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PessoaFisicaDTO pessoaFisicaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PessoaFisica partially : {}, {}", id, pessoaFisicaDTO);
        if (pessoaFisicaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pessoaFisicaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pessoaFisicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PessoaFisicaDTO> result = pessoaFisicaService.partialUpdate(pessoaFisicaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pessoaFisicaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pessoa-fisicas} : get all the pessoaFisicas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pessoaFisicas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PessoaFisicaDTO>> getAllPessoaFisicas(
        PessoaFisicaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PessoaFisicas by criteria: {}", criteria);

        Page<PessoaFisicaDTO> page = pessoaFisicaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pessoa-fisicas/count} : count all the pessoaFisicas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPessoaFisicas(PessoaFisicaCriteria criteria) {
        LOG.debug("REST request to count PessoaFisicas by criteria: {}", criteria);
        return ResponseEntity.ok().body(pessoaFisicaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pessoa-fisicas/:id} : get the "id" pessoaFisica.
     *
     * @param id the id of the pessoaFisicaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pessoaFisicaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PessoaFisicaDTO> getPessoaFisica(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PessoaFisica : {}", id);
        Optional<PessoaFisicaDTO> pessoaFisicaDTO = pessoaFisicaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pessoaFisicaDTO);
    }

    /**
     * {@code DELETE  /pessoa-fisicas/:id} : delete the "id" pessoaFisica.
     *
     * @param id the id of the pessoaFisicaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePessoaFisica(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PessoaFisica : {}", id);
        pessoaFisicaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
