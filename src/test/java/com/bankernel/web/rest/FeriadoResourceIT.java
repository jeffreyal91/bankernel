package com.bankernel.web.rest;

import static com.bankernel.domain.FeriadoAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Feriado;
import com.bankernel.repository.FeriadoRepository;
import com.bankernel.service.dto.FeriadoDTO;
import com.bankernel.service.mapper.FeriadoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link FeriadoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FeriadoResourceIT {

    private static final LocalDate DEFAULT_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final String ENTITY_API_URL = "/api/feriados";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FeriadoRepository feriadoRepository;

    @Autowired
    private FeriadoMapper feriadoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFeriadoMockMvc;

    private Feriado feriado;

    private Feriado insertedFeriado;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feriado createEntity() {
        return new Feriado().data(DEFAULT_DATA).descricao(DEFAULT_DESCRICAO).ativo(DEFAULT_ATIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Feriado createUpdatedEntity() {
        return new Feriado().data(UPDATED_DATA).descricao(UPDATED_DESCRICAO).ativo(UPDATED_ATIVO);
    }

    @BeforeEach
    void initTest() {
        feriado = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFeriado != null) {
            feriadoRepository.delete(insertedFeriado);
            insertedFeriado = null;
        }
    }

    @Test
    @Transactional
    void createFeriado() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Feriado
        FeriadoDTO feriadoDTO = feriadoMapper.toDto(feriado);
        var returnedFeriadoDTO = om.readValue(
            restFeriadoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feriadoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FeriadoDTO.class
        );

        // Validate the Feriado in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFeriado = feriadoMapper.toEntity(returnedFeriadoDTO);
        assertFeriadoUpdatableFieldsEquals(returnedFeriado, getPersistedFeriado(returnedFeriado));

        insertedFeriado = returnedFeriado;
    }

    @Test
    @Transactional
    void createFeriadoWithExistingId() throws Exception {
        // Create the Feriado with an existing ID
        feriado.setId(1L);
        FeriadoDTO feriadoDTO = feriadoMapper.toDto(feriado);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFeriadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feriadoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Feriado in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDataIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feriado.setData(null);

        // Create the Feriado, which fails.
        FeriadoDTO feriadoDTO = feriadoMapper.toDto(feriado);

        restFeriadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feriadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescricaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feriado.setDescricao(null);

        // Create the Feriado, which fails.
        FeriadoDTO feriadoDTO = feriadoMapper.toDto(feriado);

        restFeriadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feriadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        feriado.setAtivo(null);

        // Create the Feriado, which fails.
        FeriadoDTO feriadoDTO = feriadoMapper.toDto(feriado);

        restFeriadoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feriadoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFeriados() throws Exception {
        // Initialize the database
        insertedFeriado = feriadoRepository.saveAndFlush(feriado);

        // Get all the feriadoList
        restFeriadoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(feriado.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)));
    }

    @Test
    @Transactional
    void getFeriado() throws Exception {
        // Initialize the database
        insertedFeriado = feriadoRepository.saveAndFlush(feriado);

        // Get the feriado
        restFeriadoMockMvc
            .perform(get(ENTITY_API_URL_ID, feriado.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(feriado.getId().intValue()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO));
    }

    @Test
    @Transactional
    void getNonExistingFeriado() throws Exception {
        // Get the feriado
        restFeriadoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFeriado() throws Exception {
        // Initialize the database
        insertedFeriado = feriadoRepository.saveAndFlush(feriado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feriado
        Feriado updatedFeriado = feriadoRepository.findById(feriado.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFeriado are not directly saved in db
        em.detach(updatedFeriado);
        updatedFeriado.data(UPDATED_DATA).descricao(UPDATED_DESCRICAO).ativo(UPDATED_ATIVO);
        FeriadoDTO feriadoDTO = feriadoMapper.toDto(updatedFeriado);

        restFeriadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feriadoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feriadoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Feriado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFeriadoToMatchAllProperties(updatedFeriado);
    }

    @Test
    @Transactional
    void putNonExistingFeriado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feriado.setId(longCount.incrementAndGet());

        // Create the Feriado
        FeriadoDTO feriadoDTO = feriadoMapper.toDto(feriado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeriadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, feriadoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feriadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feriado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFeriado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feriado.setId(longCount.incrementAndGet());

        // Create the Feriado
        FeriadoDTO feriadoDTO = feriadoMapper.toDto(feriado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeriadoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(feriadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feriado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFeriado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feriado.setId(longCount.incrementAndGet());

        // Create the Feriado
        FeriadoDTO feriadoDTO = feriadoMapper.toDto(feriado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeriadoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(feriadoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feriado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFeriadoWithPatch() throws Exception {
        // Initialize the database
        insertedFeriado = feriadoRepository.saveAndFlush(feriado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feriado using partial update
        Feriado partialUpdatedFeriado = new Feriado();
        partialUpdatedFeriado.setId(feriado.getId());

        partialUpdatedFeriado.data(UPDATED_DATA).descricao(UPDATED_DESCRICAO).ativo(UPDATED_ATIVO);

        restFeriadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeriado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeriado))
            )
            .andExpect(status().isOk());

        // Validate the Feriado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeriadoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFeriado, feriado), getPersistedFeriado(feriado));
    }

    @Test
    @Transactional
    void fullUpdateFeriadoWithPatch() throws Exception {
        // Initialize the database
        insertedFeriado = feriadoRepository.saveAndFlush(feriado);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the feriado using partial update
        Feriado partialUpdatedFeriado = new Feriado();
        partialUpdatedFeriado.setId(feriado.getId());

        partialUpdatedFeriado.data(UPDATED_DATA).descricao(UPDATED_DESCRICAO).ativo(UPDATED_ATIVO);

        restFeriadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFeriado.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFeriado))
            )
            .andExpect(status().isOk());

        // Validate the Feriado in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFeriadoUpdatableFieldsEquals(partialUpdatedFeriado, getPersistedFeriado(partialUpdatedFeriado));
    }

    @Test
    @Transactional
    void patchNonExistingFeriado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feriado.setId(longCount.incrementAndGet());

        // Create the Feriado
        FeriadoDTO feriadoDTO = feriadoMapper.toDto(feriado);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFeriadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, feriadoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feriadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feriado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFeriado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feriado.setId(longCount.incrementAndGet());

        // Create the Feriado
        FeriadoDTO feriadoDTO = feriadoMapper.toDto(feriado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeriadoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(feriadoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Feriado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFeriado() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        feriado.setId(longCount.incrementAndGet());

        // Create the Feriado
        FeriadoDTO feriadoDTO = feriadoMapper.toDto(feriado);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFeriadoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(feriadoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Feriado in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFeriado() throws Exception {
        // Initialize the database
        insertedFeriado = feriadoRepository.saveAndFlush(feriado);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the feriado
        restFeriadoMockMvc
            .perform(delete(ENTITY_API_URL_ID, feriado.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return feriadoRepository.count();
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

    protected Feriado getPersistedFeriado(Feriado feriado) {
        return feriadoRepository.findById(feriado.getId()).orElseThrow();
    }

    protected void assertPersistedFeriadoToMatchAllProperties(Feriado expectedFeriado) {
        assertFeriadoAllPropertiesEquals(expectedFeriado, getPersistedFeriado(expectedFeriado));
    }

    protected void assertPersistedFeriadoToMatchUpdatableProperties(Feriado expectedFeriado) {
        assertFeriadoAllUpdatablePropertiesEquals(expectedFeriado, getPersistedFeriado(expectedFeriado));
    }
}
