package com.bankernel.web.rest;

import static com.bankernel.domain.DepositoPixAsserts.*;
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
import com.bankernel.domain.DepositoPix;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumStatusDepositoPix;
import com.bankernel.domain.enumeration.EnumTipoDepositoPix;
import com.bankernel.repository.DepositoPixRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.DepositoPixService;
import com.bankernel.service.dto.DepositoPixDTO;
import com.bankernel.service.mapper.DepositoPixMapper;
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
 * Integration tests for the {@link DepositoPixResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class DepositoPixResourceIT {

    private static final EnumTipoDepositoPix DEFAULT_TIPO = EnumTipoDepositoPix.QR_ESTATICO;
    private static final EnumTipoDepositoPix UPDATED_TIPO = EnumTipoDepositoPix.QR_DINAMICO;

    private static final EnumStatusDepositoPix DEFAULT_SITUACAO = EnumStatusDepositoPix.EM_PROCESSO;
    private static final EnumStatusDepositoPix UPDATED_SITUACAO = EnumStatusDepositoPix.PENDENTE;

    private static final BigDecimal DEFAULT_VALOR_ORIGINAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_ORIGINAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_CREDITADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_CREDITADO = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_RECEBIDO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_RECEBIDO = new BigDecimal(2);

    private static final String DEFAULT_CODIGO_QR = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_QR = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICADOR_TRANSACAO = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR_TRANSACAO = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICADOR_PONTA_A_PONTA = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR_PONTA_A_PONTA = "BBBBBBBBBB";

    private static final String DEFAULT_PAGADOR_NOME = "AAAAAAAAAA";
    private static final String UPDATED_PAGADOR_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_PAGADOR_CPF = "AAAAAAAAAA";
    private static final String UPDATED_PAGADOR_CPF = "BBBBBBBBBB";

    private static final String DEFAULT_PAGADOR_CNPJ = "AAAAAAAAAA";
    private static final String UPDATED_PAGADOR_CNPJ = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATA_RECEBIMENTO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATA_RECEBIMENTO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_CONTABILIZADO = false;
    private static final Boolean UPDATED_CONTABILIZADO = true;

    private static final String ENTITY_API_URL = "/api/deposito-pixes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DepositoPixRepository depositoPixRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private DepositoPixRepository depositoPixRepositoryMock;

    @Autowired
    private DepositoPixMapper depositoPixMapper;

    @Mock
    private DepositoPixService depositoPixServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDepositoPixMockMvc;

    private DepositoPix depositoPix;

    private DepositoPix insertedDepositoPix;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepositoPix createEntity(EntityManager em) {
        DepositoPix depositoPix = new DepositoPix()
            .tipo(DEFAULT_TIPO)
            .situacao(DEFAULT_SITUACAO)
            .valorOriginal(DEFAULT_VALOR_ORIGINAL)
            .valorCreditado(DEFAULT_VALOR_CREDITADO)
            .valorRecebido(DEFAULT_VALOR_RECEBIDO)
            .codigoQr(DEFAULT_CODIGO_QR)
            .identificadorTransacao(DEFAULT_IDENTIFICADOR_TRANSACAO)
            .identificadorPontaAPonta(DEFAULT_IDENTIFICADOR_PONTA_A_PONTA)
            .pagadorNome(DEFAULT_PAGADOR_NOME)
            .pagadorCpf(DEFAULT_PAGADOR_CPF)
            .pagadorCnpj(DEFAULT_PAGADOR_CNPJ)
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
        depositoPix.setDeposito(deposito);
        // Add required entity
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            carteira = CarteiraResourceIT.createEntity(em);
            em.persist(carteira);
            em.flush();
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        depositoPix.setCarteira(carteira);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        depositoPix.setUsuario(user);
        return depositoPix;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DepositoPix createUpdatedEntity(EntityManager em) {
        DepositoPix updatedDepositoPix = new DepositoPix()
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .valorOriginal(UPDATED_VALOR_ORIGINAL)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO)
            .codigoQr(UPDATED_CODIGO_QR)
            .identificadorTransacao(UPDATED_IDENTIFICADOR_TRANSACAO)
            .identificadorPontaAPonta(UPDATED_IDENTIFICADOR_PONTA_A_PONTA)
            .pagadorNome(UPDATED_PAGADOR_NOME)
            .pagadorCpf(UPDATED_PAGADOR_CPF)
            .pagadorCnpj(UPDATED_PAGADOR_CNPJ)
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
        updatedDepositoPix.setDeposito(deposito);
        // Add required entity
        Carteira carteira;
        if (TestUtil.findAll(em, Carteira.class).isEmpty()) {
            carteira = CarteiraResourceIT.createUpdatedEntity(em);
            em.persist(carteira);
            em.flush();
        } else {
            carteira = TestUtil.findAll(em, Carteira.class).get(0);
        }
        updatedDepositoPix.setCarteira(carteira);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedDepositoPix.setUsuario(user);
        return updatedDepositoPix;
    }

    @BeforeEach
    void initTest() {
        depositoPix = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDepositoPix != null) {
            depositoPixRepository.delete(insertedDepositoPix);
            insertedDepositoPix = null;
        }
    }

    @Test
    @Transactional
    void createDepositoPix() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DepositoPix
        DepositoPixDTO depositoPixDTO = depositoPixMapper.toDto(depositoPix);
        var returnedDepositoPixDTO = om.readValue(
            restDepositoPixMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoPixDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DepositoPixDTO.class
        );

        // Validate the DepositoPix in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDepositoPix = depositoPixMapper.toEntity(returnedDepositoPixDTO);
        assertDepositoPixUpdatableFieldsEquals(returnedDepositoPix, getPersistedDepositoPix(returnedDepositoPix));

        insertedDepositoPix = returnedDepositoPix;
    }

    @Test
    @Transactional
    void createDepositoPixWithExistingId() throws Exception {
        // Create the DepositoPix with an existing ID
        depositoPix.setId(1L);
        DepositoPixDTO depositoPixDTO = depositoPixMapper.toDto(depositoPix);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepositoPixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoPixDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DepositoPix in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        depositoPix.setTipo(null);

        // Create the DepositoPix, which fails.
        DepositoPixDTO depositoPixDTO = depositoPixMapper.toDto(depositoPix);

        restDepositoPixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoPixDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSituacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        depositoPix.setSituacao(null);

        // Create the DepositoPix, which fails.
        DepositoPixDTO depositoPixDTO = depositoPixMapper.toDto(depositoPix);

        restDepositoPixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoPixDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorOriginalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        depositoPix.setValorOriginal(null);

        // Create the DepositoPix, which fails.
        DepositoPixDTO depositoPixDTO = depositoPixMapper.toDto(depositoPix);

        restDepositoPixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoPixDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkContabilizadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        depositoPix.setContabilizado(null);

        // Create the DepositoPix, which fails.
        DepositoPixDTO depositoPixDTO = depositoPixMapper.toDto(depositoPix);

        restDepositoPixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoPixDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDepositoPixes() throws Exception {
        // Initialize the database
        insertedDepositoPix = depositoPixRepository.saveAndFlush(depositoPix);

        // Get all the depositoPixList
        restDepositoPixMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(depositoPix.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].valorOriginal").value(hasItem(sameNumber(DEFAULT_VALOR_ORIGINAL))))
            .andExpect(jsonPath("$.[*].valorCreditado").value(hasItem(sameNumber(DEFAULT_VALOR_CREDITADO))))
            .andExpect(jsonPath("$.[*].valorRecebido").value(hasItem(sameNumber(DEFAULT_VALOR_RECEBIDO))))
            .andExpect(jsonPath("$.[*].codigoQr").value(hasItem(DEFAULT_CODIGO_QR)))
            .andExpect(jsonPath("$.[*].identificadorTransacao").value(hasItem(DEFAULT_IDENTIFICADOR_TRANSACAO)))
            .andExpect(jsonPath("$.[*].identificadorPontaAPonta").value(hasItem(DEFAULT_IDENTIFICADOR_PONTA_A_PONTA)))
            .andExpect(jsonPath("$.[*].pagadorNome").value(hasItem(DEFAULT_PAGADOR_NOME)))
            .andExpect(jsonPath("$.[*].pagadorCpf").value(hasItem(DEFAULT_PAGADOR_CPF)))
            .andExpect(jsonPath("$.[*].pagadorCnpj").value(hasItem(DEFAULT_PAGADOR_CNPJ)))
            .andExpect(jsonPath("$.[*].dataRecebimento").value(hasItem(DEFAULT_DATA_RECEBIMENTO.toString())))
            .andExpect(jsonPath("$.[*].contabilizado").value(hasItem(DEFAULT_CONTABILIZADO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepositoPixesWithEagerRelationshipsIsEnabled() throws Exception {
        when(depositoPixServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepositoPixMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(depositoPixServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllDepositoPixesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(depositoPixServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDepositoPixMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(depositoPixRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getDepositoPix() throws Exception {
        // Initialize the database
        insertedDepositoPix = depositoPixRepository.saveAndFlush(depositoPix);

        // Get the depositoPix
        restDepositoPixMockMvc
            .perform(get(ENTITY_API_URL_ID, depositoPix.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(depositoPix.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()))
            .andExpect(jsonPath("$.valorOriginal").value(sameNumber(DEFAULT_VALOR_ORIGINAL)))
            .andExpect(jsonPath("$.valorCreditado").value(sameNumber(DEFAULT_VALOR_CREDITADO)))
            .andExpect(jsonPath("$.valorRecebido").value(sameNumber(DEFAULT_VALOR_RECEBIDO)))
            .andExpect(jsonPath("$.codigoQr").value(DEFAULT_CODIGO_QR))
            .andExpect(jsonPath("$.identificadorTransacao").value(DEFAULT_IDENTIFICADOR_TRANSACAO))
            .andExpect(jsonPath("$.identificadorPontaAPonta").value(DEFAULT_IDENTIFICADOR_PONTA_A_PONTA))
            .andExpect(jsonPath("$.pagadorNome").value(DEFAULT_PAGADOR_NOME))
            .andExpect(jsonPath("$.pagadorCpf").value(DEFAULT_PAGADOR_CPF))
            .andExpect(jsonPath("$.pagadorCnpj").value(DEFAULT_PAGADOR_CNPJ))
            .andExpect(jsonPath("$.dataRecebimento").value(DEFAULT_DATA_RECEBIMENTO.toString()))
            .andExpect(jsonPath("$.contabilizado").value(DEFAULT_CONTABILIZADO));
    }

    @Test
    @Transactional
    void getNonExistingDepositoPix() throws Exception {
        // Get the depositoPix
        restDepositoPixMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDepositoPix() throws Exception {
        // Initialize the database
        insertedDepositoPix = depositoPixRepository.saveAndFlush(depositoPix);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the depositoPix
        DepositoPix updatedDepositoPix = depositoPixRepository.findById(depositoPix.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDepositoPix are not directly saved in db
        em.detach(updatedDepositoPix);
        updatedDepositoPix
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .valorOriginal(UPDATED_VALOR_ORIGINAL)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO)
            .codigoQr(UPDATED_CODIGO_QR)
            .identificadorTransacao(UPDATED_IDENTIFICADOR_TRANSACAO)
            .identificadorPontaAPonta(UPDATED_IDENTIFICADOR_PONTA_A_PONTA)
            .pagadorNome(UPDATED_PAGADOR_NOME)
            .pagadorCpf(UPDATED_PAGADOR_CPF)
            .pagadorCnpj(UPDATED_PAGADOR_CNPJ)
            .dataRecebimento(UPDATED_DATA_RECEBIMENTO)
            .contabilizado(UPDATED_CONTABILIZADO);
        DepositoPixDTO depositoPixDTO = depositoPixMapper.toDto(updatedDepositoPix);

        restDepositoPixMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depositoPixDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(depositoPixDTO))
            )
            .andExpect(status().isOk());

        // Validate the DepositoPix in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDepositoPixToMatchAllProperties(updatedDepositoPix);
    }

    @Test
    @Transactional
    void putNonExistingDepositoPix() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        depositoPix.setId(longCount.incrementAndGet());

        // Create the DepositoPix
        DepositoPixDTO depositoPixDTO = depositoPixMapper.toDto(depositoPix);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepositoPixMockMvc
            .perform(
                put(ENTITY_API_URL_ID, depositoPixDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(depositoPixDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepositoPix in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDepositoPix() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        depositoPix.setId(longCount.incrementAndGet());

        // Create the DepositoPix
        DepositoPixDTO depositoPixDTO = depositoPixMapper.toDto(depositoPix);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositoPixMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(depositoPixDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepositoPix in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDepositoPix() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        depositoPix.setId(longCount.incrementAndGet());

        // Create the DepositoPix
        DepositoPixDTO depositoPixDTO = depositoPixMapper.toDto(depositoPix);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositoPixMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(depositoPixDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepositoPix in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDepositoPixWithPatch() throws Exception {
        // Initialize the database
        insertedDepositoPix = depositoPixRepository.saveAndFlush(depositoPix);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the depositoPix using partial update
        DepositoPix partialUpdatedDepositoPix = new DepositoPix();
        partialUpdatedDepositoPix.setId(depositoPix.getId());

        partialUpdatedDepositoPix
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO)
            .codigoQr(UPDATED_CODIGO_QR)
            .pagadorNome(UPDATED_PAGADOR_NOME);

        restDepositoPixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepositoPix.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepositoPix))
            )
            .andExpect(status().isOk());

        // Validate the DepositoPix in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepositoPixUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDepositoPix, depositoPix),
            getPersistedDepositoPix(depositoPix)
        );
    }

    @Test
    @Transactional
    void fullUpdateDepositoPixWithPatch() throws Exception {
        // Initialize the database
        insertedDepositoPix = depositoPixRepository.saveAndFlush(depositoPix);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the depositoPix using partial update
        DepositoPix partialUpdatedDepositoPix = new DepositoPix();
        partialUpdatedDepositoPix.setId(depositoPix.getId());

        partialUpdatedDepositoPix
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .valorOriginal(UPDATED_VALOR_ORIGINAL)
            .valorCreditado(UPDATED_VALOR_CREDITADO)
            .valorRecebido(UPDATED_VALOR_RECEBIDO)
            .codigoQr(UPDATED_CODIGO_QR)
            .identificadorTransacao(UPDATED_IDENTIFICADOR_TRANSACAO)
            .identificadorPontaAPonta(UPDATED_IDENTIFICADOR_PONTA_A_PONTA)
            .pagadorNome(UPDATED_PAGADOR_NOME)
            .pagadorCpf(UPDATED_PAGADOR_CPF)
            .pagadorCnpj(UPDATED_PAGADOR_CNPJ)
            .dataRecebimento(UPDATED_DATA_RECEBIMENTO)
            .contabilizado(UPDATED_CONTABILIZADO);

        restDepositoPixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDepositoPix.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDepositoPix))
            )
            .andExpect(status().isOk());

        // Validate the DepositoPix in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDepositoPixUpdatableFieldsEquals(partialUpdatedDepositoPix, getPersistedDepositoPix(partialUpdatedDepositoPix));
    }

    @Test
    @Transactional
    void patchNonExistingDepositoPix() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        depositoPix.setId(longCount.incrementAndGet());

        // Create the DepositoPix
        DepositoPixDTO depositoPixDTO = depositoPixMapper.toDto(depositoPix);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepositoPixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, depositoPixDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(depositoPixDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepositoPix in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDepositoPix() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        depositoPix.setId(longCount.incrementAndGet());

        // Create the DepositoPix
        DepositoPixDTO depositoPixDTO = depositoPixMapper.toDto(depositoPix);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositoPixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(depositoPixDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DepositoPix in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDepositoPix() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        depositoPix.setId(longCount.incrementAndGet());

        // Create the DepositoPix
        DepositoPixDTO depositoPixDTO = depositoPixMapper.toDto(depositoPix);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDepositoPixMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(depositoPixDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DepositoPix in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDepositoPix() throws Exception {
        // Initialize the database
        insertedDepositoPix = depositoPixRepository.saveAndFlush(depositoPix);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the depositoPix
        restDepositoPixMockMvc
            .perform(delete(ENTITY_API_URL_ID, depositoPix.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return depositoPixRepository.count();
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

    protected DepositoPix getPersistedDepositoPix(DepositoPix depositoPix) {
        return depositoPixRepository.findById(depositoPix.getId()).orElseThrow();
    }

    protected void assertPersistedDepositoPixToMatchAllProperties(DepositoPix expectedDepositoPix) {
        assertDepositoPixAllPropertiesEquals(expectedDepositoPix, getPersistedDepositoPix(expectedDepositoPix));
    }

    protected void assertPersistedDepositoPixToMatchUpdatableProperties(DepositoPix expectedDepositoPix) {
        assertDepositoPixAllUpdatablePropertiesEquals(expectedDepositoPix, getPersistedDepositoPix(expectedDepositoPix));
    }
}
