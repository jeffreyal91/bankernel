package com.bankernel.web.rest;

import static com.bankernel.domain.PlanoRecorrenciaAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bankernel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.PlanoRecorrencia;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumMetodoPagamentoRecorrencia;
import com.bankernel.domain.enumeration.EnumTipoPlanoRecorrencia;
import com.bankernel.repository.PlanoRecorrenciaRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.PlanoRecorrenciaService;
import com.bankernel.service.dto.PlanoRecorrenciaDTO;
import com.bankernel.service.mapper.PlanoRecorrenciaMapper;
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
 * Integration tests for the {@link PlanoRecorrenciaResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PlanoRecorrenciaResourceIT {

    private static final String DEFAULT_IDENTIFICADOR_EXTERNO = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR_EXTERNO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_REFERENCIA = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR = new BigDecimal(2);

    private static final Integer DEFAULT_DIAS_TESTE = 1;
    private static final Integer UPDATED_DIAS_TESTE = 2;

    private static final Integer DEFAULT_INTERVALO = 1;
    private static final Integer UPDATED_INTERVALO = 2;

    private static final Integer DEFAULT_PARCELAS = 1;
    private static final Integer UPDATED_PARCELAS = 2;

    private static final Integer DEFAULT_TENTATIVAS = 1;
    private static final Integer UPDATED_TENTATIVAS = 2;

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final EnumMetodoPagamentoRecorrencia DEFAULT_METODO_PAGAMENTO = EnumMetodoPagamentoRecorrencia.PIX;
    private static final EnumMetodoPagamentoRecorrencia UPDATED_METODO_PAGAMENTO = EnumMetodoPagamentoRecorrencia.BOLETO;

    private static final EnumTipoPlanoRecorrencia DEFAULT_TIPO_PLANO = EnumTipoPlanoRecorrencia.FIXO;
    private static final EnumTipoPlanoRecorrencia UPDATED_TIPO_PLANO = EnumTipoPlanoRecorrencia.VARIAVEL;

    private static final String ENTITY_API_URL = "/api/plano-recorrencias";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PlanoRecorrenciaRepository planoRecorrenciaRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private PlanoRecorrenciaRepository planoRecorrenciaRepositoryMock;

    @Autowired
    private PlanoRecorrenciaMapper planoRecorrenciaMapper;

    @Mock
    private PlanoRecorrenciaService planoRecorrenciaServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPlanoRecorrenciaMockMvc;

    private PlanoRecorrencia planoRecorrencia;

    private PlanoRecorrencia insertedPlanoRecorrencia;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanoRecorrencia createEntity(EntityManager em) {
        PlanoRecorrencia planoRecorrencia = new PlanoRecorrencia()
            .identificadorExterno(DEFAULT_IDENTIFICADOR_EXTERNO)
            .numeroReferencia(DEFAULT_NUMERO_REFERENCIA)
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .valor(DEFAULT_VALOR)
            .diasTeste(DEFAULT_DIAS_TESTE)
            .intervalo(DEFAULT_INTERVALO)
            .parcelas(DEFAULT_PARCELAS)
            .tentativas(DEFAULT_TENTATIVAS)
            .ativo(DEFAULT_ATIVO)
            .metodoPagamento(DEFAULT_METODO_PAGAMENTO)
            .tipoPlano(DEFAULT_TIPO_PLANO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        planoRecorrencia.setUsuario(user);
        return planoRecorrencia;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PlanoRecorrencia createUpdatedEntity(EntityManager em) {
        PlanoRecorrencia updatedPlanoRecorrencia = new PlanoRecorrencia()
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .diasTeste(UPDATED_DIAS_TESTE)
            .intervalo(UPDATED_INTERVALO)
            .parcelas(UPDATED_PARCELAS)
            .tentativas(UPDATED_TENTATIVAS)
            .ativo(UPDATED_ATIVO)
            .metodoPagamento(UPDATED_METODO_PAGAMENTO)
            .tipoPlano(UPDATED_TIPO_PLANO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedPlanoRecorrencia.setUsuario(user);
        return updatedPlanoRecorrencia;
    }

    @BeforeEach
    void initTest() {
        planoRecorrencia = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPlanoRecorrencia != null) {
            planoRecorrenciaRepository.delete(insertedPlanoRecorrencia);
            insertedPlanoRecorrencia = null;
        }
    }

    @Test
    @Transactional
    void createPlanoRecorrencia() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PlanoRecorrencia
        PlanoRecorrenciaDTO planoRecorrenciaDTO = planoRecorrenciaMapper.toDto(planoRecorrencia);
        var returnedPlanoRecorrenciaDTO = om.readValue(
            restPlanoRecorrenciaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoRecorrenciaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PlanoRecorrenciaDTO.class
        );

        // Validate the PlanoRecorrencia in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPlanoRecorrencia = planoRecorrenciaMapper.toEntity(returnedPlanoRecorrenciaDTO);
        assertPlanoRecorrenciaUpdatableFieldsEquals(returnedPlanoRecorrencia, getPersistedPlanoRecorrencia(returnedPlanoRecorrencia));

        insertedPlanoRecorrencia = returnedPlanoRecorrencia;
    }

    @Test
    @Transactional
    void createPlanoRecorrenciaWithExistingId() throws Exception {
        // Create the PlanoRecorrencia with an existing ID
        planoRecorrencia.setId(1L);
        PlanoRecorrenciaDTO planoRecorrenciaDTO = planoRecorrenciaMapper.toDto(planoRecorrencia);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlanoRecorrenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoRecorrenciaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PlanoRecorrencia in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        planoRecorrencia.setNome(null);

        // Create the PlanoRecorrencia, which fails.
        PlanoRecorrenciaDTO planoRecorrenciaDTO = planoRecorrenciaMapper.toDto(planoRecorrencia);

        restPlanoRecorrenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoRecorrenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        planoRecorrencia.setValor(null);

        // Create the PlanoRecorrencia, which fails.
        PlanoRecorrenciaDTO planoRecorrenciaDTO = planoRecorrenciaMapper.toDto(planoRecorrencia);

        restPlanoRecorrenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoRecorrenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIntervaloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        planoRecorrencia.setIntervalo(null);

        // Create the PlanoRecorrencia, which fails.
        PlanoRecorrenciaDTO planoRecorrenciaDTO = planoRecorrenciaMapper.toDto(planoRecorrencia);

        restPlanoRecorrenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoRecorrenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTentativasIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        planoRecorrencia.setTentativas(null);

        // Create the PlanoRecorrencia, which fails.
        PlanoRecorrenciaDTO planoRecorrenciaDTO = planoRecorrenciaMapper.toDto(planoRecorrencia);

        restPlanoRecorrenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoRecorrenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        planoRecorrencia.setAtivo(null);

        // Create the PlanoRecorrencia, which fails.
        PlanoRecorrenciaDTO planoRecorrenciaDTO = planoRecorrenciaMapper.toDto(planoRecorrencia);

        restPlanoRecorrenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoRecorrenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMetodoPagamentoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        planoRecorrencia.setMetodoPagamento(null);

        // Create the PlanoRecorrencia, which fails.
        PlanoRecorrenciaDTO planoRecorrenciaDTO = planoRecorrenciaMapper.toDto(planoRecorrencia);

        restPlanoRecorrenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoRecorrenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoPlanoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        planoRecorrencia.setTipoPlano(null);

        // Create the PlanoRecorrencia, which fails.
        PlanoRecorrenciaDTO planoRecorrenciaDTO = planoRecorrenciaMapper.toDto(planoRecorrencia);

        restPlanoRecorrenciaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoRecorrenciaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPlanoRecorrencias() throws Exception {
        // Initialize the database
        insertedPlanoRecorrencia = planoRecorrenciaRepository.saveAndFlush(planoRecorrencia);

        // Get all the planoRecorrenciaList
        restPlanoRecorrenciaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(planoRecorrencia.getId().intValue())))
            .andExpect(jsonPath("$.[*].identificadorExterno").value(hasItem(DEFAULT_IDENTIFICADOR_EXTERNO)))
            .andExpect(jsonPath("$.[*].numeroReferencia").value(hasItem(DEFAULT_NUMERO_REFERENCIA)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(sameNumber(DEFAULT_VALOR))))
            .andExpect(jsonPath("$.[*].diasTeste").value(hasItem(DEFAULT_DIAS_TESTE)))
            .andExpect(jsonPath("$.[*].intervalo").value(hasItem(DEFAULT_INTERVALO)))
            .andExpect(jsonPath("$.[*].parcelas").value(hasItem(DEFAULT_PARCELAS)))
            .andExpect(jsonPath("$.[*].tentativas").value(hasItem(DEFAULT_TENTATIVAS)))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)))
            .andExpect(jsonPath("$.[*].metodoPagamento").value(hasItem(DEFAULT_METODO_PAGAMENTO.toString())))
            .andExpect(jsonPath("$.[*].tipoPlano").value(hasItem(DEFAULT_TIPO_PLANO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlanoRecorrenciasWithEagerRelationshipsIsEnabled() throws Exception {
        when(planoRecorrenciaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlanoRecorrenciaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(planoRecorrenciaServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPlanoRecorrenciasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(planoRecorrenciaServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPlanoRecorrenciaMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(planoRecorrenciaRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPlanoRecorrencia() throws Exception {
        // Initialize the database
        insertedPlanoRecorrencia = planoRecorrenciaRepository.saveAndFlush(planoRecorrencia);

        // Get the planoRecorrencia
        restPlanoRecorrenciaMockMvc
            .perform(get(ENTITY_API_URL_ID, planoRecorrencia.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(planoRecorrencia.getId().intValue()))
            .andExpect(jsonPath("$.identificadorExterno").value(DEFAULT_IDENTIFICADOR_EXTERNO))
            .andExpect(jsonPath("$.numeroReferencia").value(DEFAULT_NUMERO_REFERENCIA))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.valor").value(sameNumber(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.diasTeste").value(DEFAULT_DIAS_TESTE))
            .andExpect(jsonPath("$.intervalo").value(DEFAULT_INTERVALO))
            .andExpect(jsonPath("$.parcelas").value(DEFAULT_PARCELAS))
            .andExpect(jsonPath("$.tentativas").value(DEFAULT_TENTATIVAS))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO))
            .andExpect(jsonPath("$.metodoPagamento").value(DEFAULT_METODO_PAGAMENTO.toString()))
            .andExpect(jsonPath("$.tipoPlano").value(DEFAULT_TIPO_PLANO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPlanoRecorrencia() throws Exception {
        // Get the planoRecorrencia
        restPlanoRecorrenciaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPlanoRecorrencia() throws Exception {
        // Initialize the database
        insertedPlanoRecorrencia = planoRecorrenciaRepository.saveAndFlush(planoRecorrencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planoRecorrencia
        PlanoRecorrencia updatedPlanoRecorrencia = planoRecorrenciaRepository.findById(planoRecorrencia.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPlanoRecorrencia are not directly saved in db
        em.detach(updatedPlanoRecorrencia);
        updatedPlanoRecorrencia
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .diasTeste(UPDATED_DIAS_TESTE)
            .intervalo(UPDATED_INTERVALO)
            .parcelas(UPDATED_PARCELAS)
            .tentativas(UPDATED_TENTATIVAS)
            .ativo(UPDATED_ATIVO)
            .metodoPagamento(UPDATED_METODO_PAGAMENTO)
            .tipoPlano(UPDATED_TIPO_PLANO);
        PlanoRecorrenciaDTO planoRecorrenciaDTO = planoRecorrenciaMapper.toDto(updatedPlanoRecorrencia);

        restPlanoRecorrenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planoRecorrenciaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planoRecorrenciaDTO))
            )
            .andExpect(status().isOk());

        // Validate the PlanoRecorrencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPlanoRecorrenciaToMatchAllProperties(updatedPlanoRecorrencia);
    }

    @Test
    @Transactional
    void putNonExistingPlanoRecorrencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planoRecorrencia.setId(longCount.incrementAndGet());

        // Create the PlanoRecorrencia
        PlanoRecorrenciaDTO planoRecorrenciaDTO = planoRecorrenciaMapper.toDto(planoRecorrencia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanoRecorrenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, planoRecorrenciaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planoRecorrenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanoRecorrencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPlanoRecorrencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planoRecorrencia.setId(longCount.incrementAndGet());

        // Create the PlanoRecorrencia
        PlanoRecorrenciaDTO planoRecorrenciaDTO = planoRecorrenciaMapper.toDto(planoRecorrencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanoRecorrenciaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(planoRecorrenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanoRecorrencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPlanoRecorrencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planoRecorrencia.setId(longCount.incrementAndGet());

        // Create the PlanoRecorrencia
        PlanoRecorrenciaDTO planoRecorrenciaDTO = planoRecorrenciaMapper.toDto(planoRecorrencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanoRecorrenciaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(planoRecorrenciaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlanoRecorrencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePlanoRecorrenciaWithPatch() throws Exception {
        // Initialize the database
        insertedPlanoRecorrencia = planoRecorrenciaRepository.saveAndFlush(planoRecorrencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planoRecorrencia using partial update
        PlanoRecorrencia partialUpdatedPlanoRecorrencia = new PlanoRecorrencia();
        partialUpdatedPlanoRecorrencia.setId(planoRecorrencia.getId());

        partialUpdatedPlanoRecorrencia
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .intervalo(UPDATED_INTERVALO)
            .metodoPagamento(UPDATED_METODO_PAGAMENTO);

        restPlanoRecorrenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanoRecorrencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlanoRecorrencia))
            )
            .andExpect(status().isOk());

        // Validate the PlanoRecorrencia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlanoRecorrenciaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPlanoRecorrencia, planoRecorrencia),
            getPersistedPlanoRecorrencia(planoRecorrencia)
        );
    }

    @Test
    @Transactional
    void fullUpdatePlanoRecorrenciaWithPatch() throws Exception {
        // Initialize the database
        insertedPlanoRecorrencia = planoRecorrenciaRepository.saveAndFlush(planoRecorrencia);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the planoRecorrencia using partial update
        PlanoRecorrencia partialUpdatedPlanoRecorrencia = new PlanoRecorrencia();
        partialUpdatedPlanoRecorrencia.setId(planoRecorrencia.getId());

        partialUpdatedPlanoRecorrencia
            .identificadorExterno(UPDATED_IDENTIFICADOR_EXTERNO)
            .numeroReferencia(UPDATED_NUMERO_REFERENCIA)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .valor(UPDATED_VALOR)
            .diasTeste(UPDATED_DIAS_TESTE)
            .intervalo(UPDATED_INTERVALO)
            .parcelas(UPDATED_PARCELAS)
            .tentativas(UPDATED_TENTATIVAS)
            .ativo(UPDATED_ATIVO)
            .metodoPagamento(UPDATED_METODO_PAGAMENTO)
            .tipoPlano(UPDATED_TIPO_PLANO);

        restPlanoRecorrenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPlanoRecorrencia.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPlanoRecorrencia))
            )
            .andExpect(status().isOk());

        // Validate the PlanoRecorrencia in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPlanoRecorrenciaUpdatableFieldsEquals(
            partialUpdatedPlanoRecorrencia,
            getPersistedPlanoRecorrencia(partialUpdatedPlanoRecorrencia)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPlanoRecorrencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planoRecorrencia.setId(longCount.incrementAndGet());

        // Create the PlanoRecorrencia
        PlanoRecorrenciaDTO planoRecorrenciaDTO = planoRecorrenciaMapper.toDto(planoRecorrencia);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlanoRecorrenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, planoRecorrenciaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planoRecorrenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanoRecorrencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPlanoRecorrencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planoRecorrencia.setId(longCount.incrementAndGet());

        // Create the PlanoRecorrencia
        PlanoRecorrenciaDTO planoRecorrenciaDTO = planoRecorrenciaMapper.toDto(planoRecorrencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanoRecorrenciaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(planoRecorrenciaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PlanoRecorrencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPlanoRecorrencia() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        planoRecorrencia.setId(longCount.incrementAndGet());

        // Create the PlanoRecorrencia
        PlanoRecorrenciaDTO planoRecorrenciaDTO = planoRecorrenciaMapper.toDto(planoRecorrencia);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPlanoRecorrenciaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(planoRecorrenciaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PlanoRecorrencia in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePlanoRecorrencia() throws Exception {
        // Initialize the database
        insertedPlanoRecorrencia = planoRecorrenciaRepository.saveAndFlush(planoRecorrencia);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the planoRecorrencia
        restPlanoRecorrenciaMockMvc
            .perform(delete(ENTITY_API_URL_ID, planoRecorrencia.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return planoRecorrenciaRepository.count();
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

    protected PlanoRecorrencia getPersistedPlanoRecorrencia(PlanoRecorrencia planoRecorrencia) {
        return planoRecorrenciaRepository.findById(planoRecorrencia.getId()).orElseThrow();
    }

    protected void assertPersistedPlanoRecorrenciaToMatchAllProperties(PlanoRecorrencia expectedPlanoRecorrencia) {
        assertPlanoRecorrenciaAllPropertiesEquals(expectedPlanoRecorrencia, getPersistedPlanoRecorrencia(expectedPlanoRecorrencia));
    }

    protected void assertPersistedPlanoRecorrenciaToMatchUpdatableProperties(PlanoRecorrencia expectedPlanoRecorrencia) {
        assertPlanoRecorrenciaAllUpdatablePropertiesEquals(
            expectedPlanoRecorrencia,
            getPersistedPlanoRecorrencia(expectedPlanoRecorrencia)
        );
    }
}
