package com.bankernel.web.rest;

import static com.bankernel.domain.PessoaJuridicaAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bankernel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Documento;
import com.bankernel.domain.Escritorio;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.Pais;
import com.bankernel.domain.PessoaJuridica;
import com.bankernel.domain.Plano;
import com.bankernel.domain.TipoNegocio;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumNivelRisco;
import com.bankernel.domain.enumeration.EnumStatusPessoa;
import com.bankernel.domain.enumeration.EnumTipoDocumentoPJ;
import com.bankernel.repository.PessoaJuridicaRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.PessoaJuridicaService;
import com.bankernel.service.dto.PessoaJuridicaDTO;
import com.bankernel.service.mapper.PessoaJuridicaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link PessoaJuridicaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PessoaJuridicaResourceIT {

    private static final String DEFAULT_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_CNPJ = "BBBBBBBBBB";

    private static final String DEFAULT_RAZAO_SOCIAL = "AAAAAAAAAA";
    private static final String UPDATED_RAZAO_SOCIAL = "BBBBBBBBBB";

    private static final String DEFAULT_NOME_FANTASIA = "AAAAAAAAAA";
    private static final String UPDATED_NOME_FANTASIA = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_SITIO_WEB = "AAAAAAAAAA";
    private static final String UPDATED_SITIO_WEB = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_FUNDACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_FUNDACAO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_DATA_FUNDACAO = LocalDate.ofEpochDay(-1L);

    private static final BigDecimal DEFAULT_CAPITAL_SOCIAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_CAPITAL_SOCIAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_CAPITAL_SOCIAL = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_FATURAMENTO_ANUAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_FATURAMENTO_ANUAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_FATURAMENTO_ANUAL = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_MEDIA_MOVIMENTACAO_MENSAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_MEDIA_MOVIMENTACAO_MENSAL = new BigDecimal(2);
    private static final BigDecimal SMALLER_MEDIA_MOVIMENTACAO_MENSAL = new BigDecimal(1 - 1);

    private static final EnumTipoDocumentoPJ DEFAULT_TIPO_DOCUMENTO = EnumTipoDocumentoPJ.CNPJ;
    private static final EnumTipoDocumentoPJ UPDATED_TIPO_DOCUMENTO = EnumTipoDocumentoPJ.INSCRICAO_ESTADUAL;

    private static final String DEFAULT_REGIME_TRIBUTARIO = "AAAAAAAAAA";
    private static final String UPDATED_REGIME_TRIBUTARIO = "BBBBBBBBBB";

    private static final String DEFAULT_CODIGO_NATUREZA_JURIDICA = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_NATUREZA_JURIDICA = "BBBBBBBBBB";

    private static final String DEFAULT_ATIVIDADE_PRINCIPAL = "AAAAAAAAAA";
    private static final String UPDATED_ATIVIDADE_PRINCIPAL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EMPRESA_ATIVA = false;
    private static final Boolean UPDATED_EMPRESA_ATIVA = true;

    private static final EnumNivelRisco DEFAULT_NIVEL_RISCO = EnumNivelRisco.BAIXO;
    private static final EnumNivelRisco UPDATED_NIVEL_RISCO = EnumNivelRisco.MEDIO;

    private static final EnumStatusPessoa DEFAULT_SITUACAO = EnumStatusPessoa.ATIVO;
    private static final EnumStatusPessoa UPDATED_SITUACAO = EnumStatusPessoa.INATIVO;

    private static final Boolean DEFAULT_BLOQUEIO_SAQUE = false;
    private static final Boolean UPDATED_BLOQUEIO_SAQUE = true;

    private static final String DEFAULT_CPF_REPRESENTANTE_LEGAL = "AAAAAAAAAA";
    private static final String UPDATED_CPF_REPRESENTANTE_LEGAL = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_REGISTRO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_REGISTRO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/pessoa-juridicas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PessoaJuridicaRepository pessoaJuridicaRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private PessoaJuridicaRepository pessoaJuridicaRepositoryMock;

    @Autowired
    private PessoaJuridicaMapper pessoaJuridicaMapper;

    @Mock
    private PessoaJuridicaService pessoaJuridicaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPessoaJuridicaMockMvc;

    private PessoaJuridica pessoaJuridica;

    private PessoaJuridica insertedPessoaJuridica;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PessoaJuridica createEntity(EntityManager em) {
        PessoaJuridica pessoaJuridica = new PessoaJuridica()
            .cnpj(DEFAULT_CNPJ)
            .razaoSocial(DEFAULT_RAZAO_SOCIAL)
            .nomeFantasia(DEFAULT_NOME_FANTASIA)
            .telefone(DEFAULT_TELEFONE)
            .sitioWeb(DEFAULT_SITIO_WEB)
            .descricao(DEFAULT_DESCRICAO)
            .dataFundacao(DEFAULT_DATA_FUNDACAO)
            .capitalSocial(DEFAULT_CAPITAL_SOCIAL)
            .faturamentoAnual(DEFAULT_FATURAMENTO_ANUAL)
            .mediaMovimentacaoMensal(DEFAULT_MEDIA_MOVIMENTACAO_MENSAL)
            .tipoDocumento(DEFAULT_TIPO_DOCUMENTO)
            .regimeTributario(DEFAULT_REGIME_TRIBUTARIO)
            .codigoNaturezaJuridica(DEFAULT_CODIGO_NATUREZA_JURIDICA)
            .atividadePrincipal(DEFAULT_ATIVIDADE_PRINCIPAL)
            .empresaAtiva(DEFAULT_EMPRESA_ATIVA)
            .nivelRisco(DEFAULT_NIVEL_RISCO)
            .situacao(DEFAULT_SITUACAO)
            .bloqueioSaque(DEFAULT_BLOQUEIO_SAQUE)
            .cpfRepresentanteLegal(DEFAULT_CPF_REPRESENTANTE_LEGAL)
            .numeroRegistro(DEFAULT_NUMERO_REGISTRO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        pessoaJuridica.setUsuario(user);
        return pessoaJuridica;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PessoaJuridica createUpdatedEntity(EntityManager em) {
        PessoaJuridica updatedPessoaJuridica = new PessoaJuridica()
            .cnpj(UPDATED_CNPJ)
            .razaoSocial(UPDATED_RAZAO_SOCIAL)
            .nomeFantasia(UPDATED_NOME_FANTASIA)
            .telefone(UPDATED_TELEFONE)
            .sitioWeb(UPDATED_SITIO_WEB)
            .descricao(UPDATED_DESCRICAO)
            .dataFundacao(UPDATED_DATA_FUNDACAO)
            .capitalSocial(UPDATED_CAPITAL_SOCIAL)
            .faturamentoAnual(UPDATED_FATURAMENTO_ANUAL)
            .mediaMovimentacaoMensal(UPDATED_MEDIA_MOVIMENTACAO_MENSAL)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .regimeTributario(UPDATED_REGIME_TRIBUTARIO)
            .codigoNaturezaJuridica(UPDATED_CODIGO_NATUREZA_JURIDICA)
            .atividadePrincipal(UPDATED_ATIVIDADE_PRINCIPAL)
            .empresaAtiva(UPDATED_EMPRESA_ATIVA)
            .nivelRisco(UPDATED_NIVEL_RISCO)
            .situacao(UPDATED_SITUACAO)
            .bloqueioSaque(UPDATED_BLOQUEIO_SAQUE)
            .cpfRepresentanteLegal(UPDATED_CPF_REPRESENTANTE_LEGAL)
            .numeroRegistro(UPDATED_NUMERO_REGISTRO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedPessoaJuridica.setUsuario(user);
        return updatedPessoaJuridica;
    }

    @BeforeEach
    void initTest() {
        pessoaJuridica = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPessoaJuridica != null) {
            pessoaJuridicaRepository.delete(insertedPessoaJuridica);
            insertedPessoaJuridica = null;
        }
    }

    @Test
    @Transactional
    void createPessoaJuridica() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PessoaJuridica
        PessoaJuridicaDTO pessoaJuridicaDTO = pessoaJuridicaMapper.toDto(pessoaJuridica);
        var returnedPessoaJuridicaDTO = om.readValue(
            restPessoaJuridicaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaJuridicaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PessoaJuridicaDTO.class
        );

        // Validate the PessoaJuridica in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPessoaJuridica = pessoaJuridicaMapper.toEntity(returnedPessoaJuridicaDTO);
        assertPessoaJuridicaUpdatableFieldsEquals(returnedPessoaJuridica, getPersistedPessoaJuridica(returnedPessoaJuridica));

        insertedPessoaJuridica = returnedPessoaJuridica;
    }

    @Test
    @Transactional
    void createPessoaJuridicaWithExistingId() throws Exception {
        // Create the PessoaJuridica with an existing ID
        pessoaJuridica.setId(1L);
        PessoaJuridicaDTO pessoaJuridicaDTO = pessoaJuridicaMapper.toDto(pessoaJuridica);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPessoaJuridicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaJuridicaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PessoaJuridica in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCnpjIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pessoaJuridica.setCnpj(null);

        // Create the PessoaJuridica, which fails.
        PessoaJuridicaDTO pessoaJuridicaDTO = pessoaJuridicaMapper.toDto(pessoaJuridica);

        restPessoaJuridicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaJuridicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRazaoSocialIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pessoaJuridica.setRazaoSocial(null);

        // Create the PessoaJuridica, which fails.
        PessoaJuridicaDTO pessoaJuridicaDTO = pessoaJuridicaMapper.toDto(pessoaJuridica);

        restPessoaJuridicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaJuridicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTelefoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pessoaJuridica.setTelefone(null);

        // Create the PessoaJuridica, which fails.
        PessoaJuridicaDTO pessoaJuridicaDTO = pessoaJuridicaMapper.toDto(pessoaJuridica);

        restPessoaJuridicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaJuridicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmpresaAtivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pessoaJuridica.setEmpresaAtiva(null);

        // Create the PessoaJuridica, which fails.
        PessoaJuridicaDTO pessoaJuridicaDTO = pessoaJuridicaMapper.toDto(pessoaJuridica);

        restPessoaJuridicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaJuridicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNivelRiscoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pessoaJuridica.setNivelRisco(null);

        // Create the PessoaJuridica, which fails.
        PessoaJuridicaDTO pessoaJuridicaDTO = pessoaJuridicaMapper.toDto(pessoaJuridica);

        restPessoaJuridicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaJuridicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSituacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pessoaJuridica.setSituacao(null);

        // Create the PessoaJuridica, which fails.
        PessoaJuridicaDTO pessoaJuridicaDTO = pessoaJuridicaMapper.toDto(pessoaJuridica);

        restPessoaJuridicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaJuridicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBloqueioSaqueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        pessoaJuridica.setBloqueioSaque(null);

        // Create the PessoaJuridica, which fails.
        PessoaJuridicaDTO pessoaJuridicaDTO = pessoaJuridicaMapper.toDto(pessoaJuridica);

        restPessoaJuridicaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaJuridicaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicas() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList
        restPessoaJuridicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pessoaJuridica.getId().intValue())))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].razaoSocial").value(hasItem(DEFAULT_RAZAO_SOCIAL)))
            .andExpect(jsonPath("$.[*].nomeFantasia").value(hasItem(DEFAULT_NOME_FANTASIA)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].sitioWeb").value(hasItem(DEFAULT_SITIO_WEB)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].dataFundacao").value(hasItem(DEFAULT_DATA_FUNDACAO.toString())))
            .andExpect(jsonPath("$.[*].capitalSocial").value(hasItem(sameNumber(DEFAULT_CAPITAL_SOCIAL))))
            .andExpect(jsonPath("$.[*].faturamentoAnual").value(hasItem(sameNumber(DEFAULT_FATURAMENTO_ANUAL))))
            .andExpect(jsonPath("$.[*].mediaMovimentacaoMensal").value(hasItem(sameNumber(DEFAULT_MEDIA_MOVIMENTACAO_MENSAL))))
            .andExpect(jsonPath("$.[*].tipoDocumento").value(hasItem(DEFAULT_TIPO_DOCUMENTO.toString())))
            .andExpect(jsonPath("$.[*].regimeTributario").value(hasItem(DEFAULT_REGIME_TRIBUTARIO)))
            .andExpect(jsonPath("$.[*].codigoNaturezaJuridica").value(hasItem(DEFAULT_CODIGO_NATUREZA_JURIDICA)))
            .andExpect(jsonPath("$.[*].atividadePrincipal").value(hasItem(DEFAULT_ATIVIDADE_PRINCIPAL)))
            .andExpect(jsonPath("$.[*].empresaAtiva").value(hasItem(DEFAULT_EMPRESA_ATIVA)))
            .andExpect(jsonPath("$.[*].nivelRisco").value(hasItem(DEFAULT_NIVEL_RISCO.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].bloqueioSaque").value(hasItem(DEFAULT_BLOQUEIO_SAQUE)))
            .andExpect(jsonPath("$.[*].cpfRepresentanteLegal").value(hasItem(DEFAULT_CPF_REPRESENTANTE_LEGAL)))
            .andExpect(jsonPath("$.[*].numeroRegistro").value(hasItem(DEFAULT_NUMERO_REGISTRO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPessoaJuridicasWithEagerRelationshipsIsEnabled() throws Exception {
        when(pessoaJuridicaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPessoaJuridicaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(pessoaJuridicaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPessoaJuridicasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(pessoaJuridicaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPessoaJuridicaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(pessoaJuridicaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPessoaJuridica() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get the pessoaJuridica
        restPessoaJuridicaMockMvc
            .perform(get(ENTITY_API_URL_ID, pessoaJuridica.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pessoaJuridica.getId().intValue()))
            .andExpect(jsonPath("$.cnpj").value(DEFAULT_CNPJ))
            .andExpect(jsonPath("$.razaoSocial").value(DEFAULT_RAZAO_SOCIAL))
            .andExpect(jsonPath("$.nomeFantasia").value(DEFAULT_NOME_FANTASIA))
            .andExpect(jsonPath("$.telefone").value(DEFAULT_TELEFONE))
            .andExpect(jsonPath("$.sitioWeb").value(DEFAULT_SITIO_WEB))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.dataFundacao").value(DEFAULT_DATA_FUNDACAO.toString()))
            .andExpect(jsonPath("$.capitalSocial").value(sameNumber(DEFAULT_CAPITAL_SOCIAL)))
            .andExpect(jsonPath("$.faturamentoAnual").value(sameNumber(DEFAULT_FATURAMENTO_ANUAL)))
            .andExpect(jsonPath("$.mediaMovimentacaoMensal").value(sameNumber(DEFAULT_MEDIA_MOVIMENTACAO_MENSAL)))
            .andExpect(jsonPath("$.tipoDocumento").value(DEFAULT_TIPO_DOCUMENTO.toString()))
            .andExpect(jsonPath("$.regimeTributario").value(DEFAULT_REGIME_TRIBUTARIO))
            .andExpect(jsonPath("$.codigoNaturezaJuridica").value(DEFAULT_CODIGO_NATUREZA_JURIDICA))
            .andExpect(jsonPath("$.atividadePrincipal").value(DEFAULT_ATIVIDADE_PRINCIPAL))
            .andExpect(jsonPath("$.empresaAtiva").value(DEFAULT_EMPRESA_ATIVA))
            .andExpect(jsonPath("$.nivelRisco").value(DEFAULT_NIVEL_RISCO.toString()))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()))
            .andExpect(jsonPath("$.bloqueioSaque").value(DEFAULT_BLOQUEIO_SAQUE))
            .andExpect(jsonPath("$.cpfRepresentanteLegal").value(DEFAULT_CPF_REPRESENTANTE_LEGAL))
            .andExpect(jsonPath("$.numeroRegistro").value(DEFAULT_NUMERO_REGISTRO));
    }

    @Test
    @Transactional
    void getPessoaJuridicasByIdFiltering() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        Long id = pessoaJuridica.getId();

        defaultPessoaJuridicaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultPessoaJuridicaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultPessoaJuridicaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCnpjIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where cnpj equals to
        defaultPessoaJuridicaFiltering("cnpj.equals=" + DEFAULT_CNPJ, "cnpj.equals=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCnpjIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where cnpj in
        defaultPessoaJuridicaFiltering("cnpj.in=" + DEFAULT_CNPJ + "," + UPDATED_CNPJ, "cnpj.in=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCnpjIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where cnpj is not null
        defaultPessoaJuridicaFiltering("cnpj.specified=true", "cnpj.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCnpjContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where cnpj contains
        defaultPessoaJuridicaFiltering("cnpj.contains=" + DEFAULT_CNPJ, "cnpj.contains=" + UPDATED_CNPJ);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCnpjNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where cnpj does not contain
        defaultPessoaJuridicaFiltering("cnpj.doesNotContain=" + UPDATED_CNPJ, "cnpj.doesNotContain=" + DEFAULT_CNPJ);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByRazaoSocialIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where razaoSocial equals to
        defaultPessoaJuridicaFiltering("razaoSocial.equals=" + DEFAULT_RAZAO_SOCIAL, "razaoSocial.equals=" + UPDATED_RAZAO_SOCIAL);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByRazaoSocialIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where razaoSocial in
        defaultPessoaJuridicaFiltering(
            "razaoSocial.in=" + DEFAULT_RAZAO_SOCIAL + "," + UPDATED_RAZAO_SOCIAL,
            "razaoSocial.in=" + UPDATED_RAZAO_SOCIAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByRazaoSocialIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where razaoSocial is not null
        defaultPessoaJuridicaFiltering("razaoSocial.specified=true", "razaoSocial.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByRazaoSocialContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where razaoSocial contains
        defaultPessoaJuridicaFiltering("razaoSocial.contains=" + DEFAULT_RAZAO_SOCIAL, "razaoSocial.contains=" + UPDATED_RAZAO_SOCIAL);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByRazaoSocialNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where razaoSocial does not contain
        defaultPessoaJuridicaFiltering(
            "razaoSocial.doesNotContain=" + UPDATED_RAZAO_SOCIAL,
            "razaoSocial.doesNotContain=" + DEFAULT_RAZAO_SOCIAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByNomeFantasiaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where nomeFantasia equals to
        defaultPessoaJuridicaFiltering("nomeFantasia.equals=" + DEFAULT_NOME_FANTASIA, "nomeFantasia.equals=" + UPDATED_NOME_FANTASIA);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByNomeFantasiaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where nomeFantasia in
        defaultPessoaJuridicaFiltering(
            "nomeFantasia.in=" + DEFAULT_NOME_FANTASIA + "," + UPDATED_NOME_FANTASIA,
            "nomeFantasia.in=" + UPDATED_NOME_FANTASIA
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByNomeFantasiaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where nomeFantasia is not null
        defaultPessoaJuridicaFiltering("nomeFantasia.specified=true", "nomeFantasia.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByNomeFantasiaContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where nomeFantasia contains
        defaultPessoaJuridicaFiltering("nomeFantasia.contains=" + DEFAULT_NOME_FANTASIA, "nomeFantasia.contains=" + UPDATED_NOME_FANTASIA);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByNomeFantasiaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where nomeFantasia does not contain
        defaultPessoaJuridicaFiltering(
            "nomeFantasia.doesNotContain=" + UPDATED_NOME_FANTASIA,
            "nomeFantasia.doesNotContain=" + DEFAULT_NOME_FANTASIA
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByTelefoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where telefone equals to
        defaultPessoaJuridicaFiltering("telefone.equals=" + DEFAULT_TELEFONE, "telefone.equals=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByTelefoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where telefone in
        defaultPessoaJuridicaFiltering("telefone.in=" + DEFAULT_TELEFONE + "," + UPDATED_TELEFONE, "telefone.in=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByTelefoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where telefone is not null
        defaultPessoaJuridicaFiltering("telefone.specified=true", "telefone.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByTelefoneContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where telefone contains
        defaultPessoaJuridicaFiltering("telefone.contains=" + DEFAULT_TELEFONE, "telefone.contains=" + UPDATED_TELEFONE);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByTelefoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where telefone does not contain
        defaultPessoaJuridicaFiltering("telefone.doesNotContain=" + UPDATED_TELEFONE, "telefone.doesNotContain=" + DEFAULT_TELEFONE);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasBySitioWebIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where sitioWeb equals to
        defaultPessoaJuridicaFiltering("sitioWeb.equals=" + DEFAULT_SITIO_WEB, "sitioWeb.equals=" + UPDATED_SITIO_WEB);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasBySitioWebIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where sitioWeb in
        defaultPessoaJuridicaFiltering("sitioWeb.in=" + DEFAULT_SITIO_WEB + "," + UPDATED_SITIO_WEB, "sitioWeb.in=" + UPDATED_SITIO_WEB);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasBySitioWebIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where sitioWeb is not null
        defaultPessoaJuridicaFiltering("sitioWeb.specified=true", "sitioWeb.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasBySitioWebContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where sitioWeb contains
        defaultPessoaJuridicaFiltering("sitioWeb.contains=" + DEFAULT_SITIO_WEB, "sitioWeb.contains=" + UPDATED_SITIO_WEB);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasBySitioWebNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where sitioWeb does not contain
        defaultPessoaJuridicaFiltering("sitioWeb.doesNotContain=" + UPDATED_SITIO_WEB, "sitioWeb.doesNotContain=" + DEFAULT_SITIO_WEB);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where descricao equals to
        defaultPessoaJuridicaFiltering("descricao.equals=" + DEFAULT_DESCRICAO, "descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where descricao in
        defaultPessoaJuridicaFiltering("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO, "descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where descricao is not null
        defaultPessoaJuridicaFiltering("descricao.specified=true", "descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where descricao contains
        defaultPessoaJuridicaFiltering("descricao.contains=" + DEFAULT_DESCRICAO, "descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where descricao does not contain
        defaultPessoaJuridicaFiltering("descricao.doesNotContain=" + UPDATED_DESCRICAO, "descricao.doesNotContain=" + DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByDataFundacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where dataFundacao equals to
        defaultPessoaJuridicaFiltering("dataFundacao.equals=" + DEFAULT_DATA_FUNDACAO, "dataFundacao.equals=" + UPDATED_DATA_FUNDACAO);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByDataFundacaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where dataFundacao in
        defaultPessoaJuridicaFiltering(
            "dataFundacao.in=" + DEFAULT_DATA_FUNDACAO + "," + UPDATED_DATA_FUNDACAO,
            "dataFundacao.in=" + UPDATED_DATA_FUNDACAO
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByDataFundacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where dataFundacao is not null
        defaultPessoaJuridicaFiltering("dataFundacao.specified=true", "dataFundacao.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByDataFundacaoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where dataFundacao is greater than or equal to
        defaultPessoaJuridicaFiltering(
            "dataFundacao.greaterThanOrEqual=" + DEFAULT_DATA_FUNDACAO,
            "dataFundacao.greaterThanOrEqual=" + UPDATED_DATA_FUNDACAO
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByDataFundacaoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where dataFundacao is less than or equal to
        defaultPessoaJuridicaFiltering(
            "dataFundacao.lessThanOrEqual=" + DEFAULT_DATA_FUNDACAO,
            "dataFundacao.lessThanOrEqual=" + SMALLER_DATA_FUNDACAO
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByDataFundacaoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where dataFundacao is less than
        defaultPessoaJuridicaFiltering("dataFundacao.lessThan=" + UPDATED_DATA_FUNDACAO, "dataFundacao.lessThan=" + DEFAULT_DATA_FUNDACAO);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByDataFundacaoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where dataFundacao is greater than
        defaultPessoaJuridicaFiltering(
            "dataFundacao.greaterThan=" + SMALLER_DATA_FUNDACAO,
            "dataFundacao.greaterThan=" + DEFAULT_DATA_FUNDACAO
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCapitalSocialIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where capitalSocial equals to
        defaultPessoaJuridicaFiltering("capitalSocial.equals=" + DEFAULT_CAPITAL_SOCIAL, "capitalSocial.equals=" + UPDATED_CAPITAL_SOCIAL);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCapitalSocialIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where capitalSocial in
        defaultPessoaJuridicaFiltering(
            "capitalSocial.in=" + DEFAULT_CAPITAL_SOCIAL + "," + UPDATED_CAPITAL_SOCIAL,
            "capitalSocial.in=" + UPDATED_CAPITAL_SOCIAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCapitalSocialIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where capitalSocial is not null
        defaultPessoaJuridicaFiltering("capitalSocial.specified=true", "capitalSocial.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCapitalSocialIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where capitalSocial is greater than or equal to
        defaultPessoaJuridicaFiltering(
            "capitalSocial.greaterThanOrEqual=" + DEFAULT_CAPITAL_SOCIAL,
            "capitalSocial.greaterThanOrEqual=" + UPDATED_CAPITAL_SOCIAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCapitalSocialIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where capitalSocial is less than or equal to
        defaultPessoaJuridicaFiltering(
            "capitalSocial.lessThanOrEqual=" + DEFAULT_CAPITAL_SOCIAL,
            "capitalSocial.lessThanOrEqual=" + SMALLER_CAPITAL_SOCIAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCapitalSocialIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where capitalSocial is less than
        defaultPessoaJuridicaFiltering(
            "capitalSocial.lessThan=" + UPDATED_CAPITAL_SOCIAL,
            "capitalSocial.lessThan=" + DEFAULT_CAPITAL_SOCIAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCapitalSocialIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where capitalSocial is greater than
        defaultPessoaJuridicaFiltering(
            "capitalSocial.greaterThan=" + SMALLER_CAPITAL_SOCIAL,
            "capitalSocial.greaterThan=" + DEFAULT_CAPITAL_SOCIAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByFaturamentoAnualIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where faturamentoAnual equals to
        defaultPessoaJuridicaFiltering(
            "faturamentoAnual.equals=" + DEFAULT_FATURAMENTO_ANUAL,
            "faturamentoAnual.equals=" + UPDATED_FATURAMENTO_ANUAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByFaturamentoAnualIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where faturamentoAnual in
        defaultPessoaJuridicaFiltering(
            "faturamentoAnual.in=" + DEFAULT_FATURAMENTO_ANUAL + "," + UPDATED_FATURAMENTO_ANUAL,
            "faturamentoAnual.in=" + UPDATED_FATURAMENTO_ANUAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByFaturamentoAnualIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where faturamentoAnual is not null
        defaultPessoaJuridicaFiltering("faturamentoAnual.specified=true", "faturamentoAnual.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByFaturamentoAnualIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where faturamentoAnual is greater than or equal to
        defaultPessoaJuridicaFiltering(
            "faturamentoAnual.greaterThanOrEqual=" + DEFAULT_FATURAMENTO_ANUAL,
            "faturamentoAnual.greaterThanOrEqual=" + UPDATED_FATURAMENTO_ANUAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByFaturamentoAnualIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where faturamentoAnual is less than or equal to
        defaultPessoaJuridicaFiltering(
            "faturamentoAnual.lessThanOrEqual=" + DEFAULT_FATURAMENTO_ANUAL,
            "faturamentoAnual.lessThanOrEqual=" + SMALLER_FATURAMENTO_ANUAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByFaturamentoAnualIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where faturamentoAnual is less than
        defaultPessoaJuridicaFiltering(
            "faturamentoAnual.lessThan=" + UPDATED_FATURAMENTO_ANUAL,
            "faturamentoAnual.lessThan=" + DEFAULT_FATURAMENTO_ANUAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByFaturamentoAnualIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where faturamentoAnual is greater than
        defaultPessoaJuridicaFiltering(
            "faturamentoAnual.greaterThan=" + SMALLER_FATURAMENTO_ANUAL,
            "faturamentoAnual.greaterThan=" + DEFAULT_FATURAMENTO_ANUAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByMediaMovimentacaoMensalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where mediaMovimentacaoMensal equals to
        defaultPessoaJuridicaFiltering(
            "mediaMovimentacaoMensal.equals=" + DEFAULT_MEDIA_MOVIMENTACAO_MENSAL,
            "mediaMovimentacaoMensal.equals=" + UPDATED_MEDIA_MOVIMENTACAO_MENSAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByMediaMovimentacaoMensalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where mediaMovimentacaoMensal in
        defaultPessoaJuridicaFiltering(
            "mediaMovimentacaoMensal.in=" + DEFAULT_MEDIA_MOVIMENTACAO_MENSAL + "," + UPDATED_MEDIA_MOVIMENTACAO_MENSAL,
            "mediaMovimentacaoMensal.in=" + UPDATED_MEDIA_MOVIMENTACAO_MENSAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByMediaMovimentacaoMensalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where mediaMovimentacaoMensal is not null
        defaultPessoaJuridicaFiltering("mediaMovimentacaoMensal.specified=true", "mediaMovimentacaoMensal.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByMediaMovimentacaoMensalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where mediaMovimentacaoMensal is greater than or equal to
        defaultPessoaJuridicaFiltering(
            "mediaMovimentacaoMensal.greaterThanOrEqual=" + DEFAULT_MEDIA_MOVIMENTACAO_MENSAL,
            "mediaMovimentacaoMensal.greaterThanOrEqual=" + UPDATED_MEDIA_MOVIMENTACAO_MENSAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByMediaMovimentacaoMensalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where mediaMovimentacaoMensal is less than or equal to
        defaultPessoaJuridicaFiltering(
            "mediaMovimentacaoMensal.lessThanOrEqual=" + DEFAULT_MEDIA_MOVIMENTACAO_MENSAL,
            "mediaMovimentacaoMensal.lessThanOrEqual=" + SMALLER_MEDIA_MOVIMENTACAO_MENSAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByMediaMovimentacaoMensalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where mediaMovimentacaoMensal is less than
        defaultPessoaJuridicaFiltering(
            "mediaMovimentacaoMensal.lessThan=" + UPDATED_MEDIA_MOVIMENTACAO_MENSAL,
            "mediaMovimentacaoMensal.lessThan=" + DEFAULT_MEDIA_MOVIMENTACAO_MENSAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByMediaMovimentacaoMensalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where mediaMovimentacaoMensal is greater than
        defaultPessoaJuridicaFiltering(
            "mediaMovimentacaoMensal.greaterThan=" + SMALLER_MEDIA_MOVIMENTACAO_MENSAL,
            "mediaMovimentacaoMensal.greaterThan=" + DEFAULT_MEDIA_MOVIMENTACAO_MENSAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByTipoDocumentoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where tipoDocumento equals to
        defaultPessoaJuridicaFiltering("tipoDocumento.equals=" + DEFAULT_TIPO_DOCUMENTO, "tipoDocumento.equals=" + UPDATED_TIPO_DOCUMENTO);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByTipoDocumentoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where tipoDocumento in
        defaultPessoaJuridicaFiltering(
            "tipoDocumento.in=" + DEFAULT_TIPO_DOCUMENTO + "," + UPDATED_TIPO_DOCUMENTO,
            "tipoDocumento.in=" + UPDATED_TIPO_DOCUMENTO
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByTipoDocumentoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where tipoDocumento is not null
        defaultPessoaJuridicaFiltering("tipoDocumento.specified=true", "tipoDocumento.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByRegimeTributarioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where regimeTributario equals to
        defaultPessoaJuridicaFiltering(
            "regimeTributario.equals=" + DEFAULT_REGIME_TRIBUTARIO,
            "regimeTributario.equals=" + UPDATED_REGIME_TRIBUTARIO
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByRegimeTributarioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where regimeTributario in
        defaultPessoaJuridicaFiltering(
            "regimeTributario.in=" + DEFAULT_REGIME_TRIBUTARIO + "," + UPDATED_REGIME_TRIBUTARIO,
            "regimeTributario.in=" + UPDATED_REGIME_TRIBUTARIO
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByRegimeTributarioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where regimeTributario is not null
        defaultPessoaJuridicaFiltering("regimeTributario.specified=true", "regimeTributario.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByRegimeTributarioContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where regimeTributario contains
        defaultPessoaJuridicaFiltering(
            "regimeTributario.contains=" + DEFAULT_REGIME_TRIBUTARIO,
            "regimeTributario.contains=" + UPDATED_REGIME_TRIBUTARIO
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByRegimeTributarioNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where regimeTributario does not contain
        defaultPessoaJuridicaFiltering(
            "regimeTributario.doesNotContain=" + UPDATED_REGIME_TRIBUTARIO,
            "regimeTributario.doesNotContain=" + DEFAULT_REGIME_TRIBUTARIO
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCodigoNaturezaJuridicaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where codigoNaturezaJuridica equals to
        defaultPessoaJuridicaFiltering(
            "codigoNaturezaJuridica.equals=" + DEFAULT_CODIGO_NATUREZA_JURIDICA,
            "codigoNaturezaJuridica.equals=" + UPDATED_CODIGO_NATUREZA_JURIDICA
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCodigoNaturezaJuridicaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where codigoNaturezaJuridica in
        defaultPessoaJuridicaFiltering(
            "codigoNaturezaJuridica.in=" + DEFAULT_CODIGO_NATUREZA_JURIDICA + "," + UPDATED_CODIGO_NATUREZA_JURIDICA,
            "codigoNaturezaJuridica.in=" + UPDATED_CODIGO_NATUREZA_JURIDICA
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCodigoNaturezaJuridicaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where codigoNaturezaJuridica is not null
        defaultPessoaJuridicaFiltering("codigoNaturezaJuridica.specified=true", "codigoNaturezaJuridica.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCodigoNaturezaJuridicaContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where codigoNaturezaJuridica contains
        defaultPessoaJuridicaFiltering(
            "codigoNaturezaJuridica.contains=" + DEFAULT_CODIGO_NATUREZA_JURIDICA,
            "codigoNaturezaJuridica.contains=" + UPDATED_CODIGO_NATUREZA_JURIDICA
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCodigoNaturezaJuridicaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where codigoNaturezaJuridica does not contain
        defaultPessoaJuridicaFiltering(
            "codigoNaturezaJuridica.doesNotContain=" + UPDATED_CODIGO_NATUREZA_JURIDICA,
            "codigoNaturezaJuridica.doesNotContain=" + DEFAULT_CODIGO_NATUREZA_JURIDICA
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByAtividadePrincipalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where atividadePrincipal equals to
        defaultPessoaJuridicaFiltering(
            "atividadePrincipal.equals=" + DEFAULT_ATIVIDADE_PRINCIPAL,
            "atividadePrincipal.equals=" + UPDATED_ATIVIDADE_PRINCIPAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByAtividadePrincipalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where atividadePrincipal in
        defaultPessoaJuridicaFiltering(
            "atividadePrincipal.in=" + DEFAULT_ATIVIDADE_PRINCIPAL + "," + UPDATED_ATIVIDADE_PRINCIPAL,
            "atividadePrincipal.in=" + UPDATED_ATIVIDADE_PRINCIPAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByAtividadePrincipalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where atividadePrincipal is not null
        defaultPessoaJuridicaFiltering("atividadePrincipal.specified=true", "atividadePrincipal.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByAtividadePrincipalContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where atividadePrincipal contains
        defaultPessoaJuridicaFiltering(
            "atividadePrincipal.contains=" + DEFAULT_ATIVIDADE_PRINCIPAL,
            "atividadePrincipal.contains=" + UPDATED_ATIVIDADE_PRINCIPAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByAtividadePrincipalNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where atividadePrincipal does not contain
        defaultPessoaJuridicaFiltering(
            "atividadePrincipal.doesNotContain=" + UPDATED_ATIVIDADE_PRINCIPAL,
            "atividadePrincipal.doesNotContain=" + DEFAULT_ATIVIDADE_PRINCIPAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByEmpresaAtivaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where empresaAtiva equals to
        defaultPessoaJuridicaFiltering("empresaAtiva.equals=" + DEFAULT_EMPRESA_ATIVA, "empresaAtiva.equals=" + UPDATED_EMPRESA_ATIVA);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByEmpresaAtivaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where empresaAtiva in
        defaultPessoaJuridicaFiltering(
            "empresaAtiva.in=" + DEFAULT_EMPRESA_ATIVA + "," + UPDATED_EMPRESA_ATIVA,
            "empresaAtiva.in=" + UPDATED_EMPRESA_ATIVA
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByEmpresaAtivaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where empresaAtiva is not null
        defaultPessoaJuridicaFiltering("empresaAtiva.specified=true", "empresaAtiva.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByNivelRiscoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where nivelRisco equals to
        defaultPessoaJuridicaFiltering("nivelRisco.equals=" + DEFAULT_NIVEL_RISCO, "nivelRisco.equals=" + UPDATED_NIVEL_RISCO);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByNivelRiscoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where nivelRisco in
        defaultPessoaJuridicaFiltering(
            "nivelRisco.in=" + DEFAULT_NIVEL_RISCO + "," + UPDATED_NIVEL_RISCO,
            "nivelRisco.in=" + UPDATED_NIVEL_RISCO
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByNivelRiscoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where nivelRisco is not null
        defaultPessoaJuridicaFiltering("nivelRisco.specified=true", "nivelRisco.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasBySituacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where situacao equals to
        defaultPessoaJuridicaFiltering("situacao.equals=" + DEFAULT_SITUACAO, "situacao.equals=" + UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasBySituacaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where situacao in
        defaultPessoaJuridicaFiltering("situacao.in=" + DEFAULT_SITUACAO + "," + UPDATED_SITUACAO, "situacao.in=" + UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasBySituacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where situacao is not null
        defaultPessoaJuridicaFiltering("situacao.specified=true", "situacao.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByBloqueioSaqueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where bloqueioSaque equals to
        defaultPessoaJuridicaFiltering("bloqueioSaque.equals=" + DEFAULT_BLOQUEIO_SAQUE, "bloqueioSaque.equals=" + UPDATED_BLOQUEIO_SAQUE);
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByBloqueioSaqueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where bloqueioSaque in
        defaultPessoaJuridicaFiltering(
            "bloqueioSaque.in=" + DEFAULT_BLOQUEIO_SAQUE + "," + UPDATED_BLOQUEIO_SAQUE,
            "bloqueioSaque.in=" + UPDATED_BLOQUEIO_SAQUE
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByBloqueioSaqueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where bloqueioSaque is not null
        defaultPessoaJuridicaFiltering("bloqueioSaque.specified=true", "bloqueioSaque.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCpfRepresentanteLegalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where cpfRepresentanteLegal equals to
        defaultPessoaJuridicaFiltering(
            "cpfRepresentanteLegal.equals=" + DEFAULT_CPF_REPRESENTANTE_LEGAL,
            "cpfRepresentanteLegal.equals=" + UPDATED_CPF_REPRESENTANTE_LEGAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCpfRepresentanteLegalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where cpfRepresentanteLegal in
        defaultPessoaJuridicaFiltering(
            "cpfRepresentanteLegal.in=" + DEFAULT_CPF_REPRESENTANTE_LEGAL + "," + UPDATED_CPF_REPRESENTANTE_LEGAL,
            "cpfRepresentanteLegal.in=" + UPDATED_CPF_REPRESENTANTE_LEGAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCpfRepresentanteLegalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where cpfRepresentanteLegal is not null
        defaultPessoaJuridicaFiltering("cpfRepresentanteLegal.specified=true", "cpfRepresentanteLegal.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCpfRepresentanteLegalContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where cpfRepresentanteLegal contains
        defaultPessoaJuridicaFiltering(
            "cpfRepresentanteLegal.contains=" + DEFAULT_CPF_REPRESENTANTE_LEGAL,
            "cpfRepresentanteLegal.contains=" + UPDATED_CPF_REPRESENTANTE_LEGAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByCpfRepresentanteLegalNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where cpfRepresentanteLegal does not contain
        defaultPessoaJuridicaFiltering(
            "cpfRepresentanteLegal.doesNotContain=" + UPDATED_CPF_REPRESENTANTE_LEGAL,
            "cpfRepresentanteLegal.doesNotContain=" + DEFAULT_CPF_REPRESENTANTE_LEGAL
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByNumeroRegistroIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where numeroRegistro equals to
        defaultPessoaJuridicaFiltering(
            "numeroRegistro.equals=" + DEFAULT_NUMERO_REGISTRO,
            "numeroRegistro.equals=" + UPDATED_NUMERO_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByNumeroRegistroIsInShouldWork() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where numeroRegistro in
        defaultPessoaJuridicaFiltering(
            "numeroRegistro.in=" + DEFAULT_NUMERO_REGISTRO + "," + UPDATED_NUMERO_REGISTRO,
            "numeroRegistro.in=" + UPDATED_NUMERO_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByNumeroRegistroIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where numeroRegistro is not null
        defaultPessoaJuridicaFiltering("numeroRegistro.specified=true", "numeroRegistro.specified=false");
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByNumeroRegistroContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where numeroRegistro contains
        defaultPessoaJuridicaFiltering(
            "numeroRegistro.contains=" + DEFAULT_NUMERO_REGISTRO,
            "numeroRegistro.contains=" + UPDATED_NUMERO_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByNumeroRegistroNotContainsSomething() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        // Get all the pessoaJuridicaList where numeroRegistro does not contain
        defaultPessoaJuridicaFiltering(
            "numeroRegistro.doesNotContain=" + UPDATED_NUMERO_REGISTRO,
            "numeroRegistro.doesNotContain=" + DEFAULT_NUMERO_REGISTRO
        );
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByUsuarioIsEqualToSomething() throws Exception {
        // Get already existing entity
        User usuario = pessoaJuridica.getUsuario();
        pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);
        Long usuarioId = usuario.getId();
        // Get all the pessoaJuridicaList where usuario equals to usuarioId
        defaultPessoaJuridicaShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the pessoaJuridicaList where usuario equals to (usuarioId + 1)
        defaultPessoaJuridicaShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByMoedaPrincipalIsEqualToSomething() throws Exception {
        MoedaCarteira moedaPrincipal;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);
            moedaPrincipal = MoedaCarteiraResourceIT.createEntity(em);
        } else {
            moedaPrincipal = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        em.persist(moedaPrincipal);
        em.flush();
        pessoaJuridica.setMoedaPrincipal(moedaPrincipal);
        pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);
        Long moedaPrincipalId = moedaPrincipal.getId();
        // Get all the pessoaJuridicaList where moedaPrincipal equals to moedaPrincipalId
        defaultPessoaJuridicaShouldBeFound("moedaPrincipalId.equals=" + moedaPrincipalId);

        // Get all the pessoaJuridicaList where moedaPrincipal equals to (moedaPrincipalId + 1)
        defaultPessoaJuridicaShouldNotBeFound("moedaPrincipalId.equals=" + (moedaPrincipalId + 1));
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByContratoSocialIsEqualToSomething() throws Exception {
        Documento contratoSocial;
        if (TestUtil.findAll(em, Documento.class).isEmpty()) {
            pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);
            contratoSocial = DocumentoResourceIT.createEntity();
        } else {
            contratoSocial = TestUtil.findAll(em, Documento.class).get(0);
        }
        em.persist(contratoSocial);
        em.flush();
        pessoaJuridica.setContratoSocial(contratoSocial);
        pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);
        Long contratoSocialId = contratoSocial.getId();
        // Get all the pessoaJuridicaList where contratoSocial equals to contratoSocialId
        defaultPessoaJuridicaShouldBeFound("contratoSocialId.equals=" + contratoSocialId);

        // Get all the pessoaJuridicaList where contratoSocial equals to (contratoSocialId + 1)
        defaultPessoaJuridicaShouldNotBeFound("contratoSocialId.equals=" + (contratoSocialId + 1));
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByNacionalidadeIsEqualToSomething() throws Exception {
        Pais nacionalidade;
        if (TestUtil.findAll(em, Pais.class).isEmpty()) {
            pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);
            nacionalidade = PaisResourceIT.createEntity();
        } else {
            nacionalidade = TestUtil.findAll(em, Pais.class).get(0);
        }
        em.persist(nacionalidade);
        em.flush();
        pessoaJuridica.setNacionalidade(nacionalidade);
        pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);
        Long nacionalidadeId = nacionalidade.getId();
        // Get all the pessoaJuridicaList where nacionalidade equals to nacionalidadeId
        defaultPessoaJuridicaShouldBeFound("nacionalidadeId.equals=" + nacionalidadeId);

        // Get all the pessoaJuridicaList where nacionalidade equals to (nacionalidadeId + 1)
        defaultPessoaJuridicaShouldNotBeFound("nacionalidadeId.equals=" + (nacionalidadeId + 1));
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByTipoNegocioIsEqualToSomething() throws Exception {
        TipoNegocio tipoNegocio;
        if (TestUtil.findAll(em, TipoNegocio.class).isEmpty()) {
            pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);
            tipoNegocio = TipoNegocioResourceIT.createEntity();
        } else {
            tipoNegocio = TestUtil.findAll(em, TipoNegocio.class).get(0);
        }
        em.persist(tipoNegocio);
        em.flush();
        pessoaJuridica.setTipoNegocio(tipoNegocio);
        pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);
        Long tipoNegocioId = tipoNegocio.getId();
        // Get all the pessoaJuridicaList where tipoNegocio equals to tipoNegocioId
        defaultPessoaJuridicaShouldBeFound("tipoNegocioId.equals=" + tipoNegocioId);

        // Get all the pessoaJuridicaList where tipoNegocio equals to (tipoNegocioId + 1)
        defaultPessoaJuridicaShouldNotBeFound("tipoNegocioId.equals=" + (tipoNegocioId + 1));
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByPlanoIsEqualToSomething() throws Exception {
        Plano plano;
        if (TestUtil.findAll(em, Plano.class).isEmpty()) {
            pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);
            plano = PlanoResourceIT.createEntity();
        } else {
            plano = TestUtil.findAll(em, Plano.class).get(0);
        }
        em.persist(plano);
        em.flush();
        pessoaJuridica.setPlano(plano);
        pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);
        Long planoId = plano.getId();
        // Get all the pessoaJuridicaList where plano equals to planoId
        defaultPessoaJuridicaShouldBeFound("planoId.equals=" + planoId);

        // Get all the pessoaJuridicaList where plano equals to (planoId + 1)
        defaultPessoaJuridicaShouldNotBeFound("planoId.equals=" + (planoId + 1));
    }

    @Test
    @Transactional
    void getAllPessoaJuridicasByEscritorioIsEqualToSomething() throws Exception {
        Escritorio escritorio;
        if (TestUtil.findAll(em, Escritorio.class).isEmpty()) {
            pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);
            escritorio = EscritorioResourceIT.createEntity();
        } else {
            escritorio = TestUtil.findAll(em, Escritorio.class).get(0);
        }
        em.persist(escritorio);
        em.flush();
        pessoaJuridica.setEscritorio(escritorio);
        pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);
        Long escritorioId = escritorio.getId();
        // Get all the pessoaJuridicaList where escritorio equals to escritorioId
        defaultPessoaJuridicaShouldBeFound("escritorioId.equals=" + escritorioId);

        // Get all the pessoaJuridicaList where escritorio equals to (escritorioId + 1)
        defaultPessoaJuridicaShouldNotBeFound("escritorioId.equals=" + (escritorioId + 1));
    }

    private void defaultPessoaJuridicaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultPessoaJuridicaShouldBeFound(shouldBeFound);
        defaultPessoaJuridicaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPessoaJuridicaShouldBeFound(String filter) throws Exception {
        restPessoaJuridicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pessoaJuridica.getId().intValue())))
            .andExpect(jsonPath("$.[*].cnpj").value(hasItem(DEFAULT_CNPJ)))
            .andExpect(jsonPath("$.[*].razaoSocial").value(hasItem(DEFAULT_RAZAO_SOCIAL)))
            .andExpect(jsonPath("$.[*].nomeFantasia").value(hasItem(DEFAULT_NOME_FANTASIA)))
            .andExpect(jsonPath("$.[*].telefone").value(hasItem(DEFAULT_TELEFONE)))
            .andExpect(jsonPath("$.[*].sitioWeb").value(hasItem(DEFAULT_SITIO_WEB)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].dataFundacao").value(hasItem(DEFAULT_DATA_FUNDACAO.toString())))
            .andExpect(jsonPath("$.[*].capitalSocial").value(hasItem(sameNumber(DEFAULT_CAPITAL_SOCIAL))))
            .andExpect(jsonPath("$.[*].faturamentoAnual").value(hasItem(sameNumber(DEFAULT_FATURAMENTO_ANUAL))))
            .andExpect(jsonPath("$.[*].mediaMovimentacaoMensal").value(hasItem(sameNumber(DEFAULT_MEDIA_MOVIMENTACAO_MENSAL))))
            .andExpect(jsonPath("$.[*].tipoDocumento").value(hasItem(DEFAULT_TIPO_DOCUMENTO.toString())))
            .andExpect(jsonPath("$.[*].regimeTributario").value(hasItem(DEFAULT_REGIME_TRIBUTARIO)))
            .andExpect(jsonPath("$.[*].codigoNaturezaJuridica").value(hasItem(DEFAULT_CODIGO_NATUREZA_JURIDICA)))
            .andExpect(jsonPath("$.[*].atividadePrincipal").value(hasItem(DEFAULT_ATIVIDADE_PRINCIPAL)))
            .andExpect(jsonPath("$.[*].empresaAtiva").value(hasItem(DEFAULT_EMPRESA_ATIVA)))
            .andExpect(jsonPath("$.[*].nivelRisco").value(hasItem(DEFAULT_NIVEL_RISCO.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].bloqueioSaque").value(hasItem(DEFAULT_BLOQUEIO_SAQUE)))
            .andExpect(jsonPath("$.[*].cpfRepresentanteLegal").value(hasItem(DEFAULT_CPF_REPRESENTANTE_LEGAL)))
            .andExpect(jsonPath("$.[*].numeroRegistro").value(hasItem(DEFAULT_NUMERO_REGISTRO)));

        // Check, that the count call also returns 1
        restPessoaJuridicaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPessoaJuridicaShouldNotBeFound(String filter) throws Exception {
        restPessoaJuridicaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPessoaJuridicaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingPessoaJuridica() throws Exception {
        // Get the pessoaJuridica
        restPessoaJuridicaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPessoaJuridica() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pessoaJuridica
        PessoaJuridica updatedPessoaJuridica = pessoaJuridicaRepository.findById(pessoaJuridica.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPessoaJuridica are not directly saved in db
        em.detach(updatedPessoaJuridica);
        updatedPessoaJuridica
            .cnpj(UPDATED_CNPJ)
            .razaoSocial(UPDATED_RAZAO_SOCIAL)
            .nomeFantasia(UPDATED_NOME_FANTASIA)
            .telefone(UPDATED_TELEFONE)
            .sitioWeb(UPDATED_SITIO_WEB)
            .descricao(UPDATED_DESCRICAO)
            .dataFundacao(UPDATED_DATA_FUNDACAO)
            .capitalSocial(UPDATED_CAPITAL_SOCIAL)
            .faturamentoAnual(UPDATED_FATURAMENTO_ANUAL)
            .mediaMovimentacaoMensal(UPDATED_MEDIA_MOVIMENTACAO_MENSAL)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .regimeTributario(UPDATED_REGIME_TRIBUTARIO)
            .codigoNaturezaJuridica(UPDATED_CODIGO_NATUREZA_JURIDICA)
            .atividadePrincipal(UPDATED_ATIVIDADE_PRINCIPAL)
            .empresaAtiva(UPDATED_EMPRESA_ATIVA)
            .nivelRisco(UPDATED_NIVEL_RISCO)
            .situacao(UPDATED_SITUACAO)
            .bloqueioSaque(UPDATED_BLOQUEIO_SAQUE)
            .cpfRepresentanteLegal(UPDATED_CPF_REPRESENTANTE_LEGAL)
            .numeroRegistro(UPDATED_NUMERO_REGISTRO);
        PessoaJuridicaDTO pessoaJuridicaDTO = pessoaJuridicaMapper.toDto(updatedPessoaJuridica);

        restPessoaJuridicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pessoaJuridicaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pessoaJuridicaDTO))
            )
            .andExpect(status().isOk());

        // Validate the PessoaJuridica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPessoaJuridicaToMatchAllProperties(updatedPessoaJuridica);
    }

    @Test
    @Transactional
    void putNonExistingPessoaJuridica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoaJuridica.setId(longCount.incrementAndGet());

        // Create the PessoaJuridica
        PessoaJuridicaDTO pessoaJuridicaDTO = pessoaJuridicaMapper.toDto(pessoaJuridica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPessoaJuridicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pessoaJuridicaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pessoaJuridicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PessoaJuridica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPessoaJuridica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoaJuridica.setId(longCount.incrementAndGet());

        // Create the PessoaJuridica
        PessoaJuridicaDTO pessoaJuridicaDTO = pessoaJuridicaMapper.toDto(pessoaJuridica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaJuridicaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(pessoaJuridicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PessoaJuridica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPessoaJuridica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoaJuridica.setId(longCount.incrementAndGet());

        // Create the PessoaJuridica
        PessoaJuridicaDTO pessoaJuridicaDTO = pessoaJuridicaMapper.toDto(pessoaJuridica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaJuridicaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(pessoaJuridicaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PessoaJuridica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePessoaJuridicaWithPatch() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pessoaJuridica using partial update
        PessoaJuridica partialUpdatedPessoaJuridica = new PessoaJuridica();
        partialUpdatedPessoaJuridica.setId(pessoaJuridica.getId());

        partialUpdatedPessoaJuridica
            .cnpj(UPDATED_CNPJ)
            .nomeFantasia(UPDATED_NOME_FANTASIA)
            .sitioWeb(UPDATED_SITIO_WEB)
            .descricao(UPDATED_DESCRICAO)
            .dataFundacao(UPDATED_DATA_FUNDACAO)
            .faturamentoAnual(UPDATED_FATURAMENTO_ANUAL)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .codigoNaturezaJuridica(UPDATED_CODIGO_NATUREZA_JURIDICA)
            .empresaAtiva(UPDATED_EMPRESA_ATIVA)
            .nivelRisco(UPDATED_NIVEL_RISCO)
            .situacao(UPDATED_SITUACAO)
            .bloqueioSaque(UPDATED_BLOQUEIO_SAQUE);

        restPessoaJuridicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPessoaJuridica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPessoaJuridica))
            )
            .andExpect(status().isOk());

        // Validate the PessoaJuridica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPessoaJuridicaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPessoaJuridica, pessoaJuridica),
            getPersistedPessoaJuridica(pessoaJuridica)
        );
    }

    @Test
    @Transactional
    void fullUpdatePessoaJuridicaWithPatch() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the pessoaJuridica using partial update
        PessoaJuridica partialUpdatedPessoaJuridica = new PessoaJuridica();
        partialUpdatedPessoaJuridica.setId(pessoaJuridica.getId());

        partialUpdatedPessoaJuridica
            .cnpj(UPDATED_CNPJ)
            .razaoSocial(UPDATED_RAZAO_SOCIAL)
            .nomeFantasia(UPDATED_NOME_FANTASIA)
            .telefone(UPDATED_TELEFONE)
            .sitioWeb(UPDATED_SITIO_WEB)
            .descricao(UPDATED_DESCRICAO)
            .dataFundacao(UPDATED_DATA_FUNDACAO)
            .capitalSocial(UPDATED_CAPITAL_SOCIAL)
            .faturamentoAnual(UPDATED_FATURAMENTO_ANUAL)
            .mediaMovimentacaoMensal(UPDATED_MEDIA_MOVIMENTACAO_MENSAL)
            .tipoDocumento(UPDATED_TIPO_DOCUMENTO)
            .regimeTributario(UPDATED_REGIME_TRIBUTARIO)
            .codigoNaturezaJuridica(UPDATED_CODIGO_NATUREZA_JURIDICA)
            .atividadePrincipal(UPDATED_ATIVIDADE_PRINCIPAL)
            .empresaAtiva(UPDATED_EMPRESA_ATIVA)
            .nivelRisco(UPDATED_NIVEL_RISCO)
            .situacao(UPDATED_SITUACAO)
            .bloqueioSaque(UPDATED_BLOQUEIO_SAQUE)
            .cpfRepresentanteLegal(UPDATED_CPF_REPRESENTANTE_LEGAL)
            .numeroRegistro(UPDATED_NUMERO_REGISTRO);

        restPessoaJuridicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPessoaJuridica.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPessoaJuridica))
            )
            .andExpect(status().isOk());

        // Validate the PessoaJuridica in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPessoaJuridicaUpdatableFieldsEquals(partialUpdatedPessoaJuridica, getPersistedPessoaJuridica(partialUpdatedPessoaJuridica));
    }

    @Test
    @Transactional
    void patchNonExistingPessoaJuridica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoaJuridica.setId(longCount.incrementAndGet());

        // Create the PessoaJuridica
        PessoaJuridicaDTO pessoaJuridicaDTO = pessoaJuridicaMapper.toDto(pessoaJuridica);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPessoaJuridicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pessoaJuridicaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pessoaJuridicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PessoaJuridica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPessoaJuridica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoaJuridica.setId(longCount.incrementAndGet());

        // Create the PessoaJuridica
        PessoaJuridicaDTO pessoaJuridicaDTO = pessoaJuridicaMapper.toDto(pessoaJuridica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaJuridicaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(pessoaJuridicaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PessoaJuridica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPessoaJuridica() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        pessoaJuridica.setId(longCount.incrementAndGet());

        // Create the PessoaJuridica
        PessoaJuridicaDTO pessoaJuridicaDTO = pessoaJuridicaMapper.toDto(pessoaJuridica);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPessoaJuridicaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(pessoaJuridicaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PessoaJuridica in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePessoaJuridica() throws Exception {
        // Initialize the database
        insertedPessoaJuridica = pessoaJuridicaRepository.saveAndFlush(pessoaJuridica);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the pessoaJuridica
        restPessoaJuridicaMockMvc
            .perform(delete(ENTITY_API_URL_ID, pessoaJuridica.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return pessoaJuridicaRepository.count();
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

    protected PessoaJuridica getPersistedPessoaJuridica(PessoaJuridica pessoaJuridica) {
        return pessoaJuridicaRepository.findById(pessoaJuridica.getId()).orElseThrow();
    }

    protected void assertPersistedPessoaJuridicaToMatchAllProperties(PessoaJuridica expectedPessoaJuridica) {
        assertPessoaJuridicaAllPropertiesEquals(expectedPessoaJuridica, getPersistedPessoaJuridica(expectedPessoaJuridica));
    }

    protected void assertPersistedPessoaJuridicaToMatchUpdatableProperties(PessoaJuridica expectedPessoaJuridica) {
        assertPessoaJuridicaAllUpdatablePropertiesEquals(expectedPessoaJuridica, getPersistedPessoaJuridica(expectedPessoaJuridica));
    }
}
