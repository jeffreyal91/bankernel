package com.bankernel.web.rest;

import static com.bankernel.domain.TransacaoAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bankernel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Carteira;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.Transacao;
import com.bankernel.domain.enumeration.EnumStatusTransacao;
import com.bankernel.domain.enumeration.EnumTipoPagamento;
import com.bankernel.domain.enumeration.EnumTipoTransacao;
import com.bankernel.repository.TransacaoRepository;
import com.bankernel.service.TransacaoService;
import com.bankernel.service.dto.TransacaoDTO;
import com.bankernel.service.mapper.TransacaoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link TransacaoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TransacaoResourceIT {

    private static final BigDecimal DEFAULT_VALOR_ENVIADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_ENVIADO = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR_ENVIADO = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_VALOR_RECEBIDO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_RECEBIDO = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR_RECEBIDO = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ESTORNADA = false;
    private static final Boolean UPDATED_ESTORNADA = true;

    private static final EnumTipoTransacao DEFAULT_TIPO_TRANSACAO = EnumTipoTransacao.DEPOSITO;
    private static final EnumTipoTransacao UPDATED_TIPO_TRANSACAO = EnumTipoTransacao.SAQUE;

    private static final EnumTipoPagamento DEFAULT_TIPO_PAGAMENTO = EnumTipoPagamento.PIX;
    private static final EnumTipoPagamento UPDATED_TIPO_PAGAMENTO = EnumTipoPagamento.BOLETO;

    private static final EnumStatusTransacao DEFAULT_SITUACAO = EnumStatusTransacao.PENDENTE;
    private static final EnumStatusTransacao UPDATED_SITUACAO = EnumStatusTransacao.EM_PROCESSAMENTO;

    private static final Boolean DEFAULT_ATIVA = false;
    private static final Boolean UPDATED_ATIVA = true;

    private static final String DEFAULT_TIPO_ENTIDADE_ORIGEM = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_ENTIDADE_ORIGEM = "BBBBBBBBBB";

    private static final Long DEFAULT_ID_ENTIDADE_ORIGEM = 1L;
    private static final Long UPDATED_ID_ENTIDADE_ORIGEM = 2L;
    private static final Long SMALLER_ID_ENTIDADE_ORIGEM = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/transacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Mock
    private TransacaoRepository transacaoRepositoryMock;

    @Autowired
    private TransacaoMapper transacaoMapper;

    @Mock
    private TransacaoService transacaoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransacaoMockMvc;

    private Transacao transacao;

    private Transacao insertedTransacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transacao createEntity() {
        return new Transacao()
            .valorEnviado(DEFAULT_VALOR_ENVIADO)
            .valorRecebido(DEFAULT_VALOR_RECEBIDO)
            .descricao(DEFAULT_DESCRICAO)
            .estornada(DEFAULT_ESTORNADA)
            .tipoTransacao(DEFAULT_TIPO_TRANSACAO)
            .tipoPagamento(DEFAULT_TIPO_PAGAMENTO)
            .situacao(DEFAULT_SITUACAO)
            .ativa(DEFAULT_ATIVA)
            .tipoEntidadeOrigem(DEFAULT_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(DEFAULT_ID_ENTIDADE_ORIGEM);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transacao createUpdatedEntity() {
        return new Transacao()
            .valorEnviado(UPDATED_VALOR_ENVIADO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO)
            .descricao(UPDATED_DESCRICAO)
            .estornada(UPDATED_ESTORNADA)
            .tipoTransacao(UPDATED_TIPO_TRANSACAO)
            .tipoPagamento(UPDATED_TIPO_PAGAMENTO)
            .situacao(UPDATED_SITUACAO)
            .ativa(UPDATED_ATIVA)
            .tipoEntidadeOrigem(UPDATED_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(UPDATED_ID_ENTIDADE_ORIGEM);
    }

    @BeforeEach
    void initTest() {
        transacao = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTransacao != null) {
            transacaoRepository.delete(insertedTransacao);
            insertedTransacao = null;
        }
    }

    @Test
    @Transactional
    void createTransacao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Transacao
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);
        var returnedTransacaoDTO = om.readValue(
            restTransacaoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transacaoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransacaoDTO.class
        );

        // Validate the Transacao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransacao = transacaoMapper.toEntity(returnedTransacaoDTO);
        assertTransacaoUpdatableFieldsEquals(returnedTransacao, getPersistedTransacao(returnedTransacao));

        insertedTransacao = returnedTransacao;
    }

    @Test
    @Transactional
    void createTransacaoWithExistingId() throws Exception {
        // Create the Transacao with an existing ID
        transacao.setId(1L);
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transacao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValorEnviadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transacao.setValorEnviado(null);

        // Create the Transacao, which fails.
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        restTransacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorRecebidoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transacao.setValorRecebido(null);

        // Create the Transacao, which fails.
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        restTransacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstornadaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transacao.setEstornada(null);

        // Create the Transacao, which fails.
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        restTransacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoTransacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transacao.setTipoTransacao(null);

        // Create the Transacao, which fails.
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        restTransacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSituacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transacao.setSituacao(null);

        // Create the Transacao, which fails.
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        restTransacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transacao.setAtiva(null);

        // Create the Transacao, which fails.
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        restTransacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransacaos() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList
        restTransacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].valorEnviado").value(hasItem(sameNumber(DEFAULT_VALOR_ENVIADO))))
            .andExpect(jsonPath("$.[*].valorRecebido").value(hasItem(sameNumber(DEFAULT_VALOR_RECEBIDO))))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].estornada").value(hasItem(DEFAULT_ESTORNADA)))
            .andExpect(jsonPath("$.[*].tipoTransacao").value(hasItem(DEFAULT_TIPO_TRANSACAO.toString())))
            .andExpect(jsonPath("$.[*].tipoPagamento").value(hasItem(DEFAULT_TIPO_PAGAMENTO.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].ativa").value(hasItem(DEFAULT_ATIVA)))
            .andExpect(jsonPath("$.[*].tipoEntidadeOrigem").value(hasItem(DEFAULT_TIPO_ENTIDADE_ORIGEM)))
            .andExpect(jsonPath("$.[*].idEntidadeOrigem").value(hasItem(DEFAULT_ID_ENTIDADE_ORIGEM.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransacaosWithEagerRelationshipsIsEnabled() throws Exception {
        when(transacaoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransacaoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transacaoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransacaosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(transacaoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransacaoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(transacaoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTransacao() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get the transacao
        restTransacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, transacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transacao.getId().intValue()))
            .andExpect(jsonPath("$.valorEnviado").value(sameNumber(DEFAULT_VALOR_ENVIADO)))
            .andExpect(jsonPath("$.valorRecebido").value(sameNumber(DEFAULT_VALOR_RECEBIDO)))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.estornada").value(DEFAULT_ESTORNADA))
            .andExpect(jsonPath("$.tipoTransacao").value(DEFAULT_TIPO_TRANSACAO.toString()))
            .andExpect(jsonPath("$.tipoPagamento").value(DEFAULT_TIPO_PAGAMENTO.toString()))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()))
            .andExpect(jsonPath("$.ativa").value(DEFAULT_ATIVA))
            .andExpect(jsonPath("$.tipoEntidadeOrigem").value(DEFAULT_TIPO_ENTIDADE_ORIGEM))
            .andExpect(jsonPath("$.idEntidadeOrigem").value(DEFAULT_ID_ENTIDADE_ORIGEM.intValue()));
    }

    @Test
    @Transactional
    void getTransacaosByIdFiltering() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        Long id = transacao.getId();

        defaultTransacaoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTransacaoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTransacaoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransacaosByValorEnviadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valorEnviado equals to
        defaultTransacaoFiltering("valorEnviado.equals=" + DEFAULT_VALOR_ENVIADO, "valorEnviado.equals=" + UPDATED_VALOR_ENVIADO);
    }

    @Test
    @Transactional
    void getAllTransacaosByValorEnviadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valorEnviado in
        defaultTransacaoFiltering(
            "valorEnviado.in=" + DEFAULT_VALOR_ENVIADO + "," + UPDATED_VALOR_ENVIADO,
            "valorEnviado.in=" + UPDATED_VALOR_ENVIADO
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByValorEnviadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valorEnviado is not null
        defaultTransacaoFiltering("valorEnviado.specified=true", "valorEnviado.specified=false");
    }

    @Test
    @Transactional
    void getAllTransacaosByValorEnviadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valorEnviado is greater than or equal to
        defaultTransacaoFiltering(
            "valorEnviado.greaterThanOrEqual=" + DEFAULT_VALOR_ENVIADO,
            "valorEnviado.greaterThanOrEqual=" + UPDATED_VALOR_ENVIADO
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByValorEnviadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valorEnviado is less than or equal to
        defaultTransacaoFiltering(
            "valorEnviado.lessThanOrEqual=" + DEFAULT_VALOR_ENVIADO,
            "valorEnviado.lessThanOrEqual=" + SMALLER_VALOR_ENVIADO
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByValorEnviadoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valorEnviado is less than
        defaultTransacaoFiltering("valorEnviado.lessThan=" + UPDATED_VALOR_ENVIADO, "valorEnviado.lessThan=" + DEFAULT_VALOR_ENVIADO);
    }

    @Test
    @Transactional
    void getAllTransacaosByValorEnviadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valorEnviado is greater than
        defaultTransacaoFiltering("valorEnviado.greaterThan=" + SMALLER_VALOR_ENVIADO, "valorEnviado.greaterThan=" + DEFAULT_VALOR_ENVIADO);
    }

    @Test
    @Transactional
    void getAllTransacaosByValorRecebidoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valorRecebido equals to
        defaultTransacaoFiltering("valorRecebido.equals=" + DEFAULT_VALOR_RECEBIDO, "valorRecebido.equals=" + UPDATED_VALOR_RECEBIDO);
    }

    @Test
    @Transactional
    void getAllTransacaosByValorRecebidoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valorRecebido in
        defaultTransacaoFiltering(
            "valorRecebido.in=" + DEFAULT_VALOR_RECEBIDO + "," + UPDATED_VALOR_RECEBIDO,
            "valorRecebido.in=" + UPDATED_VALOR_RECEBIDO
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByValorRecebidoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valorRecebido is not null
        defaultTransacaoFiltering("valorRecebido.specified=true", "valorRecebido.specified=false");
    }

    @Test
    @Transactional
    void getAllTransacaosByValorRecebidoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valorRecebido is greater than or equal to
        defaultTransacaoFiltering(
            "valorRecebido.greaterThanOrEqual=" + DEFAULT_VALOR_RECEBIDO,
            "valorRecebido.greaterThanOrEqual=" + UPDATED_VALOR_RECEBIDO
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByValorRecebidoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valorRecebido is less than or equal to
        defaultTransacaoFiltering(
            "valorRecebido.lessThanOrEqual=" + DEFAULT_VALOR_RECEBIDO,
            "valorRecebido.lessThanOrEqual=" + SMALLER_VALOR_RECEBIDO
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByValorRecebidoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valorRecebido is less than
        defaultTransacaoFiltering("valorRecebido.lessThan=" + UPDATED_VALOR_RECEBIDO, "valorRecebido.lessThan=" + DEFAULT_VALOR_RECEBIDO);
    }

    @Test
    @Transactional
    void getAllTransacaosByValorRecebidoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where valorRecebido is greater than
        defaultTransacaoFiltering(
            "valorRecebido.greaterThan=" + SMALLER_VALOR_RECEBIDO,
            "valorRecebido.greaterThan=" + DEFAULT_VALOR_RECEBIDO
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where descricao equals to
        defaultTransacaoFiltering("descricao.equals=" + DEFAULT_DESCRICAO, "descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTransacaosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where descricao in
        defaultTransacaoFiltering("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO, "descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTransacaosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where descricao is not null
        defaultTransacaoFiltering("descricao.specified=true", "descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllTransacaosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where descricao contains
        defaultTransacaoFiltering("descricao.contains=" + DEFAULT_DESCRICAO, "descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTransacaosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where descricao does not contain
        defaultTransacaoFiltering("descricao.doesNotContain=" + UPDATED_DESCRICAO, "descricao.doesNotContain=" + DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTransacaosByEstornadaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where estornada equals to
        defaultTransacaoFiltering("estornada.equals=" + DEFAULT_ESTORNADA, "estornada.equals=" + UPDATED_ESTORNADA);
    }

    @Test
    @Transactional
    void getAllTransacaosByEstornadaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where estornada in
        defaultTransacaoFiltering("estornada.in=" + DEFAULT_ESTORNADA + "," + UPDATED_ESTORNADA, "estornada.in=" + UPDATED_ESTORNADA);
    }

    @Test
    @Transactional
    void getAllTransacaosByEstornadaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where estornada is not null
        defaultTransacaoFiltering("estornada.specified=true", "estornada.specified=false");
    }

    @Test
    @Transactional
    void getAllTransacaosByTipoTransacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where tipoTransacao equals to
        defaultTransacaoFiltering("tipoTransacao.equals=" + DEFAULT_TIPO_TRANSACAO, "tipoTransacao.equals=" + UPDATED_TIPO_TRANSACAO);
    }

    @Test
    @Transactional
    void getAllTransacaosByTipoTransacaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where tipoTransacao in
        defaultTransacaoFiltering(
            "tipoTransacao.in=" + DEFAULT_TIPO_TRANSACAO + "," + UPDATED_TIPO_TRANSACAO,
            "tipoTransacao.in=" + UPDATED_TIPO_TRANSACAO
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByTipoTransacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where tipoTransacao is not null
        defaultTransacaoFiltering("tipoTransacao.specified=true", "tipoTransacao.specified=false");
    }

    @Test
    @Transactional
    void getAllTransacaosByTipoPagamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where tipoPagamento equals to
        defaultTransacaoFiltering("tipoPagamento.equals=" + DEFAULT_TIPO_PAGAMENTO, "tipoPagamento.equals=" + UPDATED_TIPO_PAGAMENTO);
    }

    @Test
    @Transactional
    void getAllTransacaosByTipoPagamentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where tipoPagamento in
        defaultTransacaoFiltering(
            "tipoPagamento.in=" + DEFAULT_TIPO_PAGAMENTO + "," + UPDATED_TIPO_PAGAMENTO,
            "tipoPagamento.in=" + UPDATED_TIPO_PAGAMENTO
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByTipoPagamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where tipoPagamento is not null
        defaultTransacaoFiltering("tipoPagamento.specified=true", "tipoPagamento.specified=false");
    }

    @Test
    @Transactional
    void getAllTransacaosBySituacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where situacao equals to
        defaultTransacaoFiltering("situacao.equals=" + DEFAULT_SITUACAO, "situacao.equals=" + UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    void getAllTransacaosBySituacaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where situacao in
        defaultTransacaoFiltering("situacao.in=" + DEFAULT_SITUACAO + "," + UPDATED_SITUACAO, "situacao.in=" + UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    void getAllTransacaosBySituacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where situacao is not null
        defaultTransacaoFiltering("situacao.specified=true", "situacao.specified=false");
    }

    @Test
    @Transactional
    void getAllTransacaosByAtivaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where ativa equals to
        defaultTransacaoFiltering("ativa.equals=" + DEFAULT_ATIVA, "ativa.equals=" + UPDATED_ATIVA);
    }

    @Test
    @Transactional
    void getAllTransacaosByAtivaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where ativa in
        defaultTransacaoFiltering("ativa.in=" + DEFAULT_ATIVA + "," + UPDATED_ATIVA, "ativa.in=" + UPDATED_ATIVA);
    }

    @Test
    @Transactional
    void getAllTransacaosByAtivaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where ativa is not null
        defaultTransacaoFiltering("ativa.specified=true", "ativa.specified=false");
    }

    @Test
    @Transactional
    void getAllTransacaosByTipoEntidadeOrigemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where tipoEntidadeOrigem equals to
        defaultTransacaoFiltering(
            "tipoEntidadeOrigem.equals=" + DEFAULT_TIPO_ENTIDADE_ORIGEM,
            "tipoEntidadeOrigem.equals=" + UPDATED_TIPO_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByTipoEntidadeOrigemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where tipoEntidadeOrigem in
        defaultTransacaoFiltering(
            "tipoEntidadeOrigem.in=" + DEFAULT_TIPO_ENTIDADE_ORIGEM + "," + UPDATED_TIPO_ENTIDADE_ORIGEM,
            "tipoEntidadeOrigem.in=" + UPDATED_TIPO_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByTipoEntidadeOrigemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where tipoEntidadeOrigem is not null
        defaultTransacaoFiltering("tipoEntidadeOrigem.specified=true", "tipoEntidadeOrigem.specified=false");
    }

    @Test
    @Transactional
    void getAllTransacaosByTipoEntidadeOrigemContainsSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where tipoEntidadeOrigem contains
        defaultTransacaoFiltering(
            "tipoEntidadeOrigem.contains=" + DEFAULT_TIPO_ENTIDADE_ORIGEM,
            "tipoEntidadeOrigem.contains=" + UPDATED_TIPO_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByTipoEntidadeOrigemNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where tipoEntidadeOrigem does not contain
        defaultTransacaoFiltering(
            "tipoEntidadeOrigem.doesNotContain=" + UPDATED_TIPO_ENTIDADE_ORIGEM,
            "tipoEntidadeOrigem.doesNotContain=" + DEFAULT_TIPO_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByIdEntidadeOrigemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where idEntidadeOrigem equals to
        defaultTransacaoFiltering(
            "idEntidadeOrigem.equals=" + DEFAULT_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.equals=" + UPDATED_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByIdEntidadeOrigemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where idEntidadeOrigem in
        defaultTransacaoFiltering(
            "idEntidadeOrigem.in=" + DEFAULT_ID_ENTIDADE_ORIGEM + "," + UPDATED_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.in=" + UPDATED_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByIdEntidadeOrigemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where idEntidadeOrigem is not null
        defaultTransacaoFiltering("idEntidadeOrigem.specified=true", "idEntidadeOrigem.specified=false");
    }

    @Test
    @Transactional
    void getAllTransacaosByIdEntidadeOrigemIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where idEntidadeOrigem is greater than or equal to
        defaultTransacaoFiltering(
            "idEntidadeOrigem.greaterThanOrEqual=" + DEFAULT_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.greaterThanOrEqual=" + UPDATED_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByIdEntidadeOrigemIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where idEntidadeOrigem is less than or equal to
        defaultTransacaoFiltering(
            "idEntidadeOrigem.lessThanOrEqual=" + DEFAULT_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.lessThanOrEqual=" + SMALLER_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByIdEntidadeOrigemIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where idEntidadeOrigem is less than
        defaultTransacaoFiltering(
            "idEntidadeOrigem.lessThan=" + UPDATED_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.lessThan=" + DEFAULT_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByIdEntidadeOrigemIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        // Get all the transacaoList where idEntidadeOrigem is greater than
        defaultTransacaoFiltering(
            "idEntidadeOrigem.greaterThan=" + SMALLER_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.greaterThan=" + DEFAULT_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllTransacaosByCarteiraOrigemIsEqualToSomething() throws Exception {
        Carteira carteiraOrigem;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            transacaoRepository.saveAndFlush(transacao);
            carteiraOrigem = CarteiraResourceIT.createEntity(em);
        } else {
            carteiraOrigem = TestUtil.findAll(em, Carteira.class).get(0);
        }
        em.persist(carteiraOrigem);
        em.flush();
        transacao.setCarteiraOrigem(carteiraOrigem);
        transacaoRepository.saveAndFlush(transacao);
        Long carteiraOrigemId = carteiraOrigem.getId();
        // Get all the transacaoList where carteiraOrigem equals to carteiraOrigemId
        defaultTransacaoShouldBeFound("carteiraOrigemId.equals=" + carteiraOrigemId);

        // Get all the transacaoList where carteiraOrigem equals to (carteiraOrigemId + 1)
        defaultTransacaoShouldNotBeFound("carteiraOrigemId.equals=" + (carteiraOrigemId + 1));
    }

    @Test
    @Transactional
    void getAllTransacaosByCarteiraDestinoIsEqualToSomething() throws Exception {
        Carteira carteiraDestino;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            transacaoRepository.saveAndFlush(transacao);
            carteiraDestino = CarteiraResourceIT.createEntity(em);
        } else {
            carteiraDestino = TestUtil.findAll(em, Carteira.class).get(0);
        }
        em.persist(carteiraDestino);
        em.flush();
        transacao.setCarteiraDestino(carteiraDestino);
        transacaoRepository.saveAndFlush(transacao);
        Long carteiraDestinoId = carteiraDestino.getId();
        // Get all the transacaoList where carteiraDestino equals to carteiraDestinoId
        defaultTransacaoShouldBeFound("carteiraDestinoId.equals=" + carteiraDestinoId);

        // Get all the transacaoList where carteiraDestino equals to (carteiraDestinoId + 1)
        defaultTransacaoShouldNotBeFound("carteiraDestinoId.equals=" + (carteiraDestinoId + 1));
    }

    @Test
    @Transactional
    void getAllTransacaosByMoedaOrigemIsEqualToSomething() throws Exception {
        MoedaCarteira moedaOrigem;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            transacaoRepository.saveAndFlush(transacao);
            moedaOrigem = MoedaCarteiraResourceIT.createEntity(em);
        } else {
            moedaOrigem = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        em.persist(moedaOrigem);
        em.flush();
        transacao.setMoedaOrigem(moedaOrigem);
        transacaoRepository.saveAndFlush(transacao);
        Long moedaOrigemId = moedaOrigem.getId();
        // Get all the transacaoList where moedaOrigem equals to moedaOrigemId
        defaultTransacaoShouldBeFound("moedaOrigemId.equals=" + moedaOrigemId);

        // Get all the transacaoList where moedaOrigem equals to (moedaOrigemId + 1)
        defaultTransacaoShouldNotBeFound("moedaOrigemId.equals=" + (moedaOrigemId + 1));
    }

    @Test
    @Transactional
    void getAllTransacaosByMoedaDestinoIsEqualToSomething() throws Exception {
        MoedaCarteira moedaDestino;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            transacaoRepository.saveAndFlush(transacao);
            moedaDestino = MoedaCarteiraResourceIT.createEntity(em);
        } else {
            moedaDestino = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        em.persist(moedaDestino);
        em.flush();
        transacao.setMoedaDestino(moedaDestino);
        transacaoRepository.saveAndFlush(transacao);
        Long moedaDestinoId = moedaDestino.getId();
        // Get all the transacaoList where moedaDestino equals to moedaDestinoId
        defaultTransacaoShouldBeFound("moedaDestinoId.equals=" + moedaDestinoId);

        // Get all the transacaoList where moedaDestino equals to (moedaDestinoId + 1)
        defaultTransacaoShouldNotBeFound("moedaDestinoId.equals=" + (moedaDestinoId + 1));
    }

    private void defaultTransacaoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTransacaoShouldBeFound(shouldBeFound);
        defaultTransacaoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransacaoShouldBeFound(String filter) throws Exception {
        restTransacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].valorEnviado").value(hasItem(sameNumber(DEFAULT_VALOR_ENVIADO))))
            .andExpect(jsonPath("$.[*].valorRecebido").value(hasItem(sameNumber(DEFAULT_VALOR_RECEBIDO))))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].estornada").value(hasItem(DEFAULT_ESTORNADA)))
            .andExpect(jsonPath("$.[*].tipoTransacao").value(hasItem(DEFAULT_TIPO_TRANSACAO.toString())))
            .andExpect(jsonPath("$.[*].tipoPagamento").value(hasItem(DEFAULT_TIPO_PAGAMENTO.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].ativa").value(hasItem(DEFAULT_ATIVA)))
            .andExpect(jsonPath("$.[*].tipoEntidadeOrigem").value(hasItem(DEFAULT_TIPO_ENTIDADE_ORIGEM)))
            .andExpect(jsonPath("$.[*].idEntidadeOrigem").value(hasItem(DEFAULT_ID_ENTIDADE_ORIGEM.intValue())));

        // Check, that the count call also returns 1
        restTransacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransacaoShouldNotBeFound(String filter) throws Exception {
        restTransacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransacao() throws Exception {
        // Get the transacao
        restTransacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransacao() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transacao
        Transacao updatedTransacao = transacaoRepository.findById(transacao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransacao are not directly saved in db
        em.detach(updatedTransacao);
        updatedTransacao
            .valorEnviado(UPDATED_VALOR_ENVIADO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO)
            .descricao(UPDATED_DESCRICAO)
            .estornada(UPDATED_ESTORNADA)
            .tipoTransacao(UPDATED_TIPO_TRANSACAO)
            .tipoPagamento(UPDATED_TIPO_PAGAMENTO)
            .situacao(UPDATED_SITUACAO)
            .ativa(UPDATED_ATIVA)
            .tipoEntidadeOrigem(UPDATED_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(UPDATED_ID_ENTIDADE_ORIGEM);
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(updatedTransacao);

        restTransacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransacaoToMatchAllProperties(updatedTransacao);
    }

    @Test
    @Transactional
    void putNonExistingTransacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transacao.setId(longCount.incrementAndGet());

        // Create the Transacao
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transacao.setId(longCount.incrementAndGet());

        // Create the Transacao
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transacao.setId(longCount.incrementAndGet());

        // Create the Transacao
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransacaoWithPatch() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transacao using partial update
        Transacao partialUpdatedTransacao = new Transacao();
        partialUpdatedTransacao.setId(transacao.getId());

        partialUpdatedTransacao
            .valorEnviado(UPDATED_VALOR_ENVIADO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO)
            .estornada(UPDATED_ESTORNADA)
            .situacao(UPDATED_SITUACAO)
            .tipoEntidadeOrigem(UPDATED_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(UPDATED_ID_ENTIDADE_ORIGEM);

        restTransacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransacao))
            )
            .andExpect(status().isOk());

        // Validate the Transacao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransacaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransacao, transacao),
            getPersistedTransacao(transacao)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransacaoWithPatch() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transacao using partial update
        Transacao partialUpdatedTransacao = new Transacao();
        partialUpdatedTransacao.setId(transacao.getId());

        partialUpdatedTransacao
            .valorEnviado(UPDATED_VALOR_ENVIADO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO)
            .descricao(UPDATED_DESCRICAO)
            .estornada(UPDATED_ESTORNADA)
            .tipoTransacao(UPDATED_TIPO_TRANSACAO)
            .tipoPagamento(UPDATED_TIPO_PAGAMENTO)
            .situacao(UPDATED_SITUACAO)
            .ativa(UPDATED_ATIVA)
            .tipoEntidadeOrigem(UPDATED_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(UPDATED_ID_ENTIDADE_ORIGEM);

        restTransacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransacao))
            )
            .andExpect(status().isOk());

        // Validate the Transacao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransacaoUpdatableFieldsEquals(partialUpdatedTransacao, getPersistedTransacao(partialUpdatedTransacao));
    }

    @Test
    @Transactional
    void patchNonExistingTransacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transacao.setId(longCount.incrementAndGet());

        // Create the Transacao
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transacao.setId(longCount.incrementAndGet());

        // Create the Transacao
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transacao.setId(longCount.incrementAndGet());

        // Create the Transacao
        TransacaoDTO transacaoDTO = transacaoMapper.toDto(transacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransacaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransacao() throws Exception {
        // Initialize the database
        insertedTransacao = transacaoRepository.saveAndFlush(transacao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transacao
        restTransacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, transacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transacaoRepository.count();
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

    protected Transacao getPersistedTransacao(Transacao transacao) {
        return transacaoRepository.findById(transacao.getId()).orElseThrow();
    }

    protected void assertPersistedTransacaoToMatchAllProperties(Transacao expectedTransacao) {
        assertTransacaoAllPropertiesEquals(expectedTransacao, getPersistedTransacao(expectedTransacao));
    }

    protected void assertPersistedTransacaoToMatchUpdatableProperties(Transacao expectedTransacao) {
        assertTransacaoAllUpdatablePropertiesEquals(expectedTransacao, getPersistedTransacao(expectedTransacao));
    }
}
