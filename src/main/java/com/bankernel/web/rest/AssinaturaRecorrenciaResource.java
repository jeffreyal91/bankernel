package com.bankernel.web.rest;

import com.bankernel.repository.AssinaturaRecorrenciaRepository;
import com.bankernel.service.AssinaturaRecorrenciaService;
import com.bankernel.service.dto.AssinaturaRecorrenciaDTO;
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
 * REST controller for managing {@link com.bankernel.domain.AssinaturaRecorrencia}.
 */
@RestController
@RequestMapping("/api/assinatura-recorrencias")
public class AssinaturaRecorrenciaResource {

    private static final Logger LOG = LoggerFactory.getLogger(AssinaturaRecorrenciaResource.class);

    private static final String ENTITY_NAME = "assinaturaRecorrencia";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssinaturaRecorrenciaService assinaturaRecorrenciaService;

    private final AssinaturaRecorrenciaRepository assinaturaRecorrenciaRepository;

    public AssinaturaRecorrenciaResource(
        AssinaturaRecorrenciaService assinaturaRecorrenciaService,
        AssinaturaRecorrenciaRepository assinaturaRecorrenciaRepository
    ) {
        this.assinaturaRecorrenciaService = assinaturaRecorrenciaService;
        this.assinaturaRecorrenciaRepository = assinaturaRecorrenciaRepository;
    }

    /**
     * {@code POST  /assinatura-recorrencias} : Create a new assinaturaRecorrencia.
     *
     * @param assinaturaRecorrenciaDTO the assinaturaRecorrenciaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assinaturaRecorrenciaDTO, or with status {@code 400 (Bad Request)} if the assinaturaRecorrencia has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AssinaturaRecorrenciaDTO> createAssinaturaRecorrencia(
        @Valid @RequestBody AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AssinaturaRecorrencia : {}", assinaturaRecorrenciaDTO);
        if (assinaturaRecorrenciaDTO.getId() != null) {
            throw new BadRequestAlertException("A new assinaturaRecorrencia cannot already have an ID", ENTITY_NAME, "idexists");
        }
        assinaturaRecorrenciaDTO = assinaturaRecorrenciaService.save(assinaturaRecorrenciaDTO);
        return ResponseEntity.created(new URI("/api/assinatura-recorrencias/" + assinaturaRecorrenciaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, assinaturaRecorrenciaDTO.getId().toString()))
            .body(assinaturaRecorrenciaDTO);
    }

    /**
     * {@code PUT  /assinatura-recorrencias/:id} : Updates an existing assinaturaRecorrencia.
     *
     * @param id the id of the assinaturaRecorrenciaDTO to save.
     * @param assinaturaRecorrenciaDTO the assinaturaRecorrenciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assinaturaRecorrenciaDTO,
     * or with status {@code 400 (Bad Request)} if the assinaturaRecorrenciaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assinaturaRecorrenciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AssinaturaRecorrenciaDTO> updateAssinaturaRecorrencia(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AssinaturaRecorrencia : {}, {}", id, assinaturaRecorrenciaDTO);
        if (assinaturaRecorrenciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assinaturaRecorrenciaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assinaturaRecorrenciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        assinaturaRecorrenciaDTO = assinaturaRecorrenciaService.update(assinaturaRecorrenciaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assinaturaRecorrenciaDTO.getId().toString()))
            .body(assinaturaRecorrenciaDTO);
    }

    /**
     * {@code PATCH  /assinatura-recorrencias/:id} : Partial updates given fields of an existing assinaturaRecorrencia, field will ignore if it is null
     *
     * @param id the id of the assinaturaRecorrenciaDTO to save.
     * @param assinaturaRecorrenciaDTO the assinaturaRecorrenciaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assinaturaRecorrenciaDTO,
     * or with status {@code 400 (Bad Request)} if the assinaturaRecorrenciaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assinaturaRecorrenciaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assinaturaRecorrenciaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssinaturaRecorrenciaDTO> partialUpdateAssinaturaRecorrencia(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AssinaturaRecorrencia partially : {}, {}", id, assinaturaRecorrenciaDTO);
        if (assinaturaRecorrenciaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assinaturaRecorrenciaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assinaturaRecorrenciaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssinaturaRecorrenciaDTO> result = assinaturaRecorrenciaService.partialUpdate(assinaturaRecorrenciaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assinaturaRecorrenciaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /assinatura-recorrencias} : get all the assinaturaRecorrencias.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assinaturaRecorrencias in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AssinaturaRecorrenciaDTO>> getAllAssinaturaRecorrencias(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of AssinaturaRecorrencias");
        Page<AssinaturaRecorrenciaDTO> page;
        if (eagerload) {
            page = assinaturaRecorrenciaService.findAllWithEagerRelationships(pageable);
        } else {
            page = assinaturaRecorrenciaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /assinatura-recorrencias/:id} : get the "id" assinaturaRecorrencia.
     *
     * @param id the id of the assinaturaRecorrenciaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assinaturaRecorrenciaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AssinaturaRecorrenciaDTO> getAssinaturaRecorrencia(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AssinaturaRecorrencia : {}", id);
        Optional<AssinaturaRecorrenciaDTO> assinaturaRecorrenciaDTO = assinaturaRecorrenciaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assinaturaRecorrenciaDTO);
    }

    /**
     * {@code DELETE  /assinatura-recorrencias/:id} : delete the "id" assinaturaRecorrencia.
     *
     * @param id the id of the assinaturaRecorrenciaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAssinaturaRecorrencia(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AssinaturaRecorrencia : {}", id);
        assinaturaRecorrenciaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
