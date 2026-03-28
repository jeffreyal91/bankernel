package com.bankernel.web.rest;

import static com.bankernel.domain.ContaContabilAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bankernel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.ContaContabil;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.enumeration.EnumCategoriaContaContabil;
import com.bankernel.domain.enumeration.EnumTipoContaContabil;
import com.bankernel.repository.ContaContabilRepository;
import com.bankernel.service.ContaContabilService;
import com.bankernel.service.dto.ContaContabilDTO;
import com.bankernel.service.mapper.ContaContabilMapper;
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
 * Integration tests for the {@link ContaContabilResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContaContabilResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_SALDO = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALDO = new BigDecimal(2);
    private static final BigDecimal SMALLER_SALDO = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final EnumTipoContaContabil DEFAULT_TIPO_CONTA_CONTABIL = EnumTipoContaContabil.ATIVO;
    private static final EnumTipoContaContabil UPDATED_TIPO_CONTA_CONTABIL = EnumTipoContaContabil.PASSIVO;

    private static final EnumCategoriaContaContabil DEFAULT_CATEGORIA_CONTA_CONTABIL = EnumCategoriaContaContabil.OPERACIONAL;
    private static final EnumCategoriaContaContabil UPDATED_CATEGORIA_CONTA_CONTABIL = EnumCategoriaContaContabil.FINANCEIRA;

    private static final Boolean DEFAULT_ATIVA = false;
    private static final Boolean UPDATED_ATIVA = true;

    private static final String ENTITY_API_URL = "/api/conta-contabils";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContaContabilRepository contaContabilRepository;

    @Mock
    private ContaContabilRepository contaContabilRepositoryMock;

    @Autowired
    private ContaContabilMapper contaContabilMapper;

    @Mock
    private ContaContabilService contaContabilServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContaContabilMockMvc;

    private ContaContabil contaContabil;

    private ContaContabil insertedContaContabil;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContaContabil createEntity(EntityManager em) {
        ContaContabil contaContabil = new ContaContabil()
            .codigo(DEFAULT_CODIGO)
            .nome(DEFAULT_NOME)
            .saldo(DEFAULT_SALDO)
            .descricao(DEFAULT_DESCRICAO)
            .tipoContaContabil(DEFAULT_TIPO_CONTA_CONTABIL)
            .categoriaContaContabil(DEFAULT_CATEGORIA_CONTA_CONTABIL)
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
        contaContabil.setMoedaCarteira(moedaCarteira);
        return contaContabil;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContaContabil createUpdatedEntity(EntityManager em) {
        ContaContabil updatedContaContabil = new ContaContabil()
            .codigo(UPDATED_CODIGO)
            .nome(UPDATED_NOME)
            .saldo(UPDATED_SALDO)
            .descricao(UPDATED_DESCRICAO)
            .tipoContaContabil(UPDATED_TIPO_CONTA_CONTABIL)
            .categoriaContaContabil(UPDATED_CATEGORIA_CONTA_CONTABIL)
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
        updatedContaContabil.setMoedaCarteira(moedaCarteira);
        return updatedContaContabil;
    }

    @BeforeEach
    void initTest() {
        contaContabil = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedContaContabil != null) {
            contaContabilRepository.delete(insertedContaContabil);
            insertedContaContabil = null;
        }
    }

    @Test
    @Transactional
    void createContaContabil() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContaContabil
        ContaContabilDTO contaContabilDTO = contaContabilMapper.toDto(contaContabil);
        var returnedContaContabilDTO = om.readValue(
            restContaContabilMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contaContabilDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContaContabilDTO.class
        );

        // Validate the ContaContabil in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedContaContabil = contaContabilMapper.toEntity(returnedContaContabilDTO);
        assertContaContabilUpdatableFieldsEquals(returnedContaContabil, getPersistedContaContabil(returnedContaContabil));

        insertedContaContabil = returnedContaContabil;
    }

    @Test
    @Transactional
    void createContaContabilWithExistingId() throws Exception {
        // Create the ContaContabil with an existing ID
        contaContabil.setId(1L);
        ContaContabilDTO contaContabilDTO = contaContabilMapper.toDto(contaContabil);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContaContabilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contaContabilDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContaContabil in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contaContabil.setCodigo(null);

        // Create the ContaContabil, which fails.
        ContaContabilDTO contaContabilDTO = contaContabilMapper.toDto(contaContabil);

        restContaContabilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contaContabilDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contaContabil.setNome(null);

        // Create the ContaContabil, which fails.
        ContaContabilDTO contaContabilDTO = contaContabilMapper.toDto(contaContabil);

        restContaContabilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contaContabilDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSaldoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contaContabil.setSaldo(null);

        // Create the ContaContabil, which fails.
        ContaContabilDTO contaContabilDTO = contaContabilMapper.toDto(contaContabil);

        restContaContabilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contaContabilDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoContaContabilIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contaContabil.setTipoContaContabil(null);

        // Create the ContaContabil, which fails.
        ContaContabilDTO contaContabilDTO = contaContabilMapper.toDto(contaContabil);

        restContaContabilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contaContabilDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCategoriaContaContabilIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contaContabil.setCategoriaContaContabil(null);

        // Create the ContaContabil, which fails.
        ContaContabilDTO contaContabilDTO = contaContabilMapper.toDto(contaContabil);

        restContaContabilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contaContabilDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contaContabil.setAtiva(null);

        // Create the ContaContabil, which fails.
        ContaContabilDTO contaContabilDTO = contaContabilMapper.toDto(contaContabil);

        restContaContabilMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contaContabilDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContaContabils() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList
        restContaContabilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaContabil.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].saldo").value(hasItem(sameNumber(DEFAULT_SALDO))))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].tipoContaContabil").value(hasItem(DEFAULT_TIPO_CONTA_CONTABIL.toString())))
            .andExpect(jsonPath("$.[*].categoriaContaContabil").value(hasItem(DEFAULT_CATEGORIA_CONTA_CONTABIL.toString())))
            .andExpect(jsonPath("$.[*].ativa").value(hasItem(DEFAULT_ATIVA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContaContabilsWithEagerRelationshipsIsEnabled() throws Exception {
        when(contaContabilServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContaContabilMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contaContabilServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContaContabilsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contaContabilServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContaContabilMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contaContabilRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getContaContabil() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get the contaContabil
        restContaContabilMockMvc
            .perform(get(ENTITY_API_URL_ID, contaContabil.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contaContabil.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.saldo").value(sameNumber(DEFAULT_SALDO)))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.tipoContaContabil").value(DEFAULT_TIPO_CONTA_CONTABIL.toString()))
            .andExpect(jsonPath("$.categoriaContaContabil").value(DEFAULT_CATEGORIA_CONTA_CONTABIL.toString()))
            .andExpect(jsonPath("$.ativa").value(DEFAULT_ATIVA));
    }

    @Test
    @Transactional
    void getContaContabilsByIdFiltering() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        Long id = contaContabil.getId();

        defaultContaContabilFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultContaContabilFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultContaContabilFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContaContabilsByCodigoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where codigo equals to
        defaultContaContabilFiltering("codigo.equals=" + DEFAULT_CODIGO, "codigo.equals=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllContaContabilsByCodigoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where codigo in
        defaultContaContabilFiltering("codigo.in=" + DEFAULT_CODIGO + "," + UPDATED_CODIGO, "codigo.in=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllContaContabilsByCodigoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where codigo is not null
        defaultContaContabilFiltering("codigo.specified=true", "codigo.specified=false");
    }

    @Test
    @Transactional
    void getAllContaContabilsByCodigoContainsSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where codigo contains
        defaultContaContabilFiltering("codigo.contains=" + DEFAULT_CODIGO, "codigo.contains=" + UPDATED_CODIGO);
    }

    @Test
    @Transactional
    void getAllContaContabilsByCodigoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where codigo does not contain
        defaultContaContabilFiltering("codigo.doesNotContain=" + UPDATED_CODIGO, "codigo.doesNotContain=" + DEFAULT_CODIGO);
    }

    @Test
    @Transactional
    void getAllContaContabilsByNomeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where nome equals to
        defaultContaContabilFiltering("nome.equals=" + DEFAULT_NOME, "nome.equals=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllContaContabilsByNomeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where nome in
        defaultContaContabilFiltering("nome.in=" + DEFAULT_NOME + "," + UPDATED_NOME, "nome.in=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllContaContabilsByNomeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where nome is not null
        defaultContaContabilFiltering("nome.specified=true", "nome.specified=false");
    }

    @Test
    @Transactional
    void getAllContaContabilsByNomeContainsSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where nome contains
        defaultContaContabilFiltering("nome.contains=" + DEFAULT_NOME, "nome.contains=" + UPDATED_NOME);
    }

    @Test
    @Transactional
    void getAllContaContabilsByNomeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where nome does not contain
        defaultContaContabilFiltering("nome.doesNotContain=" + UPDATED_NOME, "nome.doesNotContain=" + DEFAULT_NOME);
    }

    @Test
    @Transactional
    void getAllContaContabilsBySaldoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where saldo equals to
        defaultContaContabilFiltering("saldo.equals=" + DEFAULT_SALDO, "saldo.equals=" + UPDATED_SALDO);
    }

    @Test
    @Transactional
    void getAllContaContabilsBySaldoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where saldo in
        defaultContaContabilFiltering("saldo.in=" + DEFAULT_SALDO + "," + UPDATED_SALDO, "saldo.in=" + UPDATED_SALDO);
    }

    @Test
    @Transactional
    void getAllContaContabilsBySaldoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where saldo is not null
        defaultContaContabilFiltering("saldo.specified=true", "saldo.specified=false");
    }

    @Test
    @Transactional
    void getAllContaContabilsBySaldoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where saldo is greater than or equal to
        defaultContaContabilFiltering("saldo.greaterThanOrEqual=" + DEFAULT_SALDO, "saldo.greaterThanOrEqual=" + UPDATED_SALDO);
    }

    @Test
    @Transactional
    void getAllContaContabilsBySaldoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where saldo is less than or equal to
        defaultContaContabilFiltering("saldo.lessThanOrEqual=" + DEFAULT_SALDO, "saldo.lessThanOrEqual=" + SMALLER_SALDO);
    }

    @Test
    @Transactional
    void getAllContaContabilsBySaldoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where saldo is less than
        defaultContaContabilFiltering("saldo.lessThan=" + UPDATED_SALDO, "saldo.lessThan=" + DEFAULT_SALDO);
    }

    @Test
    @Transactional
    void getAllContaContabilsBySaldoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where saldo is greater than
        defaultContaContabilFiltering("saldo.greaterThan=" + SMALLER_SALDO, "saldo.greaterThan=" + DEFAULT_SALDO);
    }

    @Test
    @Transactional
    void getAllContaContabilsByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where descricao equals to
        defaultContaContabilFiltering("descricao.equals=" + DEFAULT_DESCRICAO, "descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllContaContabilsByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where descricao in
        defaultContaContabilFiltering("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO, "descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllContaContabilsByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where descricao is not null
        defaultContaContabilFiltering("descricao.specified=true", "descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllContaContabilsByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where descricao contains
        defaultContaContabilFiltering("descricao.contains=" + DEFAULT_DESCRICAO, "descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllContaContabilsByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where descricao does not contain
        defaultContaContabilFiltering("descricao.doesNotContain=" + UPDATED_DESCRICAO, "descricao.doesNotContain=" + DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllContaContabilsByTipoContaContabilIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where tipoContaContabil equals to
        defaultContaContabilFiltering(
            "tipoContaContabil.equals=" + DEFAULT_TIPO_CONTA_CONTABIL,
            "tipoContaContabil.equals=" + UPDATED_TIPO_CONTA_CONTABIL
        );
    }

    @Test
    @Transactional
    void getAllContaContabilsByTipoContaContabilIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where tipoContaContabil in
        defaultContaContabilFiltering(
            "tipoContaContabil.in=" + DEFAULT_TIPO_CONTA_CONTABIL + "," + UPDATED_TIPO_CONTA_CONTABIL,
            "tipoContaContabil.in=" + UPDATED_TIPO_CONTA_CONTABIL
        );
    }

    @Test
    @Transactional
    void getAllContaContabilsByTipoContaContabilIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where tipoContaContabil is not null
        defaultContaContabilFiltering("tipoContaContabil.specified=true", "tipoContaContabil.specified=false");
    }

    @Test
    @Transactional
    void getAllContaContabilsByCategoriaContaContabilIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where categoriaContaContabil equals to
        defaultContaContabilFiltering(
            "categoriaContaContabil.equals=" + DEFAULT_CATEGORIA_CONTA_CONTABIL,
            "categoriaContaContabil.equals=" + UPDATED_CATEGORIA_CONTA_CONTABIL
        );
    }

    @Test
    @Transactional
    void getAllContaContabilsByCategoriaContaContabilIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where categoriaContaContabil in
        defaultContaContabilFiltering(
            "categoriaContaContabil.in=" + DEFAULT_CATEGORIA_CONTA_CONTABIL + "," + UPDATED_CATEGORIA_CONTA_CONTABIL,
            "categoriaContaContabil.in=" + UPDATED_CATEGORIA_CONTA_CONTABIL
        );
    }

    @Test
    @Transactional
    void getAllContaContabilsByCategoriaContaContabilIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where categoriaContaContabil is not null
        defaultContaContabilFiltering("categoriaContaContabil.specified=true", "categoriaContaContabil.specified=false");
    }

    @Test
    @Transactional
    void getAllContaContabilsByAtivaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where ativa equals to
        defaultContaContabilFiltering("ativa.equals=" + DEFAULT_ATIVA, "ativa.equals=" + UPDATED_ATIVA);
    }

    @Test
    @Transactional
    void getAllContaContabilsByAtivaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where ativa in
        defaultContaContabilFiltering("ativa.in=" + DEFAULT_ATIVA + "," + UPDATED_ATIVA, "ativa.in=" + UPDATED_ATIVA);
    }

    @Test
    @Transactional
    void getAllContaContabilsByAtivaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        // Get all the contaContabilList where ativa is not null
        defaultContaContabilFiltering("ativa.specified=true", "ativa.specified=false");
    }

    @Test
    @Transactional
    void getAllContaContabilsByMoedaCarteiraIsEqualToSomething() throws Exception {
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            contaContabilRepository.saveAndFlush(contaContabil);
            moedaCarteira = MoedaCarteiraResourceIT.createEntity(em);
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        em.persist(moedaCarteira);
        em.flush();
        contaContabil.setMoedaCarteira(moedaCarteira);
        contaContabilRepository.saveAndFlush(contaContabil);
        Long moedaCarteiraId = moedaCarteira.getId();
        // Get all the contaContabilList where moedaCarteira equals to moedaCarteiraId
        defaultContaContabilShouldBeFound("moedaCarteiraId.equals=" + moedaCarteiraId);

        // Get all the contaContabilList where moedaCarteira equals to (moedaCarteiraId + 1)
        defaultContaContabilShouldNotBeFound("moedaCarteiraId.equals=" + (moedaCarteiraId + 1));
    }

    private void defaultContaContabilFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultContaContabilShouldBeFound(shouldBeFound);
        defaultContaContabilShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContaContabilShouldBeFound(String filter) throws Exception {
        restContaContabilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaContabil.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].saldo").value(hasItem(sameNumber(DEFAULT_SALDO))))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].tipoContaContabil").value(hasItem(DEFAULT_TIPO_CONTA_CONTABIL.toString())))
            .andExpect(jsonPath("$.[*].categoriaContaContabil").value(hasItem(DEFAULT_CATEGORIA_CONTA_CONTABIL.toString())))
            .andExpect(jsonPath("$.[*].ativa").value(hasItem(DEFAULT_ATIVA)));

        // Check, that the count call also returns 1
        restContaContabilMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContaContabilShouldNotBeFound(String filter) throws Exception {
        restContaContabilMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContaContabilMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContaContabil() throws Exception {
        // Get the contaContabil
        restContaContabilMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContaContabil() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contaContabil
        ContaContabil updatedContaContabil = contaContabilRepository.findById(contaContabil.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContaContabil are not directly saved in db
        em.detach(updatedContaContabil);
        updatedContaContabil
            .codigo(UPDATED_CODIGO)
            .nome(UPDATED_NOME)
            .saldo(UPDATED_SALDO)
            .descricao(UPDATED_DESCRICAO)
            .tipoContaContabil(UPDATED_TIPO_CONTA_CONTABIL)
            .categoriaContaContabil(UPDATED_CATEGORIA_CONTA_CONTABIL)
            .ativa(UPDATED_ATIVA);
        ContaContabilDTO contaContabilDTO = contaContabilMapper.toDto(updatedContaContabil);

        restContaContabilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contaContabilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contaContabilDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContaContabil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContaContabilToMatchAllProperties(updatedContaContabil);
    }

    @Test
    @Transactional
    void putNonExistingContaContabil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contaContabil.setId(longCount.incrementAndGet());

        // Create the ContaContabil
        ContaContabilDTO contaContabilDTO = contaContabilMapper.toDto(contaContabil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContaContabilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contaContabilDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contaContabilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaContabil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContaContabil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contaContabil.setId(longCount.incrementAndGet());

        // Create the ContaContabil
        ContaContabilDTO contaContabilDTO = contaContabilMapper.toDto(contaContabil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaContabilMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contaContabilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaContabil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContaContabil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contaContabil.setId(longCount.incrementAndGet());

        // Create the ContaContabil
        ContaContabilDTO contaContabilDTO = contaContabilMapper.toDto(contaContabil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaContabilMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contaContabilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContaContabil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContaContabilWithPatch() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contaContabil using partial update
        ContaContabil partialUpdatedContaContabil = new ContaContabil();
        partialUpdatedContaContabil.setId(contaContabil.getId());

        partialUpdatedContaContabil
            .codigo(UPDATED_CODIGO)
            .descricao(UPDATED_DESCRICAO)
            .categoriaContaContabil(UPDATED_CATEGORIA_CONTA_CONTABIL);

        restContaContabilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContaContabil.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContaContabil))
            )
            .andExpect(status().isOk());

        // Validate the ContaContabil in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContaContabilUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContaContabil, contaContabil),
            getPersistedContaContabil(contaContabil)
        );
    }

    @Test
    @Transactional
    void fullUpdateContaContabilWithPatch() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contaContabil using partial update
        ContaContabil partialUpdatedContaContabil = new ContaContabil();
        partialUpdatedContaContabil.setId(contaContabil.getId());

        partialUpdatedContaContabil
            .codigo(UPDATED_CODIGO)
            .nome(UPDATED_NOME)
            .saldo(UPDATED_SALDO)
            .descricao(UPDATED_DESCRICAO)
            .tipoContaContabil(UPDATED_TIPO_CONTA_CONTABIL)
            .categoriaContaContabil(UPDATED_CATEGORIA_CONTA_CONTABIL)
            .ativa(UPDATED_ATIVA);

        restContaContabilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContaContabil.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContaContabil))
            )
            .andExpect(status().isOk());

        // Validate the ContaContabil in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContaContabilUpdatableFieldsEquals(partialUpdatedContaContabil, getPersistedContaContabil(partialUpdatedContaContabil));
    }

    @Test
    @Transactional
    void patchNonExistingContaContabil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contaContabil.setId(longCount.incrementAndGet());

        // Create the ContaContabil
        ContaContabilDTO contaContabilDTO = contaContabilMapper.toDto(contaContabil);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContaContabilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contaContabilDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contaContabilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaContabil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContaContabil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contaContabil.setId(longCount.incrementAndGet());

        // Create the ContaContabil
        ContaContabilDTO contaContabilDTO = contaContabilMapper.toDto(contaContabil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaContabilMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contaContabilDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaContabil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContaContabil() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contaContabil.setId(longCount.incrementAndGet());

        // Create the ContaContabil
        ContaContabilDTO contaContabilDTO = contaContabilMapper.toDto(contaContabil);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaContabilMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contaContabilDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContaContabil in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContaContabil() throws Exception {
        // Initialize the database
        insertedContaContabil = contaContabilRepository.saveAndFlush(contaContabil);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contaContabil
        restContaContabilMockMvc
            .perform(delete(ENTITY_API_URL_ID, contaContabil.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contaContabilRepository.count();
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

    protected ContaContabil getPersistedContaContabil(ContaContabil contaContabil) {
        return contaContabilRepository.findById(contaContabil.getId()).orElseThrow();
    }

    protected void assertPersistedContaContabilToMatchAllProperties(ContaContabil expectedContaContabil) {
        assertContaContabilAllPropertiesEquals(expectedContaContabil, getPersistedContaContabil(expectedContaContabil));
    }

    protected void assertPersistedContaContabilToMatchUpdatableProperties(ContaContabil expectedContaContabil) {
        assertContaContabilAllUpdatablePropertiesEquals(expectedContaContabil, getPersistedContaContabil(expectedContaContabil));
    }
}
