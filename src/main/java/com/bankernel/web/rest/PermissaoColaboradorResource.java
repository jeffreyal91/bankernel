package com.bankernel.web.rest;

import com.bankernel.repository.PermissaoColaboradorRepository;
import com.bankernel.service.PermissaoColaboradorService;
import com.bankernel.service.dto.PermissaoColaboradorDTO;
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
 * REST controller for managing {@link com.bankernel.domain.PermissaoColaborador}.
 */
@RestController
@RequestMapping("/api/permissao-colaboradors")
public class PermissaoColaboradorResource {

    private static final Logger LOG = LoggerFactory.getLogger(PermissaoColaboradorResource.class);

    private static final String ENTITY_NAME = "permissaoColaborador";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PermissaoColaboradorService permissaoColaboradorService;

    private final PermissaoColaboradorRepository permissaoColaboradorRepository;

    public PermissaoColaboradorResource(
        PermissaoColaboradorService permissaoColaboradorService,
        PermissaoColaboradorRepository permissaoColaboradorRepository
    ) {
        this.permissaoColaboradorService = permissaoColaboradorService;
        this.permissaoColaboradorRepository = permissaoColaboradorRepository;
    }

    /**
     * {@code POST  /permissao-colaboradors} : Create a new permissaoColaborador.
     *
     * @param permissaoColaboradorDTO the permissaoColaboradorDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new permissaoColaboradorDTO, or with status {@code 400 (Bad Request)} if the permissaoColaborador has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PermissaoColaboradorDTO> createPermissaoColaborador(
        @Valid @RequestBody PermissaoColaboradorDTO permissaoColaboradorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save PermissaoColaborador : {}", permissaoColaboradorDTO);
        if (permissaoColaboradorDTO.getId() != null) {
            throw new BadRequestAlertException("A new permissaoColaborador cannot already have an ID", ENTITY_NAME, "idexists");
        }
        permissaoColaboradorDTO = permissaoColaboradorService.save(permissaoColaboradorDTO);
        return ResponseEntity.created(new URI("/api/permissao-colaboradors/" + permissaoColaboradorDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, permissaoColaboradorDTO.getId().toString()))
            .body(permissaoColaboradorDTO);
    }

    /**
     * {@code PUT  /permissao-colaboradors/:id} : Updates an existing permissaoColaborador.
     *
     * @param id the id of the permissaoColaboradorDTO to save.
     * @param permissaoColaboradorDTO the permissaoColaboradorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissaoColaboradorDTO,
     * or with status {@code 400 (Bad Request)} if the permissaoColaboradorDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the permissaoColaboradorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PermissaoColaboradorDTO> updatePermissaoColaborador(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PermissaoColaboradorDTO permissaoColaboradorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PermissaoColaborador : {}, {}", id, permissaoColaboradorDTO);
        if (permissaoColaboradorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissaoColaboradorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissaoColaboradorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        permissaoColaboradorDTO = permissaoColaboradorService.update(permissaoColaboradorDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, permissaoColaboradorDTO.getId().toString()))
            .body(permissaoColaboradorDTO);
    }

    /**
     * {@code PATCH  /permissao-colaboradors/:id} : Partial updates given fields of an existing permissaoColaborador, field will ignore if it is null
     *
     * @param id the id of the permissaoColaboradorDTO to save.
     * @param permissaoColaboradorDTO the permissaoColaboradorDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissaoColaboradorDTO,
     * or with status {@code 400 (Bad Request)} if the permissaoColaboradorDTO is not valid,
     * or with status {@code 404 (Not Found)} if the permissaoColaboradorDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the permissaoColaboradorDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PermissaoColaboradorDTO> partialUpdatePermissaoColaborador(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PermissaoColaboradorDTO permissaoColaboradorDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PermissaoColaborador partially : {}, {}", id, permissaoColaboradorDTO);
        if (permissaoColaboradorDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissaoColaboradorDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissaoColaboradorRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PermissaoColaboradorDTO> result = permissaoColaboradorService.partialUpdate(permissaoColaboradorDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, permissaoColaboradorDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /permissao-colaboradors} : get all the permissaoColaboradors.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of permissaoColaboradors in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PermissaoColaboradorDTO>> getAllPermissaoColaboradors(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of PermissaoColaboradors");
        Page<PermissaoColaboradorDTO> page;
        if (eagerload) {
            page = permissaoColaboradorService.findAllWithEagerRelationships(pageable);
        } else {
            page = permissaoColaboradorService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /permissao-colaboradors/:id} : get the "id" permissaoColaborador.
     *
     * @param id the id of the permissaoColaboradorDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permissaoColaboradorDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PermissaoColaboradorDTO> getPermissaoColaborador(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PermissaoColaborador : {}", id);
        Optional<PermissaoColaboradorDTO> permissaoColaboradorDTO = permissaoColaboradorService.findOne(id);
        return ResponseUtil.wrapOrNotFound(permissaoColaboradorDTO);
    }

    /**
     * {@code DELETE  /permissao-colaboradors/:id} : delete the "id" permissaoColaborador.
     *
     * @param id the id of the permissaoColaboradorDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermissaoColaborador(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PermissaoColaborador : {}", id);
        permissaoColaboradorService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
