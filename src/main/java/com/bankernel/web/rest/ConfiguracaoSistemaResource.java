package com.bankernel.web.rest;

import com.bankernel.repository.ConfiguracaoSistemaRepository;
import com.bankernel.service.ConfiguracaoSistemaService;
import com.bankernel.service.dto.ConfiguracaoSistemaDTO;
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
 * REST controller for managing {@link com.bankernel.domain.ConfiguracaoSistema}.
 */
@RestController
@RequestMapping("/api/configuracao-sistemas")
public class ConfiguracaoSistemaResource {

    private static final Logger LOG = LoggerFactory.getLogger(ConfiguracaoSistemaResource.class);

    private static final String ENTITY_NAME = "configuracaoSistema";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfiguracaoSistemaService configuracaoSistemaService;

    private final ConfiguracaoSistemaRepository configuracaoSistemaRepository;

    public ConfiguracaoSistemaResource(
        ConfiguracaoSistemaService configuracaoSistemaService,
        ConfiguracaoSistemaRepository configuracaoSistemaRepository
    ) {
        this.configuracaoSistemaService = configuracaoSistemaService;
        this.configuracaoSistemaRepository = configuracaoSistemaRepository;
    }

    /**
     * {@code POST  /configuracao-sistemas} : Create a new configuracaoSistema.
     *
     * @param configuracaoSistemaDTO the configuracaoSistemaDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configuracaoSistemaDTO, or with status {@code 400 (Bad Request)} if the configuracaoSistema has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ConfiguracaoSistemaDTO> createConfiguracaoSistema(
        @Valid @RequestBody ConfiguracaoSistemaDTO configuracaoSistemaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ConfiguracaoSistema : {}", configuracaoSistemaDTO);
        if (configuracaoSistemaDTO.getId() != null) {
            throw new BadRequestAlertException("A new configuracaoSistema cannot already have an ID", ENTITY_NAME, "idexists");
        }
        configuracaoSistemaDTO = configuracaoSistemaService.save(configuracaoSistemaDTO);
        return ResponseEntity.created(new URI("/api/configuracao-sistemas/" + configuracaoSistemaDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, configuracaoSistemaDTO.getId().toString()))
            .body(configuracaoSistemaDTO);
    }

    /**
     * {@code PUT  /configuracao-sistemas/:id} : Updates an existing configuracaoSistema.
     *
     * @param id the id of the configuracaoSistemaDTO to save.
     * @param configuracaoSistemaDTO the configuracaoSistemaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configuracaoSistemaDTO,
     * or with status {@code 400 (Bad Request)} if the configuracaoSistemaDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configuracaoSistemaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConfiguracaoSistemaDTO> updateConfiguracaoSistema(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConfiguracaoSistemaDTO configuracaoSistemaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ConfiguracaoSistema : {}, {}", id, configuracaoSistemaDTO);
        if (configuracaoSistemaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configuracaoSistemaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configuracaoSistemaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        configuracaoSistemaDTO = configuracaoSistemaService.update(configuracaoSistemaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configuracaoSistemaDTO.getId().toString()))
            .body(configuracaoSistemaDTO);
    }

    /**
     * {@code PATCH  /configuracao-sistemas/:id} : Partial updates given fields of an existing configuracaoSistema, field will ignore if it is null
     *
     * @param id the id of the configuracaoSistemaDTO to save.
     * @param configuracaoSistemaDTO the configuracaoSistemaDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configuracaoSistemaDTO,
     * or with status {@code 400 (Bad Request)} if the configuracaoSistemaDTO is not valid,
     * or with status {@code 404 (Not Found)} if the configuracaoSistemaDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the configuracaoSistemaDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConfiguracaoSistemaDTO> partialUpdateConfiguracaoSistema(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConfiguracaoSistemaDTO configuracaoSistemaDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ConfiguracaoSistema partially : {}, {}", id, configuracaoSistemaDTO);
        if (configuracaoSistemaDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configuracaoSistemaDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configuracaoSistemaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConfiguracaoSistemaDTO> result = configuracaoSistemaService.partialUpdate(configuracaoSistemaDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configuracaoSistemaDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /configuracao-sistemas} : get all the configuracaoSistemas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configuracaoSistemas in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ConfiguracaoSistemaDTO>> getAllConfiguracaoSistemas(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ConfiguracaoSistemas");
        Page<ConfiguracaoSistemaDTO> page = configuracaoSistemaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /configuracao-sistemas/:id} : get the "id" configuracaoSistema.
     *
     * @param id the id of the configuracaoSistemaDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configuracaoSistemaDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConfiguracaoSistemaDTO> getConfiguracaoSistema(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ConfiguracaoSistema : {}", id);
        Optional<ConfiguracaoSistemaDTO> configuracaoSistemaDTO = configuracaoSistemaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(configuracaoSistemaDTO);
    }

    /**
     * {@code DELETE  /configuracao-sistemas/:id} : delete the "id" configuracaoSistema.
     *
     * @param id the id of the configuracaoSistemaDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfiguracaoSistema(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ConfiguracaoSistema : {}", id);
        configuracaoSistemaService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
