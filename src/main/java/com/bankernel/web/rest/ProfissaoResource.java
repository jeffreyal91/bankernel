package com.bankernel.web.rest;

import com.bankernel.repository.ProfissaoRepository;
import com.bankernel.service.ProfissaoService;
import com.bankernel.service.dto.ProfissaoDTO;
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
 * REST controller for managing {@link com.bankernel.domain.Profissao}.
 */
@RestController
@RequestMapping("/api/profissaos")
public class ProfissaoResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProfissaoResource.class);

    private static final String ENTITY_NAME = "profissao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfissaoService profissaoService;

    private final ProfissaoRepository profissaoRepository;

    public ProfissaoResource(ProfissaoService profissaoService, ProfissaoRepository profissaoRepository) {
        this.profissaoService = profissaoService;
        this.profissaoRepository = profissaoRepository;
    }

    /**
     * {@code POST  /profissaos} : Create a new profissao.
     *
     * @param profissaoDTO the profissaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new profissaoDTO, or with status {@code 400 (Bad Request)} if the profissao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProfissaoDTO> createProfissao(@Valid @RequestBody ProfissaoDTO profissaoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Profissao : {}", profissaoDTO);
        if (profissaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new profissao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        profissaoDTO = profissaoService.save(profissaoDTO);
        return ResponseEntity.created(new URI("/api/profissaos/" + profissaoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, profissaoDTO.getId().toString()))
            .body(profissaoDTO);
    }

    /**
     * {@code PUT  /profissaos/:id} : Updates an existing profissao.
     *
     * @param id the id of the profissaoDTO to save.
     * @param profissaoDTO the profissaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profissaoDTO,
     * or with status {@code 400 (Bad Request)} if the profissaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the profissaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProfissaoDTO> updateProfissao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfissaoDTO profissaoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Profissao : {}, {}", id, profissaoDTO);
        if (profissaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profissaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!profissaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        profissaoDTO = profissaoService.update(profissaoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profissaoDTO.getId().toString()))
            .body(profissaoDTO);
    }

    /**
     * {@code PATCH  /profissaos/:id} : Partial updates given fields of an existing profissao, field will ignore if it is null
     *
     * @param id the id of the profissaoDTO to save.
     * @param profissaoDTO the profissaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated profissaoDTO,
     * or with status {@code 400 (Bad Request)} if the profissaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the profissaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the profissaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfissaoDTO> partialUpdateProfissao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfissaoDTO profissaoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Profissao partially : {}, {}", id, profissaoDTO);
        if (profissaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, profissaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!profissaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfissaoDTO> result = profissaoService.partialUpdate(profissaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, profissaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /profissaos} : get all the profissaos.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of profissaos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProfissaoDTO>> getAllProfissaos(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Profissaos");
        Page<ProfissaoDTO> page = profissaoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /profissaos/:id} : get the "id" profissao.
     *
     * @param id the id of the profissaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the profissaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProfissaoDTO> getProfissao(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Profissao : {}", id);
        Optional<ProfissaoDTO> profissaoDTO = profissaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(profissaoDTO);
    }

    /**
     * {@code DELETE  /profissaos/:id} : delete the "id" profissao.
     *
     * @param id the id of the profissaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProfissao(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Profissao : {}", id);
        profissaoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
