package com.bankernel.web.rest;

import com.bankernel.repository.FeriadoRepository;
import com.bankernel.service.FeriadoService;
import com.bankernel.service.dto.FeriadoDTO;
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
 * REST controller for managing {@link com.bankernel.domain.Feriado}.
 */
@RestController
@RequestMapping("/api/feriados")
public class FeriadoResource {

    private static final Logger LOG = LoggerFactory.getLogger(FeriadoResource.class);

    private static final String ENTITY_NAME = "feriado";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FeriadoService feriadoService;

    private final FeriadoRepository feriadoRepository;

    public FeriadoResource(FeriadoService feriadoService, FeriadoRepository feriadoRepository) {
        this.feriadoService = feriadoService;
        this.feriadoRepository = feriadoRepository;
    }

    /**
     * {@code POST  /feriados} : Create a new feriado.
     *
     * @param feriadoDTO the feriadoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new feriadoDTO, or with status {@code 400 (Bad Request)} if the feriado has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<FeriadoDTO> createFeriado(@Valid @RequestBody FeriadoDTO feriadoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Feriado : {}", feriadoDTO);
        if (feriadoDTO.getId() != null) {
            throw new BadRequestAlertException("A new feriado cannot already have an ID", ENTITY_NAME, "idexists");
        }
        feriadoDTO = feriadoService.save(feriadoDTO);
        return ResponseEntity.created(new URI("/api/feriados/" + feriadoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, feriadoDTO.getId().toString()))
            .body(feriadoDTO);
    }

    /**
     * {@code PUT  /feriados/:id} : Updates an existing feriado.
     *
     * @param id the id of the feriadoDTO to save.
     * @param feriadoDTO the feriadoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feriadoDTO,
     * or with status {@code 400 (Bad Request)} if the feriadoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the feriadoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FeriadoDTO> updateFeriado(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FeriadoDTO feriadoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Feriado : {}, {}", id, feriadoDTO);
        if (feriadoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feriadoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feriadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        feriadoDTO = feriadoService.update(feriadoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feriadoDTO.getId().toString()))
            .body(feriadoDTO);
    }

    /**
     * {@code PATCH  /feriados/:id} : Partial updates given fields of an existing feriado, field will ignore if it is null
     *
     * @param id the id of the feriadoDTO to save.
     * @param feriadoDTO the feriadoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated feriadoDTO,
     * or with status {@code 400 (Bad Request)} if the feriadoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the feriadoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the feriadoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FeriadoDTO> partialUpdateFeriado(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FeriadoDTO feriadoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Feriado partially : {}, {}", id, feriadoDTO);
        if (feriadoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, feriadoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!feriadoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FeriadoDTO> result = feriadoService.partialUpdate(feriadoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, feriadoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /feriados} : get all the feriados.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of feriados in body.
     */
    @GetMapping("")
    public ResponseEntity<List<FeriadoDTO>> getAllFeriados(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Feriados");
        Page<FeriadoDTO> page = feriadoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /feriados/:id} : get the "id" feriado.
     *
     * @param id the id of the feriadoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the feriadoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FeriadoDTO> getFeriado(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Feriado : {}", id);
        Optional<FeriadoDTO> feriadoDTO = feriadoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(feriadoDTO);
    }

    /**
     * {@code DELETE  /feriados/:id} : delete the "id" feriado.
     *
     * @param id the id of the feriadoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeriado(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Feriado : {}", id);
        feriadoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
