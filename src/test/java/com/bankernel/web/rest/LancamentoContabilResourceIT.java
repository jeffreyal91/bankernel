package com.bankernel.web.rest;

import static com.bankernel.domain.LancamentoContabilAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bankernel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.ContaContabil;
import com.bankernel.domain.LancamentoContabil;
import com.bankernel.domain.Transacao;
import com.bankernel.domain.enumeration.EnumSinalLancamento;
import com.bankernel.domain.enumeration.EnumTipoLancamento;
import com.bankernel.repository.LancamentoContabilRepository;
import com.bankernel.service.LancamentoContabilService;
import com.bankernel.service.dto.LancamentoContabilDTO;
import com.bankernel.service.mapper.LancamentoContabilMapper;
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
 * Integration tests for the {@link LancamentoContabilResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LancamentoContabilResourceIT {

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR = new BigDecimal(1 - 1);

    private static final EnumTipoLancamento DEFAULT_TIPO_LANCAMENTO = EnumTipoLancamento.OPERACAO;
    private static final EnumTipoLancamento UPDATED_TIPO_LANCAMENTO = EnumTipoLancamento.ESTORNO;

    private static final EnumSinalLancamento DEFAULT_SINAL_LANCAMENTO = EnumSinalLancamento.DEBITO;
    private static final EnumSinalLancamento UPDATED_SINAL_LANCAMENTO = EnumSinalLancamento.CREDITO;

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final String ENTITY_API_URL = "/api/lancamento-contabils";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LancamentoContabilRepository lancamentoContabilRepository;

    @Mock
    private LancamentoContabilRepository lancamentoContabilRepositoryMock;

    @Autowired
    private LancamentoContabilMapper lancamentoContabilMapper;

    @Mock
    private LancamentoContabilService lancamentoContabilServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLancamentoContabilMockMvc;

    private LancamentoContabil lancamentoContabil;

    private LancamentoContabil insertedLancamentoContabil;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LancamentoContabil createEntity(EntityManager em) {
        LancamentoContabil lancamentoContabil = new LancamentoContabil()
            .valor(DEFAULT_VALOR)
            .tipoLancamento(DEFAULT_TIPO_LANCAMENTO)
            .sinalLancamento(DEFAULT_SINAL_LANCAMENTO)
            .ativo(DEFAULT_ATIVO);
        // Add required entity
        ContaContabil contaContabil;
        if (TestUtil.findAll(em, ContaContabil.class).isEmpty()) {
            contaContabil = ContaContabilResourceIT.createEntity(em);
            em.persist(contaContabil);
            em.flush();
        } else {
            contaContabil = TestUtil.findAll(em, ContaContabil.class).get(0);
        }
        lancamentoContabil.setContaContabil(contaContabil);
        return lancamentoContabil;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LancamentoContabil createUpdatedEntity(EntityManager em) {
        LancamentoContabil updatedLancamentoContabil = new LancamentoContabil()
            .valor(UPDATED_VALOR)
            .tipoLancamento(UPDATED_TIPO_LANCAMENTO)
            .sinalLancamento(UPDATED_SINAL_LANCAMENTO)
            .ativo(UPDATED_ATIVO);
        // Add required entity
        ContaContabil contaContabil;
        if (TestUtil.findAll(em, ContaContabil.class).isEmpty()) {
            contaContabil = ContaContabilResourceIT.createUpdatedEntity(em);
            em.persist(contaContabil);
            em.flush();
        } else {
            contaContabil = TestUtil.findAll(em, ContaContabil.class).get(0);
        }
        updatedLancamentoContabil.setContaContabil(contaContabil);
        return updatedLancamentoContabil;
    }

    @BeforeEach
    void initTest() {
        lancamentoContabil = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedLancamentoContabil != null) {
            lancamentoContabilRepository.delete(insertedLancamentoContabil);
            insertedLancamentoContabil = null;
        }
    }

    @Test
    @Transactional
    void createLancamentoContabil() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LancamentoContabil
        LancamentoContabilDTO lancamentoContabilDTO = lancamentoContabilMapper.toDto(lancamentoContabil);
        var returnedLancamentoContabilDTO = om.readValue(
            restLancamentoContabilMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lancamentoContabilDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LancamentoContabilDTO.class
        );

        // Validate the LancamentoContabil in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLancamentoContabil = lancamentoContabilMapper.toEntity(returnedLancamentoContabilDTO);
        assertLancamentoContabilUpdatableFieldsEquals(
            returnedLancamentoContabil,
            getPersistedLancamentoContabil(returnedLancamentoContabil)
        );

        insertedLancamentoContabil = returnedLancamentoContabil;
    }

    @Test
    @Transactional
    void createLancamentoContabilWithExistingId() throws Exception {
        // Create the LancamentoContabil with an existing ID
        lancamentoContabil.setId(1L);
        LancamentoContabilDTO lancamentoContabilDTO = lancamentoContabilMapper.toDto(lancamentoContabil);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLancamentoContabilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lancamentoContabilDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LancamentoContabil in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lancamentoContabil.setValor(null);

        // Create the LancamentoContabil, which fails.
        LancamentoContabilDTO lancamentoContabilDTO = lancamentoContabilMapper.toDto(lancamentoContabil);

        restLancamentoContabilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lancamentoContabilDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoLancamentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lancamentoContabil.setTipoLancamento(null);

        // Create the LancamentoContabil, which fails.
        LancamentoContabilDTO lancamentoContabilDTO = lancamentoContabilMapper.toDto(lancamentoContabil);

        restLancamentoContabilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lancamentoContabilDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSinalLancamentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lancamentoContabil.setSinalLancamento(null);

        // Create the LancamentoContabil, which fails.
        LancamentoContabilDTO lancamentoContabilDTO = lancamentoContabilMapper.toDto(lancamentoContabil);

        restLancamentoContabilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lancamentoContabilDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        lancamentoContabil.setAtivo(null);

        // Create the LancamentoContabil, which fails.
        LancamentoContabilDTO lancamentoContabilDTO = lancamentoContabilMapper.toDto(lancamentoContabil);

        restLancamentoContabilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lancamentoContabilDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLancamentoContabils() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList
        restLancamentoContabilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lancamentoContabil.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].tipoLancamento").value(hasItem(DEFAULT_TIPO_LANCAMENTO.toString())))
            .andExpect(jsonPath("$.[*].sinalLancamento").value(hasItem(DEFAULT_SINAL_LANCAMENTO.toString())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLancamentoContabilsWithEagerRelationshipsIsEnabled() throws Exception {
        when(lancamentoContabilServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLancamentoContabilMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(lancamentoContabilServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLancamentoContabilsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(lancamentoContabilServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLancamentoContabilMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(lancamentoContabilRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLancamentoContabil() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get the lancamentoContabil
        restLancamentoContabilMockMvc
            .perform(get(ENTITY_API_URL_ID, lancamentoContabil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(lancamentoContabil.getId().intValue()))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.tipoLancamento").value(DEFAULT_TIPO_LANCAMENTO.toString()))
            .andExpect(jsonPath("$.sinalLancamento").value(DEFAULT_SINAL_LANCAMENTO.toString()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO));
    }

    @Test
    @Transactional
    void getLancamentoContabilsByIdFiltering() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        Long id = lancamentoContabil.getId();

        defaultLancamentoContabilFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultLancamentoContabilFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultLancamentoContabilFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList where valor equals to
        defaultLancamentoContabilFiltering("valor.equals=" + DEFAULT_VALOR, "valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsByValorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList where valor in
        defaultLancamentoContabilFiltering("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR, "valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList where valor is not null
        defaultLancamentoContabilFiltering("valor.specified=true", "valor.specified=false");
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsByValorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList where valor is greater than or equal to
        defaultLancamentoContabilFiltering("valor.greaterThanOrEqual=" + DEFAULT_VALOR, "valor.greaterThanOrEqual=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsByValorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList where valor is less than or equal to
        defaultLancamentoContabilFiltering("valor.lessThanOrEqual=" + DEFAULT_VALOR, "valor.lessThanOrEqual=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsByValorIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList where valor is less than
        defaultLancamentoContabilFiltering("valor.lessThan=" + UPDATED_VALOR, "valor.lessThan=" + DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsByValorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList where valor is greater than
        defaultLancamentoContabilFiltering("valor.greaterThan=" + SMALLER_VALOR, "valor.greaterThan=" + DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsByTipoLancamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList where tipoLancamento equals to
        defaultLancamentoContabilFiltering(
            "tipoLancamento.equals=" + DEFAULT_TIPO_LANCAMENTO,
            "tipoLancamento.equals=" + UPDATED_TIPO_LANCAMENTO
        );
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsByTipoLancamentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList where tipoLancamento in
        defaultLancamentoContabilFiltering(
            "tipoLancamento.in=" + DEFAULT_TIPO_LANCAMENTO + "," + UPDATED_TIPO_LANCAMENTO,
            "tipoLancamento.in=" + UPDATED_TIPO_LANCAMENTO
        );
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsByTipoLancamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList where tipoLancamento is not null
        defaultLancamentoContabilFiltering("tipoLancamento.specified=true", "tipoLancamento.specified=false");
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsBySinalLancamentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList where sinalLancamento equals to
        defaultLancamentoContabilFiltering(
            "sinalLancamento.equals=" + DEFAULT_SINAL_LANCAMENTO,
            "sinalLancamento.equals=" + UPDATED_SINAL_LANCAMENTO
        );
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsBySinalLancamentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList where sinalLancamento in
        defaultLancamentoContabilFiltering(
            "sinalLancamento.in=" + DEFAULT_SINAL_LANCAMENTO + "," + UPDATED_SINAL_LANCAMENTO,
            "sinalLancamento.in=" + UPDATED_SINAL_LANCAMENTO
        );
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsBySinalLancamentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList where sinalLancamento is not null
        defaultLancamentoContabilFiltering("sinalLancamento.specified=true", "sinalLancamento.specified=false");
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsByAtivoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList where ativo equals to
        defaultLancamentoContabilFiltering("ativo.equals=" + DEFAULT_ATIVO, "ativo.equals=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsByAtivoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList where ativo in
        defaultLancamentoContabilFiltering("ativo.in=" + DEFAULT_ATIVO + "," + UPDATED_ATIVO, "ativo.in=" + UPDATED_ATIVO);
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsByAtivoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        // Get all the lancamentoContabilList where ativo is not null
        defaultLancamentoContabilFiltering("ativo.specified=true", "ativo.specified=false");
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsByTransacaoIsEqualToSomething() throws Exception {
        Transacao transacao;
        if (TestUtil.findAll(em, Transacao.class).isEmpty()) {
            lancamentoContabilRepository.saveAndFlush(lancamentoContabil);
            transacao = TransacaoResourceIT.createEntity();
        } else {
            transacao = TestUtil.findAll(em, Transacao.class).get(0);
        }
        em.persist(transacao);
        em.flush();
        lancamentoContabil.setTransacao(transacao);
        lancamentoContabilRepository.saveAndFlush(lancamentoContabil);
        Long transacaoId = transacao.getId();
        // Get all the lancamentoContabilList where transacao equals to transacaoId
        defaultLancamentoContabilShouldBeFound("transacaoId.equals=" + transacaoId);

        // Get all the lancamentoContabilList where transacao equals to (transacaoId + 1)
        defaultLancamentoContabilShouldNotBeFound("transacaoId.equals=" + (transacaoId + 1));
    }

    @Test
    @Transactional
    void getAllLancamentoContabilsByContaContabilIsEqualToSomething() throws Exception {
        ContaContabil contaContabil;
        if (TestUtil.findAll(em, ContaContabil.class).isEmpty()) {
            lancamentoContabilRepository.saveAndFlush(lancamentoContabil);
            contaContabil = ContaContabilResourceIT.createEntity(em);
        } else {
            contaContabil = TestUtil.findAll(em, ContaContabil.class).get(0);
        }
        em.persist(contaContabil);
        em.flush();
        lancamentoContabil.setContaContabil(contaContabil);
        lancamentoContabilRepository.saveAndFlush(lancamentoContabil);
        Long contaContabilId = contaContabil.getId();
        // Get all the lancamentoContabilList where contaContabil equals to contaContabilId
        defaultLancamentoContabilShouldBeFound("contaContabilId.equals=" + contaContabilId);

        // Get all the lancamentoContabilList where contaContabil equals to (contaContabilId + 1)
        defaultLancamentoContabilShouldNotBeFound("contaContabilId.equals=" + (contaContabilId + 1));
    }

    private void defaultLancamentoContabilFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultLancamentoContabilShouldBeFound(shouldBeFound);
        defaultLancamentoContabilShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultLancamentoContabilShouldBeFound(String filter) throws Exception {
        restLancamentoContabilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(lancamentoContabil.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].tipoLancamento").value(hasItem(DEFAULT_TIPO_LANCAMENTO.toString())))
            .andExpect(jsonPath("$.[*].sinalLancamento").value(hasItem(DEFAULT_SINAL_LANCAMENTO.toString())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)));

        // Check, that the count call also returns 1
        restLancamentoContabilMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultLancamentoContabilShouldNotBeFound(String filter) throws Exception {
        restLancamentoContabilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restLancamentoContabilMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingLancamentoContabil() throws Exception {
        // Get the lancamentoContabil
        restLancamentoContabilMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLancamentoContabil() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lancamentoContabil
        LancamentoContabil updatedLancamentoContabil = lancamentoContabilRepository.findById(lancamentoContabil.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLancamentoContabil are not directly saved in db
        em.detach(updatedLancamentoContabil);
        updatedLancamentoContabil
            .valor(UPDATED_VALOR)
            .tipoLancamento(UPDATED_TIPO_LANCAMENTO)
            .sinalLancamento(UPDATED_SINAL_LANCAMENTO)
            .ativo(UPDATED_ATIVO);
        LancamentoContabilDTO lancamentoContabilDTO = lancamentoContabilMapper.toDto(updatedLancamentoContabil);

        restLancamentoContabilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lancamentoContabilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lancamentoContabilDTO))
            )
            .andExpect(status().isOk());

        // Validate the LancamentoContabil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLancamentoContabilToMatchAllProperties(updatedLancamentoContabil);
    }

    @Test
    @Transactional
    void putNonExistingLancamentoContabil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lancamentoContabil.setId(longCount.incrementAndGet());

        // Create the LancamentoContabil
        LancamentoContabilDTO lancamentoContabilDTO = lancamentoContabilMapper.toDto(lancamentoContabil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLancamentoContabilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, lancamentoContabilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lancamentoContabilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LancamentoContabil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLancamentoContabil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lancamentoContabil.setId(longCount.incrementAndGet());

        // Create the LancamentoContabil
        LancamentoContabilDTO lancamentoContabilDTO = lancamentoContabilMapper.toDto(lancamentoContabil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLancamentoContabilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(lancamentoContabilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LancamentoContabil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLancamentoContabil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lancamentoContabil.setId(longCount.incrementAndGet());

        // Create the LancamentoContabil
        LancamentoContabilDTO lancamentoContabilDTO = lancamentoContabilMapper.toDto(lancamentoContabil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLancamentoContabilMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(lancamentoContabilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LancamentoContabil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLancamentoContabilWithPatch() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lancamentoContabil using partial update
        LancamentoContabil partialUpdatedLancamentoContabil = new LancamentoContabil();
        partialUpdatedLancamentoContabil.setId(lancamentoContabil.getId());

        partialUpdatedLancamentoContabil.ativo(UPDATED_ATIVO);

        restLancamentoContabilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLancamentoContabil.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLancamentoContabil))
            )
            .andExpect(status().isOk());

        // Validate the LancamentoContabil in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLancamentoContabilUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLancamentoContabil, lancamentoContabil),
            getPersistedLancamentoContabil(lancamentoContabil)
        );
    }

    @Test
    @Transactional
    void fullUpdateLancamentoContabilWithPatch() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the lancamentoContabil using partial update
        LancamentoContabil partialUpdatedLancamentoContabil = new LancamentoContabil();
        partialUpdatedLancamentoContabil.setId(lancamentoContabil.getId());

        partialUpdatedLancamentoContabil
            .valor(UPDATED_VALOR)
            .tipoLancamento(UPDATED_TIPO_LANCAMENTO)
            .sinalLancamento(UPDATED_SINAL_LANCAMENTO)
            .ativo(UPDATED_ATIVO);

        restLancamentoContabilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLancamentoContabil.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLancamentoContabil))
            )
            .andExpect(status().isOk());

        // Validate the LancamentoContabil in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLancamentoContabilUpdatableFieldsEquals(
            partialUpdatedLancamentoContabil,
            getPersistedLancamentoContabil(partialUpdatedLancamentoContabil)
        );
    }

    @Test
    @Transactional
    void patchNonExistingLancamentoContabil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lancamentoContabil.setId(longCount.incrementAndGet());

        // Create the LancamentoContabil
        LancamentoContabilDTO lancamentoContabilDTO = lancamentoContabilMapper.toDto(lancamentoContabil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLancamentoContabilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, lancamentoContabilDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lancamentoContabilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LancamentoContabil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLancamentoContabil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lancamentoContabil.setId(longCount.incrementAndGet());

        // Create the LancamentoContabil
        LancamentoContabilDTO lancamentoContabilDTO = lancamentoContabilMapper.toDto(lancamentoContabil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLancamentoContabilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(lancamentoContabilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LancamentoContabil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLancamentoContabil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        lancamentoContabil.setId(longCount.incrementAndGet());

        // Create the LancamentoContabil
        LancamentoContabilDTO lancamentoContabilDTO = lancamentoContabilMapper.toDto(lancamentoContabil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLancamentoContabilMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(lancamentoContabilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LancamentoContabil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLancamentoContabil() throws Exception {
        // Initialize the database
        insertedLancamentoContabil = lancamentoContabilRepository.saveAndFlush(lancamentoContabil);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the lancamentoContabil
        restLancamentoContabilMockMvc
            .perform(delete(ENTITY_API_URL_ID, lancamentoContabil.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return lancamentoContabilRepository.count();
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

    protected LancamentoContabil getPersistedLancamentoContabil(LancamentoContabil lancamentoContabil) {
        return lancamentoContabilRepository.findById(lancamentoContabil.getId()).orElseThrow();
    }

    protected void assertPersistedLancamentoContabilToMatchAllProperties(LancamentoContabil expectedLancamentoContabil) {
        assertLancamentoContabilAllPropertiesEquals(expectedLancamentoContabil, getPersistedLancamentoContabil(expectedLancamentoContabil));
    }

    protected void assertPersistedLancamentoContabilToMatchUpdatableProperties(LancamentoContabil expectedLancamentoContabil) {
        assertLancamentoContabilAllUpdatablePropertiesEquals(
            expectedLancamentoContabil,
            getPersistedLancamentoContabil(expectedLancamentoContabil)
        );
    }
}
