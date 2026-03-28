package com.bankernel.web.rest;

import static com.bankernel.domain.HistoricoOperacaoAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bankernel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Carteira;
import com.bankernel.domain.HistoricoOperacao;
import com.bankernel.domain.Transacao;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumStatusHistorico;
import com.bankernel.domain.enumeration.EnumTipoHistorico;
import com.bankernel.domain.enumeration.EnumTipoSimbolo;
import com.bankernel.repository.HistoricoOperacaoRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.HistoricoOperacaoService;
import com.bankernel.service.dto.HistoricoOperacaoDTO;
import com.bankernel.service.mapper.HistoricoOperacaoMapper;
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
 * Integration tests for the {@link HistoricoOperacaoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class HistoricoOperacaoResourceIT {

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_SALDO_APOS = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALDO_APOS = new BigDecimal(2);
    private static final BigDecimal SMALLER_SALDO_APOS = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final EnumTipoSimbolo DEFAULT_TIPO_SIMBOLO = EnumTipoSimbolo.ENTRADA;
    private static final EnumTipoSimbolo UPDATED_TIPO_SIMBOLO = EnumTipoSimbolo.SAIDA;

    private static final String DEFAULT_NUMERO_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_REFERENCIA = "BBBBBBBBBB";

    private static final EnumTipoHistorico DEFAULT_TIPO_HISTORICO = EnumTipoHistorico.DEPOSITO;
    private static final EnumTipoHistorico UPDATED_TIPO_HISTORICO = EnumTipoHistorico.SAQUE;

    private static final EnumStatusHistorico DEFAULT_SITUACAO_HISTORICO = EnumStatusHistorico.PENDENTE;
    private static final EnumStatusHistorico UPDATED_SITUACAO_HISTORICO = EnumStatusHistorico.CONFIRMADO;

    private static final String DEFAULT_TIPO_ENTIDADE_ORIGEM = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_ENTIDADE_ORIGEM = "BBBBBBBBBB";

    private static final Long DEFAULT_ID_ENTIDADE_ORIGEM = 1L;
    private static final Long UPDATED_ID_ENTIDADE_ORIGEM = 2L;
    private static final Long SMALLER_ID_ENTIDADE_ORIGEM = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/historico-operacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HistoricoOperacaoRepository historicoOperacaoRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private HistoricoOperacaoRepository historicoOperacaoRepositoryMock;

    @Autowired
    private HistoricoOperacaoMapper historicoOperacaoMapper;

    @Mock
    private HistoricoOperacaoService historicoOperacaoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHistoricoOperacaoMockMvc;

    private HistoricoOperacao historicoOperacao;

    private HistoricoOperacao insertedHistoricoOperacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoricoOperacao createEntity(EntityManager em) {
        HistoricoOperacao historicoOperacao = new HistoricoOperacao()
            .valor(DEFAULT_VALOR)
            .saldoApos(DEFAULT_SALDO_APOS)
            .descricao(DEFAULT_DESCRICAO)
            .tipoSimbolo(DEFAULT_TIPO_SIMBOLO)
            .numeroReferencia(DEFAULT_NUMERO_REFERENCIA)
            .tipoHistorico(DEFAULT_TIPO_HISTORICO)
            .situacaoHistorico(DEFAULT_SITUACAO_HISTORICO)
            .tipoEntidadeOrigem(DEFAULT_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(DEFAULT_ID_ENTIDADE_ORIGEM);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        historicoOperacao.setUsuario(user);
        // Add required entity
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            carteira = CarteiraResourceIT.createEntity(em);
            em.persist(carteira);
            em.flush();
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        historicoOperacao.setCarteira(carteira);
        return historicoOperacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HistoricoOperacao createUpdatedEntity(EntityManager em) {
        HistoricoOperacao updatedHistoricoOperacao = new HistoricoOperacao()
            .valor(UPDATED_VALOR)
            .saldoApos(UPDATED_SALDO_APOS)
            .descricao(UPDATED_DESCRICAO)
            .tipoSimbolo(UPDATED_TIPO_SIMBOLO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .tipoHistorico(UPDATED_TIPO_HISTORICO)
            .situacaoHistorico(UPDATED_SITUACAO_HISTORICO)
            .tipoEntidadeOrigem(UPDATED_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(UPDATED_ID_ENTIDADE_ORIGEM);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedHistoricoOperacao.setUsuario(user);
        // Add required entity
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            carteira = CarteiraResourceIT.createUpdatedEntity(em);
            em.persist(carteira);
            em.flush();
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        updatedHistoricoOperacao.setCarteira(carteira);
        return updatedHistoricoOperacao;
    }

    @BeforeEach
    void initTest() {
        historicoOperacao = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedHistoricoOperacao != null) {
            historicoOperacaoRepository.delete(insertedHistoricoOperacao);
            insertedHistoricoOperacao = null;
        }
    }

    @Test
    @Transactional
    void createHistoricoOperacao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HistoricoOperacao
        HistoricoOperacaoDTO historicoOperacaoDTO = historicoOperacaoMapper.toDto(historicoOperacao);
        var returnedHistoricoOperacaoDTO = om.readValue(
            restHistoricoOperacaoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historicoOperacaoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HistoricoOperacaoDTO.class
        );

        // Validate the HistoricoOperacao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHistoricoOperacao = historicoOperacaoMapper.toEntity(returnedHistoricoOperacaoDTO);
        assertHistoricoOperacaoUpdatableFieldsEquals(returnedHistoricoOperacao, getPersistedHistoricoOperacao(returnedHistoricoOperacao));

        insertedHistoricoOperacao = returnedHistoricoOperacao;
    }

    @Test
    @Transactional
    void createHistoricoOperacaoWithExistingId() throws Exception {
        // Create the HistoricoOperacao with an existing ID
        historicoOperacao.setId(1L);
        HistoricoOperacaoDTO historicoOperacaoDTO = historicoOperacaoMapper.toDto(historicoOperacao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHistoricoOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historicoOperacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HistoricoOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historicoOperacao.setValor(null);

        // Create the HistoricoOperacao, which fails.
        HistoricoOperacaoDTO historicoOperacaoDTO = historicoOperacaoMapper.toDto(historicoOperacao);

        restHistoricoOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historicoOperacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSaldoAposIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historicoOperacao.setSaldoApos(null);

        // Create the HistoricoOperacao, which fails.
        HistoricoOperacaoDTO historicoOperacaoDTO = historicoOperacaoMapper.toDto(historicoOperacao);

        restHistoricoOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historicoOperacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoSimboloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historicoOperacao.setTipoSimbolo(null);

        // Create the HistoricoOperacao, which fails.
        HistoricoOperacaoDTO historicoOperacaoDTO = historicoOperacaoMapper.toDto(historicoOperacao);

        restHistoricoOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historicoOperacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoHistoricoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historicoOperacao.setTipoHistorico(null);

        // Create the HistoricoOperacao, which fails.
        HistoricoOperacaoDTO historicoOperacaoDTO = historicoOperacaoMapper.toDto(historicoOperacao);

        restHistoricoOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historicoOperacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSituacaoHistoricoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        historicoOperacao.setSituacaoHistorico(null);

        // Create the HistoricoOperacao, which fails.
        HistoricoOperacaoDTO historicoOperacaoDTO = historicoOperacaoMapper.toDto(historicoOperacao);

        restHistoricoOperacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historicoOperacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaos() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList
        restHistoricoOperacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historicoOperacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].saldoApos").value(hasItem(sameNumber(DEFAULT_SALDO_APOS))))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].tipoSimbolo").value(hasItem(DEFAULT_TIPO_SIMBOLO.toString())))
            .andExpect(jsonPath("$.[*].numeroReferencia").value(hasItem(DEFAULT_NUMERO_REFERENCIA)))
            .andExpect(jsonPath("$.[*].tipoHistorico").value(hasItem(DEFAULT_TIPO_HISTORICO.toString())))
            .andExpect(jsonPath("$.[*].situacaoHistorico").value(hasItem(DEFAULT_SITUACAO_HISTORICO.toString())))
            .andExpect(jsonPath("$.[*].tipoEntidadeOrigem").value(hasItem(DEFAULT_TIPO_ENTIDADE_ORIGEM)))
            .andExpect(jsonPath("$.[*].idEntidadeOrigem").value(hasItem(DEFAULT_ID_ENTIDADE_ORIGEM.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHistoricoOperacaosWithEagerRelationshipsIsEnabled() throws Exception {
        when(historicoOperacaoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHistoricoOperacaoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(historicoOperacaoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllHistoricoOperacaosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(historicoOperacaoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restHistoricoOperacaoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(historicoOperacaoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getHistoricoOperacao() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get the historicoOperacao
        restHistoricoOperacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, historicoOperacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(historicoOperacao.getId().intValue()))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.saldoApos").value(sameNumber(DEFAULT_SALDO_APOS)))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.tipoSimbolo").value(DEFAULT_TIPO_SIMBOLO.toString()))
            .andExpect(jsonPath("$.numeroReferencia").value(DEFAULT_NUMERO_REFERENCIA))
            .andExpect(jsonPath("$.tipoHistorico").value(DEFAULT_TIPO_HISTORICO.toString()))
            .andExpect(jsonPath("$.situacaoHistorico").value(DEFAULT_SITUACAO_HISTORICO.toString()))
            .andExpect(jsonPath("$.tipoEntidadeOrigem").value(DEFAULT_TIPO_ENTIDADE_ORIGEM))
            .andExpect(jsonPath("$.idEntidadeOrigem").value(DEFAULT_ID_ENTIDADE_ORIGEM.intValue()));
    }

    @Test
    @Transactional
    void getHistoricoOperacaosByIdFiltering() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        Long id = historicoOperacao.getId();

        defaultHistoricoOperacaoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHistoricoOperacaoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHistoricoOperacaoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where valor equals to
        defaultHistoricoOperacaoFiltering("valor.equals=" + DEFAULT_VALOR, "valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByValorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where valor in
        defaultHistoricoOperacaoFiltering("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR, "valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where valor is not null
        defaultHistoricoOperacaoFiltering("valor.specified=true", "valor.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByValorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where valor is greater than or equal to
        defaultHistoricoOperacaoFiltering("valor.greaterThanOrEqual=" + DEFAULT_VALOR, "valor.greaterThanOrEqual=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByValorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where valor is less than or equal to
        defaultHistoricoOperacaoFiltering("valor.lessThanOrEqual=" + DEFAULT_VALOR, "valor.lessThanOrEqual=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByValorIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where valor is less than
        defaultHistoricoOperacaoFiltering("valor.lessThan=" + UPDATED_VALOR, "valor.lessThan=" + DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByValorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where valor is greater than
        defaultHistoricoOperacaoFiltering("valor.greaterThan=" + SMALLER_VALOR, "valor.greaterThan=" + DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosBySaldoAposIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where saldoApos equals to
        defaultHistoricoOperacaoFiltering("saldoApos.equals=" + DEFAULT_SALDO_APOS, "saldoApos.equals=" + UPDATED_SALDO_APOS);
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosBySaldoAposIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where saldoApos in
        defaultHistoricoOperacaoFiltering(
            "saldoApos.in=" + DEFAULT_SALDO_APOS + "," + UPDATED_SALDO_APOS,
            "saldoApos.in=" + UPDATED_SALDO_APOS
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosBySaldoAposIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where saldoApos is not null
        defaultHistoricoOperacaoFiltering("saldoApos.specified=true", "saldoApos.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosBySaldoAposIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where saldoApos is greater than or equal to
        defaultHistoricoOperacaoFiltering(
            "saldoApos.greaterThanOrEqual=" + DEFAULT_SALDO_APOS,
            "saldoApos.greaterThanOrEqual=" + UPDATED_SALDO_APOS
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosBySaldoAposIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where saldoApos is less than or equal to
        defaultHistoricoOperacaoFiltering(
            "saldoApos.lessThanOrEqual=" + DEFAULT_SALDO_APOS,
            "saldoApos.lessThanOrEqual=" + SMALLER_SALDO_APOS
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosBySaldoAposIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where saldoApos is less than
        defaultHistoricoOperacaoFiltering("saldoApos.lessThan=" + UPDATED_SALDO_APOS, "saldoApos.lessThan=" + DEFAULT_SALDO_APOS);
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosBySaldoAposIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where saldoApos is greater than
        defaultHistoricoOperacaoFiltering("saldoApos.greaterThan=" + SMALLER_SALDO_APOS, "saldoApos.greaterThan=" + DEFAULT_SALDO_APOS);
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where descricao equals to
        defaultHistoricoOperacaoFiltering("descricao.equals=" + DEFAULT_DESCRICAO, "descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where descricao in
        defaultHistoricoOperacaoFiltering(
            "descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO,
            "descricao.in=" + UPDATED_DESCRICAO
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where descricao is not null
        defaultHistoricoOperacaoFiltering("descricao.specified=true", "descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where descricao contains
        defaultHistoricoOperacaoFiltering("descricao.contains=" + DEFAULT_DESCRICAO, "descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where descricao does not contain
        defaultHistoricoOperacaoFiltering("descricao.doesNotContain=" + UPDATED_DESCRICAO, "descricao.doesNotContain=" + DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByTipoSimboloIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where tipoSimbolo equals to
        defaultHistoricoOperacaoFiltering("tipoSimbolo.equals=" + DEFAULT_TIPO_SIMBOLO, "tipoSimbolo.equals=" + UPDATED_TIPO_SIMBOLO);
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByTipoSimboloIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where tipoSimbolo in
        defaultHistoricoOperacaoFiltering(
            "tipoSimbolo.in=" + DEFAULT_TIPO_SIMBOLO + "," + UPDATED_TIPO_SIMBOLO,
            "tipoSimbolo.in=" + UPDATED_TIPO_SIMBOLO
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByTipoSimboloIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where tipoSimbolo is not null
        defaultHistoricoOperacaoFiltering("tipoSimbolo.specified=true", "tipoSimbolo.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByNumeroReferenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where numeroReferencia equals to
        defaultHistoricoOperacaoFiltering(
            "numeroReferencia.equals=" + DEFAULT_NUMERO_REFERENCIA,
            "numeroReferencia.equals=" + UPDATED_NUMERO_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByNumeroReferenciaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where numeroReferencia in
        defaultHistoricoOperacaoFiltering(
            "numeroReferencia.in=" + DEFAULT_NUMERO_REFERENCIA + "," + UPDATED_NUMERO_REFERENCIA,
            "numeroReferencia.in=" + UPDATED_NUMERO_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByNumeroReferenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where numeroReferencia is not null
        defaultHistoricoOperacaoFiltering("numeroReferencia.specified=true", "numeroReferencia.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByNumeroReferenciaContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where numeroReferencia contains
        defaultHistoricoOperacaoFiltering(
            "numeroReferencia.contains=" + DEFAULT_NUMERO_REFERENCIA,
            "numeroReferencia.contains=" + UPDATED_NUMERO_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByNumeroReferenciaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where numeroReferencia does not contain
        defaultHistoricoOperacaoFiltering(
            "numeroReferencia.doesNotContain=" + UPDATED_NUMERO_REFERENCIA,
            "numeroReferencia.doesNotContain=" + DEFAULT_NUMERO_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByTipoHistoricoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where tipoHistorico equals to
        defaultHistoricoOperacaoFiltering(
            "tipoHistorico.equals=" + DEFAULT_TIPO_HISTORICO,
            "tipoHistorico.equals=" + UPDATED_TIPO_HISTORICO
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByTipoHistoricoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where tipoHistorico in
        defaultHistoricoOperacaoFiltering(
            "tipoHistorico.in=" + DEFAULT_TIPO_HISTORICO + "," + UPDATED_TIPO_HISTORICO,
            "tipoHistorico.in=" + UPDATED_TIPO_HISTORICO
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByTipoHistoricoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where tipoHistorico is not null
        defaultHistoricoOperacaoFiltering("tipoHistorico.specified=true", "tipoHistorico.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosBySituacaoHistoricoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where situacaoHistorico equals to
        defaultHistoricoOperacaoFiltering(
            "situacaoHistorico.equals=" + DEFAULT_SITUACAO_HISTORICO,
            "situacaoHistorico.equals=" + UPDATED_SITUACAO_HISTORICO
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosBySituacaoHistoricoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where situacaoHistorico in
        defaultHistoricoOperacaoFiltering(
            "situacaoHistorico.in=" + DEFAULT_SITUACAO_HISTORICO + "," + UPDATED_SITUACAO_HISTORICO,
            "situacaoHistorico.in=" + UPDATED_SITUACAO_HISTORICO
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosBySituacaoHistoricoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where situacaoHistorico is not null
        defaultHistoricoOperacaoFiltering("situacaoHistorico.specified=true", "situacaoHistorico.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByTipoEntidadeOrigemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where tipoEntidadeOrigem equals to
        defaultHistoricoOperacaoFiltering(
            "tipoEntidadeOrigem.equals=" + DEFAULT_TIPO_ENTIDADE_ORIGEM,
            "tipoEntidadeOrigem.equals=" + UPDATED_TIPO_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByTipoEntidadeOrigemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where tipoEntidadeOrigem in
        defaultHistoricoOperacaoFiltering(
            "tipoEntidadeOrigem.in=" + DEFAULT_TIPO_ENTIDADE_ORIGEM + "," + UPDATED_TIPO_ENTIDADE_ORIGEM,
            "tipoEntidadeOrigem.in=" + UPDATED_TIPO_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByTipoEntidadeOrigemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where tipoEntidadeOrigem is not null
        defaultHistoricoOperacaoFiltering("tipoEntidadeOrigem.specified=true", "tipoEntidadeOrigem.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByTipoEntidadeOrigemContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where tipoEntidadeOrigem contains
        defaultHistoricoOperacaoFiltering(
            "tipoEntidadeOrigem.contains=" + DEFAULT_TIPO_ENTIDADE_ORIGEM,
            "tipoEntidadeOrigem.contains=" + UPDATED_TIPO_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByTipoEntidadeOrigemNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where tipoEntidadeOrigem does not contain
        defaultHistoricoOperacaoFiltering(
            "tipoEntidadeOrigem.doesNotContain=" + UPDATED_TIPO_ENTIDADE_ORIGEM,
            "tipoEntidadeOrigem.doesNotContain=" + DEFAULT_TIPO_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByIdEntidadeOrigemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where idEntidadeOrigem equals to
        defaultHistoricoOperacaoFiltering(
            "idEntidadeOrigem.equals=" + DEFAULT_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.equals=" + UPDATED_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByIdEntidadeOrigemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where idEntidadeOrigem in
        defaultHistoricoOperacaoFiltering(
            "idEntidadeOrigem.in=" + DEFAULT_ID_ENTIDADE_ORIGEM + "," + UPDATED_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.in=" + UPDATED_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByIdEntidadeOrigemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where idEntidadeOrigem is not null
        defaultHistoricoOperacaoFiltering("idEntidadeOrigem.specified=true", "idEntidadeOrigem.specified=false");
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByIdEntidadeOrigemIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where idEntidadeOrigem is greater than or equal to
        defaultHistoricoOperacaoFiltering(
            "idEntidadeOrigem.greaterThanOrEqual=" + DEFAULT_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.greaterThanOrEqual=" + UPDATED_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByIdEntidadeOrigemIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where idEntidadeOrigem is less than or equal to
        defaultHistoricoOperacaoFiltering(
            "idEntidadeOrigem.lessThanOrEqual=" + DEFAULT_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.lessThanOrEqual=" + SMALLER_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByIdEntidadeOrigemIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where idEntidadeOrigem is less than
        defaultHistoricoOperacaoFiltering(
            "idEntidadeOrigem.lessThan=" + UPDATED_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.lessThan=" + DEFAULT_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByIdEntidadeOrigemIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        // Get all the historicoOperacaoList where idEntidadeOrigem is greater than
        defaultHistoricoOperacaoFiltering(
            "idEntidadeOrigem.greaterThan=" + SMALLER_ID_ENTIDADE_ORIGEM,
            "idEntidadeOrigem.greaterThan=" + DEFAULT_ID_ENTIDADE_ORIGEM
        );
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByTransacaoIsEqualToSomething() throws Exception {
        Transacao transacao;
        if (TestUtil.findAll(em, Transacao.class).isEmpty()) {
            historicoOperacaoRepository.saveAndFlush(historicoOperacao);
            transacao = TransacaoResourceIT.createEntity();
        } else {
            transacao = TestUtil.findAll(em, Transacao.class).get(0);
        }
        em.persist(transacao);
        em.flush();
        historicoOperacao.setTransacao(transacao);
        historicoOperacaoRepository.saveAndFlush(historicoOperacao);
        Long transacaoId = transacao.getId();
        // Get all the historicoOperacaoList where transacao equals to transacaoId
        defaultHistoricoOperacaoShouldBeFound("transacaoId.equals=" + transacaoId);

        // Get all the historicoOperacaoList where transacao equals to (transacaoId + 1)
        defaultHistoricoOperacaoShouldNotBeFound("transacaoId.equals=" + (transacaoId + 1));
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByUsuarioIsEqualToSomething() throws Exception {
        User usuario;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            historicoOperacaoRepository.saveAndFlush(historicoOperacao);
            usuario = UserResourceIT.createEntity();
        } else {
            usuario = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        historicoOperacao.setUsuario(usuario);
        historicoOperacaoRepository.saveAndFlush(historicoOperacao);
        Long usuarioId = usuario.getId();
        // Get all the historicoOperacaoList where usuario equals to usuarioId
        defaultHistoricoOperacaoShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the historicoOperacaoList where usuario equals to (usuarioId + 1)
        defaultHistoricoOperacaoShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    @Test
    @Transactional
    void getAllHistoricoOperacaosByCarteiraIsEqualToSomething() throws Exception {
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            historicoOperacaoRepository.saveAndFlush(historicoOperacao);
            carteira = CarteiraResourceIT.createEntity(em);
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        em.persist(carteira);
        em.flush();
        historicoOperacao.setCarteira(carteira);
        historicoOperacaoRepository.saveAndFlush(historicoOperacao);
        Long carteiraId = carteira.getId();
        // Get all the historicoOperacaoList where carteira equals to carteiraId
        defaultHistoricoOperacaoShouldBeFound("carteiraId.equals=" + carteiraId);

        // Get all the historicoOperacaoList where carteira equals to (carteiraId + 1)
        defaultHistoricoOperacaoShouldNotBeFound("carteiraId.equals=" + (carteiraId + 1));
    }

    private void defaultHistoricoOperacaoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHistoricoOperacaoShouldBeFound(shouldBeFound);
        defaultHistoricoOperacaoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHistoricoOperacaoShouldBeFound(String filter) throws Exception {
        restHistoricoOperacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(historicoOperacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].saldoApos").value(hasItem(sameNumber(DEFAULT_SALDO_APOS))))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].tipoSimbolo").value(hasItem(DEFAULT_TIPO_SIMBOLO.toString())))
            .andExpect(jsonPath("$.[*].numeroReferencia").value(hasItem(DEFAULT_NUMERO_REFERENCIA)))
            .andExpect(jsonPath("$.[*].tipoHistorico").value(hasItem(DEFAULT_TIPO_HISTORICO.toString())))
            .andExpect(jsonPath("$.[*].situacaoHistorico").value(hasItem(DEFAULT_SITUACAO_HISTORICO.toString())))
            .andExpect(jsonPath("$.[*].tipoEntidadeOrigem").value(hasItem(DEFAULT_TIPO_ENTIDADE_ORIGEM)))
            .andExpect(jsonPath("$.[*].idEntidadeOrigem").value(hasItem(DEFAULT_ID_ENTIDADE_ORIGEM.intValue())));

        // Check, that the count call also returns 1
        restHistoricoOperacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHistoricoOperacaoShouldNotBeFound(String filter) throws Exception {
        restHistoricoOperacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHistoricoOperacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHistoricoOperacao() throws Exception {
        // Get the historicoOperacao
        restHistoricoOperacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHistoricoOperacao() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historicoOperacao
        HistoricoOperacao updatedHistoricoOperacao = historicoOperacaoRepository.findById(historicoOperacao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHistoricoOperacao are not directly saved in db
        em.detach(updatedHistoricoOperacao);
        updatedHistoricoOperacao
            .valor(UPDATED_VALOR)
            .saldoApos(UPDATED_SALDO_APOS)
            .descricao(UPDATED_DESCRICAO)
            .tipoSimbolo(UPDATED_TIPO_SIMBOLO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .tipoHistorico(UPDATED_TIPO_HISTORICO)
            .situacaoHistorico(UPDATED_SITUACAO_HISTORICO)
            .tipoEntidadeOrigem(UPDATED_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(UPDATED_ID_ENTIDADE_ORIGEM);
        HistoricoOperacaoDTO historicoOperacaoDTO = historicoOperacaoMapper.toDto(updatedHistoricoOperacao);

        restHistoricoOperacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historicoOperacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historicoOperacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the HistoricoOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHistoricoOperacaoToMatchAllProperties(updatedHistoricoOperacao);
    }

    @Test
    @Transactional
    void putNonExistingHistoricoOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historicoOperacao.setId(longCount.incrementAndGet());

        // Create the HistoricoOperacao
        HistoricoOperacaoDTO historicoOperacaoDTO = historicoOperacaoMapper.toDto(historicoOperacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoricoOperacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, historicoOperacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historicoOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoricoOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHistoricoOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historicoOperacao.setId(longCount.incrementAndGet());

        // Create the HistoricoOperacao
        HistoricoOperacaoDTO historicoOperacaoDTO = historicoOperacaoMapper.toDto(historicoOperacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoricoOperacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(historicoOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoricoOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHistoricoOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historicoOperacao.setId(longCount.incrementAndGet());

        // Create the HistoricoOperacao
        HistoricoOperacaoDTO historicoOperacaoDTO = historicoOperacaoMapper.toDto(historicoOperacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoricoOperacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(historicoOperacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoricoOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHistoricoOperacaoWithPatch() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historicoOperacao using partial update
        HistoricoOperacao partialUpdatedHistoricoOperacao = new HistoricoOperacao();
        partialUpdatedHistoricoOperacao.setId(historicoOperacao.getId());

        partialUpdatedHistoricoOperacao
            .valor(UPDATED_VALOR)
            .saldoApos(UPDATED_SALDO_APOS)
            .tipoSimbolo(UPDATED_TIPO_SIMBOLO)
            .tipoHistorico(UPDATED_TIPO_HISTORICO)
            .situacaoHistorico(UPDATED_SITUACAO_HISTORICO)
            .idEntidadeOrigem(UPDATED_ID_ENTIDADE_ORIGEM);

        restHistoricoOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoricoOperacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoricoOperacao))
            )
            .andExpect(status().isOk());

        // Validate the HistoricoOperacao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoricoOperacaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHistoricoOperacao, historicoOperacao),
            getPersistedHistoricoOperacao(historicoOperacao)
        );
    }

    @Test
    @Transactional
    void fullUpdateHistoricoOperacaoWithPatch() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the historicoOperacao using partial update
        HistoricoOperacao partialUpdatedHistoricoOperacao = new HistoricoOperacao();
        partialUpdatedHistoricoOperacao.setId(historicoOperacao.getId());

        partialUpdatedHistoricoOperacao
            .valor(UPDATED_VALOR)
            .saldoApos(UPDATED_SALDO_APOS)
            .descricao(UPDATED_DESCRICAO)
            .tipoSimbolo(UPDATED_TIPO_SIMBOLO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .tipoHistorico(UPDATED_TIPO_HISTORICO)
            .situacaoHistorico(UPDATED_SITUACAO_HISTORICO)
            .tipoEntidadeOrigem(UPDATED_TIPO_ENTIDADE_ORIGEM)
            .idEntidadeOrigem(UPDATED_ID_ENTIDADE_ORIGEM);

        restHistoricoOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHistoricoOperacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHistoricoOperacao))
            )
            .andExpect(status().isOk());

        // Validate the HistoricoOperacao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHistoricoOperacaoUpdatableFieldsEquals(
            partialUpdatedHistoricoOperacao,
            getPersistedHistoricoOperacao(partialUpdatedHistoricoOperacao)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHistoricoOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historicoOperacao.setId(longCount.incrementAndGet());

        // Create the HistoricoOperacao
        HistoricoOperacaoDTO historicoOperacaoDTO = historicoOperacaoMapper.toDto(historicoOperacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHistoricoOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, historicoOperacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historicoOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoricoOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHistoricoOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historicoOperacao.setId(longCount.incrementAndGet());

        // Create the HistoricoOperacao
        HistoricoOperacaoDTO historicoOperacaoDTO = historicoOperacaoMapper.toDto(historicoOperacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoricoOperacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(historicoOperacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HistoricoOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHistoricoOperacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        historicoOperacao.setId(longCount.incrementAndGet());

        // Create the HistoricoOperacao
        HistoricoOperacaoDTO historicoOperacaoDTO = historicoOperacaoMapper.toDto(historicoOperacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHistoricoOperacaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(historicoOperacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HistoricoOperacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHistoricoOperacao() throws Exception {
        // Initialize the database
        insertedHistoricoOperacao = historicoOperacaoRepository.saveAndFlush(historicoOperacao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the historicoOperacao
        restHistoricoOperacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, historicoOperacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return historicoOperacaoRepository.count();
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

    protected HistoricoOperacao getPersistedHistoricoOperacao(HistoricoOperacao historicoOperacao) {
        return historicoOperacaoRepository.findById(historicoOperacao.getId()).orElseThrow();
    }

    protected void assertPersistedHistoricoOperacaoToMatchAllProperties(HistoricoOperacao expectedHistoricoOperacao) {
        assertHistoricoOperacaoAllPropertiesEquals(expectedHistoricoOperacao, getPersistedHistoricoOperacao(expectedHistoricoOperacao));
    }

    protected void assertPersistedHistoricoOperacaoToMatchUpdatableProperties(HistoricoOperacao expectedHistoricoOperacao) {
        assertHistoricoOperacaoAllUpdatablePropertiesEquals(
            expectedHistoricoOperacao,
            getPersistedHistoricoOperacao(expectedHistoricoOperacao)
        );
    }
}
