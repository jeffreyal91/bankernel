package com.bankernel.web.rest;

import static com.bankernel.domain.ProfissaoAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Profissao;
import com.bankernel.repository.ProfissaoRepository;
import com.bankernel.service.dto.ProfissaoDTO;
import com.bankernel.service.mapper.ProfissaoMapper;
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
 * Integration tests for the {@link ProfissaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProfissaoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVA = false;
    private static final Boolean UPDATED_ATIVA = true;

    private static final String ENTITY_API_URL = "/api/profissaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProfissaoRepository profissaoRepository;

    @Autowired
    private ProfissaoMapper profissaoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProfissaoMockMvc;

    private Profissao profissao;

    private Profissao insertedProfissao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profissao createEntity() {
        return new Profissao().nome(DEFAULT_NOME).ativa(DEFAULT_ATIVA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Profissao createUpdatedEntity() {
        return new Profissao().nome(UPDATED_NOME).ativa(UPDATED_ATIVA);
    }

    @BeforeEach
    void initTest() {
        profissao = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedProfissao != null) {
            profissaoRepository.delete(insertedProfissao);
            insertedProfissao = null;
        }
    }

    @Test
    @Transactional
    void createProfissao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Profissao
        ProfissaoDTO profissaoDTO = profissaoMapper.toDto(profissao);
        var returnedProfissaoDTO = om.readValue(
            restProfissaoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profissaoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProfissaoDTO.class
        );

        // Validate the Profissao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProfissao = profissaoMapper.toEntity(returnedProfissaoDTO);
        assertProfissaoUpdatableFieldsEquals(returnedProfissao, getPersistedProfissao(returnedProfissao));

        insertedProfissao = returnedProfissao;
    }

    @Test
    @Transactional
    void createProfissaoWithExistingId() throws Exception {
        // Create the Profissao with an existing ID
        profissao.setId(1L);
        ProfissaoDTO profissaoDTO = profissaoMapper.toDto(profissao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProfissaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profissaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Profissao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        profissao.setNome(null);

        // Create the Profissao, which fails.
        ProfissaoDTO profissaoDTO = profissaoMapper.toDto(profissao);

        restProfissaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profissaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        profissao.setAtiva(null);

        // Create the Profissao, which fails.
        ProfissaoDTO profissaoDTO = profissaoMapper.toDto(profissao);

        restProfissaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profissaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProfissaos() throws Exception {
        // Initialize the database
        insertedProfissao = profissaoRepository.saveAndFlush(profissao);

        // Get all the profissaoList
        restProfissaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(profissao.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].ativa").value(hasItem(DEFAULT_ATIVA)));
    }

    @Test
    @Transactional
    void getProfissao() throws Exception {
        // Initialize the database
        insertedProfissao = profissaoRepository.saveAndFlush(profissao);

        // Get the profissao
        restProfissaoMockMvc
            .perform(get(ENTITY_API_URL_ID, profissao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(profissao.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.ativa").value(DEFAULT_ATIVA));
    }

    @Test
    @Transactional
    void getNonExistingProfissao() throws Exception {
        // Get the profissao
        restProfissaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProfissao() throws Exception {
        // Initialize the database
        insertedProfissao = profissaoRepository.saveAndFlush(profissao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profissao
        Profissao updatedProfissao = profissaoRepository.findById(profissao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProfissao are not directly saved in db
        em.detach(updatedProfissao);
        updatedProfissao.nome(UPDATED_NOME).ativa(UPDATED_ATIVA);
        ProfissaoDTO profissaoDTO = profissaoMapper.toDto(updatedProfissao);

        restProfissaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profissaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profissaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Profissao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProfissaoToMatchAllProperties(updatedProfissao);
    }

    @Test
    @Transactional
    void putNonExistingProfissao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profissao.setId(longCount.incrementAndGet());

        // Create the Profissao
        ProfissaoDTO profissaoDTO = profissaoMapper.toDto(profissao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfissaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, profissaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profissaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profissao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProfissao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profissao.setId(longCount.incrementAndGet());

        // Create the Profissao
        ProfissaoDTO profissaoDTO = profissaoMapper.toDto(profissao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfissaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(profissaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profissao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProfissao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profissao.setId(longCount.incrementAndGet());

        // Create the Profissao
        ProfissaoDTO profissaoDTO = profissaoMapper.toDto(profissao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfissaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(profissaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profissao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProfissaoWithPatch() throws Exception {
        // Initialize the database
        insertedProfissao = profissaoRepository.saveAndFlush(profissao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profissao using partial update
        Profissao partialUpdatedProfissao = new Profissao();
        partialUpdatedProfissao.setId(profissao.getId());

        partialUpdatedProfissao.ativa(UPDATED_ATIVA);

        restProfissaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfissao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfissao))
            )
            .andExpect(status().isOk());

        // Validate the Profissao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfissaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProfissao, profissao),
            getPersistedProfissao(profissao)
        );
    }

    @Test
    @Transactional
    void fullUpdateProfissaoWithPatch() throws Exception {
        // Initialize the database
        insertedProfissao = profissaoRepository.saveAndFlush(profissao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the profissao using partial update
        Profissao partialUpdatedProfissao = new Profissao();
        partialUpdatedProfissao.setId(profissao.getId());

        partialUpdatedProfissao.nome(UPDATED_NOME).ativa(UPDATED_ATIVA);

        restProfissaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProfissao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProfissao))
            )
            .andExpect(status().isOk());

        // Validate the Profissao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProfissaoUpdatableFieldsEquals(partialUpdatedProfissao, getPersistedProfissao(partialUpdatedProfissao));
    }

    @Test
    @Transactional
    void patchNonExistingProfissao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profissao.setId(longCount.incrementAndGet());

        // Create the Profissao
        ProfissaoDTO profissaoDTO = profissaoMapper.toDto(profissao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProfissaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, profissaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profissaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profissao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProfissao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profissao.setId(longCount.incrementAndGet());

        // Create the Profissao
        ProfissaoDTO profissaoDTO = profissaoMapper.toDto(profissao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfissaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(profissaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Profissao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProfissao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        profissao.setId(longCount.incrementAndGet());

        // Create the Profissao
        ProfissaoDTO profissaoDTO = profissaoMapper.toDto(profissao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProfissaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(profissaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Profissao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProfissao() throws Exception {
        // Initialize the database
        insertedProfissao = profissaoRepository.saveAndFlush(profissao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the profissao
        restProfissaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, profissao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return profissaoRepository.count();
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

    protected Profissao getPersistedProfissao(Profissao profissao) {
        return profissaoRepository.findById(profissao.getId()).orElseThrow();
    }

    protected void assertPersistedProfissaoToMatchAllProperties(Profissao expectedProfissao) {
        assertProfissaoAllPropertiesEquals(expectedProfissao, getPersistedProfissao(expectedProfissao));
    }

    protected void assertPersistedProfissaoToMatchUpdatableProperties(Profissao expectedProfissao) {
        assertProfissaoAllUpdatablePropertiesEquals(expectedProfissao, getPersistedProfissao(expectedProfissao));
    }
}
