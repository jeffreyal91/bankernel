package com.bankernel.web.rest;

import static com.bankernel.domain.EscritorioAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Escritorio;
import com.bankernel.repository.EscritorioRepository;
import com.bankernel.service.dto.EscritorioDTO;
import com.bankernel.service.mapper.EscritorioMapper;
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
 * Integration tests for the {@link EscritorioResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EscritorioResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final String ENTITY_API_URL = "/api/escritorios";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EscritorioRepository escritorioRepository;

    @Autowired
    private EscritorioMapper escritorioMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEscritorioMockMvc;

    private Escritorio escritorio;

    private Escritorio insertedEscritorio;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Escritorio createEntity() {
        return new Escritorio().nome(DEFAULT_NOME).descricao(DEFAULT_DESCRICAO).ativo(DEFAULT_ATIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Escritorio createUpdatedEntity() {
        return new Escritorio().nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).ativo(UPDATED_ATIVO);
    }

    @BeforeEach
    void initTest() {
        escritorio = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEscritorio != null) {
            escritorioRepository.delete(insertedEscritorio);
            insertedEscritorio = null;
        }
    }

    @Test
    @Transactional
    void createEscritorio() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Escritorio
        EscritorioDTO escritorioDTO = escritorioMapper.toDto(escritorio);
        var returnedEscritorioDTO = om.readValue(
            restEscritorioMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(escritorioDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EscritorioDTO.class
        );

        // Validate the Escritorio in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEscritorio = escritorioMapper.toEntity(returnedEscritorioDTO);
        assertEscritorioUpdatableFieldsEquals(returnedEscritorio, getPersistedEscritorio(returnedEscritorio));

        insertedEscritorio = returnedEscritorio;
    }

    @Test
    @Transactional
    void createEscritorioWithExistingId() throws Exception {
        // Create the Escritorio with an existing ID
        escritorio.setId(1L);
        EscritorioDTO escritorioDTO = escritorioMapper.toDto(escritorio);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEscritorioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(escritorioDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Escritorio in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        escritorio.setNome(null);

        // Create the Escritorio, which fails.
        EscritorioDTO escritorioDTO = escritorioMapper.toDto(escritorio);

        restEscritorioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(escritorioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        escritorio.setAtivo(null);

        // Create the Escritorio, which fails.
        EscritorioDTO escritorioDTO = escritorioMapper.toDto(escritorio);

        restEscritorioMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(escritorioDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEscritorios() throws Exception {
        // Initialize the database
        insertedEscritorio = escritorioRepository.saveAndFlush(escritorio);

        // Get all the escritorioList
        restEscritorioMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(escritorio.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)));
    }

    @Test
    @Transactional
    void getEscritorio() throws Exception {
        // Initialize the database
        insertedEscritorio = escritorioRepository.saveAndFlush(escritorio);

        // Get the escritorio
        restEscritorioMockMvc
            .perform(get(ENTITY_API_URL_ID, escritorio.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(escritorio.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO));
    }

    @Test
    @Transactional
    void getNonExistingEscritorio() throws Exception {
        // Get the escritorio
        restEscritorioMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEscritorio() throws Exception {
        // Initialize the database
        insertedEscritorio = escritorioRepository.saveAndFlush(escritorio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the escritorio
        Escritorio updatedEscritorio = escritorioRepository.findById(escritorio.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEscritorio are not directly saved in db
        em.detach(updatedEscritorio);
        updatedEscritorio.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).ativo(UPDATED_ATIVO);
        EscritorioDTO escritorioDTO = escritorioMapper.toDto(updatedEscritorio);

        restEscritorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, escritorioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(escritorioDTO))
            )
            .andExpect(status().isOk());

        // Validate the Escritorio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEscritorioToMatchAllProperties(updatedEscritorio);
    }

    @Test
    @Transactional
    void putNonExistingEscritorio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        escritorio.setId(longCount.incrementAndGet());

        // Create the Escritorio
        EscritorioDTO escritorioDTO = escritorioMapper.toDto(escritorio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEscritorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, escritorioDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(escritorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Escritorio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEscritorio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        escritorio.setId(longCount.incrementAndGet());

        // Create the Escritorio
        EscritorioDTO escritorioDTO = escritorioMapper.toDto(escritorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEscritorioMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(escritorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Escritorio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEscritorio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        escritorio.setId(longCount.incrementAndGet());

        // Create the Escritorio
        EscritorioDTO escritorioDTO = escritorioMapper.toDto(escritorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEscritorioMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(escritorioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Escritorio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEscritorioWithPatch() throws Exception {
        // Initialize the database
        insertedEscritorio = escritorioRepository.saveAndFlush(escritorio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the escritorio using partial update
        Escritorio partialUpdatedEscritorio = new Escritorio();
        partialUpdatedEscritorio.setId(escritorio.getId());

        partialUpdatedEscritorio.descricao(UPDATED_DESCRICAO);

        restEscritorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEscritorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEscritorio))
            )
            .andExpect(status().isOk());

        // Validate the Escritorio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEscritorioUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEscritorio, escritorio),
            getPersistedEscritorio(escritorio)
        );
    }

    @Test
    @Transactional
    void fullUpdateEscritorioWithPatch() throws Exception {
        // Initialize the database
        insertedEscritorio = escritorioRepository.saveAndFlush(escritorio);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the escritorio using partial update
        Escritorio partialUpdatedEscritorio = new Escritorio();
        partialUpdatedEscritorio.setId(escritorio.getId());

        partialUpdatedEscritorio.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).ativo(UPDATED_ATIVO);

        restEscritorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEscritorio.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEscritorio))
            )
            .andExpect(status().isOk());

        // Validate the Escritorio in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEscritorioUpdatableFieldsEquals(partialUpdatedEscritorio, getPersistedEscritorio(partialUpdatedEscritorio));
    }

    @Test
    @Transactional
    void patchNonExistingEscritorio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        escritorio.setId(longCount.incrementAndGet());

        // Create the Escritorio
        EscritorioDTO escritorioDTO = escritorioMapper.toDto(escritorio);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEscritorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, escritorioDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(escritorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Escritorio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEscritorio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        escritorio.setId(longCount.incrementAndGet());

        // Create the Escritorio
        EscritorioDTO escritorioDTO = escritorioMapper.toDto(escritorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEscritorioMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(escritorioDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Escritorio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEscritorio() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        escritorio.setId(longCount.incrementAndGet());

        // Create the Escritorio
        EscritorioDTO escritorioDTO = escritorioMapper.toDto(escritorio);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEscritorioMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(escritorioDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Escritorio in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEscritorio() throws Exception {
        // Initialize the database
        insertedEscritorio = escritorioRepository.saveAndFlush(escritorio);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the escritorio
        restEscritorioMockMvc
            .perform(delete(ENTITY_API_URL_ID, escritorio.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return escritorioRepository.count();
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

    protected Escritorio getPersistedEscritorio(Escritorio escritorio) {
        return escritorioRepository.findById(escritorio.getId()).orElseThrow();
    }

    protected void assertPersistedEscritorioToMatchAllProperties(Escritorio expectedEscritorio) {
        assertEscritorioAllPropertiesEquals(expectedEscritorio, getPersistedEscritorio(expectedEscritorio));
    }

    protected void assertPersistedEscritorioToMatchUpdatableProperties(Escritorio expectedEscritorio) {
        assertEscritorioAllUpdatablePropertiesEquals(expectedEscritorio, getPersistedEscritorio(expectedEscritorio));
    }
}
