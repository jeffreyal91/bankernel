package com.bankernel.web.rest;

import static com.bankernel.domain.AssinaturaRecorrenciaAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.AssinaturaRecorrencia;
import com.bankernel.domain.PlanoRecorrencia;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumStatusAssinatura;
import com.bankernel.domain.enumeration.EnumTipoAssinatura;
import com.bankernel.domain.enumeration.EnumTipoPagamentoAssinatura;
import com.bankernel.repository.AssinaturaRecorrenciaRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.AssinaturaRecorrenciaService;
import com.bankernel.service.dto.AssinaturaRecorrenciaDTO;
import com.bankernel.service.mapper.AssinaturaRecorrenciaMapper;
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
 * Integration tests for the {@link AssinaturaRecorrenciaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AssinaturaRecorrenciaResourceIT {

    private static final String DEFAULT_NUMERO_ORDEM = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_ORDEM = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_REFERENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_DEVEDOR_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_DEVEDOR_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_DEVEDOR_DOCUMENTO = "AAAAAAAAAA";
    private static final String UPDATED_DEVEDOR_DOCUMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_DEVEDOR_NOME = "AAAAAAAAAA";
    private static final String UPDATED_DEVEDOR_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DEVEDOR_TELEFONE = "AAAAAAAAAA";
    private static final String UPDATED_DEVEDOR_TELEFONE = "BBBBBBBBBB";

    private static final String DEFAULT_DEVEDOR_CEP = "AAAAAAAA";
    private static final String UPDATED_DEVEDOR_CEP = "BBBBBBBB";

    private static final String DEFAULT_DEVEDOR_UF = "AA";
    private static final String UPDATED_DEVEDOR_UF = "BB";

    private static final String DEFAULT_DEVEDOR_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_DEVEDOR_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_DEVEDOR_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_DEVEDOR_BAIRRO = "BBBBBBBBBB";

    private static final String DEFAULT_DEVEDOR_ENDERECO = "AAAAAAAAAA";
    private static final String UPDATED_DEVEDOR_ENDERECO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final String DEFAULT_ID_PRODUTO_EXTERNO = "AAAAAAAAAA";
    private static final String UPDATED_ID_PRODUTO_EXTERNO = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICADOR_EXTERNO = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR_EXTERNO = "BBBBBBBBBB";

    private static final String DEFAULT_NOME_PRODUTO_EXTERNO = "AAAAAAAAAA";
    private static final String UPDATED_NOME_PRODUTO_EXTERNO = "BBBBBBBBBB";

    private static final EnumTipoAssinatura DEFAULT_TIPO = EnumTipoAssinatura.PADRAO;
    private static final EnumTipoAssinatura UPDATED_TIPO = EnumTipoAssinatura.PERSONALIZADA;

    private static final EnumTipoPagamentoAssinatura DEFAULT_TIPO_PAGAMENTO = EnumTipoPagamentoAssinatura.PIX;
    private static final EnumTipoPagamentoAssinatura UPDATED_TIPO_PAGAMENTO = EnumTipoPagamentoAssinatura.BOLETO;

    private static final EnumStatusAssinatura DEFAULT_SITUACAO = EnumStatusAssinatura.ATIVA;
    private static final EnumStatusAssinatura UPDATED_SITUACAO = EnumStatusAssinatura.CANCELADA;

    private static final String ENTITY_API_URL = "/api/assinatura-recorrencias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AssinaturaRecorrenciaRepository assinaturaRecorrenciaRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private AssinaturaRecorrenciaRepository assinaturaRecorrenciaRepositoryMock;

    @Autowired
    private AssinaturaRecorrenciaMapper assinaturaRecorrenciaMapper;

    @Mock
    private AssinaturaRecorrenciaService assinaturaRecorrenciaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAssinaturaRecorrenciaMockMvc;

    private AssinaturaRecorrencia assinaturaRecorrencia;

    private AssinaturaRecorrencia insertedAssinaturaRecorrencia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssinaturaRecorrencia createEntity(EntityManager em) {
        AssinaturaRecorrencia assinaturaRecorrencia = new AssinaturaRecorrencia()
            .numeroOrdem(DEFAULT_NUMERO_ORDEM)
            .numeroReferencia(DEFAULT_NUMERO_REFERENCIA)
            .devedorEmail(DEFAULT_DEVEDOR_EMAIL)
            .devedorDocumento(DEFAULT_DEVEDOR_DOCUMENTO)
            .devedorNome(DEFAULT_DEVEDOR_NOME)
            .devedorTelefone(DEFAULT_DEVEDOR_TELEFONE)
            .devedorCep(DEFAULT_DEVEDOR_CEP)
            .devedorUf(DEFAULT_DEVEDOR_UF)
            .devedorCidade(DEFAULT_DEVEDOR_CIDADE)
            .devedorBairro(DEFAULT_DEVEDOR_BAIRRO)
            .devedorEndereco(DEFAULT_DEVEDOR_ENDERECO)
            .ativo(DEFAULT_ATIVO)
            .idProdutoExterno(DEFAULT_ID_PRODUTO_EXTERNO)
            .identificadorExterno(DEFAULT_IDENTIFICADOR_EXTERNO)
            .nomeProdutoExterno(DEFAULT_NOME_PRODUTO_EXTERNO)
            .tipo(DEFAULT_TIPO)
            .tipoPagamento(DEFAULT_TIPO_PAGAMENTO)
            .situacao(DEFAULT_SITUACAO);
        // Add required entity
        PlanoRecorrencia planoRecorrencia;
        if (TestUtil.findAll(em, PlanoRecorrencia.class).isEmpty()) {
            planoRecorrencia = PlanoRecorrenciaResourceIT.createEntity(em);
            em.persist(planoRecorrencia);
            em.flush();
        } else {
            planoRecorrencia = TestUtil.findAll(em, PlanoRecorrencia.class).get(0);
        }
        assinaturaRecorrencia.setPlanoRecorrencia(planoRecorrencia);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        assinaturaRecorrencia.setUsuario(user);
        return assinaturaRecorrencia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AssinaturaRecorrencia createUpdatedEntity(EntityManager em) {
        AssinaturaRecorrencia updatedAssinaturaRecorrencia = new AssinaturaRecorrencia()
            .numeroOrdem(UPDATED_NUMERO_ORDEM)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .devedorEmail(UPDATED_DEVEDOR_EMAIL)
            .devedorDocumento(UPDATED_DEVEDOR_DOCUMENTO)
            .devedorNome(UPDATED_DEVEDOR_NOME)
            .devedorTelefone(UPDATED_DEVEDOR_TELEFONE)
            .devedorCep(UPDATED_DEVEDOR_CEP)
            .devedorUf(UPDATED_DEVEDOR_UF)
            .devedorCidade(UPDATED_DEVEDOR_CIDADE)
            .devedorBairro(UPDATED_DEVEDOR_BAIRRO)
            .devedorEndereco(UPDATED_DEVEDOR_ENDERECO)
            .ativo(UPDATED_ATIVO)
            .idProdutoExterno(UPDATED_ID_PRODUTO_EXTERNO)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .nomeProdutoExterno(UPDATED_NOME_PRODUTO_EXTERNO)
            .tipo(UPDATED_TIPO)
            .tipoPagamento(UPDATED_TIPO_PAGAMENTO)
            .situacao(UPDATED_SITUACAO);
        // Add required entity
        PlanoRecorrencia planoRecorrencia;
        if (TestUtil.findAll(em, PlanoRecorrencia.class).isEmpty()) {
            planoRecorrencia = PlanoRecorrenciaResourceIT.createUpdatedEntity(em);
            em.persist(planoRecorrencia);
            em.flush();
        } else {
            planoRecorrencia = TestUtil.findAll(em, PlanoRecorrencia.class).get(0);
        }
        updatedAssinaturaRecorrencia.setPlanoRecorrencia(planoRecorrencia);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedAssinaturaRecorrencia.setUsuario(user);
        return updatedAssinaturaRecorrencia;
    }

    @BeforeEach
    void initTest() {
        assinaturaRecorrencia = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedAssinaturaRecorrencia != null) {
            assinaturaRecorrenciaRepository.delete(insertedAssinaturaRecorrencia);
            insertedAssinaturaRecorrencia = null;
        }
    }

    @Test
    @Transactional
    void createAssinaturaRecorrencia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AssinaturaRecorrencia
        AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO = assinaturaRecorrenciaMapper.toDto(assinaturaRecorrencia);
        var returnedAssinaturaRecorrenciaDTO = om.readValue(
            restAssinaturaRecorrenciaMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assinaturaRecorrenciaDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AssinaturaRecorrenciaDTO.class
        );

        // Validate the AssinaturaRecorrencia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAssinaturaRecorrencia = assinaturaRecorrenciaMapper.toEntity(returnedAssinaturaRecorrenciaDTO);
        assertAssinaturaRecorrenciaUpdatableFieldsEquals(
            returnedAssinaturaRecorrencia,
            getPersistedAssinaturaRecorrencia(returnedAssinaturaRecorrencia)
        );

        insertedAssinaturaRecorrencia = returnedAssinaturaRecorrencia;
    }

    @Test
    @Transactional
    void createAssinaturaRecorrenciaWithExistingId() throws Exception {
        // Create the AssinaturaRecorrencia with an existing ID
        assinaturaRecorrencia.setId(1L);
        AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO = assinaturaRecorrenciaMapper.toDto(assinaturaRecorrencia);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAssinaturaRecorrenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assinaturaRecorrenciaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AssinaturaRecorrencia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        assinaturaRecorrencia.setAtivo(null);

        // Create the AssinaturaRecorrencia, which fails.
        AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO = assinaturaRecorrenciaMapper.toDto(assinaturaRecorrencia);

        restAssinaturaRecorrenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assinaturaRecorrenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        assinaturaRecorrencia.setTipo(null);

        // Create the AssinaturaRecorrencia, which fails.
        AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO = assinaturaRecorrenciaMapper.toDto(assinaturaRecorrencia);

        restAssinaturaRecorrenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assinaturaRecorrenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoPagamentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        assinaturaRecorrencia.setTipoPagamento(null);

        // Create the AssinaturaRecorrencia, which fails.
        AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO = assinaturaRecorrenciaMapper.toDto(assinaturaRecorrencia);

        restAssinaturaRecorrenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assinaturaRecorrenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSituacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        assinaturaRecorrencia.setSituacao(null);

        // Create the AssinaturaRecorrencia, which fails.
        AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO = assinaturaRecorrenciaMapper.toDto(assinaturaRecorrencia);

        restAssinaturaRecorrenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assinaturaRecorrenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAssinaturaRecorrencias() throws Exception {
        // Initialize the database
        insertedAssinaturaRecorrencia = assinaturaRecorrenciaRepository.saveAndFlush(assinaturaRecorrencia);

        // Get all the assinaturaRecorrenciaList
        restAssinaturaRecorrenciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(assinaturaRecorrencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].numeroOrdem").value(hasItem(DEFAULT_NUMERO_ORDEM)))
            .andExpect(jsonPath("$.[*].numeroReferencia").value(hasItem(DEFAULT_NUMERO_REFERENCIA)))
            .andExpect(jsonPath("$.[*].devedorEmail").value(hasItem(DEFAULT_DEVEDOR_EMAIL)))
            .andExpect(jsonPath("$.[*].devedorDocumento").value(hasItem(DEFAULT_DEVEDOR_DOCUMENTO)))
            .andExpect(jsonPath("$.[*].devedorNome").value(hasItem(DEFAULT_DEVEDOR_NOME)))
            .andExpect(jsonPath("$.[*].devedorTelefone").value(hasItem(DEFAULT_DEVEDOR_TELEFONE)))
            .andExpect(jsonPath("$.[*].devedorCep").value(hasItem(DEFAULT_DEVEDOR_CEP)))
            .andExpect(jsonPath("$.[*].devedorUf").value(hasItem(DEFAULT_DEVEDOR_UF)))
            .andExpect(jsonPath("$.[*].devedorCidade").value(hasItem(DEFAULT_DEVEDOR_CIDADE)))
            .andExpect(jsonPath("$.[*].devedorBairro").value(hasItem(DEFAULT_DEVEDOR_BAIRRO)))
            .andExpect(jsonPath("$.[*].devedorEndereco").value(hasItem(DEFAULT_DEVEDOR_ENDERECO)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)))
            .andExpect(jsonPath("$.[*].idProdutoExterno").value(hasItem(DEFAULT_ID_PRODUTO_EXTERNO)))
            .andExpect(jsonPath("$.[*].identificadorExterno").value(hasItem(DEFAULT_IDENTIFICADOR_EXTERNO)))
            .andExpect(jsonPath("$.[*].nomeProdutoExterno").value(hasItem(DEFAULT_NOME_PRODUTO_EXTERNO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].tipoPagamento").value(hasItem(DEFAULT_TIPO_PAGAMENTO.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssinaturaRecorrenciasWithEagerRelationshipsIsEnabled() throws Exception {
        when(assinaturaRecorrenciaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssinaturaRecorrenciaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(assinaturaRecorrenciaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAssinaturaRecorrenciasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(assinaturaRecorrenciaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAssinaturaRecorrenciaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(assinaturaRecorrenciaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAssinaturaRecorrencia() throws Exception {
        // Initialize the database
        insertedAssinaturaRecorrencia = assinaturaRecorrenciaRepository.saveAndFlush(assinaturaRecorrencia);

        // Get the assinaturaRecorrencia
        restAssinaturaRecorrenciaMockMvc
            .perform(get(ENTITY_API_URL_ID, assinaturaRecorrencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(assinaturaRecorrencia.getId().intValue()))
            .andExpect(jsonPath("$.numeroOrdem").value(DEFAULT_NUMERO_ORDEM))
            .andExpect(jsonPath("$.numeroReferencia").value(DEFAULT_NUMERO_REFERENCIA))
            .andExpect(jsonPath("$.devedorEmail").value(DEFAULT_DEVEDOR_EMAIL))
            .andExpect(jsonPath("$.devedorDocumento").value(DEFAULT_DEVEDOR_DOCUMENTO))
            .andExpect(jsonPath("$.devedorNome").value(DEFAULT_DEVEDOR_NOME))
            .andExpect(jsonPath("$.devedorTelefone").value(DEFAULT_DEVEDOR_TELEFONE))
            .andExpect(jsonPath("$.devedorCep").value(DEFAULT_DEVEDOR_CEP))
            .andExpect(jsonPath("$.devedorUf").value(DEFAULT_DEVEDOR_UF))
            .andExpect(jsonPath("$.devedorCidade").value(DEFAULT_DEVEDOR_CIDADE))
            .andExpect(jsonPath("$.devedorBairro").value(DEFAULT_DEVEDOR_BAIRRO))
            .andExpect(jsonPath("$.devedorEndereco").value(DEFAULT_DEVEDOR_ENDERECO))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO))
            .andExpect(jsonPath("$.idProdutoExterno").value(DEFAULT_ID_PRODUTO_EXTERNO))
            .andExpect(jsonPath("$.identificadorExterno").value(DEFAULT_IDENTIFICADOR_EXTERNO))
            .andExpect(jsonPath("$.nomeProdutoExterno").value(DEFAULT_NOME_PRODUTO_EXTERNO))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.tipoPagamento").value(DEFAULT_TIPO_PAGAMENTO.toString()))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAssinaturaRecorrencia() throws Exception {
        // Get the assinaturaRecorrencia
        restAssinaturaRecorrenciaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAssinaturaRecorrencia() throws Exception {
        // Initialize the database
        insertedAssinaturaRecorrencia = assinaturaRecorrenciaRepository.saveAndFlush(assinaturaRecorrencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assinaturaRecorrencia
        AssinaturaRecorrencia updatedAssinaturaRecorrencia = assinaturaRecorrenciaRepository
            .findById(assinaturaRecorrencia.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAssinaturaRecorrencia are not directly saved in db
        em.detach(updatedAssinaturaRecorrencia);
        updatedAssinaturaRecorrencia
            .numeroOrdem(UPDATED_NUMERO_ORDEM)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .devedorEmail(UPDATED_DEVEDOR_EMAIL)
            .devedorDocumento(UPDATED_DEVEDOR_DOCUMENTO)
            .devedorNome(UPDATED_DEVEDOR_NOME)
            .devedorTelefone(UPDATED_DEVEDOR_TELEFONE)
            .devedorCep(UPDATED_DEVEDOR_CEP)
            .devedorUf(UPDATED_DEVEDOR_UF)
            .devedorCidade(UPDATED_DEVEDOR_CIDADE)
            .devedorBairro(UPDATED_DEVEDOR_BAIRRO)
            .devedorEndereco(UPDATED_DEVEDOR_ENDERECO)
            .ativo(UPDATED_ATIVO)
            .idProdutoExterno(UPDATED_ID_PRODUTO_EXTERNO)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .nomeProdutoExterno(UPDATED_NOME_PRODUTO_EXTERNO)
            .tipo(UPDATED_TIPO)
            .tipoPagamento(UPDATED_TIPO_PAGAMENTO)
            .situacao(UPDATED_SITUACAO);
        AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO = assinaturaRecorrenciaMapper.toDto(updatedAssinaturaRecorrencia);

        restAssinaturaRecorrenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assinaturaRecorrenciaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assinaturaRecorrenciaDTO))
            )
            .andExpect(status().isOk());

        // Validate the AssinaturaRecorrencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAssinaturaRecorrenciaToMatchAllProperties(updatedAssinaturaRecorrencia);
    }

    @Test
    @Transactional
    void putNonExistingAssinaturaRecorrencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assinaturaRecorrencia.setId(longCount.incrementAndGet());

        // Create the AssinaturaRecorrencia
        AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO = assinaturaRecorrenciaMapper.toDto(assinaturaRecorrencia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssinaturaRecorrenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, assinaturaRecorrenciaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assinaturaRecorrenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssinaturaRecorrencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAssinaturaRecorrencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assinaturaRecorrencia.setId(longCount.incrementAndGet());

        // Create the AssinaturaRecorrencia
        AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO = assinaturaRecorrenciaMapper.toDto(assinaturaRecorrencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssinaturaRecorrenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(assinaturaRecorrenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssinaturaRecorrencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAssinaturaRecorrencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assinaturaRecorrencia.setId(longCount.incrementAndGet());

        // Create the AssinaturaRecorrencia
        AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO = assinaturaRecorrenciaMapper.toDto(assinaturaRecorrencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssinaturaRecorrenciaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(assinaturaRecorrenciaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssinaturaRecorrencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAssinaturaRecorrenciaWithPatch() throws Exception {
        // Initialize the database
        insertedAssinaturaRecorrencia = assinaturaRecorrenciaRepository.saveAndFlush(assinaturaRecorrencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assinaturaRecorrencia using partial update
        AssinaturaRecorrencia partialUpdatedAssinaturaRecorrencia = new AssinaturaRecorrencia();
        partialUpdatedAssinaturaRecorrencia.setId(assinaturaRecorrencia.getId());

        partialUpdatedAssinaturaRecorrencia
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .devedorEmail(UPDATED_DEVEDOR_EMAIL)
            .devedorUf(UPDATED_DEVEDOR_UF)
            .devedorEndereco(UPDATED_DEVEDOR_ENDERECO)
            .ativo(UPDATED_ATIVO)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .tipo(UPDATED_TIPO)
            .tipoPagamento(UPDATED_TIPO_PAGAMENTO);

        restAssinaturaRecorrenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssinaturaRecorrencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAssinaturaRecorrencia))
            )
            .andExpect(status().isOk());

        // Validate the AssinaturaRecorrencia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssinaturaRecorrenciaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAssinaturaRecorrencia, assinaturaRecorrencia),
            getPersistedAssinaturaRecorrencia(assinaturaRecorrencia)
        );
    }

    @Test
    @Transactional
    void fullUpdateAssinaturaRecorrenciaWithPatch() throws Exception {
        // Initialize the database
        insertedAssinaturaRecorrencia = assinaturaRecorrenciaRepository.saveAndFlush(assinaturaRecorrencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the assinaturaRecorrencia using partial update
        AssinaturaRecorrencia partialUpdatedAssinaturaRecorrencia = new AssinaturaRecorrencia();
        partialUpdatedAssinaturaRecorrencia.setId(assinaturaRecorrencia.getId());

        partialUpdatedAssinaturaRecorrencia
            .numeroOrdem(UPDATED_NUMERO_ORDEM)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .devedorEmail(UPDATED_DEVEDOR_EMAIL)
            .devedorDocumento(UPDATED_DEVEDOR_DOCUMENTO)
            .devedorNome(UPDATED_DEVEDOR_NOME)
            .devedorTelefone(UPDATED_DEVEDOR_TELEFONE)
            .devedorCep(UPDATED_DEVEDOR_CEP)
            .devedorUf(UPDATED_DEVEDOR_UF)
            .devedorCidade(UPDATED_DEVEDOR_CIDADE)
            .devedorBairro(UPDATED_DEVEDOR_BAIRRO)
            .devedorEndereco(UPDATED_DEVEDOR_ENDERECO)
            .ativo(UPDATED_ATIVO)
            .idProdutoExterno(UPDATED_ID_PRODUTO_EXTERNO)
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .nomeProdutoExterno(UPDATED_NOME_PRODUTO_EXTERNO)
            .tipo(UPDATED_TIPO)
            .tipoPagamento(UPDATED_TIPO_PAGAMENTO)
            .situacao(UPDATED_SITUACAO);

        restAssinaturaRecorrenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAssinaturaRecorrencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAssinaturaRecorrencia))
            )
            .andExpect(status().isOk());

        // Validate the AssinaturaRecorrencia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAssinaturaRecorrenciaUpdatableFieldsEquals(
            partialUpdatedAssinaturaRecorrencia,
            getPersistedAssinaturaRecorrencia(partialUpdatedAssinaturaRecorrencia)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAssinaturaRecorrencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assinaturaRecorrencia.setId(longCount.incrementAndGet());

        // Create the AssinaturaRecorrencia
        AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO = assinaturaRecorrenciaMapper.toDto(assinaturaRecorrencia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAssinaturaRecorrenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, assinaturaRecorrenciaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(assinaturaRecorrenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssinaturaRecorrencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAssinaturaRecorrencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assinaturaRecorrencia.setId(longCount.incrementAndGet());

        // Create the AssinaturaRecorrencia
        AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO = assinaturaRecorrenciaMapper.toDto(assinaturaRecorrencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssinaturaRecorrenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(assinaturaRecorrenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AssinaturaRecorrencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAssinaturaRecorrencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        assinaturaRecorrencia.setId(longCount.incrementAndGet());

        // Create the AssinaturaRecorrencia
        AssinaturaRecorrenciaDTO assinaturaRecorrenciaDTO = assinaturaRecorrenciaMapper.toDto(assinaturaRecorrencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAssinaturaRecorrenciaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(assinaturaRecorrenciaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AssinaturaRecorrencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAssinaturaRecorrencia() throws Exception {
        // Initialize the database
        insertedAssinaturaRecorrencia = assinaturaRecorrenciaRepository.saveAndFlush(assinaturaRecorrencia);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the assinaturaRecorrencia
        restAssinaturaRecorrenciaMockMvc
            .perform(delete(ENTITY_API_URL_ID, assinaturaRecorrencia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return assinaturaRecorrenciaRepository.count();
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

    protected AssinaturaRecorrencia getPersistedAssinaturaRecorrencia(AssinaturaRecorrencia assinaturaRecorrencia) {
        return assinaturaRecorrenciaRepository.findById(assinaturaRecorrencia.getId()).orElseThrow();
    }

    protected void assertPersistedAssinaturaRecorrenciaToMatchAllProperties(AssinaturaRecorrencia expectedAssinaturaRecorrencia) {
        assertAssinaturaRecorrenciaAllPropertiesEquals(
            expectedAssinaturaRecorrencia,
            getPersistedAssinaturaRecorrencia(expectedAssinaturaRecorrencia)
        );
    }

    protected void assertPersistedAssinaturaRecorrenciaToMatchUpdatableProperties(AssinaturaRecorrencia expectedAssinaturaRecorrencia) {
        assertAssinaturaRecorrenciaAllUpdatablePropertiesEquals(
            expectedAssinaturaRecorrencia,
            getPersistedAssinaturaRecorrencia(expectedAssinaturaRecorrencia)
        );
    }
}
