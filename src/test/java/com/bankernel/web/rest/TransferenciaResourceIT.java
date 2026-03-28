package com.bankernel.web.rest;

import static com.bankernel.domain.TransferenciaAsserts.*;
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
import com.bankernel.domain.Transacao;
import com.bankernel.domain.Transferencia;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumStatusTransferencia;
import com.bankernel.domain.enumeration.EnumTipoChaveTransferencia;
import com.bankernel.repository.TransferenciaRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.TransferenciaService;
import com.bankernel.service.dto.TransferenciaDTO;
import com.bankernel.service.mapper.TransferenciaMapper;
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
 * Integration tests for the {@link TransferenciaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TransferenciaResourceIT {

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR = new BigDecimal(1 - 1);

    private static final String DEFAULT_CHAVE_INTERNA = "AAAAAAAAAA";
    private static final String UPDATED_CHAVE_INTERNA = "BBBBBBBBBB";

    private static final EnumTipoChaveTransferencia DEFAULT_TIPO_CHAVE = EnumTipoChaveTransferencia.EMAIL;
    private static final EnumTipoChaveTransferencia UPDATED_TIPO_CHAVE = EnumTipoChaveTransferencia.CPF;

    private static final EnumStatusTransferencia DEFAULT_SITUACAO = EnumStatusTransferencia.PENDENTE;
    private static final EnumStatusTransferencia UPDATED_SITUACAO = EnumStatusTransferencia.APROVADA;

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final String DEFAULT_MOTIVO_REJEICAO = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO_REJEICAO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_REFERENCIA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transferencias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransferenciaRepository transferenciaRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private TransferenciaRepository transferenciaRepositoryMock;

    @Autowired
    private TransferenciaMapper transferenciaMapper;

    @Mock
    private TransferenciaService transferenciaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransferenciaMockMvc;

    private Transferencia transferencia;

    private Transferencia insertedTransferencia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transferencia createEntity(EntityManager em) {
        Transferencia transferencia = new Transferencia()
            .valor(DEFAULT_VALOR)
            .chaveInterna(DEFAULT_CHAVE_INTERNA)
            .tipoChave(DEFAULT_TIPO_CHAVE)
            .situacao(DEFAULT_SITUACAO)
            .descricao(DEFAULT_DESCRICAO)
            .motivoRejeicao(DEFAULT_MOTIVO_REJEICAO)
            .numeroReferencia(DEFAULT_NUMERO_REFERENCIA);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        transferencia.setUsuarioOrigem(user);
        // Add required entity
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            carteira = CarteiraResourceIT.createEntity(em);
            em.persist(carteira);
            em.flush();
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        transferencia.setCarteiraOrigem(carteira);
        // Add required entity
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            moedaCarteira = MoedaCarteiraResourceIT.createEntity(em);
            em.persist(moedaCarteira);
            em.flush();
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        transferencia.setMoedaCarteira(moedaCarteira);
        return transferencia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Transferencia createUpdatedEntity(EntityManager em) {
        Transferencia updatedTransferencia = new Transferencia()
            .valor(UPDATED_VALOR)
            .chaveInterna(UPDATED_CHAVE_INTERNA)
            .tipoChave(UPDATED_TIPO_CHAVE)
            .situacao(UPDATED_SITUACAO)
            .descricao(UPDATED_DESCRICAO)
            .motivoRejeicao(UPDATED_MOTIVO_REJEICAO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedTransferencia.setUsuarioOrigem(user);
        // Add required entity
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            carteira = CarteiraResourceIT.createUpdatedEntity(em);
            em.persist(carteira);
            em.flush();
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        updatedTransferencia.setCarteiraOrigem(carteira);
        // Add required entity
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            moedaCarteira = MoedaCarteiraResourceIT.createUpdatedEntity(em);
            em.persist(moedaCarteira);
            em.flush();
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        updatedTransferencia.setMoedaCarteira(moedaCarteira);
        return updatedTransferencia;
    }

    @BeforeEach
    void initTest() {
        transferencia = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTransferencia != null) {
            transferenciaRepository.delete(insertedTransferencia);
            insertedTransferencia = null;
        }
    }

    @Test
    @Transactional
    void createTransferencia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Transferencia
        TransferenciaDTO transferenciaDTO = transferenciaMapper.toDto(transferencia);
        var returnedTransferenciaDTO = om.readValue(
            restTransferenciaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transferenciaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransferenciaDTO.class
        );

        // Validate the Transferencia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransferencia = transferenciaMapper.toEntity(returnedTransferenciaDTO);
        assertTransferenciaUpdatableFieldsEquals(returnedTransferencia, getPersistedTransferencia(returnedTransferencia));

        insertedTransferencia = returnedTransferencia;
    }

    @Test
    @Transactional
    void createTransferenciaWithExistingId() throws Exception {
        // Create the Transferencia with an existing ID
        transferencia.setId(1L);
        TransferenciaDTO transferenciaDTO = transferenciaMapper.toDto(transferencia);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransferenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transferenciaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Transferencia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transferencia.setValor(null);

        // Create the Transferencia, which fails.
        TransferenciaDTO transferenciaDTO = transferenciaMapper.toDto(transferencia);

        restTransferenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transferenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoChaveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transferencia.setTipoChave(null);

        // Create the Transferencia, which fails.
        TransferenciaDTO transferenciaDTO = transferenciaMapper.toDto(transferencia);

        restTransferenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transferenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSituacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transferencia.setSituacao(null);

        // Create the Transferencia, which fails.
        TransferenciaDTO transferenciaDTO = transferenciaMapper.toDto(transferencia);

        restTransferenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transferenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroReferenciaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transferencia.setNumeroReferencia(null);

        // Create the Transferencia, which fails.
        TransferenciaDTO transferenciaDTO = transferenciaMapper.toDto(transferencia);

        restTransferenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transferenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransferencias() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList
        restTransferenciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transferencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].chaveInterna").value(hasItem(DEFAULT_CHAVE_INTERNA)))
            .andExpect(jsonPath("$.[*].tipoChave").value(hasItem(DEFAULT_TIPO_CHAVE.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].motivoRejeicao").value(hasItem(DEFAULT_MOTIVO_REJEICAO)))
            .andExpect(jsonPath("$.[*].numeroReferencia").value(hasItem(DEFAULT_NUMERO_REFERENCIA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransferenciasWithEagerRelationshipsIsEnabled() throws Exception {
        when(transferenciaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransferenciaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(transferenciaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTransferenciasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(transferenciaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTransferenciaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(transferenciaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTransferencia() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get the transferencia
        restTransferenciaMockMvc
            .perform(get(ENTITY_API_URL_ID, transferencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transferencia.getId().intValue()))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.chaveInterna").value(DEFAULT_CHAVE_INTERNA))
            .andExpect(jsonPath("$.tipoChave").value(DEFAULT_TIPO_CHAVE.toString()))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.motivoRejeicao").value(DEFAULT_MOTIVO_REJEICAO))
            .andExpect(jsonPath("$.numeroReferencia").value(DEFAULT_NUMERO_REFERENCIA));
    }

    @Test
    @Transactional
    void getTransferenciasByIdFiltering() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        Long id = transferencia.getId();

        defaultTransferenciaFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTransferenciaFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTransferenciaFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransferenciasByValorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where valor equals to
        defaultTransferenciaFiltering("valor.equals=" + DEFAULT_VALOR, "valor.equals=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllTransferenciasByValorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where valor in
        defaultTransferenciaFiltering("valor.in=" + DEFAULT_VALOR + "," + UPDATED_VALOR, "valor.in=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllTransferenciasByValorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where valor is not null
        defaultTransferenciaFiltering("valor.specified=true", "valor.specified=false");
    }

    @Test
    @Transactional
    void getAllTransferenciasByValorIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where valor is greater than or equal to
        defaultTransferenciaFiltering("valor.greaterThanOrEqual=" + DEFAULT_VALOR, "valor.greaterThanOrEqual=" + UPDATED_VALOR);
    }

    @Test
    @Transactional
    void getAllTransferenciasByValorIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where valor is less than or equal to
        defaultTransferenciaFiltering("valor.lessThanOrEqual=" + DEFAULT_VALOR, "valor.lessThanOrEqual=" + SMALLER_VALOR);
    }

    @Test
    @Transactional
    void getAllTransferenciasByValorIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where valor is less than
        defaultTransferenciaFiltering("valor.lessThan=" + UPDATED_VALOR, "valor.lessThan=" + DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void getAllTransferenciasByValorIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where valor is greater than
        defaultTransferenciaFiltering("valor.greaterThan=" + SMALLER_VALOR, "valor.greaterThan=" + DEFAULT_VALOR);
    }

    @Test
    @Transactional
    void getAllTransferenciasByChaveInternaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where chaveInterna equals to
        defaultTransferenciaFiltering("chaveInterna.equals=" + DEFAULT_CHAVE_INTERNA, "chaveInterna.equals=" + UPDATED_CHAVE_INTERNA);
    }

    @Test
    @Transactional
    void getAllTransferenciasByChaveInternaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where chaveInterna in
        defaultTransferenciaFiltering(
            "chaveInterna.in=" + DEFAULT_CHAVE_INTERNA + "," + UPDATED_CHAVE_INTERNA,
            "chaveInterna.in=" + UPDATED_CHAVE_INTERNA
        );
    }

    @Test
    @Transactional
    void getAllTransferenciasByChaveInternaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where chaveInterna is not null
        defaultTransferenciaFiltering("chaveInterna.specified=true", "chaveInterna.specified=false");
    }

    @Test
    @Transactional
    void getAllTransferenciasByChaveInternaContainsSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where chaveInterna contains
        defaultTransferenciaFiltering("chaveInterna.contains=" + DEFAULT_CHAVE_INTERNA, "chaveInterna.contains=" + UPDATED_CHAVE_INTERNA);
    }

    @Test
    @Transactional
    void getAllTransferenciasByChaveInternaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where chaveInterna does not contain
        defaultTransferenciaFiltering(
            "chaveInterna.doesNotContain=" + UPDATED_CHAVE_INTERNA,
            "chaveInterna.doesNotContain=" + DEFAULT_CHAVE_INTERNA
        );
    }

    @Test
    @Transactional
    void getAllTransferenciasByTipoChaveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where tipoChave equals to
        defaultTransferenciaFiltering("tipoChave.equals=" + DEFAULT_TIPO_CHAVE, "tipoChave.equals=" + UPDATED_TIPO_CHAVE);
    }

    @Test
    @Transactional
    void getAllTransferenciasByTipoChaveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where tipoChave in
        defaultTransferenciaFiltering(
            "tipoChave.in=" + DEFAULT_TIPO_CHAVE + "," + UPDATED_TIPO_CHAVE,
            "tipoChave.in=" + UPDATED_TIPO_CHAVE
        );
    }

    @Test
    @Transactional
    void getAllTransferenciasByTipoChaveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where tipoChave is not null
        defaultTransferenciaFiltering("tipoChave.specified=true", "tipoChave.specified=false");
    }

    @Test
    @Transactional
    void getAllTransferenciasBySituacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where situacao equals to
        defaultTransferenciaFiltering("situacao.equals=" + DEFAULT_SITUACAO, "situacao.equals=" + UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    void getAllTransferenciasBySituacaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where situacao in
        defaultTransferenciaFiltering("situacao.in=" + DEFAULT_SITUACAO + "," + UPDATED_SITUACAO, "situacao.in=" + UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    void getAllTransferenciasBySituacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where situacao is not null
        defaultTransferenciaFiltering("situacao.specified=true", "situacao.specified=false");
    }

    @Test
    @Transactional
    void getAllTransferenciasByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where descricao equals to
        defaultTransferenciaFiltering("descricao.equals=" + DEFAULT_DESCRICAO, "descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTransferenciasByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where descricao in
        defaultTransferenciaFiltering("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO, "descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTransferenciasByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where descricao is not null
        defaultTransferenciaFiltering("descricao.specified=true", "descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllTransferenciasByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where descricao contains
        defaultTransferenciaFiltering("descricao.contains=" + DEFAULT_DESCRICAO, "descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTransferenciasByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where descricao does not contain
        defaultTransferenciaFiltering("descricao.doesNotContain=" + UPDATED_DESCRICAO, "descricao.doesNotContain=" + DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllTransferenciasByMotivoRejeicaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where motivoRejeicao equals to
        defaultTransferenciaFiltering(
            "motivoRejeicao.equals=" + DEFAULT_MOTIVO_REJEICAO,
            "motivoRejeicao.equals=" + UPDATED_MOTIVO_REJEICAO
        );
    }

    @Test
    @Transactional
    void getAllTransferenciasByMotivoRejeicaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where motivoRejeicao in
        defaultTransferenciaFiltering(
            "motivoRejeicao.in=" + DEFAULT_MOTIVO_REJEICAO + "," + UPDATED_MOTIVO_REJEICAO,
            "motivoRejeicao.in=" + UPDATED_MOTIVO_REJEICAO
        );
    }

    @Test
    @Transactional
    void getAllTransferenciasByMotivoRejeicaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where motivoRejeicao is not null
        defaultTransferenciaFiltering("motivoRejeicao.specified=true", "motivoRejeicao.specified=false");
    }

    @Test
    @Transactional
    void getAllTransferenciasByMotivoRejeicaoContainsSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where motivoRejeicao contains
        defaultTransferenciaFiltering(
            "motivoRejeicao.contains=" + DEFAULT_MOTIVO_REJEICAO,
            "motivoRejeicao.contains=" + UPDATED_MOTIVO_REJEICAO
        );
    }

    @Test
    @Transactional
    void getAllTransferenciasByMotivoRejeicaoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where motivoRejeicao does not contain
        defaultTransferenciaFiltering(
            "motivoRejeicao.doesNotContain=" + UPDATED_MOTIVO_REJEICAO,
            "motivoRejeicao.doesNotContain=" + DEFAULT_MOTIVO_REJEICAO
        );
    }

    @Test
    @Transactional
    void getAllTransferenciasByNumeroReferenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where numeroReferencia equals to
        defaultTransferenciaFiltering(
            "numeroReferencia.equals=" + DEFAULT_NUMERO_REFERENCIA,
            "numeroReferencia.equals=" + UPDATED_NUMERO_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllTransferenciasByNumeroReferenciaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where numeroReferencia in
        defaultTransferenciaFiltering(
            "numeroReferencia.in=" + DEFAULT_NUMERO_REFERENCIA + "," + UPDATED_NUMERO_REFERENCIA,
            "numeroReferencia.in=" + UPDATED_NUMERO_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllTransferenciasByNumeroReferenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where numeroReferencia is not null
        defaultTransferenciaFiltering("numeroReferencia.specified=true", "numeroReferencia.specified=false");
    }

    @Test
    @Transactional
    void getAllTransferenciasByNumeroReferenciaContainsSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where numeroReferencia contains
        defaultTransferenciaFiltering(
            "numeroReferencia.contains=" + DEFAULT_NUMERO_REFERENCIA,
            "numeroReferencia.contains=" + UPDATED_NUMERO_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllTransferenciasByNumeroReferenciaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        // Get all the transferenciaList where numeroReferencia does not contain
        defaultTransferenciaFiltering(
            "numeroReferencia.doesNotContain=" + UPDATED_NUMERO_REFERENCIA,
            "numeroReferencia.doesNotContain=" + DEFAULT_NUMERO_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllTransferenciasByTransacaoIsEqualToSomething() throws Exception {
        Transacao transacao;
        if (TestUtil.findAll(em, Transacao.class).isEmpty()) {
            transferenciaRepository.saveAndFlush(transferencia);
            transacao = TransacaoResourceIT.createEntity();
        } else {
            transacao = TestUtil.findAll(em, Transacao.class).get(0);
        }
        em.persist(transacao);
        em.flush();
        transferencia.setTransacao(transacao);
        transferenciaRepository.saveAndFlush(transferencia);
        Long transacaoId = transacao.getId();
        // Get all the transferenciaList where transacao equals to transacaoId
        defaultTransferenciaShouldBeFound("transacaoId.equals=" + transacaoId);

        // Get all the transferenciaList where transacao equals to (transacaoId + 1)
        defaultTransferenciaShouldNotBeFound("transacaoId.equals=" + (transacaoId + 1));
    }

    @Test
    @Transactional
    void getAllTransferenciasByUsuarioOrigemIsEqualToSomething() throws Exception {
        User usuarioOrigem;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            transferenciaRepository.saveAndFlush(transferencia);
            usuarioOrigem = UserResourceIT.createEntity();
        } else {
            usuarioOrigem = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(usuarioOrigem);
        em.flush();
        transferencia.setUsuarioOrigem(usuarioOrigem);
        transferenciaRepository.saveAndFlush(transferencia);
        Long usuarioOrigemId = usuarioOrigem.getId();
        // Get all the transferenciaList where usuarioOrigem equals to usuarioOrigemId
        defaultTransferenciaShouldBeFound("usuarioOrigemId.equals=" + usuarioOrigemId);

        // Get all the transferenciaList where usuarioOrigem equals to (usuarioOrigemId + 1)
        defaultTransferenciaShouldNotBeFound("usuarioOrigemId.equals=" + (usuarioOrigemId + 1));
    }

    @Test
    @Transactional
    void getAllTransferenciasByUsuarioDestinoIsEqualToSomething() throws Exception {
        User usuarioDestino;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            transferenciaRepository.saveAndFlush(transferencia);
            usuarioDestino = UserResourceIT.createEntity();
        } else {
            usuarioDestino = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(usuarioDestino);
        em.flush();
        transferencia.setUsuarioDestino(usuarioDestino);
        transferenciaRepository.saveAndFlush(transferencia);
        Long usuarioDestinoId = usuarioDestino.getId();
        // Get all the transferenciaList where usuarioDestino equals to usuarioDestinoId
        defaultTransferenciaShouldBeFound("usuarioDestinoId.equals=" + usuarioDestinoId);

        // Get all the transferenciaList where usuarioDestino equals to (usuarioDestinoId + 1)
        defaultTransferenciaShouldNotBeFound("usuarioDestinoId.equals=" + (usuarioDestinoId + 1));
    }

    @Test
    @Transactional
    void getAllTransferenciasByCarteiraOrigemIsEqualToSomething() throws Exception {
        Carteira carteiraOrigem;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            transferenciaRepository.saveAndFlush(transferencia);
            carteiraOrigem = CarteiraResourceIT.createEntity(em);
        } else {
            carteiraOrigem = TestUtil.findAll(em, Carteira.class).get(0);
        }
        em.persist(carteiraOrigem);
        em.flush();
        transferencia.setCarteiraOrigem(carteiraOrigem);
        transferenciaRepository.saveAndFlush(transferencia);
        Long carteiraOrigemId = carteiraOrigem.getId();
        // Get all the transferenciaList where carteiraOrigem equals to carteiraOrigemId
        defaultTransferenciaShouldBeFound("carteiraOrigemId.equals=" + carteiraOrigemId);

        // Get all the transferenciaList where carteiraOrigem equals to (carteiraOrigemId + 1)
        defaultTransferenciaShouldNotBeFound("carteiraOrigemId.equals=" + (carteiraOrigemId + 1));
    }

    @Test
    @Transactional
    void getAllTransferenciasByCarteiraDestinoIsEqualToSomething() throws Exception {
        Carteira carteiraDestino;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            transferenciaRepository.saveAndFlush(transferencia);
            carteiraDestino = CarteiraResourceIT.createEntity(em);
        } else {
            carteiraDestino = TestUtil.findAll(em, Carteira.class).get(0);
        }
        em.persist(carteiraDestino);
        em.flush();
        transferencia.setCarteiraDestino(carteiraDestino);
        transferenciaRepository.saveAndFlush(transferencia);
        Long carteiraDestinoId = carteiraDestino.getId();
        // Get all the transferenciaList where carteiraDestino equals to carteiraDestinoId
        defaultTransferenciaShouldBeFound("carteiraDestinoId.equals=" + carteiraDestinoId);

        // Get all the transferenciaList where carteiraDestino equals to (carteiraDestinoId + 1)
        defaultTransferenciaShouldNotBeFound("carteiraDestinoId.equals=" + (carteiraDestinoId + 1));
    }

    @Test
    @Transactional
    void getAllTransferenciasByMoedaCarteiraIsEqualToSomething() throws Exception {
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            transferenciaRepository.saveAndFlush(transferencia);
            moedaCarteira = MoedaCarteiraResourceIT.createEntity(em);
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        em.persist(moedaCarteira);
        em.flush();
        transferencia.setMoedaCarteira(moedaCarteira);
        transferenciaRepository.saveAndFlush(transferencia);
        Long moedaCarteiraId = moedaCarteira.getId();
        // Get all the transferenciaList where moedaCarteira equals to moedaCarteiraId
        defaultTransferenciaShouldBeFound("moedaCarteiraId.equals=" + moedaCarteiraId);

        // Get all the transferenciaList where moedaCarteira equals to (moedaCarteiraId + 1)
        defaultTransferenciaShouldNotBeFound("moedaCarteiraId.equals=" + (moedaCarteiraId + 1));
    }

    private void defaultTransferenciaFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTransferenciaShouldBeFound(shouldBeFound);
        defaultTransferenciaShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransferenciaShouldBeFound(String filter) throws Exception {
        restTransferenciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transferencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].chaveInterna").value(hasItem(DEFAULT_CHAVE_INTERNA)))
            .andExpect(jsonPath("$.[*].tipoChave").value(hasItem(DEFAULT_TIPO_CHAVE.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].motivoRejeicao").value(hasItem(DEFAULT_MOTIVO_REJEICAO)))
            .andExpect(jsonPath("$.[*].numeroReferencia").value(hasItem(DEFAULT_NUMERO_REFERENCIA)));

        // Check, that the count call also returns 1
        restTransferenciaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransferenciaShouldNotBeFound(String filter) throws Exception {
        restTransferenciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransferenciaMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransferencia() throws Exception {
        // Get the transferencia
        restTransferenciaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransferencia() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transferencia
        Transferencia updatedTransferencia = transferenciaRepository.findById(transferencia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransferencia are not directly saved in db
        em.detach(updatedTransferencia);
        updatedTransferencia
            .valor(UPDATED_VALOR)
            .chaveInterna(UPDATED_CHAVE_INTERNA)
            .tipoChave(UPDATED_TIPO_CHAVE)
            .situacao(UPDATED_SITUACAO)
            .descricao(UPDATED_DESCRICAO)
            .motivoRejeicao(UPDATED_MOTIVO_REJEICAO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA);
        TransferenciaDTO transferenciaDTO = transferenciaMapper.toDto(updatedTransferencia);

        restTransferenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transferenciaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transferenciaDTO))
            )
            .andExpect(status().isOk());

        // Validate the Transferencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransferenciaToMatchAllProperties(updatedTransferencia);
    }

    @Test
    @Transactional
    void putNonExistingTransferencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transferencia.setId(longCount.incrementAndGet());

        // Create the Transferencia
        TransferenciaDTO transferenciaDTO = transferenciaMapper.toDto(transferencia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransferenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transferenciaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transferenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transferencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransferencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transferencia.setId(longCount.incrementAndGet());

        // Create the Transferencia
        TransferenciaDTO transferenciaDTO = transferenciaMapper.toDto(transferencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransferenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transferenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transferencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransferencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transferencia.setId(longCount.incrementAndGet());

        // Create the Transferencia
        TransferenciaDTO transferenciaDTO = transferenciaMapper.toDto(transferencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransferenciaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transferenciaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transferencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransferenciaWithPatch() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transferencia using partial update
        Transferencia partialUpdatedTransferencia = new Transferencia();
        partialUpdatedTransferencia.setId(transferencia.getId());

        partialUpdatedTransferencia
            .valor(UPDATED_VALOR)
            .chaveInterna(UPDATED_CHAVE_INTERNA)
            .tipoChave(UPDATED_TIPO_CHAVE)
            .descricao(UPDATED_DESCRICAO);

        restTransferenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransferencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransferencia))
            )
            .andExpect(status().isOk());

        // Validate the Transferencia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransferenciaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransferencia, transferencia),
            getPersistedTransferencia(transferencia)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransferenciaWithPatch() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transferencia using partial update
        Transferencia partialUpdatedTransferencia = new Transferencia();
        partialUpdatedTransferencia.setId(transferencia.getId());

        partialUpdatedTransferencia
            .valor(UPDATED_VALOR)
            .chaveInterna(UPDATED_CHAVE_INTERNA)
            .tipoChave(UPDATED_TIPO_CHAVE)
            .situacao(UPDATED_SITUACAO)
            .descricao(UPDATED_DESCRICAO)
            .motivoRejeicao(UPDATED_MOTIVO_REJEICAO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA);

        restTransferenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransferencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransferencia))
            )
            .andExpect(status().isOk());

        // Validate the Transferencia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransferenciaUpdatableFieldsEquals(partialUpdatedTransferencia, getPersistedTransferencia(partialUpdatedTransferencia));
    }

    @Test
    @Transactional
    void patchNonExistingTransferencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transferencia.setId(longCount.incrementAndGet());

        // Create the Transferencia
        TransferenciaDTO transferenciaDTO = transferenciaMapper.toDto(transferencia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransferenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transferenciaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transferenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transferencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransferencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transferencia.setId(longCount.incrementAndGet());

        // Create the Transferencia
        TransferenciaDTO transferenciaDTO = transferenciaMapper.toDto(transferencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransferenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transferenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Transferencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransferencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transferencia.setId(longCount.incrementAndGet());

        // Create the Transferencia
        TransferenciaDTO transferenciaDTO = transferenciaMapper.toDto(transferencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransferenciaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transferenciaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Transferencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransferencia() throws Exception {
        // Initialize the database
        insertedTransferencia = transferenciaRepository.saveAndFlush(transferencia);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transferencia
        restTransferenciaMockMvc
            .perform(delete(ENTITY_API_URL_ID, transferencia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transferenciaRepository.count();
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

    protected Transferencia getPersistedTransferencia(Transferencia transferencia) {
        return transferenciaRepository.findById(transferencia.getId()).orElseThrow();
    }

    protected void assertPersistedTransferenciaToMatchAllProperties(Transferencia expectedTransferencia) {
        assertTransferenciaAllPropertiesEquals(expectedTransferencia, getPersistedTransferencia(expectedTransferencia));
    }

    protected void assertPersistedTransferenciaToMatchUpdatableProperties(Transferencia expectedTransferencia) {
        assertTransferenciaAllUpdatablePropertiesEquals(expectedTransferencia, getPersistedTransferencia(expectedTransferencia));
    }
}
