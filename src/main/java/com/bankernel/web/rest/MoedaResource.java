package com.bankernel.web.rest;

import com.bankernel.repository.MoedaRepository;
import com.bankernel.service.MoedaService;
import com.bankernel.service.dto.MoedaDTO;
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
 * REST controller for managing {@link com.bankernel.domain.Moeda}.
 */
@RestController
@RequestMapping("/api/moedas")
public class MoedaResource {

    private static final Logger LOG = LoggerFactory.getLogger(MoedaResource.class);

    private static final String ENTITY_NAME = "moeda";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoedaService moedaService;

    private final MoedaRepository moedaRepository;

    public MoedaResource(MoedaService moedaService, MoedaRepository moedaRepository) {
        this.moedaService = moedaService;
        this.moedaRepository = moedaRepository;
    }

    /**
     * {@code POST  /moedas} : Create a new moeda.
     *
     * @param moedaDTO the moedaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moedaDTO, or with status {@code 400 (Bad Request)} if the moeda has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MoedaDTO> createMoeda(@Valid @RequestBody MoedaDTO moedaDTO) throws URISyntaxException {
        LOG.debug("REST request to save Moeda : {}", moedaDTO);
        if (moedaDTO.getId() != null) {
            throw new BadRequestAlertException("A new moeda cannot already have an ID", ENTITY_NAME, "idexists");
        }
        moedaDTO = moedaService.save(moedaDTO);
        return ResponseEntity.created(new URI("/api/moedas/" + moedaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, moedaDTO.getId().toString()))
            .body(moedaDTO);
    }

    /**
     * {@code PUT  /moedas/:id} : Updates an existing moeda.
     *
     * @param id the id of the moedaDTO to save.
     * @param moedaDTO the moedaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moedaDTO,
     * or with status {@code 400 (Bad Request)} if the moedaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moedaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MoedaDTO> updateMoeda(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MoedaDTO moedaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Moeda : {}, {}", id, moedaDTO);
        if (moedaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moedaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moedaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        moedaDTO = moedaService.update(moedaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moedaDTO.getId().toString()))
            .body(moedaDTO);
    }

    /**
     * {@code PATCH  /moedas/:id} : Partial updates given fields of an existing moeda, field will ignore if it is null
     *
     * @param id the id of the moedaDTO to save.
     * @param moedaDTO the moedaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moedaDTO,
     * or with status {@code 400 (Bad Request)} if the moedaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moedaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moedaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MoedaDTO> partialUpdateMoeda(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MoedaDTO moedaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Moeda partially : {}, {}", id, moedaDTO);
        if (moedaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moedaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moedaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MoedaDTO> result = moedaService.partialUpdate(moedaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moedaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /moedas} : get all the moedas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moedas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MoedaDTO>> getAllMoedas(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Moedas");
        Page<MoedaDTO> page = moedaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /moedas/:id} : get the "id" moeda.
     *
     * @param id the id of the moedaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moedaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MoedaDTO> getMoeda(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Moeda : {}", id);
        Optional<MoedaDTO> moedaDTO = moedaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moedaDTO);
    }

    /**
     * {@code DELETE  /moedas/:id} : delete the "id" moeda.
     *
     * @param id the id of the moedaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoeda(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Moeda : {}", id);
        moedaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
