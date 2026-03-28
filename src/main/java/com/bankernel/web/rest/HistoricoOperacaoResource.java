package com.bankernel.web.rest;

import com.bankernel.repository.HistoricoOperacaoRepository;
import com.bankernel.service.HistoricoOperacaoQueryService;
import com.bankernel.service.HistoricoOperacaoService;
import com.bankernel.service.criteria.HistoricoOperacaoCriteria;
import com.bankernel.service.dto.HistoricoOperacaoDTO;
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
 * REST controller for managing {@link com.bankernel.domain.HistoricoOperacao}.
 */
@RestController
@RequestMapping("/api/historico-operacaos")
public class HistoricoOperacaoResource {

    private static final Logger LOG = LoggerFactory.getLogger(HistoricoOperacaoResource.class);

    private static final String ENTITY_NAME = "historicoOperacao";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HistoricoOperacaoService historicoOperacaoService;

    private final HistoricoOperacaoRepository historicoOperacaoRepository;

    private final HistoricoOperacaoQueryService historicoOperacaoQueryService;

    public HistoricoOperacaoResource(
        HistoricoOperacaoService historicoOperacaoService,
        HistoricoOperacaoRepository historicoOperacaoRepository,
        HistoricoOperacaoQueryService historicoOperacaoQueryService
    ) {
        this.historicoOperacaoService = historicoOperacaoService;
        this.historicoOperacaoRepository = historicoOperacaoRepository;
        this.historicoOperacaoQueryService = historicoOperacaoQueryService;
    }

    /**
     * {@code POST  /historico-operacaos} : Create a new historicoOperacao.
     *
     * @param historicoOperacaoDTO the historicoOperacaoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new historicoOperacaoDTO, or with status {@code 400 (Bad Request)} if the historicoOperacao has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HistoricoOperacaoDTO> createHistoricoOperacao(@Valid @RequestBody HistoricoOperacaoDTO historicoOperacaoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HistoricoOperacao : {}", historicoOperacaoDTO);
        if (historicoOperacaoDTO.getId() != null) {
            throw new BadRequestAlertException("A new historicoOperacao cannot already have an ID", ENTITY_NAME, "idexists");
        }
        historicoOperacaoDTO = historicoOperacaoService.save(historicoOperacaoDTO);
        return ResponseEntity.created(new URI("/api/historico-operacaos/" + historicoOperacaoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, historicoOperacaoDTO.getId().toString()))
            .body(historicoOperacaoDTO);
    }

    /**
     * {@code PUT  /historico-operacaos/:id} : Updates an existing historicoOperacao.
     *
     * @param id the id of the historicoOperacaoDTO to save.
     * @param historicoOperacaoDTO the historicoOperacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historicoOperacaoDTO,
     * or with status {@code 400 (Bad Request)} if the historicoOperacaoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the historicoOperacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HistoricoOperacaoDTO> updateHistoricoOperacao(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HistoricoOperacaoDTO historicoOperacaoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HistoricoOperacao : {}, {}", id, historicoOperacaoDTO);
        if (historicoOperacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historicoOperacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historicoOperacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        historicoOperacaoDTO = historicoOperacaoService.update(historicoOperacaoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historicoOperacaoDTO.getId().toString()))
            .body(historicoOperacaoDTO);
    }

    /**
     * {@code PATCH  /historico-operacaos/:id} : Partial updates given fields of an existing historicoOperacao, field will ignore if it is null
     *
     * @param id the id of the historicoOperacaoDTO to save.
     * @param historicoOperacaoDTO the historicoOperacaoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated historicoOperacaoDTO,
     * or with status {@code 400 (Bad Request)} if the historicoOperacaoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the historicoOperacaoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the historicoOperacaoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HistoricoOperacaoDTO> partialUpdateHistoricoOperacao(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HistoricoOperacaoDTO historicoOperacaoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HistoricoOperacao partially : {}, {}", id, historicoOperacaoDTO);
        if (historicoOperacaoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, historicoOperacaoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!historicoOperacaoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HistoricoOperacaoDTO> result = historicoOperacaoService.partialUpdate(historicoOperacaoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, historicoOperacaoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /historico-operacaos} : get all the historicoOperacaos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of historicoOperacaos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HistoricoOperacaoDTO>> getAllHistoricoOperacaos(
        HistoricoOperacaoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HistoricoOperacaos by criteria: {}", criteria);

        Page<HistoricoOperacaoDTO> page = historicoOperacaoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /historico-operacaos/count} : count all the historicoOperacaos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHistoricoOperacaos(HistoricoOperacaoCriteria criteria) {
        LOG.debug("REST request to count HistoricoOperacaos by criteria: {}", criteria);
        return ResponseEntity.ok().body(historicoOperacaoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /historico-operacaos/:id} : get the "id" historicoOperacao.
     *
     * @param id the id of the historicoOperacaoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the historicoOperacaoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HistoricoOperacaoDTO> getHistoricoOperacao(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HistoricoOperacao : {}", id);
        Optional<HistoricoOperacaoDTO> historicoOperacaoDTO = historicoOperacaoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(historicoOperacaoDTO);
    }

    /**
     * {@code DELETE  /historico-operacaos/:id} : delete the "id" historicoOperacao.
     *
     * @param id the id of the historicoOperacaoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHistoricoOperacao(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HistoricoOperacao : {}", id);
        historicoOperacaoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
