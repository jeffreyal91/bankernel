package com.bankernel.web.rest;

import static com.bankernel.domain.BancoReferenciaAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.BancoReferencia;
import com.bankernel.repository.BancoReferenciaRepository;
import com.bankernel.service.dto.BancoReferenciaDTO;
import com.bankernel.service.mapper.BancoReferenciaMapper;
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
 * Integration tests for the {@link BancoReferenciaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BancoReferenciaResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_ISPB = "AAAAAAAA";
    private static final String UPDATED_ISPB = "BBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final String ENTITY_API_URL = "/api/banco-referencias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BancoReferenciaRepository bancoReferenciaRepository;

    @Autowired
    private BancoReferenciaMapper bancoReferenciaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBancoReferenciaMockMvc;

    private BancoReferencia bancoReferencia;

    private BancoReferencia insertedBancoReferencia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BancoReferencia createEntity() {
        return new BancoReferencia().codigo(DEFAULT_CODIGO).nome(DEFAULT_NOME).ispb(DEFAULT_ISPB).ativo(DEFAULT_ATIVO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BancoReferencia createUpdatedEntity() {
        return new BancoReferencia().codigo(UPDATED_CODIGO).nome(UPDATED_NOME).ispb(UPDATED_ISPB).ativo(UPDATED_ATIVO);
    }

    @BeforeEach
    void initTest() {
        bancoReferencia = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBancoReferencia != null) {
            bancoReferenciaRepository.delete(insertedBancoReferencia);
            insertedBancoReferencia = null;
        }
    }

    @Test
    @Transactional
    void createBancoReferencia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BancoReferencia
        BancoReferenciaDTO bancoReferenciaDTO = bancoReferenciaMapper.toDto(bancoReferencia);
        var returnedBancoReferenciaDTO = om.readValue(
            restBancoReferenciaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bancoReferenciaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BancoReferenciaDTO.class
        );

        // Validate the BancoReferencia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBancoReferencia = bancoReferenciaMapper.toEntity(returnedBancoReferenciaDTO);
        assertBancoReferenciaUpdatableFieldsEquals(returnedBancoReferencia, getPersistedBancoReferencia(returnedBancoReferencia));

        insertedBancoReferencia = returnedBancoReferencia;
    }

    @Test
    @Transactional
    void createBancoReferenciaWithExistingId() throws Exception {
        // Create the BancoReferencia with an existing ID
        bancoReferencia.setId(1L);
        BancoReferenciaDTO bancoReferenciaDTO = bancoReferenciaMapper.toDto(bancoReferencia);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBancoReferenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bancoReferenciaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BancoReferencia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bancoReferencia.setCodigo(null);

        // Create the BancoReferencia, which fails.
        BancoReferenciaDTO bancoReferenciaDTO = bancoReferenciaMapper.toDto(bancoReferencia);

        restBancoReferenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bancoReferenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bancoReferencia.setNome(null);

        // Create the BancoReferencia, which fails.
        BancoReferenciaDTO bancoReferenciaDTO = bancoReferenciaMapper.toDto(bancoReferencia);

        restBancoReferenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bancoReferenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bancoReferencia.setAtivo(null);

        // Create the BancoReferencia, which fails.
        BancoReferenciaDTO bancoReferenciaDTO = bancoReferenciaMapper.toDto(bancoReferencia);

        restBancoReferenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bancoReferenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBancoReferencias() throws Exception {
        // Initialize the database
        insertedBancoReferencia = bancoReferenciaRepository.saveAndFlush(bancoReferencia);

        // Get all the bancoReferenciaList
        restBancoReferenciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bancoReferencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].ispb").value(hasItem(DEFAULT_ISPB)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)));
    }

    @Test
    @Transactional
    void getBancoReferencia() throws Exception {
        // Initialize the database
        insertedBancoReferencia = bancoReferenciaRepository.saveAndFlush(bancoReferencia);

        // Get the bancoReferencia
        restBancoReferenciaMockMvc
            .perform(get(ENTITY_API_URL_ID, bancoReferencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bancoReferencia.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.ispb").value(DEFAULT_ISPB))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO));
    }

    @Test
    @Transactional
    void getNonExistingBancoReferencia() throws Exception {
        // Get the bancoReferencia
        restBancoReferenciaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBancoReferencia() throws Exception {
        // Initialize the database
        insertedBancoReferencia = bancoReferenciaRepository.saveAndFlush(bancoReferencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bancoReferencia
        BancoReferencia updatedBancoReferencia = bancoReferenciaRepository.findById(bancoReferencia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBancoReferencia are not directly saved in db
        em.detach(updatedBancoReferencia);
        updatedBancoReferencia.codigo(UPDATED_CODIGO).nome(UPDATED_NOME).ispb(UPDATED_ISPB).ativo(UPDATED_ATIVO);
        BancoReferenciaDTO bancoReferenciaDTO = bancoReferenciaMapper.toDto(updatedBancoReferencia);

        restBancoReferenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bancoReferenciaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bancoReferenciaDTO))
            )
            .andExpect(status().isOk());

        // Validate the BancoReferencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBancoReferenciaToMatchAllProperties(updatedBancoReferencia);
    }

    @Test
    @Transactional
    void putNonExistingBancoReferencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bancoReferencia.setId(longCount.incrementAndGet());

        // Create the BancoReferencia
        BancoReferenciaDTO bancoReferenciaDTO = bancoReferenciaMapper.toDto(bancoReferencia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBancoReferenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bancoReferenciaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bancoReferenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BancoReferencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBancoReferencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bancoReferencia.setId(longCount.incrementAndGet());

        // Create the BancoReferencia
        BancoReferenciaDTO bancoReferenciaDTO = bancoReferenciaMapper.toDto(bancoReferencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBancoReferenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bancoReferenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BancoReferencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBancoReferencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bancoReferencia.setId(longCount.incrementAndGet());

        // Create the BancoReferencia
        BancoReferenciaDTO bancoReferenciaDTO = bancoReferenciaMapper.toDto(bancoReferencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBancoReferenciaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bancoReferenciaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BancoReferencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBancoReferenciaWithPatch() throws Exception {
        // Initialize the database
        insertedBancoReferencia = bancoReferenciaRepository.saveAndFlush(bancoReferencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bancoReferencia using partial update
        BancoReferencia partialUpdatedBancoReferencia = new BancoReferencia();
        partialUpdatedBancoReferencia.setId(bancoReferencia.getId());

        partialUpdatedBancoReferencia.ispb(UPDATED_ISPB).ativo(UPDATED_ATIVO);

        restBancoReferenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBancoReferencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBancoReferencia))
            )
            .andExpect(status().isOk());

        // Validate the BancoReferencia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBancoReferenciaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBancoReferencia, bancoReferencia),
            getPersistedBancoReferencia(bancoReferencia)
        );
    }

    @Test
    @Transactional
    void fullUpdateBancoReferenciaWithPatch() throws Exception {
        // Initialize the database
        insertedBancoReferencia = bancoReferenciaRepository.saveAndFlush(bancoReferencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bancoReferencia using partial update
        BancoReferencia partialUpdatedBancoReferencia = new BancoReferencia();
        partialUpdatedBancoReferencia.setId(bancoReferencia.getId());

        partialUpdatedBancoReferencia.codigo(UPDATED_CODIGO).nome(UPDATED_NOME).ispb(UPDATED_ISPB).ativo(UPDATED_ATIVO);

        restBancoReferenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBancoReferencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBancoReferencia))
            )
            .andExpect(status().isOk());

        // Validate the BancoReferencia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBancoReferenciaUpdatableFieldsEquals(
            partialUpdatedBancoReferencia,
            getPersistedBancoReferencia(partialUpdatedBancoReferencia)
        );
    }

    @Test
    @Transactional
    void patchNonExistingBancoReferencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bancoReferencia.setId(longCount.incrementAndGet());

        // Create the BancoReferencia
        BancoReferenciaDTO bancoReferenciaDTO = bancoReferenciaMapper.toDto(bancoReferencia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBancoReferenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bancoReferenciaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bancoReferenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BancoReferencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBancoReferencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bancoReferencia.setId(longCount.incrementAndGet());

        // Create the BancoReferencia
        BancoReferenciaDTO bancoReferenciaDTO = bancoReferenciaMapper.toDto(bancoReferencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBancoReferenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bancoReferenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BancoReferencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBancoReferencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bancoReferencia.setId(longCount.incrementAndGet());

        // Create the BancoReferencia
        BancoReferenciaDTO bancoReferenciaDTO = bancoReferenciaMapper.toDto(bancoReferencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBancoReferenciaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bancoReferenciaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BancoReferencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBancoReferencia() throws Exception {
        // Initialize the database
        insertedBancoReferencia = bancoReferenciaRepository.saveAndFlush(bancoReferencia);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bancoReferencia
        restBancoReferenciaMockMvc
            .perform(delete(ENTITY_API_URL_ID, bancoReferencia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bancoReferenciaRepository.count();
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

    protected BancoReferencia getPersistedBancoReferencia(BancoReferencia bancoReferencia) {
        return bancoReferenciaRepository.findById(bancoReferencia.getId()).orElseThrow();
    }

    protected void assertPersistedBancoReferenciaToMatchAllProperties(BancoReferencia expectedBancoReferencia) {
        assertBancoReferenciaAllPropertiesEquals(expectedBancoReferencia, getPersistedBancoReferencia(expectedBancoReferencia));
    }

    protected void assertPersistedBancoReferenciaToMatchUpdatableProperties(BancoReferencia expectedBancoReferencia) {
        assertBancoReferenciaAllUpdatablePropertiesEquals(expectedBancoReferencia, getPersistedBancoReferencia(expectedBancoReferencia));
    }
}
