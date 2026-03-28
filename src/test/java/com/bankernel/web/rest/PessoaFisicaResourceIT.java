package com.bankernel.web.rest;

import static com.bankernel.domain.PessoaFisicaAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Escritorio;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.Pais;
import com.bankernel.domain.PessoaFisica;
import com.bankernel.domain.Plano;
import com.bankernel.domain.Profissao;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumGenero;
import com.bankernel.domain.enumeration.EnumNivelRisco;
import com.bankernel.domain.enumeration.EnumStatusPessoa;
import com.bankernel.repository.PessoaFisicaRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.PessoaFisicaService;
import com.bankernel.service.dto.PessoaFisicaDTO;
import com.bankernel.service.mapper.PessoaFisicaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PessoaFisicaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PessoaFisicaResourceIT {

    private static final String DEFAULT_CPF = "AAAAAAAAAA";
    private static final String UPDATED_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_NOME_COMPLETO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_COMPLETO = "BBBBBBBBBB";

    private static final String DEFAULT_NOME_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_NOME_SOCIAL = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_NASCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_NASCIMENTO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_NASCIMENTO = LocalDate.ofEpochDay(-1L);

    private static final EnumGenero DEFAULT_GENERO = EnumGenero.MASCULINO;
    private static final EnumGenero UPDATED_GENERO = EnumGenero.FEMININO;

    private static final String DEFAULT_NOME_MAE = "AAAAAAAAAA";
    private static final String UPDATED_NOME_MAE = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_TELEFONE_VERIFICADO = false;
    private static final Boolean UPDATED_TELEFONE_VERIFICADO = true;

    private static final EnumNivelRisco DEFAULT_NIVEL_RISCO = EnumNivelRisco.BAIXO;
    private static final EnumNivelRisco UPDATED_NIVEL_RISCO = EnumNivelRisco.MEDIO;

    private static final EnumStatusPessoa DEFAULT_SITUACAO = EnumStatusPessoa.ATIVO;
    private static final EnumStatusPessoa UPDATED_SITUACAO = EnumStatusPessoa.INATIVO;

    private static final Boolean DEFAULT_BLOQUEIO_SAQUE = false;
    private static final Boolean UPDATED_BLOQUEIO_SAQUE = true;

    private static final String ENTITY_API_URL = "/api/pessoa-fisicas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PessoaFisicaRepository pessoaFisicaRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private PessoaFisicaRepository pessoaFisicaRepositoryMock;

    @Autowired
    private PessoaFisicaMapper pessoaFisicaMapper;

    @Mock
    private PessoaFisicaService pessoaFisicaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPessoaFisicaMockMvc;

    private PessoaFisica pessoaFisica;

    private PessoaFisica insertedPessoaFisica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PessoaFisica createEntity(EntityManager em) {
        PessoaFisica pessoaFisica = new PessoaFisica()
            .cpf(DEFAULT_CPF)
            .nomeCompleto(DEFAULT_NOME_COMPLETO)
            .nomeSocial(DEFAULT_NOME_SOCIAL)
            .dataNascimento(DEFAULT_DATA_NASCIMENTO)
            .genero(DEFAULT_GENERO)
            .nomeMae(DEFAULT_NOME_MAE)
            .telefone(DEFAULT_TELEFONE)
            .telefoneVerificado(DEFAULT_TELEFONE_VERIFICADO)
            .nivelRisco(DEFAULT_NIVEL_RISCO)
            .situacao(DEFAULT_SITUACAO)
            .bloqueioSaque(DEFAULT_BLOQUEIO_SAQUE);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        pessoaFisica.setUsuario(user);
        return pessoaFisica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PessoaFisica createUpdatedEntity(EntityManager em) {
        PessoaFisica updatedPessoaFisica = new PessoaFisica()
            .cpf(UPDATED_CPF)
            .nomeCompleto(UPDATED_NOME_COMPLETO)
            .nomeSocial(UPDATED_NOME_SOCIAL)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .genero(UPDATED_GENERO)
            .nomeMae(UPDATED_NOME_MAE)
            .telefone(UPDATED_TELEFONE)
            .telefoneVerificado(UPDATED_TELEFONE_VERIFICADO)
            .nivelRisco(UPDATED_NIVEL_RISCO)
            .situacao(UPDATED_SITUACAO)
            .bloqueioSaque(UPDATED_BLOQUEIO_SAQUE);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedPessoaFisica.setUsuario(user);
        return updatedPessoaFisica;
    }

    @BeforeEach
    void initTest() {
        pessoaFisica = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPessoaFisica != null) {
            pessoaFisicaRepository.delete(insertedPessoaFisica);
            insertedPessoaFisica = null;
        }
    }

    @Test
    @Transactional
    void createPessoaFisica() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PessoaFisica
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(pessoaFisica);
        var returnedPessoaFisicaDTO = om.readValue(
            restPessoaFisicaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaFisicaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PessoaFisicaDTO.class
        );

        // Validate the PessoaFisica in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPessoaFisica = pessoaFisicaMapper.toEntity(returnedPessoaFisicaDTO);
        assertPessoaFisicaUpdatableFieldsEquals(returnedPessoaFisica, getPersistedPessoaFisica(returnedPessoaFisica));

        insertedPessoaFisica = returnedPessoaFisica;
    }

    @Test
    @Transactional
    void createPessoaFisicaWithExistingId() throws Exception {
        // Create the PessoaFisica with an existing ID
        pessoaFisica.setId(1L);
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(pessoaFisica);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPessoaFisicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaFisicaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PessoaFisica in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCpfIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pessoaFisica.setCpf(null);

        // Create the PessoaFisica, which fails.
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(pessoaFisica);

        restPessoaFisicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaFisicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeCompletoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pessoaFisica.setNomeCompleto(null);

        // Create the PessoaFisica, which fails.
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(pessoaFisica);

        restPessoaFisicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaFisicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDataNascimentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pessoaFisica.setDataNascimento(null);

        // Create the PessoaFisica, which fails.
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(pessoaFisica);

        restPessoaFisicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaFisicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pessoaFisica.setTelefone(null);

        // Create the PessoaFisica, which fails.
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(pessoaFisica);

        restPessoaFisicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaFisicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefoneVerificadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pessoaFisica.setTelefoneVerificado(null);

        // Create the PessoaFisica, which fails.
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(pessoaFisica);

        restPessoaFisicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaFisicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNivelRiscoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pessoaFisica.setNivelRisco(null);

        // Create the PessoaFisica, which fails.
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(pessoaFisica);

        restPessoaFisicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaFisicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSituacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pessoaFisica.setSituacao(null);

        // Create the PessoaFisica, which fails.
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(pessoaFisica);

        restPessoaFisicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaFisicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBloqueioSaqueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pessoaFisica.setBloqueioSaque(null);

        // Create the PessoaFisica, which fails.
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(pessoaFisica);

        restPessoaFisicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaFisicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPessoaFisicas() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList
        restPessoaFisicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pessoaFisica.getId().intValue())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].nomeCompleto").value(hasItem(DEFAULT_NOME_COMPLETO)))
            .andExpect(jsonPath("$.[*].nomeSocial").value(hasItem(DEFAULT_NOME_SOCIAL)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO.toString())))
            .andExpect(jsonPath("$.[*].nomeMae").value(hasItem(DEFAULT_NOME_MAE)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].telefoneVerificado").value(hasItem(DEFAULT_TELEFONE_VERIFICADO)))
            .andExpect(jsonPath("$.[*].nivelRisco").value(hasItem(DEFAULT_NIVEL_RISCO.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].bloqueioSaque").value(hasItem(DEFAULT_BLOQUEIO_SAQUE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPessoaFisicasWithEagerRelationshipsIsEnabled() throws Exception {
        when(pessoaFisicaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPessoaFisicaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pessoaFisicaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPessoaFisicasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pessoaFisicaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPessoaFisicaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pessoaFisicaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPessoaFisica() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get the pessoaFisica
        restPessoaFisicaMockMvc
            .perform(get(ENTITY_API_URL_ID, pessoaFisica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pessoaFisica.getId().intValue()))
            .andExpect(jsonPath("$.cpf").value(DEFAULT_CPF))
            .andExpect(jsonPath("$.nomeCompleto").value(DEFAULT_NOME_COMPLETO))
            .andExpect(jsonPath("$.nomeSocial").value(DEFAULT_NOME_SOCIAL))
            .andExpect(jsonPath("$.dataNascimento").value(DEFAULT_DATA_NASCIMENTO.toString()))
            .andExpect(jsonPath("$.genero").value(DEFAULT_GENERO.toString()))
            .andExpect(jsonPath("$.nomeMae").value(DEFAULT_NOME_MAE))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.telefoneVerificado").value(DEFAULT_TELEFONE_VERIFICADO))
            .andExpect(jsonPath("$.nivelRisco").value(DEFAULT_NIVEL_RISCO.toString()))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()))
            .andExpect(jsonPath("$.bloqueioSaque").value(DEFAULT_BLOQUEIO_SAQUE));
    }

    @Test
    @Transactional
    void getPessoaFisicasByIdFiltering() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        Long id = pessoaFisica.getId();

        defaultPessoaFisicaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPessoaFisicaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPessoaFisicaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByCpfIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where cpf equals to
        defaultPessoaFisicaFiltering("cpf.equals=" + DEFAULT_CPF, "cpf.equals=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByCpfIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where cpf in
        defaultPessoaFisicaFiltering("cpf.in=" + DEFAULT_CPF + "," + UPDATED_CPF, "cpf.in=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByCpfIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where cpf is not null
        defaultPessoaFisicaFiltering("cpf.specified=true", "cpf.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByCpfContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where cpf contains
        defaultPessoaFisicaFiltering("cpf.contains=" + DEFAULT_CPF, "cpf.contains=" + UPDATED_CPF);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByCpfNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where cpf does not contain
        defaultPessoaFisicaFiltering("cpf.doesNotContain=" + UPDATED_CPF, "cpf.doesNotContain=" + DEFAULT_CPF);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNomeCompletoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nomeCompleto equals to
        defaultPessoaFisicaFiltering("nomeCompleto.equals=" + DEFAULT_NOME_COMPLETO, "nomeCompleto.equals=" + UPDATED_NOME_COMPLETO);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNomeCompletoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nomeCompleto in
        defaultPessoaFisicaFiltering(
            "nomeCompleto.in=" + DEFAULT_NOME_COMPLETO + "," + UPDATED_NOME_COMPLETO,
            "nomeCompleto.in=" + UPDATED_NOME_COMPLETO
        );
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNomeCompletoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nomeCompleto is not null
        defaultPessoaFisicaFiltering("nomeCompleto.specified=true", "nomeCompleto.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNomeCompletoContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nomeCompleto contains
        defaultPessoaFisicaFiltering("nomeCompleto.contains=" + DEFAULT_NOME_COMPLETO, "nomeCompleto.contains=" + UPDATED_NOME_COMPLETO);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNomeCompletoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nomeCompleto does not contain
        defaultPessoaFisicaFiltering(
            "nomeCompleto.doesNotContain=" + UPDATED_NOME_COMPLETO,
            "nomeCompleto.doesNotContain=" + DEFAULT_NOME_COMPLETO
        );
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNomeSocialIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nomeSocial equals to
        defaultPessoaFisicaFiltering("nomeSocial.equals=" + DEFAULT_NOME_SOCIAL, "nomeSocial.equals=" + UPDATED_NOME_SOCIAL);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNomeSocialIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nomeSocial in
        defaultPessoaFisicaFiltering(
            "nomeSocial.in=" + DEFAULT_NOME_SOCIAL + "," + UPDATED_NOME_SOCIAL,
            "nomeSocial.in=" + UPDATED_NOME_SOCIAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNomeSocialIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nomeSocial is not null
        defaultPessoaFisicaFiltering("nomeSocial.specified=true", "nomeSocial.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNomeSocialContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nomeSocial contains
        defaultPessoaFisicaFiltering("nomeSocial.contains=" + DEFAULT_NOME_SOCIAL, "nomeSocial.contains=" + UPDATED_NOME_SOCIAL);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNomeSocialNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nomeSocial does not contain
        defaultPessoaFisicaFiltering(
            "nomeSocial.doesNotContain=" + UPDATED_NOME_SOCIAL,
            "nomeSocial.doesNotContain=" + DEFAULT_NOME_SOCIAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByDataNascimentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where dataNascimento equals to
        defaultPessoaFisicaFiltering(
            "dataNascimento.equals=" + DEFAULT_DATA_NASCIMENTO,
            "dataNascimento.equals=" + UPDATED_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByDataNascimentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where dataNascimento in
        defaultPessoaFisicaFiltering(
            "dataNascimento.in=" + DEFAULT_DATA_NASCIMENTO + "," + UPDATED_DATA_NASCIMENTO,
            "dataNascimento.in=" + UPDATED_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByDataNascimentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where dataNascimento is not null
        defaultPessoaFisicaFiltering("dataNascimento.specified=true", "dataNascimento.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByDataNascimentoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where dataNascimento is greater than or equal to
        defaultPessoaFisicaFiltering(
            "dataNascimento.greaterThanOrEqual=" + DEFAULT_DATA_NASCIMENTO,
            "dataNascimento.greaterThanOrEqual=" + UPDATED_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByDataNascimentoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where dataNascimento is less than or equal to
        defaultPessoaFisicaFiltering(
            "dataNascimento.lessThanOrEqual=" + DEFAULT_DATA_NASCIMENTO,
            "dataNascimento.lessThanOrEqual=" + SMALLER_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByDataNascimentoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where dataNascimento is less than
        defaultPessoaFisicaFiltering(
            "dataNascimento.lessThan=" + UPDATED_DATA_NASCIMENTO,
            "dataNascimento.lessThan=" + DEFAULT_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByDataNascimentoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where dataNascimento is greater than
        defaultPessoaFisicaFiltering(
            "dataNascimento.greaterThan=" + SMALLER_DATA_NASCIMENTO,
            "dataNascimento.greaterThan=" + DEFAULT_DATA_NASCIMENTO
        );
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByGeneroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where genero equals to
        defaultPessoaFisicaFiltering("genero.equals=" + DEFAULT_GENERO, "genero.equals=" + UPDATED_GENERO);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByGeneroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where genero in
        defaultPessoaFisicaFiltering("genero.in=" + DEFAULT_GENERO + "," + UPDATED_GENERO, "genero.in=" + UPDATED_GENERO);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByGeneroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where genero is not null
        defaultPessoaFisicaFiltering("genero.specified=true", "genero.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNomeMaeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nomeMae equals to
        defaultPessoaFisicaFiltering("nomeMae.equals=" + DEFAULT_NOME_MAE, "nomeMae.equals=" + UPDATED_NOME_MAE);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNomeMaeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nomeMae in
        defaultPessoaFisicaFiltering("nomeMae.in=" + DEFAULT_NOME_MAE + "," + UPDATED_NOME_MAE, "nomeMae.in=" + UPDATED_NOME_MAE);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNomeMaeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nomeMae is not null
        defaultPessoaFisicaFiltering("nomeMae.specified=true", "nomeMae.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNomeMaeContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nomeMae contains
        defaultPessoaFisicaFiltering("nomeMae.contains=" + DEFAULT_NOME_MAE, "nomeMae.contains=" + UPDATED_NOME_MAE);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNomeMaeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nomeMae does not contain
        defaultPessoaFisicaFiltering("nomeMae.doesNotContain=" + UPDATED_NOME_MAE, "nomeMae.doesNotContain=" + DEFAULT_NOME_MAE);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByTelefoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where telefone equals to
        defaultPessoaFisicaFiltering("telefone.equals=" + DEFAULT_TELEFONE, "telefone.equals=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByTelefoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where telefone in
        defaultPessoaFisicaFiltering("telefone.in=" + DEFAULT_TELEFONE + "," + UPDATED_TELEFONE, "telefone.in=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByTelefoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where telefone is not null
        defaultPessoaFisicaFiltering("telefone.specified=true", "telefone.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByTelefoneContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where telefone contains
        defaultPessoaFisicaFiltering("telefone.contains=" + DEFAULT_TELEFONE, "telefone.contains=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByTelefoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where telefone does not contain
        defaultPessoaFisicaFiltering("telefone.doesNotContain=" + UPDATED_TELEFONE, "telefone.doesNotContain=" + DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByTelefoneVerificadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where telefoneVerificado equals to
        defaultPessoaFisicaFiltering(
            "telefoneVerificado.equals=" + DEFAULT_TELEFONE_VERIFICADO,
            "telefoneVerificado.equals=" + UPDATED_TELEFONE_VERIFICADO
        );
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByTelefoneVerificadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where telefoneVerificado in
        defaultPessoaFisicaFiltering(
            "telefoneVerificado.in=" + DEFAULT_TELEFONE_VERIFICADO + "," + UPDATED_TELEFONE_VERIFICADO,
            "telefoneVerificado.in=" + UPDATED_TELEFONE_VERIFICADO
        );
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByTelefoneVerificadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where telefoneVerificado is not null
        defaultPessoaFisicaFiltering("telefoneVerificado.specified=true", "telefoneVerificado.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNivelRiscoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nivelRisco equals to
        defaultPessoaFisicaFiltering("nivelRisco.equals=" + DEFAULT_NIVEL_RISCO, "nivelRisco.equals=" + UPDATED_NIVEL_RISCO);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNivelRiscoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nivelRisco in
        defaultPessoaFisicaFiltering(
            "nivelRisco.in=" + DEFAULT_NIVEL_RISCO + "," + UPDATED_NIVEL_RISCO,
            "nivelRisco.in=" + UPDATED_NIVEL_RISCO
        );
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNivelRiscoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where nivelRisco is not null
        defaultPessoaFisicaFiltering("nivelRisco.specified=true", "nivelRisco.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaFisicasBySituacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where situacao equals to
        defaultPessoaFisicaFiltering("situacao.equals=" + DEFAULT_SITUACAO, "situacao.equals=" + UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasBySituacaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where situacao in
        defaultPessoaFisicaFiltering("situacao.in=" + DEFAULT_SITUACAO + "," + UPDATED_SITUACAO, "situacao.in=" + UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasBySituacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where situacao is not null
        defaultPessoaFisicaFiltering("situacao.specified=true", "situacao.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByBloqueioSaqueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where bloqueioSaque equals to
        defaultPessoaFisicaFiltering("bloqueioSaque.equals=" + DEFAULT_BLOQUEIO_SAQUE, "bloqueioSaque.equals=" + UPDATED_BLOQUEIO_SAQUE);
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByBloqueioSaqueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where bloqueioSaque in
        defaultPessoaFisicaFiltering(
            "bloqueioSaque.in=" + DEFAULT_BLOQUEIO_SAQUE + "," + UPDATED_BLOQUEIO_SAQUE,
            "bloqueioSaque.in=" + UPDATED_BLOQUEIO_SAQUE
        );
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByBloqueioSaqueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        // Get all the pessoaFisicaList where bloqueioSaque is not null
        defaultPessoaFisicaFiltering("bloqueioSaque.specified=true", "bloqueioSaque.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByUsuarioIsEqualToSomething() throws Exception {
        // Get already existing entity
        User usuario = pessoaFisica.getUsuario();
        pessoaFisicaRepository.saveAndFlush(pessoaFisica);
        Long usuarioId = usuario.getId();
        // Get all the pessoaFisicaList where usuario equals to usuarioId
        defaultPessoaFisicaShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the pessoaFisicaList where usuario equals to (usuarioId + 1)
        defaultPessoaFisicaShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByMoedaPrincipalIsEqualToSomething() throws Exception {
        MoedaCarteira moedaPrincipal;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            pessoaFisicaRepository.saveAndFlush(pessoaFisica);
            moedaPrincipal = MoedaCarteiraResourceIT.createEntity(em);
        } else {
            moedaPrincipal = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        em.persist(moedaPrincipal);
        em.flush();
        pessoaFisica.setMoedaPrincipal(moedaPrincipal);
        pessoaFisicaRepository.saveAndFlush(pessoaFisica);
        Long moedaPrincipalId = moedaPrincipal.getId();
        // Get all the pessoaFisicaList where moedaPrincipal equals to moedaPrincipalId
        defaultPessoaFisicaShouldBeFound("moedaPrincipalId.equals=" + moedaPrincipalId);

        // Get all the pessoaFisicaList where moedaPrincipal equals to (moedaPrincipalId + 1)
        defaultPessoaFisicaShouldNotBeFound("moedaPrincipalId.equals=" + (moedaPrincipalId + 1));
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByNacionalidadeIsEqualToSomething() throws Exception {
        Pais nacionalidade;
        if (TestUtil.findAll(em, Pais.class).isEmpty()) {
            pessoaFisicaRepository.saveAndFlush(pessoaFisica);
            nacionalidade = PaisResourceIT.createEntity();
        } else {
            nacionalidade = TestUtil.findAll(em, Pais.class).get(0);
        }
        em.persist(nacionalidade);
        em.flush();
        pessoaFisica.setNacionalidade(nacionalidade);
        pessoaFisicaRepository.saveAndFlush(pessoaFisica);
        Long nacionalidadeId = nacionalidade.getId();
        // Get all the pessoaFisicaList where nacionalidade equals to nacionalidadeId
        defaultPessoaFisicaShouldBeFound("nacionalidadeId.equals=" + nacionalidadeId);

        // Get all the pessoaFisicaList where nacionalidade equals to (nacionalidadeId + 1)
        defaultPessoaFisicaShouldNotBeFound("nacionalidadeId.equals=" + (nacionalidadeId + 1));
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByProfissaoIsEqualToSomething() throws Exception {
        Profissao profissao;
        if (TestUtil.findAll(em, Profissao.class).isEmpty()) {
            pessoaFisicaRepository.saveAndFlush(pessoaFisica);
            profissao = ProfissaoResourceIT.createEntity();
        } else {
            profissao = TestUtil.findAll(em, Profissao.class).get(0);
        }
        em.persist(profissao);
        em.flush();
        pessoaFisica.setProfissao(profissao);
        pessoaFisicaRepository.saveAndFlush(pessoaFisica);
        Long profissaoId = profissao.getId();
        // Get all the pessoaFisicaList where profissao equals to profissaoId
        defaultPessoaFisicaShouldBeFound("profissaoId.equals=" + profissaoId);

        // Get all the pessoaFisicaList where profissao equals to (profissaoId + 1)
        defaultPessoaFisicaShouldNotBeFound("profissaoId.equals=" + (profissaoId + 1));
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByPlanoIsEqualToSomething() throws Exception {
        Plano plano;
        if (TestUtil.findAll(em, Plano.class).isEmpty()) {
            pessoaFisicaRepository.saveAndFlush(pessoaFisica);
            plano = PlanoResourceIT.createEntity();
        } else {
            plano = TestUtil.findAll(em, Plano.class).get(0);
        }
        em.persist(plano);
        em.flush();
        pessoaFisica.setPlano(plano);
        pessoaFisicaRepository.saveAndFlush(pessoaFisica);
        Long planoId = plano.getId();
        // Get all the pessoaFisicaList where plano equals to planoId
        defaultPessoaFisicaShouldBeFound("planoId.equals=" + planoId);

        // Get all the pessoaFisicaList where plano equals to (planoId + 1)
        defaultPessoaFisicaShouldNotBeFound("planoId.equals=" + (planoId + 1));
    }

    @Test
    @Transactional
    void getAllPessoaFisicasByEscritorioIsEqualToSomething() throws Exception {
        Escritorio escritorio;
        if (TestUtil.findAll(em, Escritorio.class).isEmpty()) {
            pessoaFisicaRepository.saveAndFlush(pessoaFisica);
            escritorio = EscritorioResourceIT.createEntity();
        } else {
            escritorio = TestUtil.findAll(em, Escritorio.class).get(0);
        }
        em.persist(escritorio);
        em.flush();
        pessoaFisica.setEscritorio(escritorio);
        pessoaFisicaRepository.saveAndFlush(pessoaFisica);
        Long escritorioId = escritorio.getId();
        // Get all the pessoaFisicaList where escritorio equals to escritorioId
        defaultPessoaFisicaShouldBeFound("escritorioId.equals=" + escritorioId);

        // Get all the pessoaFisicaList where escritorio equals to (escritorioId + 1)
        defaultPessoaFisicaShouldNotBeFound("escritorioId.equals=" + (escritorioId + 1));
    }

    private void defaultPessoaFisicaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPessoaFisicaShouldBeFound(shouldBeFound);
        defaultPessoaFisicaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPessoaFisicaShouldBeFound(String filter) throws Exception {
        restPessoaFisicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pessoaFisica.getId().intValue())))
            .andExpect(jsonPath("$.[*].cpf").value(hasItem(DEFAULT_CPF)))
            .andExpect(jsonPath("$.[*].nomeCompleto").value(hasItem(DEFAULT_NOME_COMPLETO)))
            .andExpect(jsonPath("$.[*].nomeSocial").value(hasItem(DEFAULT_NOME_SOCIAL)))
            .andExpect(jsonPath("$.[*].dataNascimento").value(hasItem(DEFAULT_DATA_NASCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].genero").value(hasItem(DEFAULT_GENERO.toString())))
            .andExpect(jsonPath("$.[*].nomeMae").value(hasItem(DEFAULT_NOME_MAE)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].telefoneVerificado").value(hasItem(DEFAULT_TELEFONE_VERIFICADO)))
            .andExpect(jsonPath("$.[*].nivelRisco").value(hasItem(DEFAULT_NIVEL_RISCO.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].bloqueioSaque").value(hasItem(DEFAULT_BLOQUEIO_SAQUE)));

        // Check, that the count call also returns 1
        restPessoaFisicaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPessoaFisicaShouldNotBeFound(String filter) throws Exception {
        restPessoaFisicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPessoaFisicaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPessoaFisica() throws Exception {
        // Get the pessoaFisica
        restPessoaFisicaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPessoaFisica() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pessoaFisica
        PessoaFisica updatedPessoaFisica = pessoaFisicaRepository.findById(pessoaFisica.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPessoaFisica are not directly saved in db
        em.detach(updatedPessoaFisica);
        updatedPessoaFisica
            .cpf(UPDATED_CPF)
            .nomeCompleto(UPDATED_NOME_COMPLETO)
            .nomeSocial(UPDATED_NOME_SOCIAL)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .genero(UPDATED_GENERO)
            .nomeMae(UPDATED_NOME_MAE)
            .telefone(UPDATED_TELEFONE)
            .telefoneVerificado(UPDATED_TELEFONE_VERIFICADO)
            .nivelRisco(UPDATED_NIVEL_RISCO)
            .situacao(UPDATED_SITUACAO)
            .bloqueioSaque(UPDATED_BLOQUEIO_SAQUE);
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(updatedPessoaFisica);

        restPessoaFisicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pessoaFisicaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pessoaFisicaDTO))
            )
            .andExpect(status().isOk());

        // Validate the PessoaFisica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPessoaFisicaToMatchAllProperties(updatedPessoaFisica);
    }

    @Test
    @Transactional
    void putNonExistingPessoaFisica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoaFisica.setId(longCount.incrementAndGet());

        // Create the PessoaFisica
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(pessoaFisica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPessoaFisicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pessoaFisicaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pessoaFisicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PessoaFisica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPessoaFisica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoaFisica.setId(longCount.incrementAndGet());

        // Create the PessoaFisica
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(pessoaFisica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaFisicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pessoaFisicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PessoaFisica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPessoaFisica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoaFisica.setId(longCount.incrementAndGet());

        // Create the PessoaFisica
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(pessoaFisica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaFisicaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaFisicaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PessoaFisica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePessoaFisicaWithPatch() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pessoaFisica using partial update
        PessoaFisica partialUpdatedPessoaFisica = new PessoaFisica();
        partialUpdatedPessoaFisica.setId(pessoaFisica.getId());

        partialUpdatedPessoaFisica
            .nomeCompleto(UPDATED_NOME_COMPLETO)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .nomeMae(UPDATED_NOME_MAE)
            .telefone(UPDATED_TELEFONE)
            .telefoneVerificado(UPDATED_TELEFONE_VERIFICADO)
            .situacao(UPDATED_SITUACAO);

        restPessoaFisicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPessoaFisica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPessoaFisica))
            )
            .andExpect(status().isOk());

        // Validate the PessoaFisica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPessoaFisicaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPessoaFisica, pessoaFisica),
            getPersistedPessoaFisica(pessoaFisica)
        );
    }

    @Test
    @Transactional
    void fullUpdatePessoaFisicaWithPatch() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pessoaFisica using partial update
        PessoaFisica partialUpdatedPessoaFisica = new PessoaFisica();
        partialUpdatedPessoaFisica.setId(pessoaFisica.getId());

        partialUpdatedPessoaFisica
            .cpf(UPDATED_CPF)
            .nomeCompleto(UPDATED_NOME_COMPLETO)
            .nomeSocial(UPDATED_NOME_SOCIAL)
            .dataNascimento(UPDATED_DATA_NASCIMENTO)
            .genero(UPDATED_GENERO)
            .nomeMae(UPDATED_NOME_MAE)
            .telefone(UPDATED_TELEFONE)
            .telefoneVerificado(UPDATED_TELEFONE_VERIFICADO)
            .nivelRisco(UPDATED_NIVEL_RISCO)
            .situacao(UPDATED_SITUACAO)
            .bloqueioSaque(UPDATED_BLOQUEIO_SAQUE);

        restPessoaFisicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPessoaFisica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPessoaFisica))
            )
            .andExpect(status().isOk());

        // Validate the PessoaFisica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPessoaFisicaUpdatableFieldsEquals(partialUpdatedPessoaFisica, getPersistedPessoaFisica(partialUpdatedPessoaFisica));
    }

    @Test
    @Transactional
    void patchNonExistingPessoaFisica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoaFisica.setId(longCount.incrementAndGet());

        // Create the PessoaFisica
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(pessoaFisica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPessoaFisicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pessoaFisicaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pessoaFisicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PessoaFisica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPessoaFisica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoaFisica.setId(longCount.incrementAndGet());

        // Create the PessoaFisica
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(pessoaFisica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaFisicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pessoaFisicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PessoaFisica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPessoaFisica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoaFisica.setId(longCount.incrementAndGet());

        // Create the PessoaFisica
        PessoaFisicaDTO pessoaFisicaDTO = pessoaFisicaMapper.toDto(pessoaFisica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaFisicaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pessoaFisicaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PessoaFisica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePessoaFisica() throws Exception {
        // Initialize the database
        insertedPessoaFisica = pessoaFisicaRepository.saveAndFlush(pessoaFisica);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pessoaFisica
        restPessoaFisicaMockMvc
            .perform(delete(ENTITY_API_URL_ID, pessoaFisica.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pessoaFisicaRepository.count();
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

    protected PessoaFisica getPersistedPessoaFisica(PessoaFisica pessoaFisica) {
        return pessoaFisicaRepository.findById(pessoaFisica.getId()).orElseThrow();
    }

    protected void assertPersistedPessoaFisicaToMatchAllProperties(PessoaFisica expectedPessoaFisica) {
        assertPessoaFisicaAllPropertiesEquals(expectedPessoaFisica, getPersistedPessoaFisica(expectedPessoaFisica));
    }

    protected void assertPersistedPessoaFisicaToMatchUpdatableProperties(PessoaFisica expectedPessoaFisica) {
        assertPessoaFisicaAllUpdatablePropertiesEquals(expectedPessoaFisica, getPersistedPessoaFisica(expectedPessoaFisica));
    }
}
