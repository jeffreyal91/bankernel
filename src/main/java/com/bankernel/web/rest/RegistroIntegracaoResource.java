package com.bankernel.web.rest;

import com.bankernel.repository.RegistroIntegracaoRepository;
import com.bankernel.service.RegistroIntegracaoQueryService;
import com.bankernel.service.RegistroIntegracaoService;
import com.bankernel.service.criteria.RegistroIntegracaoCriteria;
import com.bankernel.service.dto.RegistroIntegracaoDTO;
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
 * REST controller for managing {@link com.bankernel.domain.RegistroIntegracao}.
 */
@RestController
@RequestMapping("/api/registro-integracaos")
public class RegistroIntegracaoResource {

    private static final Logger LOG = LoggerFactory.getLogger(RegistroIntegracaoResource.class);

    private static final String ENTITY_NAME = "registroIntegracao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RegistroIntegracaoService registroIntegracaoService;

    private final RegistroIntegracaoRepository registroIntegracaoRepository;

    private final RegistroIntegracaoQueryService registroIntegracaoQueryService;

    public RegistroIntegracaoResource(
        RegistroIntegracaoService registroIntegracaoService,
        RegistroIntegracaoRepository registroIntegracaoRepository,
        RegistroIntegracaoQueryService registroIntegracaoQueryService
    ) {
        this.registroIntegracaoService = registroIntegracaoService;
        this.registroIntegracaoRepository = registroIntegracaoRepository;
        this.registroIntegracaoQueryService = registroIntegracaoQueryService;
    }

    /**
     * {@code POST  /registro-integracaos} : Create a new registroIntegracao.
     *
     * @param registroIntegracaoDTO the registroIntegracaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new registroIntegracaoDTO, or with status {@code 400 (Bad Request)} if the registroIntegracao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<RegistroIntegracaoDTO> createRegistroIntegracao(@Valid @RequestBody RegistroIntegracaoDTO registroIntegracaoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save RegistroIntegracao : {}", registroIntegracaoDTO);
        if (registroIntegracaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new registroIntegracao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        registroIntegracaoDTO = registroIntegracaoService.save(registroIntegracaoDTO);
        return ResponseEntity.created(new URI("/api/registro-integracaos/" + registroIntegracaoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, registroIntegracaoDTO.getId().toString()))
            .body(registroIntegracaoDTO);
    }

    /**
     * {@code PUT  /registro-integracaos/:id} : Updates an existing registroIntegracao.
     *
     * @param id the id of the registroIntegracaoDTO to save.
     * @param registroIntegracaoDTO the registroIntegracaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registroIntegracaoDTO,
     * or with status {@code 400 (Bad Request)} if the registroIntegracaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the registroIntegracaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<RegistroIntegracaoDTO> updateRegistroIntegracao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RegistroIntegracaoDTO registroIntegracaoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update RegistroIntegracao : {}, {}", id, registroIntegracaoDTO);
        if (registroIntegracaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, registroIntegracaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!registroIntegracaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        registroIntegracaoDTO = registroIntegracaoService.update(registroIntegracaoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, registroIntegracaoDTO.getId().toString()))
            .body(registroIntegracaoDTO);
    }

    /**
     * {@code PATCH  /registro-integracaos/:id} : Partial updates given fields of an existing registroIntegracao, field will ignore if it is null
     *
     * @param id the id of the registroIntegracaoDTO to save.
     * @param registroIntegracaoDTO the registroIntegracaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated registroIntegracaoDTO,
     * or with status {@code 400 (Bad Request)} if the registroIntegracaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the registroIntegracaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the registroIntegracaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RegistroIntegracaoDTO> partialUpdateRegistroIntegracao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RegistroIntegracaoDTO registroIntegracaoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update RegistroIntegracao partially : {}, {}", id, registroIntegracaoDTO);
        if (registroIntegracaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, registroIntegracaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!registroIntegracaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RegistroIntegracaoDTO> result = registroIntegracaoService.partialUpdate(registroIntegracaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, registroIntegracaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /registro-integracaos} : get all the registroIntegracaos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of registroIntegracaos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<RegistroIntegracaoDTO>> getAllRegistroIntegracaos(
        RegistroIntegracaoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get RegistroIntegracaos by criteria: {}", criteria);

        Page<RegistroIntegracaoDTO> page = registroIntegracaoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /registro-integracaos/count} : count all the registroIntegracaos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countRegistroIntegracaos(RegistroIntegracaoCriteria criteria) {
        LOG.debug("REST request to count RegistroIntegracaos by criteria: {}", criteria);
        return ResponseEntity.ok().body(registroIntegracaoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /registro-integracaos/:id} : get the "id" registroIntegracao.
     *
     * @param id the id of the registroIntegracaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the registroIntegracaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RegistroIntegracaoDTO> getRegistroIntegracao(@PathVariable("id") Long id) {
        LOG.debug("REST request to get RegistroIntegracao : {}", id);
        Optional<RegistroIntegracaoDTO> registroIntegracaoDTO = registroIntegracaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(registroIntegracaoDTO);
    }

    /**
     * {@code DELETE  /registro-integracaos/:id} : delete the "id" registroIntegracao.
     *
     * @param id the id of the registroIntegracaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRegistroIntegracao(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete RegistroIntegracao : {}", id);
        registroIntegracaoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
