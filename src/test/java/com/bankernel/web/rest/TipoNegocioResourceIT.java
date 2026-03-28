package com.bankernel.web.rest;

import static com.bankernel.domain.TipoNegocioAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.TipoNegocio;
import com.bankernel.repository.TipoNegocioRepository;
import com.bankernel.service.dto.TipoNegocioDTO;
import com.bankernel.service.mapper.TipoNegocioMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TipoNegocioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TipoNegocioResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final String ENTITY_API_URL = "/api/tipo-negocios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TipoNegocioRepository tipoNegocioRepository;

    @Autowired
    private TipoNegocioMapper tipoNegocioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoNegocioMockMvc;

    private TipoNegocio tipoNegocio;

    private TipoNegocio insertedTipoNegocio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoNegocio createEntity() {
        return new TipoNegocio().nome(DEFAULT_NOME).ativo(DEFAULT_ATIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoNegocio createUpdatedEntity() {
        return new TipoNegocio().nome(UPDATED_NOME).ativo(UPDATED_ATIVO);
    }

    @BeforeEach
    void initTest() {
        tipoNegocio = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTipoNegocio != null) {
            tipoNegocioRepository.delete(insertedTipoNegocio);
            insertedTipoNegocio = null;
        }
    }

    @Test
    @Transactional
    void createTipoNegocio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TipoNegocio
        TipoNegocioDTO tipoNegocioDTO = tipoNegocioMapper.toDto(tipoNegocio);
        var returnedTipoNegocioDTO = om.readValue(
            restTipoNegocioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoNegocioDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TipoNegocioDTO.class
        );

        // Validate the TipoNegocio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTipoNegocio = tipoNegocioMapper.toEntity(returnedTipoNegocioDTO);
        assertTipoNegocioUpdatableFieldsEquals(returnedTipoNegocio, getPersistedTipoNegocio(returnedTipoNegocio));

        insertedTipoNegocio = returnedTipoNegocio;
    }

    @Test
    @Transactional
    void createTipoNegocioWithExistingId() throws Exception {
        // Create the TipoNegocio with an existing ID
        tipoNegocio.setId(1L);
        TipoNegocioDTO tipoNegocioDTO = tipoNegocioMapper.toDto(tipoNegocio);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoNegocioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoNegocioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoNegocio.setNome(null);

        // Create the TipoNegocio, which fails.
        TipoNegocioDTO tipoNegocioDTO = tipoNegocioMapper.toDto(tipoNegocio);

        restTipoNegocioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoNegocioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoNegocio.setAtivo(null);

        // Create the TipoNegocio, which fails.
        TipoNegocioDTO tipoNegocioDTO = tipoNegocioMapper.toDto(tipoNegocio);

        restTipoNegocioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoNegocioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoNegocios() throws Exception {
        // Initialize the database
        insertedTipoNegocio = tipoNegocioRepository.saveAndFlush(tipoNegocio);

        // Get all the tipoNegocioList
        restTipoNegocioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoNegocio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)));
    }

    @Test
    @Transactional
    void getTipoNegocio() throws Exception {
        // Initialize the database
        insertedTipoNegocio = tipoNegocioRepository.saveAndFlush(tipoNegocio);

        // Get the tipoNegocio
        restTipoNegocioMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoNegocio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoNegocio.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO));
    }

    @Test
    @Transactional
    void getNonExistingTipoNegocio() throws Exception {
        // Get the tipoNegocio
        restTipoNegocioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipoNegocio() throws Exception {
        // Initialize the database
        insertedTipoNegocio = tipoNegocioRepository.saveAndFlush(tipoNegocio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoNegocio
        TipoNegocio updatedTipoNegocio = tipoNegocioRepository.findById(tipoNegocio.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTipoNegocio are not directly saved in db
        em.detach(updatedTipoNegocio);
        updatedTipoNegocio.nome(UPDATED_NOME).ativo(UPDATED_ATIVO);
        TipoNegocioDTO tipoNegocioDTO = tipoNegocioMapper.toDto(updatedTipoNegocio);

        restTipoNegocioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoNegocioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoNegocioDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTipoNegocioToMatchAllProperties(updatedTipoNegocio);
    }

    @Test
    @Transactional
    void putNonExistingTipoNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoNegocio.setId(longCount.incrementAndGet());

        // Create the TipoNegocio
        TipoNegocioDTO tipoNegocioDTO = tipoNegocioMapper.toDto(tipoNegocio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoNegocioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoNegocioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoNegocioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoNegocio.setId(longCount.incrementAndGet());

        // Create the TipoNegocio
        TipoNegocioDTO tipoNegocioDTO = tipoNegocioMapper.toDto(tipoNegocio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoNegocioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoNegocioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoNegocio.setId(longCount.incrementAndGet());

        // Create the TipoNegocio
        TipoNegocioDTO tipoNegocioDTO = tipoNegocioMapper.toDto(tipoNegocio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoNegocioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoNegocioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoNegocioWithPatch() throws Exception {
        // Initialize the database
        insertedTipoNegocio = tipoNegocioRepository.saveAndFlush(tipoNegocio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoNegocio using partial update
        TipoNegocio partialUpdatedTipoNegocio = new TipoNegocio();
        partialUpdatedTipoNegocio.setId(tipoNegocio.getId());

        restTipoNegocioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoNegocio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoNegocio))
            )
            .andExpect(status().isOk());

        // Validate the TipoNegocio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoNegocioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTipoNegocio, tipoNegocio),
            getPersistedTipoNegocio(tipoNegocio)
        );
    }

    @Test
    @Transactional
    void fullUpdateTipoNegocioWithPatch() throws Exception {
        // Initialize the database
        insertedTipoNegocio = tipoNegocioRepository.saveAndFlush(tipoNegocio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoNegocio using partial update
        TipoNegocio partialUpdatedTipoNegocio = new TipoNegocio();
        partialUpdatedTipoNegocio.setId(tipoNegocio.getId());

        partialUpdatedTipoNegocio.nome(UPDATED_NOME).ativo(UPDATED_ATIVO);

        restTipoNegocioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoNegocio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoNegocio))
            )
            .andExpect(status().isOk());

        // Validate the TipoNegocio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoNegocioUpdatableFieldsEquals(partialUpdatedTipoNegocio, getPersistedTipoNegocio(partialUpdatedTipoNegocio));
    }

    @Test
    @Transactional
    void patchNonExistingTipoNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoNegocio.setId(longCount.incrementAndGet());

        // Create the TipoNegocio
        TipoNegocioDTO tipoNegocioDTO = tipoNegocioMapper.toDto(tipoNegocio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoNegocioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoNegocioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoNegocioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoNegocio.setId(longCount.incrementAndGet());

        // Create the TipoNegocio
        TipoNegocioDTO tipoNegocioDTO = tipoNegocioMapper.toDto(tipoNegocio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoNegocioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoNegocioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoNegocio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoNegocio.setId(longCount.incrementAndGet());

        // Create the TipoNegocio
        TipoNegocioDTO tipoNegocioDTO = tipoNegocioMapper.toDto(tipoNegocio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoNegocioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tipoNegocioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoNegocio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoNegocio() throws Exception {
        // Initialize the database
        insertedTipoNegocio = tipoNegocioRepository.saveAndFlush(tipoNegocio);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tipoNegocio
        restTipoNegocioMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoNegocio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tipoNegocioRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected TipoNegocio getPersistedTipoNegocio(TipoNegocio tipoNegocio) {
        return tipoNegocioRepository.findById(tipoNegocio.getId()).orElseThrow();
    }

    protected void assertPersistedTipoNegocioToMatchAllProperties(TipoNegocio expectedTipoNegocio) {
        assertTipoNegocioAllPropertiesEquals(expectedTipoNegocio, getPersistedTipoNegocio(expectedTipoNegocio));
    }

    protected void assertPersistedTipoNegocioToMatchUpdatableProperties(TipoNegocio expectedTipoNegocio) {
        assertTipoNegocioAllUpdatablePropertiesEquals(expectedTipoNegocio, getPersistedTipoNegocio(expectedTipoNegocio));
    }
}
