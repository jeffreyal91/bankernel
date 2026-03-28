package com.bankernel.web.rest;

import static com.bankernel.domain.DepositoBoletoAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bankernel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Carteira;
import com.bankernel.domain.Deposito;
import com.bankernel.domain.DepositoBoleto;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumStatusDepositoBoleto;
import com.bankernel.domain.enumeration.EnumTipoDepositoBoleto;
import com.bankernel.repository.DepositoBoletoRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.DepositoBoletoService;
import com.bankernel.service.dto.DepositoBoletoDTO;
import com.bankernel.service.mapper.DepositoBoletoMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link DepositoBoletoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepositoBoletoResourceIT {

    private static final EnumTipoDepositoBoleto DEFAULT_TIPO = EnumTipoDepositoBoleto.COBRANCA;
    private static final EnumTipoDepositoBoleto UPDATED_TIPO = EnumTipoDepositoBoleto.DEPOSITO;

    private static final EnumStatusDepositoBoleto DEFAULT_SITUACAO = EnumStatusDepositoBoleto.CRIADO;
    private static final EnumStatusDepositoBoleto UPDATED_SITUACAO = EnumStatusDepositoBoleto.EM_PROCESSO;

    private static final String DEFAULT_CODIGO_BARRAS = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_BARRAS = "BBBBBBBBBB";

    private static final String DEFAULT_LINHA_DIGITAVEL = "AAAAAAAAAA";
    private static final String UPDATED_LINHA_DIGITAVEL = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_VALOR_ORIGINAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_ORIGINAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_CREDITADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_CREDITADO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_RECEBIDO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_RECEBIDO = new BigDecimal(2);

    private static final String DEFAULT_PAGADOR_NOME = "AAAAAAAAAA";
    private static final String UPDATED_PAGADOR_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_PAGADOR_CPF = "AAAAAAAAAA";
    private static final String UPDATED_PAGADOR_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_PAGADOR_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_PAGADOR_CNPJ = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA_VENCIMENTO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA_VENCIMENTO = LocalDate.now(ZoneId.systemDefault());

    private static final Instant DEFAULT_DATA_RECEBIMENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_RECEBIMENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_CONTABILIZADO = false;
    private static final Boolean UPDATED_CONTABILIZADO = true;

    private static final String ENTITY_API_URL = "/api/deposito-boletos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DepositoBoletoRepository depositoBoletoRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private DepositoBoletoRepository depositoBoletoRepositoryMock;

    @Autowired
    private DepositoBoletoMapper depositoBoletoMapper;

    @Mock
    private DepositoBoletoService depositoBoletoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepositoBoletoMockMvc;

    private DepositoBoleto depositoBoleto;

    private DepositoBoleto insertedDepositoBoleto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepositoBoleto createEntity(EntityManager em) {
        DepositoBoleto depositoBoleto = new DepositoBoleto()
            .tipo(DEFAULT_TIPO)
            .situacao(DEFAULT_SITUACAO)
            .codigoBarras(DEFAULT_CODIGO_BARRAS)
            .linhaDigitavel(DEFAULT_LINHA_DIGITAVEL)
            .valorOriginal(DEFAULT_VALOR_ORIGINAL)
            .valorCreditado(DEFAULT_VALOR_CREDITADO)
            .valorRecebido(DEFAULT_VALOR_RECEBIDO)
            .pagadorNome(DEFAULT_PAGADOR_NOME)
            .pagadorCpf(DEFAULT_PAGADOR_CPF)
            .pagadorCnpj(DEFAULT_PAGADOR_CNPJ)
            .dataVencimento(DEFAULT_DATA_VENCIMENTO)
            .dataRecebimento(DEFAULT_DATA_RECEBIMENTO)
            .contabilizado(DEFAULT_CONTABILIZADO);
        // Add required entity
        Deposito deposito;
        if (TestUtil.findAll(em, Deposito.class).isEmpty()) {
            deposito = DepositoResourceIT.createEntity(em);
            em.persist(deposito);
            em.flush();
        } else {
            deposito = TestUtil.findAll(em, Deposito.class).get(0);
        }
        depositoBoleto.setDeposito(deposito);
        // Add required entity
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            carteira = CarteiraResourceIT.createEntity(em);
            em.persist(carteira);
            em.flush();
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        depositoBoleto.setCarteira(carteira);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        depositoBoleto.setUsuario(user);
        return depositoBoleto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepositoBoleto createUpdatedEntity(EntityManager em) {
        DepositoBoleto updatedDepositoBoleto = new DepositoBoleto()
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .codigoBarras(UPDATED_CODIGO_BARRAS)
            .linhaDigitavel(UPDATED_LINHA_DIGITAVEL)
            .valorOriginal(UPDATED_VALOR_ORIGINAL)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO)
            .pagadorNome(UPDATED_PAGADOR_NOME)
            .pagadorCpf(UPDATED_PAGADOR_CPF)
            .pagadorCnpj(UPDATED_PAGADOR_CNPJ)
            .dataVencimento(UPDATED_DATA_VENCIMENTO)
            .dataRecebimento(UPDATED_DATA_RECEBIMENTO)
            .contabilizado(UPDATED_CONTABILIZADO);
        // Add required entity
        Deposito deposito;
        if (TestUtil.findAll(em, Deposito.class).isEmpty()) {
            deposito = DepositoResourceIT.createUpdatedEntity(em);
            em.persist(deposito);
            em.flush();
        } else {
            deposito = TestUtil.findAll(em, Deposito.class).get(0);
        }
        updatedDepositoBoleto.setDeposito(deposito);
        // Add required entity
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            carteira = CarteiraResourceIT.createUpdatedEntity(em);
            em.persist(carteira);
            em.flush();
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        updatedDepositoBoleto.setCarteira(carteira);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedDepositoBoleto.setUsuario(user);
        return updatedDepositoBoleto;
    }

    @BeforeEach
    void initTest() {
        depositoBoleto = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDepositoBoleto != null) {
            depositoBoletoRepository.delete(insertedDepositoBoleto);
            insertedDepositoBoleto = null;
        }
    }

    @Test
    @Transactional
    void createDepositoBoleto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DepositoBoleto
        DepositoBoletoDTO depositoBoletoDTO = depositoBoletoMapper.toDto(depositoBoleto);
        var returnedDepositoBoletoDTO = om.readValue(
            restDepositoBoletoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoBoletoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DepositoBoletoDTO.class
        );

        // Validate the DepositoBoleto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDepositoBoleto = depositoBoletoMapper.toEntity(returnedDepositoBoletoDTO);
        assertDepositoBoletoUpdatableFieldsEquals(returnedDepositoBoleto, getPersistedDepositoBoleto(returnedDepositoBoleto));

        insertedDepositoBoleto = returnedDepositoBoleto;
    }

    @Test
    @Transactional
    void createDepositoBoletoWithExistingId() throws Exception {
        // Create the DepositoBoleto with an existing ID
        depositoBoleto.setId(1L);
        DepositoBoletoDTO depositoBoletoDTO = depositoBoletoMapper.toDto(depositoBoleto);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepositoBoletoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoBoletoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DepositoBoleto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        depositoBoleto.setTipo(null);

        // Create the DepositoBoleto, which fails.
        DepositoBoletoDTO depositoBoletoDTO = depositoBoletoMapper.toDto(depositoBoleto);

        restDepositoBoletoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoBoletoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSituacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        depositoBoleto.setSituacao(null);

        // Create the DepositoBoleto, which fails.
        DepositoBoletoDTO depositoBoletoDTO = depositoBoletoMapper.toDto(depositoBoleto);

        restDepositoBoletoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoBoletoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorOriginalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        depositoBoleto.setValorOriginal(null);

        // Create the DepositoBoleto, which fails.
        DepositoBoletoDTO depositoBoletoDTO = depositoBoletoMapper.toDto(depositoBoleto);

        restDepositoBoletoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoBoletoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContabilizadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        depositoBoleto.setContabilizado(null);

        // Create the DepositoBoleto, which fails.
        DepositoBoletoDTO depositoBoletoDTO = depositoBoletoMapper.toDto(depositoBoleto);

        restDepositoBoletoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoBoletoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepositoBoletos() throws Exception {
        // Initialize the database
        insertedDepositoBoleto = depositoBoletoRepository.saveAndFlush(depositoBoleto);

        // Get all the depositoBoletoList
        restDepositoBoletoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depositoBoleto.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].codigoBarras").value(hasItem(DEFAULT_CODIGO_BARRAS)))
            .andExpect(jsonPath("$.[*].linhaDigitavel").value(hasItem(DEFAULT_LINHA_DIGITAVEL)))
            .andExpect(jsonPath("$.[*].valorOriginal").value(hasItem(sameNumber(DEFAULT_VALOR_ORIGINAL))))
            .andExpect(jsonPath("$.[*].valorCreditado").value(hasItem(sameNumber(DEFAULT_VALOR_CREDITADO))))
            .andExpect(jsonPath("$.[*].valorRecebido").value(hasItem(sameNumber(DEFAULT_VALOR_RECEBIDO))))
            .andExpect(jsonPath("$.[*].pagadorNome").value(hasItem(DEFAULT_PAGADOR_NOME)))
            .andExpect(jsonPath("$.[*].pagadorCpf").value(hasItem(DEFAULT_PAGADOR_CPF)))
            .andExpect(jsonPath("$.[*].pagadorCnpj").value(hasItem(DEFAULT_PAGADOR_CNPJ)))
            .andExpect(jsonPath("$.[*].dataVencimento").value(hasItem(DEFAULT_DATA_VENCIMENTO.toString())))
            .andExpect(jsonPath("$.[*].dataRecebimento").value(hasItem(DEFAULT_DATA_RECEBIMENTO.toString())))
            .andExpect(jsonPath("$.[*].contabilizado").value(hasItem(DEFAULT_CONTABILIZADO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepositoBoletosWithEagerRelationshipsIsEnabled() throws Exception {
        when(depositoBoletoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepositoBoletoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(depositoBoletoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepositoBoletosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(depositoBoletoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepositoBoletoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(depositoBoletoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDepositoBoleto() throws Exception {
        // Initialize the database
        insertedDepositoBoleto = depositoBoletoRepository.saveAndFlush(depositoBoleto);

        // Get the depositoBoleto
        restDepositoBoletoMockMvc
            .perform(get(ENTITY_API_URL_ID, depositoBoleto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depositoBoleto.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()))
            .andExpect(jsonPath("$.codigoBarras").value(DEFAULT_CODIGO_BARRAS))
            .andExpect(jsonPath("$.linhaDigitavel").value(DEFAULT_LINHA_DIGITAVEL))
            .andExpect(jsonPath("$.valorOriginal").value(sameNumber(DEFAULT_VALOR_ORIGINAL)))
            .andExpect(jsonPath("$.valorCreditado").value(sameNumber(DEFAULT_VALOR_CREDITADO)))
            .andExpect(jsonPath("$.valorRecebido").value(sameNumber(DEFAULT_VALOR_RECEBIDO)))
            .andExpect(jsonPath("$.pagadorNome").value(DEFAULT_PAGADOR_NOME))
            .andExpect(jsonPath("$.pagadorCpf").value(DEFAULT_PAGADOR_CPF))
            .andExpect(jsonPath("$.pagadorCnpj").value(DEFAULT_PAGADOR_CNPJ))
            .andExpect(jsonPath("$.dataVencimento").value(DEFAULT_DATA_VENCIMENTO.toString()))
            .andExpect(jsonPath("$.dataRecebimento").value(DEFAULT_DATA_RECEBIMENTO.toString()))
            .andExpect(jsonPath("$.contabilizado").value(DEFAULT_CONTABILIZADO));
    }

    @Test
    @Transactional
    void getNonExistingDepositoBoleto() throws Exception {
        // Get the depositoBoleto
        restDepositoBoletoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepositoBoleto() throws Exception {
        // Initialize the database
        insertedDepositoBoleto = depositoBoletoRepository.saveAndFlush(depositoBoleto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the depositoBoleto
        DepositoBoleto updatedDepositoBoleto = depositoBoletoRepository.findById(depositoBoleto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDepositoBoleto are not directly saved in db
        em.detach(updatedDepositoBoleto);
        updatedDepositoBoleto
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .codigoBarras(UPDATED_CODIGO_BARRAS)
            .linhaDigitavel(UPDATED_LINHA_DIGITAVEL)
            .valorOriginal(UPDATED_VALOR_ORIGINAL)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO)
            .pagadorNome(UPDATED_PAGADOR_NOME)
            .pagadorCpf(UPDATED_PAGADOR_CPF)
            .pagadorCnpj(UPDATED_PAGADOR_CNPJ)
            .dataVencimento(UPDATED_DATA_VENCIMENTO)
            .dataRecebimento(UPDATED_DATA_RECEBIMENTO)
            .contabilizado(UPDATED_CONTABILIZADO);
        DepositoBoletoDTO depositoBoletoDTO = depositoBoletoMapper.toDto(updatedDepositoBoleto);

        restDepositoBoletoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depositoBoletoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(depositoBoletoDTO))
            )
            .andExpect(status().isOk());

        // Validate the DepositoBoleto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDepositoBoletoToMatchAllProperties(updatedDepositoBoleto);
    }

    @Test
    @Transactional
    void putNonExistingDepositoBoleto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        depositoBoleto.setId(longCount.incrementAndGet());

        // Create the DepositoBoleto
        DepositoBoletoDTO depositoBoletoDTO = depositoBoletoMapper.toDto(depositoBoleto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepositoBoletoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depositoBoletoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(depositoBoletoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepositoBoleto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepositoBoleto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        depositoBoleto.setId(longCount.incrementAndGet());

        // Create the DepositoBoleto
        DepositoBoletoDTO depositoBoletoDTO = depositoBoletoMapper.toDto(depositoBoleto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositoBoletoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(depositoBoletoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepositoBoleto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepositoBoleto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        depositoBoleto.setId(longCount.incrementAndGet());

        // Create the DepositoBoleto
        DepositoBoletoDTO depositoBoletoDTO = depositoBoletoMapper.toDto(depositoBoleto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositoBoletoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoBoletoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepositoBoleto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepositoBoletoWithPatch() throws Exception {
        // Initialize the database
        insertedDepositoBoleto = depositoBoletoRepository.saveAndFlush(depositoBoleto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the depositoBoleto using partial update
        DepositoBoleto partialUpdatedDepositoBoleto = new DepositoBoleto();
        partialUpdatedDepositoBoleto.setId(depositoBoleto.getId());

        partialUpdatedDepositoBoleto
            .tipo(UPDATED_TIPO)
            .codigoBarras(UPDATED_CODIGO_BARRAS)
            .valorRecebido(UPDATED_VALOR_RECEBIDO)
            .pagadorNome(UPDATED_PAGADOR_NOME)
            .pagadorCnpj(UPDATED_PAGADOR_CNPJ)
            .dataRecebimento(UPDATED_DATA_RECEBIMENTO)
            .contabilizado(UPDATED_CONTABILIZADO);

        restDepositoBoletoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepositoBoleto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepositoBoleto))
            )
            .andExpect(status().isOk());

        // Validate the DepositoBoleto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepositoBoletoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDepositoBoleto, depositoBoleto),
            getPersistedDepositoBoleto(depositoBoleto)
        );
    }

    @Test
    @Transactional
    void fullUpdateDepositoBoletoWithPatch() throws Exception {
        // Initialize the database
        insertedDepositoBoleto = depositoBoletoRepository.saveAndFlush(depositoBoleto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the depositoBoleto using partial update
        DepositoBoleto partialUpdatedDepositoBoleto = new DepositoBoleto();
        partialUpdatedDepositoBoleto.setId(depositoBoleto.getId());

        partialUpdatedDepositoBoleto
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .codigoBarras(UPDATED_CODIGO_BARRAS)
            .linhaDigitavel(UPDATED_LINHA_DIGITAVEL)
            .valorOriginal(UPDATED_VALOR_ORIGINAL)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO)
            .pagadorNome(UPDATED_PAGADOR_NOME)
            .pagadorCpf(UPDATED_PAGADOR_CPF)
            .pagadorCnpj(UPDATED_PAGADOR_CNPJ)
            .dataVencimento(UPDATED_DATA_VENCIMENTO)
            .dataRecebimento(UPDATED_DATA_RECEBIMENTO)
            .contabilizado(UPDATED_CONTABILIZADO);

        restDepositoBoletoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepositoBoleto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepositoBoleto))
            )
            .andExpect(status().isOk());

        // Validate the DepositoBoleto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepositoBoletoUpdatableFieldsEquals(partialUpdatedDepositoBoleto, getPersistedDepositoBoleto(partialUpdatedDepositoBoleto));
    }

    @Test
    @Transactional
    void patchNonExistingDepositoBoleto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        depositoBoleto.setId(longCount.incrementAndGet());

        // Create the DepositoBoleto
        DepositoBoletoDTO depositoBoletoDTO = depositoBoletoMapper.toDto(depositoBoleto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepositoBoletoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depositoBoletoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(depositoBoletoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepositoBoleto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepositoBoleto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        depositoBoleto.setId(longCount.incrementAndGet());

        // Create the DepositoBoleto
        DepositoBoletoDTO depositoBoletoDTO = depositoBoletoMapper.toDto(depositoBoleto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositoBoletoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(depositoBoletoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepositoBoleto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepositoBoleto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        depositoBoleto.setId(longCount.incrementAndGet());

        // Create the DepositoBoleto
        DepositoBoletoDTO depositoBoletoDTO = depositoBoletoMapper.toDto(depositoBoleto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositoBoletoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(depositoBoletoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepositoBoleto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepositoBoleto() throws Exception {
        // Initialize the database
        insertedDepositoBoleto = depositoBoletoRepository.saveAndFlush(depositoBoleto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the depositoBoleto
        restDepositoBoletoMockMvc
            .perform(delete(ENTITY_API_URL_ID, depositoBoleto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return depositoBoletoRepository.count();
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

    protected DepositoBoleto getPersistedDepositoBoleto(DepositoBoleto depositoBoleto) {
        return depositoBoletoRepository.findById(depositoBoleto.getId()).orElseThrow();
    }

    protected void assertPersistedDepositoBoletoToMatchAllProperties(DepositoBoleto expectedDepositoBoleto) {
        assertDepositoBoletoAllPropertiesEquals(expectedDepositoBoleto, getPersistedDepositoBoleto(expectedDepositoBoleto));
    }

    protected void assertPersistedDepositoBoletoToMatchUpdatableProperties(DepositoBoleto expectedDepositoBoleto) {
        assertDepositoBoletoAllUpdatablePropertiesEquals(expectedDepositoBoleto, getPersistedDepositoBoleto(expectedDepositoBoleto));
    }
}
