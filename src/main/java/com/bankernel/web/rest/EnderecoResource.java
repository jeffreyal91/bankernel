package com.bankernel.web.rest;

import com.bankernel.repository.EnderecoRepository;
import com.bankernel.service.EnderecoService;
import com.bankernel.service.dto.EnderecoDTO;
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
 * REST controller for managing {@link com.bankernel.domain.Endereco}.
 */
@RestController
@RequestMapping("/api/enderecos")
public class EnderecoResource {

    private static final Logger LOG = LoggerFactory.getLogger(EnderecoResource.class);

    private static final String ENTITY_NAME = "endereco";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EnderecoService enderecoService;

    private final EnderecoRepository enderecoRepository;

    public EnderecoResource(EnderecoService enderecoService, EnderecoRepository enderecoRepository) {
        this.enderecoService = enderecoService;
        this.enderecoRepository = enderecoRepository;
    }

    /**
     * {@code POST  /enderecos} : Create a new endereco.
     *
     * @param enderecoDTO the enderecoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new enderecoDTO, or with status {@code 400 (Bad Request)} if the endereco has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EnderecoDTO> createEndereco(@Valid @RequestBody EnderecoDTO enderecoDTO) throws URISyntaxException {
        LOG.debug("REST request to save Endereco : {}", enderecoDTO);
        if (enderecoDTO.getId() != null) {
            throw new BadRequestAlertException("A new endereco cannot already have an ID", ENTITY_NAME, "idexists");
        }
        enderecoDTO = enderecoService.save(enderecoDTO);
        return ResponseEntity.created(new URI("/api/enderecos/" + enderecoDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, enderecoDTO.getId().toString()))
            .body(enderecoDTO);
    }

    /**
     * {@code PUT  /enderecos/:id} : Updates an existing endereco.
     *
     * @param id the id of the enderecoDTO to save.
     * @param enderecoDTO the enderecoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enderecoDTO,
     * or with status {@code 400 (Bad Request)} if the enderecoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the enderecoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EnderecoDTO> updateEndereco(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EnderecoDTO enderecoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Endereco : {}, {}", id, enderecoDTO);
        if (enderecoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enderecoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enderecoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        enderecoDTO = enderecoService.update(enderecoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enderecoDTO.getId().toString()))
            .body(enderecoDTO);
    }

    /**
     * {@code PATCH  /enderecos/:id} : Partial updates given fields of an existing endereco, field will ignore if it is null
     *
     * @param id the id of the enderecoDTO to save.
     * @param enderecoDTO the enderecoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated enderecoDTO,
     * or with status {@code 400 (Bad Request)} if the enderecoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the enderecoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the enderecoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EnderecoDTO> partialUpdateEndereco(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EnderecoDTO enderecoDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Endereco partially : {}, {}", id, enderecoDTO);
        if (enderecoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, enderecoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!enderecoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EnderecoDTO> result = enderecoService.partialUpdate(enderecoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, enderecoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /enderecos} : get all the enderecos.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of enderecos in body.
     */
    @GetMapping("")
    public ResponseEntity<List<EnderecoDTO>> getAllEnderecos(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of Enderecos");
        Page<EnderecoDTO> page;
        if (eagerload) {
            page = enderecoService.findAllWithEagerRelationships(pageable);
        } else {
            page = enderecoService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /enderecos/:id} : get the "id" endereco.
     *
     * @param id the id of the enderecoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the enderecoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnderecoDTO> getEndereco(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Endereco : {}", id);
        Optional<EnderecoDTO> enderecoDTO = enderecoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(enderecoDTO);
    }

    /**
     * {@code DELETE  /enderecos/:id} : delete the "id" endereco.
     *
     * @param id the id of the enderecoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEndereco(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Endereco : {}", id);
        enderecoService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
