package com.bankernel.web.rest;

import static com.bankernel.domain.NotificacaoAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Notificacao;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumCanalNotificacao;
import com.bankernel.domain.enumeration.EnumStatusNotificacao;
import com.bankernel.domain.enumeration.EnumTipoNotificacao;
import com.bankernel.repository.NotificacaoRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.NotificacaoService;
import com.bankernel.service.dto.NotificacaoDTO;
import com.bankernel.service.mapper.NotificacaoMapper;
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
 * Integration tests for the {@link NotificacaoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class NotificacaoResourceIT {

    private static final String DEFAULT_TITULO = "AAAAAAAAAA";
    private static final String UPDATED_TITULO = "BBBBBBBBBB";

    private static final String DEFAULT_MENSAGEM = "AAAAAAAAAA";
    private static final String UPDATED_MENSAGEM = "BBBBBBBBBB";

    private static final EnumTipoNotificacao DEFAULT_TIPO = EnumTipoNotificacao.EMAIL;
    private static final EnumTipoNotificacao UPDATED_TIPO = EnumTipoNotificacao.PUSH;

    private static final EnumStatusNotificacao DEFAULT_SITUACAO = EnumStatusNotificacao.PENDENTE;
    private static final EnumStatusNotificacao UPDATED_SITUACAO = EnumStatusNotificacao.ENVIADA;

    private static final EnumCanalNotificacao DEFAULT_CANAL = EnumCanalNotificacao.EMAIL;
    private static final EnumCanalNotificacao UPDATED_CANAL = EnumCanalNotificacao.PUSH;

    private static final Boolean DEFAULT_LIDA = false;
    private static final Boolean UPDATED_LIDA = true;

    private static final String ENTITY_API_URL = "/api/notificacaos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private NotificacaoRepository notificacaoRepositoryMock;

    @Autowired
    private NotificacaoMapper notificacaoMapper;

    @Mock
    private NotificacaoService notificacaoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificacaoMockMvc;

    private Notificacao notificacao;

    private Notificacao insertedNotificacao;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notificacao createEntity(EntityManager em) {
        Notificacao notificacao = new Notificacao()
            .titulo(DEFAULT_TITULO)
            .mensagem(DEFAULT_MENSAGEM)
            .tipo(DEFAULT_TIPO)
            .situacao(DEFAULT_SITUACAO)
            .canal(DEFAULT_CANAL)
            .lida(DEFAULT_LIDA);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        notificacao.setUsuario(user);
        return notificacao;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notificacao createUpdatedEntity(EntityManager em) {
        Notificacao updatedNotificacao = new Notificacao()
            .titulo(UPDATED_TITULO)
            .mensagem(UPDATED_MENSAGEM)
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .canal(UPDATED_CANAL)
            .lida(UPDATED_LIDA);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedNotificacao.setUsuario(user);
        return updatedNotificacao;
    }

    @BeforeEach
    void initTest() {
        notificacao = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedNotificacao != null) {
            notificacaoRepository.delete(insertedNotificacao);
            insertedNotificacao = null;
        }
    }

    @Test
    @Transactional
    void createNotificacao() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);
        var returnedNotificacaoDTO = om.readValue(
            restNotificacaoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificacaoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NotificacaoDTO.class
        );

        // Validate the Notificacao in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNotificacao = notificacaoMapper.toEntity(returnedNotificacaoDTO);
        assertNotificacaoUpdatableFieldsEquals(returnedNotificacao, getPersistedNotificacao(returnedNotificacao));

        insertedNotificacao = returnedNotificacao;
    }

    @Test
    @Transactional
    void createNotificacaoWithExistingId() throws Exception {
        // Create the Notificacao with an existing ID
        notificacao.setId(1L);
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificacaoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notificacao in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTituloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificacao.setTitulo(null);

        // Create the Notificacao, which fails.
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        restNotificacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMensagemIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificacao.setMensagem(null);

        // Create the Notificacao, which fails.
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        restNotificacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificacao.setTipo(null);

        // Create the Notificacao, which fails.
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        restNotificacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSituacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificacao.setSituacao(null);

        // Create the Notificacao, which fails.
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        restNotificacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCanalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificacao.setCanal(null);

        // Create the Notificacao, which fails.
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        restNotificacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLidaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificacao.setLida(null);

        // Create the Notificacao, which fails.
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        restNotificacaoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificacaoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotificacaos() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList
        restNotificacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].mensagem").value(hasItem(DEFAULT_MENSAGEM)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].canal").value(hasItem(DEFAULT_CANAL.toString())))
            .andExpect(jsonPath("$.[*].lida").value(hasItem(DEFAULT_LIDA)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNotificacaosWithEagerRelationshipsIsEnabled() throws Exception {
        when(notificacaoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNotificacaoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(notificacaoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllNotificacaosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(notificacaoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restNotificacaoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(notificacaoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getNotificacao() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get the notificacao
        restNotificacaoMockMvc
            .perform(get(ENTITY_API_URL_ID, notificacao.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificacao.getId().intValue()))
            .andExpect(jsonPath("$.titulo").value(DEFAULT_TITULO))
            .andExpect(jsonPath("$.mensagem").value(DEFAULT_MENSAGEM))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()))
            .andExpect(jsonPath("$.canal").value(DEFAULT_CANAL.toString()))
            .andExpect(jsonPath("$.lida").value(DEFAULT_LIDA));
    }

    @Test
    @Transactional
    void getNotificacaosByIdFiltering() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        Long id = notificacao.getId();

        defaultNotificacaoFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNotificacaoFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNotificacaoFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotificacaosByTituloIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where titulo equals to
        defaultNotificacaoFiltering("titulo.equals=" + DEFAULT_TITULO, "titulo.equals=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByTituloIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where titulo in
        defaultNotificacaoFiltering("titulo.in=" + DEFAULT_TITULO + "," + UPDATED_TITULO, "titulo.in=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByTituloIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where titulo is not null
        defaultNotificacaoFiltering("titulo.specified=true", "titulo.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacaosByTituloContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where titulo contains
        defaultNotificacaoFiltering("titulo.contains=" + DEFAULT_TITULO, "titulo.contains=" + UPDATED_TITULO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByTituloNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where titulo does not contain
        defaultNotificacaoFiltering("titulo.doesNotContain=" + UPDATED_TITULO, "titulo.doesNotContain=" + DEFAULT_TITULO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByMensagemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where mensagem equals to
        defaultNotificacaoFiltering("mensagem.equals=" + DEFAULT_MENSAGEM, "mensagem.equals=" + UPDATED_MENSAGEM);
    }

    @Test
    @Transactional
    void getAllNotificacaosByMensagemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where mensagem in
        defaultNotificacaoFiltering("mensagem.in=" + DEFAULT_MENSAGEM + "," + UPDATED_MENSAGEM, "mensagem.in=" + UPDATED_MENSAGEM);
    }

    @Test
    @Transactional
    void getAllNotificacaosByMensagemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where mensagem is not null
        defaultNotificacaoFiltering("mensagem.specified=true", "mensagem.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacaosByMensagemContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where mensagem contains
        defaultNotificacaoFiltering("mensagem.contains=" + DEFAULT_MENSAGEM, "mensagem.contains=" + UPDATED_MENSAGEM);
    }

    @Test
    @Transactional
    void getAllNotificacaosByMensagemNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where mensagem does not contain
        defaultNotificacaoFiltering("mensagem.doesNotContain=" + UPDATED_MENSAGEM, "mensagem.doesNotContain=" + DEFAULT_MENSAGEM);
    }

    @Test
    @Transactional
    void getAllNotificacaosByTipoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where tipo equals to
        defaultNotificacaoFiltering("tipo.equals=" + DEFAULT_TIPO, "tipo.equals=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByTipoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where tipo in
        defaultNotificacaoFiltering("tipo.in=" + DEFAULT_TIPO + "," + UPDATED_TIPO, "tipo.in=" + UPDATED_TIPO);
    }

    @Test
    @Transactional
    void getAllNotificacaosByTipoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where tipo is not null
        defaultNotificacaoFiltering("tipo.specified=true", "tipo.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacaosBySituacaoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where situacao equals to
        defaultNotificacaoFiltering("situacao.equals=" + DEFAULT_SITUACAO, "situacao.equals=" + UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    void getAllNotificacaosBySituacaoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where situacao in
        defaultNotificacaoFiltering("situacao.in=" + DEFAULT_SITUACAO + "," + UPDATED_SITUACAO, "situacao.in=" + UPDATED_SITUACAO);
    }

    @Test
    @Transactional
    void getAllNotificacaosBySituacaoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where situacao is not null
        defaultNotificacaoFiltering("situacao.specified=true", "situacao.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacaosByCanalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where canal equals to
        defaultNotificacaoFiltering("canal.equals=" + DEFAULT_CANAL, "canal.equals=" + UPDATED_CANAL);
    }

    @Test
    @Transactional
    void getAllNotificacaosByCanalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where canal in
        defaultNotificacaoFiltering("canal.in=" + DEFAULT_CANAL + "," + UPDATED_CANAL, "canal.in=" + UPDATED_CANAL);
    }

    @Test
    @Transactional
    void getAllNotificacaosByCanalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where canal is not null
        defaultNotificacaoFiltering("canal.specified=true", "canal.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacaosByLidaIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where lida equals to
        defaultNotificacaoFiltering("lida.equals=" + DEFAULT_LIDA, "lida.equals=" + UPDATED_LIDA);
    }

    @Test
    @Transactional
    void getAllNotificacaosByLidaIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where lida in
        defaultNotificacaoFiltering("lida.in=" + DEFAULT_LIDA + "," + UPDATED_LIDA, "lida.in=" + UPDATED_LIDA);
    }

    @Test
    @Transactional
    void getAllNotificacaosByLidaIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        // Get all the notificacaoList where lida is not null
        defaultNotificacaoFiltering("lida.specified=true", "lida.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificacaosByUsuarioIsEqualToSomething() throws Exception {
        User usuario;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            notificacaoRepository.saveAndFlush(notificacao);
            usuario = UserResourceIT.createEntity();
        } else {
            usuario = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(usuario);
        em.flush();
        notificacao.setUsuario(usuario);
        notificacaoRepository.saveAndFlush(notificacao);
        Long usuarioId = usuario.getId();
        // Get all the notificacaoList where usuario equals to usuarioId
        defaultNotificacaoShouldBeFound("usuarioId.equals=" + usuarioId);

        // Get all the notificacaoList where usuario equals to (usuarioId + 1)
        defaultNotificacaoShouldNotBeFound("usuarioId.equals=" + (usuarioId + 1));
    }

    private void defaultNotificacaoFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNotificacaoShouldBeFound(shouldBeFound);
        defaultNotificacaoShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificacaoShouldBeFound(String filter) throws Exception {
        restNotificacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificacao.getId().intValue())))
            .andExpect(jsonPath("$.[*].titulo").value(hasItem(DEFAULT_TITULO)))
            .andExpect(jsonPath("$.[*].mensagem").value(hasItem(DEFAULT_MENSAGEM)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].canal").value(hasItem(DEFAULT_CANAL.toString())))
            .andExpect(jsonPath("$.[*].lida").value(hasItem(DEFAULT_LIDA)));

        // Check, that the count call also returns 1
        restNotificacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificacaoShouldNotBeFound(String filter) throws Exception {
        restNotificacaoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificacaoMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotificacao() throws Exception {
        // Get the notificacao
        restNotificacaoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotificacao() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificacao
        Notificacao updatedNotificacao = notificacaoRepository.findById(notificacao.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNotificacao are not directly saved in db
        em.detach(updatedNotificacao);
        updatedNotificacao
            .titulo(UPDATED_TITULO)
            .mensagem(UPDATED_MENSAGEM)
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .canal(UPDATED_CANAL)
            .lida(UPDATED_LIDA);
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(updatedNotificacao);

        restNotificacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificacaoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Notificacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotificacaoToMatchAllProperties(updatedNotificacao);
    }

    @Test
    @Transactional
    void putNonExistingNotificacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificacaoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotificacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotificacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notificacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificacaoWithPatch() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificacao using partial update
        Notificacao partialUpdatedNotificacao = new Notificacao();
        partialUpdatedNotificacao.setId(notificacao.getId());

        partialUpdatedNotificacao.tipo(UPDATED_TIPO).canal(UPDATED_CANAL);

        restNotificacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificacao))
            )
            .andExpect(status().isOk());

        // Validate the Notificacao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificacaoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNotificacao, notificacao),
            getPersistedNotificacao(notificacao)
        );
    }

    @Test
    @Transactional
    void fullUpdateNotificacaoWithPatch() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificacao using partial update
        Notificacao partialUpdatedNotificacao = new Notificacao();
        partialUpdatedNotificacao.setId(notificacao.getId());

        partialUpdatedNotificacao
            .titulo(UPDATED_TITULO)
            .mensagem(UPDATED_MENSAGEM)
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .canal(UPDATED_CANAL)
            .lida(UPDATED_LIDA);

        restNotificacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificacao.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificacao))
            )
            .andExpect(status().isOk());

        // Validate the Notificacao in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificacaoUpdatableFieldsEquals(partialUpdatedNotificacao, getPersistedNotificacao(partialUpdatedNotificacao));
    }

    @Test
    @Transactional
    void patchNonExistingNotificacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificacaoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotificacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificacaoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notificacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotificacao() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificacao.setId(longCount.incrementAndGet());

        // Create the Notificacao
        NotificacaoDTO notificacaoDTO = notificacaoMapper.toDto(notificacao);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificacaoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(notificacaoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notificacao in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotificacao() throws Exception {
        // Initialize the database
        insertedNotificacao = notificacaoRepository.saveAndFlush(notificacao);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the notificacao
        restNotificacaoMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificacao.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notificacaoRepository.count();
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

    protected Notificacao getPersistedNotificacao(Notificacao notificacao) {
        return notificacaoRepository.findById(notificacao.getId()).orElseThrow();
    }

    protected void assertPersistedNotificacaoToMatchAllProperties(Notificacao expectedNotificacao) {
        assertNotificacaoAllPropertiesEquals(expectedNotificacao, getPersistedNotificacao(expectedNotificacao));
    }

    protected void assertPersistedNotificacaoToMatchUpdatableProperties(Notificacao expectedNotificacao) {
        assertNotificacaoAllUpdatablePropertiesEquals(expectedNotificacao, getPersistedNotificacao(expectedNotificacao));
    }
}
