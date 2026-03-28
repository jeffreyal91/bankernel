package com.bankernel.web.rest;

import com.bankernel.repository.PessoaJuridicaRepository;
import com.bankernel.service.PessoaJuridicaQueryService;
import com.bankernel.service.PessoaJuridicaService;
import com.bankernel.service.criteria.PessoaJuridicaCriteria;
import com.bankernel.service.dto.PessoaJuridicaDTO;
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
 * REST controller for managing {@link com.bankernel.domain.PessoaJuridica}.
 */
@RestController
@RequestMapping("/api/pessoa-juridicas")
public class PessoaJuridicaResource {

    private static final Logger LOG = LoggerFactory.getLogger(PessoaJuridicaResource.class);

    private static final String ENTITY_NAME = "pessoaJuridica";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PessoaJuridicaService pessoaJuridicaService;

    private final PessoaJuridicaRepository pessoaJuridicaRepository;

    private final PessoaJuridicaQueryService pessoaJuridicaQueryService;

    public PessoaJuridicaResource(
        PessoaJuridicaService pessoaJuridicaService,
        PessoaJuridicaRepository pessoaJuridicaRepository,
        PessoaJuridicaQueryService pessoaJuridicaQueryService
    ) {
        this.pessoaJuridicaService = pessoaJuridicaService;
        this.pessoaJuridicaRepository = pessoaJuridicaRepository;
        this.pessoaJuridicaQueryService = pessoaJuridicaQueryService;
    }

    /**
     * {@code POST  /pessoa-juridicas} : Create a new pessoaJuridica.
     *
     * @param pessoaJuridicaDTO the pessoaJuridicaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pessoaJuridicaDTO, or with status {@code 400 (Bad Request)} if the pessoaJuridica has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PessoaJuridicaDTO> createPessoaJuridica(@Valid @RequestBody PessoaJuridicaDTO pessoaJuridicaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PessoaJuridica : {}", pessoaJuridicaDTO);
        if (pessoaJuridicaDTO.getId() != null) {
            throw new BadRequestAlertException("A new pessoaJuridica cannot already have an ID", ENTITY_NAME, "idexists");
        }
        pessoaJuridicaDTO = pessoaJuridicaService.save(pessoaJuridicaDTO);
        return ResponseEntity.created(new URI("/api/pessoa-juridicas/" + pessoaJuridicaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, pessoaJuridicaDTO.getId().toString()))
            .body(pessoaJuridicaDTO);
    }

    /**
     * {@code PUT  /pessoa-juridicas/:id} : Updates an existing pessoaJuridica.
     *
     * @param id the id of the pessoaJuridicaDTO to save.
     * @param pessoaJuridicaDTO the pessoaJuridicaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pessoaJuridicaDTO,
     * or with status {@code 400 (Bad Request)} if the pessoaJuridicaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pessoaJuridicaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PessoaJuridicaDTO> updatePessoaJuridica(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PessoaJuridicaDTO pessoaJuridicaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PessoaJuridica : {}, {}", id, pessoaJuridicaDTO);
        if (pessoaJuridicaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pessoaJuridicaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pessoaJuridicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        pessoaJuridicaDTO = pessoaJuridicaService.update(pessoaJuridicaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pessoaJuridicaDTO.getId().toString()))
            .body(pessoaJuridicaDTO);
    }

    /**
     * {@code PATCH  /pessoa-juridicas/:id} : Partial updates given fields of an existing pessoaJuridica, field will ignore if it is null
     *
     * @param id the id of the pessoaJuridicaDTO to save.
     * @param pessoaJuridicaDTO the pessoaJuridicaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pessoaJuridicaDTO,
     * or with status {@code 400 (Bad Request)} if the pessoaJuridicaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pessoaJuridicaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pessoaJuridicaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PessoaJuridicaDTO> partialUpdatePessoaJuridica(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PessoaJuridicaDTO pessoaJuridicaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PessoaJuridica partially : {}, {}", id, pessoaJuridicaDTO);
        if (pessoaJuridicaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pessoaJuridicaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pessoaJuridicaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PessoaJuridicaDTO> result = pessoaJuridicaService.partialUpdate(pessoaJuridicaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pessoaJuridicaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pessoa-juridicas} : get all the pessoaJuridicas.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pessoaJuridicas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PessoaJuridicaDTO>> getAllPessoaJuridicas(
        PessoaJuridicaCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get PessoaJuridicas by criteria: {}", criteria);

        Page<PessoaJuridicaDTO> page = pessoaJuridicaQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pessoa-juridicas/count} : count all the pessoaJuridicas.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countPessoaJuridicas(PessoaJuridicaCriteria criteria) {
        LOG.debug("REST request to count PessoaJuridicas by criteria: {}", criteria);
        return ResponseEntity.ok().body(pessoaJuridicaQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pessoa-juridicas/:id} : get the "id" pessoaJuridica.
     *
     * @param id the id of the pessoaJuridicaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pessoaJuridicaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PessoaJuridicaDTO> getPessoaJuridica(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PessoaJuridica : {}", id);
        Optional<PessoaJuridicaDTO> pessoaJuridicaDTO = pessoaJuridicaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pessoaJuridicaDTO);
    }

    /**
     * {@code DELETE  /pessoa-juridicas/:id} : delete the "id" pessoaJuridica.
     *
     * @param id the id of the pessoaJuridicaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePessoaJuridica(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PessoaJuridica : {}", id);
        pessoaJuridicaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
