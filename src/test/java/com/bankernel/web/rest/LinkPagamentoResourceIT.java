package com.bankernel.web.rest;

import static com.bankernel.domain.LinkPagamentoAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bankernel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.LinkPagamento;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumStatusLinkPagamento;
import com.bankernel.domain.enumeration.EnumTipoDesconto;
import com.bankernel.domain.enumeration.EnumTipoLinkPagamento;
import com.bankernel.repository.LinkPagamentoRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.LinkPagamentoService;
import com.bankernel.service.dto.LinkPagamentoDTO;
import com.bankernel.service.mapper.LinkPagamentoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link LinkPagamentoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LinkPagamentoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_TOKEN = "AAAAAAAAAA";
    private static final String UPDATED_TOKEN = "BBBBBBBBBB";

    private static final String DEFAULT_CHAVE = "AAAAAAAAAA";
    private static final String UPDATED_CHAVE = "BBBBBBBBBB";

    private static final String DEFAULT_WEBHOOK_URL = "AAAAAAAAAA";
    private static final String UPDATED_WEBHOOK_URL = "BBBBBBBBBB";

    private static final String DEFAULT_URL_PAGAMENTO_APROVADO = "AAAAAAAAAA";
    private static final String UPDATED_URL_PAGAMENTO_APROVADO = "BBBBBBBBBB";

    private static final String DEFAULT_URL_PAGAMENTO_CANCELADO = "AAAAAAAAAA";
    private static final String UPDATED_URL_PAGAMENTO_CANCELADO = "BBBBBBBBBB";

    private static final String DEFAULT_URL_PAGAMENTO_REJEITADO = "AAAAAAAAAA";
    private static final String UPDATED_URL_PAGAMENTO_REJEITADO = "BBBBBBBBBB";

    private static final EnumTipoLinkPagamento DEFAULT_TIPO = EnumTipoLinkPagamento.UNICO;
    private static final EnumTipoLinkPagamento UPDATED_TIPO = EnumTipoLinkPagamento.RECORRENTE;

    private static final EnumStatusLinkPagamento DEFAULT_STATUS = EnumStatusLinkPagamento.ATIVO;
    private static final EnumStatusLinkPagamento UPDATED_STATUS = EnumStatusLinkPagamento.INATIVO;

    private static final Instant DEFAULT_DATA_INICIO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_INICIO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DATA_FIM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_FIM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_DESCONTO = new BigDecimal(1);
    private static final BigDecimal UPDATED_DESCONTO = new BigDecimal(2);

    private static final EnumTipoDesconto DEFAULT_TIPO_DESCONTO = EnumTipoDesconto.FIXO;
    private static final EnumTipoDesconto UPDATED_TIPO_DESCONTO = EnumTipoDesconto.PERCENTUAL;

    private static final BigDecimal DEFAULT_VALOR_MINIMO_PAGAMENTO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_MINIMO_PAGAMENTO = new BigDecimal(2);

    private static final String DEFAULT_ID_EXTERNO = "AAAAAAAAAA";
    private static final String UPDATED_ID_EXTERNO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_REFERENCIA = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final Boolean DEFAULT_BLOQUEADO = false;
    private static final Boolean UPDATED_BLOQUEADO = true;

    private static final String ENTITY_API_URL = "/api/link-pagamentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LinkPagamentoRepository linkPagamentoRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private LinkPagamentoRepository linkPagamentoRepositoryMock;

    @Autowired
    private LinkPagamentoMapper linkPagamentoMapper;

    @Mock
    private LinkPagamentoService linkPagamentoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLinkPagamentoMockMvc;

    private LinkPagamento linkPagamento;

    private LinkPagamento insertedLinkPagamento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LinkPagamento createEntity(EntityManager em) {
        LinkPagamento linkPagamento = new LinkPagamento()
            .nome(DEFAULT_NOME)
            .token(DEFAULT_TOKEN)
            .chave(DEFAULT_CHAVE)
            .webhookUrl(DEFAULT_WEBHOOK_URL)
            .urlPagamentoAprovado(DEFAULT_URL_PAGAMENTO_APROVADO)
            .urlPagamentoCancelado(DEFAULT_URL_PAGAMENTO_CANCELADO)
            .urlPagamentoRejeitado(DEFAULT_URL_PAGAMENTO_REJEITADO)
            .tipo(DEFAULT_TIPO)
            .status(DEFAULT_STATUS)
            .dataInicio(DEFAULT_DATA_INICIO)
            .dataFim(DEFAULT_DATA_FIM)
            .desconto(DEFAULT_DESCONTO)
            .tipoDesconto(DEFAULT_TIPO_DESCONTO)
            .valorMinimoPagamento(DEFAULT_VALOR_MINIMO_PAGAMENTO)
            .idExterno(DEFAULT_ID_EXTERNO)
            .numeroReferencia(DEFAULT_NUMERO_REFERENCIA)
            .ativo(DEFAULT_ATIVO)
            .bloqueado(DEFAULT_BLOQUEADO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        linkPagamento.setUsuario(user);
        // Add required entity
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            moedaCarteira = MoedaCarteiraResourceIT.createEntity(em);
            em.persist(moedaCarteira);
            em.flush();
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        linkPagamento.setMoedaCarteira(moedaCarteira);
        return linkPagamento;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LinkPagamento createUpdatedEntity(EntityManager em) {
        LinkPagamento updatedLinkPagamento = new LinkPagamento()
            .nome(UPDATED_NOME)
            .token(UPDATED_TOKEN)
            .chave(UPDATED_CHAVE)
            .webhookUrl(UPDATED_WEBHOOK_URL)
            .urlPagamentoAprovado(UPDATED_URL_PAGAMENTO_APROVADO)
            .urlPagamentoCancelado(UPDATED_URL_PAGAMENTO_CANCELADO)
            .urlPagamentoRejeitado(UPDATED_URL_PAGAMENTO_REJEITADO)
            .tipo(UPDATED_TIPO)
            .status(UPDATED_STATUS)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM)
            .desconto(UPDATED_DESCONTO)
            .tipoDesconto(UPDATED_TIPO_DESCONTO)
            .valorMinimoPagamento(UPDATED_VALOR_MINIMO_PAGAMENTO)
            .idExterno(UPDATED_ID_EXTERNO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .ativo(UPDATED_ATIVO)
            .bloqueado(UPDATED_BLOQUEADO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedLinkPagamento.setUsuario(user);
        // Add required entity
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            moedaCarteira = MoedaCarteiraResourceIT.createUpdatedEntity(em);
            em.persist(moedaCarteira);
            em.flush();
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        updatedLinkPagamento.setMoedaCarteira(moedaCarteira);
        return updatedLinkPagamento;
    }

    @BeforeEach
    void initTest() {
        linkPagamento = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedLinkPagamento != null) {
            linkPagamentoRepository.delete(insertedLinkPagamento);
            insertedLinkPagamento = null;
        }
    }

    @Test
    @Transactional
    void createLinkPagamento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LinkPagamento
        LinkPagamentoDTO linkPagamentoDTO = linkPagamentoMapper.toDto(linkPagamento);
        var returnedLinkPagamentoDTO = om.readValue(
            restLinkPagamentoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkPagamentoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LinkPagamentoDTO.class
        );

        // Validate the LinkPagamento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLinkPagamento = linkPagamentoMapper.toEntity(returnedLinkPagamentoDTO);
        assertLinkPagamentoUpdatableFieldsEquals(returnedLinkPagamento, getPersistedLinkPagamento(returnedLinkPagamento));

        insertedLinkPagamento = returnedLinkPagamento;
    }

    @Test
    @Transactional
    void createLinkPagamentoWithExistingId() throws Exception {
        // Create the LinkPagamento with an existing ID
        linkPagamento.setId(1L);
        LinkPagamentoDTO linkPagamentoDTO = linkPagamentoMapper.toDto(linkPagamento);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLinkPagamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkPagamentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LinkPagamento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        linkPagamento.setNome(null);

        // Create the LinkPagamento, which fails.
        LinkPagamentoDTO linkPagamentoDTO = linkPagamentoMapper.toDto(linkPagamento);

        restLinkPagamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkPagamentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTokenIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        linkPagamento.setToken(null);

        // Create the LinkPagamento, which fails.
        LinkPagamentoDTO linkPagamentoDTO = linkPagamentoMapper.toDto(linkPagamento);

        restLinkPagamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkPagamentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        linkPagamento.setTipo(null);

        // Create the LinkPagamento, which fails.
        LinkPagamentoDTO linkPagamentoDTO = linkPagamentoMapper.toDto(linkPagamento);

        restLinkPagamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkPagamentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        linkPagamento.setStatus(null);

        // Create the LinkPagamento, which fails.
        LinkPagamentoDTO linkPagamentoDTO = linkPagamentoMapper.toDto(linkPagamento);

        restLinkPagamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkPagamentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        linkPagamento.setAtivo(null);

        // Create the LinkPagamento, which fails.
        LinkPagamentoDTO linkPagamentoDTO = linkPagamentoMapper.toDto(linkPagamento);

        restLinkPagamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkPagamentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBloqueadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        linkPagamento.setBloqueado(null);

        // Create the LinkPagamento, which fails.
        LinkPagamentoDTO linkPagamentoDTO = linkPagamentoMapper.toDto(linkPagamento);

        restLinkPagamentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkPagamentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLinkPagamentos() throws Exception {
        // Initialize the database
        insertedLinkPagamento = linkPagamentoRepository.saveAndFlush(linkPagamento);

        // Get all the linkPagamentoList
        restLinkPagamentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(linkPagamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].token").value(hasItem(DEFAULT_TOKEN)))
            .andExpect(jsonPath("$.[*].chave").value(hasItem(DEFAULT_CHAVE)))
            .andExpect(jsonPath("$.[*].webhookUrl").value(hasItem(DEFAULT_WEBHOOK_URL)))
            .andExpect(jsonPath("$.[*].urlPagamentoAprovado").value(hasItem(DEFAULT_URL_PAGAMENTO_APROVADO)))
            .andExpect(jsonPath("$.[*].urlPagamentoCancelado").value(hasItem(DEFAULT_URL_PAGAMENTO_CANCELADO)))
            .andExpect(jsonPath("$.[*].urlPagamentoRejeitado").value(hasItem(DEFAULT_URL_PAGAMENTO_REJEITADO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].dataFim").value(hasItem(DEFAULT_DATA_FIM.toString())))
            .andExpect(jsonPath("$.[*].desconto").value(hasItem(sameNumber(DEFAULT_DESCONTO))))
            .andExpect(jsonPath("$.[*].tipoDesconto").value(hasItem(DEFAULT_TIPO_DESCONTO.toString())))
            .andExpect(jsonPath("$.[*].valorMinimoPagamento").value(hasItem(sameNumber(DEFAULT_VALOR_MINIMO_PAGAMENTO))))
            .andExpect(jsonPath("$.[*].idExterno").value(hasItem(DEFAULT_ID_EXTERNO)))
            .andExpect(jsonPath("$.[*].numeroReferencia").value(hasItem(DEFAULT_NUMERO_REFERENCIA)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)))
            .andExpect(jsonPath("$.[*].bloqueado").value(hasItem(DEFAULT_BLOQUEADO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLinkPagamentosWithEagerRelationshipsIsEnabled() throws Exception {
        when(linkPagamentoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLinkPagamentoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(linkPagamentoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLinkPagamentosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(linkPagamentoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLinkPagamentoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(linkPagamentoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLinkPagamento() throws Exception {
        // Initialize the database
        insertedLinkPagamento = linkPagamentoRepository.saveAndFlush(linkPagamento);

        // Get the linkPagamento
        restLinkPagamentoMockMvc
            .perform(get(ENTITY_API_URL_ID, linkPagamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(linkPagamento.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.token").value(DEFAULT_TOKEN))
            .andExpect(jsonPath("$.chave").value(DEFAULT_CHAVE))
            .andExpect(jsonPath("$.webhookUrl").value(DEFAULT_WEBHOOK_URL))
            .andExpect(jsonPath("$.urlPagamentoAprovado").value(DEFAULT_URL_PAGAMENTO_APROVADO))
            .andExpect(jsonPath("$.urlPagamentoCancelado").value(DEFAULT_URL_PAGAMENTO_CANCELADO))
            .andExpect(jsonPath("$.urlPagamentoRejeitado").value(DEFAULT_URL_PAGAMENTO_REJEITADO))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.dataInicio").value(DEFAULT_DATA_INICIO.toString()))
            .andExpect(jsonPath("$.dataFim").value(DEFAULT_DATA_FIM.toString()))
            .andExpect(jsonPath("$.desconto").value(sameNumber(DEFAULT_DESCONTO)))
            .andExpect(jsonPath("$.tipoDesconto").value(DEFAULT_TIPO_DESCONTO.toString()))
            .andExpect(jsonPath("$.valorMinimoPagamento").value(sameNumber(DEFAULT_VALOR_MINIMO_PAGAMENTO)))
            .andExpect(jsonPath("$.idExterno").value(DEFAULT_ID_EXTERNO))
            .andExpect(jsonPath("$.numeroReferencia").value(DEFAULT_NUMERO_REFERENCIA))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO))
            .andExpect(jsonPath("$.bloqueado").value(DEFAULT_BLOQUEADO));
    }

    @Test
    @Transactional
    void getNonExistingLinkPagamento() throws Exception {
        // Get the linkPagamento
        restLinkPagamentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLinkPagamento() throws Exception {
        // Initialize the database
        insertedLinkPagamento = linkPagamentoRepository.saveAndFlush(linkPagamento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the linkPagamento
        LinkPagamento updatedLinkPagamento = linkPagamentoRepository.findById(linkPagamento.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLinkPagamento are not directly saved in db
        em.detach(updatedLinkPagamento);
        updatedLinkPagamento
            .nome(UPDATED_NOME)
            .token(UPDATED_TOKEN)
            .chave(UPDATED_CHAVE)
            .webhookUrl(UPDATED_WEBHOOK_URL)
            .urlPagamentoAprovado(UPDATED_URL_PAGAMENTO_APROVADO)
            .urlPagamentoCancelado(UPDATED_URL_PAGAMENTO_CANCELADO)
            .urlPagamentoRejeitado(UPDATED_URL_PAGAMENTO_REJEITADO)
            .tipo(UPDATED_TIPO)
            .status(UPDATED_STATUS)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM)
            .desconto(UPDATED_DESCONTO)
            .tipoDesconto(UPDATED_TIPO_DESCONTO)
            .valorMinimoPagamento(UPDATED_VALOR_MINIMO_PAGAMENTO)
            .idExterno(UPDATED_ID_EXTERNO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .ativo(UPDATED_ATIVO)
            .bloqueado(UPDATED_BLOQUEADO);
        LinkPagamentoDTO linkPagamentoDTO = linkPagamentoMapper.toDto(updatedLinkPagamento);

        restLinkPagamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, linkPagamentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(linkPagamentoDTO))
            )
            .andExpect(status().isOk());

        // Validate the LinkPagamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLinkPagamentoToMatchAllProperties(updatedLinkPagamento);
    }

    @Test
    @Transactional
    void putNonExistingLinkPagamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkPagamento.setId(longCount.incrementAndGet());

        // Create the LinkPagamento
        LinkPagamentoDTO linkPagamentoDTO = linkPagamentoMapper.toDto(linkPagamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLinkPagamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, linkPagamentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(linkPagamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LinkPagamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLinkPagamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkPagamento.setId(longCount.incrementAndGet());

        // Create the LinkPagamento
        LinkPagamentoDTO linkPagamentoDTO = linkPagamentoMapper.toDto(linkPagamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLinkPagamentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(linkPagamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LinkPagamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLinkPagamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkPagamento.setId(longCount.incrementAndGet());

        // Create the LinkPagamento
        LinkPagamentoDTO linkPagamentoDTO = linkPagamentoMapper.toDto(linkPagamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLinkPagamentoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkPagamentoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LinkPagamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLinkPagamentoWithPatch() throws Exception {
        // Initialize the database
        insertedLinkPagamento = linkPagamentoRepository.saveAndFlush(linkPagamento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the linkPagamento using partial update
        LinkPagamento partialUpdatedLinkPagamento = new LinkPagamento();
        partialUpdatedLinkPagamento.setId(linkPagamento.getId());

        partialUpdatedLinkPagamento
            .nome(UPDATED_NOME)
            .token(UPDATED_TOKEN)
            .urlPagamentoCancelado(UPDATED_URL_PAGAMENTO_CANCELADO)
            .urlPagamentoRejeitado(UPDATED_URL_PAGAMENTO_REJEITADO)
            .status(UPDATED_STATUS)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM)
            .valorMinimoPagamento(UPDATED_VALOR_MINIMO_PAGAMENTO)
            .idExterno(UPDATED_ID_EXTERNO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .ativo(UPDATED_ATIVO);

        restLinkPagamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLinkPagamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLinkPagamento))
            )
            .andExpect(status().isOk());

        // Validate the LinkPagamento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLinkPagamentoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLinkPagamento, linkPagamento),
            getPersistedLinkPagamento(linkPagamento)
        );
    }

    @Test
    @Transactional
    void fullUpdateLinkPagamentoWithPatch() throws Exception {
        // Initialize the database
        insertedLinkPagamento = linkPagamentoRepository.saveAndFlush(linkPagamento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the linkPagamento using partial update
        LinkPagamento partialUpdatedLinkPagamento = new LinkPagamento();
        partialUpdatedLinkPagamento.setId(linkPagamento.getId());

        partialUpdatedLinkPagamento
            .nome(UPDATED_NOME)
            .token(UPDATED_TOKEN)
            .chave(UPDATED_CHAVE)
            .webhookUrl(UPDATED_WEBHOOK_URL)
            .urlPagamentoAprovado(UPDATED_URL_PAGAMENTO_APROVADO)
            .urlPagamentoCancelado(UPDATED_URL_PAGAMENTO_CANCELADO)
            .urlPagamentoRejeitado(UPDATED_URL_PAGAMENTO_REJEITADO)
            .tipo(UPDATED_TIPO)
            .status(UPDATED_STATUS)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM)
            .desconto(UPDATED_DESCONTO)
            .tipoDesconto(UPDATED_TIPO_DESCONTO)
            .valorMinimoPagamento(UPDATED_VALOR_MINIMO_PAGAMENTO)
            .idExterno(UPDATED_ID_EXTERNO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .ativo(UPDATED_ATIVO)
            .bloqueado(UPDATED_BLOQUEADO);

        restLinkPagamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLinkPagamento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLinkPagamento))
            )
            .andExpect(status().isOk());

        // Validate the LinkPagamento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLinkPagamentoUpdatableFieldsEquals(partialUpdatedLinkPagamento, getPersistedLinkPagamento(partialUpdatedLinkPagamento));
    }

    @Test
    @Transactional
    void patchNonExistingLinkPagamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkPagamento.setId(longCount.incrementAndGet());

        // Create the LinkPagamento
        LinkPagamentoDTO linkPagamentoDTO = linkPagamentoMapper.toDto(linkPagamento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLinkPagamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, linkPagamentoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(linkPagamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LinkPagamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLinkPagamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkPagamento.setId(longCount.incrementAndGet());

        // Create the LinkPagamento
        LinkPagamentoDTO linkPagamentoDTO = linkPagamentoMapper.toDto(linkPagamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLinkPagamentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(linkPagamentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LinkPagamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLinkPagamento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkPagamento.setId(longCount.incrementAndGet());

        // Create the LinkPagamento
        LinkPagamentoDTO linkPagamentoDTO = linkPagamentoMapper.toDto(linkPagamento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLinkPagamentoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(linkPagamentoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LinkPagamento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLinkPagamento() throws Exception {
        // Initialize the database
        insertedLinkPagamento = linkPagamentoRepository.saveAndFlush(linkPagamento);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the linkPagamento
        restLinkPagamentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, linkPagamento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return linkPagamentoRepository.count();
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

    protected LinkPagamento getPersistedLinkPagamento(LinkPagamento linkPagamento) {
        return linkPagamentoRepository.findById(linkPagamento.getId()).orElseThrow();
    }

    protected void assertPersistedLinkPagamentoToMatchAllProperties(LinkPagamento expectedLinkPagamento) {
        assertLinkPagamentoAllPropertiesEquals(expectedLinkPagamento, getPersistedLinkPagamento(expectedLinkPagamento));
    }

    protected void assertPersistedLinkPagamentoToMatchUpdatableProperties(LinkPagamento expectedLinkPagamento) {
        assertLinkPagamentoAllUpdatablePropertiesEquals(expectedLinkPagamento, getPersistedLinkPagamento(expectedLinkPagamento));
    }
}
