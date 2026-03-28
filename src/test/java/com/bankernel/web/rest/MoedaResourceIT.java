package com.bankernel.web.rest;

import static com.bankernel.domain.MoedaAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Moeda;
import com.bankernel.domain.enumeration.EnumCodigoMoeda;
import com.bankernel.repository.MoedaRepository;
import com.bankernel.service.dto.MoedaDTO;
import com.bankernel.service.mapper.MoedaMapper;
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
 * Integration tests for the {@link MoedaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MoedaResourceIT {

    private static final EnumCodigoMoeda DEFAULT_CODIGO_MOEDA = EnumCodigoMoeda.BRL;
    private static final EnumCodigoMoeda UPDATED_CODIGO_MOEDA = EnumCodigoMoeda.USD;

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVA = false;
    private static final Boolean UPDATED_ATIVA = true;

    private static final String ENTITY_API_URL = "/api/moedas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MoedaRepository moedaRepository;

    @Autowired
    private MoedaMapper moedaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoedaMockMvc;

    private Moeda moeda;

    private Moeda insertedMoeda;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Moeda createEntity() {
        return new Moeda().codigoMoeda(DEFAULT_CODIGO_MOEDA).nome(DEFAULT_NOME).descricao(DEFAULT_DESCRICAO).ativa(DEFAULT_ATIVA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Moeda createUpdatedEntity() {
        return new Moeda().codigoMoeda(UPDATED_CODIGO_MOEDA).nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).ativa(UPDATED_ATIVA);
    }

    @BeforeEach
    void initTest() {
        moeda = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMoeda != null) {
            moedaRepository.delete(insertedMoeda);
            insertedMoeda = null;
        }
    }

    @Test
    @Transactional
    void createMoeda() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Moeda
        MoedaDTO moedaDTO = moedaMapper.toDto(moeda);
        var returnedMoedaDTO = om.readValue(
            restMoedaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MoedaDTO.class
        );

        // Validate the Moeda in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMoeda = moedaMapper.toEntity(returnedMoedaDTO);
        assertMoedaUpdatableFieldsEquals(returnedMoeda, getPersistedMoeda(returnedMoeda));

        insertedMoeda = returnedMoeda;
    }

    @Test
    @Transactional
    void createMoedaWithExistingId() throws Exception {
        // Create the Moeda with an existing ID
        moeda.setId(1L);
        MoedaDTO moedaDTO = moedaMapper.toDto(moeda);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoedaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Moeda in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoMoedaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moeda.setCodigoMoeda(null);

        // Create the Moeda, which fails.
        MoedaDTO moedaDTO = moedaMapper.toDto(moeda);

        restMoedaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moeda.setNome(null);

        // Create the Moeda, which fails.
        MoedaDTO moedaDTO = moedaMapper.toDto(moeda);

        restMoedaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moeda.setAtiva(null);

        // Create the Moeda, which fails.
        MoedaDTO moedaDTO = moedaMapper.toDto(moeda);

        restMoedaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMoedas() throws Exception {
        // Initialize the database
        insertedMoeda = moedaRepository.saveAndFlush(moeda);

        // Get all the moedaList
        restMoedaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moeda.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigoMoeda").value(hasItem(DEFAULT_CODIGO_MOEDA.toString())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].ativa").value(hasItem(DEFAULT_ATIVA)));
    }

    @Test
    @Transactional
    void getMoeda() throws Exception {
        // Initialize the database
        insertedMoeda = moedaRepository.saveAndFlush(moeda);

        // Get the moeda
        restMoedaMockMvc
            .perform(get(ENTITY_API_URL_ID, moeda.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moeda.getId().intValue()))
            .andExpect(jsonPath("$.codigoMoeda").value(DEFAULT_CODIGO_MOEDA.toString()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.ativa").value(DEFAULT_ATIVA));
    }

    @Test
    @Transactional
    void getNonExistingMoeda() throws Exception {
        // Get the moeda
        restMoedaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMoeda() throws Exception {
        // Initialize the database
        insertedMoeda = moedaRepository.saveAndFlush(moeda);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moeda
        Moeda updatedMoeda = moedaRepository.findById(moeda.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMoeda are not directly saved in db
        em.detach(updatedMoeda);
        updatedMoeda.codigoMoeda(UPDATED_CODIGO_MOEDA).nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).ativa(UPDATED_ATIVA);
        MoedaDTO moedaDTO = moedaMapper.toDto(updatedMoeda);

        restMoedaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moedaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Moeda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMoedaToMatchAllProperties(updatedMoeda);
    }

    @Test
    @Transactional
    void putNonExistingMoeda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moeda.setId(longCount.incrementAndGet());

        // Create the Moeda
        MoedaDTO moedaDTO = moedaMapper.toDto(moeda);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoedaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moedaDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Moeda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMoeda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moeda.setId(longCount.incrementAndGet());

        // Create the Moeda
        MoedaDTO moedaDTO = moedaMapper.toDto(moeda);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoedaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moedaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Moeda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMoeda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moeda.setId(longCount.incrementAndGet());

        // Create the Moeda
        MoedaDTO moedaDTO = moedaMapper.toDto(moeda);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoedaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Moeda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMoedaWithPatch() throws Exception {
        // Initialize the database
        insertedMoeda = moedaRepository.saveAndFlush(moeda);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moeda using partial update
        Moeda partialUpdatedMoeda = new Moeda();
        partialUpdatedMoeda.setId(moeda.getId());

        partialUpdatedMoeda.codigoMoeda(UPDATED_CODIGO_MOEDA);

        restMoedaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoeda.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoeda))
            )
            .andExpect(status().isOk());

        // Validate the Moeda in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoedaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMoeda, moeda), getPersistedMoeda(moeda));
    }

    @Test
    @Transactional
    void fullUpdateMoedaWithPatch() throws Exception {
        // Initialize the database
        insertedMoeda = moedaRepository.saveAndFlush(moeda);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moeda using partial update
        Moeda partialUpdatedMoeda = new Moeda();
        partialUpdatedMoeda.setId(moeda.getId());

        partialUpdatedMoeda.codigoMoeda(UPDATED_CODIGO_MOEDA).nome(UPDATED_NOME).descricao(UPDATED_DESCRICAO).ativa(UPDATED_ATIVA);

        restMoedaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoeda.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoeda))
            )
            .andExpect(status().isOk());

        // Validate the Moeda in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoedaUpdatableFieldsEquals(partialUpdatedMoeda, getPersistedMoeda(partialUpdatedMoeda));
    }

    @Test
    @Transactional
    void patchNonExistingMoeda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moeda.setId(longCount.incrementAndGet());

        // Create the Moeda
        MoedaDTO moedaDTO = moedaMapper.toDto(moeda);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoedaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moedaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moedaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Moeda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMoeda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moeda.setId(longCount.incrementAndGet());

        // Create the Moeda
        MoedaDTO moedaDTO = moedaMapper.toDto(moeda);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoedaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moedaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Moeda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMoeda() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moeda.setId(longCount.incrementAndGet());

        // Create the Moeda
        MoedaDTO moedaDTO = moedaMapper.toDto(moeda);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoedaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(moedaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Moeda in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMoeda() throws Exception {
        // Initialize the database
        insertedMoeda = moedaRepository.saveAndFlush(moeda);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the moeda
        restMoedaMockMvc
            .perform(delete(ENTITY_API_URL_ID, moeda.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return moedaRepository.count();
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

    protected Moeda getPersistedMoeda(Moeda moeda) {
        return moedaRepository.findById(moeda.getId()).orElseThrow();
    }

    protected void assertPersistedMoedaToMatchAllProperties(Moeda expectedMoeda) {
        assertMoedaAllPropertiesEquals(expectedMoeda, getPersistedMoeda(expectedMoeda));
    }

    protected void assertPersistedMoedaToMatchUpdatableProperties(Moeda expectedMoeda) {
        assertMoedaAllUpdatablePropertiesEquals(expectedMoeda, getPersistedMoeda(expectedMoeda));
    }
}
