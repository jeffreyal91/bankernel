package com.bankernel.web.rest;

import static com.bankernel.domain.MoedaCarteiraAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bankernel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Moeda;
import com.bankernel.domain.MoedaCarteira;
import com.bankernel.repository.MoedaCarteiraRepository;
import com.bankernel.service.MoedaCarteiraService;
import com.bankernel.service.dto.MoedaCarteiraDTO;
import com.bankernel.service.mapper.MoedaCarteiraMapper;
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
 * Integration tests for the {@link MoedaCarteiraResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class MoedaCarteiraResourceIT {

    private static final String DEFAULT_CODIGO = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO = "BBBBBBBBBB";

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_FATOR_CONVERSAO = new BigDecimal(1);
    private static final BigDecimal UPDATED_FATOR_CONVERSAO = new BigDecimal(2);

    private static final Boolean DEFAULT_ATIVA = false;
    private static final Boolean UPDATED_ATIVA = true;

    private static final Boolean DEFAULT_PRINCIPAL = false;
    private static final Boolean UPDATED_PRINCIPAL = true;

    private static final Integer DEFAULT_CASAS_DECIMAIS = 1;
    private static final Integer UPDATED_CASAS_DECIMAIS = 2;

    private static final String DEFAULT_SEPARADOR_MILHAR = "A";
    private static final String UPDATED_SEPARADOR_MILHAR = "B";

    private static final String DEFAULT_SEPARADOR_DECIMAL = "A";
    private static final String UPDATED_SEPARADOR_DECIMAL = "B";

    private static final String DEFAULT_SIMBOLO = "AAAAA";
    private static final String UPDATED_SIMBOLO = "BBBBB";

    private static final String ENTITY_API_URL = "/api/moeda-carteiras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MoedaCarteiraRepository moedaCarteiraRepository;

    @Mock
    private MoedaCarteiraRepository moedaCarteiraRepositoryMock;

    @Autowired
    private MoedaCarteiraMapper moedaCarteiraMapper;

    @Mock
    private MoedaCarteiraService moedaCarteiraServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMoedaCarteiraMockMvc;

    private MoedaCarteira moedaCarteira;

    private MoedaCarteira insertedMoedaCarteira;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoedaCarteira createEntity(EntityManager em) {
        MoedaCarteira moedaCarteira = new MoedaCarteira()
            .codigo(DEFAULT_CODIGO)
            .nome(DEFAULT_NOME)
            .descricao(DEFAULT_DESCRICAO)
            .fatorConversao(DEFAULT_FATOR_CONVERSAO)
            .ativa(DEFAULT_ATIVA)
            .principal(DEFAULT_PRINCIPAL)
            .casasDecimais(DEFAULT_CASAS_DECIMAIS)
            .separadorMilhar(DEFAULT_SEPARADOR_MILHAR)
            .separadorDecimal(DEFAULT_SEPARADOR_DECIMAL)
            .simbolo(DEFAULT_SIMBOLO);
        // Add required entity
        Moeda moeda;
        if (TestUtil.findAll(em, Moeda.class).isEmpty()) {
            moeda = MoedaResourceIT.createEntity();
            em.persist(moeda);
            em.flush();
        } else {
            moeda = TestUtil.findAll(em, Moeda.class).get(0);
        }
        moedaCarteira.setMoeda(moeda);
        return moedaCarteira;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MoedaCarteira createUpdatedEntity(EntityManager em) {
        MoedaCarteira updatedMoedaCarteira = new MoedaCarteira()
            .codigo(UPDATED_CODIGO)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .fatorConversao(UPDATED_FATOR_CONVERSAO)
            .ativa(UPDATED_ATIVA)
            .principal(UPDATED_PRINCIPAL)
            .casasDecimais(UPDATED_CASAS_DECIMAIS)
            .separadorMilhar(UPDATED_SEPARADOR_MILHAR)
            .separadorDecimal(UPDATED_SEPARADOR_DECIMAL)
            .simbolo(UPDATED_SIMBOLO);
        // Add required entity
        Moeda moeda;
        if (TestUtil.findAll(em, Moeda.class).isEmpty()) {
            moeda = MoedaResourceIT.createUpdatedEntity();
            em.persist(moeda);
            em.flush();
        } else {
            moeda = TestUtil.findAll(em, Moeda.class).get(0);
        }
        updatedMoedaCarteira.setMoeda(moeda);
        return updatedMoedaCarteira;
    }

    @BeforeEach
    void initTest() {
        moedaCarteira = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedMoedaCarteira != null) {
            moedaCarteiraRepository.delete(insertedMoedaCarteira);
            insertedMoedaCarteira = null;
        }
    }

    @Test
    @Transactional
    void createMoedaCarteira() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MoedaCarteira
        MoedaCarteiraDTO moedaCarteiraDTO = moedaCarteiraMapper.toDto(moedaCarteira);
        var returnedMoedaCarteiraDTO = om.readValue(
            restMoedaCarteiraMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaCarteiraDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MoedaCarteiraDTO.class
        );

        // Validate the MoedaCarteira in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMoedaCarteira = moedaCarteiraMapper.toEntity(returnedMoedaCarteiraDTO);
        assertMoedaCarteiraUpdatableFieldsEquals(returnedMoedaCarteira, getPersistedMoedaCarteira(returnedMoedaCarteira));

        insertedMoedaCarteira = returnedMoedaCarteira;
    }

    @Test
    @Transactional
    void createMoedaCarteiraWithExistingId() throws Exception {
        // Create the MoedaCarteira with an existing ID
        moedaCarteira.setId(1L);
        MoedaCarteiraDTO moedaCarteiraDTO = moedaCarteiraMapper.toDto(moedaCarteira);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMoedaCarteiraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaCarteiraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MoedaCarteira in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodigoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moedaCarteira.setCodigo(null);

        // Create the MoedaCarteira, which fails.
        MoedaCarteiraDTO moedaCarteiraDTO = moedaCarteiraMapper.toDto(moedaCarteira);

        restMoedaCarteiraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaCarteiraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moedaCarteira.setNome(null);

        // Create the MoedaCarteira, which fails.
        MoedaCarteiraDTO moedaCarteiraDTO = moedaCarteiraMapper.toDto(moedaCarteira);

        restMoedaCarteiraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaCarteiraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFatorConversaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moedaCarteira.setFatorConversao(null);

        // Create the MoedaCarteira, which fails.
        MoedaCarteiraDTO moedaCarteiraDTO = moedaCarteiraMapper.toDto(moedaCarteira);

        restMoedaCarteiraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaCarteiraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moedaCarteira.setAtiva(null);

        // Create the MoedaCarteira, which fails.
        MoedaCarteiraDTO moedaCarteiraDTO = moedaCarteiraMapper.toDto(moedaCarteira);

        restMoedaCarteiraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaCarteiraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrincipalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moedaCarteira.setPrincipal(null);

        // Create the MoedaCarteira, which fails.
        MoedaCarteiraDTO moedaCarteiraDTO = moedaCarteiraMapper.toDto(moedaCarteira);

        restMoedaCarteiraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaCarteiraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCasasDecimaisIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        moedaCarteira.setCasasDecimais(null);

        // Create the MoedaCarteira, which fails.
        MoedaCarteiraDTO moedaCarteiraDTO = moedaCarteiraMapper.toDto(moedaCarteira);

        restMoedaCarteiraMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaCarteiraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMoedaCarteiras() throws Exception {
        // Initialize the database
        insertedMoedaCarteira = moedaCarteiraRepository.saveAndFlush(moedaCarteira);

        // Get all the moedaCarteiraList
        restMoedaCarteiraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(moedaCarteira.getId().intValue())))
            .andExpect(jsonPath("$.[*].codigo").value(hasItem(DEFAULT_CODIGO)))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].fatorConversao").value(hasItem(sameNumber(DEFAULT_FATOR_CONVERSAO))))
            .andExpect(jsonPath("$.[*].ativa").value(hasItem(DEFAULT_ATIVA)))
            .andExpect(jsonPath("$.[*].principal").value(hasItem(DEFAULT_PRINCIPAL)))
            .andExpect(jsonPath("$.[*].casasDecimais").value(hasItem(DEFAULT_CASAS_DECIMAIS)))
            .andExpect(jsonPath("$.[*].separadorMilhar").value(hasItem(DEFAULT_SEPARADOR_MILHAR)))
            .andExpect(jsonPath("$.[*].separadorDecimal").value(hasItem(DEFAULT_SEPARADOR_DECIMAL)))
            .andExpect(jsonPath("$.[*].simbolo").value(hasItem(DEFAULT_SIMBOLO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoedaCarteirasWithEagerRelationshipsIsEnabled() throws Exception {
        when(moedaCarteiraServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMoedaCarteiraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(moedaCarteiraServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllMoedaCarteirasWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(moedaCarteiraServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restMoedaCarteiraMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(moedaCarteiraRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getMoedaCarteira() throws Exception {
        // Initialize the database
        insertedMoedaCarteira = moedaCarteiraRepository.saveAndFlush(moedaCarteira);

        // Get the moedaCarteira
        restMoedaCarteiraMockMvc
            .perform(get(ENTITY_API_URL_ID, moedaCarteira.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(moedaCarteira.getId().intValue()))
            .andExpect(jsonPath("$.codigo").value(DEFAULT_CODIGO))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.fatorConversao").value(sameNumber(DEFAULT_FATOR_CONVERSAO)))
            .andExpect(jsonPath("$.ativa").value(DEFAULT_ATIVA))
            .andExpect(jsonPath("$.principal").value(DEFAULT_PRINCIPAL))
            .andExpect(jsonPath("$.casasDecimais").value(DEFAULT_CASAS_DECIMAIS))
            .andExpect(jsonPath("$.separadorMilhar").value(DEFAULT_SEPARADOR_MILHAR))
            .andExpect(jsonPath("$.separadorDecimal").value(DEFAULT_SEPARADOR_DECIMAL))
            .andExpect(jsonPath("$.simbolo").value(DEFAULT_SIMBOLO));
    }

    @Test
    @Transactional
    void getNonExistingMoedaCarteira() throws Exception {
        // Get the moedaCarteira
        restMoedaCarteiraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMoedaCarteira() throws Exception {
        // Initialize the database
        insertedMoedaCarteira = moedaCarteiraRepository.saveAndFlush(moedaCarteira);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moedaCarteira
        MoedaCarteira updatedMoedaCarteira = moedaCarteiraRepository.findById(moedaCarteira.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMoedaCarteira are not directly saved in db
        em.detach(updatedMoedaCarteira);
        updatedMoedaCarteira
            .codigo(UPDATED_CODIGO)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .fatorConversao(UPDATED_FATOR_CONVERSAO)
            .ativa(UPDATED_ATIVA)
            .principal(UPDATED_PRINCIPAL)
            .casasDecimais(UPDATED_CASAS_DECIMAIS)
            .separadorMilhar(UPDATED_SEPARADOR_MILHAR)
            .separadorDecimal(UPDATED_SEPARADOR_DECIMAL)
            .simbolo(UPDATED_SIMBOLO);
        MoedaCarteiraDTO moedaCarteiraDTO = moedaCarteiraMapper.toDto(updatedMoedaCarteira);

        restMoedaCarteiraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moedaCarteiraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moedaCarteiraDTO))
            )
            .andExpect(status().isOk());

        // Validate the MoedaCarteira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMoedaCarteiraToMatchAllProperties(updatedMoedaCarteira);
    }

    @Test
    @Transactional
    void putNonExistingMoedaCarteira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moedaCarteira.setId(longCount.incrementAndGet());

        // Create the MoedaCarteira
        MoedaCarteiraDTO moedaCarteiraDTO = moedaCarteiraMapper.toDto(moedaCarteira);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoedaCarteiraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, moedaCarteiraDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moedaCarteiraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoedaCarteira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMoedaCarteira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moedaCarteira.setId(longCount.incrementAndGet());

        // Create the MoedaCarteira
        MoedaCarteiraDTO moedaCarteiraDTO = moedaCarteiraMapper.toDto(moedaCarteira);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoedaCarteiraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(moedaCarteiraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoedaCarteira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMoedaCarteira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moedaCarteira.setId(longCount.incrementAndGet());

        // Create the MoedaCarteira
        MoedaCarteiraDTO moedaCarteiraDTO = moedaCarteiraMapper.toDto(moedaCarteira);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoedaCarteiraMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(moedaCarteiraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoedaCarteira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMoedaCarteiraWithPatch() throws Exception {
        // Initialize the database
        insertedMoedaCarteira = moedaCarteiraRepository.saveAndFlush(moedaCarteira);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moedaCarteira using partial update
        MoedaCarteira partialUpdatedMoedaCarteira = new MoedaCarteira();
        partialUpdatedMoedaCarteira.setId(moedaCarteira.getId());

        partialUpdatedMoedaCarteira
            .descricao(UPDATED_DESCRICAO)
            .ativa(UPDATED_ATIVA)
            .principal(UPDATED_PRINCIPAL)
            .casasDecimais(UPDATED_CASAS_DECIMAIS)
            .simbolo(UPDATED_SIMBOLO);

        restMoedaCarteiraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoedaCarteira.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoedaCarteira))
            )
            .andExpect(status().isOk());

        // Validate the MoedaCarteira in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoedaCarteiraUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMoedaCarteira, moedaCarteira),
            getPersistedMoedaCarteira(moedaCarteira)
        );
    }

    @Test
    @Transactional
    void fullUpdateMoedaCarteiraWithPatch() throws Exception {
        // Initialize the database
        insertedMoedaCarteira = moedaCarteiraRepository.saveAndFlush(moedaCarteira);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the moedaCarteira using partial update
        MoedaCarteira partialUpdatedMoedaCarteira = new MoedaCarteira();
        partialUpdatedMoedaCarteira.setId(moedaCarteira.getId());

        partialUpdatedMoedaCarteira
            .codigo(UPDATED_CODIGO)
            .nome(UPDATED_NOME)
            .descricao(UPDATED_DESCRICAO)
            .fatorConversao(UPDATED_FATOR_CONVERSAO)
            .ativa(UPDATED_ATIVA)
            .principal(UPDATED_PRINCIPAL)
            .casasDecimais(UPDATED_CASAS_DECIMAIS)
            .separadorMilhar(UPDATED_SEPARADOR_MILHAR)
            .separadorDecimal(UPDATED_SEPARADOR_DECIMAL)
            .simbolo(UPDATED_SIMBOLO);

        restMoedaCarteiraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMoedaCarteira.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMoedaCarteira))
            )
            .andExpect(status().isOk());

        // Validate the MoedaCarteira in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMoedaCarteiraUpdatableFieldsEquals(partialUpdatedMoedaCarteira, getPersistedMoedaCarteira(partialUpdatedMoedaCarteira));
    }

    @Test
    @Transactional
    void patchNonExistingMoedaCarteira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moedaCarteira.setId(longCount.incrementAndGet());

        // Create the MoedaCarteira
        MoedaCarteiraDTO moedaCarteiraDTO = moedaCarteiraMapper.toDto(moedaCarteira);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMoedaCarteiraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, moedaCarteiraDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moedaCarteiraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoedaCarteira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMoedaCarteira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moedaCarteira.setId(longCount.incrementAndGet());

        // Create the MoedaCarteira
        MoedaCarteiraDTO moedaCarteiraDTO = moedaCarteiraMapper.toDto(moedaCarteira);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoedaCarteiraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(moedaCarteiraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MoedaCarteira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMoedaCarteira() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        moedaCarteira.setId(longCount.incrementAndGet());

        // Create the MoedaCarteira
        MoedaCarteiraDTO moedaCarteiraDTO = moedaCarteiraMapper.toDto(moedaCarteira);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMoedaCarteiraMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(moedaCarteiraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MoedaCarteira in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMoedaCarteira() throws Exception {
        // Initialize the database
        insertedMoedaCarteira = moedaCarteiraRepository.saveAndFlush(moedaCarteira);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the moedaCarteira
        restMoedaCarteiraMockMvc
            .perform(delete(ENTITY_API_URL_ID, moedaCarteira.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return moedaCarteiraRepository.count();
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

    protected MoedaCarteira getPersistedMoedaCarteira(MoedaCarteira moedaCarteira) {
        return moedaCarteiraRepository.findById(moedaCarteira.getId()).orElseThrow();
    }

    protected void assertPersistedMoedaCarteiraToMatchAllProperties(MoedaCarteira expectedMoedaCarteira) {
        assertMoedaCarteiraAllPropertiesEquals(expectedMoedaCarteira, getPersistedMoedaCarteira(expectedMoedaCarteira));
    }

    protected void assertPersistedMoedaCarteiraToMatchUpdatableProperties(MoedaCarteira expectedMoedaCarteira) {
        assertMoedaCarteiraAllUpdatablePropertiesEquals(expectedMoedaCarteira, getPersistedMoedaCarteira(expectedMoedaCarteira));
    }
}
