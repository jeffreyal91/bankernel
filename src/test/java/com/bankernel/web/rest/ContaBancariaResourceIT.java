package com.bankernel.web.rest;

import static com.bankernel.domain.ContaBancariaAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.ContaBancaria;
import com.bankernel.domain.Moeda;
import com.bankernel.domain.Pais;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumTipoConta;
import com.bankernel.repository.ContaBancariaRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.ContaBancariaService;
import com.bankernel.service.dto.ContaBancariaDTO;
import com.bankernel.service.mapper.ContaBancariaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ContaBancariaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ContaBancariaResourceIT {

    private static final String DEFAULT_NOME_TITULAR = "AAAAAAAAAA";
    private static final String UPDATED_NOME_TITULAR = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_CONTA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_CONTA = "BBBBBBBBBB";

    private static final String DEFAULT_AGENCIA = "AAAAAAAAAA";
    private static final String UPDATED_AGENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_NOME_BANCO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_BANCO = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_BANCO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_BANCO = "BBBBBBBBBB";

    private static final String DEFAULT_ISPB = "AAAAAAAA";
    private static final String UPDATED_ISPB = "BBBBBBBB";

    private static final String DEFAULT_CODIGO_SWIFT = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_SWIFT = "BBBBBBBBBB";

    private static final EnumTipoConta DEFAULT_TIPO_CONTA = EnumTipoConta.CORRENTE;
    private static final EnumTipoConta UPDATED_TIPO_CONTA = EnumTipoConta.POUPANCA;

    private static final Boolean DEFAULT_ATIVA = false;
    private static final Boolean UPDATED_ATIVA = true;

    private static final String ENTITY_API_URL = "/api/conta-bancarias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContaBancariaRepository contaBancariaRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ContaBancariaRepository contaBancariaRepositoryMock;

    @Autowired
    private ContaBancariaMapper contaBancariaMapper;

    @Mock
    private ContaBancariaService contaBancariaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContaBancariaMockMvc;

    private ContaBancaria contaBancaria;

    private ContaBancaria insertedContaBancaria;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContaBancaria createEntity(EntityManager em) {
        ContaBancaria contaBancaria = new ContaBancaria()
            .nomeTitular(DEFAULT_NOME_TITULAR)
            .numeroConta(DEFAULT_NUMERO_CONTA)
            .agencia(DEFAULT_AGENCIA)
            .nomeBanco(DEFAULT_NOME_BANCO)
            .codigoBanco(DEFAULT_CODIGO_BANCO)
            .ispb(DEFAULT_ISPB)
            .codigoSwift(DEFAULT_CODIGO_SWIFT)
            .tipoConta(DEFAULT_TIPO_CONTA)
            .ativa(DEFAULT_ATIVA);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        contaBancaria.setUsuario(user);
        return contaBancaria;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContaBancaria createUpdatedEntity(EntityManager em) {
        ContaBancaria updatedContaBancaria = new ContaBancaria()
            .nomeTitular(UPDATED_NOME_TITULAR)
            .numeroConta(UPDATED_NUMERO_CONTA)
            .agencia(UPDATED_AGENCIA)
            .nomeBanco(UPDATED_NOME_BANCO)
            .codigoBanco(UPDATED_CODIGO_BANCO)
            .ispb(UPDATED_ISPB)
            .codigoSwift(UPDATED_CODIGO_SWIFT)
            .tipoConta(UPDATED_TIPO_CONTA)
            .ativa(UPDATED_ATIVA);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedContaBancaria.setUsuario(user);
        return updatedContaBancaria;
    }

    @BeforeEach
    void initTest() {
        contaBancaria = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedContaBancaria != null) {
            contaBancariaRepository.delete(insertedContaBancaria);
            insertedContaBancaria = null;
        }
    }

    @Test
    @Transactional
    void createContaBancaria() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);
        var returnedContaBancariaDTO = om.readValue(
            restContaBancariaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contaBancariaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContaBancariaDTO.class
        );

        // Validate the ContaBancaria in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedContaBancaria = contaBancariaMapper.toEntity(returnedContaBancariaDTO);
        assertContaBancariaUpdatableFieldsEquals(returnedContaBancaria, getPersistedContaBancaria(returnedContaBancaria));

        insertedContaBancaria = returnedContaBancaria;
    }

    @Test
    @Transactional
    void createContaBancariaWithExistingId() throws Exception {
        // Create the ContaBancaria with an existing ID
        contaBancaria.setId(1L);
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContaBancariaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contaBancariaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContaBancaria in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeTitularIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contaBancaria.setNomeTitular(null);

        // Create the ContaBancaria, which fails.
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        restContaBancariaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contaBancariaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroContaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contaBancaria.setNumeroConta(null);

        // Create the ContaBancaria, which fails.
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        restContaBancariaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contaBancariaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoContaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contaBancaria.setTipoConta(null);

        // Create the ContaBancaria, which fails.
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        restContaBancariaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contaBancariaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contaBancaria.setAtiva(null);

        // Create the ContaBancaria, which fails.
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        restContaBancariaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contaBancariaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContaBancarias() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList
        restContaBancariaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaBancaria.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeTitular").value(hasItem(DEFAULT_NOME_TITULAR)))
            .andExpect(jsonPath("$.[*].numeroConta").value(hasItem(DEFAULT_NUMERO_CONTA)))
            .andExpect(jsonPath("$.[*].agencia").value(hasItem(DEFAULT_AGENCIA)))
            .andExpect(jsonPath("$.[*].nomeBanco").value(hasItem(DEFAULT_NOME_BANCO)))
            .andExpect(jsonPath("$.[*].codigoBanco").value(hasItem(DEFAULT_CODIGO_BANCO)))
            .andExpect(jsonPath("$.[*].ispb").value(hasItem(DEFAULT_ISPB)))
            .andExpect(jsonPath("$.[*].codigoSwift").value(hasItem(DEFAULT_CODIGO_SWIFT)))
            .andExpect(jsonPath("$.[*].tipoConta").value(hasItem(DEFAULT_TIPO_CONTA.toString())))
            .andExpect(jsonPath("$.[*].ativa").value(hasItem(DEFAULT_ATIVA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContaBancariasWithEagerRelationshipsIsEnabled() throws Exception {
        when(contaBancariaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContaBancariaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(contaBancariaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllContaBancariasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(contaBancariaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restContaBancariaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(contaBancariaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getContaBancaria() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get the contaBancaria
        restContaBancariaMockMvc
            .perform(get(ENTITY_API_URL_ID, contaBancaria.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contaBancaria.getId().intValue()))
            .andExpect(jsonPath("$.nomeTitular").value(DEFAULT_NOME_TITULAR))
            .andExpect(jsonPath("$.numeroConta").value(DEFAULT_NUMERO_CONTA))
            .andExpect(jsonPath("$.agencia").value(DEFAULT_AGENCIA))
            .andExpect(jsonPath("$.nomeBanco").value(DEFAULT_NOME_BANCO))
            .andExpect(jsonPath("$.codigoBanco").value(DEFAULT_CODIGO_BANCO))
            .andExpect(jsonPath("$.ispb").value(DEFAULT_ISPB))
            .andExpect(jsonPath("$.codigoSwift").value(DEFAULT_CODIGO_SWIFT))
            .andExpect(jsonPath("$.tipoConta").value(DEFAULT_TIPO_CONTA.toString()))
            .andExpect(jsonPath("$.ativa").value(DEFAULT_ATIVA));
    }

    @Test
    @Transactional
    void getContaBancariasByIdFiltering() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        Long id = contaBancaria.getId();

        defaultContaBancariaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultContaBancariaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultContaBancariaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContaBancariasByNomeTitularIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where nomeTitular equals to
        defaultContaBancariaFiltering("nomeTitular.equals=" + DEFAULT_NOME_TITULAR, "nomeTitular.equals=" + UPDATED_NOME_TITULAR);
    }

    @Test
    @Transactional
    void getAllContaBancariasByNomeTitularIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where nomeTitular in
        defaultContaBancariaFiltering(
            "nomeTitular.in=" + DEFAULT_NOME_TITULAR + "," + UPDATED_NOME_TITULAR,
            "nomeTitular.in=" + UPDATED_NOME_TITULAR
        );
    }

    @Test
    @Transactional
    void getAllContaBancariasByNomeTitularIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where nomeTitular is not null
        defaultContaBancariaFiltering("nomeTitular.specified=true", "nomeTitular.specified=false");
    }

    @Test
    @Transactional
    void getAllContaBancariasByNomeTitularContainsSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where nomeTitular contains
        defaultContaBancariaFiltering("nomeTitular.contains=" + DEFAULT_NOME_TITULAR, "nomeTitular.contains=" + UPDATED_NOME_TITULAR);
    }

    @Test
    @Transactional
    void getAllContaBancariasByNomeTitularNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where nomeTitular does not contain
        defaultContaBancariaFiltering(
            "nomeTitular.doesNotContain=" + UPDATED_NOME_TITULAR,
            "nomeTitular.doesNotContain=" + DEFAULT_NOME_TITULAR
        );
    }

    @Test
    @Transactional
    void getAllContaBancariasByNumeroContaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where numeroConta equals to
        defaultContaBancariaFiltering("numeroConta.equals=" + DEFAULT_NUMERO_CONTA, "numeroConta.equals=" + UPDATED_NUMERO_CONTA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByNumeroContaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where numeroConta in
        defaultContaBancariaFiltering(
            "numeroConta.in=" + DEFAULT_NUMERO_CONTA + "," + UPDATED_NUMERO_CONTA,
            "numeroConta.in=" + UPDATED_NUMERO_CONTA
        );
    }

    @Test
    @Transactional
    void getAllContaBancariasByNumeroContaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where numeroConta is not null
        defaultContaBancariaFiltering("numeroConta.specified=true", "numeroConta.specified=false");
    }

    @Test
    @Transactional
    void getAllContaBancariasByNumeroContaContainsSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where numeroConta contains
        defaultContaBancariaFiltering("numeroConta.contains=" + DEFAULT_NUMERO_CONTA, "numeroConta.contains=" + UPDATED_NUMERO_CONTA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByNumeroContaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where numeroConta does not contain
        defaultContaBancariaFiltering(
            "numeroConta.doesNotContain=" + UPDATED_NUMERO_CONTA,
            "numeroConta.doesNotContain=" + DEFAULT_NUMERO_CONTA
        );
    }

    @Test
    @Transactional
    void getAllContaBancariasByAgenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where agencia equals to
        defaultContaBancariaFiltering("agencia.equals=" + DEFAULT_AGENCIA, "agencia.equals=" + UPDATED_AGENCIA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByAgenciaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where agencia in
        defaultContaBancariaFiltering("agencia.in=" + DEFAULT_AGENCIA + "," + UPDATED_AGENCIA, "agencia.in=" + UPDATED_AGENCIA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByAgenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where agencia is not null
        defaultContaBancariaFiltering("agencia.specified=true", "agencia.specified=false");
    }

    @Test
    @Transactional
    void getAllContaBancariasByAgenciaContainsSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where agencia contains
        defaultContaBancariaFiltering("agencia.contains=" + DEFAULT_AGENCIA, "agencia.contains=" + UPDATED_AGENCIA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByAgenciaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where agencia does not contain
        defaultContaBancariaFiltering("agencia.doesNotContain=" + UPDATED_AGENCIA, "agencia.doesNotContain=" + DEFAULT_AGENCIA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByNomeBancoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where nomeBanco equals to
        defaultContaBancariaFiltering("nomeBanco.equals=" + DEFAULT_NOME_BANCO, "nomeBanco.equals=" + UPDATED_NOME_BANCO);
    }

    @Test
    @Transactional
    void getAllContaBancariasByNomeBancoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where nomeBanco in
        defaultContaBancariaFiltering(
            "nomeBanco.in=" + DEFAULT_NOME_BANCO + "," + UPDATED_NOME_BANCO,
            "nomeBanco.in=" + UPDATED_NOME_BANCO
        );
    }

    @Test
    @Transactional
    void getAllContaBancariasByNomeBancoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where nomeBanco is not null
        defaultContaBancariaFiltering("nomeBanco.specified=true", "nomeBanco.specified=false");
    }

    @Test
    @Transactional
    void getAllContaBancariasByNomeBancoContainsSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where nomeBanco contains
        defaultContaBancariaFiltering("nomeBanco.contains=" + DEFAULT_NOME_BANCO, "nomeBanco.contains=" + UPDATED_NOME_BANCO);
    }

    @Test
    @Transactional
    void getAllContaBancariasByNomeBancoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where nomeBanco does not contain
        defaultContaBancariaFiltering("nomeBanco.doesNotContain=" + UPDATED_NOME_BANCO, "nomeBanco.doesNotContain=" + DEFAULT_NOME_BANCO);
    }

    @Test
    @Transactional
    void getAllContaBancariasByCodigoBancoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where codigoBanco equals to
        defaultContaBancariaFiltering("codigoBanco.equals=" + DEFAULT_CODIGO_BANCO, "codigoBanco.equals=" + UPDATED_CODIGO_BANCO);
    }

    @Test
    @Transactional
    void getAllContaBancariasByCodigoBancoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where codigoBanco in
        defaultContaBancariaFiltering(
            "codigoBanco.in=" + DEFAULT_CODIGO_BANCO + "," + UPDATED_CODIGO_BANCO,
            "codigoBanco.in=" + UPDATED_CODIGO_BANCO
        );
    }

    @Test
    @Transactional
    void getAllContaBancariasByCodigoBancoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where codigoBanco is not null
        defaultContaBancariaFiltering("codigoBanco.specified=true", "codigoBanco.specified=false");
    }

    @Test
    @Transactional
    void getAllContaBancariasByCodigoBancoContainsSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where codigoBanco contains
        defaultContaBancariaFiltering("codigoBanco.contains=" + DEFAULT_CODIGO_BANCO, "codigoBanco.contains=" + UPDATED_CODIGO_BANCO);
    }

    @Test
    @Transactional
    void getAllContaBancariasByCodigoBancoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where codigoBanco does not contain
        defaultContaBancariaFiltering(
            "codigoBanco.doesNotContain=" + UPDATED_CODIGO_BANCO,
            "codigoBanco.doesNotContain=" + DEFAULT_CODIGO_BANCO
        );
    }

    @Test
    @Transactional
    void getAllContaBancariasByIspbIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where ispb equals to
        defaultContaBancariaFiltering("ispb.equals=" + DEFAULT_ISPB, "ispb.equals=" + UPDATED_ISPB);
    }

    @Test
    @Transactional
    void getAllContaBancariasByIspbIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where ispb in
        defaultContaBancariaFiltering("ispb.in=" + DEFAULT_ISPB + "," + UPDATED_ISPB, "ispb.in=" + UPDATED_ISPB);
    }

    @Test
    @Transactional
    void getAllContaBancariasByIspbIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where ispb is not null
        defaultContaBancariaFiltering("ispb.specified=true", "ispb.specified=false");
    }

    @Test
    @Transactional
    void getAllContaBancariasByIspbContainsSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where ispb contains
        defaultContaBancariaFiltering("ispb.contains=" + DEFAULT_ISPB, "ispb.contains=" + UPDATED_ISPB);
    }

    @Test
    @Transactional
    void getAllContaBancariasByIspbNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where ispb does not contain
        defaultContaBancariaFiltering("ispb.doesNotContain=" + UPDATED_ISPB, "ispb.doesNotContain=" + DEFAULT_ISPB);
    }

    @Test
    @Transactional
    void getAllContaBancariasByCodigoSwiftIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where codigoSwift equals to
        defaultContaBancariaFiltering("codigoSwift.equals=" + DEFAULT_CODIGO_SWIFT, "codigoSwift.equals=" + UPDATED_CODIGO_SWIFT);
    }

    @Test
    @Transactional
    void getAllContaBancariasByCodigoSwiftIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where codigoSwift in
        defaultContaBancariaFiltering(
            "codigoSwift.in=" + DEFAULT_CODIGO_SWIFT + "," + UPDATED_CODIGO_SWIFT,
            "codigoSwift.in=" + UPDATED_CODIGO_SWIFT
        );
    }

    @Test
    @Transactional
    void getAllContaBancariasByCodigoSwiftIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where codigoSwift is not null
        defaultContaBancariaFiltering("codigoSwift.specified=true", "codigoSwift.specified=false");
    }

    @Test
    @Transactional
    void getAllContaBancariasByCodigoSwiftContainsSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where codigoSwift contains
        defaultContaBancariaFiltering("codigoSwift.contains=" + DEFAULT_CODIGO_SWIFT, "codigoSwift.contains=" + UPDATED_CODIGO_SWIFT);
    }

    @Test
    @Transactional
    void getAllContaBancariasByCodigoSwiftNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where codigoSwift does not contain
        defaultContaBancariaFiltering(
            "codigoSwift.doesNotContain=" + UPDATED_CODIGO_SWIFT,
            "codigoSwift.doesNotContain=" + DEFAULT_CODIGO_SWIFT
        );
    }

    @Test
    @Transactional
    void getAllContaBancariasByTipoContaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where tipoConta equals to
        defaultContaBancariaFiltering("tipoConta.equals=" + DEFAULT_TIPO_CONTA, "tipoConta.equals=" + UPDATED_TIPO_CONTA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByTipoContaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where tipoConta in
        defaultContaBancariaFiltering(
            "tipoConta.in=" + DEFAULT_TIPO_CONTA + "," + UPDATED_TIPO_CONTA,
            "tipoConta.in=" + UPDATED_TIPO_CONTA
        );
    }

    @Test
    @Transactional
    void getAllContaBancariasByTipoContaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where tipoConta is not null
        defaultContaBancariaFiltering("tipoConta.specified=true", "tipoConta.specified=false");
    }

    @Test
    @Transactional
    void getAllContaBancariasByAtivaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where ativa equals to
        defaultContaBancariaFiltering("ativa.equals=" + DEFAULT_ATIVA, "ativa.equals=" + UPDATED_ATIVA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByAtivaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where ativa in
        defaultContaBancariaFiltering("ativa.in=" + DEFAULT_ATIVA + "," + UPDATED_ATIVA, "ativa.in=" + UPDATED_ATIVA);
    }

    @Test
    @Transactional
    void getAllContaBancariasByAtivaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        // Get all the contaBancariaList where ativa is not null
        defaultContaBancariaFiltering("ativa.specified=true", "ativa.specified=false");
    }

    @Test
    @Transactional
    void getAllContaBancariasByUsuarioIsEqualToSomething() throws Exception {
        User usuario;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            contaBancariaRepository.saveAndFlush(contaBancaria);
            usuario = UserResourceIT.createEntity();
        } else {
            usuario = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        contaBancaria.setUsuario(usuario);
        contaBancariaRepository.saveAndFlush(contaBancaria);
        Long usuarioId = usuario.getId();
        // Get all the contaBancariaList where usuario equals to usuarioId
        defaultContaBancariaShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the contaBancariaList where usuario equals to (usuarioId + 1)
        defaultContaBancariaShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    @Test
    @Transactional
    void getAllContaBancariasByPaisIsEqualToSomething() throws Exception {
        Pais pais;
        if (TestUtil.findAll(em, Pais.class).isEmpty()) {
            contaBancariaRepository.saveAndFlush(contaBancaria);
            pais = PaisResourceIT.createEntity();
        } else {
            pais = TestUtil.findAll(em, Pais.class).get(0);
        }
        em.persist(pais);
        em.flush();
        contaBancaria.setPais(pais);
        contaBancariaRepository.saveAndFlush(contaBancaria);
        Long paisId = pais.getId();
        // Get all the contaBancariaList where pais equals to paisId
        defaultContaBancariaShouldBeFound("paisId.equals=" + paisId);

        // Get all the contaBancariaList where pais equals to (paisId + 1)
        defaultContaBancariaShouldNotBeFound("paisId.equals=" + (paisId + 1));
    }

    @Test
    @Transactional
    void getAllContaBancariasByMoedaIsEqualToSomething() throws Exception {
        Moeda moeda;
        if (TestUtil.findAll(em, Moeda.class).isEmpty()) {
            contaBancariaRepository.saveAndFlush(contaBancaria);
            moeda = MoedaResourceIT.createEntity();
        } else {
            moeda = TestUtil.findAll(em, Moeda.class).get(0);
        }
        em.persist(moeda);
        em.flush();
        contaBancaria.setMoeda(moeda);
        contaBancariaRepository.saveAndFlush(contaBancaria);
        Long moedaId = moeda.getId();
        // Get all the contaBancariaList where moeda equals to moedaId
        defaultContaBancariaShouldBeFound("moedaId.equals=" + moedaId);

        // Get all the contaBancariaList where moeda equals to (moedaId + 1)
        defaultContaBancariaShouldNotBeFound("moedaId.equals=" + (moedaId + 1));
    }

    private void defaultContaBancariaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultContaBancariaShouldBeFound(shouldBeFound);
        defaultContaBancariaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContaBancariaShouldBeFound(String filter) throws Exception {
        restContaBancariaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contaBancaria.getId().intValue())))
            .andExpect(jsonPath("$.[*].nomeTitular").value(hasItem(DEFAULT_NOME_TITULAR)))
            .andExpect(jsonPath("$.[*].numeroConta").value(hasItem(DEFAULT_NUMERO_CONTA)))
            .andExpect(jsonPath("$.[*].agencia").value(hasItem(DEFAULT_AGENCIA)))
            .andExpect(jsonPath("$.[*].nomeBanco").value(hasItem(DEFAULT_NOME_BANCO)))
            .andExpect(jsonPath("$.[*].codigoBanco").value(hasItem(DEFAULT_CODIGO_BANCO)))
            .andExpect(jsonPath("$.[*].ispb").value(hasItem(DEFAULT_ISPB)))
            .andExpect(jsonPath("$.[*].codigoSwift").value(hasItem(DEFAULT_CODIGO_SWIFT)))
            .andExpect(jsonPath("$.[*].tipoConta").value(hasItem(DEFAULT_TIPO_CONTA.toString())))
            .andExpect(jsonPath("$.[*].ativa").value(hasItem(DEFAULT_ATIVA)));

        // Check, that the count call also returns 1
        restContaBancariaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContaBancariaShouldNotBeFound(String filter) throws Exception {
        restContaBancariaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContaBancariaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContaBancaria() throws Exception {
        // Get the contaBancaria
        restContaBancariaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContaBancaria() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contaBancaria
        ContaBancaria updatedContaBancaria = contaBancariaRepository.findById(contaBancaria.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContaBancaria are not directly saved in db
        em.detach(updatedContaBancaria);
        updatedContaBancaria
            .nomeTitular(UPDATED_NOME_TITULAR)
            .numeroConta(UPDATED_NUMERO_CONTA)
            .agencia(UPDATED_AGENCIA)
            .nomeBanco(UPDATED_NOME_BANCO)
            .codigoBanco(UPDATED_CODIGO_BANCO)
            .ispb(UPDATED_ISPB)
            .codigoSwift(UPDATED_CODIGO_SWIFT)
            .tipoConta(UPDATED_TIPO_CONTA)
            .ativa(UPDATED_ATIVA);
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(updatedContaBancaria);

        restContaBancariaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contaBancariaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contaBancariaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContaBancaria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContaBancariaToMatchAllProperties(updatedContaBancaria);
    }

    @Test
    @Transactional
    void putNonExistingContaBancaria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contaBancariaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaBancaria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContaBancaria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaBancaria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContaBancaria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contaBancariaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContaBancaria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContaBancariaWithPatch() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contaBancaria using partial update
        ContaBancaria partialUpdatedContaBancaria = new ContaBancaria();
        partialUpdatedContaBancaria.setId(contaBancaria.getId());

        partialUpdatedContaBancaria
            .nomeTitular(UPDATED_NOME_TITULAR)
            .numeroConta(UPDATED_NUMERO_CONTA)
            .nomeBanco(UPDATED_NOME_BANCO)
            .codigoBanco(UPDATED_CODIGO_BANCO)
            .ispb(UPDATED_ISPB)
            .codigoSwift(UPDATED_CODIGO_SWIFT);

        restContaBancariaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContaBancaria.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContaBancaria))
            )
            .andExpect(status().isOk());

        // Validate the ContaBancaria in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContaBancariaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContaBancaria, contaBancaria),
            getPersistedContaBancaria(contaBancaria)
        );
    }

    @Test
    @Transactional
    void fullUpdateContaBancariaWithPatch() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contaBancaria using partial update
        ContaBancaria partialUpdatedContaBancaria = new ContaBancaria();
        partialUpdatedContaBancaria.setId(contaBancaria.getId());

        partialUpdatedContaBancaria
            .nomeTitular(UPDATED_NOME_TITULAR)
            .numeroConta(UPDATED_NUMERO_CONTA)
            .agencia(UPDATED_AGENCIA)
            .nomeBanco(UPDATED_NOME_BANCO)
            .codigoBanco(UPDATED_CODIGO_BANCO)
            .ispb(UPDATED_ISPB)
            .codigoSwift(UPDATED_CODIGO_SWIFT)
            .tipoConta(UPDATED_TIPO_CONTA)
            .ativa(UPDATED_ATIVA);

        restContaBancariaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContaBancaria.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContaBancaria))
            )
            .andExpect(status().isOk());

        // Validate the ContaBancaria in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContaBancariaUpdatableFieldsEquals(partialUpdatedContaBancaria, getPersistedContaBancaria(partialUpdatedContaBancaria));
    }

    @Test
    @Transactional
    void patchNonExistingContaBancaria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contaBancariaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaBancaria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContaBancaria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contaBancariaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContaBancaria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContaBancaria() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contaBancaria.setId(longCount.incrementAndGet());

        // Create the ContaBancaria
        ContaBancariaDTO contaBancariaDTO = contaBancariaMapper.toDto(contaBancaria);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContaBancariaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contaBancariaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContaBancaria in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContaBancaria() throws Exception {
        // Initialize the database
        insertedContaBancaria = contaBancariaRepository.saveAndFlush(contaBancaria);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contaBancaria
        restContaBancariaMockMvc
            .perform(delete(ENTITY_API_URL_ID, contaBancaria.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contaBancariaRepository.count();
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

    protected ContaBancaria getPersistedContaBancaria(ContaBancaria contaBancaria) {
        return contaBancariaRepository.findById(contaBancaria.getId()).orElseThrow();
    }

    protected void assertPersistedContaBancariaToMatchAllProperties(ContaBancaria expectedContaBancaria) {
        assertContaBancariaAllPropertiesEquals(expectedContaBancaria, getPersistedContaBancaria(expectedContaBancaria));
    }

    protected void assertPersistedContaBancariaToMatchUpdatableProperties(ContaBancaria expectedContaBancaria) {
        assertContaBancariaAllUpdatablePropertiesEquals(expectedContaBancaria, getPersistedContaBancaria(expectedContaBancaria));
    }
}
