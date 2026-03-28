package com.bankernel.web.rest;

import com.bankernel.repository.DepositoBoletoRepository;
import com.bankernel.service.DepositoBoletoService;
import com.bankernel.service.dto.DepositoBoletoDTO;
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
 * REST controller for managing {@link com.bankernel.domain.DepositoBoleto}.
 */
@RestController
@RequestMapping("/api/deposito-boletos")
public class DepositoBoletoResource {

    private static final Logger LOG = LoggerFactory.getLogger(DepositoBoletoResource.class);

    private static final String ENTITY_NAME = "depositoBoleto";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepositoBoletoService depositoBoletoService;

    private final DepositoBoletoRepository depositoBoletoRepository;

    public DepositoBoletoResource(DepositoBoletoService depositoBoletoService, DepositoBoletoRepository depositoBoletoRepository) {
        this.depositoBoletoService = depositoBoletoService;
        this.depositoBoletoRepository = depositoBoletoRepository;
    }

    /**
     * {@code POST  /deposito-boletos} : Create a new depositoBoleto.
     *
     * @param depositoBoletoDTO the depositoBoletoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depositoBoletoDTO, or with status {@code 400 (Bad Request)} if the depositoBoleto has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DepositoBoletoDTO> createDepositoBoleto(@Valid @RequestBody DepositoBoletoDTO depositoBoletoDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DepositoBoleto : {}", depositoBoletoDTO);
        if (depositoBoletoDTO.getId() != null) {
            throw new BadRequestAlertException("A new depositoBoleto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        depositoBoletoDTO = depositoBoletoService.save(depositoBoletoDTO);
        return ResponseEntity.created(new URI("/api/deposito-boletos/" + depositoBoletoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, depositoBoletoDTO.getId().toString()))
            .body(depositoBoletoDTO);
    }

    /**
     * {@code PUT  /deposito-boletos/:id} : Updates an existing depositoBoleto.
     *
     * @param id the id of the depositoBoletoDTO to save.
     * @param depositoBoletoDTO the depositoBoletoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depositoBoletoDTO,
     * or with status {@code 400 (Bad Request)} if the depositoBoletoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depositoBoletoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepositoBoletoDTO> updateDepositoBoleto(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DepositoBoletoDTO depositoBoletoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DepositoBoleto : {}, {}", id, depositoBoletoDTO);
        if (depositoBoletoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depositoBoletoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depositoBoletoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        depositoBoletoDTO = depositoBoletoService.update(depositoBoletoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depositoBoletoDTO.getId().toString()))
            .body(depositoBoletoDTO);
    }

    /**
     * {@code PATCH  /deposito-boletos/:id} : Partial updates given fields of an existing depositoBoleto, field will ignore if it is null
     *
     * @param id the id of the depositoBoletoDTO to save.
     * @param depositoBoletoDTO the depositoBoletoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depositoBoletoDTO,
     * or with status {@code 400 (Bad Request)} if the depositoBoletoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the depositoBoletoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the depositoBoletoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DepositoBoletoDTO> partialUpdateDepositoBoleto(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DepositoBoletoDTO depositoBoletoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DepositoBoleto partially : {}, {}", id, depositoBoletoDTO);
        if (depositoBoletoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depositoBoletoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depositoBoletoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepositoBoletoDTO> result = depositoBoletoService.partialUpdate(depositoBoletoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depositoBoletoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /deposito-boletos} : get all the depositoBoletos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depositoBoletos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DepositoBoletoDTO>> getAllDepositoBoletos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of DepositoBoletos");
        Page<DepositoBoletoDTO> page;
        if (eagerload) {
            page = depositoBoletoService.findAllWithEagerRelationships(pageable);
        } else {
            page = depositoBoletoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /deposito-boletos/:id} : get the "id" depositoBoleto.
     *
     * @param id the id of the depositoBoletoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depositoBoletoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepositoBoletoDTO> getDepositoBoleto(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DepositoBoleto : {}", id);
        Optional<DepositoBoletoDTO> depositoBoletoDTO = depositoBoletoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depositoBoletoDTO);
    }

    /**
     * {@code DELETE  /deposito-boletos/:id} : delete the "id" depositoBoleto.
     *
     * @param id the id of the depositoBoletoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDepositoBoleto(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DepositoBoleto : {}", id);
        depositoBoletoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
