package com.bankernel.web.rest;

import static com.bankernel.domain.LinkCobrancaAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bankernel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.LinkCobranca;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumStatusLinkCobranca;
import com.bankernel.domain.enumeration.EnumTipoDesconto;
import com.bankernel.domain.enumeration.EnumTipoLinkCobranca;
import com.bankernel.repository.LinkCobrancaRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.LinkCobrancaService;
import com.bankernel.service.dto.LinkCobrancaDTO;
import com.bankernel.service.mapper.LinkCobrancaMapper;
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
 * Integration tests for the {@link LinkCobrancaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LinkCobrancaResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_CHAVE_ACESSO = "AAAAAAAAAA";
    private static final String UPDATED_CHAVE_ACESSO = "BBBBBBBBBB";

    private static final String DEFAULT_CHAVE_API = "AAAAAAAAAA";
    private static final String UPDATED_CHAVE_API = "BBBBBBBBBB";

    private static final String DEFAULT_URL_RETORNO = "AAAAAAAAAA";
    private static final String UPDATED_URL_RETORNO = "BBBBBBBBBB";

    private static final String DEFAULT_URL_PAGAMENTO_APROVADO = "AAAAAAAAAA";
    private static final String UPDATED_URL_PAGAMENTO_APROVADO = "BBBBBBBBBB";

    private static final String DEFAULT_URL_PAGAMENTO_CANCELADO = "AAAAAAAAAA";
    private static final String UPDATED_URL_PAGAMENTO_CANCELADO = "BBBBBBBBBB";

    private static final String DEFAULT_URL_PAGAMENTO_REJEITADO = "AAAAAAAAAA";
    private static final String UPDATED_URL_PAGAMENTO_REJEITADO = "BBBBBBBBBB";

    private static final EnumTipoLinkCobranca DEFAULT_TIPO = EnumTipoLinkCobranca.UNICO;
    private static final EnumTipoLinkCobranca UPDATED_TIPO = EnumTipoLinkCobranca.RECORRENTE;

    private static final EnumStatusLinkCobranca DEFAULT_SITUACAO = EnumStatusLinkCobranca.ATIVO;
    private static final EnumStatusLinkCobranca UPDATED_SITUACAO = EnumStatusLinkCobranca.INATIVO;

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

    private static final String DEFAULT_IDENTIFICADOR_EXTERNO = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR_EXTERNO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_REFERENCIA = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final Boolean DEFAULT_BLOQUEADO = false;
    private static final Boolean UPDATED_BLOQUEADO = true;

    private static final String ENTITY_API_URL = "/api/link-cobrancas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LinkCobrancaRepository linkCobrancaRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private LinkCobrancaRepository linkCobrancaRepositoryMock;

    @Autowired
    private LinkCobrancaMapper linkCobrancaMapper;

    @Mock
    private LinkCobrancaService linkCobrancaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLinkCobrancaMockMvc;

    private LinkCobranca linkCobranca;

    private LinkCobranca insertedLinkCobranca;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LinkCobranca createEntity(EntityManager em) {
        LinkCobranca linkCobranca = new LinkCobranca()
            .nome(DEFAULT_NOME)
            .chaveAcesso(DEFAULT_CHAVE_ACESSO)
            .chaveApi(DEFAULT_CHAVE_API)
            .urlRetorno(DEFAULT_URL_RETORNO)
            .urlPagamentoAprovado(DEFAULT_URL_PAGAMENTO_APROVADO)
            .urlPagamentoCancelado(DEFAULT_URL_PAGAMENTO_CANCELADO)
            .urlPagamentoRejeitado(DEFAULT_URL_PAGAMENTO_REJEITADO)
            .tipo(DEFAULT_TIPO)
            .situacao(DEFAULT_SITUACAO)
            .dataInicio(DEFAULT_DATA_INICIO)
            .dataFim(DEFAULT_DATA_FIM)
            .desconto(DEFAULT_DESCONTO)
            .tipoDesconto(DEFAULT_TIPO_DESCONTO)
            .valorMinimoPagamento(DEFAULT_VALOR_MINIMO_PAGAMENTO)
            .identificadorExterno(DEFAULT_IDENTIFICADOR_EXTERNO)
            .numeroReferencia(DEFAULT_NUMERO_REFERENCIA)
            .ativo(DEFAULT_ATIVO)
            .bloqueado(DEFAULT_BLOQUEADO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        linkCobranca.setUsuario(user);
        // Add required entity
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            moedaCarteira = MoedaCarteiraResourceIT.createEntity(em);
            em.persist(moedaCarteira);
            em.flush();
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        linkCobranca.setMoedaCarteira(moedaCarteira);
        return linkCobranca;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LinkCobranca createUpdatedEntity(EntityManager em) {
        LinkCobranca updatedLinkCobranca = new LinkCobranca()
            .nome(UPDATED_NOME)
            .chaveAcesso(UPDATED_CHAVE_ACESSO)
            .chaveApi(UPDATED_CHAVE_API)
            .urlRetorno(UPDATED_URL_RETORNO)
            .urlPagamentoAprovado(UPDATED_URL_PAGAMENTO_APROVADO)
            .urlPagamentoCancelado(UPDATED_URL_PAGAMENTO_CANCELADO)
            .urlPagamentoRejeitado(UPDATED_URL_PAGAMENTO_REJEITADO)
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM)
            .desconto(UPDATED_DESCONTO)
            .tipoDesconto(UPDATED_TIPO_DESCONTO)
            .valorMinimoPagamento(UPDATED_VALOR_MINIMO_PAGAMENTO)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .ativo(UPDATED_ATIVO)
            .bloqueado(UPDATED_BLOQUEADO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedLinkCobranca.setUsuario(user);
        // Add required entity
        MoedaCarteira moedaCarteira;
        if (TestUtil.findAll(em, MoedaCarteira.class).isEmpty()) {
            moedaCarteira = MoedaCarteiraResourceIT.createUpdatedEntity(em);
            em.persist(moedaCarteira);
            em.flush();
        } else {
            moedaCarteira = TestUtil.findAll(em, MoedaCarteira.class).get(0);
        }
        updatedLinkCobranca.setMoedaCarteira(moedaCarteira);
        return updatedLinkCobranca;
    }

    @BeforeEach
    void initTest() {
        linkCobranca = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedLinkCobranca != null) {
            linkCobrancaRepository.delete(insertedLinkCobranca);
            insertedLinkCobranca = null;
        }
    }

    @Test
    @Transactional
    void createLinkCobranca() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LinkCobranca
        LinkCobrancaDTO linkCobrancaDTO = linkCobrancaMapper.toDto(linkCobranca);
        var returnedLinkCobrancaDTO = om.readValue(
            restLinkCobrancaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkCobrancaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LinkCobrancaDTO.class
        );

        // Validate the LinkCobranca in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLinkCobranca = linkCobrancaMapper.toEntity(returnedLinkCobrancaDTO);
        assertLinkCobrancaUpdatableFieldsEquals(returnedLinkCobranca, getPersistedLinkCobranca(returnedLinkCobranca));

        insertedLinkCobranca = returnedLinkCobranca;
    }

    @Test
    @Transactional
    void createLinkCobrancaWithExistingId() throws Exception {
        // Create the LinkCobranca with an existing ID
        linkCobranca.setId(1L);
        LinkCobrancaDTO linkCobrancaDTO = linkCobrancaMapper.toDto(linkCobranca);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLinkCobrancaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkCobrancaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LinkCobranca in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        linkCobranca.setNome(null);

        // Create the LinkCobranca, which fails.
        LinkCobrancaDTO linkCobrancaDTO = linkCobrancaMapper.toDto(linkCobranca);

        restLinkCobrancaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkCobrancaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChaveAcessoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        linkCobranca.setChaveAcesso(null);

        // Create the LinkCobranca, which fails.
        LinkCobrancaDTO linkCobrancaDTO = linkCobrancaMapper.toDto(linkCobranca);

        restLinkCobrancaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkCobrancaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        linkCobranca.setTipo(null);

        // Create the LinkCobranca, which fails.
        LinkCobrancaDTO linkCobrancaDTO = linkCobrancaMapper.toDto(linkCobranca);

        restLinkCobrancaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkCobrancaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSituacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        linkCobranca.setSituacao(null);

        // Create the LinkCobranca, which fails.
        LinkCobrancaDTO linkCobrancaDTO = linkCobrancaMapper.toDto(linkCobranca);

        restLinkCobrancaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkCobrancaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        linkCobranca.setAtivo(null);

        // Create the LinkCobranca, which fails.
        LinkCobrancaDTO linkCobrancaDTO = linkCobrancaMapper.toDto(linkCobranca);

        restLinkCobrancaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkCobrancaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBloqueadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        linkCobranca.setBloqueado(null);

        // Create the LinkCobranca, which fails.
        LinkCobrancaDTO linkCobrancaDTO = linkCobrancaMapper.toDto(linkCobranca);

        restLinkCobrancaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkCobrancaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLinkCobrancas() throws Exception {
        // Initialize the database
        insertedLinkCobranca = linkCobrancaRepository.saveAndFlush(linkCobranca);

        // Get all the linkCobrancaList
        restLinkCobrancaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(linkCobranca.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].chaveAcesso").value(hasItem(DEFAULT_CHAVE_ACESSO)))
            .andExpect(jsonPath("$.[*].chaveApi").value(hasItem(DEFAULT_CHAVE_API)))
            .andExpect(jsonPath("$.[*].urlRetorno").value(hasItem(DEFAULT_URL_RETORNO)))
            .andExpect(jsonPath("$.[*].urlPagamentoAprovado").value(hasItem(DEFAULT_URL_PAGAMENTO_APROVADO)))
            .andExpect(jsonPath("$.[*].urlPagamentoCancelado").value(hasItem(DEFAULT_URL_PAGAMENTO_CANCELADO)))
            .andExpect(jsonPath("$.[*].urlPagamentoRejeitado").value(hasItem(DEFAULT_URL_PAGAMENTO_REJEITADO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].dataInicio").value(hasItem(DEFAULT_DATA_INICIO.toString())))
            .andExpect(jsonPath("$.[*].dataFim").value(hasItem(DEFAULT_DATA_FIM.toString())))
            .andExpect(jsonPath("$.[*].desconto").value(hasItem(sameNumber(DEFAULT_DESCONTO))))
            .andExpect(jsonPath("$.[*].tipoDesconto").value(hasItem(DEFAULT_TIPO_DESCONTO.toString())))
            .andExpect(jsonPath("$.[*].valorMinimoPagamento").value(hasItem(sameNumber(DEFAULT_VALOR_MINIMO_PAGAMENTO))))
            .andExpect(jsonPath("$.[*].identificadorExterno").value(hasItem(DEFAULT_IDENTIFICADOR_EXTERNO)))
            .andExpect(jsonPath("$.[*].numeroReferencia").value(hasItem(DEFAULT_NUMERO_REFERENCIA)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)))
            .andExpect(jsonPath("$.[*].bloqueado").value(hasItem(DEFAULT_BLOQUEADO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLinkCobrancasWithEagerRelationshipsIsEnabled() throws Exception {
        when(linkCobrancaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLinkCobrancaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(linkCobrancaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLinkCobrancasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(linkCobrancaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLinkCobrancaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(linkCobrancaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLinkCobranca() throws Exception {
        // Initialize the database
        insertedLinkCobranca = linkCobrancaRepository.saveAndFlush(linkCobranca);

        // Get the linkCobranca
        restLinkCobrancaMockMvc
            .perform(get(ENTITY_API_URL_ID, linkCobranca.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(linkCobranca.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.chaveAcesso").value(DEFAULT_CHAVE_ACESSO))
            .andExpect(jsonPath("$.chaveApi").value(DEFAULT_CHAVE_API))
            .andExpect(jsonPath("$.urlRetorno").value(DEFAULT_URL_RETORNO))
            .andExpect(jsonPath("$.urlPagamentoAprovado").value(DEFAULT_URL_PAGAMENTO_APROVADO))
            .andExpect(jsonPath("$.urlPagamentoCancelado").value(DEFAULT_URL_PAGAMENTO_CANCELADO))
            .andExpect(jsonPath("$.urlPagamentoRejeitado").value(DEFAULT_URL_PAGAMENTO_REJEITADO))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()))
            .andExpect(jsonPath("$.dataInicio").value(DEFAULT_DATA_INICIO.toString()))
            .andExpect(jsonPath("$.dataFim").value(DEFAULT_DATA_FIM.toString()))
            .andExpect(jsonPath("$.desconto").value(sameNumber(DEFAULT_DESCONTO)))
            .andExpect(jsonPath("$.tipoDesconto").value(DEFAULT_TIPO_DESCONTO.toString()))
            .andExpect(jsonPath("$.valorMinimoPagamento").value(sameNumber(DEFAULT_VALOR_MINIMO_PAGAMENTO)))
            .andExpect(jsonPath("$.identificadorExterno").value(DEFAULT_IDENTIFICADOR_EXTERNO))
            .andExpect(jsonPath("$.numeroReferencia").value(DEFAULT_NUMERO_REFERENCIA))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO))
            .andExpect(jsonPath("$.bloqueado").value(DEFAULT_BLOQUEADO));
    }

    @Test
    @Transactional
    void getNonExistingLinkCobranca() throws Exception {
        // Get the linkCobranca
        restLinkCobrancaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLinkCobranca() throws Exception {
        // Initialize the database
        insertedLinkCobranca = linkCobrancaRepository.saveAndFlush(linkCobranca);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the linkCobranca
        LinkCobranca updatedLinkCobranca = linkCobrancaRepository.findById(linkCobranca.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLinkCobranca are not directly saved in db
        em.detach(updatedLinkCobranca);
        updatedLinkCobranca
            .nome(UPDATED_NOME)
            .chaveAcesso(UPDATED_CHAVE_ACESSO)
            .chaveApi(UPDATED_CHAVE_API)
            .urlRetorno(UPDATED_URL_RETORNO)
            .urlPagamentoAprovado(UPDATED_URL_PAGAMENTO_APROVADO)
            .urlPagamentoCancelado(UPDATED_URL_PAGAMENTO_CANCELADO)
            .urlPagamentoRejeitado(UPDATED_URL_PAGAMENTO_REJEITADO)
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM)
            .desconto(UPDATED_DESCONTO)
            .tipoDesconto(UPDATED_TIPO_DESCONTO)
            .valorMinimoPagamento(UPDATED_VALOR_MINIMO_PAGAMENTO)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .ativo(UPDATED_ATIVO)
            .bloqueado(UPDATED_BLOQUEADO);
        LinkCobrancaDTO linkCobrancaDTO = linkCobrancaMapper.toDto(updatedLinkCobranca);

        restLinkCobrancaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, linkCobrancaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(linkCobrancaDTO))
            )
            .andExpect(status().isOk());

        // Validate the LinkCobranca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLinkCobrancaToMatchAllProperties(updatedLinkCobranca);
    }

    @Test
    @Transactional
    void putNonExistingLinkCobranca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkCobranca.setId(longCount.incrementAndGet());

        // Create the LinkCobranca
        LinkCobrancaDTO linkCobrancaDTO = linkCobrancaMapper.toDto(linkCobranca);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLinkCobrancaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, linkCobrancaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(linkCobrancaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LinkCobranca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLinkCobranca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkCobranca.setId(longCount.incrementAndGet());

        // Create the LinkCobranca
        LinkCobrancaDTO linkCobrancaDTO = linkCobrancaMapper.toDto(linkCobranca);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLinkCobrancaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(linkCobrancaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LinkCobranca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLinkCobranca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkCobranca.setId(longCount.incrementAndGet());

        // Create the LinkCobranca
        LinkCobrancaDTO linkCobrancaDTO = linkCobrancaMapper.toDto(linkCobranca);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLinkCobrancaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(linkCobrancaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LinkCobranca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLinkCobrancaWithPatch() throws Exception {
        // Initialize the database
        insertedLinkCobranca = linkCobrancaRepository.saveAndFlush(linkCobranca);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the linkCobranca using partial update
        LinkCobranca partialUpdatedLinkCobranca = new LinkCobranca();
        partialUpdatedLinkCobranca.setId(linkCobranca.getId());

        partialUpdatedLinkCobranca
            .nome(UPDATED_NOME)
            .chaveAcesso(UPDATED_CHAVE_ACESSO)
            .urlRetorno(UPDATED_URL_RETORNO)
            .urlPagamentoAprovado(UPDATED_URL_PAGAMENTO_APROVADO)
            .urlPagamentoCancelado(UPDATED_URL_PAGAMENTO_CANCELADO)
            .tipo(UPDATED_TIPO)
            .dataInicio(UPDATED_DATA_INICIO)
            .valorMinimoPagamento(UPDATED_VALOR_MINIMO_PAGAMENTO)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .ativo(UPDATED_ATIVO)
            .bloqueado(UPDATED_BLOQUEADO);

        restLinkCobrancaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLinkCobranca.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLinkCobranca))
            )
            .andExpect(status().isOk());

        // Validate the LinkCobranca in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLinkCobrancaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLinkCobranca, linkCobranca),
            getPersistedLinkCobranca(linkCobranca)
        );
    }

    @Test
    @Transactional
    void fullUpdateLinkCobrancaWithPatch() throws Exception {
        // Initialize the database
        insertedLinkCobranca = linkCobrancaRepository.saveAndFlush(linkCobranca);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the linkCobranca using partial update
        LinkCobranca partialUpdatedLinkCobranca = new LinkCobranca();
        partialUpdatedLinkCobranca.setId(linkCobranca.getId());

        partialUpdatedLinkCobranca
            .nome(UPDATED_NOME)
            .chaveAcesso(UPDATED_CHAVE_ACESSO)
            .chaveApi(UPDATED_CHAVE_API)
            .urlRetorno(UPDATED_URL_RETORNO)
            .urlPagamentoAprovado(UPDATED_URL_PAGAMENTO_APROVADO)
            .urlPagamentoCancelado(UPDATED_URL_PAGAMENTO_CANCELADO)
            .urlPagamentoRejeitado(UPDATED_URL_PAGAMENTO_REJEITADO)
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .dataInicio(UPDATED_DATA_INICIO)
            .dataFim(UPDATED_DATA_FIM)
            .desconto(UPDATED_DESCONTO)
            .tipoDesconto(UPDATED_TIPO_DESCONTO)
            .valorMinimoPagamento(UPDATED_VALOR_MINIMO_PAGAMENTO)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .ativo(UPDATED_ATIVO)
            .bloqueado(UPDATED_BLOQUEADO);

        restLinkCobrancaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLinkCobranca.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLinkCobranca))
            )
            .andExpect(status().isOk());

        // Validate the LinkCobranca in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLinkCobrancaUpdatableFieldsEquals(partialUpdatedLinkCobranca, getPersistedLinkCobranca(partialUpdatedLinkCobranca));
    }

    @Test
    @Transactional
    void patchNonExistingLinkCobranca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkCobranca.setId(longCount.incrementAndGet());

        // Create the LinkCobranca
        LinkCobrancaDTO linkCobrancaDTO = linkCobrancaMapper.toDto(linkCobranca);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLinkCobrancaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, linkCobrancaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(linkCobrancaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LinkCobranca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLinkCobranca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkCobranca.setId(longCount.incrementAndGet());

        // Create the LinkCobranca
        LinkCobrancaDTO linkCobrancaDTO = linkCobrancaMapper.toDto(linkCobranca);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLinkCobrancaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(linkCobrancaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LinkCobranca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLinkCobranca() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        linkCobranca.setId(longCount.incrementAndGet());

        // Create the LinkCobranca
        LinkCobrancaDTO linkCobrancaDTO = linkCobrancaMapper.toDto(linkCobranca);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLinkCobrancaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(linkCobrancaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LinkCobranca in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLinkCobranca() throws Exception {
        // Initialize the database
        insertedLinkCobranca = linkCobrancaRepository.saveAndFlush(linkCobranca);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the linkCobranca
        restLinkCobrancaMockMvc
            .perform(delete(ENTITY_API_URL_ID, linkCobranca.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return linkCobrancaRepository.count();
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

    protected LinkCobranca getPersistedLinkCobranca(LinkCobranca linkCobranca) {
        return linkCobrancaRepository.findById(linkCobranca.getId()).orElseThrow();
    }

    protected void assertPersistedLinkCobrancaToMatchAllProperties(LinkCobranca expectedLinkCobranca) {
        assertLinkCobrancaAllPropertiesEquals(expectedLinkCobranca, getPersistedLinkCobranca(expectedLinkCobranca));
    }

    protected void assertPersistedLinkCobrancaToMatchUpdatableProperties(LinkCobranca expectedLinkCobranca) {
        assertLinkCobrancaAllUpdatablePropertiesEquals(expectedLinkCobranca, getPersistedLinkCobranca(expectedLinkCobranca));
    }
}
