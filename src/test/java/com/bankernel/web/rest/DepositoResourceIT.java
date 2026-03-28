package com.bankernel.web.rest;

import static com.bankernel.domain.DepositoAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bankernel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Carteira;
import com.bankernel.domain.ContaBancaria;
import com.bankernel.domain.Deposito;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.Transacao;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumStatusDeposito;
import com.bankernel.domain.enumeration.EnumTipoDeposito;
import com.bankernel.repository.DepositoRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.DepositoService;
import com.bankernel.service.dto.DepositoDTO;
import com.bankernel.service.mapper.DepositoMapper;
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
 * Integration tests for the {@link DepositoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepositoResourceIT {

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_VALOR_CREDITADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_CREDITADO = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR_CREDITADO = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_VALOR_SALDO_CARTEIRA = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_SALDO_CARTEIRA = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR_SALDO_CARTEIRA = new BigDecimal(1 - 1);

    private static final EnumTipoDeposito DEFAULT_TIPO_DEPOSITO = EnumTipoDeposito.PIX;
    private static final EnumTipoDeposito UPDATED_TIPO_DEPOSITO = EnumTipoDeposito.BOLETO;

    private static final EnumStatusDeposito DEFAULT_SITUACAO_DEPOSITO = EnumStatusDeposito.PENDENTE;
    private static final EnumStatusDeposito UPDATED_SITUACAO_DEPOSITO = EnumStatusDeposito.EM_PROCESSAMENTO;

    private static final String DEFAULT_NUMERO_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_REFERENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCIA_EXTERNA = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCIA_EXTERNA = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_MOTIVO_REJEICAO = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO_REJEICAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CONTABILIZADO = false;
    private static final Boolean UPDATED_CONTABILIZADO = true;

    private static final String DEFAULT_NOME_USUARIO_FIXO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_USUARIO_FIXO = "BBBBBBBBBB";

    private static final Integer DEFAULT_NUMERO_PARCELA = 1;
    private static final Integer UPDATED_NUMERO_PARCELA = 2;
    private static final Integer SMALLER_NUMERO_PARCELA = 1 - 1;

    private static final String ENTITY_API_URL = "/api/depositos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DepositoRepository depositoRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private DepositoRepository depositoRepositoryMock;

    @Autowired
    private DepositoMapper depositoMapper;

    @Mock
    private DepositoService depositoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepositoMockMvc;

    private Deposito deposito;

    private Deposito insertedDeposito;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deposito createEntity(EntityManager em) {
        Deposito deposito = new Deposito()
            .valor(DEFAULT_VALOR)
            .valorCreditado(DEFAULT_VALOR_CREDITADO)
            .valorSaldoCarteira(DEFAULT_VALOR_SALDO_CARTEIRA)
            .tipoDeposito(DEFAULT_TIPO_DEPOSITO)
            .situacaoDeposito(DEFAULT_SITUACAO_DEPOSITO)
            .numeroReferencia(DEFAULT_NUMERO_REFERENCIA)
            .referenciaExterna(DEFAULT_REFERENCIA_EXTERNA)
            .descricao(DEFAULT_DESCRICAO)
            .motivoRejeicao(DEFAULT_MOTIVO_REJEICAO)
            .contabilizado(DEFAULT_CONTABILIZADO)
            .nomeUsuarioFixo(DEFAULT_NOME_USUARIO_FIXO)
            .numeroParcela(DEFAULT_NUMERO_PARCELA);
        // Add required entity
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            carteira = CarteiraResourceIT.createEntity(em);
            em.persist(carteira);
            em.flush();
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        deposito.setCarteira(carteira);
        // Add required entity
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            moedaCarteira = MoedaCarteiraResourceIT.createEntity(em);
            em.persist(moedaCarteira);
            em.flush();
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        deposito.setMoedaCarteira(moedaCarteira);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        deposito.setUsuario(user);
        return deposito;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Deposito createUpdatedEntity(EntityManager em) {
        Deposito updatedDeposito = new Deposito()
            .valor(UPDATED_VALOR)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .valorSaldoCarteira(UPDATED_VALOR_SALDO_CARTEIRA)
            .tipoDeposito(UPDATED_TIPO_DEPOSITO)
            .situacaoDeposito(UPDATED_SITUACAO_DEPOSITO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .referenciaExterna(UPDATED_REFERENCIA_EXTERNA)
            .descricao(UPDATED_DESCRICAO)
            .motivoRejeicao(UPDATED_MOTIVO_REJEICAO)
            .contabilizado(UPDATED_CONTABILIZADO)
            .nomeUsuarioFixo(UPDATED_NOME_USUARIO_FIXO)
            .numeroParcela(UPDATED_NUMERO_PARCELA);
        // Add required entity
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            carteira = CarteiraResourceIT.createUpdatedEntity(em);
            em.persist(carteira);
            em.flush();
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        updatedDeposito.setCarteira(carteira);
        // Add required entity
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            moedaCarteira = MoedaCarteiraResourceIT.createUpdatedEntity(em);
            em.persist(moedaCarteira);
            em.flush();
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        updatedDeposito.setMoedaCarteira(moedaCarteira);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedDeposito.setUsuario(user);
        return updatedDeposito;
    }

    @BeforeEach
    void initTest() {
        deposito = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDeposito != null) {
            depositoRepository.delete(insertedDeposito);
            insertedDeposito = null;
        }
    }

    @Test
    @Transactional
    void createDeposito() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Deposito
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);
        var returnedDepositoDTO = om.readValue(
            restDepositoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DepositoDTO.class
        );

        // Validate the Deposito in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDeposito = depositoMapper.toEntity(returnedDepositoDTO);
        assertDepositoUpdatableFieldsEquals(returnedDeposito, getPersistedDeposito(returnedDeposito));

        insertedDeposito = returnedDeposito;
    }

    @Test
    @Transactional
    void createDepositoWithExistingId() throws Exception {
        // Create the Deposito with an existing ID
        deposito.setId(1L);
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepositoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Deposito in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deposito.setValor(null);

        // Create the Deposito, which fails.
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        restDepositoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoDepositoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deposito.setTipoDeposito(null);

        // Create the Deposito, which fails.
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        restDepositoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSituacaoDepositoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deposito.setSituacaoDeposito(null);

        // Create the Deposito, which fails.
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        restDepositoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroReferenciaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deposito.setNumeroReferencia(null);

        // Create the Deposito, which fails.
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        restDepositoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContabilizadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        deposito.setContabilizado(null);

        // Create the Deposito, which fails.
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        restDepositoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepositos() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList
        restDepositoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deposito.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].valorCreditado").value(hasItem(sameNumber(DEFAULT_VALOR_CREDITADO))))
            .andExpect(jsonPath("$.[*].valorSaldoCarteira").value(hasItem(sameNumber(DEFAULT_VALOR_SALDO_CARTEIRA))))
            .andExpect(jsonPath("$.[*].tipoDeposito").value(hasItem(DEFAULT_TIPO_DEPOSITO.toString())))
            .andExpect(jsonPath("$.[*].situacaoDeposito").value(hasItem(DEFAULT_SITUACAO_DEPOSITO.toString())))
            .andExpect(jsonPath("$.[*].numeroReferencia").value(hasItem(DEFAULT_NUMERO_REFERENCIA)))
            .andExpect(jsonPath("$.[*].referenciaExterna").value(hasItem(DEFAULT_REFERENCIA_EXTERNA)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].motivoRejeicao").value(hasItem(DEFAULT_MOTIVO_REJEICAO)))
            .andExpect(jsonPath("$.[*].contabilizado").value(hasItem(DEFAULT_CONTABILIZADO)))
            .andExpect(jsonPath("$.[*].nomeUsuarioFixo").value(hasItem(DEFAULT_NOME_USUARIO_FIXO)))
            .andExpect(jsonPath("$.[*].numeroParcela").value(hasItem(DEFAULT_NUMERO_PARCELA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepositosWithEagerRelationshipsIsEnabled() throws Exception {
        when(depositoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepositoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(depositoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepositosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(depositoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepositoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(depositoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDeposito() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get the deposito
        restDepositoMockMvc
            .perform(get(ENTITY_API_URL_ID, deposito.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deposito.getId().intValue()))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.valorCreditado").value(sameNumber(DEFAULT_VALOR_CREDITADO)))
            .andExpect(jsonPath("$.valorSaldoCarteira").value(sameNumber(DEFAULT_VALOR_SALDO_CARTEIRA)))
            .andExpect(jsonPath("$.tipoDeposito").value(DEFAULT_TIPO_DEPOSITO.toString()))
            .andExpect(jsonPath("$.situacaoDeposito").value(DEFAULT_SITUACAO_DEPOSITO.toString()))
            .andExpect(jsonPath("$.numeroReferencia").value(DEFAULT_NUMERO_REFERENCIA))
            .andExpect(jsonPath("$.referenciaExterna").value(DEFAULT_REFERENCIA_EXTERNA))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.motivoRejeicao").value(DEFAULT_MOTIVO_REJEICAO))
            .andExpect(jsonPath("$.contabilizado").value(DEFAULT_CONTABILIZADO))
            .andExpect(jsonPath("$.nomeUsuarioFixo").value(DEFAULT_NOME_USUARIO_FIXO))
            .andExpect(jsonPath("$.numeroParcela").value(DEFAULT_NUMERO_PARCELA));
    }

    @Test
    @Transactional
    void getDepositosByIdFiltering() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        Long id = deposito.getId();

        defaultDepositoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDepositoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDepositoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDepositosByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valor equals to
        defaultDepositoFiltering("valor.equals=" + DEFAULT_VALOR, "valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllDepositosByValorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valor in
        defaultDepositoFiltering("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR, "valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllDepositosByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valor is not null
        defaultDepositoFiltering("valor.specified=true", "valor.specified=false");
    }

    @Test
    @Transactional
    void getAllDepositosByValorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valor is greater than or equal to
        defaultDepositoFiltering("valor.greaterThanOrEqual=" + DEFAULT_VALOR, "valor.greaterThanOrEqual=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllDepositosByValorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valor is less than or equal to
        defaultDepositoFiltering("valor.lessThanOrEqual=" + DEFAULT_VALOR, "valor.lessThanOrEqual=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllDepositosByValorIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valor is less than
        defaultDepositoFiltering("valor.lessThan=" + UPDATED_VALOR, "valor.lessThan=" + DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void getAllDepositosByValorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valor is greater than
        defaultDepositoFiltering("valor.greaterThan=" + SMALLER_VALOR, "valor.greaterThan=" + DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void getAllDepositosByValorCreditadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valorCreditado equals to
        defaultDepositoFiltering("valorCreditado.equals=" + DEFAULT_VALOR_CREDITADO, "valorCreditado.equals=" + UPDATED_VALOR_CREDITADO);
    }

    @Test
    @Transactional
    void getAllDepositosByValorCreditadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valorCreditado in
        defaultDepositoFiltering(
            "valorCreditado.in=" + DEFAULT_VALOR_CREDITADO + "," + UPDATED_VALOR_CREDITADO,
            "valorCreditado.in=" + UPDATED_VALOR_CREDITADO
        );
    }

    @Test
    @Transactional
    void getAllDepositosByValorCreditadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valorCreditado is not null
        defaultDepositoFiltering("valorCreditado.specified=true", "valorCreditado.specified=false");
    }

    @Test
    @Transactional
    void getAllDepositosByValorCreditadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valorCreditado is greater than or equal to
        defaultDepositoFiltering(
            "valorCreditado.greaterThanOrEqual=" + DEFAULT_VALOR_CREDITADO,
            "valorCreditado.greaterThanOrEqual=" + UPDATED_VALOR_CREDITADO
        );
    }

    @Test
    @Transactional
    void getAllDepositosByValorCreditadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valorCreditado is less than or equal to
        defaultDepositoFiltering(
            "valorCreditado.lessThanOrEqual=" + DEFAULT_VALOR_CREDITADO,
            "valorCreditado.lessThanOrEqual=" + SMALLER_VALOR_CREDITADO
        );
    }

    @Test
    @Transactional
    void getAllDepositosByValorCreditadoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valorCreditado is less than
        defaultDepositoFiltering(
            "valorCreditado.lessThan=" + UPDATED_VALOR_CREDITADO,
            "valorCreditado.lessThan=" + DEFAULT_VALOR_CREDITADO
        );
    }

    @Test
    @Transactional
    void getAllDepositosByValorCreditadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valorCreditado is greater than
        defaultDepositoFiltering(
            "valorCreditado.greaterThan=" + SMALLER_VALOR_CREDITADO,
            "valorCreditado.greaterThan=" + DEFAULT_VALOR_CREDITADO
        );
    }

    @Test
    @Transactional
    void getAllDepositosByValorSaldoCarteiraIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valorSaldoCarteira equals to
        defaultDepositoFiltering(
            "valorSaldoCarteira.equals=" + DEFAULT_VALOR_SALDO_CARTEIRA,
            "valorSaldoCarteira.equals=" + UPDATED_VALOR_SALDO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByValorSaldoCarteiraIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valorSaldoCarteira in
        defaultDepositoFiltering(
            "valorSaldoCarteira.in=" + DEFAULT_VALOR_SALDO_CARTEIRA + "," + UPDATED_VALOR_SALDO_CARTEIRA,
            "valorSaldoCarteira.in=" + UPDATED_VALOR_SALDO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByValorSaldoCarteiraIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valorSaldoCarteira is not null
        defaultDepositoFiltering("valorSaldoCarteira.specified=true", "valorSaldoCarteira.specified=false");
    }

    @Test
    @Transactional
    void getAllDepositosByValorSaldoCarteiraIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valorSaldoCarteira is greater than or equal to
        defaultDepositoFiltering(
            "valorSaldoCarteira.greaterThanOrEqual=" + DEFAULT_VALOR_SALDO_CARTEIRA,
            "valorSaldoCarteira.greaterThanOrEqual=" + UPDATED_VALOR_SALDO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByValorSaldoCarteiraIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valorSaldoCarteira is less than or equal to
        defaultDepositoFiltering(
            "valorSaldoCarteira.lessThanOrEqual=" + DEFAULT_VALOR_SALDO_CARTEIRA,
            "valorSaldoCarteira.lessThanOrEqual=" + SMALLER_VALOR_SALDO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByValorSaldoCarteiraIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valorSaldoCarteira is less than
        defaultDepositoFiltering(
            "valorSaldoCarteira.lessThan=" + UPDATED_VALOR_SALDO_CARTEIRA,
            "valorSaldoCarteira.lessThan=" + DEFAULT_VALOR_SALDO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByValorSaldoCarteiraIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where valorSaldoCarteira is greater than
        defaultDepositoFiltering(
            "valorSaldoCarteira.greaterThan=" + SMALLER_VALOR_SALDO_CARTEIRA,
            "valorSaldoCarteira.greaterThan=" + DEFAULT_VALOR_SALDO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByTipoDepositoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where tipoDeposito equals to
        defaultDepositoFiltering("tipoDeposito.equals=" + DEFAULT_TIPO_DEPOSITO, "tipoDeposito.equals=" + UPDATED_TIPO_DEPOSITO);
    }

    @Test
    @Transactional
    void getAllDepositosByTipoDepositoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where tipoDeposito in
        defaultDepositoFiltering(
            "tipoDeposito.in=" + DEFAULT_TIPO_DEPOSITO + "," + UPDATED_TIPO_DEPOSITO,
            "tipoDeposito.in=" + UPDATED_TIPO_DEPOSITO
        );
    }

    @Test
    @Transactional
    void getAllDepositosByTipoDepositoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where tipoDeposito is not null
        defaultDepositoFiltering("tipoDeposito.specified=true", "tipoDeposito.specified=false");
    }

    @Test
    @Transactional
    void getAllDepositosBySituacaoDepositoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where situacaoDeposito equals to
        defaultDepositoFiltering(
            "situacaoDeposito.equals=" + DEFAULT_SITUACAO_DEPOSITO,
            "situacaoDeposito.equals=" + UPDATED_SITUACAO_DEPOSITO
        );
    }

    @Test
    @Transactional
    void getAllDepositosBySituacaoDepositoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where situacaoDeposito in
        defaultDepositoFiltering(
            "situacaoDeposito.in=" + DEFAULT_SITUACAO_DEPOSITO + "," + UPDATED_SITUACAO_DEPOSITO,
            "situacaoDeposito.in=" + UPDATED_SITUACAO_DEPOSITO
        );
    }

    @Test
    @Transactional
    void getAllDepositosBySituacaoDepositoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where situacaoDeposito is not null
        defaultDepositoFiltering("situacaoDeposito.specified=true", "situacaoDeposito.specified=false");
    }

    @Test
    @Transactional
    void getAllDepositosByNumeroReferenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where numeroReferencia equals to
        defaultDepositoFiltering(
            "numeroReferencia.equals=" + DEFAULT_NUMERO_REFERENCIA,
            "numeroReferencia.equals=" + UPDATED_NUMERO_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByNumeroReferenciaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where numeroReferencia in
        defaultDepositoFiltering(
            "numeroReferencia.in=" + DEFAULT_NUMERO_REFERENCIA + "," + UPDATED_NUMERO_REFERENCIA,
            "numeroReferencia.in=" + UPDATED_NUMERO_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByNumeroReferenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where numeroReferencia is not null
        defaultDepositoFiltering("numeroReferencia.specified=true", "numeroReferencia.specified=false");
    }

    @Test
    @Transactional
    void getAllDepositosByNumeroReferenciaContainsSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where numeroReferencia contains
        defaultDepositoFiltering(
            "numeroReferencia.contains=" + DEFAULT_NUMERO_REFERENCIA,
            "numeroReferencia.contains=" + UPDATED_NUMERO_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByNumeroReferenciaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where numeroReferencia does not contain
        defaultDepositoFiltering(
            "numeroReferencia.doesNotContain=" + UPDATED_NUMERO_REFERENCIA,
            "numeroReferencia.doesNotContain=" + DEFAULT_NUMERO_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByReferenciaExternaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where referenciaExterna equals to
        defaultDepositoFiltering(
            "referenciaExterna.equals=" + DEFAULT_REFERENCIA_EXTERNA,
            "referenciaExterna.equals=" + UPDATED_REFERENCIA_EXTERNA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByReferenciaExternaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where referenciaExterna in
        defaultDepositoFiltering(
            "referenciaExterna.in=" + DEFAULT_REFERENCIA_EXTERNA + "," + UPDATED_REFERENCIA_EXTERNA,
            "referenciaExterna.in=" + UPDATED_REFERENCIA_EXTERNA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByReferenciaExternaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where referenciaExterna is not null
        defaultDepositoFiltering("referenciaExterna.specified=true", "referenciaExterna.specified=false");
    }

    @Test
    @Transactional
    void getAllDepositosByReferenciaExternaContainsSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where referenciaExterna contains
        defaultDepositoFiltering(
            "referenciaExterna.contains=" + DEFAULT_REFERENCIA_EXTERNA,
            "referenciaExterna.contains=" + UPDATED_REFERENCIA_EXTERNA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByReferenciaExternaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where referenciaExterna does not contain
        defaultDepositoFiltering(
            "referenciaExterna.doesNotContain=" + UPDATED_REFERENCIA_EXTERNA,
            "referenciaExterna.doesNotContain=" + DEFAULT_REFERENCIA_EXTERNA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where descricao equals to
        defaultDepositoFiltering("descricao.equals=" + DEFAULT_DESCRICAO, "descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllDepositosByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where descricao in
        defaultDepositoFiltering("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO, "descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllDepositosByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where descricao is not null
        defaultDepositoFiltering("descricao.specified=true", "descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllDepositosByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where descricao contains
        defaultDepositoFiltering("descricao.contains=" + DEFAULT_DESCRICAO, "descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllDepositosByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where descricao does not contain
        defaultDepositoFiltering("descricao.doesNotContain=" + UPDATED_DESCRICAO, "descricao.doesNotContain=" + DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllDepositosByMotivoRejeicaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where motivoRejeicao equals to
        defaultDepositoFiltering("motivoRejeicao.equals=" + DEFAULT_MOTIVO_REJEICAO, "motivoRejeicao.equals=" + UPDATED_MOTIVO_REJEICAO);
    }

    @Test
    @Transactional
    void getAllDepositosByMotivoRejeicaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where motivoRejeicao in
        defaultDepositoFiltering(
            "motivoRejeicao.in=" + DEFAULT_MOTIVO_REJEICAO + "," + UPDATED_MOTIVO_REJEICAO,
            "motivoRejeicao.in=" + UPDATED_MOTIVO_REJEICAO
        );
    }

    @Test
    @Transactional
    void getAllDepositosByMotivoRejeicaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where motivoRejeicao is not null
        defaultDepositoFiltering("motivoRejeicao.specified=true", "motivoRejeicao.specified=false");
    }

    @Test
    @Transactional
    void getAllDepositosByMotivoRejeicaoContainsSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where motivoRejeicao contains
        defaultDepositoFiltering(
            "motivoRejeicao.contains=" + DEFAULT_MOTIVO_REJEICAO,
            "motivoRejeicao.contains=" + UPDATED_MOTIVO_REJEICAO
        );
    }

    @Test
    @Transactional
    void getAllDepositosByMotivoRejeicaoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where motivoRejeicao does not contain
        defaultDepositoFiltering(
            "motivoRejeicao.doesNotContain=" + UPDATED_MOTIVO_REJEICAO,
            "motivoRejeicao.doesNotContain=" + DEFAULT_MOTIVO_REJEICAO
        );
    }

    @Test
    @Transactional
    void getAllDepositosByContabilizadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where contabilizado equals to
        defaultDepositoFiltering("contabilizado.equals=" + DEFAULT_CONTABILIZADO, "contabilizado.equals=" + UPDATED_CONTABILIZADO);
    }

    @Test
    @Transactional
    void getAllDepositosByContabilizadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where contabilizado in
        defaultDepositoFiltering(
            "contabilizado.in=" + DEFAULT_CONTABILIZADO + "," + UPDATED_CONTABILIZADO,
            "contabilizado.in=" + UPDATED_CONTABILIZADO
        );
    }

    @Test
    @Transactional
    void getAllDepositosByContabilizadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where contabilizado is not null
        defaultDepositoFiltering("contabilizado.specified=true", "contabilizado.specified=false");
    }

    @Test
    @Transactional
    void getAllDepositosByNomeUsuarioFixoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where nomeUsuarioFixo equals to
        defaultDepositoFiltering(
            "nomeUsuarioFixo.equals=" + DEFAULT_NOME_USUARIO_FIXO,
            "nomeUsuarioFixo.equals=" + UPDATED_NOME_USUARIO_FIXO
        );
    }

    @Test
    @Transactional
    void getAllDepositosByNomeUsuarioFixoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where nomeUsuarioFixo in
        defaultDepositoFiltering(
            "nomeUsuarioFixo.in=" + DEFAULT_NOME_USUARIO_FIXO + "," + UPDATED_NOME_USUARIO_FIXO,
            "nomeUsuarioFixo.in=" + UPDATED_NOME_USUARIO_FIXO
        );
    }

    @Test
    @Transactional
    void getAllDepositosByNomeUsuarioFixoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where nomeUsuarioFixo is not null
        defaultDepositoFiltering("nomeUsuarioFixo.specified=true", "nomeUsuarioFixo.specified=false");
    }

    @Test
    @Transactional
    void getAllDepositosByNomeUsuarioFixoContainsSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where nomeUsuarioFixo contains
        defaultDepositoFiltering(
            "nomeUsuarioFixo.contains=" + DEFAULT_NOME_USUARIO_FIXO,
            "nomeUsuarioFixo.contains=" + UPDATED_NOME_USUARIO_FIXO
        );
    }

    @Test
    @Transactional
    void getAllDepositosByNomeUsuarioFixoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where nomeUsuarioFixo does not contain
        defaultDepositoFiltering(
            "nomeUsuarioFixo.doesNotContain=" + UPDATED_NOME_USUARIO_FIXO,
            "nomeUsuarioFixo.doesNotContain=" + DEFAULT_NOME_USUARIO_FIXO
        );
    }

    @Test
    @Transactional
    void getAllDepositosByNumeroParcelaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where numeroParcela equals to
        defaultDepositoFiltering("numeroParcela.equals=" + DEFAULT_NUMERO_PARCELA, "numeroParcela.equals=" + UPDATED_NUMERO_PARCELA);
    }

    @Test
    @Transactional
    void getAllDepositosByNumeroParcelaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where numeroParcela in
        defaultDepositoFiltering(
            "numeroParcela.in=" + DEFAULT_NUMERO_PARCELA + "," + UPDATED_NUMERO_PARCELA,
            "numeroParcela.in=" + UPDATED_NUMERO_PARCELA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByNumeroParcelaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where numeroParcela is not null
        defaultDepositoFiltering("numeroParcela.specified=true", "numeroParcela.specified=false");
    }

    @Test
    @Transactional
    void getAllDepositosByNumeroParcelaIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where numeroParcela is greater than or equal to
        defaultDepositoFiltering(
            "numeroParcela.greaterThanOrEqual=" + DEFAULT_NUMERO_PARCELA,
            "numeroParcela.greaterThanOrEqual=" + UPDATED_NUMERO_PARCELA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByNumeroParcelaIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where numeroParcela is less than or equal to
        defaultDepositoFiltering(
            "numeroParcela.lessThanOrEqual=" + DEFAULT_NUMERO_PARCELA,
            "numeroParcela.lessThanOrEqual=" + SMALLER_NUMERO_PARCELA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByNumeroParcelaIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where numeroParcela is less than
        defaultDepositoFiltering("numeroParcela.lessThan=" + UPDATED_NUMERO_PARCELA, "numeroParcela.lessThan=" + DEFAULT_NUMERO_PARCELA);
    }

    @Test
    @Transactional
    void getAllDepositosByNumeroParcelaIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        // Get all the depositoList where numeroParcela is greater than
        defaultDepositoFiltering(
            "numeroParcela.greaterThan=" + SMALLER_NUMERO_PARCELA,
            "numeroParcela.greaterThan=" + DEFAULT_NUMERO_PARCELA
        );
    }

    @Test
    @Transactional
    void getAllDepositosByTransacaoIsEqualToSomething() throws Exception {
        Transacao transacao;
        if (TestUtil.findAll(em, Transacao.class).isEmpty()) {
            depositoRepository.saveAndFlush(deposito);
            transacao = TransacaoResourceIT.createEntity();
        } else {
            transacao = TestUtil.findAll(em, Transacao.class).get(0);
        }
        em.persist(transacao);
        em.flush();
        deposito.setTransacao(transacao);
        depositoRepository.saveAndFlush(deposito);
        Long transacaoId = transacao.getId();
        // Get all the depositoList where transacao equals to transacaoId
        defaultDepositoShouldBeFound("transacaoId.equals=" + transacaoId);

        // Get all the depositoList where transacao equals to (transacaoId + 1)
        defaultDepositoShouldNotBeFound("transacaoId.equals=" + (transacaoId + 1));
    }

    @Test
    @Transactional
    void getAllDepositosByCarteiraIsEqualToSomething() throws Exception {
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            depositoRepository.saveAndFlush(deposito);
            carteira = CarteiraResourceIT.createEntity(em);
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        em.persist(carteira);
        em.flush();
        deposito.setCarteira(carteira);
        depositoRepository.saveAndFlush(deposito);
        Long carteiraId = carteira.getId();
        // Get all the depositoList where carteira equals to carteiraId
        defaultDepositoShouldBeFound("carteiraId.equals=" + carteiraId);

        // Get all the depositoList where carteira equals to (carteiraId + 1)
        defaultDepositoShouldNotBeFound("carteiraId.equals=" + (carteiraId + 1));
    }

    @Test
    @Transactional
    void getAllDepositosByMoedaCarteiraIsEqualToSomething() throws Exception {
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            depositoRepository.saveAndFlush(deposito);
            moedaCarteira = MoedaCarteiraResourceIT.createEntity(em);
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        em.persist(moedaCarteira);
        em.flush();
        deposito.setMoedaCarteira(moedaCarteira);
        depositoRepository.saveAndFlush(deposito);
        Long moedaCarteiraId = moedaCarteira.getId();
        // Get all the depositoList where moedaCarteira equals to moedaCarteiraId
        defaultDepositoShouldBeFound("moedaCarteiraId.equals=" + moedaCarteiraId);

        // Get all the depositoList where moedaCarteira equals to (moedaCarteiraId + 1)
        defaultDepositoShouldNotBeFound("moedaCarteiraId.equals=" + (moedaCarteiraId + 1));
    }

    @Test
    @Transactional
    void getAllDepositosByUsuarioIsEqualToSomething() throws Exception {
        User usuario;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            depositoRepository.saveAndFlush(deposito);
            usuario = UserResourceIT.createEntity();
        } else {
            usuario = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        deposito.setUsuario(usuario);
        depositoRepository.saveAndFlush(deposito);
        Long usuarioId = usuario.getId();
        // Get all the depositoList where usuario equals to usuarioId
        defaultDepositoShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the depositoList where usuario equals to (usuarioId + 1)
        defaultDepositoShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    @Test
    @Transactional
    void getAllDepositosByContaBancariaIsEqualToSomething() throws Exception {
        ContaBancaria contaBancaria;
        if (TestUtil.findAll(em, ContaBancaria.class).isEmpty()) {
            depositoRepository.saveAndFlush(deposito);
            contaBancaria = ContaBancariaResourceIT.createEntity(em);
        } else {
            contaBancaria = TestUtil.findAll(em, ContaBancaria.class).get(0);
        }
        em.persist(contaBancaria);
        em.flush();
        deposito.setContaBancaria(contaBancaria);
        depositoRepository.saveAndFlush(deposito);
        Long contaBancariaId = contaBancaria.getId();
        // Get all the depositoList where contaBancaria equals to contaBancariaId
        defaultDepositoShouldBeFound("contaBancariaId.equals=" + contaBancariaId);

        // Get all the depositoList where contaBancaria equals to (contaBancariaId + 1)
        defaultDepositoShouldNotBeFound("contaBancariaId.equals=" + (contaBancariaId + 1));
    }

    private void defaultDepositoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDepositoShouldBeFound(shouldBeFound);
        defaultDepositoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepositoShouldBeFound(String filter) throws Exception {
        restDepositoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deposito.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].valorCreditado").value(hasItem(sameNumber(DEFAULT_VALOR_CREDITADO))))
            .andExpect(jsonPath("$.[*].valorSaldoCarteira").value(hasItem(sameNumber(DEFAULT_VALOR_SALDO_CARTEIRA))))
            .andExpect(jsonPath("$.[*].tipoDeposito").value(hasItem(DEFAULT_TIPO_DEPOSITO.toString())))
            .andExpect(jsonPath("$.[*].situacaoDeposito").value(hasItem(DEFAULT_SITUACAO_DEPOSITO.toString())))
            .andExpect(jsonPath("$.[*].numeroReferencia").value(hasItem(DEFAULT_NUMERO_REFERENCIA)))
            .andExpect(jsonPath("$.[*].referenciaExterna").value(hasItem(DEFAULT_REFERENCIA_EXTERNA)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].motivoRejeicao").value(hasItem(DEFAULT_MOTIVO_REJEICAO)))
            .andExpect(jsonPath("$.[*].contabilizado").value(hasItem(DEFAULT_CONTABILIZADO)))
            .andExpect(jsonPath("$.[*].nomeUsuarioFixo").value(hasItem(DEFAULT_NOME_USUARIO_FIXO)))
            .andExpect(jsonPath("$.[*].numeroParcela").value(hasItem(DEFAULT_NUMERO_PARCELA)));

        // Check, that the count call also returns 1
        restDepositoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepositoShouldNotBeFound(String filter) throws Exception {
        restDepositoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepositoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDeposito() throws Exception {
        // Get the deposito
        restDepositoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDeposito() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deposito
        Deposito updatedDeposito = depositoRepository.findById(deposito.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDeposito are not directly saved in db
        em.detach(updatedDeposito);
        updatedDeposito
            .valor(UPDATED_VALOR)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .valorSaldoCarteira(UPDATED_VALOR_SALDO_CARTEIRA)
            .tipoDeposito(UPDATED_TIPO_DEPOSITO)
            .situacaoDeposito(UPDATED_SITUACAO_DEPOSITO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .referenciaExterna(UPDATED_REFERENCIA_EXTERNA)
            .descricao(UPDATED_DESCRICAO)
            .motivoRejeicao(UPDATED_MOTIVO_REJEICAO)
            .contabilizado(UPDATED_CONTABILIZADO)
            .nomeUsuarioFixo(UPDATED_NOME_USUARIO_FIXO)
            .numeroParcela(UPDATED_NUMERO_PARCELA);
        DepositoDTO depositoDTO = depositoMapper.toDto(updatedDeposito);

        restDepositoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depositoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(depositoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Deposito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDepositoToMatchAllProperties(updatedDeposito);
    }

    @Test
    @Transactional
    void putNonExistingDeposito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deposito.setId(longCount.incrementAndGet());

        // Create the Deposito
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepositoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depositoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(depositoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deposito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDeposito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deposito.setId(longCount.incrementAndGet());

        // Create the Deposito
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(depositoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deposito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDeposito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deposito.setId(longCount.incrementAndGet());

        // Create the Deposito
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deposito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepositoWithPatch() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deposito using partial update
        Deposito partialUpdatedDeposito = new Deposito();
        partialUpdatedDeposito.setId(deposito.getId());

        partialUpdatedDeposito
            .valor(UPDATED_VALOR)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .tipoDeposito(UPDATED_TIPO_DEPOSITO)
            .referenciaExterna(UPDATED_REFERENCIA_EXTERNA)
            .contabilizado(UPDATED_CONTABILIZADO)
            .nomeUsuarioFixo(UPDATED_NOME_USUARIO_FIXO);

        restDepositoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeposito.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeposito))
            )
            .andExpect(status().isOk());

        // Validate the Deposito in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepositoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDeposito, deposito), getPersistedDeposito(deposito));
    }

    @Test
    @Transactional
    void fullUpdateDepositoWithPatch() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the deposito using partial update
        Deposito partialUpdatedDeposito = new Deposito();
        partialUpdatedDeposito.setId(deposito.getId());

        partialUpdatedDeposito
            .valor(UPDATED_VALOR)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .valorSaldoCarteira(UPDATED_VALOR_SALDO_CARTEIRA)
            .tipoDeposito(UPDATED_TIPO_DEPOSITO)
            .situacaoDeposito(UPDATED_SITUACAO_DEPOSITO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .referenciaExterna(UPDATED_REFERENCIA_EXTERNA)
            .descricao(UPDATED_DESCRICAO)
            .motivoRejeicao(UPDATED_MOTIVO_REJEICAO)
            .contabilizado(UPDATED_CONTABILIZADO)
            .nomeUsuarioFixo(UPDATED_NOME_USUARIO_FIXO)
            .numeroParcela(UPDATED_NUMERO_PARCELA);

        restDepositoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDeposito.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDeposito))
            )
            .andExpect(status().isOk());

        // Validate the Deposito in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepositoUpdatableFieldsEquals(partialUpdatedDeposito, getPersistedDeposito(partialUpdatedDeposito));
    }

    @Test
    @Transactional
    void patchNonExistingDeposito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deposito.setId(longCount.incrementAndGet());

        // Create the Deposito
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepositoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depositoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(depositoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deposito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDeposito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deposito.setId(longCount.incrementAndGet());

        // Create the Deposito
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(depositoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Deposito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDeposito() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        deposito.setId(longCount.incrementAndGet());

        // Create the Deposito
        DepositoDTO depositoDTO = depositoMapper.toDto(deposito);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(depositoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Deposito in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDeposito() throws Exception {
        // Initialize the database
        insertedDeposito = depositoRepository.saveAndFlush(deposito);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the deposito
        restDepositoMockMvc
            .perform(delete(ENTITY_API_URL_ID, deposito.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return depositoRepository.count();
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

    protected Deposito getPersistedDeposito(Deposito deposito) {
        return depositoRepository.findById(deposito.getId()).orElseThrow();
    }

    protected void assertPersistedDepositoToMatchAllProperties(Deposito expectedDeposito) {
        assertDepositoAllPropertiesEquals(expectedDeposito, getPersistedDeposito(expectedDeposito));
    }

    protected void assertPersistedDepositoToMatchUpdatableProperties(Deposito expectedDeposito) {
        assertDepositoAllUpdatablePropertiesEquals(expectedDeposito, getPersistedDeposito(expectedDeposito));
    }
}
