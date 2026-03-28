package com.bankernel.web.rest;

import static com.bankernel.domain.SaqueBoletoAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bankernel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Saque;
import com.bankernel.domain.SaqueBoleto;
import com.bankernel.domain.User;
import com.bankernel.domain.enumeration.EnumStatusSaqueBoleto;
import com.bankernel.domain.enumeration.EnumTipoSaqueBoleto;
import com.bankernel.repository.SaqueBoletoRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.SaqueBoletoService;
import com.bankernel.service.dto.SaqueBoletoDTO;
import com.bankernel.service.mapper.SaqueBoletoMapper;
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
 * Integration tests for the {@link SaqueBoletoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class SaqueBoletoResourceIT {

    private static final EnumTipoSaqueBoleto DEFAULT_TIPO = EnumTipoSaqueBoleto.PAGAMENTO;
    private static final EnumTipoSaqueBoleto UPDATED_TIPO = EnumTipoSaqueBoleto.TRANSFERENCIA;

    private static final EnumStatusSaqueBoleto DEFAULT_SITUACAO = EnumStatusSaqueBoleto.CRIADO;
    private static final EnumStatusSaqueBoleto UPDATED_SITUACAO = EnumStatusSaqueBoleto.EM_PROCESSAMENTO;

    private static final BigDecimal DEFAULT_VALOR_SAQUE = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_SAQUE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_VALOR_ENVIADO = new BigDecimal(1);
    private static final BigDecimal UPDATED_VALOR_ENVIADO = new BigDecimal(2);

    private static final String DEFAULT_CODIGO_BARRAS = "AAAAAAAAAA";
    private static final String UPDATED_CODIGO_BARRAS = "BBBBBBBBBB";

    private static final String DEFAULT_CAMPO_LIVRE = "AAAAAAAAAA";
    private static final String UPDATED_CAMPO_LIVRE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/saque-boletos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SaqueBoletoRepository saqueBoletoRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private SaqueBoletoRepository saqueBoletoRepositoryMock;

    @Autowired
    private SaqueBoletoMapper saqueBoletoMapper;

    @Mock
    private SaqueBoletoService saqueBoletoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSaqueBoletoMockMvc;

    private SaqueBoleto saqueBoleto;

    private SaqueBoleto insertedSaqueBoleto;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SaqueBoleto createEntity(EntityManager em) {
        SaqueBoleto saqueBoleto = new SaqueBoleto()
            .tipo(DEFAULT_TIPO)
            .situacao(DEFAULT_SITUACAO)
            .valorSaque(DEFAULT_VALOR_SAQUE)
            .valorEnviado(DEFAULT_VALOR_ENVIADO)
            .codigoBarras(DEFAULT_CODIGO_BARRAS)
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
        saqueBoleto.setSaque(saque);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        saqueBoleto.setUsuario(user);
        return saqueBoleto;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SaqueBoleto createUpdatedEntity(EntityManager em) {
        SaqueBoleto updatedSaqueBoleto = new SaqueBoleto()
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .valorSaque(UPDATED_VALOR_SAQUE)
            .valorEnviado(UPDATED_VALOR_ENVIADO)
            .codigoBarras(UPDATED_CODIGO_BARRAS)
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
        updatedSaqueBoleto.setSaque(saque);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedSaqueBoleto.setUsuario(user);
        return updatedSaqueBoleto;
    }

    @BeforeEach
    void initTest() {
        saqueBoleto = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedSaqueBoleto != null) {
            saqueBoletoRepository.delete(insertedSaqueBoleto);
            insertedSaqueBoleto = null;
        }
    }

    @Test
    @Transactional
    void createSaqueBoleto() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SaqueBoleto
        SaqueBoletoDTO saqueBoletoDTO = saqueBoletoMapper.toDto(saqueBoleto);
        var returnedSaqueBoletoDTO = om.readValue(
            restSaqueBoletoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saqueBoletoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SaqueBoletoDTO.class
        );

        // Validate the SaqueBoleto in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSaqueBoleto = saqueBoletoMapper.toEntity(returnedSaqueBoletoDTO);
        assertSaqueBoletoUpdatableFieldsEquals(returnedSaqueBoleto, getPersistedSaqueBoleto(returnedSaqueBoleto));

        insertedSaqueBoleto = returnedSaqueBoleto;
    }

    @Test
    @Transactional
    void createSaqueBoletoWithExistingId() throws Exception {
        // Create the SaqueBoleto with an existing ID
        saqueBoleto.setId(1L);
        SaqueBoletoDTO saqueBoletoDTO = saqueBoletoMapper.toDto(saqueBoleto);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSaqueBoletoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saqueBoletoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SaqueBoleto in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        saqueBoleto.setTipo(null);

        // Create the SaqueBoleto, which fails.
        SaqueBoletoDTO saqueBoletoDTO = saqueBoletoMapper.toDto(saqueBoleto);

        restSaqueBoletoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saqueBoletoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSituacaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        saqueBoleto.setSituacao(null);

        // Create the SaqueBoleto, which fails.
        SaqueBoletoDTO saqueBoletoDTO = saqueBoletoMapper.toDto(saqueBoleto);

        restSaqueBoletoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saqueBoletoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorSaqueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        saqueBoleto.setValorSaque(null);

        // Create the SaqueBoleto, which fails.
        SaqueBoletoDTO saqueBoletoDTO = saqueBoletoMapper.toDto(saqueBoleto);

        restSaqueBoletoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saqueBoletoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSaqueBoletos() throws Exception {
        // Initialize the database
        insertedSaqueBoleto = saqueBoletoRepository.saveAndFlush(saqueBoleto);

        // Get all the saqueBoletoList
        restSaqueBoletoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(saqueBoleto.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].situacao").value(hasItem(DEFAULT_SITUACAO.toString())))
            .andExpect(jsonPath("$.[*].valorSaque").value(hasItem(sameNumber(DEFAULT_VALOR_SAQUE))))
            .andExpect(jsonPath("$.[*].valorEnviado").value(hasItem(sameNumber(DEFAULT_VALOR_ENVIADO))))
            .andExpect(jsonPath("$.[*].codigoBarras").value(hasItem(DEFAULT_CODIGO_BARRAS)))
            .andExpect(jsonPath("$.[*].campoLivre").value(hasItem(DEFAULT_CAMPO_LIVRE)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSaqueBoletosWithEagerRelationshipsIsEnabled() throws Exception {
        when(saqueBoletoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSaqueBoletoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(saqueBoletoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSaqueBoletosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(saqueBoletoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restSaqueBoletoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(saqueBoletoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSaqueBoleto() throws Exception {
        // Initialize the database
        insertedSaqueBoleto = saqueBoletoRepository.saveAndFlush(saqueBoleto);

        // Get the saqueBoleto
        restSaqueBoletoMockMvc
            .perform(get(ENTITY_API_URL_ID, saqueBoleto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(saqueBoleto.getId().intValue()))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.situacao").value(DEFAULT_SITUACAO.toString()))
            .andExpect(jsonPath("$.valorSaque").value(sameNumber(DEFAULT_VALOR_SAQUE)))
            .andExpect(jsonPath("$.valorEnviado").value(sameNumber(DEFAULT_VALOR_ENVIADO)))
            .andExpect(jsonPath("$.codigoBarras").value(DEFAULT_CODIGO_BARRAS))
            .andExpect(jsonPath("$.campoLivre").value(DEFAULT_CAMPO_LIVRE));
    }

    @Test
    @Transactional
    void getNonExistingSaqueBoleto() throws Exception {
        // Get the saqueBoleto
        restSaqueBoletoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSaqueBoleto() throws Exception {
        // Initialize the database
        insertedSaqueBoleto = saqueBoletoRepository.saveAndFlush(saqueBoleto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saqueBoleto
        SaqueBoleto updatedSaqueBoleto = saqueBoletoRepository.findById(saqueBoleto.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSaqueBoleto are not directly saved in db
        em.detach(updatedSaqueBoleto);
        updatedSaqueBoleto
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .valorSaque(UPDATED_VALOR_SAQUE)
            .valorEnviado(UPDATED_VALOR_ENVIADO)
            .codigoBarras(UPDATED_CODIGO_BARRAS)
            .campoLivre(UPDATED_CAMPO_LIVRE);
        SaqueBoletoDTO saqueBoletoDTO = saqueBoletoMapper.toDto(updatedSaqueBoleto);

        restSaqueBoletoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, saqueBoletoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(saqueBoletoDTO))
            )
            .andExpect(status().isOk());

        // Validate the SaqueBoleto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSaqueBoletoToMatchAllProperties(updatedSaqueBoleto);
    }

    @Test
    @Transactional
    void putNonExistingSaqueBoleto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saqueBoleto.setId(longCount.incrementAndGet());

        // Create the SaqueBoleto
        SaqueBoletoDTO saqueBoletoDTO = saqueBoletoMapper.toDto(saqueBoleto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaqueBoletoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, saqueBoletoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(saqueBoletoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaqueBoleto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSaqueBoleto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saqueBoleto.setId(longCount.incrementAndGet());

        // Create the SaqueBoleto
        SaqueBoletoDTO saqueBoletoDTO = saqueBoletoMapper.toDto(saqueBoleto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaqueBoletoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(saqueBoletoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaqueBoleto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSaqueBoleto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saqueBoleto.setId(longCount.incrementAndGet());

        // Create the SaqueBoleto
        SaqueBoletoDTO saqueBoletoDTO = saqueBoletoMapper.toDto(saqueBoleto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaqueBoletoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(saqueBoletoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SaqueBoleto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSaqueBoletoWithPatch() throws Exception {
        // Initialize the database
        insertedSaqueBoleto = saqueBoletoRepository.saveAndFlush(saqueBoleto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saqueBoleto using partial update
        SaqueBoleto partialUpdatedSaqueBoleto = new SaqueBoleto();
        partialUpdatedSaqueBoleto.setId(saqueBoleto.getId());

        partialUpdatedSaqueBoleto
            .tipo(UPDATED_TIPO)
            .valorSaque(UPDATED_VALOR_SAQUE)
            .valorEnviado(UPDATED_VALOR_ENVIADO)
            .campoLivre(UPDATED_CAMPO_LIVRE);

        restSaqueBoletoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaqueBoleto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSaqueBoleto))
            )
            .andExpect(status().isOk());

        // Validate the SaqueBoleto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSaqueBoletoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSaqueBoleto, saqueBoleto),
            getPersistedSaqueBoleto(saqueBoleto)
        );
    }

    @Test
    @Transactional
    void fullUpdateSaqueBoletoWithPatch() throws Exception {
        // Initialize the database
        insertedSaqueBoleto = saqueBoletoRepository.saveAndFlush(saqueBoleto);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the saqueBoleto using partial update
        SaqueBoleto partialUpdatedSaqueBoleto = new SaqueBoleto();
        partialUpdatedSaqueBoleto.setId(saqueBoleto.getId());

        partialUpdatedSaqueBoleto
            .tipo(UPDATED_TIPO)
            .situacao(UPDATED_SITUACAO)
            .valorSaque(UPDATED_VALOR_SAQUE)
            .valorEnviado(UPDATED_VALOR_ENVIADO)
            .codigoBarras(UPDATED_CODIGO_BARRAS)
            .campoLivre(UPDATED_CAMPO_LIVRE);

        restSaqueBoletoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSaqueBoleto.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSaqueBoleto))
            )
            .andExpect(status().isOk());

        // Validate the SaqueBoleto in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSaqueBoletoUpdatableFieldsEquals(partialUpdatedSaqueBoleto, getPersistedSaqueBoleto(partialUpdatedSaqueBoleto));
    }

    @Test
    @Transactional
    void patchNonExistingSaqueBoleto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saqueBoleto.setId(longCount.incrementAndGet());

        // Create the SaqueBoleto
        SaqueBoletoDTO saqueBoletoDTO = saqueBoletoMapper.toDto(saqueBoleto);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSaqueBoletoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, saqueBoletoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(saqueBoletoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaqueBoleto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSaqueBoleto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saqueBoleto.setId(longCount.incrementAndGet());

        // Create the SaqueBoleto
        SaqueBoletoDTO saqueBoletoDTO = saqueBoletoMapper.toDto(saqueBoleto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaqueBoletoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(saqueBoletoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SaqueBoleto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSaqueBoleto() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        saqueBoleto.setId(longCount.incrementAndGet());

        // Create the SaqueBoleto
        SaqueBoletoDTO saqueBoletoDTO = saqueBoletoMapper.toDto(saqueBoleto);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSaqueBoletoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(saqueBoletoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SaqueBoleto in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSaqueBoleto() throws Exception {
        // Initialize the database
        insertedSaqueBoleto = saqueBoletoRepository.saveAndFlush(saqueBoleto);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the saqueBoleto
        restSaqueBoletoMockMvc
            .perform(delete(ENTITY_API_URL_ID, saqueBoleto.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return saqueBoletoRepository.count();
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

    protected SaqueBoleto getPersistedSaqueBoleto(SaqueBoleto saqueBoleto) {
        return saqueBoletoRepository.findById(saqueBoleto.getId()).orElseThrow();
    }

    protected void assertPersistedSaqueBoletoToMatchAllProperties(SaqueBoleto expectedSaqueBoleto) {
        assertSaqueBoletoAllPropertiesEquals(expectedSaqueBoleto, getPersistedSaqueBoleto(expectedSaqueBoleto));
    }

    protected void assertPersistedSaqueBoletoToMatchUpdatableProperties(SaqueBoleto expectedSaqueBoleto) {
        assertSaqueBoletoAllUpdatablePropertiesEquals(expectedSaqueBoleto, getPersistedSaqueBoleto(expectedSaqueBoleto));
    }
}
