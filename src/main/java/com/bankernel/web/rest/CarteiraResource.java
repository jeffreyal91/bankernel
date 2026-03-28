package com.bankernel.web.rest;

import com.bankernel.repository.CarteiraRepository;
import com.bankernel.service.CarteiraQueryService;
import com.bankernel.service.CarteiraService;
import com.bankernel.service.criteria.CarteiraCriteria;
import com.bankernel.service.dto.CarteiraDTO;
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
 * REST controller for managing {@link com.bankernel.domain.Carteira}.
 */
@RestController
@RequestMapping("/api/carteiras")
public class CarteiraResource {

    private static final Logger LOG = LoggerFactory.getLogger(CarteiraResource.class);

    private static final String ENTITY_NAME = "carteira";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CarteiraService carteiraService;

    private final CarteiraRepository carteiraRepository;

    private final CarteiraQueryService carteiraQueryService;

    public CarteiraResource(
        CarteiraService carteiraService,
        CarteiraRepository carteiraRepository,
        CarteiraQueryService carteiraQueryService
    ) {
        this.carteiraService = carteiraService;
        this.carteiraRepository = carteiraRepository;
        this.carteiraQueryService = carteiraQueryService;
    }

    /**
     * {@code POST  /carteiras} : Create a new carteira.
     *
     * @param carteiraDTO the carteiraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new carteiraDTO, or with status {@code 400 (Bad Request)} if the carteira has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CarteiraDTO> createCarteira(@Valid @RequestBody CarteiraDTO carteiraDTO) throws URISyntaxException {
        LOG.debug("REST request to save Carteira : {}", carteiraDTO);
        if (carteiraDTO.getId() != null) {
            throw new BadRequestAlertException("A new carteira cannot already have an ID", ENTITY_NAME, "idexists");
        }
        carteiraDTO = carteiraService.save(carteiraDTO);
        return ResponseEntity.created(new URI("/api/carteiras/" + carteiraDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, carteiraDTO.getId().toString()))
            .body(carteiraDTO);
    }

    /**
     * {@code PUT  /carteiras/:id} : Updates an existing carteira.
     *
     * @param id the id of the carteiraDTO to save.
     * @param carteiraDTO the carteiraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carteiraDTO,
     * or with status {@code 400 (Bad Request)} if the carteiraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the carteiraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CarteiraDTO> updateCarteira(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CarteiraDTO carteiraDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Carteira : {}, {}", id, carteiraDTO);
        if (carteiraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carteiraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carteiraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        carteiraDTO = carteiraService.update(carteiraDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carteiraDTO.getId().toString()))
            .body(carteiraDTO);
    }

    /**
     * {@code PATCH  /carteiras/:id} : Partial updates given fields of an existing carteira, field will ignore if it is null
     *
     * @param id the id of the carteiraDTO to save.
     * @param carteiraDTO the carteiraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated carteiraDTO,
     * or with status {@code 400 (Bad Request)} if the carteiraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the carteiraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the carteiraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CarteiraDTO> partialUpdateCarteira(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CarteiraDTO carteiraDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Carteira partially : {}, {}", id, carteiraDTO);
        if (carteiraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, carteiraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!carteiraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CarteiraDTO> result = carteiraService.partialUpdate(carteiraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, carteiraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /carteiras} : get all the carteiras.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carteiras in body.
     */
    @GetMapping("")
    public ResponseEntity<List<CarteiraDTO>> getAllCarteiras(
        CarteiraCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get Carteiras by criteria: {}", criteria);

        Page<CarteiraDTO> page = carteiraQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /carteiras/count} : count all the carteiras.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countCarteiras(CarteiraCriteria criteria) {
        LOG.debug("REST request to count Carteiras by criteria: {}", criteria);
        return ResponseEntity.ok().body(carteiraQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /carteiras/:id} : get the "id" carteira.
     *
     * @param id the id of the carteiraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carteiraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarteiraDTO> getCarteira(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Carteira : {}", id);
        Optional<CarteiraDTO> carteiraDTO = carteiraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carteiraDTO);
    }

    /**
     * {@code DELETE  /carteiras/:id} : delete the "id" carteira.
     *
     * @param id the id of the carteiraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarteira(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Carteira : {}", id);
        carteiraService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
