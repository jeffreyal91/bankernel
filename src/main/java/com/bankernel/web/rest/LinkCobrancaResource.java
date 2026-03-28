package com.bankernel.web.rest;

import com.bankernel.repository.LinkCobrancaRepository;
import com.bankernel.service.LinkCobrancaService;
import com.bankernel.service.dto.LinkCobrancaDTO;
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
 * REST controller for managing {@link com.bankernel.domain.LinkCobranca}.
 */
@RestController
@RequestMapping("/api/link-cobrancas")
public class LinkCobrancaResource {

    private static final Logger LOG = LoggerFactory.getLogger(LinkCobrancaResource.class);

    private static final String ENTITY_NAME = "linkCobranca";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LinkCobrancaService linkCobrancaService;

    private final LinkCobrancaRepository linkCobrancaRepository;

    public LinkCobrancaResource(LinkCobrancaService linkCobrancaService, LinkCobrancaRepository linkCobrancaRepository) {
        this.linkCobrancaService = linkCobrancaService;
        this.linkCobrancaRepository = linkCobrancaRepository;
    }

    /**
     * {@code POST  /link-cobrancas} : Create a new linkCobranca.
     *
     * @param linkCobrancaDTO the linkCobrancaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new linkCobrancaDTO, or with status {@code 400 (Bad Request)} if the linkCobranca has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LinkCobrancaDTO> createLinkCobranca(@Valid @RequestBody LinkCobrancaDTO linkCobrancaDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save LinkCobranca : {}", linkCobrancaDTO);
        if (linkCobrancaDTO.getId() != null) {
            throw new BadRequestAlertException("A new linkCobranca cannot already have an ID", ENTITY_NAME, "idexists");
        }
        linkCobrancaDTO = linkCobrancaService.save(linkCobrancaDTO);
        return ResponseEntity.created(new URI("/api/link-cobrancas/" + linkCobrancaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, linkCobrancaDTO.getId().toString()))
            .body(linkCobrancaDTO);
    }

    /**
     * {@code PUT  /link-cobrancas/:id} : Updates an existing linkCobranca.
     *
     * @param id the id of the linkCobrancaDTO to save.
     * @param linkCobrancaDTO the linkCobrancaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated linkCobrancaDTO,
     * or with status {@code 400 (Bad Request)} if the linkCobrancaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the linkCobrancaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LinkCobrancaDTO> updateLinkCobranca(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LinkCobrancaDTO linkCobrancaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LinkCobranca : {}, {}", id, linkCobrancaDTO);
        if (linkCobrancaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, linkCobrancaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!linkCobrancaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        linkCobrancaDTO = linkCobrancaService.update(linkCobrancaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, linkCobrancaDTO.getId().toString()))
            .body(linkCobrancaDTO);
    }

    /**
     * {@code PATCH  /link-cobrancas/:id} : Partial updates given fields of an existing linkCobranca, field will ignore if it is null
     *
     * @param id the id of the linkCobrancaDTO to save.
     * @param linkCobrancaDTO the linkCobrancaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated linkCobrancaDTO,
     * or with status {@code 400 (Bad Request)} if the linkCobrancaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the linkCobrancaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the linkCobrancaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LinkCobrancaDTO> partialUpdateLinkCobranca(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LinkCobrancaDTO linkCobrancaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LinkCobranca partially : {}, {}", id, linkCobrancaDTO);
        if (linkCobrancaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, linkCobrancaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!linkCobrancaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LinkCobrancaDTO> result = linkCobrancaService.partialUpdate(linkCobrancaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, linkCobrancaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /link-cobrancas} : get all the linkCobrancas.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of linkCobrancas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LinkCobrancaDTO>> getAllLinkCobrancas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of LinkCobrancas");
        Page<LinkCobrancaDTO> page;
        if (eagerload) {
            page = linkCobrancaService.findAllWithEagerRelationships(pageable);
        } else {
            page = linkCobrancaService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /link-cobrancas/:id} : get the "id" linkCobranca.
     *
     * @param id the id of the linkCobrancaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the linkCobrancaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LinkCobrancaDTO> getLinkCobranca(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LinkCobranca : {}", id);
        Optional<LinkCobrancaDTO> linkCobrancaDTO = linkCobrancaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(linkCobrancaDTO);
    }

    /**
     * {@code DELETE  /link-cobrancas/:id} : delete the "id" linkCobranca.
     *
     * @param id the id of the linkCobrancaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLinkCobranca(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LinkCobranca : {}", id);
        linkCobrancaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
