package com.bankernel.web.rest;

import com.bankernel.repository.MoedaCarteiraRepository;
import com.bankernel.service.MoedaCarteiraService;
import com.bankernel.service.dto.MoedaCarteiraDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.bankernel.domain.MoedaCarteira}.
 */
@RestController
@RequestMapping("/api/moeda-carteiras")
public class MoedaCarteiraResource {

    private static final Logger LOG = LoggerFactory.getLogger(MoedaCarteiraResource.class);

    private static final String ENTITY_NAME = "moedaCarteira";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoedaCarteiraService moedaCarteiraService;

    private final MoedaCarteiraRepository moedaCarteiraRepository;

    public MoedaCarteiraResource(MoedaCarteiraService moedaCarteiraService, MoedaCarteiraRepository moedaCarteiraRepository) {
        this.moedaCarteiraService = moedaCarteiraService;
        this.moedaCarteiraRepository = moedaCarteiraRepository;
    }

    /**
     * {@code POST  /moeda-carteiras} : Create a new moedaCarteira.
     *
     * @param moedaCarteiraDTO the moedaCarteiraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moedaCarteiraDTO, or with status {@code 400 (Bad Request)} if the moedaCarteira has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MoedaCarteiraDTO> createMoedaCarteira(@Valid @RequestBody MoedaCarteiraDTO moedaCarteiraDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MoedaCarteira : {}", moedaCarteiraDTO);
        if (moedaCarteiraDTO.getId() != null) {
            throw new BadRequestAlertException("A new moedaCarteira cannot already have an ID", ENTITY_NAME, "idexists");
        }
        moedaCarteiraDTO = moedaCarteiraService.save(moedaCarteiraDTO);
        return ResponseEntity.created(new URI("/api/moeda-carteiras/" + moedaCarteiraDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, moedaCarteiraDTO.getId().toString()))
            .body(moedaCarteiraDTO);
    }

    /**
     * {@code PUT  /moeda-carteiras/:id} : Updates an existing moedaCarteira.
     *
     * @param id the id of the moedaCarteiraDTO to save.
     * @param moedaCarteiraDTO the moedaCarteiraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moedaCarteiraDTO,
     * or with status {@code 400 (Bad Request)} if the moedaCarteiraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moedaCarteiraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MoedaCarteiraDTO> updateMoedaCarteira(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MoedaCarteiraDTO moedaCarteiraDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MoedaCarteira : {}, {}", id, moedaCarteiraDTO);
        if (moedaCarteiraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moedaCarteiraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moedaCarteiraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        moedaCarteiraDTO = moedaCarteiraService.update(moedaCarteiraDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moedaCarteiraDTO.getId().toString()))
            .body(moedaCarteiraDTO);
    }

    /**
     * {@code PATCH  /moeda-carteiras/:id} : Partial updates given fields of an existing moedaCarteira, field will ignore if it is null
     *
     * @param id the id of the moedaCarteiraDTO to save.
     * @param moedaCarteiraDTO the moedaCarteiraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moedaCarteiraDTO,
     * or with status {@code 400 (Bad Request)} if the moedaCarteiraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moedaCarteiraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moedaCarteiraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MoedaCarteiraDTO> partialUpdateMoedaCarteira(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MoedaCarteiraDTO moedaCarteiraDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MoedaCarteira partially : {}, {}", id, moedaCarteiraDTO);
        if (moedaCarteiraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moedaCarteiraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moedaCarteiraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MoedaCarteiraDTO> result = moedaCarteiraService.partialUpdate(moedaCarteiraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moedaCarteiraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /moeda-carteiras} : get all the moedaCarteiras.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moedaCarteiras in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MoedaCarteiraDTO>> getAllMoedaCarteiras(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("pessoafisica-is-null".equals(filter)) {
            LOG.debug("REST request to get all MoedaCarteiras where pessoaFisica is null");
            return new ResponseEntity<>(moedaCarteiraService.findAllWherePessoaFisicaIsNull(), HttpStatus.OK);
        }

        if ("pessoajuridica-is-null".equals(filter)) {
            LOG.debug("REST request to get all MoedaCarteiras where pessoaJuridica is null");
            return new ResponseEntity<>(moedaCarteiraService.findAllWherePessoaJuridicaIsNull(), HttpStatus.OK);
        }
        LOG.debug("REST request to get a page of MoedaCarteiras");
        Page<MoedaCarteiraDTO> page;
        if (eagerload) {
            page = moedaCarteiraService.findAllWithEagerRelationships(pageable);
        } else {
            page = moedaCarteiraService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /moeda-carteiras/:id} : get the "id" moedaCarteira.
     *
     * @param id the id of the moedaCarteiraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moedaCarteiraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MoedaCarteiraDTO> getMoedaCarteira(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MoedaCarteira : {}", id);
        Optional<MoedaCarteiraDTO> moedaCarteiraDTO = moedaCarteiraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moedaCarteiraDTO);
    }

    /**
     * {@code DELETE  /moeda-carteiras/:id} : delete the "id" moedaCarteira.
     *
     * @param id the id of the moedaCarteiraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoedaCarteira(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MoedaCarteira : {}", id);
        moedaCarteiraService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
