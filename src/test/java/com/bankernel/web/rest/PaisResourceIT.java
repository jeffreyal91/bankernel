package com.bankernel.web.rest;

import static com.bankernel.domain.PaisAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Pais;
import com.bankernel.repository.PaisRepository;
import com.bankernel.service.dto.PaisDTO;
import com.bankernel.service.mapper.PaisMapper;
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
 * Integration tests for the {@link PaisResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PaisResourceIT {

    private static final String DEFAULT_CODIGO = "AAA";
    private static final String UPDATED_CODIGO = "BBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final String ENTITY_API_URL = "/api/pais";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PaisRepository paisRepository;

    @Autowired
    private PaisMapper paisMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPaisMockMvc;

    private Pais pais;

    private Pais insertedPais;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pais createEntity() {
        return new Pais().codigo(DEFAULT_CODIGO).nome(DEFAULT_NOME).ativo(DEFAULT_ATIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Pais createUpdatedEntity() {
        return new Pais().codigo(UPDATED_CODIGO).nome(UPDATED_NOME).ativo(UPDATED_ATIVO);
    }

    @BeforeEach
    void initTest() {
        pais = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPais != null) {
            paisRepository.delete(insertedPais);
            insertedPais = null;
        }
    }

    @Test
    @Transactional
    void createPais() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);
        var returnedPaisDTO = om.readValue(
            restPaisMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PaisDTO.class
        );

        // Validate the Pais in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPais = paisMapper.toEntity(returnedPaisDTO);
        assertPaisUpdatableFieldsEquals(returnedPais, getPersistedPais(returnedPais));

        insertedPais = returnedPais;
    }

    @Test
    @Transactional
    void createPaisWithExistingId() throws Exception {
        // Create the Pais with an existing ID
        pais.setId(1L);
        PaisDTO paisDTO = paisMapper.toDto(pais);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPaisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pais.setCodigo(null);

        // Create the Pais, which fails.
        PaisDTO paisDTO = paisMapper.toDto(pais);

        restPaisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pais.setNome(null);

        // Create the Pais, which fails.
        PaisDTO paisDTO = paisMapper.toDto(pais);

        restPaisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pais.setAtivo(null);

        // Create the Pais, which fails.
        PaisDTO paisDTO = paisMapper.toDto(pais);

        restPaisMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPais() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get all the paisList
        restPaisMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pais.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)));
    }

    @Test
    @Transactional
    void getPais() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        // Get the pais
        restPaisMockMvc
            .perform(get(ENTITY_API_URL_ID, pais.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pais.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO));
    }

    @Test
    @Transactional
    void getNonExistingPais() throws Exception {
        // Get the pais
        restPaisMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPais() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pais
        Pais updatedPais = paisRepository.findById(pais.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPais are not directly saved in db
        em.detach(updatedPais);
        updatedPais.codigo(UPDATED_CODIGO).nome(UPDATED_NOME).ativo(UPDATED_ATIVO);
        PaisDTO paisDTO = paisMapper.toDto(updatedPais);

        restPaisMockMvc
            .perform(put(ENTITY_API_URL_ID, paisDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isOk());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPaisToMatchAllProperties(updatedPais);
    }

    @Test
    @Transactional
    void putNonExistingPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(put(ENTITY_API_URL_ID, paisDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(paisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePaisWithPatch() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pais using partial update
        Pais partialUpdatedPais = new Pais();
        partialUpdatedPais.setId(pais.getId());

        partialUpdatedPais.codigo(UPDATED_CODIGO).ativo(UPDATED_ATIVO);

        restPaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPais.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPais))
            )
            .andExpect(status().isOk());

        // Validate the Pais in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaisUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPais, pais), getPersistedPais(pais));
    }

    @Test
    @Transactional
    void fullUpdatePaisWithPatch() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pais using partial update
        Pais partialUpdatedPais = new Pais();
        partialUpdatedPais.setId(pais.getId());

        partialUpdatedPais.codigo(UPDATED_CODIGO).nome(UPDATED_NOME).ativo(UPDATED_ATIVO);

        restPaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPais.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPais))
            )
            .andExpect(status().isOk());

        // Validate the Pais in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPaisUpdatableFieldsEquals(partialUpdatedPais, getPersistedPais(partialUpdatedPais));
    }

    @Test
    @Transactional
    void patchNonExistingPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, paisDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(paisDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPais() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pais.setId(longCount.incrementAndGet());

        // Create the Pais
        PaisDTO paisDTO = paisMapper.toDto(pais);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPaisMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(paisDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Pais in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePais() throws Exception {
        // Initialize the database
        insertedPais = paisRepository.saveAndFlush(pais);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pais
        restPaisMockMvc
            .perform(delete(ENTITY_API_URL_ID, pais.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return paisRepository.count();
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

    protected Pais getPersistedPais(Pais pais) {
        return paisRepository.findById(pais.getId()).orElseThrow();
    }

    protected void assertPersistedPaisToMatchAllProperties(Pais expectedPais) {
        assertPaisAllPropertiesEquals(expectedPais, getPersistedPais(expectedPais));
    }

    protected void assertPersistedPaisToMatchUpdatableProperties(Pais expectedPais) {
        assertPaisAllUpdatablePropertiesEquals(expectedPais, getPersistedPais(expectedPais));
    }
}
