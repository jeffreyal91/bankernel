package com.bankernel.web.rest;

import static com.bankernel.domain.BloqueioOperacaoAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.BloqueioOperacao;
import com.bankernel.domain.enumeration.EnumDiaSemana;
import com.bankernel.domain.enumeration.EnumTipoExecucao;
import com.bankernel.domain.enumeration.EnumTipoOperacao;
import com.bankernel.repository.BloqueioOperacaoRepository;
import com.bankernel.service.dto.BloqueioOperacaoDTO;
import com.bankernel.service.mapper.BloqueioOperacaoMapper;
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
 * Integration tests for the {@link BloqueioOperacaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BloqueioOperacaoResourceIT {

    private static final EnumTipoOperacao DEFAULT_TIPO_OPERACAO = EnumTipoOperacao.DEPOSITO_PIX;
    private static final EnumTipoOperacao UPDATED_TIPO_OPERACAO = EnumTipoOperacao.DEPOSITO_BOLETO;

    private static final EnumDiaSemana DEFAULT_DIA_SEMANA = EnumDiaSemana.SEGUNDA;
    private static final EnumDiaSemana UPDATED_DIA_SEMANA = EnumDiaSemana.TERCA;

    private static final String DEFAULT_HORA_INICIO = "AAAAA";
    private static final String UPDATED_HORA_INICIO = "BBBBB";

    private static final String DEFAULT_HORA_FIM = "AAAAA";
    private static final String UPDATED_HORA_FIM = "BBBBB";

    private static final String DEFAULT_MOTIVO = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final EnumTipoExecucao DEFAULT_TIPO_EXECUCAO = EnumTipoExecucao.MANUAL;
    private static final EnumTipoExecucao UPDATED_TIPO_EXECUCAO = EnumTipoExecucao.AUTOMATICO;

    private static final String ENTITY_API_URL = "/api/bloqueio-operacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BloqueioOperacaoRepository bloqueioOperacaoRepository;

    @Autowired
    private BloqueioOperacaoMapper bloqueioOperacaoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBloqueioOperacaoMockMvc;

    private BloqueioOperacao bloqueioOperacao;

    private BloqueioOperacao insertedBloqueioOperacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BloqueioOperacao createEntity() {
        return new BloqueioOperacao()
            .tipoOperacao(DEFAULT_TIPO_OPERACAO)
            .diaSemana(DEFAULT_DIA_SEMANA)
            .horaInicio(DEFAULT_HORA_INICIO)
            .horaFim(DEFAULT_HORA_FIM)
            .motivo(DEFAULT_MOTIVO)
            .ativo(DEFAULT_ATIVO)
            .tipoExecucao(DEFAULT_TIPO_EXECUCAO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BloqueioOperacao createUpdatedEntity() {
        return new BloqueioOperacao()
            .tipoOperacao(UPDATED_TIPO_OPERACAO)
            .diaSemana(UPDATED_DIA_SEMANA)
            .horaInicio(UPDATED_HORA_INICIO)
            .horaFim(UPDATED_HORA_FIM)
            .motivo(UPDATED_MOTIVO)
            .ativo(UPDATED_ATIVO)
            .tipoExecucao(UPDATED_TIPO_EXECUCAO);
    }

    @BeforeEach
    void initTest() {
        bloqueioOperacao = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBloqueioOperacao != null) {
            bloqueioOperacaoRepository.delete(insertedBloqueioOperacao);
            insertedBloqueioOperacao = null;
        }
    }

    @Test
    @Transactional
    void createBloqueioOperacao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BloqueioOperacao
        BloqueioOperacaoDTO bloqueioOperacaoDTO = bloqueioOperacaoMapper.toDto(bloqueioOperacao);
        var returnedBloqueioOperacaoDTO = om.readValue(
            restBloqueioOperacaoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bloqueioOperacaoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BloqueioOperacaoDTO.class
        );

        // Validate the BloqueioOperacao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBloqueioOperacao = bloqueioOperacaoMapper.toEntity(returnedBloqueioOperacaoDTO);
        assertBloqueioOperacaoUpdatableFieldsEquals(returnedBloqueioOperacao, getPersistedBloqueioOperacao(returnedBloqueioOperacao));

        insertedBloqueioOperacao = returnedBloqueioOperacao;
    }

    @Test
    @Transactional
    void createBloqueioOperacaoWithExistingId() throws Exception {
        // Create the BloqueioOperacao with an existing ID
        bloqueioOperacao.setId(1L);
        BloqueioOperacaoDTO bloqueioOperacaoDTO = bloqueioOperacaoMapper.toDto(bloqueioOperacao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBloqueioOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bloqueioOperacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BloqueioOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoOperacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bloqueioOperacao.setTipoOperacao(null);

        // Create the BloqueioOperacao, which fails.
        BloqueioOperacaoDTO bloqueioOperacaoDTO = bloqueioOperacaoMapper.toDto(bloqueioOperacao);

        restBloqueioOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bloqueioOperacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bloqueioOperacao.setAtivo(null);

        // Create the BloqueioOperacao, which fails.
        BloqueioOperacaoDTO bloqueioOperacaoDTO = bloqueioOperacaoMapper.toDto(bloqueioOperacao);

        restBloqueioOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bloqueioOperacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoExecucaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bloqueioOperacao.setTipoExecucao(null);

        // Create the BloqueioOperacao, which fails.
        BloqueioOperacaoDTO bloqueioOperacaoDTO = bloqueioOperacaoMapper.toDto(bloqueioOperacao);

        restBloqueioOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bloqueioOperacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBloqueioOperacaos() throws Exception {
        // Initialize the database
        insertedBloqueioOperacao = bloqueioOperacaoRepository.saveAndFlush(bloqueioOperacao);

        // Get all the bloqueioOperacaoList
        restBloqueioOperacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bloqueioOperacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoOperacao").value(hasItem(DEFAULT_TIPO_OPERACAO.toString())))
            .andExpect(jsonPath("$.[*].diaSemana").value(hasItem(DEFAULT_DIA_SEMANA.toString())))
            .andExpect(jsonPath("$.[*].horaInicio").value(hasItem(DEFAULT_HORA_INICIO)))
            .andExpect(jsonPath("$.[*].horaFim").value(hasItem(DEFAULT_HORA_FIM)))
            .andExpect(jsonPath("$.[*].motivo").value(hasItem(DEFAULT_MOTIVO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)))
            .andExpect(jsonPath("$.[*].tipoExecucao").value(hasItem(DEFAULT_TIPO_EXECUCAO.toString())));
    }

    @Test
    @Transactional
    void getBloqueioOperacao() throws Exception {
        // Initialize the database
        insertedBloqueioOperacao = bloqueioOperacaoRepository.saveAndFlush(bloqueioOperacao);

        // Get the bloqueioOperacao
        restBloqueioOperacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, bloqueioOperacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bloqueioOperacao.getId().intValue()))
            .andExpect(jsonPath("$.tipoOperacao").value(DEFAULT_TIPO_OPERACAO.toString()))
            .andExpect(jsonPath("$.diaSemana").value(DEFAULT_DIA_SEMANA.toString()))
            .andExpect(jsonPath("$.horaInicio").value(DEFAULT_HORA_INICIO))
            .andExpect(jsonPath("$.horaFim").value(DEFAULT_HORA_FIM))
            .andExpect(jsonPath("$.motivo").value(DEFAULT_MOTIVO))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO))
            .andExpect(jsonPath("$.tipoExecucao").value(DEFAULT_TIPO_EXECUCAO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBloqueioOperacao() throws Exception {
        // Get the bloqueioOperacao
        restBloqueioOperacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBloqueioOperacao() throws Exception {
        // Initialize the database
        insertedBloqueioOperacao = bloqueioOperacaoRepository.saveAndFlush(bloqueioOperacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bloqueioOperacao
        BloqueioOperacao updatedBloqueioOperacao = bloqueioOperacaoRepository.findById(bloqueioOperacao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBloqueioOperacao are not directly saved in db
        em.detach(updatedBloqueioOperacao);
        updatedBloqueioOperacao
            .tipoOperacao(UPDATED_TIPO_OPERACAO)
            .diaSemana(UPDATED_DIA_SEMANA)
            .horaInicio(UPDATED_HORA_INICIO)
            .horaFim(UPDATED_HORA_FIM)
            .motivo(UPDATED_MOTIVO)
            .ativo(UPDATED_ATIVO)
            .tipoExecucao(UPDATED_TIPO_EXECUCAO);
        BloqueioOperacaoDTO bloqueioOperacaoDTO = bloqueioOperacaoMapper.toDto(updatedBloqueioOperacao);

        restBloqueioOperacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bloqueioOperacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bloqueioOperacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the BloqueioOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBloqueioOperacaoToMatchAllProperties(updatedBloqueioOperacao);
    }

    @Test
    @Transactional
    void putNonExistingBloqueioOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bloqueioOperacao.setId(longCount.incrementAndGet());

        // Create the BloqueioOperacao
        BloqueioOperacaoDTO bloqueioOperacaoDTO = bloqueioOperacaoMapper.toDto(bloqueioOperacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBloqueioOperacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bloqueioOperacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bloqueioOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BloqueioOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBloqueioOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bloqueioOperacao.setId(longCount.incrementAndGet());

        // Create the BloqueioOperacao
        BloqueioOperacaoDTO bloqueioOperacaoDTO = bloqueioOperacaoMapper.toDto(bloqueioOperacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBloqueioOperacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bloqueioOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BloqueioOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBloqueioOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bloqueioOperacao.setId(longCount.incrementAndGet());

        // Create the BloqueioOperacao
        BloqueioOperacaoDTO bloqueioOperacaoDTO = bloqueioOperacaoMapper.toDto(bloqueioOperacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBloqueioOperacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bloqueioOperacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BloqueioOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBloqueioOperacaoWithPatch() throws Exception {
        // Initialize the database
        insertedBloqueioOperacao = bloqueioOperacaoRepository.saveAndFlush(bloqueioOperacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bloqueioOperacao using partial update
        BloqueioOperacao partialUpdatedBloqueioOperacao = new BloqueioOperacao();
        partialUpdatedBloqueioOperacao.setId(bloqueioOperacao.getId());

        partialUpdatedBloqueioOperacao
            .diaSemana(UPDATED_DIA_SEMANA)
            .horaInicio(UPDATED_HORA_INICIO)
            .horaFim(UPDATED_HORA_FIM)
            .ativo(UPDATED_ATIVO);

        restBloqueioOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBloqueioOperacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBloqueioOperacao))
            )
            .andExpect(status().isOk());

        // Validate the BloqueioOperacao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBloqueioOperacaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBloqueioOperacao, bloqueioOperacao),
            getPersistedBloqueioOperacao(bloqueioOperacao)
        );
    }

    @Test
    @Transactional
    void fullUpdateBloqueioOperacaoWithPatch() throws Exception {
        // Initialize the database
        insertedBloqueioOperacao = bloqueioOperacaoRepository.saveAndFlush(bloqueioOperacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bloqueioOperacao using partial update
        BloqueioOperacao partialUpdatedBloqueioOperacao = new BloqueioOperacao();
        partialUpdatedBloqueioOperacao.setId(bloqueioOperacao.getId());

        partialUpdatedBloqueioOperacao
            .tipoOperacao(UPDATED_TIPO_OPERACAO)
            .diaSemana(UPDATED_DIA_SEMANA)
            .horaInicio(UPDATED_HORA_INICIO)
            .horaFim(UPDATED_HORA_FIM)
            .motivo(UPDATED_MOTIVO)
            .ativo(UPDATED_ATIVO)
            .tipoExecucao(UPDATED_TIPO_EXECUCAO);

        restBloqueioOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBloqueioOperacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBloqueioOperacao))
            )
            .andExpect(status().isOk());

        // Validate the BloqueioOperacao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBloqueioOperacaoUpdatableFieldsEquals(
            partialUpdatedBloqueioOperacao,
            getPersistedBloqueioOperacao(partialUpdatedBloqueioOperacao)
        );
    }

    @Test
    @Transactional
    void patchNonExistingBloqueioOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bloqueioOperacao.setId(longCount.incrementAndGet());

        // Create the BloqueioOperacao
        BloqueioOperacaoDTO bloqueioOperacaoDTO = bloqueioOperacaoMapper.toDto(bloqueioOperacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBloqueioOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bloqueioOperacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bloqueioOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BloqueioOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBloqueioOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bloqueioOperacao.setId(longCount.incrementAndGet());

        // Create the BloqueioOperacao
        BloqueioOperacaoDTO bloqueioOperacaoDTO = bloqueioOperacaoMapper.toDto(bloqueioOperacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBloqueioOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bloqueioOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BloqueioOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBloqueioOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bloqueioOperacao.setId(longCount.incrementAndGet());

        // Create the BloqueioOperacao
        BloqueioOperacaoDTO bloqueioOperacaoDTO = bloqueioOperacaoMapper.toDto(bloqueioOperacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBloqueioOperacaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bloqueioOperacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BloqueioOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBloqueioOperacao() throws Exception {
        // Initialize the database
        insertedBloqueioOperacao = bloqueioOperacaoRepository.saveAndFlush(bloqueioOperacao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bloqueioOperacao
        restBloqueioOperacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, bloqueioOperacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bloqueioOperacaoRepository.count();
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

    protected BloqueioOperacao getPersistedBloqueioOperacao(BloqueioOperacao bloqueioOperacao) {
        return bloqueioOperacaoRepository.findById(bloqueioOperacao.getId()).orElseThrow();
    }

    protected void assertPersistedBloqueioOperacaoToMatchAllProperties(BloqueioOperacao expectedBloqueioOperacao) {
        assertBloqueioOperacaoAllPropertiesEquals(expectedBloqueioOperacao, getPersistedBloqueioOperacao(expectedBloqueioOperacao));
    }

    protected void assertPersistedBloqueioOperacaoToMatchUpdatableProperties(BloqueioOperacao expectedBloqueioOperacao) {
        assertBloqueioOperacaoAllUpdatablePropertiesEquals(
            expectedBloqueioOperacao,
            getPersistedBloqueioOperacao(expectedBloqueioOperacao)
        );
    }
}
