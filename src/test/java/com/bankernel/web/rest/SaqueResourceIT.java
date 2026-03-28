package com.bankernel.web.rest;

import static com.bankernel.domain.SaqueAsserts.*;
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
import com.bankernel.domain.Escritorio;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.Saque;
import com.bankernel.domain.Transacao;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumStatusSaque;
import com.bankernel.domain.enumeration.EnumTipoSaque;
import com.bankernel.repository.SaqueRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.SaqueService;
import com.bankernel.service.dto.SaqueDTO;
import com.bankernel.service.mapper.SaqueMapper;
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
 * Integration tests for the {@link SaqueResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SaqueResourceIT {

    private static final BigDecimal DEFAULT_VALOR_SAQUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_SAQUE = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR_SAQUE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_VALOR_ENVIADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_ENVIADO = new BigDecimal(2);
    private static final BigDecimal SMALLER_VALOR_ENVIADO = new BigDecimal(1 - 1);

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final EnumTipoSaque DEFAULT_TIPO_SAQUE = EnumTipoSaque.PIX_CHAVE;
    private static final EnumTipoSaque UPDATED_TIPO_SAQUE = EnumTipoSaque.PIX_MANUAL;

    private static final EnumStatusSaque DEFAULT_SITUACAO_SAQUE = EnumStatusSaque.PENDENTE;
    private static final EnumStatusSaque UPDATED_SITUACAO_SAQUE = EnumStatusSaque.EM_PROCESSAMENTO;

    private static final String DEFAULT_NUMERO_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_REFERENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_MOTIVO_REJEICAO = "AAAAAAAAAA";
    private static final String UPDATED_MOTIVO_REJEICAO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_CONTABILIZADO = false;
    private static final Boolean UPDATED_CONTABILIZADO = true;

    private static final String DEFAULT_NOME_USUARIO_FIXO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_USUARIO_FIXO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/saques";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SaqueRepository saqueRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private SaqueRepository saqueRepositoryMock;

    @Autowired
    private SaqueMapper saqueMapper;

    @Mock
    private SaqueService saqueServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSaqueMockMvc;

    private Saque saque;

    private Saque insertedSaque;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Saque createEntity(EntityManager em) {
        Saque saque = new Saque()
            .valorSaque(DEFAULT_VALOR_SAQUE)
            .valorEnviado(DEFAULT_VALOR_ENVIADO)
            .descricao(DEFAULT_DESCRICAO)
            .tipoSaque(DEFAULT_TIPO_SAQUE)
            .situacaoSaque(DEFAULT_SITUACAO_SAQUE)
            .numeroReferencia(DEFAULT_NUMERO_REFERENCIA)
            .motivoRejeicao(DEFAULT_MOTIVO_REJEICAO)
            .contabilizado(DEFAULT_CONTABILIZADO)
            .nomeUsuarioFixo(DEFAULT_NOME_USUARIO_FIXO);
        // Add required entity
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            carteira = CarteiraResourceIT.createEntity(em);
            em.persist(carteira);
            em.flush();
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        saque.setCarteira(carteira);
        // Add required entity
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            moedaCarteira = MoedaCarteiraResourceIT.createEntity(em);
            em.persist(moedaCarteira);
            em.flush();
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        saque.setMoedaCarteira(moedaCarteira);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        saque.setUsuario(user);
        return saque;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Saque createUpdatedEntity(EntityManager em) {
        Saque updatedSaque = new Saque()
            .valorSaque(UPDATED_VALOR_SAQUE)
            .valorEnviado(UPDATED_VALOR_ENVIADO)
            .descricao(UPDATED_DESCRICAO)
            .tipoSaque(UPDATED_TIPO_SAQUE)
            .situacaoSaque(UPDATED_SITUACAO_SAQUE)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .motivoRejeicao(UPDATED_MOTIVO_REJEICAO)
            .contabilizado(UPDATED_CONTABILIZADO)
            .nomeUsuarioFixo(UPDATED_NOME_USUARIO_FIXO);
        // Add required entity
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            carteira = CarteiraResourceIT.createUpdatedEntity(em);
            em.persist(carteira);
            em.flush();
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        updatedSaque.setCarteira(carteira);
        // Add required entity
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            moedaCarteira = MoedaCarteiraResourceIT.createUpdatedEntity(em);
            em.persist(moedaCarteira);
            em.flush();
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        updatedSaque.setMoedaCarteira(moedaCarteira);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedSaque.setUsuario(user);
        return updatedSaque;
    }

    @BeforeEach
    void initTest() {
        saque = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSaque != null) {
            saqueRepository.delete(insertedSaque);
            insertedSaque = null;
        }
    }

    @Test
    @Transactional
    void createSaque() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Saque
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);
        var returnedSaqueDTO = om.readValue(
            restSaqueMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saqueDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SaqueDTO.class
        );

        // Validate the Saque in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSaque = saqueMapper.toEntity(returnedSaqueDTO);
        assertSaqueUpdatableFieldsEquals(returnedSaque, getPersistedSaque(returnedSaque));

        insertedSaque = returnedSaque;
    }

    @Test
    @Transactional
    void createSaqueWithExistingId() throws Exception {
        // Create the Saque with an existing ID
        saque.setId(1L);
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saqueDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Saque in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkValorSaqueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        saque.setValorSaque(null);

        // Create the Saque, which fails.
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        restSaqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoSaqueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        saque.setTipoSaque(null);

        // Create the Saque, which fails.
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        restSaqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSituacaoSaqueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        saque.setSituacaoSaque(null);

        // Create the Saque, which fails.
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        restSaqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroReferenciaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        saque.setNumeroReferencia(null);

        // Create the Saque, which fails.
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        restSaqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContabilizadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        saque.setContabilizado(null);

        // Create the Saque, which fails.
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        restSaqueMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saqueDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSaques() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList
        restSaqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saque.getId().intValue())))
            .andExpect(jsonPath("$.[*].valorSaque").value(hasItem(sameNumber(DEFAULT_VALOR_SAQUE))))
            .andExpect(jsonPath("$.[*].valorEnviado").value(hasItem(sameNumber(DEFAULT_VALOR_ENVIADO))))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].tipoSaque").value(hasItem(DEFAULT_TIPO_SAQUE.toString())))
            .andExpect(jsonPath("$.[*].situacaoSaque").value(hasItem(DEFAULT_SITUACAO_SAQUE.toString())))
            .andExpect(jsonPath("$.[*].numeroReferencia").value(hasItem(DEFAULT_NUMERO_REFERENCIA)))
            .andExpect(jsonPath("$.[*].motivoRejeicao").value(hasItem(DEFAULT_MOTIVO_REJEICAO)))
            .andExpect(jsonPath("$.[*].contabilizado").value(hasItem(DEFAULT_CONTABILIZADO)))
            .andExpect(jsonPath("$.[*].nomeUsuarioFixo").value(hasItem(DEFAULT_NOME_USUARIO_FIXO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSaquesWithEagerRelationshipsIsEnabled() throws Exception {
        when(saqueServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSaqueMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(saqueServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSaquesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(saqueServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSaqueMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(saqueRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSaque() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get the saque
        restSaqueMockMvc
            .perform(get(ENTITY_API_URL_ID, saque.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(saque.getId().intValue()))
            .andExpect(jsonPath("$.valorSaque").value(sameNumber(DEFAULT_VALOR_SAQUE)))
            .andExpect(jsonPath("$.valorEnviado").value(sameNumber(DEFAULT_VALOR_ENVIADO)))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.tipoSaque").value(DEFAULT_TIPO_SAQUE.toString()))
            .andExpect(jsonPath("$.situacaoSaque").value(DEFAULT_SITUACAO_SAQUE.toString()))
            .andExpect(jsonPath("$.numeroReferencia").value(DEFAULT_NUMERO_REFERENCIA))
            .andExpect(jsonPath("$.motivoRejeicao").value(DEFAULT_MOTIVO_REJEICAO))
            .andExpect(jsonPath("$.contabilizado").value(DEFAULT_CONTABILIZADO))
            .andExpect(jsonPath("$.nomeUsuarioFixo").value(DEFAULT_NOME_USUARIO_FIXO));
    }

    @Test
    @Transactional
    void getSaquesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        Long id = saque.getId();

        defaultSaqueFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSaqueFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSaqueFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSaquesByValorSaqueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where valorSaque equals to
        defaultSaqueFiltering("valorSaque.equals=" + DEFAULT_VALOR_SAQUE, "valorSaque.equals=" + UPDATED_VALOR_SAQUE);
    }

    @Test
    @Transactional
    void getAllSaquesByValorSaqueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where valorSaque in
        defaultSaqueFiltering("valorSaque.in=" + DEFAULT_VALOR_SAQUE + "," + UPDATED_VALOR_SAQUE, "valorSaque.in=" + UPDATED_VALOR_SAQUE);
    }

    @Test
    @Transactional
    void getAllSaquesByValorSaqueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where valorSaque is not null
        defaultSaqueFiltering("valorSaque.specified=true", "valorSaque.specified=false");
    }

    @Test
    @Transactional
    void getAllSaquesByValorSaqueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where valorSaque is greater than or equal to
        defaultSaqueFiltering(
            "valorSaque.greaterThanOrEqual=" + DEFAULT_VALOR_SAQUE,
            "valorSaque.greaterThanOrEqual=" + UPDATED_VALOR_SAQUE
        );
    }

    @Test
    @Transactional
    void getAllSaquesByValorSaqueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where valorSaque is less than or equal to
        defaultSaqueFiltering("valorSaque.lessThanOrEqual=" + DEFAULT_VALOR_SAQUE, "valorSaque.lessThanOrEqual=" + SMALLER_VALOR_SAQUE);
    }

    @Test
    @Transactional
    void getAllSaquesByValorSaqueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where valorSaque is less than
        defaultSaqueFiltering("valorSaque.lessThan=" + UPDATED_VALOR_SAQUE, "valorSaque.lessThan=" + DEFAULT_VALOR_SAQUE);
    }

    @Test
    @Transactional
    void getAllSaquesByValorSaqueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where valorSaque is greater than
        defaultSaqueFiltering("valorSaque.greaterThan=" + SMALLER_VALOR_SAQUE, "valorSaque.greaterThan=" + DEFAULT_VALOR_SAQUE);
    }

    @Test
    @Transactional
    void getAllSaquesByValorEnviadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where valorEnviado equals to
        defaultSaqueFiltering("valorEnviado.equals=" + DEFAULT_VALOR_ENVIADO, "valorEnviado.equals=" + UPDATED_VALOR_ENVIADO);
    }

    @Test
    @Transactional
    void getAllSaquesByValorEnviadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where valorEnviado in
        defaultSaqueFiltering(
            "valorEnviado.in=" + DEFAULT_VALOR_ENVIADO + "," + UPDATED_VALOR_ENVIADO,
            "valorEnviado.in=" + UPDATED_VALOR_ENVIADO
        );
    }

    @Test
    @Transactional
    void getAllSaquesByValorEnviadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where valorEnviado is not null
        defaultSaqueFiltering("valorEnviado.specified=true", "valorEnviado.specified=false");
    }

    @Test
    @Transactional
    void getAllSaquesByValorEnviadoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where valorEnviado is greater than or equal to
        defaultSaqueFiltering(
            "valorEnviado.greaterThanOrEqual=" + DEFAULT_VALOR_ENVIADO,
            "valorEnviado.greaterThanOrEqual=" + UPDATED_VALOR_ENVIADO
        );
    }

    @Test
    @Transactional
    void getAllSaquesByValorEnviadoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where valorEnviado is less than or equal to
        defaultSaqueFiltering(
            "valorEnviado.lessThanOrEqual=" + DEFAULT_VALOR_ENVIADO,
            "valorEnviado.lessThanOrEqual=" + SMALLER_VALOR_ENVIADO
        );
    }

    @Test
    @Transactional
    void getAllSaquesByValorEnviadoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where valorEnviado is less than
        defaultSaqueFiltering("valorEnviado.lessThan=" + UPDATED_VALOR_ENVIADO, "valorEnviado.lessThan=" + DEFAULT_VALOR_ENVIADO);
    }

    @Test
    @Transactional
    void getAllSaquesByValorEnviadoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where valorEnviado is greater than
        defaultSaqueFiltering("valorEnviado.greaterThan=" + SMALLER_VALOR_ENVIADO, "valorEnviado.greaterThan=" + DEFAULT_VALOR_ENVIADO);
    }

    @Test
    @Transactional
    void getAllSaquesByDescricaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where descricao equals to
        defaultSaqueFiltering("descricao.equals=" + DEFAULT_DESCRICAO, "descricao.equals=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllSaquesByDescricaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where descricao in
        defaultSaqueFiltering("descricao.in=" + DEFAULT_DESCRICAO + "," + UPDATED_DESCRICAO, "descricao.in=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllSaquesByDescricaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where descricao is not null
        defaultSaqueFiltering("descricao.specified=true", "descricao.specified=false");
    }

    @Test
    @Transactional
    void getAllSaquesByDescricaoContainsSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where descricao contains
        defaultSaqueFiltering("descricao.contains=" + DEFAULT_DESCRICAO, "descricao.contains=" + UPDATED_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllSaquesByDescricaoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where descricao does not contain
        defaultSaqueFiltering("descricao.doesNotContain=" + UPDATED_DESCRICAO, "descricao.doesNotContain=" + DEFAULT_DESCRICAO);
    }

    @Test
    @Transactional
    void getAllSaquesByTipoSaqueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where tipoSaque equals to
        defaultSaqueFiltering("tipoSaque.equals=" + DEFAULT_TIPO_SAQUE, "tipoSaque.equals=" + UPDATED_TIPO_SAQUE);
    }

    @Test
    @Transactional
    void getAllSaquesByTipoSaqueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where tipoSaque in
        defaultSaqueFiltering("tipoSaque.in=" + DEFAULT_TIPO_SAQUE + "," + UPDATED_TIPO_SAQUE, "tipoSaque.in=" + UPDATED_TIPO_SAQUE);
    }

    @Test
    @Transactional
    void getAllSaquesByTipoSaqueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where tipoSaque is not null
        defaultSaqueFiltering("tipoSaque.specified=true", "tipoSaque.specified=false");
    }

    @Test
    @Transactional
    void getAllSaquesBySituacaoSaqueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where situacaoSaque equals to
        defaultSaqueFiltering("situacaoSaque.equals=" + DEFAULT_SITUACAO_SAQUE, "situacaoSaque.equals=" + UPDATED_SITUACAO_SAQUE);
    }

    @Test
    @Transactional
    void getAllSaquesBySituacaoSaqueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where situacaoSaque in
        defaultSaqueFiltering(
            "situacaoSaque.in=" + DEFAULT_SITUACAO_SAQUE + "," + UPDATED_SITUACAO_SAQUE,
            "situacaoSaque.in=" + UPDATED_SITUACAO_SAQUE
        );
    }

    @Test
    @Transactional
    void getAllSaquesBySituacaoSaqueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where situacaoSaque is not null
        defaultSaqueFiltering("situacaoSaque.specified=true", "situacaoSaque.specified=false");
    }

    @Test
    @Transactional
    void getAllSaquesByNumeroReferenciaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where numeroReferencia equals to
        defaultSaqueFiltering(
            "numeroReferencia.equals=" + DEFAULT_NUMERO_REFERENCIA,
            "numeroReferencia.equals=" + UPDATED_NUMERO_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllSaquesByNumeroReferenciaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where numeroReferencia in
        defaultSaqueFiltering(
            "numeroReferencia.in=" + DEFAULT_NUMERO_REFERENCIA + "," + UPDATED_NUMERO_REFERENCIA,
            "numeroReferencia.in=" + UPDATED_NUMERO_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllSaquesByNumeroReferenciaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where numeroReferencia is not null
        defaultSaqueFiltering("numeroReferencia.specified=true", "numeroReferencia.specified=false");
    }

    @Test
    @Transactional
    void getAllSaquesByNumeroReferenciaContainsSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where numeroReferencia contains
        defaultSaqueFiltering(
            "numeroReferencia.contains=" + DEFAULT_NUMERO_REFERENCIA,
            "numeroReferencia.contains=" + UPDATED_NUMERO_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllSaquesByNumeroReferenciaNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where numeroReferencia does not contain
        defaultSaqueFiltering(
            "numeroReferencia.doesNotContain=" + UPDATED_NUMERO_REFERENCIA,
            "numeroReferencia.doesNotContain=" + DEFAULT_NUMERO_REFERENCIA
        );
    }

    @Test
    @Transactional
    void getAllSaquesByMotivoRejeicaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where motivoRejeicao equals to
        defaultSaqueFiltering("motivoRejeicao.equals=" + DEFAULT_MOTIVO_REJEICAO, "motivoRejeicao.equals=" + UPDATED_MOTIVO_REJEICAO);
    }

    @Test
    @Transactional
    void getAllSaquesByMotivoRejeicaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where motivoRejeicao in
        defaultSaqueFiltering(
            "motivoRejeicao.in=" + DEFAULT_MOTIVO_REJEICAO + "," + UPDATED_MOTIVO_REJEICAO,
            "motivoRejeicao.in=" + UPDATED_MOTIVO_REJEICAO
        );
    }

    @Test
    @Transactional
    void getAllSaquesByMotivoRejeicaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where motivoRejeicao is not null
        defaultSaqueFiltering("motivoRejeicao.specified=true", "motivoRejeicao.specified=false");
    }

    @Test
    @Transactional
    void getAllSaquesByMotivoRejeicaoContainsSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where motivoRejeicao contains
        defaultSaqueFiltering("motivoRejeicao.contains=" + DEFAULT_MOTIVO_REJEICAO, "motivoRejeicao.contains=" + UPDATED_MOTIVO_REJEICAO);
    }

    @Test
    @Transactional
    void getAllSaquesByMotivoRejeicaoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where motivoRejeicao does not contain
        defaultSaqueFiltering(
            "motivoRejeicao.doesNotContain=" + UPDATED_MOTIVO_REJEICAO,
            "motivoRejeicao.doesNotContain=" + DEFAULT_MOTIVO_REJEICAO
        );
    }

    @Test
    @Transactional
    void getAllSaquesByContabilizadoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where contabilizado equals to
        defaultSaqueFiltering("contabilizado.equals=" + DEFAULT_CONTABILIZADO, "contabilizado.equals=" + UPDATED_CONTABILIZADO);
    }

    @Test
    @Transactional
    void getAllSaquesByContabilizadoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where contabilizado in
        defaultSaqueFiltering(
            "contabilizado.in=" + DEFAULT_CONTABILIZADO + "," + UPDATED_CONTABILIZADO,
            "contabilizado.in=" + UPDATED_CONTABILIZADO
        );
    }

    @Test
    @Transactional
    void getAllSaquesByContabilizadoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where contabilizado is not null
        defaultSaqueFiltering("contabilizado.specified=true", "contabilizado.specified=false");
    }

    @Test
    @Transactional
    void getAllSaquesByNomeUsuarioFixoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where nomeUsuarioFixo equals to
        defaultSaqueFiltering("nomeUsuarioFixo.equals=" + DEFAULT_NOME_USUARIO_FIXO, "nomeUsuarioFixo.equals=" + UPDATED_NOME_USUARIO_FIXO);
    }

    @Test
    @Transactional
    void getAllSaquesByNomeUsuarioFixoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where nomeUsuarioFixo in
        defaultSaqueFiltering(
            "nomeUsuarioFixo.in=" + DEFAULT_NOME_USUARIO_FIXO + "," + UPDATED_NOME_USUARIO_FIXO,
            "nomeUsuarioFixo.in=" + UPDATED_NOME_USUARIO_FIXO
        );
    }

    @Test
    @Transactional
    void getAllSaquesByNomeUsuarioFixoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where nomeUsuarioFixo is not null
        defaultSaqueFiltering("nomeUsuarioFixo.specified=true", "nomeUsuarioFixo.specified=false");
    }

    @Test
    @Transactional
    void getAllSaquesByNomeUsuarioFixoContainsSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where nomeUsuarioFixo contains
        defaultSaqueFiltering(
            "nomeUsuarioFixo.contains=" + DEFAULT_NOME_USUARIO_FIXO,
            "nomeUsuarioFixo.contains=" + UPDATED_NOME_USUARIO_FIXO
        );
    }

    @Test
    @Transactional
    void getAllSaquesByNomeUsuarioFixoNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        // Get all the saqueList where nomeUsuarioFixo does not contain
        defaultSaqueFiltering(
            "nomeUsuarioFixo.doesNotContain=" + UPDATED_NOME_USUARIO_FIXO,
            "nomeUsuarioFixo.doesNotContain=" + DEFAULT_NOME_USUARIO_FIXO
        );
    }

    @Test
    @Transactional
    void getAllSaquesByTransacaoIsEqualToSomething() throws Exception {
        Transacao transacao;
        if (TestUtil.findAll(em, Transacao.class).isEmpty()) {
            saqueRepository.saveAndFlush(saque);
            transacao = TransacaoResourceIT.createEntity();
        } else {
            transacao = TestUtil.findAll(em, Transacao.class).get(0);
        }
        em.persist(transacao);
        em.flush();
        saque.setTransacao(transacao);
        saqueRepository.saveAndFlush(saque);
        Long transacaoId = transacao.getId();
        // Get all the saqueList where transacao equals to transacaoId
        defaultSaqueShouldBeFound("transacaoId.equals=" + transacaoId);

        // Get all the saqueList where transacao equals to (transacaoId + 1)
        defaultSaqueShouldNotBeFound("transacaoId.equals=" + (transacaoId + 1));
    }

    @Test
    @Transactional
    void getAllSaquesByTransacaoEstornoIsEqualToSomething() throws Exception {
        Transacao transacaoEstorno;
        if (TestUtil.findAll(em, Transacao.class).isEmpty()) {
            saqueRepository.saveAndFlush(saque);
            transacaoEstorno = TransacaoResourceIT.createEntity();
        } else {
            transacaoEstorno = TestUtil.findAll(em, Transacao.class).get(0);
        }
        em.persist(transacaoEstorno);
        em.flush();
        saque.setTransacaoEstorno(transacaoEstorno);
        saqueRepository.saveAndFlush(saque);
        Long transacaoEstornoId = transacaoEstorno.getId();
        // Get all the saqueList where transacaoEstorno equals to transacaoEstornoId
        defaultSaqueShouldBeFound("transacaoEstornoId.equals=" + transacaoEstornoId);

        // Get all the saqueList where transacaoEstorno equals to (transacaoEstornoId + 1)
        defaultSaqueShouldNotBeFound("transacaoEstornoId.equals=" + (transacaoEstornoId + 1));
    }

    @Test
    @Transactional
    void getAllSaquesByCarteiraIsEqualToSomething() throws Exception {
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            saqueRepository.saveAndFlush(saque);
            carteira = CarteiraResourceIT.createEntity(em);
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        em.persist(carteira);
        em.flush();
        saque.setCarteira(carteira);
        saqueRepository.saveAndFlush(saque);
        Long carteiraId = carteira.getId();
        // Get all the saqueList where carteira equals to carteiraId
        defaultSaqueShouldBeFound("carteiraId.equals=" + carteiraId);

        // Get all the saqueList where carteira equals to (carteiraId + 1)
        defaultSaqueShouldNotBeFound("carteiraId.equals=" + (carteiraId + 1));
    }

    @Test
    @Transactional
    void getAllSaquesByMoedaCarteiraIsEqualToSomething() throws Exception {
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            saqueRepository.saveAndFlush(saque);
            moedaCarteira = MoedaCarteiraResourceIT.createEntity(em);
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        em.persist(moedaCarteira);
        em.flush();
        saque.setMoedaCarteira(moedaCarteira);
        saqueRepository.saveAndFlush(saque);
        Long moedaCarteiraId = moedaCarteira.getId();
        // Get all the saqueList where moedaCarteira equals to moedaCarteiraId
        defaultSaqueShouldBeFound("moedaCarteiraId.equals=" + moedaCarteiraId);

        // Get all the saqueList where moedaCarteira equals to (moedaCarteiraId + 1)
        defaultSaqueShouldNotBeFound("moedaCarteiraId.equals=" + (moedaCarteiraId + 1));
    }

    @Test
    @Transactional
    void getAllSaquesByContaBancariaDestinoIsEqualToSomething() throws Exception {
        ContaBancaria contaBancariaDestino;
        if (TestUtil.findAll(em, ContaBancaria.class).isEmpty()) {
            saqueRepository.saveAndFlush(saque);
            contaBancariaDestino = ContaBancariaResourceIT.createEntity(em);
        } else {
            contaBancariaDestino = TestUtil.findAll(em, ContaBancaria.class).get(0);
        }
        em.persist(contaBancariaDestino);
        em.flush();
        saque.setContaBancariaDestino(contaBancariaDestino);
        saqueRepository.saveAndFlush(saque);
        Long contaBancariaDestinoId = contaBancariaDestino.getId();
        // Get all the saqueList where contaBancariaDestino equals to contaBancariaDestinoId
        defaultSaqueShouldBeFound("contaBancariaDestinoId.equals=" + contaBancariaDestinoId);

        // Get all the saqueList where contaBancariaDestino equals to (contaBancariaDestinoId + 1)
        defaultSaqueShouldNotBeFound("contaBancariaDestinoId.equals=" + (contaBancariaDestinoId + 1));
    }

    @Test
    @Transactional
    void getAllSaquesByUsuarioIsEqualToSomething() throws Exception {
        User usuario;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            saqueRepository.saveAndFlush(saque);
            usuario = UserResourceIT.createEntity();
        } else {
            usuario = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        saque.setUsuario(usuario);
        saqueRepository.saveAndFlush(saque);
        Long usuarioId = usuario.getId();
        // Get all the saqueList where usuario equals to usuarioId
        defaultSaqueShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the saqueList where usuario equals to (usuarioId + 1)
        defaultSaqueShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    @Test
    @Transactional
    void getAllSaquesByEscritorioIsEqualToSomething() throws Exception {
        Escritorio escritorio;
        if (TestUtil.findAll(em, Escritorio.class).isEmpty()) {
            saqueRepository.saveAndFlush(saque);
            escritorio = EscritorioResourceIT.createEntity();
        } else {
            escritorio = TestUtil.findAll(em, Escritorio.class).get(0);
        }
        em.persist(escritorio);
        em.flush();
        saque.setEscritorio(escritorio);
        saqueRepository.saveAndFlush(saque);
        Long escritorioId = escritorio.getId();
        // Get all the saqueList where escritorio equals to escritorioId
        defaultSaqueShouldBeFound("escritorioId.equals=" + escritorioId);

        // Get all the saqueList where escritorio equals to (escritorioId + 1)
        defaultSaqueShouldNotBeFound("escritorioId.equals=" + (escritorioId + 1));
    }

    private void defaultSaqueFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSaqueShouldBeFound(shouldBeFound);
        defaultSaqueShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSaqueShouldBeFound(String filter) throws Exception {
        restSaqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saque.getId().intValue())))
            .andExpect(jsonPath("$.[*].valorSaque").value(hasItem(sameNumber(DEFAULT_VALOR_SAQUE))))
            .andExpect(jsonPath("$.[*].valorEnviado").value(hasItem(sameNumber(DEFAULT_VALOR_ENVIADO))))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].tipoSaque").value(hasItem(DEFAULT_TIPO_SAQUE.toString())))
            .andExpect(jsonPath("$.[*].situacaoSaque").value(hasItem(DEFAULT_SITUACAO_SAQUE.toString())))
            .andExpect(jsonPath("$.[*].numeroReferencia").value(hasItem(DEFAULT_NUMERO_REFERENCIA)))
            .andExpect(jsonPath("$.[*].motivoRejeicao").value(hasItem(DEFAULT_MOTIVO_REJEICAO)))
            .andExpect(jsonPath("$.[*].contabilizado").value(hasItem(DEFAULT_CONTABILIZADO)))
            .andExpect(jsonPath("$.[*].nomeUsuarioFixo").value(hasItem(DEFAULT_NOME_USUARIO_FIXO)));

        // Check, that the count call also returns 1
        restSaqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSaqueShouldNotBeFound(String filter) throws Exception {
        restSaqueMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSaqueMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSaque() throws Exception {
        // Get the saque
        restSaqueMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSaque() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saque
        Saque updatedSaque = saqueRepository.findById(saque.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSaque are not directly saved in db
        em.detach(updatedSaque);
        updatedSaque
            .valorSaque(UPDATED_VALOR_SAQUE)
            .valorEnviado(UPDATED_VALOR_ENVIADO)
            .descricao(UPDATED_DESCRICAO)
            .tipoSaque(UPDATED_TIPO_SAQUE)
            .situacaoSaque(UPDATED_SITUACAO_SAQUE)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .motivoRejeicao(UPDATED_MOTIVO_REJEICAO)
            .contabilizado(UPDATED_CONTABILIZADO)
            .nomeUsuarioFixo(UPDATED_NOME_USUARIO_FIXO);
        SaqueDTO saqueDTO = saqueMapper.toDto(updatedSaque);

        restSaqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, saqueDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saqueDTO))
            )
            .andExpect(status().isOk());

        // Validate the Saque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSaqueToMatchAllProperties(updatedSaque);
    }

    @Test
    @Transactional
    void putNonExistingSaque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saque.setId(longCount.incrementAndGet());

        // Create the Saque
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, saqueDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Saque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSaque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saque.setId(longCount.incrementAndGet());

        // Create the Saque
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaqueMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(saqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Saque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSaque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saque.setId(longCount.incrementAndGet());

        // Create the Saque
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaqueMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saqueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Saque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSaqueWithPatch() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saque using partial update
        Saque partialUpdatedSaque = new Saque();
        partialUpdatedSaque.setId(saque.getId());

        partialUpdatedSaque
            .valorSaque(UPDATED_VALOR_SAQUE)
            .valorEnviado(UPDATED_VALOR_ENVIADO)
            .descricao(UPDATED_DESCRICAO)
            .tipoSaque(UPDATED_TIPO_SAQUE)
            .situacaoSaque(UPDATED_SITUACAO_SAQUE)
            .nomeUsuarioFixo(UPDATED_NOME_USUARIO_FIXO);

        restSaqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaque.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSaque))
            )
            .andExpect(status().isOk());

        // Validate the Saque in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSaqueUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSaque, saque), getPersistedSaque(saque));
    }

    @Test
    @Transactional
    void fullUpdateSaqueWithPatch() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saque using partial update
        Saque partialUpdatedSaque = new Saque();
        partialUpdatedSaque.setId(saque.getId());

        partialUpdatedSaque
            .valorSaque(UPDATED_VALOR_SAQUE)
            .valorEnviado(UPDATED_VALOR_ENVIADO)
            .descricao(UPDATED_DESCRICAO)
            .tipoSaque(UPDATED_TIPO_SAQUE)
            .situacaoSaque(UPDATED_SITUACAO_SAQUE)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .motivoRejeicao(UPDATED_MOTIVO_REJEICAO)
            .contabilizado(UPDATED_CONTABILIZADO)
            .nomeUsuarioFixo(UPDATED_NOME_USUARIO_FIXO);

        restSaqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaque.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSaque))
            )
            .andExpect(status().isOk());

        // Validate the Saque in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSaqueUpdatableFieldsEquals(partialUpdatedSaque, getPersistedSaque(partialUpdatedSaque));
    }

    @Test
    @Transactional
    void patchNonExistingSaque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saque.setId(longCount.incrementAndGet());

        // Create the Saque
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, saqueDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(saqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Saque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSaque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saque.setId(longCount.incrementAndGet());

        // Create the Saque
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaqueMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(saqueDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Saque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSaque() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saque.setId(longCount.incrementAndGet());

        // Create the Saque
        SaqueDTO saqueDTO = saqueMapper.toDto(saque);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaqueMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(saqueDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Saque in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSaque() throws Exception {
        // Initialize the database
        insertedSaque = saqueRepository.saveAndFlush(saque);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the saque
        restSaqueMockMvc
            .perform(delete(ENTITY_API_URL_ID, saque.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return saqueRepository.count();
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

    protected Saque getPersistedSaque(Saque saque) {
        return saqueRepository.findById(saque.getId()).orElseThrow();
    }

    protected void assertPersistedSaqueToMatchAllProperties(Saque expectedSaque) {
        assertSaqueAllPropertiesEquals(expectedSaque, getPersistedSaque(expectedSaque));
    }

    protected void assertPersistedSaqueToMatchUpdatableProperties(Saque expectedSaque) {
        assertSaqueAllUpdatablePropertiesEquals(expectedSaque, getPersistedSaque(expectedSaque));
    }
}
