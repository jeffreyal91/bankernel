package com.bankernel.web.rest;

import static com.bankernel.domain.PlanoAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Plano;
import com.bankernel.repository.PlanoRepository;
import com.bankernel.service.dto.PlanoDTO;
import com.bankernel.service.mapper.PlanoMapper;
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
 * Integration tests for the {@link PlanoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PlanoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final Boolean DEFAULT_PADRAO = false;
    private static final Boolean UPDATED_PADRAO = true;

    private static final String ENTITY_API_URL = "/api/planos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlanoRepository planoRepository;

    @Autowired
    private PlanoMapper planoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanoMockMvc;

    private Plano plano;

    private Plano insertedPlano;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plano createEntity() {
        return new Plano().nome(DEFAULT_NOME).descricao(DEFAULT_DESCRICAO).ativo(DEFAULT_ATIVO).padrao(DEFAULT_PADRAO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Plano createUpdatedEntity() {
        return new Plano().nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).ativo(UPDATED_ATIVO).padrao(UPDATED_PADRAO);
    }

    @BeforeEach
    void initTest() {
        plano = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPlano != null) {
            planoRepository.delete(insertedPlano);
            insertedPlano = null;
        }
    }

    @Test
    @Transactional
    void createPlano() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Plano
        PlanoDTO planoDTO = planoMapper.toDto(plano);
        var returnedPlanoDTO = om.readValue(
            restPlanoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PlanoDTO.class
        );

        // Validate the Plano in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlano = planoMapper.toEntity(returnedPlanoDTO);
        assertPlanoUpdatableFieldsEquals(returnedPlano, getPersistedPlano(returnedPlano));

        insertedPlano = returnedPlano;
    }

    @Test
    @Transactional
    void createPlanoWithExistingId() throws Exception {
        // Create the Plano with an existing ID
        plano.setId(1L);
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Plano in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        plano.setNome(null);

        // Create the Plano, which fails.
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        restPlanoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        plano.setAtivo(null);

        // Create the Plano, which fails.
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        restPlanoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPadraoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        plano.setPadrao(null);

        // Create the Plano, which fails.
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        restPlanoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlanos() throws Exception {
        // Initialize the database
        insertedPlano = planoRepository.saveAndFlush(plano);

        // Get all the planoList
        restPlanoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(plano.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)))
            .andExpect(jsonPath("$.[*].padrao").value(hasItem(DEFAULT_PADRAO)));
    }

    @Test
    @Transactional
    void getPlano() throws Exception {
        // Initialize the database
        insertedPlano = planoRepository.saveAndFlush(plano);

        // Get the plano
        restPlanoMockMvc
            .perform(get(ENTITY_API_URL_ID, plano.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(plano.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO))
            .andExpect(jsonPath("$.padrao").value(DEFAULT_PADRAO));
    }

    @Test
    @Transactional
    void getNonExistingPlano() throws Exception {
        // Get the plano
        restPlanoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlano() throws Exception {
        // Initialize the database
        insertedPlano = planoRepository.saveAndFlush(plano);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the plano
        Plano updatedPlano = planoRepository.findById(plano.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlano are not directly saved in db
        em.detach(updatedPlano);
        updatedPlano.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).ativo(UPDATED_ATIVO).padrao(UPDATED_PADRAO);
        PlanoDTO planoDTO = planoMapper.toDto(updatedPlano);

        restPlanoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Plano in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlanoToMatchAllProperties(updatedPlano);
    }

    @Test
    @Transactional
    void putNonExistingPlano() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plano.setId(longCount.incrementAndGet());

        // Create the Plano
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planoDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plano in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlano() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plano.setId(longCount.incrementAndGet());

        // Create the Plano
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plano in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlano() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plano.setId(longCount.incrementAndGet());

        // Create the Plano
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plano in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlanoWithPatch() throws Exception {
        // Initialize the database
        insertedPlano = planoRepository.saveAndFlush(plano);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the plano using partial update
        Plano partialUpdatedPlano = new Plano();
        partialUpdatedPlano.setId(plano.getId());

        partialUpdatedPlano.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).ativo(UPDATED_ATIVO).padrao(UPDATED_PADRAO);

        restPlanoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlano.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlano))
            )
            .andExpect(status().isOk());

        // Validate the Plano in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlanoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedPlano, plano), getPersistedPlano(plano));
    }

    @Test
    @Transactional
    void fullUpdatePlanoWithPatch() throws Exception {
        // Initialize the database
        insertedPlano = planoRepository.saveAndFlush(plano);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the plano using partial update
        Plano partialUpdatedPlano = new Plano();
        partialUpdatedPlano.setId(plano.getId());

        partialUpdatedPlano.nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).ativo(UPDATED_ATIVO).padrao(UPDATED_PADRAO);

        restPlanoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlano.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlano))
            )
            .andExpect(status().isOk());

        // Validate the Plano in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlanoUpdatableFieldsEquals(partialUpdatedPlano, getPersistedPlano(partialUpdatedPlano));
    }

    @Test
    @Transactional
    void patchNonExistingPlano() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plano.setId(longCount.incrementAndGet());

        // Create the Plano
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, planoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plano in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlano() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plano.setId(longCount.incrementAndGet());

        // Create the Plano
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Plano in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlano() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        plano.setId(longCount.incrementAndGet());

        // Create the Plano
        PlanoDTO planoDTO = planoMapper.toDto(plano);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(planoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Plano in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlano() throws Exception {
        // Initialize the database
        insertedPlano = planoRepository.saveAndFlush(plano);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the plano
        restPlanoMockMvc
            .perform(delete(ENTITY_API_URL_ID, plano.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return planoRepository.count();
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

    protected Plano getPersistedPlano(Plano plano) {
        return planoRepository.findById(plano.getId()).orElseThrow();
    }

    protected void assertPersistedPlanoToMatchAllProperties(Plano expectedPlano) {
        assertPlanoAllPropertiesEquals(expectedPlano, getPersistedPlano(expectedPlano));
    }

    protected void assertPersistedPlanoToMatchUpdatableProperties(Plano expectedPlano) {
        assertPlanoAllUpdatablePropertiesEquals(expectedPlano, getPersistedPlano(expectedPlano));
    }
}
