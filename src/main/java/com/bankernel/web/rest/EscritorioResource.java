package com.bankernel.web.rest;

import com.bankernel.repository.EscritorioRepository;
import com.bankernel.service.EscritorioService;
import com.bankernel.service.dto.EscritorioDTO;
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
 * REST controller for managing {@link com.bankernel.domain.Escritorio}.
 */
@RestController
@RequestMapping("/api/escritorios")
public class EscritorioResource {

    private static final Logger LOG = LoggerFactory.getLogger(EscritorioResource.class);

    private static final String ENTITY_NAME = "escritorio";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EscritorioService escritorioService;

    private final EscritorioRepository escritorioRepository;

    public EscritorioResource(EscritorioService escritorioService, EscritorioRepository escritorioRepository) {
        this.escritorioService = escritorioService;
        this.escritorioRepository = escritorioRepository;
    }

    /**
     * {@code POST  /escritorios} : Create a new escritorio.
     *
     * @param escritorioDTO the escritorioDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new escritorioDTO, or with status {@code 400 (Bad Request)} if the escritorio has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EscritorioDTO> createEscritorio(@Valid @RequestBody EscritorioDTO escritorioDTO) throws URISyntaxException {
        LOG.debug("REST request to save Escritorio : {}", escritorioDTO);
        if (escritorioDTO.getId() != null) {
            throw new BadRequestAlertException("A new escritorio cannot already have an ID", ENTITY_NAME, "idexists");
        }
        escritorioDTO = escritorioService.save(escritorioDTO);
        return ResponseEntity.created(new URI("/api/escritorios/" + escritorioDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, escritorioDTO.getId().toString()))
            .body(escritorioDTO);
    }

    /**
     * {@code PUT  /escritorios/:id} : Updates an existing escritorio.
     *
     * @param id the id of the escritorioDTO to save.
     * @param escritorioDTO the escritorioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated escritorioDTO,
     * or with status {@code 400 (Bad Request)} if the escritorioDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the escritorioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EscritorioDTO> updateEscritorio(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EscritorioDTO escritorioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Escritorio : {}, {}", id, escritorioDTO);
        if (escritorioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, escritorioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!escritorioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        escritorioDTO = escritorioService.update(escritorioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, escritorioDTO.getId().toString()))
            .body(escritorioDTO);
    }

    /**
     * {@code PATCH  /escritorios/:id} : Partial updates given fields of an existing escritorio, field will ignore if it is null
     *
     * @param id the id of the escritorioDTO to save.
     * @param escritorioDTO the escritorioDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated escritorioDTO,
     * or with status {@code 400 (Bad Request)} if the escritorioDTO is not valid,
     * or with status {@code 404 (Not Found)} if the escritorioDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the escritorioDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EscritorioDTO> partialUpdateEscritorio(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EscritorioDTO escritorioDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Escritorio partially : {}, {}", id, escritorioDTO);
        if (escritorioDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, escritorioDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!escritorioRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EscritorioDTO> result = escritorioService.partialUpdate(escritorioDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, escritorioDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /escritorios} : get all the escritorios.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of escritorios in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EscritorioDTO>> getAllEscritorios(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Escritorios");
        Page<EscritorioDTO> page = escritorioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /escritorios/:id} : get the "id" escritorio.
     *
     * @param id the id of the escritorioDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the escritorioDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EscritorioDTO> getEscritorio(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Escritorio : {}", id);
        Optional<EscritorioDTO> escritorioDTO = escritorioService.findOne(id);
        return ResponseUtil.wrapOrNotFound(escritorioDTO);
    }

    /**
     * {@code DELETE  /escritorios/:id} : delete the "id" escritorio.
     *
     * @param id the id of the escritorioDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEscritorio(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Escritorio : {}", id);
        escritorioService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
