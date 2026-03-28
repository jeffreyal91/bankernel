package com.bankernel.web.rest;

import static com.bankernel.domain.CheckoutAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bankernel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Carteira;
import com.bankernel.domain.Checkout;
import com.bankernel.domain.LinkPagamento;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.Transacao;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumStatusCheckout;
import com.bankernel.domain.enumeration.EnumTipoCheckout;
import com.bankernel.domain.enumeration.EnumTipoDesconto;
import com.bankernel.repository.CheckoutRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.CheckoutService;
import com.bankernel.service.dto.CheckoutDTO;
import com.bankernel.service.mapper.CheckoutMapper;
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
 * Integration tests for the {@link CheckoutResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CheckoutResourceIT {

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

    private static final EnumStatusCheckout DEFAULT_STATUS = EnumStatusCheckout.PENDENTE;
    private static final EnumStatusCheckout UPDATED_STATUS = EnumStatusCheckout.EM_PROCESSAMENTO;

    private static final EnumTipoCheckout DEFAULT_TIPO = EnumTipoCheckout.PIX;
    private static final EnumTipoCheckout UPDATED_TIPO = EnumTipoCheckout.BOLETO;

    private static final BigDecimal DEFAULT_DESCONTO_GERAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_DESCONTO_GERAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_DESCONTO_GERAL = new BigDecimal(1 - 1);

    private static final EnumTipoDesconto DEFAULT_TIPO_DESCONTO = EnumTipoDesconto.FIXO;
    private static final EnumTipoDesconto UPDATED_TIPO_DESCONTO = EnumTipoDesconto.PERCENTUAL;

    private static final Boolean DEFAULT_CONTABILIZADO = false;
    private static final Boolean UPDATED_CONTABILIZADO = true;

    private static final String DEFAULT_NOME_USUARIO_FIXO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_USUARIO_FIXO = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN_CHECKOUT = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN_CHECKOUT = "BBBBBBBBBB";

    private static final String DEFAULT_ID_EXTERNO = "AAAAAAAAAA";
    private static final String UPDATED_ID_EXTERNO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_WEBHOOK_NOTIFICADO = false;
    private static final Boolean UPDATED_WEBHOOK_NOTIFICADO = true;

    private static final String ENTITY_API_URL = "/api/checkouts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CheckoutRepository checkoutRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private CheckoutRepository checkoutRepositoryMock;

    @Autowired
    private CheckoutMapper checkoutMapper;

    @Mock
    private CheckoutService checkoutServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCheckoutMockMvc;

    private Checkout checkout;

    private Checkout insertedCheckout;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Checkout createEntity(EntityManager em) {
        Checkout checkout = new Checkout()
            .valor(DEFAULT_VALOR)
            .valorCreditado(DEFAULT_VALOR_CREDITADO)
            .valorCreditadoCarteira(DEFAULT_VALOR_CREDITADO_CARTEIRA)
            .idProdutoExterno(DEFAULT_ID_PRODUTO_EXTERNO)
            .nomeProdutoExterno(DEFAULT_NOME_PRODUTO_EXTERNO)
            .status(DEFAULT_STATUS)
            .tipo(DEFAULT_TIPO)
            .descontoGeral(DEFAULT_DESCONTO_GERAL)
            .tipoDesconto(DEFAULT_TIPO_DESCONTO)
            .contabilizado(DEFAULT_CONTABILIZADO)
            .nomeUsuarioFixo(DEFAULT_NOME_USUARIO_FIXO)
            .tokenCheckout(DEFAULT_TOKEN_CHECKOUT)
            .idExterno(DEFAULT_ID_EXTERNO)
            .webhookNotificado(DEFAULT_WEBHOOK_NOTIFICADO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        checkout.setUsuario(user);
        // Add required entity
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            carteira = CarteiraResourceIT.createEntity(em);
            em.persist(carteira);
            em.flush();
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        checkout.setCarteira(carteira);
        // Add required entity
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            moedaCarteira = MoedaCarteiraResourceIT.createEntity(em);
            em.persist(moedaCarteira);
            em.flush();
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        checkout.setMoedaCarteira(moedaCarteira);
        return checkout;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Checkout createUpdatedEntity(EntityManager em) {
        Checkout updatedCheckout = new Checkout()
            .valor(UPDATED_VALOR)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .valorCreditadoCarteira(UPDATED_VALOR_CREDITADO_CARTEIRA)
            .idProdutoExterno(UPDATED_ID_PRODUTO_EXTERNO)
            .nomeProdutoExterno(UPDATED_NOME_PRODUTO_EXTERNO)
            .status(UPDATED_STATUS)
            .tipo(UPDATED_TIPO)
            .descontoGeral(UPDATED_DESCONTO_GERAL)
            .tipoDesconto(UPDATED_TIPO_DESCONTO)
            .contabilizado(UPDATED_CONTABILIZADO)
            .nomeUsuarioFixo(UPDATED_NOME_USUARIO_FIXO)
            .tokenCheckout(UPDATED_TOKEN_CHECKOUT)
            .idExterno(UPDATED_ID_EXTERNO)
            .webhookNotificado(UPDATED_WEBHOOK_NOTIFICADO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedCheckout.setUsuario(user);
        // Add required entity
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            carteira = CarteiraResourceIT.createUpdatedEntity(em);
            em.persist(carteira);
            em.flush();
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        updatedCheckout.setCarteira(carteira);
        // Add required entity
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            moedaCarteira = MoedaCarteiraResourceIT.createUpdatedEntity(em);
            em.persist(moedaCarteira);
            em.flush();
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        updatedCheckout.setMoedaCarteira(moedaCarteira);
        return updatedCheckout;
    }

    @BeforeEach
    void initTest() {
        checkout = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCheckout != null) {
            checkoutRepository.delete(insertedCheckout);
            insertedCheckout = null;
        }
    }

    @Test
    @Transactional
    void createCheckout() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Checkout
        CheckoutDTO checkoutDTO = checkoutMapper.toDto(checkout);
        var returnedCheckoutDTO = om.readValue(
            restCheckoutMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(checkoutDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CheckoutDTO.class
        );

        // Validate the Checkout in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCheckout = checkoutMapper.toEntity(returnedCheckoutDTO);
        assertCheckoutUpdatableFieldsEquals(returnedCheckout, getPersistedCheckout(returnedCheckout));

        insertedCheckout = returnedCheckout;
    }

    @Test
    @Transactional
    void createCheckoutWithExistingId() throws Exception {
        // Create the Checkout with an existing ID
        checkout.setId(1L);
        CheckoutDTO checkoutDTO = checkoutMapper.toDto(checkout);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCheckoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(checkoutDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Checkout in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        checkout.setValor(null);

        // Create the Checkout, which fails.
        CheckoutDTO checkoutDTO = checkoutMapper.toDto(checkout);

        restCheckoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(checkoutDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        checkout.setStatus(null);

        // Create the Checkout, which fails.
        CheckoutDTO checkoutDTO = checkoutMapper.toDto(checkout);

        restCheckoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(checkoutDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        checkout.setTipo(null);

        // Create the Checkout, which fails.
        CheckoutDTO checkoutDTO = checkoutMapper.toDto(checkout);

        restCheckoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(checkoutDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContabilizadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        checkout.setContabilizado(null);

        // Create the Checkout, which fails.
        CheckoutDTO checkoutDTO = checkoutMapper.toDto(checkout);

        restCheckoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(checkoutDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWebhookNotificadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        checkout.setWebhookNotificado(null);

        // Create the Checkout, which fails.
        CheckoutDTO checkoutDTO = checkoutMapper.toDto(checkout);

        restCheckoutMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(checkoutDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCheckouts() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList
        restCheckoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checkout.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].valorCreditado").value(hasItem(sameNumber(DEFAULT_VALOR_CREDITADO))))
            .andExpect(jsonPath("$.[*].valorCreditadoCarteira").value(hasItem(sameNumber(DEFAULT_VALOR_CREDITADO_CARTEIRA))))
            .andExpect(jsonPath("$.[*].idProdutoExterno").value(hasItem(DEFAULT_ID_PRODUTO_EXTERNO)))
            .andExpect(jsonPath("$.[*].nomeProdutoExterno").value(hasItem(DEFAULT_NOME_PRODUTO_EXTERNO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].descontoGeral").value(hasItem(sameNumber(DEFAULT_DESCONTO_GERAL))))
            .andExpect(jsonPath("$.[*].tipoDesconto").value(hasItem(DEFAULT_TIPO_DESCONTO.toString())))
            .andExpect(jsonPath("$.[*].contabilizado").value(hasItem(DEFAULT_CONTABILIZADO)))
            .andExpect(jsonPath("$.[*].nomeUsuarioFixo").value(hasItem(DEFAULT_NOME_USUARIO_FIXO)))
            .andExpect(jsonPath("$.[*].tokenCheckout").value(hasItem(DEFAULT_TOKEN_CHECKOUT)))
            .andExpect(jsonPath("$.[*].idExterno").value(hasItem(DEFAULT_ID_EXTERNO)))
            .andExpect(jsonPath("$.[*].webhookNotificado").value(hasItem(DEFAULT_WEBHOOK_NOTIFICADO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCheckoutsWithEagerRelationshipsIsEnabled() throws Exception {
        when(checkoutServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCheckoutMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(checkoutServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCheckoutsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(checkoutServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCheckoutMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(checkoutRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCheckout() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get the checkout
        restCheckoutMockMvc
            .perform(get(ENTITY_API_URL_ID, checkout.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(checkout.getId().intValue()))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.valorCreditado").value(sameNumber(DEFAULT_VALOR_CREDITADO)))
            .andExpect(jsonPath("$.valorCreditadoCarteira").value(sameNumber(DEFAULT_VALOR_CREDITADO_CARTEIRA)))
            .andExpect(jsonPath("$.idProdutoExterno").value(DEFAULT_ID_PRODUTO_EXTERNO))
            .andExpect(jsonPath("$.nomeProdutoExterno").value(DEFAULT_NOME_PRODUTO_EXTERNO))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.descontoGeral").value(sameNumber(DEFAULT_DESCONTO_GERAL)))
            .andExpect(jsonPath("$.tipoDesconto").value(DEFAULT_TIPO_DESCONTO.toString()))
            .andExpect(jsonPath("$.contabilizado").value(DEFAULT_CONTABILIZADO))
            .andExpect(jsonPath("$.nomeUsuarioFixo").value(DEFAULT_NOME_USUARIO_FIXO))
            .andExpect(jsonPath("$.tokenCheckout").value(DEFAULT_TOKEN_CHECKOUT))
            .andExpect(jsonPath("$.idExterno").value(DEFAULT_ID_EXTERNO))
            .andExpect(jsonPath("$.webhookNotificado").value(DEFAULT_WEBHOOK_NOTIFICADO));
    }

    @Test
    @Transactional
    void getCheckoutsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        Long id = checkout.getId();

        defaultCheckoutFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCheckoutFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCheckoutFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valor equals to
        defaultCheckoutFiltering("valor.equals=" + DEFAULT_VALOR, "valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valor in
        defaultCheckoutFiltering("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR, "valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valor is not null
        defaultCheckoutFiltering("valor.specified=true", "valor.specified=false");
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valor is greater than or equal to
        defaultCheckoutFiltering("valor.greaterThanOrEqual=" + DEFAULT_VALOR, "valor.greaterThanOrEqual=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valor is less than or equal to
        defaultCheckoutFiltering("valor.lessThanOrEqual=" + DEFAULT_VALOR, "valor.lessThanOrEqual=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valor is less than
        defaultCheckoutFiltering("valor.lessThan=" + UPDATED_VALOR, "valor.lessThan=" + DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valor is greater than
        defaultCheckoutFiltering("valor.greaterThan=" + SMALLER_VALOR, "valor.greaterThan=" + DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorCreditadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valorCreditado equals to
        defaultCheckoutFiltering("valorCreditado.equals=" + DEFAULT_VALOR_CREDITADO, "valorCreditado.equals=" + UPDATED_VALOR_CREDITADO);
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorCreditadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valorCreditado in
        defaultCheckoutFiltering(
            "valorCreditado.in=" + DEFAULT_VALOR_CREDITADO + "," + UPDATED_VALOR_CREDITADO,
            "valorCreditado.in=" + UPDATED_VALOR_CREDITADO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorCreditadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valorCreditado is not null
        defaultCheckoutFiltering("valorCreditado.specified=true", "valorCreditado.specified=false");
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorCreditadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valorCreditado is greater than or equal to
        defaultCheckoutFiltering(
            "valorCreditado.greaterThanOrEqual=" + DEFAULT_VALOR_CREDITADO,
            "valorCreditado.greaterThanOrEqual=" + UPDATED_VALOR_CREDITADO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorCreditadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valorCreditado is less than or equal to
        defaultCheckoutFiltering(
            "valorCreditado.lessThanOrEqual=" + DEFAULT_VALOR_CREDITADO,
            "valorCreditado.lessThanOrEqual=" + SMALLER_VALOR_CREDITADO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorCreditadoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valorCreditado is less than
        defaultCheckoutFiltering(
            "valorCreditado.lessThan=" + UPDATED_VALOR_CREDITADO,
            "valorCreditado.lessThan=" + DEFAULT_VALOR_CREDITADO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorCreditadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valorCreditado is greater than
        defaultCheckoutFiltering(
            "valorCreditado.greaterThan=" + SMALLER_VALOR_CREDITADO,
            "valorCreditado.greaterThan=" + DEFAULT_VALOR_CREDITADO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorCreditadoCarteiraIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valorCreditadoCarteira equals to
        defaultCheckoutFiltering(
            "valorCreditadoCarteira.equals=" + DEFAULT_VALOR_CREDITADO_CARTEIRA,
            "valorCreditadoCarteira.equals=" + UPDATED_VALOR_CREDITADO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorCreditadoCarteiraIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valorCreditadoCarteira in
        defaultCheckoutFiltering(
            "valorCreditadoCarteira.in=" + DEFAULT_VALOR_CREDITADO_CARTEIRA + "," + UPDATED_VALOR_CREDITADO_CARTEIRA,
            "valorCreditadoCarteira.in=" + UPDATED_VALOR_CREDITADO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorCreditadoCarteiraIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valorCreditadoCarteira is not null
        defaultCheckoutFiltering("valorCreditadoCarteira.specified=true", "valorCreditadoCarteira.specified=false");
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorCreditadoCarteiraIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valorCreditadoCarteira is greater than or equal to
        defaultCheckoutFiltering(
            "valorCreditadoCarteira.greaterThanOrEqual=" + DEFAULT_VALOR_CREDITADO_CARTEIRA,
            "valorCreditadoCarteira.greaterThanOrEqual=" + UPDATED_VALOR_CREDITADO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorCreditadoCarteiraIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valorCreditadoCarteira is less than or equal to
        defaultCheckoutFiltering(
            "valorCreditadoCarteira.lessThanOrEqual=" + DEFAULT_VALOR_CREDITADO_CARTEIRA,
            "valorCreditadoCarteira.lessThanOrEqual=" + SMALLER_VALOR_CREDITADO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorCreditadoCarteiraIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valorCreditadoCarteira is less than
        defaultCheckoutFiltering(
            "valorCreditadoCarteira.lessThan=" + UPDATED_VALOR_CREDITADO_CARTEIRA,
            "valorCreditadoCarteira.lessThan=" + DEFAULT_VALOR_CREDITADO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByValorCreditadoCarteiraIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where valorCreditadoCarteira is greater than
        defaultCheckoutFiltering(
            "valorCreditadoCarteira.greaterThan=" + SMALLER_VALOR_CREDITADO_CARTEIRA,
            "valorCreditadoCarteira.greaterThan=" + DEFAULT_VALOR_CREDITADO_CARTEIRA
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByIdProdutoExternoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where idProdutoExterno equals to
        defaultCheckoutFiltering(
            "idProdutoExterno.equals=" + DEFAULT_ID_PRODUTO_EXTERNO,
            "idProdutoExterno.equals=" + UPDATED_ID_PRODUTO_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByIdProdutoExternoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where idProdutoExterno in
        defaultCheckoutFiltering(
            "idProdutoExterno.in=" + DEFAULT_ID_PRODUTO_EXTERNO + "," + UPDATED_ID_PRODUTO_EXTERNO,
            "idProdutoExterno.in=" + UPDATED_ID_PRODUTO_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByIdProdutoExternoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where idProdutoExterno is not null
        defaultCheckoutFiltering("idProdutoExterno.specified=true", "idProdutoExterno.specified=false");
    }

    @Test
    @Transactional
    void getAllCheckoutsByIdProdutoExternoContainsSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where idProdutoExterno contains
        defaultCheckoutFiltering(
            "idProdutoExterno.contains=" + DEFAULT_ID_PRODUTO_EXTERNO,
            "idProdutoExterno.contains=" + UPDATED_ID_PRODUTO_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByIdProdutoExternoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where idProdutoExterno does not contain
        defaultCheckoutFiltering(
            "idProdutoExterno.doesNotContain=" + UPDATED_ID_PRODUTO_EXTERNO,
            "idProdutoExterno.doesNotContain=" + DEFAULT_ID_PRODUTO_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByNomeProdutoExternoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where nomeProdutoExterno equals to
        defaultCheckoutFiltering(
            "nomeProdutoExterno.equals=" + DEFAULT_NOME_PRODUTO_EXTERNO,
            "nomeProdutoExterno.equals=" + UPDATED_NOME_PRODUTO_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByNomeProdutoExternoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where nomeProdutoExterno in
        defaultCheckoutFiltering(
            "nomeProdutoExterno.in=" + DEFAULT_NOME_PRODUTO_EXTERNO + "," + UPDATED_NOME_PRODUTO_EXTERNO,
            "nomeProdutoExterno.in=" + UPDATED_NOME_PRODUTO_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByNomeProdutoExternoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where nomeProdutoExterno is not null
        defaultCheckoutFiltering("nomeProdutoExterno.specified=true", "nomeProdutoExterno.specified=false");
    }

    @Test
    @Transactional
    void getAllCheckoutsByNomeProdutoExternoContainsSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where nomeProdutoExterno contains
        defaultCheckoutFiltering(
            "nomeProdutoExterno.contains=" + DEFAULT_NOME_PRODUTO_EXTERNO,
            "nomeProdutoExterno.contains=" + UPDATED_NOME_PRODUTO_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByNomeProdutoExternoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where nomeProdutoExterno does not contain
        defaultCheckoutFiltering(
            "nomeProdutoExterno.doesNotContain=" + UPDATED_NOME_PRODUTO_EXTERNO,
            "nomeProdutoExterno.doesNotContain=" + DEFAULT_NOME_PRODUTO_EXTERNO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where status equals to
        defaultCheckoutFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCheckoutsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where status in
        defaultCheckoutFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCheckoutsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where status is not null
        defaultCheckoutFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllCheckoutsByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where tipo equals to
        defaultCheckoutFiltering("tipo.equals=" + DEFAULT_TIPO, "tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllCheckoutsByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where tipo in
        defaultCheckoutFiltering("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO, "tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllCheckoutsByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where tipo is not null
        defaultCheckoutFiltering("tipo.specified=true", "tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllCheckoutsByDescontoGeralIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where descontoGeral equals to
        defaultCheckoutFiltering("descontoGeral.equals=" + DEFAULT_DESCONTO_GERAL, "descontoGeral.equals=" + UPDATED_DESCONTO_GERAL);
    }

    @Test
    @Transactional
    void getAllCheckoutsByDescontoGeralIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where descontoGeral in
        defaultCheckoutFiltering(
            "descontoGeral.in=" + DEFAULT_DESCONTO_GERAL + "," + UPDATED_DESCONTO_GERAL,
            "descontoGeral.in=" + UPDATED_DESCONTO_GERAL
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByDescontoGeralIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where descontoGeral is not null
        defaultCheckoutFiltering("descontoGeral.specified=true", "descontoGeral.specified=false");
    }

    @Test
    @Transactional
    void getAllCheckoutsByDescontoGeralIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where descontoGeral is greater than or equal to
        defaultCheckoutFiltering(
            "descontoGeral.greaterThanOrEqual=" + DEFAULT_DESCONTO_GERAL,
            "descontoGeral.greaterThanOrEqual=" + UPDATED_DESCONTO_GERAL
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByDescontoGeralIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where descontoGeral is less than or equal to
        defaultCheckoutFiltering(
            "descontoGeral.lessThanOrEqual=" + DEFAULT_DESCONTO_GERAL,
            "descontoGeral.lessThanOrEqual=" + SMALLER_DESCONTO_GERAL
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByDescontoGeralIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where descontoGeral is less than
        defaultCheckoutFiltering("descontoGeral.lessThan=" + UPDATED_DESCONTO_GERAL, "descontoGeral.lessThan=" + DEFAULT_DESCONTO_GERAL);
    }

    @Test
    @Transactional
    void getAllCheckoutsByDescontoGeralIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where descontoGeral is greater than
        defaultCheckoutFiltering(
            "descontoGeral.greaterThan=" + SMALLER_DESCONTO_GERAL,
            "descontoGeral.greaterThan=" + DEFAULT_DESCONTO_GERAL
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByTipoDescontoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where tipoDesconto equals to
        defaultCheckoutFiltering("tipoDesconto.equals=" + DEFAULT_TIPO_DESCONTO, "tipoDesconto.equals=" + UPDATED_TIPO_DESCONTO);
    }

    @Test
    @Transactional
    void getAllCheckoutsByTipoDescontoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where tipoDesconto in
        defaultCheckoutFiltering(
            "tipoDesconto.in=" + DEFAULT_TIPO_DESCONTO + "," + UPDATED_TIPO_DESCONTO,
            "tipoDesconto.in=" + UPDATED_TIPO_DESCONTO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByTipoDescontoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where tipoDesconto is not null
        defaultCheckoutFiltering("tipoDesconto.specified=true", "tipoDesconto.specified=false");
    }

    @Test
    @Transactional
    void getAllCheckoutsByContabilizadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where contabilizado equals to
        defaultCheckoutFiltering("contabilizado.equals=" + DEFAULT_CONTABILIZADO, "contabilizado.equals=" + UPDATED_CONTABILIZADO);
    }

    @Test
    @Transactional
    void getAllCheckoutsByContabilizadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where contabilizado in
        defaultCheckoutFiltering(
            "contabilizado.in=" + DEFAULT_CONTABILIZADO + "," + UPDATED_CONTABILIZADO,
            "contabilizado.in=" + UPDATED_CONTABILIZADO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByContabilizadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where contabilizado is not null
        defaultCheckoutFiltering("contabilizado.specified=true", "contabilizado.specified=false");
    }

    @Test
    @Transactional
    void getAllCheckoutsByNomeUsuarioFixoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where nomeUsuarioFixo equals to
        defaultCheckoutFiltering(
            "nomeUsuarioFixo.equals=" + DEFAULT_NOME_USUARIO_FIXO,
            "nomeUsuarioFixo.equals=" + UPDATED_NOME_USUARIO_FIXO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByNomeUsuarioFixoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where nomeUsuarioFixo in
        defaultCheckoutFiltering(
            "nomeUsuarioFixo.in=" + DEFAULT_NOME_USUARIO_FIXO + "," + UPDATED_NOME_USUARIO_FIXO,
            "nomeUsuarioFixo.in=" + UPDATED_NOME_USUARIO_FIXO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByNomeUsuarioFixoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where nomeUsuarioFixo is not null
        defaultCheckoutFiltering("nomeUsuarioFixo.specified=true", "nomeUsuarioFixo.specified=false");
    }

    @Test
    @Transactional
    void getAllCheckoutsByNomeUsuarioFixoContainsSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where nomeUsuarioFixo contains
        defaultCheckoutFiltering(
            "nomeUsuarioFixo.contains=" + DEFAULT_NOME_USUARIO_FIXO,
            "nomeUsuarioFixo.contains=" + UPDATED_NOME_USUARIO_FIXO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByNomeUsuarioFixoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where nomeUsuarioFixo does not contain
        defaultCheckoutFiltering(
            "nomeUsuarioFixo.doesNotContain=" + UPDATED_NOME_USUARIO_FIXO,
            "nomeUsuarioFixo.doesNotContain=" + DEFAULT_NOME_USUARIO_FIXO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByTokenCheckoutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where tokenCheckout equals to
        defaultCheckoutFiltering("tokenCheckout.equals=" + DEFAULT_TOKEN_CHECKOUT, "tokenCheckout.equals=" + UPDATED_TOKEN_CHECKOUT);
    }

    @Test
    @Transactional
    void getAllCheckoutsByTokenCheckoutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where tokenCheckout in
        defaultCheckoutFiltering(
            "tokenCheckout.in=" + DEFAULT_TOKEN_CHECKOUT + "," + UPDATED_TOKEN_CHECKOUT,
            "tokenCheckout.in=" + UPDATED_TOKEN_CHECKOUT
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByTokenCheckoutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where tokenCheckout is not null
        defaultCheckoutFiltering("tokenCheckout.specified=true", "tokenCheckout.specified=false");
    }

    @Test
    @Transactional
    void getAllCheckoutsByTokenCheckoutContainsSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where tokenCheckout contains
        defaultCheckoutFiltering("tokenCheckout.contains=" + DEFAULT_TOKEN_CHECKOUT, "tokenCheckout.contains=" + UPDATED_TOKEN_CHECKOUT);
    }

    @Test
    @Transactional
    void getAllCheckoutsByTokenCheckoutNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where tokenCheckout does not contain
        defaultCheckoutFiltering(
            "tokenCheckout.doesNotContain=" + UPDATED_TOKEN_CHECKOUT,
            "tokenCheckout.doesNotContain=" + DEFAULT_TOKEN_CHECKOUT
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByIdExternoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where idExterno equals to
        defaultCheckoutFiltering("idExterno.equals=" + DEFAULT_ID_EXTERNO, "idExterno.equals=" + UPDATED_ID_EXTERNO);
    }

    @Test
    @Transactional
    void getAllCheckoutsByIdExternoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where idExterno in
        defaultCheckoutFiltering("idExterno.in=" + DEFAULT_ID_EXTERNO + "," + UPDATED_ID_EXTERNO, "idExterno.in=" + UPDATED_ID_EXTERNO);
    }

    @Test
    @Transactional
    void getAllCheckoutsByIdExternoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where idExterno is not null
        defaultCheckoutFiltering("idExterno.specified=true", "idExterno.specified=false");
    }

    @Test
    @Transactional
    void getAllCheckoutsByIdExternoContainsSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where idExterno contains
        defaultCheckoutFiltering("idExterno.contains=" + DEFAULT_ID_EXTERNO, "idExterno.contains=" + UPDATED_ID_EXTERNO);
    }

    @Test
    @Transactional
    void getAllCheckoutsByIdExternoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where idExterno does not contain
        defaultCheckoutFiltering("idExterno.doesNotContain=" + UPDATED_ID_EXTERNO, "idExterno.doesNotContain=" + DEFAULT_ID_EXTERNO);
    }

    @Test
    @Transactional
    void getAllCheckoutsByWebhookNotificadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where webhookNotificado equals to
        defaultCheckoutFiltering(
            "webhookNotificado.equals=" + DEFAULT_WEBHOOK_NOTIFICADO,
            "webhookNotificado.equals=" + UPDATED_WEBHOOK_NOTIFICADO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByWebhookNotificadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where webhookNotificado in
        defaultCheckoutFiltering(
            "webhookNotificado.in=" + DEFAULT_WEBHOOK_NOTIFICADO + "," + UPDATED_WEBHOOK_NOTIFICADO,
            "webhookNotificado.in=" + UPDATED_WEBHOOK_NOTIFICADO
        );
    }

    @Test
    @Transactional
    void getAllCheckoutsByWebhookNotificadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        // Get all the checkoutList where webhookNotificado is not null
        defaultCheckoutFiltering("webhookNotificado.specified=true", "webhookNotificado.specified=false");
    }

    @Test
    @Transactional
    void getAllCheckoutsByTransacaoIsEqualToSomething() throws Exception {
        Transacao transacao;
        if (TestUtil.findAll(em, Transacao.class).isEmpty()) {
            checkoutRepository.saveAndFlush(checkout);
            transacao = TransacaoResourceIT.createEntity();
        } else {
            transacao = TestUtil.findAll(em, Transacao.class).get(0);
        }
        em.persist(transacao);
        em.flush();
        checkout.setTransacao(transacao);
        checkoutRepository.saveAndFlush(checkout);
        Long transacaoId = transacao.getId();
        // Get all the checkoutList where transacao equals to transacaoId
        defaultCheckoutShouldBeFound("transacaoId.equals=" + transacaoId);

        // Get all the checkoutList where transacao equals to (transacaoId + 1)
        defaultCheckoutShouldNotBeFound("transacaoId.equals=" + (transacaoId + 1));
    }

    @Test
    @Transactional
    void getAllCheckoutsByUsuarioIsEqualToSomething() throws Exception {
        User usuario;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            checkoutRepository.saveAndFlush(checkout);
            usuario = UserResourceIT.createEntity();
        } else {
            usuario = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        checkout.setUsuario(usuario);
        checkoutRepository.saveAndFlush(checkout);
        Long usuarioId = usuario.getId();
        // Get all the checkoutList where usuario equals to usuarioId
        defaultCheckoutShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the checkoutList where usuario equals to (usuarioId + 1)
        defaultCheckoutShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    @Test
    @Transactional
    void getAllCheckoutsByCarteiraIsEqualToSomething() throws Exception {
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            checkoutRepository.saveAndFlush(checkout);
            carteira = CarteiraResourceIT.createEntity(em);
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        em.persist(carteira);
        em.flush();
        checkout.setCarteira(carteira);
        checkoutRepository.saveAndFlush(checkout);
        Long carteiraId = carteira.getId();
        // Get all the checkoutList where carteira equals to carteiraId
        defaultCheckoutShouldBeFound("carteiraId.equals=" + carteiraId);

        // Get all the checkoutList where carteira equals to (carteiraId + 1)
        defaultCheckoutShouldNotBeFound("carteiraId.equals=" + (carteiraId + 1));
    }

    @Test
    @Transactional
    void getAllCheckoutsByCarteiraCreditadaIsEqualToSomething() throws Exception {
        Carteira carteiraCreditada;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            checkoutRepository.saveAndFlush(checkout);
            carteiraCreditada = CarteiraResourceIT.createEntity(em);
        } else {
            carteiraCreditada = TestUtil.findAll(em, Carteira.class).get(0);
        }
        em.persist(carteiraCreditada);
        em.flush();
        checkout.setCarteiraCreditada(carteiraCreditada);
        checkoutRepository.saveAndFlush(checkout);
        Long carteiraCreditadaId = carteiraCreditada.getId();
        // Get all the checkoutList where carteiraCreditada equals to carteiraCreditadaId
        defaultCheckoutShouldBeFound("carteiraCreditadaId.equals=" + carteiraCreditadaId);

        // Get all the checkoutList where carteiraCreditada equals to (carteiraCreditadaId + 1)
        defaultCheckoutShouldNotBeFound("carteiraCreditadaId.equals=" + (carteiraCreditadaId + 1));
    }

    @Test
    @Transactional
    void getAllCheckoutsByMoedaCarteiraIsEqualToSomething() throws Exception {
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            checkoutRepository.saveAndFlush(checkout);
            moedaCarteira = MoedaCarteiraResourceIT.createEntity(em);
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        em.persist(moedaCarteira);
        em.flush();
        checkout.setMoedaCarteira(moedaCarteira);
        checkoutRepository.saveAndFlush(checkout);
        Long moedaCarteiraId = moedaCarteira.getId();
        // Get all the checkoutList where moedaCarteira equals to moedaCarteiraId
        defaultCheckoutShouldBeFound("moedaCarteiraId.equals=" + moedaCarteiraId);

        // Get all the checkoutList where moedaCarteira equals to (moedaCarteiraId + 1)
        defaultCheckoutShouldNotBeFound("moedaCarteiraId.equals=" + (moedaCarteiraId + 1));
    }

    @Test
    @Transactional
    void getAllCheckoutsByLinkPagamentoIsEqualToSomething() throws Exception {
        LinkPagamento linkPagamento;
        if (TestUtil.findAll(em, LinkPagamento.class).isEmpty()) {
            checkoutRepository.saveAndFlush(checkout);
            linkPagamento = LinkPagamentoResourceIT.createEntity(em);
        } else {
            linkPagamento = TestUtil.findAll(em, LinkPagamento.class).get(0);
        }
        em.persist(linkPagamento);
        em.flush();
        checkout.setLinkPagamento(linkPagamento);
        checkoutRepository.saveAndFlush(checkout);
        Long linkPagamentoId = linkPagamento.getId();
        // Get all the checkoutList where linkPagamento equals to linkPagamentoId
        defaultCheckoutShouldBeFound("linkPagamentoId.equals=" + linkPagamentoId);

        // Get all the checkoutList where linkPagamento equals to (linkPagamentoId + 1)
        defaultCheckoutShouldNotBeFound("linkPagamentoId.equals=" + (linkPagamentoId + 1));
    }

    private void defaultCheckoutFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCheckoutShouldBeFound(shouldBeFound);
        defaultCheckoutShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCheckoutShouldBeFound(String filter) throws Exception {
        restCheckoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(checkout.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].valorCreditado").value(hasItem(sameNumber(DEFAULT_VALOR_CREDITADO))))
            .andExpect(jsonPath("$.[*].valorCreditadoCarteira").value(hasItem(sameNumber(DEFAULT_VALOR_CREDITADO_CARTEIRA))))
            .andExpect(jsonPath("$.[*].idProdutoExterno").value(hasItem(DEFAULT_ID_PRODUTO_EXTERNO)))
            .andExpect(jsonPath("$.[*].nomeProdutoExterno").value(hasItem(DEFAULT_NOME_PRODUTO_EXTERNO)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].descontoGeral").value(hasItem(sameNumber(DEFAULT_DESCONTO_GERAL))))
            .andExpect(jsonPath("$.[*].tipoDesconto").value(hasItem(DEFAULT_TIPO_DESCONTO.toString())))
            .andExpect(jsonPath("$.[*].contabilizado").value(hasItem(DEFAULT_CONTABILIZADO)))
            .andExpect(jsonPath("$.[*].nomeUsuarioFixo").value(hasItem(DEFAULT_NOME_USUARIO_FIXO)))
            .andExpect(jsonPath("$.[*].tokenCheckout").value(hasItem(DEFAULT_TOKEN_CHECKOUT)))
            .andExpect(jsonPath("$.[*].idExterno").value(hasItem(DEFAULT_ID_EXTERNO)))
            .andExpect(jsonPath("$.[*].webhookNotificado").value(hasItem(DEFAULT_WEBHOOK_NOTIFICADO)));

        // Check, that the count call also returns 1
        restCheckoutMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCheckoutShouldNotBeFound(String filter) throws Exception {
        restCheckoutMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCheckoutMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCheckout() throws Exception {
        // Get the checkout
        restCheckoutMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCheckout() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the checkout
        Checkout updatedCheckout = checkoutRepository.findById(checkout.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCheckout are not directly saved in db
        em.detach(updatedCheckout);
        updatedCheckout
            .valor(UPDATED_VALOR)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .valorCreditadoCarteira(UPDATED_VALOR_CREDITADO_CARTEIRA)
            .idProdutoExterno(UPDATED_ID_PRODUTO_EXTERNO)
            .nomeProdutoExterno(UPDATED_NOME_PRODUTO_EXTERNO)
            .status(UPDATED_STATUS)
            .tipo(UPDATED_TIPO)
            .descontoGeral(UPDATED_DESCONTO_GERAL)
            .tipoDesconto(UPDATED_TIPO_DESCONTO)
            .contabilizado(UPDATED_CONTABILIZADO)
            .nomeUsuarioFixo(UPDATED_NOME_USUARIO_FIXO)
            .tokenCheckout(UPDATED_TOKEN_CHECKOUT)
            .idExterno(UPDATED_ID_EXTERNO)
            .webhookNotificado(UPDATED_WEBHOOK_NOTIFICADO);
        CheckoutDTO checkoutDTO = checkoutMapper.toDto(updatedCheckout);

        restCheckoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, checkoutDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(checkoutDTO))
            )
            .andExpect(status().isOk());

        // Validate the Checkout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCheckoutToMatchAllProperties(updatedCheckout);
    }

    @Test
    @Transactional
    void putNonExistingCheckout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkout.setId(longCount.incrementAndGet());

        // Create the Checkout
        CheckoutDTO checkoutDTO = checkoutMapper.toDto(checkout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCheckoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, checkoutDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(checkoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Checkout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCheckout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkout.setId(longCount.incrementAndGet());

        // Create the Checkout
        CheckoutDTO checkoutDTO = checkoutMapper.toDto(checkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCheckoutMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(checkoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Checkout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCheckout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkout.setId(longCount.incrementAndGet());

        // Create the Checkout
        CheckoutDTO checkoutDTO = checkoutMapper.toDto(checkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCheckoutMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(checkoutDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Checkout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCheckoutWithPatch() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the checkout using partial update
        Checkout partialUpdatedCheckout = new Checkout();
        partialUpdatedCheckout.setId(checkout.getId());

        partialUpdatedCheckout
            .idProdutoExterno(UPDATED_ID_PRODUTO_EXTERNO)
            .nomeProdutoExterno(UPDATED_NOME_PRODUTO_EXTERNO)
            .status(UPDATED_STATUS)
            .tokenCheckout(UPDATED_TOKEN_CHECKOUT)
            .idExterno(UPDATED_ID_EXTERNO)
            .webhookNotificado(UPDATED_WEBHOOK_NOTIFICADO);

        restCheckoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCheckout.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCheckout))
            )
            .andExpect(status().isOk());

        // Validate the Checkout in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCheckoutUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCheckout, checkout), getPersistedCheckout(checkout));
    }

    @Test
    @Transactional
    void fullUpdateCheckoutWithPatch() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the checkout using partial update
        Checkout partialUpdatedCheckout = new Checkout();
        partialUpdatedCheckout.setId(checkout.getId());

        partialUpdatedCheckout
            .valor(UPDATED_VALOR)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .valorCreditadoCarteira(UPDATED_VALOR_CREDITADO_CARTEIRA)
            .idProdutoExterno(UPDATED_ID_PRODUTO_EXTERNO)
            .nomeProdutoExterno(UPDATED_NOME_PRODUTO_EXTERNO)
            .status(UPDATED_STATUS)
            .tipo(UPDATED_TIPO)
            .descontoGeral(UPDATED_DESCONTO_GERAL)
            .tipoDesconto(UPDATED_TIPO_DESCONTO)
            .contabilizado(UPDATED_CONTABILIZADO)
            .nomeUsuarioFixo(UPDATED_NOME_USUARIO_FIXO)
            .tokenCheckout(UPDATED_TOKEN_CHECKOUT)
            .idExterno(UPDATED_ID_EXTERNO)
            .webhookNotificado(UPDATED_WEBHOOK_NOTIFICADO);

        restCheckoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCheckout.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCheckout))
            )
            .andExpect(status().isOk());

        // Validate the Checkout in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCheckoutUpdatableFieldsEquals(partialUpdatedCheckout, getPersistedCheckout(partialUpdatedCheckout));
    }

    @Test
    @Transactional
    void patchNonExistingCheckout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkout.setId(longCount.incrementAndGet());

        // Create the Checkout
        CheckoutDTO checkoutDTO = checkoutMapper.toDto(checkout);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCheckoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, checkoutDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(checkoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Checkout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCheckout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkout.setId(longCount.incrementAndGet());

        // Create the Checkout
        CheckoutDTO checkoutDTO = checkoutMapper.toDto(checkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCheckoutMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(checkoutDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Checkout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCheckout() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        checkout.setId(longCount.incrementAndGet());

        // Create the Checkout
        CheckoutDTO checkoutDTO = checkoutMapper.toDto(checkout);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCheckoutMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(checkoutDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Checkout in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCheckout() throws Exception {
        // Initialize the database
        insertedCheckout = checkoutRepository.saveAndFlush(checkout);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the checkout
        restCheckoutMockMvc
            .perform(delete(ENTITY_API_URL_ID, checkout.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return checkoutRepository.count();
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

    protected Checkout getPersistedCheckout(Checkout checkout) {
        return checkoutRepository.findById(checkout.getId()).orElseThrow();
    }

    protected void assertPersistedCheckoutToMatchAllProperties(Checkout expectedCheckout) {
        assertCheckoutAllPropertiesEquals(expectedCheckout, getPersistedCheckout(expectedCheckout));
    }

    protected void assertPersistedCheckoutToMatchUpdatableProperties(Checkout expectedCheckout) {
        assertCheckoutAllUpdatablePropertiesEquals(expectedCheckout, getPersistedCheckout(expectedCheckout));
    }
}
