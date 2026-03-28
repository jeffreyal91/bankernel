package com.bankernel.web.rest;

import static com.bankernel.domain.RegistroIntegracaoAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.RegistroIntegracao;
import com.bankernel.domain.enumeration.EnumTipoIntegracao;
import com.bankernel.repository.RegistroIntegracaoRepository;
import com.bankernel.service.dto.RegistroIntegracaoDTO;
import com.bankernel.service.mapper.RegistroIntegracaoMapper;
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
 * Integration tests for the {@link RegistroIntegracaoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RegistroIntegracaoResourceIT {

    private static final String DEFAULT_FORNECEDOR = "AAAAAAAAAA";
    private static final String UPDATED_FORNECEDOR = "BBBBBBBBBB";

    private static final EnumTipoIntegracao DEFAULT_TIPO_INTEGRACAO = EnumTipoIntegracao.PIX;
    private static final EnumTipoIntegracao UPDATED_TIPO_INTEGRACAO = EnumTipoIntegracao.BOLETO;

    private static final String DEFAULT_OPERACAO = "AAAAAAAAAA";
    private static final String UPDATED_OPERACAO = "BBBBBBBBBB";

    private static final String DEFAULT_CORPO_REQUISICAO = "AAAAAAAAAA";
    private static final String UPDATED_CORPO_REQUISICAO = "BBBBBBBBBB";

    private static final String DEFAULT_CORPO_RESPOSTA = "AAAAAAAAAA";
    private static final String UPDATED_CORPO_RESPOSTA = "BBBBBBBBBB";

    private static final Integer DEFAULT_CODIGO_HTTP = 1;
    private static final Integer UPDATED_CODIGO_HTTP = 2;
    private static final Integer SMALLER_CODIGO_HTTP = 1 - 1;

    private static final Boolean DEFAULT_SUCESSO = false;
    private static final Boolean UPDATED_SUCESSO = true;

    private static final String DEFAULT_MENSAGEM_ERRO = "AAAAAAAAAA";
    private static final String UPDATED_MENSAGEM_ERRO = "BBBBBBBBBB";

    private static final Long DEFAULT_DURACAO_MILISSEGUNDOS = 1L;
    private static final Long UPDATED_DURACAO_MILISSEGUNDOS = 2L;
    private static final Long SMALLER_DURACAO_MILISSEGUNDOS = 1L - 1L;

    private static final String DEFAULT_TIPO_ENTIDADE_ORIGEM = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_ENTIDADE_ORIGEM = "BBBBBBBBBB";

    private static final Long DEFAULT_ID_ENTIDADE_ORIGEM = 1L;
    private static final Long UPDATED_ID_ENTIDADE_ORIGEM = 2L;
    private static final Long SMALLER_ID_ENTIDADE_ORIGEM = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/registro-integracaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RegistroIntegracaoRepository registroIntegracaoRepository;

    @Autowired
    private RegistroIntegracaoMapper registroIntegracaoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRegistroIntegracaoMockMvc;

    private RegistroIntegracao registroIntegracao;

    private RegistroIntegracao insertedRegistroIntegracao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegistroIntegracao createEntity() {
        return new RegistroIntegracao()
            .fornecedor(DEFAULT_FORNECEDOR)
            .tipoIntegracao(DEFAULT_TIPO_INTEGRACAO)
            .operacao(DEFAULT_OPERACAO)
            .corpoRequisicao(DEFAULT_CORPO_REQUISICAO)
            .corpoResposta(DEFAULT_CORPO_RESPOSTA)
            .codigoHttp(DEFAULT_CODIGO_HTTP)
            .sucesso(DEFAULT_SUCESSO)
            .mensagemErro(DEFAULT_MENSAGEM_ERRO)
            .duracaoMilissegundos(DEFAULT_DURACAO_MILISSEGUNDOS)
            .tipoEntidadeOrigem(DEFAULT_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(DEFAULT_ID_ENTIDADE_ORIGEM);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RegistroIntegracao createUpdatedEntity() {
        return new RegistroIntegracao()
            .fornecedor(UPDATED_FORNECEDOR)
            .tipoIntegracao(UPDATED_TIPO_INTEGRACAO)
            .operacao(UPDATED_OPERACAO)
            .corpoRequisicao(UPDATED_CORPO_REQUISICAO)
            .corpoResposta(UPDATED_CORPO_RESPOSTA)
            .codigoHttp(UPDATED_CODIGO_HTTP)
            .sucesso(UPDATED_SUCESSO)
            .mensagemErro(UPDATED_MENSAGEM_ERRO)
            .duracaoMilissegundos(UPDATED_DURACAO_MILISSEGUNDOS)
            .tipoEntidadeOrigem(UPDATED_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(UPDATED_ID_ENTIDADE_ORIGEM);
    }

    @BeforeEach
    void initTest() {
        registroIntegracao = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRegistroIntegracao != null) {
            registroIntegracaoRepository.delete(insertedRegistroIntegracao);
            insertedRegistroIntegracao = null;
        }
    }

    @Test
    @Transactional
    void createRegistroIntegracao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RegistroIntegracao
        RegistroIntegracaoDTO registroIntegracaoDTO = registroIntegracaoMapper.toDto(registroIntegracao);
        var returnedRegistroIntegracaoDTO = om.readValue(
            restRegistroIntegracaoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(registroIntegracaoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RegistroIntegracaoDTO.class
        );

        // Validate the RegistroIntegracao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRegistroIntegracao = registroIntegracaoMapper.toEntity(returnedRegistroIntegracaoDTO);
        assertRegistroIntegracaoUpdatableFieldsEquals(
            returnedRegistroIntegracao,
            getPersistedRegistroIntegracao(returnedRegistroIntegracao)
        );

        insertedRegistroIntegracao = returnedRegistroIntegracao;
    }

    @Test
    @Transactional
    void createRegistroIntegracaoWithExistingId() throws Exception {
        // Create the RegistroIntegracao with an existing ID
        registroIntegracao.setId(1L);
        RegistroIntegracaoDTO registroIntegracaoDTO = registroIntegracaoMapper.toDto(registroIntegracao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRegistroIntegracaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(registroIntegracaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RegistroIntegracao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFornecedorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        registroIntegracao.setFornecedor(null);

        // Create the RegistroIntegracao, which fails.
        RegistroIntegracaoDTO registroIntegracaoDTO = registroIntegracaoMapper.toDto(registroIntegracao);

        restRegistroIntegracaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(registroIntegracaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIntegracaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        registroIntegracao.setTipoIntegracao(null);

        // Create the RegistroIntegracao, which fails.
        RegistroIntegracaoDTO registroIntegracaoDTO = registroIntegracaoMapper.toDto(registroIntegracao);

        restRegistroIntegracaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(registroIntegracaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOperacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        registroIntegracao.setOperacao(null);

        // Create the RegistroIntegracao, which fails.
        RegistroIntegracaoDTO registroIntegracaoDTO = registroIntegracaoMapper.toDto(registroIntegracao);

        restRegistroIntegracaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(registroIntegracaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSucessoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        registroIntegracao.setSucesso(null);

        // Create the RegistroIntegracao, which fails.
        RegistroIntegracaoDTO registroIntegracaoDTO = registroIntegracaoMapper.toDto(registroIntegracao);

        restRegistroIntegracaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(registroIntegracaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaos() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList
        restRegistroIntegracaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registroIntegracao.getId().intValue())))
            .andExpect(jsonPath("$.[*].fornecedor").value(hasItem(DEFAULT_FORNECEDOR)))
            .andExpect(jsonPath("$.[*].tipoIntegracao").value(hasItem(DEFAULT_TIPO_INTEGRACAO.toString())))
            .andExpect(jsonPath("$.[*].operacao").value(hasItem(DEFAULT_OPERACAO)))
            .andExpect(jsonPath("$.[*].corpoRequisicao").value(hasItem(DEFAULT_CORPO_REQUISICAO)))
            .andExpect(jsonPath("$.[*].corpoResposta").value(hasItem(DEFAULT_CORPO_RESPOSTA)))
            .andExpect(jsonPath("$.[*].codigoHttp").value(hasItem(DEFAULT_CODIGO_HTTP)))
            .andExpect(jsonPath("$.[*].sucesso").value(hasItem(DEFAULT_SUCESSO)))
            .andExpect(jsonPath("$.[*].mensagemErro").value(hasItem(DEFAULT_MENSAGEM_ERRO)))
            .andExpect(jsonPath("$.[*].duracaoMilissegundos").value(hasItem(DEFAULT_DURACAO_MILISSEGUNDOS.intValue())))
            .andExpect(jsonPath("$.[*].tipoEntidadeOrigem").value(hasItem(DEFAULT_TIPO_ENTIDADE_ORIGEM)))
            .andExpect(jsonPath("$.[*].idEntidadeOrigem").value(hasItem(DEFAULT_ID_ENTIDADE_ORIGEM.intValue())));
    }

    @Test
    @Transactional
    void getRegistroIntegracao() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get the registroIntegracao
        restRegistroIntegracaoMockMvc
            .perform(get(ENTITY_API_URL_ID, registroIntegracao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(registroIntegracao.getId().intValue()))
            .andExpect(jsonPath("$.fornecedor").value(DEFAULT_FORNECEDOR))
            .andExpect(jsonPath("$.tipoIntegracao").value(DEFAULT_TIPO_INTEGRACAO.toString()))
            .andExpect(jsonPath("$.operacao").value(DEFAULT_OPERACAO))
            .andExpect(jsonPath("$.corpoRequisicao").value(DEFAULT_CORPO_REQUISICAO))
            .andExpect(jsonPath("$.corpoResposta").value(DEFAULT_CORPO_RESPOSTA))
            .andExpect(jsonPath("$.codigoHttp").value(DEFAULT_CODIGO_HTTP))
            .andExpect(jsonPath("$.sucesso").value(DEFAULT_SUCESSO))
            .andExpect(jsonPath("$.mensagemErro").value(DEFAULT_MENSAGEM_ERRO))
            .andExpect(jsonPath("$.duracaoMilissegundos").value(DEFAULT_DURACAO_MILISSEGUNDOS.intValue()))
            .andExpect(jsonPath("$.tipoEntidadeOrigem").value(DEFAULT_TIPO_ENTIDADE_ORIGEM))
            .andExpect(jsonPath("$.idEntidadeOrigem").value(DEFAULT_ID_ENTIDADE_ORIGEM.intValue()));
    }

    @Test
    @Transactional
    void getRegistroIntegracaosByIdFiltering() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        Long id = registroIntegracao.getId();

        defaultRegistroIntegracaoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultRegistroIntegracaoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultRegistroIntegracaoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByFornecedorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where fornecedor equals to
        defaultRegistroIntegracaoFiltering("fornecedor.equals=" + DEFAULT_FORNECEDOR, "fornecedor.equals=" + UPDATED_FORNECEDOR);
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByFornecedorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where fornecedor in
        defaultRegistroIntegracaoFiltering(
            "fornecedor.in=" + DEFAULT_FORNECEDOR + "," + UPDATED_FORNECEDOR,
            "fornecedor.in=" + UPDATED_FORNECEDOR
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByFornecedorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where fornecedor is not null
        defaultRegistroIntegracaoFiltering("fornecedor.specified=true", "fornecedor.specified=false");
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByFornecedorContainsSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where fornecedor contains
        defaultRegistroIntegracaoFiltering("fornecedor.contains=" + DEFAULT_FORNECEDOR, "fornecedor.contains=" + UPDATED_FORNECEDOR);
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByFornecedorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where fornecedor does not contain
        defaultRegistroIntegracaoFiltering(
            "fornecedor.doesNotContain=" + UPDATED_FORNECEDOR,
            "fornecedor.doesNotContain=" + DEFAULT_FORNECEDOR
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByTipoIntegracaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where tipoIntegracao equals to
        defaultRegistroIntegracaoFiltering(
            "tipoIntegracao.equals=" + DEFAULT_TIPO_INTEGRACAO,
            "tipoIntegracao.equals=" + UPDATED_TIPO_INTEGRACAO
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByTipoIntegracaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where tipoIntegracao in
        defaultRegistroIntegracaoFiltering(
            "tipoIntegracao.in=" + DEFAULT_TIPO_INTEGRACAO + "," + UPDATED_TIPO_INTEGRACAO,
            "tipoIntegracao.in=" + UPDATED_TIPO_INTEGRACAO
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByTipoIntegracaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where tipoIntegracao is not null
        defaultRegistroIntegracaoFiltering("tipoIntegracao.specified=true", "tipoIntegracao.specified=false");
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByOperacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where operacao equals to
        defaultRegistroIntegracaoFiltering("operacao.equals=" + DEFAULT_OPERACAO, "operacao.equals=" + UPDATED_OPERACAO);
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByOperacaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where operacao in
        defaultRegistroIntegracaoFiltering("operacao.in=" + DEFAULT_OPERACAO + "," + UPDATED_OPERACAO, "operacao.in=" + UPDATED_OPERACAO);
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByOperacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where operacao is not null
        defaultRegistroIntegracaoFiltering("operacao.specified=true", "operacao.specified=false");
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByOperacaoContainsSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where operacao contains
        defaultRegistroIntegracaoFiltering("operacao.contains=" + DEFAULT_OPERACAO, "operacao.contains=" + UPDATED_OPERACAO);
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByOperacaoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where operacao does not contain
        defaultRegistroIntegracaoFiltering("operacao.doesNotContain=" + UPDATED_OPERACAO, "operacao.doesNotContain=" + DEFAULT_OPERACAO);
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByCodigoHttpIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where codigoHttp equals to
        defaultRegistroIntegracaoFiltering("codigoHttp.equals=" + DEFAULT_CODIGO_HTTP, "codigoHttp.equals=" + UPDATED_CODIGO_HTTP);
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByCodigoHttpIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where codigoHttp in
        defaultRegistroIntegracaoFiltering(
            "codigoHttp.in=" + DEFAULT_CODIGO_HTTP + "," + UPDATED_CODIGO_HTTP,
            "codigoHttp.in=" + UPDATED_CODIGO_HTTP
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByCodigoHttpIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where codigoHttp is not null
        defaultRegistroIntegracaoFiltering("codigoHttp.specified=true", "codigoHttp.specified=false");
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByCodigoHttpIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where codigoHttp is greater than or equal to
        defaultRegistroIntegracaoFiltering(
            "codigoHttp.greaterThanOrEqual=" + DEFAULT_CODIGO_HTTP,
            "codigoHttp.greaterThanOrEqual=" + UPDATED_CODIGO_HTTP
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByCodigoHttpIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where codigoHttp is less than or equal to
        defaultRegistroIntegracaoFiltering(
            "codigoHttp.lessThanOrEqual=" + DEFAULT_CODIGO_HTTP,
            "codigoHttp.lessThanOrEqual=" + SMALLER_CODIGO_HTTP
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByCodigoHttpIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where codigoHttp is less than
        defaultRegistroIntegracaoFiltering("codigoHttp.lessThan=" + UPDATED_CODIGO_HTTP, "codigoHttp.lessThan=" + DEFAULT_CODIGO_HTTP);
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByCodigoHttpIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where codigoHttp is greater than
        defaultRegistroIntegracaoFiltering(
            "codigoHttp.greaterThan=" + SMALLER_CODIGO_HTTP,
            "codigoHttp.greaterThan=" + DEFAULT_CODIGO_HTTP
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosBySucessoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where sucesso equals to
        defaultRegistroIntegracaoFiltering("sucesso.equals=" + DEFAULT_SUCESSO, "sucesso.equals=" + UPDATED_SUCESSO);
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosBySucessoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where sucesso in
        defaultRegistroIntegracaoFiltering("sucesso.in=" + DEFAULT_SUCESSO + "," + UPDATED_SUCESSO, "sucesso.in=" + UPDATED_SUCESSO);
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosBySucessoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where sucesso is not null
        defaultRegistroIntegracaoFiltering("sucesso.specified=true", "sucesso.specified=false");
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByMensagemErroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where mensagemErro equals to
        defaultRegistroIntegracaoFiltering("mensagemErro.equals=" + DEFAULT_MENSAGEM_ERRO, "mensagemErro.equals=" + UPDATED_MENSAGEM_ERRO);
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByMensagemErroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where mensagemErro in
        defaultRegistroIntegracaoFiltering(
            "mensagemErro.in=" + DEFAULT_MENSAGEM_ERRO + "," + UPDATED_MENSAGEM_ERRO,
            "mensagemErro.in=" + UPDATED_MENSAGEM_ERRO
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByMensagemErroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where mensagemErro is not null
        defaultRegistroIntegracaoFiltering("mensagemErro.specified=true", "mensagemErro.specified=false");
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByMensagemErroContainsSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where mensagemErro contains
        defaultRegistroIntegracaoFiltering(
            "mensagemErro.contains=" + DEFAULT_MENSAGEM_ERRO,
            "mensagemErro.contains=" + UPDATED_MENSAGEM_ERRO
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByMensagemErroNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where mensagemErro does not contain
        defaultRegistroIntegracaoFiltering(
            "mensagemErro.doesNotContain=" + UPDATED_MENSAGEM_ERRO,
            "mensagemErro.doesNotContain=" + DEFAULT_MENSAGEM_ERRO
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByDuracaoMilissegundosIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where duracaoMilissegundos equals to
        defaultRegistroIntegracaoFiltering(
            "duracaoMilissegundos.equals=" + DEFAULT_DURACAO_MILISSEGUNDOS,
            "duracaoMilissegundos.equals=" + UPDATED_DURACAO_MILISSEGUNDOS
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByDuracaoMilissegundosIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where duracaoMilissegundos in
        defaultRegistroIntegracaoFiltering(
            "duracaoMilissegundos.in=" + DEFAULT_DURACAO_MILISSEGUNDOS + "," + UPDATED_DURACAO_MILISSEGUNDOS,
            "duracaoMilissegundos.in=" + UPDATED_DURACAO_MILISSEGUNDOS
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByDuracaoMilissegundosIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where duracaoMilissegundos is not null
        defaultRegistroIntegracaoFiltering("duracaoMilissegundos.specified=true", "duracaoMilissegundos.specified=false");
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByDuracaoMilissegundosIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where duracaoMilissegundos is greater than or equal to
        defaultRegistroIntegracaoFiltering(
            "duracaoMilissegundos.greaterThanOrEqual=" + DEFAULT_DURACAO_MILISSEGUNDOS,
            "duracaoMilissegundos.greaterThanOrEqual=" + UPDATED_DURACAO_MILISSEGUNDOS
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByDuracaoMilissegundosIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where duracaoMilissegundos is less than or equal to
        defaultRegistroIntegracaoFiltering(
            "duracaoMilissegundos.lessThanOrEqual=" + DEFAULT_DURACAO_MILISSEGUNDOS,
            "duracaoMilissegundos.lessThanOrEqual=" + SMALLER_DURACAO_MILISSEGUNDOS
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByDuracaoMilissegundosIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where duracaoMilissegundos is less than
        defaultRegistroIntegracaoFiltering(
            "duracaoMilissegundos.lessThan=" + UPDATED_DURACAO_MILISSEGUNDOS,
            "duracaoMilissegundos.lessThan=" + DEFAULT_DURACAO_MILISSEGUNDOS
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByDuracaoMilissegundosIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where duracaoMilissegundos is greater than
        defaultRegistroIntegracaoFiltering(
            "duracaoMilissegundos.greaterThan=" + SMALLER_DURACAO_MILISSEGUNDOS,
            "duracaoMilissegundos.greaterThan=" + DEFAULT_DURACAO_MILISSEGUNDOS
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByTipoEntidadeOrigemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where tipoEntidadeOrigem equals to
        defaultRegistroIntegracaoFiltering(
            "tipoEntidadeOrigem.equals=" + DEFAULT_TIPO_ENTIDADE_ORIGEM,
            "tipoEntidadeOrigem.equals=" + UPDATED_TIPO_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByTipoEntidadeOrigemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where tipoEntidadeOrigem in
        defaultRegistroIntegracaoFiltering(
            "tipoEntidadeOrigem.in=" + DEFAULT_TIPO_ENTIDADE_ORIGEM + "," + UPDATED_TIPO_ENTIDADE_ORIGEM,
            "tipoEntidadeOrigem.in=" + UPDATED_TIPO_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByTipoEntidadeOrigemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where tipoEntidadeOrigem is not null
        defaultRegistroIntegracaoFiltering("tipoEntidadeOrigem.specified=true", "tipoEntidadeOrigem.specified=false");
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByTipoEntidadeOrigemContainsSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where tipoEntidadeOrigem contains
        defaultRegistroIntegracaoFiltering(
            "tipoEntidadeOrigem.contains=" + DEFAULT_TIPO_ENTIDADE_ORIGEM,
            "tipoEntidadeOrigem.contains=" + UPDATED_TIPO_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByTipoEntidadeOrigemNotContainsSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where tipoEntidadeOrigem does not contain
        defaultRegistroIntegracaoFiltering(
            "tipoEntidadeOrigem.doesNotContain=" + UPDATED_TIPO_ENTIDADE_ORIGEM,
            "tipoEntidadeOrigem.doesNotContain=" + DEFAULT_TIPO_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByIdEntidadeOrigemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where idEntidadeOrigem equals to
        defaultRegistroIntegracaoFiltering(
            "idEntidadeOrigem.equals=" + DEFAULT_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.equals=" + UPDATED_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByIdEntidadeOrigemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where idEntidadeOrigem in
        defaultRegistroIntegracaoFiltering(
            "idEntidadeOrigem.in=" + DEFAULT_ID_ENTIDADE_ORIGEM + "," + UPDATED_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.in=" + UPDATED_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByIdEntidadeOrigemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where idEntidadeOrigem is not null
        defaultRegistroIntegracaoFiltering("idEntidadeOrigem.specified=true", "idEntidadeOrigem.specified=false");
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByIdEntidadeOrigemIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where idEntidadeOrigem is greater than or equal to
        defaultRegistroIntegracaoFiltering(
            "idEntidadeOrigem.greaterThanOrEqual=" + DEFAULT_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.greaterThanOrEqual=" + UPDATED_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByIdEntidadeOrigemIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where idEntidadeOrigem is less than or equal to
        defaultRegistroIntegracaoFiltering(
            "idEntidadeOrigem.lessThanOrEqual=" + DEFAULT_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.lessThanOrEqual=" + SMALLER_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByIdEntidadeOrigemIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where idEntidadeOrigem is less than
        defaultRegistroIntegracaoFiltering(
            "idEntidadeOrigem.lessThan=" + UPDATED_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.lessThan=" + DEFAULT_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllRegistroIntegracaosByIdEntidadeOrigemIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        // Get all the registroIntegracaoList where idEntidadeOrigem is greater than
        defaultRegistroIntegracaoFiltering(
            "idEntidadeOrigem.greaterThan=" + SMALLER_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.greaterThan=" + DEFAULT_ID_ENTIDADE_ORIGEM
        );
    }

    private void defaultRegistroIntegracaoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultRegistroIntegracaoShouldBeFound(shouldBeFound);
        defaultRegistroIntegracaoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultRegistroIntegracaoShouldBeFound(String filter) throws Exception {
        restRegistroIntegracaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(registroIntegracao.getId().intValue())))
            .andExpect(jsonPath("$.[*].fornecedor").value(hasItem(DEFAULT_FORNECEDOR)))
            .andExpect(jsonPath("$.[*].tipoIntegracao").value(hasItem(DEFAULT_TIPO_INTEGRACAO.toString())))
            .andExpect(jsonPath("$.[*].operacao").value(hasItem(DEFAULT_OPERACAO)))
            .andExpect(jsonPath("$.[*].corpoRequisicao").value(hasItem(DEFAULT_CORPO_REQUISICAO)))
            .andExpect(jsonPath("$.[*].corpoResposta").value(hasItem(DEFAULT_CORPO_RESPOSTA)))
            .andExpect(jsonPath("$.[*].codigoHttp").value(hasItem(DEFAULT_CODIGO_HTTP)))
            .andExpect(jsonPath("$.[*].sucesso").value(hasItem(DEFAULT_SUCESSO)))
            .andExpect(jsonPath("$.[*].mensagemErro").value(hasItem(DEFAULT_MENSAGEM_ERRO)))
            .andExpect(jsonPath("$.[*].duracaoMilissegundos").value(hasItem(DEFAULT_DURACAO_MILISSEGUNDOS.intValue())))
            .andExpect(jsonPath("$.[*].tipoEntidadeOrigem").value(hasItem(DEFAULT_TIPO_ENTIDADE_ORIGEM)))
            .andExpect(jsonPath("$.[*].idEntidadeOrigem").value(hasItem(DEFAULT_ID_ENTIDADE_ORIGEM.intValue())));

        // Check, that the count call also returns 1
        restRegistroIntegracaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultRegistroIntegracaoShouldNotBeFound(String filter) throws Exception {
        restRegistroIntegracaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restRegistroIntegracaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingRegistroIntegracao() throws Exception {
        // Get the registroIntegracao
        restRegistroIntegracaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRegistroIntegracao() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the registroIntegracao
        RegistroIntegracao updatedRegistroIntegracao = registroIntegracaoRepository.findById(registroIntegracao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRegistroIntegracao are not directly saved in db
        em.detach(updatedRegistroIntegracao);
        updatedRegistroIntegracao
            .fornecedor(UPDATED_FORNECEDOR)
            .tipoIntegracao(UPDATED_TIPO_INTEGRACAO)
            .operacao(UPDATED_OPERACAO)
            .corpoRequisicao(UPDATED_CORPO_REQUISICAO)
            .corpoResposta(UPDATED_CORPO_RESPOSTA)
            .codigoHttp(UPDATED_CODIGO_HTTP)
            .sucesso(UPDATED_SUCESSO)
            .mensagemErro(UPDATED_MENSAGEM_ERRO)
            .duracaoMilissegundos(UPDATED_DURACAO_MILISSEGUNDOS)
            .tipoEntidadeOrigem(UPDATED_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(UPDATED_ID_ENTIDADE_ORIGEM);
        RegistroIntegracaoDTO registroIntegracaoDTO = registroIntegracaoMapper.toDto(updatedRegistroIntegracao);

        restRegistroIntegracaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, registroIntegracaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(registroIntegracaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the RegistroIntegracao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRegistroIntegracaoToMatchAllProperties(updatedRegistroIntegracao);
    }

    @Test
    @Transactional
    void putNonExistingRegistroIntegracao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        registroIntegracao.setId(longCount.incrementAndGet());

        // Create the RegistroIntegracao
        RegistroIntegracaoDTO registroIntegracaoDTO = registroIntegracaoMapper.toDto(registroIntegracao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistroIntegracaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, registroIntegracaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(registroIntegracaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroIntegracao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRegistroIntegracao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        registroIntegracao.setId(longCount.incrementAndGet());

        // Create the RegistroIntegracao
        RegistroIntegracaoDTO registroIntegracaoDTO = registroIntegracaoMapper.toDto(registroIntegracao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroIntegracaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(registroIntegracaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroIntegracao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRegistroIntegracao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        registroIntegracao.setId(longCount.incrementAndGet());

        // Create the RegistroIntegracao
        RegistroIntegracaoDTO registroIntegracaoDTO = registroIntegracaoMapper.toDto(registroIntegracao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroIntegracaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(registroIntegracaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RegistroIntegracao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRegistroIntegracaoWithPatch() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the registroIntegracao using partial update
        RegistroIntegracao partialUpdatedRegistroIntegracao = new RegistroIntegracao();
        partialUpdatedRegistroIntegracao.setId(registroIntegracao.getId());

        partialUpdatedRegistroIntegracao
            .tipoIntegracao(UPDATED_TIPO_INTEGRACAO)
            .operacao(UPDATED_OPERACAO)
            .mensagemErro(UPDATED_MENSAGEM_ERRO);

        restRegistroIntegracaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegistroIntegracao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRegistroIntegracao))
            )
            .andExpect(status().isOk());

        // Validate the RegistroIntegracao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRegistroIntegracaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRegistroIntegracao, registroIntegracao),
            getPersistedRegistroIntegracao(registroIntegracao)
        );
    }

    @Test
    @Transactional
    void fullUpdateRegistroIntegracaoWithPatch() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the registroIntegracao using partial update
        RegistroIntegracao partialUpdatedRegistroIntegracao = new RegistroIntegracao();
        partialUpdatedRegistroIntegracao.setId(registroIntegracao.getId());

        partialUpdatedRegistroIntegracao
            .fornecedor(UPDATED_FORNECEDOR)
            .tipoIntegracao(UPDATED_TIPO_INTEGRACAO)
            .operacao(UPDATED_OPERACAO)
            .corpoRequisicao(UPDATED_CORPO_REQUISICAO)
            .corpoResposta(UPDATED_CORPO_RESPOSTA)
            .codigoHttp(UPDATED_CODIGO_HTTP)
            .sucesso(UPDATED_SUCESSO)
            .mensagemErro(UPDATED_MENSAGEM_ERRO)
            .duracaoMilissegundos(UPDATED_DURACAO_MILISSEGUNDOS)
            .tipoEntidadeOrigem(UPDATED_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(UPDATED_ID_ENTIDADE_ORIGEM);

        restRegistroIntegracaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRegistroIntegracao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRegistroIntegracao))
            )
            .andExpect(status().isOk());

        // Validate the RegistroIntegracao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRegistroIntegracaoUpdatableFieldsEquals(
            partialUpdatedRegistroIntegracao,
            getPersistedRegistroIntegracao(partialUpdatedRegistroIntegracao)
        );
    }

    @Test
    @Transactional
    void patchNonExistingRegistroIntegracao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        registroIntegracao.setId(longCount.incrementAndGet());

        // Create the RegistroIntegracao
        RegistroIntegracaoDTO registroIntegracaoDTO = registroIntegracaoMapper.toDto(registroIntegracao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRegistroIntegracaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, registroIntegracaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(registroIntegracaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroIntegracao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRegistroIntegracao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        registroIntegracao.setId(longCount.incrementAndGet());

        // Create the RegistroIntegracao
        RegistroIntegracaoDTO registroIntegracaoDTO = registroIntegracaoMapper.toDto(registroIntegracao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroIntegracaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(registroIntegracaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RegistroIntegracao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRegistroIntegracao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        registroIntegracao.setId(longCount.incrementAndGet());

        // Create the RegistroIntegracao
        RegistroIntegracaoDTO registroIntegracaoDTO = registroIntegracaoMapper.toDto(registroIntegracao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRegistroIntegracaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(registroIntegracaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RegistroIntegracao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRegistroIntegracao() throws Exception {
        // Initialize the database
        insertedRegistroIntegracao = registroIntegracaoRepository.saveAndFlush(registroIntegracao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the registroIntegracao
        restRegistroIntegracaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, registroIntegracao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return registroIntegracaoRepository.count();
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

    protected RegistroIntegracao getPersistedRegistroIntegracao(RegistroIntegracao registroIntegracao) {
        return registroIntegracaoRepository.findById(registroIntegracao.getId()).orElseThrow();
    }

    protected void assertPersistedRegistroIntegracaoToMatchAllProperties(RegistroIntegracao expectedRegistroIntegracao) {
        assertRegistroIntegracaoAllPropertiesEquals(expectedRegistroIntegracao, getPersistedRegistroIntegracao(expectedRegistroIntegracao));
    }

    protected void assertPersistedRegistroIntegracaoToMatchUpdatableProperties(RegistroIntegracao expectedRegistroIntegracao) {
        assertRegistroIntegracaoAllUpdatablePropertiesEquals(
            expectedRegistroIntegracao,
            getPersistedRegistroIntegracao(expectedRegistroIntegracao)
        );
    }
}
