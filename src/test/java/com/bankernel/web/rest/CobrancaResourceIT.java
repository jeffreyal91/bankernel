package com.bankernel.web.rest;

import static com.bankernel.domain.CobrancaAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bankernel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Carteira;
import com.bankernel.domain.Cobranca;
import com.bankernel.domain.LinkCobranca;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.Transacao;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumStatusCobranca;
import com.bankernel.domain.enumeration.EnumTipoCobranca;
import com.bankernel.domain.enumeration.EnumTipoDesconto;
import com.bankernel.repository.CobrancaRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.CobrancaService;
import com.bankernel.service.dto.CobrancaDTO;
import com.bankernel.service.mapper.CobrancaMapper;
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
 * Integration tests for the {@link CobrancaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CobrancaResourceIT {

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_VALOR_CREDITADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_CREDITADO = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR_CREDITADO = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_VALOR_CREDITADO_CARTEIRA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_CREDITADO_CARTEIRA = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR_CREDITADO_CARTEIRA = new BigDecimal(1 - 1);

    private static final String DEFAULT_ID_PRODUTO_EXTERNO = "AAAAAAAAAA";
    private static final String UPDATED_ID_PRODUTO_EXTERNO = "BBBBBBBBBB";

    private static final String DEFAULT_NOME_PRODUTO_EXTERNO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_PRODUTO_EXTERNO = "BBBBBBBBBB";

    private static final EnumStatusCobranca DEFAULT_SITUACAO = EnumStatusCobranca.PENDENTE;
    private static final EnumStatusCobranca UPDATED_SITUACAO = EnumStatusCobranca.EM_PROCESSAMENTO;

    private static final EnumTipoCobranca DEFAULT_TIPO = EnumTipoCobranca.PIX;
    private static final EnumTipoCobranca UPDATED_TIPO = EnumTipoCobranca.BOLETO;

    private static final BigDecimal DEFAULT_DESCONTO_GERAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_DESCONTO_GERAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_DESCONTO_GERAL = new BigDecimal(1 - 1);

    private static final EnumTipoDesconto DEFAULT_TIPO_DESCONTO = EnumTipoDesconto.FIXO;
    private static final EnumTipoDesconto UPDATED_TIPO_DESCONTO = EnumTipoDesconto.PERCENTUAL;

    private static final Boolean DEFAULT_CONTABILIZADO = false;
    private static final Boolean UPDATED_CONTABILIZADO = true;

    private static final String DEFAULT_NOME_USUARIO_FIXO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_USUARIO_FIXO = "BBBBBBBBBB";

    private static final String DEFAULT_CHAVE_COBRANCA = "AAAAAAAAAA";
    private static final String UPDATED_CHAVE_COBRANCA = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICADOR_EXTERNO = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR_EXTERNO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_RETORNO_NOTIFICADO = false;
    private static final Boolean UPDATED_RETORNO_NOTIFICADO = true;

    private static final String ENTITY_API_URL = "/api/cobrancas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CobrancaRepository cobrancaRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private CobrancaRepository cobrancaRepositoryMock;

    @Autowired
    private CobrancaMapper cobrancaMapper;

    @Mock
    private CobrancaService cobrancaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCobrancaMockMvc;

    private Cobranca cobranca;

    private Cobranca insertedCobranca;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cobranca createEntity(EntityManager em) {
        Cobranca cobranca = new Cobranca()
            .valor(DEFAULT_VALOR)
            .valorCreditado(DEFAULT_VALOR_CREDITADO)
            .valorCreditadoCarteira(DEFAULT_VALOR_CREDITADO_CARTEIRA)
            .idProdutoExterno(DEFAULT_ID_PRODUTO_EXTERNO)
            .nomeProdutoExterno(DEFAULT_NOME_PRODUTO_EXTERNO)
            .situacao(DEFAULT_SITUACAO)
            .tipo(DEFAULT_TIPO)
            .descontoGeral(DEFAULT_DESCONTO_GERAL)
            .tipoDesconto(DEFAULT_TIPO_DESCONTO)
            .contabilizado(DEFAULT_CONTABILIZADO)
            .nomeUsuarioFixo(DEFAULT_NOME_USUARIO_FIXO)
            .chaveCobranca(DEFAULT_CHAVE_COBRANCA)
            .identificadorExterno(DEFAULT_IDENTIFICADOR_EXTERNO)
            .retornoNotificado(DEFAULT_RETORNO_NOTIFICADO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        cobranca.setUsuario(user);
        // Add required entity
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            carteira = CarteiraResourceIT.createEntity(em);
            em.persist(carteira);
            em.flush();
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        cobranca.setCarteira(carteira);
        // Add required entity
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            moedaCarteira = MoedaCarteiraResourceIT.createEntity(em);
            em.persist(moedaCarteira);
            em.flush();
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        cobranca.setMoedaCarteira(moedaCarteira);
        return cobranca;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cobranca createUpdatedEntity(EntityManager em) {
        Cobranca updatedCobranca = new Cobranca()
            .valor(UPDATED_VALOR)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .valorCreditadoCarteira(UPDATED_VALOR_CREDITADO_CARTEIRA)
            .idProdutoExterno(UPDATED_ID_PRODUTO_EXTERNO)
            .nomeProdutoExterno(UPDATED_NOME_PRODUTO_EXTERNO)
            .situacao(UPDATED_SITUACAO)
            .tipo(UPDATED_TIPO)
            .descontoGeral(UPDATED_DESCONTO_GERAL)
            .tipoDesconto(UPDATED_TIPO_DESCONTO)
            .contabilizado(UPDATED_CONTABILIZADO)
            .nomeUsuarioFixo(UPDATED_NOME_USUARIO_FIXO)
            .chaveCobranca(UPDATED_CHAVE_COBRANCA)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .retornoNotificado(UPDATED_RETORNO_NOTIFICADO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedCobranca.setUsuario(user);
        // Add required entity
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            carteira = CarteiraResourceIT.createUpdatedEntity(em);
            em.persist(carteira);
            em.flush();
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        updatedCobranca.setCarteira(carteira);
        // Add required entity
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            moedaCarteira = MoedaCarteiraResourceIT.createUpdatedEntity(em);
            em.persist(moedaCarteira);
            em.flush();
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        updatedCobranca.setMoedaCarteira(moedaCarteira);
        return updatedCobranca;
    }

    @BeforeEach
    void initTest() {
        cobranca = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCobranca != null) {
            cobrancaRepository.delete(insertedCobranca);
            insertedCobranca = null;
        }
    }

    @Test
    @Transactional
    void createCobranca() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Cobranca
        CobrancaDTO cobrancaDTO = cobrancaMapper.toDto(cobranca);
        var returnedCobrancaDTO = om.readValue(
            restCobrancaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cobrancaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CobrancaDTO.class
        );

        // Validate the Cobranca in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCobranca = cobrancaMapper.toEntity(returnedCobrancaDTO);
        assertCobrancaUpdatableFieldsEquals(returnedCobranca, getPersistedCobranca(returnedCobranca));

        insertedCobranca = returnedCobranca;
    }

    @Test
    @Transactional
    void createCobrancaWithExistingId() throws Exception {
        // Create the Cobranca with an existing ID
        cobranca.setId(1L);
        CobrancaDTO cobrancaDTO = cobrancaMapper.toDto(cobranca);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCobrancaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cobrancaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Cobranca in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cobranca.setValor(null);

        // Create the Cobranca, which fails.
        CobrancaDTO cobrancaDTO = cobrancaMapper.toDto(cobranca);

        restCobrancaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cobrancaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSituacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cobranca.setSituacao(null);

        // Create the Cobranca, which fails.
        CobrancaDTO cobrancaDTO = cobrancaMapper.toDto(cobranca);

        restCobrancaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cobrancaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cobranca.setTipo(null);

        // Create the Cobranca, which fails.
        CobrancaDTO cobrancaDTO = cobrancaMapper.toDto(cobranca);

        restCobrancaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cobrancaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContabilizadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cobranca.setContabilizado(null);

        // Create the Cobranca, which fails.
        CobrancaDTO cobrancaDTO = cobrancaMapper.toDto(cobranca);

        restCobrancaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cobrancaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRetornoNotificadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        cobranca.setRetornoNotificado(null);

        // Create the Cobranca, which fails.
        CobrancaDTO cobrancaDTO = cobrancaMapper.toDto(cobranca);

        restCobrancaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cobrancaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCobrancas() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList
        restCobrancaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cobranca.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].valorCreditado").value(hasItem(sameNumber(DEFAULT_VALOR_CREDITADO))))
            .andExpect(jsonPath("$.[*].valorCreditadoCarteira").value(hasItem(sameNumber(DEFAULT_VALOR_CREDITADO_CARTEIRA))))
            .andExpect(jsonPath("$.[*].idProdutoExterno").value(hasItem(DEFAULT_ID_PRODUTO_EXTERNO)))
            .andExpect(jsonPath("$.[*].nomeProdutoExterno").value(hasItem(DEFAULT_NOME_PRODUTO_EXTERNO)))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].descontoGeral").value(hasItem(sameNumber(DEFAULT_DESCONTO_GERAL))))
            .andExpect(jsonPath("$.[*].tipoDesconto").value(hasItem(DEFAULT_TIPO_DESCONTO.toString())))
            .andExpect(jsonPath("$.[*].contabilizado").value(hasItem(DEFAULT_CONTABILIZADO)))
            .andExpect(jsonPath("$.[*].nomeUsuarioFixo").value(hasItem(DEFAULT_NOME_USUARIO_FIXO)))
            .andExpect(jsonPath("$.[*].chaveCobranca").value(hasItem(DEFAULT_CHAVE_COBRANCA)))
            .andExpect(jsonPath("$.[*].identificadorExterno").value(hasItem(DEFAULT_IDENTIFICADOR_EXTERNO)))
            .andExpect(jsonPath("$.[*].retornoNotificado").value(hasItem(DEFAULT_RETORNO_NOTIFICADO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCobrancasWithEagerRelationshipsIsEnabled() throws Exception {
        when(cobrancaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCobrancaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cobrancaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCobrancasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cobrancaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCobrancaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cobrancaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCobranca() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get the cobranca
        restCobrancaMockMvc
            .perform(get(ENTITY_API_URL_ID, cobranca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cobranca.getId().intValue()))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.valorCreditado").value(sameNumber(DEFAULT_VALOR_CREDITADO)))
            .andExpect(jsonPath("$.valorCreditadoCarteira").value(sameNumber(DEFAULT_VALOR_CREDITADO_CARTEIRA)))
            .andExpect(jsonPath("$.idProdutoExterno").value(DEFAULT_ID_PRODUTO_EXTERNO))
            .andExpect(jsonPath("$.nomeProdutoExterno").value(DEFAULT_NOME_PRODUTO_EXTERNO))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.descontoGeral").value(sameNumber(DEFAULT_DESCONTO_GERAL)))
            .andExpect(jsonPath("$.tipoDesconto").value(DEFAULT_TIPO_DESCONTO.toString()))
            .andExpect(jsonPath("$.contabilizado").value(DEFAULT_CONTABILIZADO))
            .andExpect(jsonPath("$.nomeUsuarioFixo").value(DEFAULT_NOME_USUARIO_FIXO))
            .andExpect(jsonPath("$.chaveCobranca").value(DEFAULT_CHAVE_COBRANCA))
            .andExpect(jsonPath("$.identificadorExterno").value(DEFAULT_IDENTIFICADOR_EXTERNO))
            .andExpect(jsonPath("$.retornoNotificado").value(DEFAULT_RETORNO_NOTIFICADO));
    }

    @Test
    @Transactional
    void getCobrancasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        Long id = cobranca.getId();

        defaultCobrancaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCobrancaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCobrancaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCobrancasByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valor equals to
        defaultCobrancaFiltering("valor.equals=" + DEFAULT_VALOR, "valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllCobrancasByValorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valor in
        defaultCobrancaFiltering("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR, "valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllCobrancasByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valor is not null
        defaultCobrancaFiltering("valor.specified=true", "valor.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrancasByValorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valor is greater than or equal to
        defaultCobrancaFiltering("valor.greaterThanOrEqual=" + DEFAULT_VALOR, "valor.greaterThanOrEqual=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllCobrancasByValorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valor is less than or equal to
        defaultCobrancaFiltering("valor.lessThanOrEqual=" + DEFAULT_VALOR, "valor.lessThanOrEqual=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllCobrancasByValorIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valor is less than
        defaultCobrancaFiltering("valor.lessThan=" + UPDATED_VALOR, "valor.lessThan=" + DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void getAllCobrancasByValorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valor is greater than
        defaultCobrancaFiltering("valor.greaterThan=" + SMALLER_VALOR, "valor.greaterThan=" + DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void getAllCobrancasByValorCreditadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valorCreditado equals to
        defaultCobrancaFiltering("valorCreditado.equals=" + DEFAULT_VALOR_CREDITADO, "valorCreditado.equals=" + UPDATED_VALOR_CREDITADO);
    }

    @Test
    @Transactional
    void getAllCobrancasByValorCreditadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valorCreditado in
        defaultCobrancaFiltering(
            "valorCreditado.in=" + DEFAULT_VALOR_CREDITADO + "," + UPDATED_VALOR_CREDITADO,
            "valorCreditado.in=" + UPDATED_VALOR_CREDITADO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByValorCreditadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valorCreditado is not null
        defaultCobrancaFiltering("valorCreditado.specified=true", "valorCreditado.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrancasByValorCreditadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valorCreditado is greater than or equal to
        defaultCobrancaFiltering(
            "valorCreditado.greaterThanOrEqual=" + DEFAULT_VALOR_CREDITADO,
            "valorCreditado.greaterThanOrEqual=" + UPDATED_VALOR_CREDITADO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByValorCreditadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valorCreditado is less than or equal to
        defaultCobrancaFiltering(
            "valorCreditado.lessThanOrEqual=" + DEFAULT_VALOR_CREDITADO,
            "valorCreditado.lessThanOrEqual=" + SMALLER_VALOR_CREDITADO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByValorCreditadoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valorCreditado is less than
        defaultCobrancaFiltering(
            "valorCreditado.lessThan=" + UPDATED_VALOR_CREDITADO,
            "valorCreditado.lessThan=" + DEFAULT_VALOR_CREDITADO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByValorCreditadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valorCreditado is greater than
        defaultCobrancaFiltering(
            "valorCreditado.greaterThan=" + SMALLER_VALOR_CREDITADO,
            "valorCreditado.greaterThan=" + DEFAULT_VALOR_CREDITADO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByValorCreditadoCarteiraIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valorCreditadoCarteira equals to
        defaultCobrancaFiltering(
            "valorCreditadoCarteira.equals=" + DEFAULT_VALOR_CREDITADO_CARTEIRA,
            "valorCreditadoCarteira.equals=" + UPDATED_VALOR_CREDITADO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByValorCreditadoCarteiraIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valorCreditadoCarteira in
        defaultCobrancaFiltering(
            "valorCreditadoCarteira.in=" + DEFAULT_VALOR_CREDITADO_CARTEIRA + "," + UPDATED_VALOR_CREDITADO_CARTEIRA,
            "valorCreditadoCarteira.in=" + UPDATED_VALOR_CREDITADO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByValorCreditadoCarteiraIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valorCreditadoCarteira is not null
        defaultCobrancaFiltering("valorCreditadoCarteira.specified=true", "valorCreditadoCarteira.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrancasByValorCreditadoCarteiraIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valorCreditadoCarteira is greater than or equal to
        defaultCobrancaFiltering(
            "valorCreditadoCarteira.greaterThanOrEqual=" + DEFAULT_VALOR_CREDITADO_CARTEIRA,
            "valorCreditadoCarteira.greaterThanOrEqual=" + UPDATED_VALOR_CREDITADO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByValorCreditadoCarteiraIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valorCreditadoCarteira is less than or equal to
        defaultCobrancaFiltering(
            "valorCreditadoCarteira.lessThanOrEqual=" + DEFAULT_VALOR_CREDITADO_CARTEIRA,
            "valorCreditadoCarteira.lessThanOrEqual=" + SMALLER_VALOR_CREDITADO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByValorCreditadoCarteiraIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valorCreditadoCarteira is less than
        defaultCobrancaFiltering(
            "valorCreditadoCarteira.lessThan=" + UPDATED_VALOR_CREDITADO_CARTEIRA,
            "valorCreditadoCarteira.lessThan=" + DEFAULT_VALOR_CREDITADO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByValorCreditadoCarteiraIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where valorCreditadoCarteira is greater than
        defaultCobrancaFiltering(
            "valorCreditadoCarteira.greaterThan=" + SMALLER_VALOR_CREDITADO_CARTEIRA,
            "valorCreditadoCarteira.greaterThan=" + DEFAULT_VALOR_CREDITADO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByIdProdutoExternoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where idProdutoExterno equals to
        defaultCobrancaFiltering(
            "idProdutoExterno.equals=" + DEFAULT_ID_PRODUTO_EXTERNO,
            "idProdutoExterno.equals=" + UPDATED_ID_PRODUTO_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByIdProdutoExternoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where idProdutoExterno in
        defaultCobrancaFiltering(
            "idProdutoExterno.in=" + DEFAULT_ID_PRODUTO_EXTERNO + "," + UPDATED_ID_PRODUTO_EXTERNO,
            "idProdutoExterno.in=" + UPDATED_ID_PRODUTO_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByIdProdutoExternoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where idProdutoExterno is not null
        defaultCobrancaFiltering("idProdutoExterno.specified=true", "idProdutoExterno.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrancasByIdProdutoExternoContainsSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where idProdutoExterno contains
        defaultCobrancaFiltering(
            "idProdutoExterno.contains=" + DEFAULT_ID_PRODUTO_EXTERNO,
            "idProdutoExterno.contains=" + UPDATED_ID_PRODUTO_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByIdProdutoExternoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where idProdutoExterno does not contain
        defaultCobrancaFiltering(
            "idProdutoExterno.doesNotContain=" + UPDATED_ID_PRODUTO_EXTERNO,
            "idProdutoExterno.doesNotContain=" + DEFAULT_ID_PRODUTO_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByNomeProdutoExternoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where nomeProdutoExterno equals to
        defaultCobrancaFiltering(
            "nomeProdutoExterno.equals=" + DEFAULT_NOME_PRODUTO_EXTERNO,
            "nomeProdutoExterno.equals=" + UPDATED_NOME_PRODUTO_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByNomeProdutoExternoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where nomeProdutoExterno in
        defaultCobrancaFiltering(
            "nomeProdutoExterno.in=" + DEFAULT_NOME_PRODUTO_EXTERNO + "," + UPDATED_NOME_PRODUTO_EXTERNO,
            "nomeProdutoExterno.in=" + UPDATED_NOME_PRODUTO_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByNomeProdutoExternoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where nomeProdutoExterno is not null
        defaultCobrancaFiltering("nomeProdutoExterno.specified=true", "nomeProdutoExterno.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrancasByNomeProdutoExternoContainsSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where nomeProdutoExterno contains
        defaultCobrancaFiltering(
            "nomeProdutoExterno.contains=" + DEFAULT_NOME_PRODUTO_EXTERNO,
            "nomeProdutoExterno.contains=" + UPDATED_NOME_PRODUTO_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByNomeProdutoExternoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where nomeProdutoExterno does not contain
        defaultCobrancaFiltering(
            "nomeProdutoExterno.doesNotContain=" + UPDATED_NOME_PRODUTO_EXTERNO,
            "nomeProdutoExterno.doesNotContain=" + DEFAULT_NOME_PRODUTO_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasBySituacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where situacao equals to
        defaultCobrancaFiltering("situacao.equals=" + DEFAULT_SITUACAO, "situacao.equals=" + UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    void getAllCobrancasBySituacaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where situacao in
        defaultCobrancaFiltering("situacao.in=" + DEFAULT_SITUACAO + "," + UPDATED_SITUACAO, "situacao.in=" + UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    void getAllCobrancasBySituacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where situacao is not null
        defaultCobrancaFiltering("situacao.specified=true", "situacao.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrancasByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where tipo equals to
        defaultCobrancaFiltering("tipo.equals=" + DEFAULT_TIPO, "tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllCobrancasByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where tipo in
        defaultCobrancaFiltering("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO, "tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllCobrancasByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where tipo is not null
        defaultCobrancaFiltering("tipo.specified=true", "tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrancasByDescontoGeralIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where descontoGeral equals to
        defaultCobrancaFiltering("descontoGeral.equals=" + DEFAULT_DESCONTO_GERAL, "descontoGeral.equals=" + UPDATED_DESCONTO_GERAL);
    }

    @Test
    @Transactional
    void getAllCobrancasByDescontoGeralIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where descontoGeral in
        defaultCobrancaFiltering(
            "descontoGeral.in=" + DEFAULT_DESCONTO_GERAL + "," + UPDATED_DESCONTO_GERAL,
            "descontoGeral.in=" + UPDATED_DESCONTO_GERAL
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByDescontoGeralIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where descontoGeral is not null
        defaultCobrancaFiltering("descontoGeral.specified=true", "descontoGeral.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrancasByDescontoGeralIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where descontoGeral is greater than or equal to
        defaultCobrancaFiltering(
            "descontoGeral.greaterThanOrEqual=" + DEFAULT_DESCONTO_GERAL,
            "descontoGeral.greaterThanOrEqual=" + UPDATED_DESCONTO_GERAL
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByDescontoGeralIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where descontoGeral is less than or equal to
        defaultCobrancaFiltering(
            "descontoGeral.lessThanOrEqual=" + DEFAULT_DESCONTO_GERAL,
            "descontoGeral.lessThanOrEqual=" + SMALLER_DESCONTO_GERAL
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByDescontoGeralIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where descontoGeral is less than
        defaultCobrancaFiltering("descontoGeral.lessThan=" + UPDATED_DESCONTO_GERAL, "descontoGeral.lessThan=" + DEFAULT_DESCONTO_GERAL);
    }

    @Test
    @Transactional
    void getAllCobrancasByDescontoGeralIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where descontoGeral is greater than
        defaultCobrancaFiltering(
            "descontoGeral.greaterThan=" + SMALLER_DESCONTO_GERAL,
            "descontoGeral.greaterThan=" + DEFAULT_DESCONTO_GERAL
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByTipoDescontoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where tipoDesconto equals to
        defaultCobrancaFiltering("tipoDesconto.equals=" + DEFAULT_TIPO_DESCONTO, "tipoDesconto.equals=" + UPDATED_TIPO_DESCONTO);
    }

    @Test
    @Transactional
    void getAllCobrancasByTipoDescontoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where tipoDesconto in
        defaultCobrancaFiltering(
            "tipoDesconto.in=" + DEFAULT_TIPO_DESCONTO + "," + UPDATED_TIPO_DESCONTO,
            "tipoDesconto.in=" + UPDATED_TIPO_DESCONTO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByTipoDescontoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where tipoDesconto is not null
        defaultCobrancaFiltering("tipoDesconto.specified=true", "tipoDesconto.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrancasByContabilizadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where contabilizado equals to
        defaultCobrancaFiltering("contabilizado.equals=" + DEFAULT_CONTABILIZADO, "contabilizado.equals=" + UPDATED_CONTABILIZADO);
    }

    @Test
    @Transactional
    void getAllCobrancasByContabilizadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where contabilizado in
        defaultCobrancaFiltering(
            "contabilizado.in=" + DEFAULT_CONTABILIZADO + "," + UPDATED_CONTABILIZADO,
            "contabilizado.in=" + UPDATED_CONTABILIZADO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByContabilizadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where contabilizado is not null
        defaultCobrancaFiltering("contabilizado.specified=true", "contabilizado.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrancasByNomeUsuarioFixoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where nomeUsuarioFixo equals to
        defaultCobrancaFiltering(
            "nomeUsuarioFixo.equals=" + DEFAULT_NOME_USUARIO_FIXO,
            "nomeUsuarioFixo.equals=" + UPDATED_NOME_USUARIO_FIXO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByNomeUsuarioFixoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where nomeUsuarioFixo in
        defaultCobrancaFiltering(
            "nomeUsuarioFixo.in=" + DEFAULT_NOME_USUARIO_FIXO + "," + UPDATED_NOME_USUARIO_FIXO,
            "nomeUsuarioFixo.in=" + UPDATED_NOME_USUARIO_FIXO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByNomeUsuarioFixoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where nomeUsuarioFixo is not null
        defaultCobrancaFiltering("nomeUsuarioFixo.specified=true", "nomeUsuarioFixo.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrancasByNomeUsuarioFixoContainsSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where nomeUsuarioFixo contains
        defaultCobrancaFiltering(
            "nomeUsuarioFixo.contains=" + DEFAULT_NOME_USUARIO_FIXO,
            "nomeUsuarioFixo.contains=" + UPDATED_NOME_USUARIO_FIXO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByNomeUsuarioFixoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where nomeUsuarioFixo does not contain
        defaultCobrancaFiltering(
            "nomeUsuarioFixo.doesNotContain=" + UPDATED_NOME_USUARIO_FIXO,
            "nomeUsuarioFixo.doesNotContain=" + DEFAULT_NOME_USUARIO_FIXO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByChaveCobrancaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where chaveCobranca equals to
        defaultCobrancaFiltering("chaveCobranca.equals=" + DEFAULT_CHAVE_COBRANCA, "chaveCobranca.equals=" + UPDATED_CHAVE_COBRANCA);
    }

    @Test
    @Transactional
    void getAllCobrancasByChaveCobrancaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where chaveCobranca in
        defaultCobrancaFiltering(
            "chaveCobranca.in=" + DEFAULT_CHAVE_COBRANCA + "," + UPDATED_CHAVE_COBRANCA,
            "chaveCobranca.in=" + UPDATED_CHAVE_COBRANCA
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByChaveCobrancaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where chaveCobranca is not null
        defaultCobrancaFiltering("chaveCobranca.specified=true", "chaveCobranca.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrancasByChaveCobrancaContainsSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where chaveCobranca contains
        defaultCobrancaFiltering("chaveCobranca.contains=" + DEFAULT_CHAVE_COBRANCA, "chaveCobranca.contains=" + UPDATED_CHAVE_COBRANCA);
    }

    @Test
    @Transactional
    void getAllCobrancasByChaveCobrancaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where chaveCobranca does not contain
        defaultCobrancaFiltering(
            "chaveCobranca.doesNotContain=" + UPDATED_CHAVE_COBRANCA,
            "chaveCobranca.doesNotContain=" + DEFAULT_CHAVE_COBRANCA
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByIdentificadorExternoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where identificadorExterno equals to
        defaultCobrancaFiltering(
            "identificadorExterno.equals=" + DEFAULT_IDENTIFICADOR_EXTERNO,
            "identificadorExterno.equals=" + UPDATED_IDENTIFICADOR_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByIdentificadorExternoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where identificadorExterno in
        defaultCobrancaFiltering(
            "identificadorExterno.in=" + DEFAULT_IDENTIFICADOR_EXTERNO + "," + UPDATED_IDENTIFICADOR_EXTERNO,
            "identificadorExterno.in=" + UPDATED_IDENTIFICADOR_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByIdentificadorExternoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where identificadorExterno is not null
        defaultCobrancaFiltering("identificadorExterno.specified=true", "identificadorExterno.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrancasByIdentificadorExternoContainsSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where identificadorExterno contains
        defaultCobrancaFiltering(
            "identificadorExterno.contains=" + DEFAULT_IDENTIFICADOR_EXTERNO,
            "identificadorExterno.contains=" + UPDATED_IDENTIFICADOR_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByIdentificadorExternoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where identificadorExterno does not contain
        defaultCobrancaFiltering(
            "identificadorExterno.doesNotContain=" + UPDATED_IDENTIFICADOR_EXTERNO,
            "identificadorExterno.doesNotContain=" + DEFAULT_IDENTIFICADOR_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByRetornoNotificadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where retornoNotificado equals to
        defaultCobrancaFiltering(
            "retornoNotificado.equals=" + DEFAULT_RETORNO_NOTIFICADO,
            "retornoNotificado.equals=" + UPDATED_RETORNO_NOTIFICADO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByRetornoNotificadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where retornoNotificado in
        defaultCobrancaFiltering(
            "retornoNotificado.in=" + DEFAULT_RETORNO_NOTIFICADO + "," + UPDATED_RETORNO_NOTIFICADO,
            "retornoNotificado.in=" + UPDATED_RETORNO_NOTIFICADO
        );
    }

    @Test
    @Transactional
    void getAllCobrancasByRetornoNotificadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        // Get all the cobrancaList where retornoNotificado is not null
        defaultCobrancaFiltering("retornoNotificado.specified=true", "retornoNotificado.specified=false");
    }

    @Test
    @Transactional
    void getAllCobrancasByTransacaoIsEqualToSomething() throws Exception {
        Transacao transacao;
        if (TestUtil.findAll(em, Transacao.class).isEmpty()) {
            cobrancaRepository.saveAndFlush(cobranca);
            transacao = TransacaoResourceIT.createEntity();
        } else {
            transacao = TestUtil.findAll(em, Transacao.class).get(0);
        }
        em.persist(transacao);
        em.flush();
        cobranca.setTransacao(transacao);
        cobrancaRepository.saveAndFlush(cobranca);
        Long transacaoId = transacao.getId();
        // Get all the cobrancaList where transacao equals to transacaoId
        defaultCobrancaShouldBeFound("transacaoId.equals=" + transacaoId);

        // Get all the cobrancaList where transacao equals to (transacaoId + 1)
        defaultCobrancaShouldNotBeFound("transacaoId.equals=" + (transacaoId + 1));
    }

    @Test
    @Transactional
    void getAllCobrancasByUsuarioIsEqualToSomething() throws Exception {
        User usuario;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            cobrancaRepository.saveAndFlush(cobranca);
            usuario = UserResourceIT.createEntity();
        } else {
            usuario = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        cobranca.setUsuario(usuario);
        cobrancaRepository.saveAndFlush(cobranca);
        Long usuarioId = usuario.getId();
        // Get all the cobrancaList where usuario equals to usuarioId
        defaultCobrancaShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the cobrancaList where usuario equals to (usuarioId + 1)
        defaultCobrancaShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    @Test
    @Transactional
    void getAllCobrancasByCarteiraIsEqualToSomething() throws Exception {
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            cobrancaRepository.saveAndFlush(cobranca);
            carteira = CarteiraResourceIT.createEntity(em);
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        em.persist(carteira);
        em.flush();
        cobranca.setCarteira(carteira);
        cobrancaRepository.saveAndFlush(cobranca);
        Long carteiraId = carteira.getId();
        // Get all the cobrancaList where carteira equals to carteiraId
        defaultCobrancaShouldBeFound("carteiraId.equals=" + carteiraId);

        // Get all the cobrancaList where carteira equals to (carteiraId + 1)
        defaultCobrancaShouldNotBeFound("carteiraId.equals=" + (carteiraId + 1));
    }

    @Test
    @Transactional
    void getAllCobrancasByCarteiraCreditadaIsEqualToSomething() throws Exception {
        Carteira carteiraCreditada;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            cobrancaRepository.saveAndFlush(cobranca);
            carteiraCreditada = CarteiraResourceIT.createEntity(em);
        } else {
            carteiraCreditada = TestUtil.findAll(em, Carteira.class).get(0);
        }
        em.persist(carteiraCreditada);
        em.flush();
        cobranca.setCarteiraCreditada(carteiraCreditada);
        cobrancaRepository.saveAndFlush(cobranca);
        Long carteiraCreditadaId = carteiraCreditada.getId();
        // Get all the cobrancaList where carteiraCreditada equals to carteiraCreditadaId
        defaultCobrancaShouldBeFound("carteiraCreditadaId.equals=" + carteiraCreditadaId);

        // Get all the cobrancaList where carteiraCreditada equals to (carteiraCreditadaId + 1)
        defaultCobrancaShouldNotBeFound("carteiraCreditadaId.equals=" + (carteiraCreditadaId + 1));
    }

    @Test
    @Transactional
    void getAllCobrancasByMoedaCarteiraIsEqualToSomething() throws Exception {
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            cobrancaRepository.saveAndFlush(cobranca);
            moedaCarteira = MoedaCarteiraResourceIT.createEntity(em);
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        em.persist(moedaCarteira);
        em.flush();
        cobranca.setMoedaCarteira(moedaCarteira);
        cobrancaRepository.saveAndFlush(cobranca);
        Long moedaCarteiraId = moedaCarteira.getId();
        // Get all the cobrancaList where moedaCarteira equals to moedaCarteiraId
        defaultCobrancaShouldBeFound("moedaCarteiraId.equals=" + moedaCarteiraId);

        // Get all the cobrancaList where moedaCarteira equals to (moedaCarteiraId + 1)
        defaultCobrancaShouldNotBeFound("moedaCarteiraId.equals=" + (moedaCarteiraId + 1));
    }

    @Test
    @Transactional
    void getAllCobrancasByLinkCobrancaIsEqualToSomething() throws Exception {
        LinkCobranca linkCobranca;
        if (TestUtil.findAll(em, LinkCobranca.class).isEmpty()) {
            cobrancaRepository.saveAndFlush(cobranca);
            linkCobranca = LinkCobrancaResourceIT.createEntity(em);
        } else {
            linkCobranca = TestUtil.findAll(em, LinkCobranca.class).get(0);
        }
        em.persist(linkCobranca);
        em.flush();
        cobranca.setLinkCobranca(linkCobranca);
        cobrancaRepository.saveAndFlush(cobranca);
        Long linkCobrancaId = linkCobranca.getId();
        // Get all the cobrancaList where linkCobranca equals to linkCobrancaId
        defaultCobrancaShouldBeFound("linkCobrancaId.equals=" + linkCobrancaId);

        // Get all the cobrancaList where linkCobranca equals to (linkCobrancaId + 1)
        defaultCobrancaShouldNotBeFound("linkCobrancaId.equals=" + (linkCobrancaId + 1));
    }

    private void defaultCobrancaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCobrancaShouldBeFound(shouldBeFound);
        defaultCobrancaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCobrancaShouldBeFound(String filter) throws Exception {
        restCobrancaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cobranca.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].valorCreditado").value(hasItem(sameNumber(DEFAULT_VALOR_CREDITADO))))
            .andExpect(jsonPath("$.[*].valorCreditadoCarteira").value(hasItem(sameNumber(DEFAULT_VALOR_CREDITADO_CARTEIRA))))
            .andExpect(jsonPath("$.[*].idProdutoExterno").value(hasItem(DEFAULT_ID_PRODUTO_EXTERNO)))
            .andExpect(jsonPath("$.[*].nomeProdutoExterno").value(hasItem(DEFAULT_NOME_PRODUTO_EXTERNO)))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].descontoGeral").value(hasItem(sameNumber(DEFAULT_DESCONTO_GERAL))))
            .andExpect(jsonPath("$.[*].tipoDesconto").value(hasItem(DEFAULT_TIPO_DESCONTO.toString())))
            .andExpect(jsonPath("$.[*].contabilizado").value(hasItem(DEFAULT_CONTABILIZADO)))
            .andExpect(jsonPath("$.[*].nomeUsuarioFixo").value(hasItem(DEFAULT_NOME_USUARIO_FIXO)))
            .andExpect(jsonPath("$.[*].chaveCobranca").value(hasItem(DEFAULT_CHAVE_COBRANCA)))
            .andExpect(jsonPath("$.[*].identificadorExterno").value(hasItem(DEFAULT_IDENTIFICADOR_EXTERNO)))
            .andExpect(jsonPath("$.[*].retornoNotificado").value(hasItem(DEFAULT_RETORNO_NOTIFICADO)));

        // Check, that the count call also returns 1
        restCobrancaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCobrancaShouldNotBeFound(String filter) throws Exception {
        restCobrancaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCobrancaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCobranca() throws Exception {
        // Get the cobranca
        restCobrancaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCobranca() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cobranca
        Cobranca updatedCobranca = cobrancaRepository.findById(cobranca.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCobranca are not directly saved in db
        em.detach(updatedCobranca);
        updatedCobranca
            .valor(UPDATED_VALOR)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .valorCreditadoCarteira(UPDATED_VALOR_CREDITADO_CARTEIRA)
            .idProdutoExterno(UPDATED_ID_PRODUTO_EXTERNO)
            .nomeProdutoExterno(UPDATED_NOME_PRODUTO_EXTERNO)
            .situacao(UPDATED_SITUACAO)
            .tipo(UPDATED_TIPO)
            .descontoGeral(UPDATED_DESCONTO_GERAL)
            .tipoDesconto(UPDATED_TIPO_DESCONTO)
            .contabilizado(UPDATED_CONTABILIZADO)
            .nomeUsuarioFixo(UPDATED_NOME_USUARIO_FIXO)
            .chaveCobranca(UPDATED_CHAVE_COBRANCA)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .retornoNotificado(UPDATED_RETORNO_NOTIFICADO);
        CobrancaDTO cobrancaDTO = cobrancaMapper.toDto(updatedCobranca);

        restCobrancaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cobrancaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cobrancaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Cobranca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCobrancaToMatchAllProperties(updatedCobranca);
    }

    @Test
    @Transactional
    void putNonExistingCobranca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cobranca.setId(longCount.incrementAndGet());

        // Create the Cobranca
        CobrancaDTO cobrancaDTO = cobrancaMapper.toDto(cobranca);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCobrancaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cobrancaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cobrancaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cobranca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCobranca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cobranca.setId(longCount.incrementAndGet());

        // Create the Cobranca
        CobrancaDTO cobrancaDTO = cobrancaMapper.toDto(cobranca);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCobrancaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(cobrancaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cobranca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCobranca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cobranca.setId(longCount.incrementAndGet());

        // Create the Cobranca
        CobrancaDTO cobrancaDTO = cobrancaMapper.toDto(cobranca);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCobrancaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(cobrancaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cobranca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCobrancaWithPatch() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cobranca using partial update
        Cobranca partialUpdatedCobranca = new Cobranca();
        partialUpdatedCobranca.setId(cobranca.getId());

        partialUpdatedCobranca
            .valor(UPDATED_VALOR)
            .idProdutoExterno(UPDATED_ID_PRODUTO_EXTERNO)
            .situacao(UPDATED_SITUACAO)
            .tipo(UPDATED_TIPO)
            .descontoGeral(UPDATED_DESCONTO_GERAL)
            .tipoDesconto(UPDATED_TIPO_DESCONTO)
            .contabilizado(UPDATED_CONTABILIZADO)
            .nomeUsuarioFixo(UPDATED_NOME_USUARIO_FIXO)
            .chaveCobranca(UPDATED_CHAVE_COBRANCA)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .retornoNotificado(UPDATED_RETORNO_NOTIFICADO);

        restCobrancaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCobranca.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCobranca))
            )
            .andExpect(status().isOk());

        // Validate the Cobranca in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCobrancaUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCobranca, cobranca), getPersistedCobranca(cobranca));
    }

    @Test
    @Transactional
    void fullUpdateCobrancaWithPatch() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the cobranca using partial update
        Cobranca partialUpdatedCobranca = new Cobranca();
        partialUpdatedCobranca.setId(cobranca.getId());

        partialUpdatedCobranca
            .valor(UPDATED_VALOR)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .valorCreditadoCarteira(UPDATED_VALOR_CREDITADO_CARTEIRA)
            .idProdutoExterno(UPDATED_ID_PRODUTO_EXTERNO)
            .nomeProdutoExterno(UPDATED_NOME_PRODUTO_EXTERNO)
            .situacao(UPDATED_SITUACAO)
            .tipo(UPDATED_TIPO)
            .descontoGeral(UPDATED_DESCONTO_GERAL)
            .tipoDesconto(UPDATED_TIPO_DESCONTO)
            .contabilizado(UPDATED_CONTABILIZADO)
            .nomeUsuarioFixo(UPDATED_NOME_USUARIO_FIXO)
            .chaveCobranca(UPDATED_CHAVE_COBRANCA)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .retornoNotificado(UPDATED_RETORNO_NOTIFICADO);

        restCobrancaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCobranca.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCobranca))
            )
            .andExpect(status().isOk());

        // Validate the Cobranca in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCobrancaUpdatableFieldsEquals(partialUpdatedCobranca, getPersistedCobranca(partialUpdatedCobranca));
    }

    @Test
    @Transactional
    void patchNonExistingCobranca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cobranca.setId(longCount.incrementAndGet());

        // Create the Cobranca
        CobrancaDTO cobrancaDTO = cobrancaMapper.toDto(cobranca);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCobrancaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cobrancaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cobrancaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cobranca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCobranca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cobranca.setId(longCount.incrementAndGet());

        // Create the Cobranca
        CobrancaDTO cobrancaDTO = cobrancaMapper.toDto(cobranca);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCobrancaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(cobrancaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cobranca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCobranca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        cobranca.setId(longCount.incrementAndGet());

        // Create the Cobranca
        CobrancaDTO cobrancaDTO = cobrancaMapper.toDto(cobranca);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCobrancaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(cobrancaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cobranca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCobranca() throws Exception {
        // Initialize the database
        insertedCobranca = cobrancaRepository.saveAndFlush(cobranca);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the cobranca
        restCobrancaMockMvc
            .perform(delete(ENTITY_API_URL_ID, cobranca.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return cobrancaRepository.count();
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

    protected Cobranca getPersistedCobranca(Cobranca cobranca) {
        return cobrancaRepository.findById(cobranca.getId()).orElseThrow();
    }

    protected void assertPersistedCobrancaToMatchAllProperties(Cobranca expectedCobranca) {
        assertCobrancaAllPropertiesEquals(expectedCobranca, getPersistedCobranca(expectedCobranca));
    }

    protected void assertPersistedCobrancaToMatchUpdatableProperties(Cobranca expectedCobranca) {
        assertCobrancaAllUpdatablePropertiesEquals(expectedCobranca, getPersistedCobranca(expectedCobranca));
    }
}
