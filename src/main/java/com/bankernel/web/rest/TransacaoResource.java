package com.bankernel.web.rest;

import com.bankernel.repository.TransacaoRepository;
import com.bankernel.service.TransacaoQueryService;
import com.bankernel.service.TransacaoService;
import com.bankernel.service.criteria.TransacaoCriteria;
import com.bankernel.service.dto.TransacaoDTO;
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
 * REST controller for managing {@link com.bankernel.domain.Transacao}.
 */
@RestController
@RequestMapping("/api/transacaos")
public class TransacaoResource {

    private static final Logger LOG = LoggerFactory.getLogger(TransacaoResource.class);

    private static final String ENTITY_NAME = "transacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransacaoService transacaoService;

    private final TransacaoRepository transacaoRepository;

    private final TransacaoQueryService transacaoQueryService;

    public TransacaoResource(
        TransacaoService transacaoService,
        TransacaoRepository transacaoRepository,
        TransacaoQueryService transacaoQueryService
    ) {
        this.transacaoService = transacaoService;
        this.transacaoRepository = transacaoRepository;
        this.transacaoQueryService = transacaoQueryService;
    }

    /**
     * {@code POST  /transacaos} : Create a new transacao.
     *
     * @param transacaoDTO the transacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transacaoDTO, or with status {@code 400 (Bad Request)} if the transacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransacaoDTO> createTransacao(@Valid @RequestBody TransacaoDTO transacaoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Transacao : {}", transacaoDTO);
        if (transacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new transacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transacaoDTO = transacaoService.save(transacaoDTO);
        return ResponseEntity.created(new URI("/api/transacaos/" + transacaoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transacaoDTO.getId().toString()))
            .body(transacaoDTO);
    }

    /**
     * {@code PUT  /transacaos/:id} : Updates an existing transacao.
     *
     * @param id the id of the transacaoDTO to save.
     * @param transacaoDTO the transacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transacaoDTO,
     * or with status {@code 400 (Bad Request)} if the transacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransacaoDTO> updateTransacao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransacaoDTO transacaoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Transacao : {}, {}", id, transacaoDTO);
        if (transacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transacaoDTO = transacaoService.update(transacaoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transacaoDTO.getId().toString()))
            .body(transacaoDTO);
    }

    /**
     * {@code PATCH  /transacaos/:id} : Partial updates given fields of an existing transacao, field will ignore if it is null
     *
     * @param id the id of the transacaoDTO to save.
     * @param transacaoDTO the transacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transacaoDTO,
     * or with status {@code 400 (Bad Request)} if the transacaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transacaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransacaoDTO> partialUpdateTransacao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransacaoDTO transacaoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Transacao partially : {}, {}", id, transacaoDTO);
        if (transacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransacaoDTO> result = transacaoService.partialUpdate(transacaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transacaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transacaos} : get all the transacaos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transacaos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransacaoDTO>> getAllTransacaos(
        TransacaoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Transacaos by criteria: {}", criteria);

        Page<TransacaoDTO> page = transacaoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transacaos/count} : count all the transacaos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTransacaos(TransacaoCriteria criteria) {
        LOG.debug("REST request to count Transacaos by criteria: {}", criteria);
        return ResponseEntity.ok().body(transacaoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transacaos/:id} : get the "id" transacao.
     *
     * @param id the id of the transacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransacaoDTO> getTransacao(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Transacao : {}", id);
        Optional<TransacaoDTO> transacaoDTO = transacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transacaoDTO);
    }

    /**
     * {@code DELETE  /transacaos/:id} : delete the "id" transacao.
     *
     * @param id the id of the transacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransacao(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Transacao : {}", id);
        transacaoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
