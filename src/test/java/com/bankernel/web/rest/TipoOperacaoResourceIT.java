package com.bankernel.web.rest;

import static com.bankernel.domain.TipoOperacaoAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.TipoOperacao;
import com.bankernel.domain.enumeration.EnumSinalContabil;
import com.bankernel.domain.enumeration.EnumSinalContabil;
import com.bankernel.domain.enumeration.EnumTipoOperacao;
import com.bankernel.repository.TipoOperacaoRepository;
import com.bankernel.service.TipoOperacaoService;
import com.bankernel.service.dto.TipoOperacaoDTO;
import com.bankernel.service.mapper.TipoOperacaoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TipoOperacaoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TipoOperacaoResourceIT {

    private static final EnumTipoOperacao DEFAULT_CODIGO = EnumTipoOperacao.DEPOSITO_PIX;
    private static final EnumTipoOperacao UPDATED_CODIGO = EnumTipoOperacao.DEPOSITO_BOLETO;

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final EnumSinalContabil DEFAULT_SINAL_CREDITO = EnumSinalContabil.POSITIVO;
    private static final EnumSinalContabil UPDATED_SINAL_CREDITO = EnumSinalContabil.NEGATIVO;

    private static final EnumSinalContabil DEFAULT_SINAL_DEBITO = EnumSinalContabil.POSITIVO;
    private static final EnumSinalContabil UPDATED_SINAL_DEBITO = EnumSinalContabil.NEGATIVO;

    private static final String ENTITY_API_URL = "/api/tipo-operacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TipoOperacaoRepository tipoOperacaoRepository;

    @Mock
    private TipoOperacaoRepository tipoOperacaoRepositoryMock;

    @Autowired
    private TipoOperacaoMapper tipoOperacaoMapper;

    @Mock
    private TipoOperacaoService tipoOperacaoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTipoOperacaoMockMvc;

    private TipoOperacao tipoOperacao;

    private TipoOperacao insertedTipoOperacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoOperacao createEntity() {
        return new TipoOperacao()
            .codigo(DEFAULT_CODIGO)
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .ativo(DEFAULT_ATIVO)
            .sinalCredito(DEFAULT_SINAL_CREDITO)
            .sinalDebito(DEFAULT_SINAL_DEBITO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TipoOperacao createUpdatedEntity() {
        return new TipoOperacao()
            .codigo(UPDATED_CODIGO)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .ativo(UPDATED_ATIVO)
            .sinalCredito(UPDATED_SINAL_CREDITO)
            .sinalDebito(UPDATED_SINAL_DEBITO);
    }

    @BeforeEach
    void initTest() {
        tipoOperacao = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTipoOperacao != null) {
            tipoOperacaoRepository.delete(insertedTipoOperacao);
            insertedTipoOperacao = null;
        }
    }

    @Test
    @Transactional
    void createTipoOperacao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TipoOperacao
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);
        var returnedTipoOperacaoDTO = om.readValue(
            restTipoOperacaoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoOperacaoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TipoOperacaoDTO.class
        );

        // Validate the TipoOperacao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTipoOperacao = tipoOperacaoMapper.toEntity(returnedTipoOperacaoDTO);
        assertTipoOperacaoUpdatableFieldsEquals(returnedTipoOperacao, getPersistedTipoOperacao(returnedTipoOperacao));

        insertedTipoOperacao = returnedTipoOperacao;
    }

    @Test
    @Transactional
    void createTipoOperacaoWithExistingId() throws Exception {
        // Create the TipoOperacao with an existing ID
        tipoOperacao.setId(1L);
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTipoOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoOperacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TipoOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoOperacao.setCodigo(null);

        // Create the TipoOperacao, which fails.
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        restTipoOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoOperacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoOperacao.setNome(null);

        // Create the TipoOperacao, which fails.
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        restTipoOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoOperacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoOperacao.setAtivo(null);

        // Create the TipoOperacao, which fails.
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        restTipoOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoOperacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSinalCreditoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoOperacao.setSinalCredito(null);

        // Create the TipoOperacao, which fails.
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        restTipoOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoOperacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSinalDebitoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tipoOperacao.setSinalDebito(null);

        // Create the TipoOperacao, which fails.
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        restTipoOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoOperacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTipoOperacaos() throws Exception {
        // Initialize the database
        insertedTipoOperacao = tipoOperacaoRepository.saveAndFlush(tipoOperacao);

        // Get all the tipoOperacaoList
        restTipoOperacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tipoOperacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO.toString())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)))
            .andExpect(jsonPath("$.[*].sinalCredito").value(hasItem(DEFAULT_SINAL_CREDITO.toString())))
            .andExpect(jsonPath("$.[*].sinalDebito").value(hasItem(DEFAULT_SINAL_DEBITO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTipoOperacaosWithEagerRelationshipsIsEnabled() throws Exception {
        when(tipoOperacaoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTipoOperacaoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(tipoOperacaoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTipoOperacaosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(tipoOperacaoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTipoOperacaoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(tipoOperacaoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTipoOperacao() throws Exception {
        // Initialize the database
        insertedTipoOperacao = tipoOperacaoRepository.saveAndFlush(tipoOperacao);

        // Get the tipoOperacao
        restTipoOperacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, tipoOperacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tipoOperacao.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO.toString()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO))
            .andExpect(jsonPath("$.sinalCredito").value(DEFAULT_SINAL_CREDITO.toString()))
            .andExpect(jsonPath("$.sinalDebito").value(DEFAULT_SINAL_DEBITO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTipoOperacao() throws Exception {
        // Get the tipoOperacao
        restTipoOperacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTipoOperacao() throws Exception {
        // Initialize the database
        insertedTipoOperacao = tipoOperacaoRepository.saveAndFlush(tipoOperacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoOperacao
        TipoOperacao updatedTipoOperacao = tipoOperacaoRepository.findById(tipoOperacao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTipoOperacao are not directly saved in db
        em.detach(updatedTipoOperacao);
        updatedTipoOperacao
            .codigo(UPDATED_CODIGO)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .ativo(UPDATED_ATIVO)
            .sinalCredito(UPDATED_SINAL_CREDITO)
            .sinalDebito(UPDATED_SINAL_DEBITO);
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(updatedTipoOperacao);

        restTipoOperacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoOperacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoOperacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the TipoOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTipoOperacaoToMatchAllProperties(updatedTipoOperacao);
    }

    @Test
    @Transactional
    void putNonExistingTipoOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoOperacao.setId(longCount.incrementAndGet());

        // Create the TipoOperacao
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoOperacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tipoOperacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTipoOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoOperacao.setId(longCount.incrementAndGet());

        // Create the TipoOperacao
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoOperacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tipoOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTipoOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoOperacao.setId(longCount.incrementAndGet());

        // Create the TipoOperacao
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoOperacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tipoOperacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTipoOperacaoWithPatch() throws Exception {
        // Initialize the database
        insertedTipoOperacao = tipoOperacaoRepository.saveAndFlush(tipoOperacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoOperacao using partial update
        TipoOperacao partialUpdatedTipoOperacao = new TipoOperacao();
        partialUpdatedTipoOperacao.setId(tipoOperacao.getId());

        partialUpdatedTipoOperacao
            .codigo(UPDATED_CODIGO)
            .descricao(UPDATED_DESCRICAO)
            .ativo(UPDATED_ATIVO)
            .sinalCredito(UPDATED_SINAL_CREDITO);

        restTipoOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoOperacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoOperacao))
            )
            .andExpect(status().isOk());

        // Validate the TipoOperacao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoOperacaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTipoOperacao, tipoOperacao),
            getPersistedTipoOperacao(tipoOperacao)
        );
    }

    @Test
    @Transactional
    void fullUpdateTipoOperacaoWithPatch() throws Exception {
        // Initialize the database
        insertedTipoOperacao = tipoOperacaoRepository.saveAndFlush(tipoOperacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tipoOperacao using partial update
        TipoOperacao partialUpdatedTipoOperacao = new TipoOperacao();
        partialUpdatedTipoOperacao.setId(tipoOperacao.getId());

        partialUpdatedTipoOperacao
            .codigo(UPDATED_CODIGO)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .ativo(UPDATED_ATIVO)
            .sinalCredito(UPDATED_SINAL_CREDITO)
            .sinalDebito(UPDATED_SINAL_DEBITO);

        restTipoOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTipoOperacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTipoOperacao))
            )
            .andExpect(status().isOk());

        // Validate the TipoOperacao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTipoOperacaoUpdatableFieldsEquals(partialUpdatedTipoOperacao, getPersistedTipoOperacao(partialUpdatedTipoOperacao));
    }

    @Test
    @Transactional
    void patchNonExistingTipoOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoOperacao.setId(longCount.incrementAndGet());

        // Create the TipoOperacao
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTipoOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tipoOperacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTipoOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoOperacao.setId(longCount.incrementAndGet());

        // Create the TipoOperacao
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tipoOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TipoOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTipoOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tipoOperacao.setId(longCount.incrementAndGet());

        // Create the TipoOperacao
        TipoOperacaoDTO tipoOperacaoDTO = tipoOperacaoMapper.toDto(tipoOperacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTipoOperacaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tipoOperacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TipoOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTipoOperacao() throws Exception {
        // Initialize the database
        insertedTipoOperacao = tipoOperacaoRepository.saveAndFlush(tipoOperacao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tipoOperacao
        restTipoOperacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, tipoOperacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tipoOperacaoRepository.count();
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

    protected TipoOperacao getPersistedTipoOperacao(TipoOperacao tipoOperacao) {
        return tipoOperacaoRepository.findById(tipoOperacao.getId()).orElseThrow();
    }

    protected void assertPersistedTipoOperacaoToMatchAllProperties(TipoOperacao expectedTipoOperacao) {
        assertTipoOperacaoAllPropertiesEquals(expectedTipoOperacao, getPersistedTipoOperacao(expectedTipoOperacao));
    }

    protected void assertPersistedTipoOperacaoToMatchUpdatableProperties(TipoOperacao expectedTipoOperacao) {
        assertTipoOperacaoAllUpdatablePropertiesEquals(expectedTipoOperacao, getPersistedTipoOperacao(expectedTipoOperacao));
    }
}
