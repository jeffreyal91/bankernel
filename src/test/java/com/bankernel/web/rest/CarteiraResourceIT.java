package com.bankernel.web.rest;

import static com.bankernel.domain.CarteiraAsserts.*;
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
import com.bankernel.domain.User;
import com.bankernel.repository.CarteiraRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.CarteiraService;
import com.bankernel.service.dto.CarteiraDTO;
import com.bankernel.service.mapper.CarteiraMapper;
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
 * Integration tests for the {@link CarteiraResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CarteiraResourceIT {

    private static final BigDecimal DEFAULT_SALDO = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALDO = new BigDecimal(2);
    private static final BigDecimal SMALLER_SALDO = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_LIMITE_NEGATIVO = new BigDecimal(1);
    private static final BigDecimal UPDATED_LIMITE_NEGATIVO = new BigDecimal(2);
    private static final BigDecimal SMALLER_LIMITE_NEGATIVO = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_VALOR_CONGELADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_CONGELADO = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR_CONGELADO = new BigDecimal(1 - 1);

    private static final String DEFAULT_NUMERO_CONTA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_CONTA = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVA = false;
    private static final Boolean UPDATED_ATIVA = true;

    private static final String ENTITY_API_URL = "/api/carteiras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CarteiraRepository carteiraRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private CarteiraRepository carteiraRepositoryMock;

    @Autowired
    private CarteiraMapper carteiraMapper;

    @Mock
    private CarteiraService carteiraServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCarteiraMockMvc;

    private Carteira carteira;

    private Carteira insertedCarteira;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Carteira createEntity(EntityManager em) {
        Carteira carteira = new Carteira()
            .saldo(DEFAULT_SALDO)
            .limiteNegativo(DEFAULT_LIMITE_NEGATIVO)
            .valorCongelado(DEFAULT_VALOR_CONGELADO)
            .numeroConta(DEFAULT_NUMERO_CONTA)
            .ativa(DEFAULT_ATIVA);
        // Add required entity
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            moedaCarteira = MoedaCarteiraResourceIT.createEntity(em);
            em.persist(moedaCarteira);
            em.flush();
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        carteira.setMoedaCarteira(moedaCarteira);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        carteira.setUsuario(user);
        return carteira;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Carteira createUpdatedEntity(EntityManager em) {
        Carteira updatedCarteira = new Carteira()
            .saldo(UPDATED_SALDO)
            .limiteNegativo(UPDATED_LIMITE_NEGATIVO)
            .valorCongelado(UPDATED_VALOR_CONGELADO)
            .numeroConta(UPDATED_NUMERO_CONTA)
            .ativa(UPDATED_ATIVA);
        // Add required entity
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            moedaCarteira = MoedaCarteiraResourceIT.createUpdatedEntity(em);
            em.persist(moedaCarteira);
            em.flush();
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        updatedCarteira.setMoedaCarteira(moedaCarteira);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedCarteira.setUsuario(user);
        return updatedCarteira;
    }

    @BeforeEach
    void initTest() {
        carteira = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedCarteira != null) {
            carteiraRepository.delete(insertedCarteira);
            insertedCarteira = null;
        }
    }

    @Test
    @Transactional
    void createCarteira() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Carteira
        CarteiraDTO carteiraDTO = carteiraMapper.toDto(carteira);
        var returnedCarteiraDTO = om.readValue(
            restCarteiraMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carteiraDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CarteiraDTO.class
        );

        // Validate the Carteira in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCarteira = carteiraMapper.toEntity(returnedCarteiraDTO);
        assertCarteiraUpdatableFieldsEquals(returnedCarteira, getPersistedCarteira(returnedCarteira));

        insertedCarteira = returnedCarteira;
    }

    @Test
    @Transactional
    void createCarteiraWithExistingId() throws Exception {
        // Create the Carteira with an existing ID
        carteira.setId(1L);
        CarteiraDTO carteiraDTO = carteiraMapper.toDto(carteira);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCarteiraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carteiraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Carteira in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSaldoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        carteira.setSaldo(null);

        // Create the Carteira, which fails.
        CarteiraDTO carteiraDTO = carteiraMapper.toDto(carteira);

        restCarteiraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carteiraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLimiteNegativoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        carteira.setLimiteNegativo(null);

        // Create the Carteira, which fails.
        CarteiraDTO carteiraDTO = carteiraMapper.toDto(carteira);

        restCarteiraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carteiraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorCongeladoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        carteira.setValorCongelado(null);

        // Create the Carteira, which fails.
        CarteiraDTO carteiraDTO = carteiraMapper.toDto(carteira);

        restCarteiraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carteiraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroContaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        carteira.setNumeroConta(null);

        // Create the Carteira, which fails.
        CarteiraDTO carteiraDTO = carteiraMapper.toDto(carteira);

        restCarteiraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carteiraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        carteira.setAtiva(null);

        // Create the Carteira, which fails.
        CarteiraDTO carteiraDTO = carteiraMapper.toDto(carteira);

        restCarteiraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carteiraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCarteiras() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList
        restCarteiraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carteira.getId().intValue())))
            .andExpect(jsonPath("$.[*].saldo").value(hasItem(sameNumber(DEFAULT_SALDO))))
            .andExpect(jsonPath("$.[*].limiteNegativo").value(hasItem(sameNumber(DEFAULT_LIMITE_NEGATIVO))))
            .andExpect(jsonPath("$.[*].valorCongelado").value(hasItem(sameNumber(DEFAULT_VALOR_CONGELADO))))
            .andExpect(jsonPath("$.[*].numeroConta").value(hasItem(DEFAULT_NUMERO_CONTA)))
            .andExpect(jsonPath("$.[*].ativa").value(hasItem(DEFAULT_ATIVA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarteirasWithEagerRelationshipsIsEnabled() throws Exception {
        when(carteiraServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarteiraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(carteiraServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCarteirasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(carteiraServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCarteiraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(carteiraRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCarteira() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get the carteira
        restCarteiraMockMvc
            .perform(get(ENTITY_API_URL_ID, carteira.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(carteira.getId().intValue()))
            .andExpect(jsonPath("$.saldo").value(sameNumber(DEFAULT_SALDO)))
            .andExpect(jsonPath("$.limiteNegativo").value(sameNumber(DEFAULT_LIMITE_NEGATIVO)))
            .andExpect(jsonPath("$.valorCongelado").value(sameNumber(DEFAULT_VALOR_CONGELADO)))
            .andExpect(jsonPath("$.numeroConta").value(DEFAULT_NUMERO_CONTA))
            .andExpect(jsonPath("$.ativa").value(DEFAULT_ATIVA));
    }

    @Test
    @Transactional
    void getCarteirasByIdFiltering() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        Long id = carteira.getId();

        defaultCarteiraFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCarteiraFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCarteiraFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCarteirasBySaldoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where saldo equals to
        defaultCarteiraFiltering("saldo.equals=" + DEFAULT_SALDO, "saldo.equals=" + UPDATED_SALDO);
    }

    @Test
    @Transactional
    void getAllCarteirasBySaldoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where saldo in
        defaultCarteiraFiltering("saldo.in=" + DEFAULT_SALDO + "," + UPDATED_SALDO, "saldo.in=" + UPDATED_SALDO);
    }

    @Test
    @Transactional
    void getAllCarteirasBySaldoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where saldo is not null
        defaultCarteiraFiltering("saldo.specified=true", "saldo.specified=false");
    }

    @Test
    @Transactional
    void getAllCarteirasBySaldoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where saldo is greater than or equal to
        defaultCarteiraFiltering("saldo.greaterThanOrEqual=" + DEFAULT_SALDO, "saldo.greaterThanOrEqual=" + UPDATED_SALDO);
    }

    @Test
    @Transactional
    void getAllCarteirasBySaldoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where saldo is less than or equal to
        defaultCarteiraFiltering("saldo.lessThanOrEqual=" + DEFAULT_SALDO, "saldo.lessThanOrEqual=" + SMALLER_SALDO);
    }

    @Test
    @Transactional
    void getAllCarteirasBySaldoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where saldo is less than
        defaultCarteiraFiltering("saldo.lessThan=" + UPDATED_SALDO, "saldo.lessThan=" + DEFAULT_SALDO);
    }

    @Test
    @Transactional
    void getAllCarteirasBySaldoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where saldo is greater than
        defaultCarteiraFiltering("saldo.greaterThan=" + SMALLER_SALDO, "saldo.greaterThan=" + DEFAULT_SALDO);
    }

    @Test
    @Transactional
    void getAllCarteirasByLimiteNegativoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where limiteNegativo equals to
        defaultCarteiraFiltering("limiteNegativo.equals=" + DEFAULT_LIMITE_NEGATIVO, "limiteNegativo.equals=" + UPDATED_LIMITE_NEGATIVO);
    }

    @Test
    @Transactional
    void getAllCarteirasByLimiteNegativoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where limiteNegativo in
        defaultCarteiraFiltering(
            "limiteNegativo.in=" + DEFAULT_LIMITE_NEGATIVO + "," + UPDATED_LIMITE_NEGATIVO,
            "limiteNegativo.in=" + UPDATED_LIMITE_NEGATIVO
        );
    }

    @Test
    @Transactional
    void getAllCarteirasByLimiteNegativoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where limiteNegativo is not null
        defaultCarteiraFiltering("limiteNegativo.specified=true", "limiteNegativo.specified=false");
    }

    @Test
    @Transactional
    void getAllCarteirasByLimiteNegativoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where limiteNegativo is greater than or equal to
        defaultCarteiraFiltering(
            "limiteNegativo.greaterThanOrEqual=" + DEFAULT_LIMITE_NEGATIVO,
            "limiteNegativo.greaterThanOrEqual=" + UPDATED_LIMITE_NEGATIVO
        );
    }

    @Test
    @Transactional
    void getAllCarteirasByLimiteNegativoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where limiteNegativo is less than or equal to
        defaultCarteiraFiltering(
            "limiteNegativo.lessThanOrEqual=" + DEFAULT_LIMITE_NEGATIVO,
            "limiteNegativo.lessThanOrEqual=" + SMALLER_LIMITE_NEGATIVO
        );
    }

    @Test
    @Transactional
    void getAllCarteirasByLimiteNegativoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where limiteNegativo is less than
        defaultCarteiraFiltering(
            "limiteNegativo.lessThan=" + UPDATED_LIMITE_NEGATIVO,
            "limiteNegativo.lessThan=" + DEFAULT_LIMITE_NEGATIVO
        );
    }

    @Test
    @Transactional
    void getAllCarteirasByLimiteNegativoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where limiteNegativo is greater than
        defaultCarteiraFiltering(
            "limiteNegativo.greaterThan=" + SMALLER_LIMITE_NEGATIVO,
            "limiteNegativo.greaterThan=" + DEFAULT_LIMITE_NEGATIVO
        );
    }

    @Test
    @Transactional
    void getAllCarteirasByValorCongeladoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where valorCongelado equals to
        defaultCarteiraFiltering("valorCongelado.equals=" + DEFAULT_VALOR_CONGELADO, "valorCongelado.equals=" + UPDATED_VALOR_CONGELADO);
    }

    @Test
    @Transactional
    void getAllCarteirasByValorCongeladoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where valorCongelado in
        defaultCarteiraFiltering(
            "valorCongelado.in=" + DEFAULT_VALOR_CONGELADO + "," + UPDATED_VALOR_CONGELADO,
            "valorCongelado.in=" + UPDATED_VALOR_CONGELADO
        );
    }

    @Test
    @Transactional
    void getAllCarteirasByValorCongeladoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where valorCongelado is not null
        defaultCarteiraFiltering("valorCongelado.specified=true", "valorCongelado.specified=false");
    }

    @Test
    @Transactional
    void getAllCarteirasByValorCongeladoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where valorCongelado is greater than or equal to
        defaultCarteiraFiltering(
            "valorCongelado.greaterThanOrEqual=" + DEFAULT_VALOR_CONGELADO,
            "valorCongelado.greaterThanOrEqual=" + UPDATED_VALOR_CONGELADO
        );
    }

    @Test
    @Transactional
    void getAllCarteirasByValorCongeladoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where valorCongelado is less than or equal to
        defaultCarteiraFiltering(
            "valorCongelado.lessThanOrEqual=" + DEFAULT_VALOR_CONGELADO,
            "valorCongelado.lessThanOrEqual=" + SMALLER_VALOR_CONGELADO
        );
    }

    @Test
    @Transactional
    void getAllCarteirasByValorCongeladoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where valorCongelado is less than
        defaultCarteiraFiltering(
            "valorCongelado.lessThan=" + UPDATED_VALOR_CONGELADO,
            "valorCongelado.lessThan=" + DEFAULT_VALOR_CONGELADO
        );
    }

    @Test
    @Transactional
    void getAllCarteirasByValorCongeladoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where valorCongelado is greater than
        defaultCarteiraFiltering(
            "valorCongelado.greaterThan=" + SMALLER_VALOR_CONGELADO,
            "valorCongelado.greaterThan=" + DEFAULT_VALOR_CONGELADO
        );
    }

    @Test
    @Transactional
    void getAllCarteirasByNumeroContaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where numeroConta equals to
        defaultCarteiraFiltering("numeroConta.equals=" + DEFAULT_NUMERO_CONTA, "numeroConta.equals=" + UPDATED_NUMERO_CONTA);
    }

    @Test
    @Transactional
    void getAllCarteirasByNumeroContaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where numeroConta in
        defaultCarteiraFiltering(
            "numeroConta.in=" + DEFAULT_NUMERO_CONTA + "," + UPDATED_NUMERO_CONTA,
            "numeroConta.in=" + UPDATED_NUMERO_CONTA
        );
    }

    @Test
    @Transactional
    void getAllCarteirasByNumeroContaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where numeroConta is not null
        defaultCarteiraFiltering("numeroConta.specified=true", "numeroConta.specified=false");
    }

    @Test
    @Transactional
    void getAllCarteirasByNumeroContaContainsSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where numeroConta contains
        defaultCarteiraFiltering("numeroConta.contains=" + DEFAULT_NUMERO_CONTA, "numeroConta.contains=" + UPDATED_NUMERO_CONTA);
    }

    @Test
    @Transactional
    void getAllCarteirasByNumeroContaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where numeroConta does not contain
        defaultCarteiraFiltering(
            "numeroConta.doesNotContain=" + UPDATED_NUMERO_CONTA,
            "numeroConta.doesNotContain=" + DEFAULT_NUMERO_CONTA
        );
    }

    @Test
    @Transactional
    void getAllCarteirasByAtivaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where ativa equals to
        defaultCarteiraFiltering("ativa.equals=" + DEFAULT_ATIVA, "ativa.equals=" + UPDATED_ATIVA);
    }

    @Test
    @Transactional
    void getAllCarteirasByAtivaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where ativa in
        defaultCarteiraFiltering("ativa.in=" + DEFAULT_ATIVA + "," + UPDATED_ATIVA, "ativa.in=" + UPDATED_ATIVA);
    }

    @Test
    @Transactional
    void getAllCarteirasByAtivaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        // Get all the carteiraList where ativa is not null
        defaultCarteiraFiltering("ativa.specified=true", "ativa.specified=false");
    }

    @Test
    @Transactional
    void getAllCarteirasByMoedaCarteiraIsEqualToSomething() throws Exception {
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            carteiraRepository.saveAndFlush(carteira);
            moedaCarteira = MoedaCarteiraResourceIT.createEntity(em);
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        em.persist(moedaCarteira);
        em.flush();
        carteira.setMoedaCarteira(moedaCarteira);
        carteiraRepository.saveAndFlush(carteira);
        Long moedaCarteiraId = moedaCarteira.getId();
        // Get all the carteiraList where moedaCarteira equals to moedaCarteiraId
        defaultCarteiraShouldBeFound("moedaCarteiraId.equals=" + moedaCarteiraId);

        // Get all the carteiraList where moedaCarteira equals to (moedaCarteiraId + 1)
        defaultCarteiraShouldNotBeFound("moedaCarteiraId.equals=" + (moedaCarteiraId + 1));
    }

    @Test
    @Transactional
    void getAllCarteirasByUsuarioIsEqualToSomething() throws Exception {
        User usuario;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            carteiraRepository.saveAndFlush(carteira);
            usuario = UserResourceIT.createEntity();
        } else {
            usuario = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        carteira.setUsuario(usuario);
        carteiraRepository.saveAndFlush(carteira);
        Long usuarioId = usuario.getId();
        // Get all the carteiraList where usuario equals to usuarioId
        defaultCarteiraShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the carteiraList where usuario equals to (usuarioId + 1)
        defaultCarteiraShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    private void defaultCarteiraFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCarteiraShouldBeFound(shouldBeFound);
        defaultCarteiraShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCarteiraShouldBeFound(String filter) throws Exception {
        restCarteiraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carteira.getId().intValue())))
            .andExpect(jsonPath("$.[*].saldo").value(hasItem(sameNumber(DEFAULT_SALDO))))
            .andExpect(jsonPath("$.[*].limiteNegativo").value(hasItem(sameNumber(DEFAULT_LIMITE_NEGATIVO))))
            .andExpect(jsonPath("$.[*].valorCongelado").value(hasItem(sameNumber(DEFAULT_VALOR_CONGELADO))))
            .andExpect(jsonPath("$.[*].numeroConta").value(hasItem(DEFAULT_NUMERO_CONTA)))
            .andExpect(jsonPath("$.[*].ativa").value(hasItem(DEFAULT_ATIVA)));

        // Check, that the count call also returns 1
        restCarteiraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCarteiraShouldNotBeFound(String filter) throws Exception {
        restCarteiraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCarteiraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCarteira() throws Exception {
        // Get the carteira
        restCarteiraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCarteira() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carteira
        Carteira updatedCarteira = carteiraRepository.findById(carteira.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCarteira are not directly saved in db
        em.detach(updatedCarteira);
        updatedCarteira
            .saldo(UPDATED_SALDO)
            .limiteNegativo(UPDATED_LIMITE_NEGATIVO)
            .valorCongelado(UPDATED_VALOR_CONGELADO)
            .numeroConta(UPDATED_NUMERO_CONTA)
            .ativa(UPDATED_ATIVA);
        CarteiraDTO carteiraDTO = carteiraMapper.toDto(updatedCarteira);

        restCarteiraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carteiraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carteiraDTO))
            )
            .andExpect(status().isOk());

        // Validate the Carteira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCarteiraToMatchAllProperties(updatedCarteira);
    }

    @Test
    @Transactional
    void putNonExistingCarteira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carteira.setId(longCount.incrementAndGet());

        // Create the Carteira
        CarteiraDTO carteiraDTO = carteiraMapper.toDto(carteira);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarteiraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, carteiraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carteiraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carteira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCarteira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carteira.setId(longCount.incrementAndGet());

        // Create the Carteira
        CarteiraDTO carteiraDTO = carteiraMapper.toDto(carteira);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarteiraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(carteiraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carteira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCarteira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carteira.setId(longCount.incrementAndGet());

        // Create the Carteira
        CarteiraDTO carteiraDTO = carteiraMapper.toDto(carteira);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarteiraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(carteiraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Carteira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCarteiraWithPatch() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carteira using partial update
        Carteira partialUpdatedCarteira = new Carteira();
        partialUpdatedCarteira.setId(carteira.getId());

        partialUpdatedCarteira.ativa(UPDATED_ATIVA);

        restCarteiraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarteira.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarteira))
            )
            .andExpect(status().isOk());

        // Validate the Carteira in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarteiraUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedCarteira, carteira), getPersistedCarteira(carteira));
    }

    @Test
    @Transactional
    void fullUpdateCarteiraWithPatch() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the carteira using partial update
        Carteira partialUpdatedCarteira = new Carteira();
        partialUpdatedCarteira.setId(carteira.getId());

        partialUpdatedCarteira
            .saldo(UPDATED_SALDO)
            .limiteNegativo(UPDATED_LIMITE_NEGATIVO)
            .valorCongelado(UPDATED_VALOR_CONGELADO)
            .numeroConta(UPDATED_NUMERO_CONTA)
            .ativa(UPDATED_ATIVA);

        restCarteiraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCarteira.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCarteira))
            )
            .andExpect(status().isOk());

        // Validate the Carteira in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCarteiraUpdatableFieldsEquals(partialUpdatedCarteira, getPersistedCarteira(partialUpdatedCarteira));
    }

    @Test
    @Transactional
    void patchNonExistingCarteira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carteira.setId(longCount.incrementAndGet());

        // Create the Carteira
        CarteiraDTO carteiraDTO = carteiraMapper.toDto(carteira);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCarteiraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, carteiraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carteiraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carteira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCarteira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carteira.setId(longCount.incrementAndGet());

        // Create the Carteira
        CarteiraDTO carteiraDTO = carteiraMapper.toDto(carteira);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarteiraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(carteiraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Carteira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCarteira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        carteira.setId(longCount.incrementAndGet());

        // Create the Carteira
        CarteiraDTO carteiraDTO = carteiraMapper.toDto(carteira);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCarteiraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(carteiraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Carteira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCarteira() throws Exception {
        // Initialize the database
        insertedCarteira = carteiraRepository.saveAndFlush(carteira);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the carteira
        restCarteiraMockMvc
            .perform(delete(ENTITY_API_URL_ID, carteira.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return carteiraRepository.count();
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

    protected Carteira getPersistedCarteira(Carteira carteira) {
        return carteiraRepository.findById(carteira.getId()).orElseThrow();
    }

    protected void assertPersistedCarteiraToMatchAllProperties(Carteira expectedCarteira) {
        assertCarteiraAllPropertiesEquals(expectedCarteira, getPersistedCarteira(expectedCarteira));
    }

    protected void assertPersistedCarteiraToMatchUpdatableProperties(Carteira expectedCarteira) {
        assertCarteiraAllUpdatablePropertiesEquals(expectedCarteira, getPersistedCarteira(expectedCarteira));
    }
}
