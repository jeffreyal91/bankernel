package com.bankernel.web.rest;

import com.bankernel.repository.TipoOperacaoRepository;
import com.bankernel.service.TipoOperacaoService;
import com.bankernel.service.dto.TipoOperacaoDTO;
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
 * REST controller for managing {@link com.bankernel.domain.TipoOperacao}.
 */
@RestController
@RequestMapping("/api/tipo-operacaos")
public class TipoOperacaoResource {

    private static final Logger LOG = LoggerFactory.getLogger(TipoOperacaoResource.class);

    private static final String ENTITY_NAME = "tipoOperacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TipoOperacaoService tipoOperacaoService;

    private final TipoOperacaoRepository tipoOperacaoRepository;

    public TipoOperacaoResource(TipoOperacaoService tipoOperacaoService, TipoOperacaoRepository tipoOperacaoRepository) {
        this.tipoOperacaoService = tipoOperacaoService;
        this.tipoOperacaoRepository = tipoOperacaoRepository;
    }

    /**
     * {@code POST  /tipo-operacaos} : Create a new tipoOperacao.
     *
     * @param tipoOperacaoDTO the tipoOperacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tipoOperacaoDTO, or with status {@code 400 (Bad Request)} if the tipoOperacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TipoOperacaoDTO> createTipoOperacao(@Valid @RequestBody TipoOperacaoDTO tipoOperacaoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TipoOperacao : {}", tipoOperacaoDTO);
        if (tipoOperacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new tipoOperacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tipoOperacaoDTO = tipoOperacaoService.save(tipoOperacaoDTO);
        return ResponseEntity.created(new URI("/api/tipo-operacaos/" + tipoOperacaoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tipoOperacaoDTO.getId().toString()))
            .body(tipoOperacaoDTO);
    }

    /**
     * {@code PUT  /tipo-operacaos/:id} : Updates an existing tipoOperacao.
     *
     * @param id the id of the tipoOperacaoDTO to save.
     * @param tipoOperacaoDTO the tipoOperacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoOperacaoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoOperacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tipoOperacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TipoOperacaoDTO> updateTipoOperacao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TipoOperacaoDTO tipoOperacaoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TipoOperacao : {}, {}", id, tipoOperacaoDTO);
        if (tipoOperacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoOperacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoOperacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tipoOperacaoDTO = tipoOperacaoService.update(tipoOperacaoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoOperacaoDTO.getId().toString()))
            .body(tipoOperacaoDTO);
    }

    /**
     * {@code PATCH  /tipo-operacaos/:id} : Partial updates given fields of an existing tipoOperacao, field will ignore if it is null
     *
     * @param id the id of the tipoOperacaoDTO to save.
     * @param tipoOperacaoDTO the tipoOperacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tipoOperacaoDTO,
     * or with status {@code 400 (Bad Request)} if the tipoOperacaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tipoOperacaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tipoOperacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TipoOperacaoDTO> partialUpdateTipoOperacao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TipoOperacaoDTO tipoOperacaoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TipoOperacao partially : {}, {}", id, tipoOperacaoDTO);
        if (tipoOperacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tipoOperacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tipoOperacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TipoOperacaoDTO> result = tipoOperacaoService.partialUpdate(tipoOperacaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tipoOperacaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tipo-operacaos} : get all the tipoOperacaos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tipoOperacaos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TipoOperacaoDTO>> getAllTipoOperacaos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of TipoOperacaos");
        Page<TipoOperacaoDTO> page;
        if (eagerload) {
            page = tipoOperacaoService.findAllWithEagerRelationships(pageable);
        } else {
            page = tipoOperacaoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tipo-operacaos/:id} : get the "id" tipoOperacao.
     *
     * @param id the id of the tipoOperacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tipoOperacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TipoOperacaoDTO> getTipoOperacao(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TipoOperacao : {}", id);
        Optional<TipoOperacaoDTO> tipoOperacaoDTO = tipoOperacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tipoOperacaoDTO);
    }

    /**
     * {@code DELETE  /tipo-operacaos/:id} : delete the "id" tipoOperacao.
     *
     * @param id the id of the tipoOperacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTipoOperacao(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TipoOperacao : {}", id);
        tipoOperacaoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
