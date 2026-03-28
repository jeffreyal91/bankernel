package com.bankernel.web.rest;

import com.bankernel.repository.DepositoRepository;
import com.bankernel.service.DepositoQueryService;
import com.bankernel.service.DepositoService;
import com.bankernel.service.criteria.DepositoCriteria;
import com.bankernel.service.dto.DepositoDTO;
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
 * REST controller for managing {@link com.bankernel.domain.Deposito}.
 */
@RestController
@RequestMapping("/api/depositos")
public class DepositoResource {

    private static final Logger LOG = LoggerFactory.getLogger(DepositoResource.class);

    private static final String ENTITY_NAME = "deposito";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepositoService depositoService;

    private final DepositoRepository depositoRepository;

    private final DepositoQueryService depositoQueryService;

    public DepositoResource(
        DepositoService depositoService,
        DepositoRepository depositoRepository,
        DepositoQueryService depositoQueryService
    ) {
        this.depositoService = depositoService;
        this.depositoRepository = depositoRepository;
        this.depositoQueryService = depositoQueryService;
    }

    /**
     * {@code POST  /depositos} : Create a new deposito.
     *
     * @param depositoDTO the depositoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depositoDTO, or with status {@code 400 (Bad Request)} if the deposito has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DepositoDTO> createDeposito(@Valid @RequestBody DepositoDTO depositoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Deposito : {}", depositoDTO);
        if (depositoDTO.getId() != null) {
            throw new BadRequestAlertException("A new deposito cannot already have an ID", ENTITY_NAME, "idexists");
        }
        depositoDTO = depositoService.save(depositoDTO);
        return ResponseEntity.created(new URI("/api/depositos/" + depositoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, depositoDTO.getId().toString()))
            .body(depositoDTO);
    }

    /**
     * {@code PUT  /depositos/:id} : Updates an existing deposito.
     *
     * @param id the id of the depositoDTO to save.
     * @param depositoDTO the depositoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depositoDTO,
     * or with status {@code 400 (Bad Request)} if the depositoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depositoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DepositoDTO> updateDeposito(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DepositoDTO depositoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Deposito : {}, {}", id, depositoDTO);
        if (depositoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depositoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depositoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        depositoDTO = depositoService.update(depositoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depositoDTO.getId().toString()))
            .body(depositoDTO);
    }

    /**
     * {@code PATCH  /depositos/:id} : Partial updates given fields of an existing deposito, field will ignore if it is null
     *
     * @param id the id of the depositoDTO to save.
     * @param depositoDTO the depositoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depositoDTO,
     * or with status {@code 400 (Bad Request)} if the depositoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the depositoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the depositoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DepositoDTO> partialUpdateDeposito(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DepositoDTO depositoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Deposito partially : {}, {}", id, depositoDTO);
        if (depositoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depositoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depositoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepositoDTO> result = depositoService.partialUpdate(depositoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depositoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /depositos} : get all the depositos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depositos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DepositoDTO>> getAllDepositos(
        DepositoCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Depositos by criteria: {}", criteria);

        Page<DepositoDTO> page = depositoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /depositos/count} : count all the depositos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countDepositos(DepositoCriteria criteria) {
        LOG.debug("REST request to count Depositos by criteria: {}", criteria);
        return ResponseEntity.ok().body(depositoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /depositos/:id} : get the "id" deposito.
     *
     * @param id the id of the depositoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depositoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DepositoDTO> getDeposito(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Deposito : {}", id);
        Optional<DepositoDTO> depositoDTO = depositoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depositoDTO);
    }

    /**
     * {@code DELETE  /depositos/:id} : delete the "id" deposito.
     *
     * @param id the id of the depositoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDeposito(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Deposito : {}", id);
        depositoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
