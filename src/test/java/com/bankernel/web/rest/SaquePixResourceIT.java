package com.bankernel.web.rest;

import static com.bankernel.domain.SaquePixAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bankernel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Saque;
import com.bankernel.domain.SaquePix;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumStatusSaquePix;
import com.bankernel.domain.enumeration.EnumTipoSaquePix;
import com.bankernel.repository.SaquePixRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.SaquePixService;
import com.bankernel.service.dto.SaquePixDTO;
import com.bankernel.service.mapper.SaquePixMapper;
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
 * Integration tests for the {@link SaquePixResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SaquePixResourceIT {

    private static final EnumTipoSaquePix DEFAULT_TIPO = EnumTipoSaquePix.CHAVE;
    private static final EnumTipoSaquePix UPDATED_TIPO = EnumTipoSaquePix.MANUAL;

    private static final EnumStatusSaquePix DEFAULT_SITUACAO = EnumStatusSaquePix.INICIADO;
    private static final EnumStatusSaquePix UPDATED_SITUACAO = EnumStatusSaquePix.PROCESSADO;

    private static final BigDecimal DEFAULT_VALOR_SAQUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_SAQUE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_ENVIADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_ENVIADO = new BigDecimal(2);

    private static final String DEFAULT_IDENTIFICADOR_PAGAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR_PAGAMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICADOR_PONTA_A_PONTA = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICADOR_PONTA_A_PONTA = "BBBBBBBBBB";

    private static final String DEFAULT_CAMPO_LIVRE = "AAAAAAAAAA";
    private static final String UPDATED_CAMPO_LIVRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/saque-pixes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SaquePixRepository saquePixRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private SaquePixRepository saquePixRepositoryMock;

    @Autowired
    private SaquePixMapper saquePixMapper;

    @Mock
    private SaquePixService saquePixServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSaquePixMockMvc;

    private SaquePix saquePix;

    private SaquePix insertedSaquePix;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SaquePix createEntity(EntityManager em) {
        SaquePix saquePix = new SaquePix()
            .tipo(DEFAULT_TIPO)
            .situacao(DEFAULT_SITUACAO)
            .valorSaque(DEFAULT_VALOR_SAQUE)
            .valorEnviado(DEFAULT_VALOR_ENVIADO)
            .identificadorPagamento(DEFAULT_IDENTIFICADOR_PAGAMENTO)
            .identificadorPontaAPonta(DEFAULT_IDENTIFICADOR_PONTA_A_PONTA)
            .campoLivre(DEFAULT_CAMPO_LIVRE);
        // Add required entity
        Saque saque;
        if (TestUtil.findAll(em, Saque.class).isEmpty()) {
            saque = SaqueResourceIT.createEntity(em);
            em.persist(saque);
            em.flush();
        } else {
            saque = TestUtil.findAll(em, Saque.class).get(0);
        }
        saquePix.setSaque(saque);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        saquePix.setUsuario(user);
        return saquePix;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SaquePix createUpdatedEntity(EntityManager em) {
        SaquePix updatedSaquePix = new SaquePix()
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .valorSaque(UPDATED_VALOR_SAQUE)
            .valorEnviado(UPDATED_VALOR_ENVIADO)
            .identificadorPagamento(UPDATED_IDENTIFICADOR_PAGAMENTO)
            .identificadorPontaAPonta(UPDATED_IDENTIFICADOR_PONTA_A_PONTA)
            .campoLivre(UPDATED_CAMPO_LIVRE);
        // Add required entity
        Saque saque;
        if (TestUtil.findAll(em, Saque.class).isEmpty()) {
            saque = SaqueResourceIT.createUpdatedEntity(em);
            em.persist(saque);
            em.flush();
        } else {
            saque = TestUtil.findAll(em, Saque.class).get(0);
        }
        updatedSaquePix.setSaque(saque);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedSaquePix.setUsuario(user);
        return updatedSaquePix;
    }

    @BeforeEach
    void initTest() {
        saquePix = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSaquePix != null) {
            saquePixRepository.delete(insertedSaquePix);
            insertedSaquePix = null;
        }
    }

    @Test
    @Transactional
    void createSaquePix() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SaquePix
        SaquePixDTO saquePixDTO = saquePixMapper.toDto(saquePix);
        var returnedSaquePixDTO = om.readValue(
            restSaquePixMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saquePixDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SaquePixDTO.class
        );

        // Validate the SaquePix in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSaquePix = saquePixMapper.toEntity(returnedSaquePixDTO);
        assertSaquePixUpdatableFieldsEquals(returnedSaquePix, getPersistedSaquePix(returnedSaquePix));

        insertedSaquePix = returnedSaquePix;
    }

    @Test
    @Transactional
    void createSaquePixWithExistingId() throws Exception {
        // Create the SaquePix with an existing ID
        saquePix.setId(1L);
        SaquePixDTO saquePixDTO = saquePixMapper.toDto(saquePix);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaquePixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saquePixDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SaquePix in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        saquePix.setTipo(null);

        // Create the SaquePix, which fails.
        SaquePixDTO saquePixDTO = saquePixMapper.toDto(saquePix);

        restSaquePixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saquePixDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSituacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        saquePix.setSituacao(null);

        // Create the SaquePix, which fails.
        SaquePixDTO saquePixDTO = saquePixMapper.toDto(saquePix);

        restSaquePixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saquePixDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorSaqueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        saquePix.setValorSaque(null);

        // Create the SaquePix, which fails.
        SaquePixDTO saquePixDTO = saquePixMapper.toDto(saquePix);

        restSaquePixMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saquePixDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSaquePixes() throws Exception {
        // Initialize the database
        insertedSaquePix = saquePixRepository.saveAndFlush(saquePix);

        // Get all the saquePixList
        restSaquePixMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saquePix.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].valorSaque").value(hasItem(sameNumber(DEFAULT_VALOR_SAQUE))))
            .andExpect(jsonPath("$.[*].valorEnviado").value(hasItem(sameNumber(DEFAULT_VALOR_ENVIADO))))
            .andExpect(jsonPath("$.[*].identificadorPagamento").value(hasItem(DEFAULT_IDENTIFICADOR_PAGAMENTO)))
            .andExpect(jsonPath("$.[*].identificadorPontaAPonta").value(hasItem(DEFAULT_IDENTIFICADOR_PONTA_A_PONTA)))
            .andExpect(jsonPath("$.[*].campoLivre").value(hasItem(DEFAULT_CAMPO_LIVRE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSaquePixesWithEagerRelationshipsIsEnabled() throws Exception {
        when(saquePixServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSaquePixMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(saquePixServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSaquePixesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(saquePixServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSaquePixMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(saquePixRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSaquePix() throws Exception {
        // Initialize the database
        insertedSaquePix = saquePixRepository.saveAndFlush(saquePix);

        // Get the saquePix
        restSaquePixMockMvc
            .perform(get(ENTITY_API_URL_ID, saquePix.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(saquePix.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()))
            .andExpect(jsonPath("$.valorSaque").value(sameNumber(DEFAULT_VALOR_SAQUE)))
            .andExpect(jsonPath("$.valorEnviado").value(sameNumber(DEFAULT_VALOR_ENVIADO)))
            .andExpect(jsonPath("$.identificadorPagamento").value(DEFAULT_IDENTIFICADOR_PAGAMENTO))
            .andExpect(jsonPath("$.identificadorPontaAPonta").value(DEFAULT_IDENTIFICADOR_PONTA_A_PONTA))
            .andExpect(jsonPath("$.campoLivre").value(DEFAULT_CAMPO_LIVRE));
    }

    @Test
    @Transactional
    void getNonExistingSaquePix() throws Exception {
        // Get the saquePix
        restSaquePixMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSaquePix() throws Exception {
        // Initialize the database
        insertedSaquePix = saquePixRepository.saveAndFlush(saquePix);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saquePix
        SaquePix updatedSaquePix = saquePixRepository.findById(saquePix.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSaquePix are not directly saved in db
        em.detach(updatedSaquePix);
        updatedSaquePix
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .valorSaque(UPDATED_VALOR_SAQUE)
            .valorEnviado(UPDATED_VALOR_ENVIADO)
            .identificadorPagamento(UPDATED_IDENTIFICADOR_PAGAMENTO)
            .identificadorPontaAPonta(UPDATED_IDENTIFICADOR_PONTA_A_PONTA)
            .campoLivre(UPDATED_CAMPO_LIVRE);
        SaquePixDTO saquePixDTO = saquePixMapper.toDto(updatedSaquePix);

        restSaquePixMockMvc
            .perform(
                put(ENTITY_API_URL_ID, saquePixDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(saquePixDTO))
            )
            .andExpect(status().isOk());

        // Validate the SaquePix in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSaquePixToMatchAllProperties(updatedSaquePix);
    }

    @Test
    @Transactional
    void putNonExistingSaquePix() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saquePix.setId(longCount.incrementAndGet());

        // Create the SaquePix
        SaquePixDTO saquePixDTO = saquePixMapper.toDto(saquePix);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaquePixMockMvc
            .perform(
                put(ENTITY_API_URL_ID, saquePixDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(saquePixDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaquePix in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSaquePix() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saquePix.setId(longCount.incrementAndGet());

        // Create the SaquePix
        SaquePixDTO saquePixDTO = saquePixMapper.toDto(saquePix);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaquePixMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(saquePixDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaquePix in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSaquePix() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saquePix.setId(longCount.incrementAndGet());

        // Create the SaquePix
        SaquePixDTO saquePixDTO = saquePixMapper.toDto(saquePix);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaquePixMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saquePixDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SaquePix in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSaquePixWithPatch() throws Exception {
        // Initialize the database
        insertedSaquePix = saquePixRepository.saveAndFlush(saquePix);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saquePix using partial update
        SaquePix partialUpdatedSaquePix = new SaquePix();
        partialUpdatedSaquePix.setId(saquePix.getId());

        partialUpdatedSaquePix
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .valorEnviado(UPDATED_VALOR_ENVIADO)
            .identificadorPagamento(UPDATED_IDENTIFICADOR_PAGAMENTO)
            .campoLivre(UPDATED_CAMPO_LIVRE);

        restSaquePixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaquePix.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSaquePix))
            )
            .andExpect(status().isOk());

        // Validate the SaquePix in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSaquePixUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSaquePix, saquePix), getPersistedSaquePix(saquePix));
    }

    @Test
    @Transactional
    void fullUpdateSaquePixWithPatch() throws Exception {
        // Initialize the database
        insertedSaquePix = saquePixRepository.saveAndFlush(saquePix);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saquePix using partial update
        SaquePix partialUpdatedSaquePix = new SaquePix();
        partialUpdatedSaquePix.setId(saquePix.getId());

        partialUpdatedSaquePix
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .valorSaque(UPDATED_VALOR_SAQUE)
            .valorEnviado(UPDATED_VALOR_ENVIADO)
            .identificadorPagamento(UPDATED_IDENTIFICADOR_PAGAMENTO)
            .identificadorPontaAPonta(UPDATED_IDENTIFICADOR_PONTA_A_PONTA)
            .campoLivre(UPDATED_CAMPO_LIVRE);

        restSaquePixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaquePix.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSaquePix))
            )
            .andExpect(status().isOk());

        // Validate the SaquePix in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSaquePixUpdatableFieldsEquals(partialUpdatedSaquePix, getPersistedSaquePix(partialUpdatedSaquePix));
    }

    @Test
    @Transactional
    void patchNonExistingSaquePix() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saquePix.setId(longCount.incrementAndGet());

        // Create the SaquePix
        SaquePixDTO saquePixDTO = saquePixMapper.toDto(saquePix);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaquePixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, saquePixDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(saquePixDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaquePix in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSaquePix() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saquePix.setId(longCount.incrementAndGet());

        // Create the SaquePix
        SaquePixDTO saquePixDTO = saquePixMapper.toDto(saquePix);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaquePixMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(saquePixDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaquePix in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSaquePix() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saquePix.setId(longCount.incrementAndGet());

        // Create the SaquePix
        SaquePixDTO saquePixDTO = saquePixMapper.toDto(saquePix);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaquePixMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(saquePixDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SaquePix in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSaquePix() throws Exception {
        // Initialize the database
        insertedSaquePix = saquePixRepository.saveAndFlush(saquePix);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the saquePix
        restSaquePixMockMvc
            .perform(delete(ENTITY_API_URL_ID, saquePix.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return saquePixRepository.count();
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

    protected SaquePix getPersistedSaquePix(SaquePix saquePix) {
        return saquePixRepository.findById(saquePix.getId()).orElseThrow();
    }

    protected void assertPersistedSaquePixToMatchAllProperties(SaquePix expectedSaquePix) {
        assertSaquePixAllPropertiesEquals(expectedSaquePix, getPersistedSaquePix(expectedSaquePix));
    }

    protected void assertPersistedSaquePixToMatchUpdatableProperties(SaquePix expectedSaquePix) {
        assertSaquePixAllUpdatablePropertiesEquals(expectedSaquePix, getPersistedSaquePix(expectedSaquePix));
    }
}
