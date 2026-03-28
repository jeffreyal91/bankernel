package com.bankernel.web.rest;

import static com.bankernel.domain.IntegracaoLogAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.IntegracaoLog;
import com.bankernel.domain.enumeration.EnumTipoIntegracao;
import com.bankernel.repository.IntegracaoLogRepository;
import com.bankernel.service.dto.IntegracaoLogDTO;
import com.bankernel.service.mapper.IntegracaoLogMapper;
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
 * Integration tests for the {@link IntegracaoLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IntegracaoLogResourceIT {

    private static final String DEFAULT_PROVIDER = "AAAAAAAAAA";
    private static final String UPDATED_PROVIDER = "BBBBBBBBBB";

    private static final EnumTipoIntegracao DEFAULT_TIPO_INTEGRACAO = EnumTipoIntegracao.PIX;
    private static final EnumTipoIntegracao UPDATED_TIPO_INTEGRACAO = EnumTipoIntegracao.BOLETO;

    private static final String DEFAULT_OPERACAO = "AAAAAAAAAA";
    private static final String UPDATED_OPERACAO = "BBBBBBBBBB";

    private static final String DEFAULT_REQUEST_BODY = "AAAAAAAAAA";
    private static final String UPDATED_REQUEST_BODY = "BBBBBBBBBB";

    private static final String DEFAULT_RESPONSE_BODY = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_BODY = "BBBBBBBBBB";

    private static final Integer DEFAULT_STATUS_HTTP = 1;
    private static final Integer UPDATED_STATUS_HTTP = 2;
    private static final Integer SMALLER_STATUS_HTTP = 1 - 1;

    private static final Boolean DEFAULT_SUCESSO = false;
    private static final Boolean UPDATED_SUCESSO = true;

    private static final String DEFAULT_MENSAGEM_ERRO = "AAAAAAAAAA";
    private static final String UPDATED_MENSAGEM_ERRO = "BBBBBBBBBB";

    private static final Long DEFAULT_DURACAO_MS = 1L;
    private static final Long UPDATED_DURACAO_MS = 2L;
    private static final Long SMALLER_DURACAO_MS = 1L - 1L;

    private static final String DEFAULT_TIPO_ENTIDADE_ORIGEM = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_ENTIDADE_ORIGEM = "BBBBBBBBBB";

    private static final Long DEFAULT_ID_ENTIDADE_ORIGEM = 1L;
    private static final Long UPDATED_ID_ENTIDADE_ORIGEM = 2L;
    private static final Long SMALLER_ID_ENTIDADE_ORIGEM = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/integracao-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private IntegracaoLogRepository integracaoLogRepository;

    @Autowired
    private IntegracaoLogMapper integracaoLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIntegracaoLogMockMvc;

    private IntegracaoLog integracaoLog;

    private IntegracaoLog insertedIntegracaoLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IntegracaoLog createEntity() {
        return new IntegracaoLog()
            .provider(DEFAULT_PROVIDER)
            .tipoIntegracao(DEFAULT_TIPO_INTEGRACAO)
            .operacao(DEFAULT_OPERACAO)
            .requestBody(DEFAULT_REQUEST_BODY)
            .responseBody(DEFAULT_RESPONSE_BODY)
            .statusHttp(DEFAULT_STATUS_HTTP)
            .sucesso(DEFAULT_SUCESSO)
            .mensagemErro(DEFAULT_MENSAGEM_ERRO)
            .duracaoMs(DEFAULT_DURACAO_MS)
            .tipoEntidadeOrigem(DEFAULT_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(DEFAULT_ID_ENTIDADE_ORIGEM);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IntegracaoLog createUpdatedEntity() {
        return new IntegracaoLog()
            .provider(UPDATED_PROVIDER)
            .tipoIntegracao(UPDATED_TIPO_INTEGRACAO)
            .operacao(UPDATED_OPERACAO)
            .requestBody(UPDATED_REQUEST_BODY)
            .responseBody(UPDATED_RESPONSE_BODY)
            .statusHttp(UPDATED_STATUS_HTTP)
            .sucesso(UPDATED_SUCESSO)
            .mensagemErro(UPDATED_MENSAGEM_ERRO)
            .duracaoMs(UPDATED_DURACAO_MS)
            .tipoEntidadeOrigem(UPDATED_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(UPDATED_ID_ENTIDADE_ORIGEM);
    }

    @BeforeEach
    void initTest() {
        integracaoLog = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedIntegracaoLog != null) {
            integracaoLogRepository.delete(insertedIntegracaoLog);
            insertedIntegracaoLog = null;
        }
    }

    @Test
    @Transactional
    void createIntegracaoLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the IntegracaoLog
        IntegracaoLogDTO integracaoLogDTO = integracaoLogMapper.toDto(integracaoLog);
        var returnedIntegracaoLogDTO = om.readValue(
            restIntegracaoLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(integracaoLogDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            IntegracaoLogDTO.class
        );

        // Validate the IntegracaoLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedIntegracaoLog = integracaoLogMapper.toEntity(returnedIntegracaoLogDTO);
        assertIntegracaoLogUpdatableFieldsEquals(returnedIntegracaoLog, getPersistedIntegracaoLog(returnedIntegracaoLog));

        insertedIntegracaoLog = returnedIntegracaoLog;
    }

    @Test
    @Transactional
    void createIntegracaoLogWithExistingId() throws Exception {
        // Create the IntegracaoLog with an existing ID
        integracaoLog.setId(1L);
        IntegracaoLogDTO integracaoLogDTO = integracaoLogMapper.toDto(integracaoLog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIntegracaoLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(integracaoLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the IntegracaoLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProviderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        integracaoLog.setProvider(null);

        // Create the IntegracaoLog, which fails.
        IntegracaoLogDTO integracaoLogDTO = integracaoLogMapper.toDto(integracaoLog);

        restIntegracaoLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(integracaoLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIntegracaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        integracaoLog.setTipoIntegracao(null);

        // Create the IntegracaoLog, which fails.
        IntegracaoLogDTO integracaoLogDTO = integracaoLogMapper.toDto(integracaoLog);

        restIntegracaoLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(integracaoLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOperacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        integracaoLog.setOperacao(null);

        // Create the IntegracaoLog, which fails.
        IntegracaoLogDTO integracaoLogDTO = integracaoLogMapper.toDto(integracaoLog);

        restIntegracaoLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(integracaoLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSucessoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        integracaoLog.setSucesso(null);

        // Create the IntegracaoLog, which fails.
        IntegracaoLogDTO integracaoLogDTO = integracaoLogMapper.toDto(integracaoLog);

        restIntegracaoLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(integracaoLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogs() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList
        restIntegracaoLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(integracaoLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER)))
            .andExpect(jsonPath("$.[*].tipoIntegracao").value(hasItem(DEFAULT_TIPO_INTEGRACAO.toString())))
            .andExpect(jsonPath("$.[*].operacao").value(hasItem(DEFAULT_OPERACAO)))
            .andExpect(jsonPath("$.[*].requestBody").value(hasItem(DEFAULT_REQUEST_BODY)))
            .andExpect(jsonPath("$.[*].responseBody").value(hasItem(DEFAULT_RESPONSE_BODY)))
            .andExpect(jsonPath("$.[*].statusHttp").value(hasItem(DEFAULT_STATUS_HTTP)))
            .andExpect(jsonPath("$.[*].sucesso").value(hasItem(DEFAULT_SUCESSO)))
            .andExpect(jsonPath("$.[*].mensagemErro").value(hasItem(DEFAULT_MENSAGEM_ERRO)))
            .andExpect(jsonPath("$.[*].duracaoMs").value(hasItem(DEFAULT_DURACAO_MS.intValue())))
            .andExpect(jsonPath("$.[*].tipoEntidadeOrigem").value(hasItem(DEFAULT_TIPO_ENTIDADE_ORIGEM)))
            .andExpect(jsonPath("$.[*].idEntidadeOrigem").value(hasItem(DEFAULT_ID_ENTIDADE_ORIGEM.intValue())));
    }

    @Test
    @Transactional
    void getIntegracaoLog() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get the integracaoLog
        restIntegracaoLogMockMvc
            .perform(get(ENTITY_API_URL_ID, integracaoLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(integracaoLog.getId().intValue()))
            .andExpect(jsonPath("$.provider").value(DEFAULT_PROVIDER))
            .andExpect(jsonPath("$.tipoIntegracao").value(DEFAULT_TIPO_INTEGRACAO.toString()))
            .andExpect(jsonPath("$.operacao").value(DEFAULT_OPERACAO))
            .andExpect(jsonPath("$.requestBody").value(DEFAULT_REQUEST_BODY))
            .andExpect(jsonPath("$.responseBody").value(DEFAULT_RESPONSE_BODY))
            .andExpect(jsonPath("$.statusHttp").value(DEFAULT_STATUS_HTTP))
            .andExpect(jsonPath("$.sucesso").value(DEFAULT_SUCESSO))
            .andExpect(jsonPath("$.mensagemErro").value(DEFAULT_MENSAGEM_ERRO))
            .andExpect(jsonPath("$.duracaoMs").value(DEFAULT_DURACAO_MS.intValue()))
            .andExpect(jsonPath("$.tipoEntidadeOrigem").value(DEFAULT_TIPO_ENTIDADE_ORIGEM))
            .andExpect(jsonPath("$.idEntidadeOrigem").value(DEFAULT_ID_ENTIDADE_ORIGEM.intValue()));
    }

    @Test
    @Transactional
    void getIntegracaoLogsByIdFiltering() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        Long id = integracaoLog.getId();

        defaultIntegracaoLogFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultIntegracaoLogFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultIntegracaoLogFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where provider equals to
        defaultIntegracaoLogFiltering("provider.equals=" + DEFAULT_PROVIDER, "provider.equals=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByProviderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where provider in
        defaultIntegracaoLogFiltering("provider.in=" + DEFAULT_PROVIDER + "," + UPDATED_PROVIDER, "provider.in=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where provider is not null
        defaultIntegracaoLogFiltering("provider.specified=true", "provider.specified=false");
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByProviderContainsSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where provider contains
        defaultIntegracaoLogFiltering("provider.contains=" + DEFAULT_PROVIDER, "provider.contains=" + UPDATED_PROVIDER);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByProviderNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where provider does not contain
        defaultIntegracaoLogFiltering("provider.doesNotContain=" + UPDATED_PROVIDER, "provider.doesNotContain=" + DEFAULT_PROVIDER);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByTipoIntegracaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where tipoIntegracao equals to
        defaultIntegracaoLogFiltering(
            "tipoIntegracao.equals=" + DEFAULT_TIPO_INTEGRACAO,
            "tipoIntegracao.equals=" + UPDATED_TIPO_INTEGRACAO
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByTipoIntegracaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where tipoIntegracao in
        defaultIntegracaoLogFiltering(
            "tipoIntegracao.in=" + DEFAULT_TIPO_INTEGRACAO + "," + UPDATED_TIPO_INTEGRACAO,
            "tipoIntegracao.in=" + UPDATED_TIPO_INTEGRACAO
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByTipoIntegracaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where tipoIntegracao is not null
        defaultIntegracaoLogFiltering("tipoIntegracao.specified=true", "tipoIntegracao.specified=false");
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByOperacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where operacao equals to
        defaultIntegracaoLogFiltering("operacao.equals=" + DEFAULT_OPERACAO, "operacao.equals=" + UPDATED_OPERACAO);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByOperacaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where operacao in
        defaultIntegracaoLogFiltering("operacao.in=" + DEFAULT_OPERACAO + "," + UPDATED_OPERACAO, "operacao.in=" + UPDATED_OPERACAO);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByOperacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where operacao is not null
        defaultIntegracaoLogFiltering("operacao.specified=true", "operacao.specified=false");
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByOperacaoContainsSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where operacao contains
        defaultIntegracaoLogFiltering("operacao.contains=" + DEFAULT_OPERACAO, "operacao.contains=" + UPDATED_OPERACAO);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByOperacaoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where operacao does not contain
        defaultIntegracaoLogFiltering("operacao.doesNotContain=" + UPDATED_OPERACAO, "operacao.doesNotContain=" + DEFAULT_OPERACAO);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByStatusHttpIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where statusHttp equals to
        defaultIntegracaoLogFiltering("statusHttp.equals=" + DEFAULT_STATUS_HTTP, "statusHttp.equals=" + UPDATED_STATUS_HTTP);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByStatusHttpIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where statusHttp in
        defaultIntegracaoLogFiltering(
            "statusHttp.in=" + DEFAULT_STATUS_HTTP + "," + UPDATED_STATUS_HTTP,
            "statusHttp.in=" + UPDATED_STATUS_HTTP
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByStatusHttpIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where statusHttp is not null
        defaultIntegracaoLogFiltering("statusHttp.specified=true", "statusHttp.specified=false");
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByStatusHttpIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where statusHttp is greater than or equal to
        defaultIntegracaoLogFiltering(
            "statusHttp.greaterThanOrEqual=" + DEFAULT_STATUS_HTTP,
            "statusHttp.greaterThanOrEqual=" + UPDATED_STATUS_HTTP
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByStatusHttpIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where statusHttp is less than or equal to
        defaultIntegracaoLogFiltering(
            "statusHttp.lessThanOrEqual=" + DEFAULT_STATUS_HTTP,
            "statusHttp.lessThanOrEqual=" + SMALLER_STATUS_HTTP
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByStatusHttpIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where statusHttp is less than
        defaultIntegracaoLogFiltering("statusHttp.lessThan=" + UPDATED_STATUS_HTTP, "statusHttp.lessThan=" + DEFAULT_STATUS_HTTP);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByStatusHttpIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where statusHttp is greater than
        defaultIntegracaoLogFiltering("statusHttp.greaterThan=" + SMALLER_STATUS_HTTP, "statusHttp.greaterThan=" + DEFAULT_STATUS_HTTP);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsBySucessoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where sucesso equals to
        defaultIntegracaoLogFiltering("sucesso.equals=" + DEFAULT_SUCESSO, "sucesso.equals=" + UPDATED_SUCESSO);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsBySucessoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where sucesso in
        defaultIntegracaoLogFiltering("sucesso.in=" + DEFAULT_SUCESSO + "," + UPDATED_SUCESSO, "sucesso.in=" + UPDATED_SUCESSO);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsBySucessoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where sucesso is not null
        defaultIntegracaoLogFiltering("sucesso.specified=true", "sucesso.specified=false");
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByMensagemErroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where mensagemErro equals to
        defaultIntegracaoLogFiltering("mensagemErro.equals=" + DEFAULT_MENSAGEM_ERRO, "mensagemErro.equals=" + UPDATED_MENSAGEM_ERRO);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByMensagemErroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where mensagemErro in
        defaultIntegracaoLogFiltering(
            "mensagemErro.in=" + DEFAULT_MENSAGEM_ERRO + "," + UPDATED_MENSAGEM_ERRO,
            "mensagemErro.in=" + UPDATED_MENSAGEM_ERRO
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByMensagemErroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where mensagemErro is not null
        defaultIntegracaoLogFiltering("mensagemErro.specified=true", "mensagemErro.specified=false");
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByMensagemErroContainsSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where mensagemErro contains
        defaultIntegracaoLogFiltering("mensagemErro.contains=" + DEFAULT_MENSAGEM_ERRO, "mensagemErro.contains=" + UPDATED_MENSAGEM_ERRO);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByMensagemErroNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where mensagemErro does not contain
        defaultIntegracaoLogFiltering(
            "mensagemErro.doesNotContain=" + UPDATED_MENSAGEM_ERRO,
            "mensagemErro.doesNotContain=" + DEFAULT_MENSAGEM_ERRO
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByDuracaoMsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where duracaoMs equals to
        defaultIntegracaoLogFiltering("duracaoMs.equals=" + DEFAULT_DURACAO_MS, "duracaoMs.equals=" + UPDATED_DURACAO_MS);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByDuracaoMsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where duracaoMs in
        defaultIntegracaoLogFiltering(
            "duracaoMs.in=" + DEFAULT_DURACAO_MS + "," + UPDATED_DURACAO_MS,
            "duracaoMs.in=" + UPDATED_DURACAO_MS
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByDuracaoMsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where duracaoMs is not null
        defaultIntegracaoLogFiltering("duracaoMs.specified=true", "duracaoMs.specified=false");
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByDuracaoMsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where duracaoMs is greater than or equal to
        defaultIntegracaoLogFiltering(
            "duracaoMs.greaterThanOrEqual=" + DEFAULT_DURACAO_MS,
            "duracaoMs.greaterThanOrEqual=" + UPDATED_DURACAO_MS
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByDuracaoMsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where duracaoMs is less than or equal to
        defaultIntegracaoLogFiltering("duracaoMs.lessThanOrEqual=" + DEFAULT_DURACAO_MS, "duracaoMs.lessThanOrEqual=" + SMALLER_DURACAO_MS);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByDuracaoMsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where duracaoMs is less than
        defaultIntegracaoLogFiltering("duracaoMs.lessThan=" + UPDATED_DURACAO_MS, "duracaoMs.lessThan=" + DEFAULT_DURACAO_MS);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByDuracaoMsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where duracaoMs is greater than
        defaultIntegracaoLogFiltering("duracaoMs.greaterThan=" + SMALLER_DURACAO_MS, "duracaoMs.greaterThan=" + DEFAULT_DURACAO_MS);
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByTipoEntidadeOrigemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where tipoEntidadeOrigem equals to
        defaultIntegracaoLogFiltering(
            "tipoEntidadeOrigem.equals=" + DEFAULT_TIPO_ENTIDADE_ORIGEM,
            "tipoEntidadeOrigem.equals=" + UPDATED_TIPO_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByTipoEntidadeOrigemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where tipoEntidadeOrigem in
        defaultIntegracaoLogFiltering(
            "tipoEntidadeOrigem.in=" + DEFAULT_TIPO_ENTIDADE_ORIGEM + "," + UPDATED_TIPO_ENTIDADE_ORIGEM,
            "tipoEntidadeOrigem.in=" + UPDATED_TIPO_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByTipoEntidadeOrigemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where tipoEntidadeOrigem is not null
        defaultIntegracaoLogFiltering("tipoEntidadeOrigem.specified=true", "tipoEntidadeOrigem.specified=false");
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByTipoEntidadeOrigemContainsSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where tipoEntidadeOrigem contains
        defaultIntegracaoLogFiltering(
            "tipoEntidadeOrigem.contains=" + DEFAULT_TIPO_ENTIDADE_ORIGEM,
            "tipoEntidadeOrigem.contains=" + UPDATED_TIPO_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByTipoEntidadeOrigemNotContainsSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where tipoEntidadeOrigem does not contain
        defaultIntegracaoLogFiltering(
            "tipoEntidadeOrigem.doesNotContain=" + UPDATED_TIPO_ENTIDADE_ORIGEM,
            "tipoEntidadeOrigem.doesNotContain=" + DEFAULT_TIPO_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByIdEntidadeOrigemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where idEntidadeOrigem equals to
        defaultIntegracaoLogFiltering(
            "idEntidadeOrigem.equals=" + DEFAULT_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.equals=" + UPDATED_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByIdEntidadeOrigemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where idEntidadeOrigem in
        defaultIntegracaoLogFiltering(
            "idEntidadeOrigem.in=" + DEFAULT_ID_ENTIDADE_ORIGEM + "," + UPDATED_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.in=" + UPDATED_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByIdEntidadeOrigemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where idEntidadeOrigem is not null
        defaultIntegracaoLogFiltering("idEntidadeOrigem.specified=true", "idEntidadeOrigem.specified=false");
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByIdEntidadeOrigemIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where idEntidadeOrigem is greater than or equal to
        defaultIntegracaoLogFiltering(
            "idEntidadeOrigem.greaterThanOrEqual=" + DEFAULT_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.greaterThanOrEqual=" + UPDATED_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByIdEntidadeOrigemIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where idEntidadeOrigem is less than or equal to
        defaultIntegracaoLogFiltering(
            "idEntidadeOrigem.lessThanOrEqual=" + DEFAULT_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.lessThanOrEqual=" + SMALLER_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByIdEntidadeOrigemIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where idEntidadeOrigem is less than
        defaultIntegracaoLogFiltering(
            "idEntidadeOrigem.lessThan=" + UPDATED_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.lessThan=" + DEFAULT_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllIntegracaoLogsByIdEntidadeOrigemIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        // Get all the integracaoLogList where idEntidadeOrigem is greater than
        defaultIntegracaoLogFiltering(
            "idEntidadeOrigem.greaterThan=" + SMALLER_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.greaterThan=" + DEFAULT_ID_ENTIDADE_ORIGEM
        );
    }

    private void defaultIntegracaoLogFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultIntegracaoLogShouldBeFound(shouldBeFound);
        defaultIntegracaoLogShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultIntegracaoLogShouldBeFound(String filter) throws Exception {
        restIntegracaoLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(integracaoLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].provider").value(hasItem(DEFAULT_PROVIDER)))
            .andExpect(jsonPath("$.[*].tipoIntegracao").value(hasItem(DEFAULT_TIPO_INTEGRACAO.toString())))
            .andExpect(jsonPath("$.[*].operacao").value(hasItem(DEFAULT_OPERACAO)))
            .andExpect(jsonPath("$.[*].requestBody").value(hasItem(DEFAULT_REQUEST_BODY)))
            .andExpect(jsonPath("$.[*].responseBody").value(hasItem(DEFAULT_RESPONSE_BODY)))
            .andExpect(jsonPath("$.[*].statusHttp").value(hasItem(DEFAULT_STATUS_HTTP)))
            .andExpect(jsonPath("$.[*].sucesso").value(hasItem(DEFAULT_SUCESSO)))
            .andExpect(jsonPath("$.[*].mensagemErro").value(hasItem(DEFAULT_MENSAGEM_ERRO)))
            .andExpect(jsonPath("$.[*].duracaoMs").value(hasItem(DEFAULT_DURACAO_MS.intValue())))
            .andExpect(jsonPath("$.[*].tipoEntidadeOrigem").value(hasItem(DEFAULT_TIPO_ENTIDADE_ORIGEM)))
            .andExpect(jsonPath("$.[*].idEntidadeOrigem").value(hasItem(DEFAULT_ID_ENTIDADE_ORIGEM.intValue())));

        // Check, that the count call also returns 1
        restIntegracaoLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultIntegracaoLogShouldNotBeFound(String filter) throws Exception {
        restIntegracaoLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restIntegracaoLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingIntegracaoLog() throws Exception {
        // Get the integracaoLog
        restIntegracaoLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIntegracaoLog() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the integracaoLog
        IntegracaoLog updatedIntegracaoLog = integracaoLogRepository.findById(integracaoLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedIntegracaoLog are not directly saved in db
        em.detach(updatedIntegracaoLog);
        updatedIntegracaoLog
            .provider(UPDATED_PROVIDER)
            .tipoIntegracao(UPDATED_TIPO_INTEGRACAO)
            .operacao(UPDATED_OPERACAO)
            .requestBody(UPDATED_REQUEST_BODY)
            .responseBody(UPDATED_RESPONSE_BODY)
            .statusHttp(UPDATED_STATUS_HTTP)
            .sucesso(UPDATED_SUCESSO)
            .mensagemErro(UPDATED_MENSAGEM_ERRO)
            .duracaoMs(UPDATED_DURACAO_MS)
            .tipoEntidadeOrigem(UPDATED_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(UPDATED_ID_ENTIDADE_ORIGEM);
        IntegracaoLogDTO integracaoLogDTO = integracaoLogMapper.toDto(updatedIntegracaoLog);

        restIntegracaoLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, integracaoLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(integracaoLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the IntegracaoLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedIntegracaoLogToMatchAllProperties(updatedIntegracaoLog);
    }

    @Test
    @Transactional
    void putNonExistingIntegracaoLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integracaoLog.setId(longCount.incrementAndGet());

        // Create the IntegracaoLog
        IntegracaoLogDTO integracaoLogDTO = integracaoLogMapper.toDto(integracaoLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntegracaoLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, integracaoLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(integracaoLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IntegracaoLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIntegracaoLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integracaoLog.setId(longCount.incrementAndGet());

        // Create the IntegracaoLog
        IntegracaoLogDTO integracaoLogDTO = integracaoLogMapper.toDto(integracaoLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntegracaoLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(integracaoLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IntegracaoLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIntegracaoLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integracaoLog.setId(longCount.incrementAndGet());

        // Create the IntegracaoLog
        IntegracaoLogDTO integracaoLogDTO = integracaoLogMapper.toDto(integracaoLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntegracaoLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(integracaoLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IntegracaoLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIntegracaoLogWithPatch() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the integracaoLog using partial update
        IntegracaoLog partialUpdatedIntegracaoLog = new IntegracaoLog();
        partialUpdatedIntegracaoLog.setId(integracaoLog.getId());

        partialUpdatedIntegracaoLog
            .provider(UPDATED_PROVIDER)
            .operacao(UPDATED_OPERACAO)
            .requestBody(UPDATED_REQUEST_BODY)
            .statusHttp(UPDATED_STATUS_HTTP)
            .sucesso(UPDATED_SUCESSO)
            .duracaoMs(UPDATED_DURACAO_MS)
            .idEntidadeOrigem(UPDATED_ID_ENTIDADE_ORIGEM);

        restIntegracaoLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntegracaoLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIntegracaoLog))
            )
            .andExpect(status().isOk());

        // Validate the IntegracaoLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIntegracaoLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedIntegracaoLog, integracaoLog),
            getPersistedIntegracaoLog(integracaoLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateIntegracaoLogWithPatch() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the integracaoLog using partial update
        IntegracaoLog partialUpdatedIntegracaoLog = new IntegracaoLog();
        partialUpdatedIntegracaoLog.setId(integracaoLog.getId());

        partialUpdatedIntegracaoLog
            .provider(UPDATED_PROVIDER)
            .tipoIntegracao(UPDATED_TIPO_INTEGRACAO)
            .operacao(UPDATED_OPERACAO)
            .requestBody(UPDATED_REQUEST_BODY)
            .responseBody(UPDATED_RESPONSE_BODY)
            .statusHttp(UPDATED_STATUS_HTTP)
            .sucesso(UPDATED_SUCESSO)
            .mensagemErro(UPDATED_MENSAGEM_ERRO)
            .duracaoMs(UPDATED_DURACAO_MS)
            .tipoEntidadeOrigem(UPDATED_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(UPDATED_ID_ENTIDADE_ORIGEM);

        restIntegracaoLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIntegracaoLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedIntegracaoLog))
            )
            .andExpect(status().isOk());

        // Validate the IntegracaoLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertIntegracaoLogUpdatableFieldsEquals(partialUpdatedIntegracaoLog, getPersistedIntegracaoLog(partialUpdatedIntegracaoLog));
    }

    @Test
    @Transactional
    void patchNonExistingIntegracaoLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integracaoLog.setId(longCount.incrementAndGet());

        // Create the IntegracaoLog
        IntegracaoLogDTO integracaoLogDTO = integracaoLogMapper.toDto(integracaoLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIntegracaoLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, integracaoLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(integracaoLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IntegracaoLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIntegracaoLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integracaoLog.setId(longCount.incrementAndGet());

        // Create the IntegracaoLog
        IntegracaoLogDTO integracaoLogDTO = integracaoLogMapper.toDto(integracaoLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntegracaoLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(integracaoLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the IntegracaoLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIntegracaoLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        integracaoLog.setId(longCount.incrementAndGet());

        // Create the IntegracaoLog
        IntegracaoLogDTO integracaoLogDTO = integracaoLogMapper.toDto(integracaoLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIntegracaoLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(integracaoLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IntegracaoLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIntegracaoLog() throws Exception {
        // Initialize the database
        insertedIntegracaoLog = integracaoLogRepository.saveAndFlush(integracaoLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the integracaoLog
        restIntegracaoLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, integracaoLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return integracaoLogRepository.count();
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

    protected IntegracaoLog getPersistedIntegracaoLog(IntegracaoLog integracaoLog) {
        return integracaoLogRepository.findById(integracaoLog.getId()).orElseThrow();
    }

    protected void assertPersistedIntegracaoLogToMatchAllProperties(IntegracaoLog expectedIntegracaoLog) {
        assertIntegracaoLogAllPropertiesEquals(expectedIntegracaoLog, getPersistedIntegracaoLog(expectedIntegracaoLog));
    }

    protected void assertPersistedIntegracaoLogToMatchUpdatableProperties(IntegracaoLog expectedIntegracaoLog) {
        assertIntegracaoLogAllUpdatablePropertiesEquals(expectedIntegracaoLog, getPersistedIntegracaoLog(expectedIntegracaoLog));
    }
}
