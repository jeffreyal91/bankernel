package com.bankernel.web.rest;

import static com.bankernel.domain.ColaboradorPJAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.ColaboradorPJ;
import com.bankernel.domain.PessoaJuridica;
import com.bankernel.domain.User;
import com.bankernel.repository.ColaboradorPJRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.ColaboradorPJService;
import com.bankernel.service.dto.ColaboradorPJDTO;
import com.bankernel.service.mapper.ColaboradorPJMapper;
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
 * Integration tests for the {@link ColaboradorPJResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ColaboradorPJResourceIT {

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final String DEFAULT_DEPARTAMENTO = "AAAAAAAAAA";
    private static final String UPDATED_DEPARTAMENTO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/colaborador-pjs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ColaboradorPJRepository colaboradorPJRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ColaboradorPJRepository colaboradorPJRepositoryMock;

    @Autowired
    private ColaboradorPJMapper colaboradorPJMapper;

    @Mock
    private ColaboradorPJService colaboradorPJServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restColaboradorPJMockMvc;

    private ColaboradorPJ colaboradorPJ;

    private ColaboradorPJ insertedColaboradorPJ;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ColaboradorPJ createEntity(EntityManager em) {
        ColaboradorPJ colaboradorPJ = new ColaboradorPJ().ativo(DEFAULT_ATIVO).departamento(DEFAULT_DEPARTAMENTO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        colaboradorPJ.setUsuario(user);
        // Add required entity
        PessoaJuridica pessoaJuridica;
        if (TestUtil.findAll(em, PessoaJuridica.class).isEmpty()) {
            pessoaJuridica = PessoaJuridicaResourceIT.createEntity(em);
            em.persist(pessoaJuridica);
            em.flush();
        } else {
            pessoaJuridica = TestUtil.findAll(em, PessoaJuridica.class).get(0);
        }
        colaboradorPJ.setPessoaJuridica(pessoaJuridica);
        return colaboradorPJ;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ColaboradorPJ createUpdatedEntity(EntityManager em) {
        ColaboradorPJ updatedColaboradorPJ = new ColaboradorPJ().ativo(UPDATED_ATIVO).departamento(UPDATED_DEPARTAMENTO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedColaboradorPJ.setUsuario(user);
        // Add required entity
        PessoaJuridica pessoaJuridica;
        if (TestUtil.findAll(em, PessoaJuridica.class).isEmpty()) {
            pessoaJuridica = PessoaJuridicaResourceIT.createUpdatedEntity(em);
            em.persist(pessoaJuridica);
            em.flush();
        } else {
            pessoaJuridica = TestUtil.findAll(em, PessoaJuridica.class).get(0);
        }
        updatedColaboradorPJ.setPessoaJuridica(pessoaJuridica);
        return updatedColaboradorPJ;
    }

    @BeforeEach
    void initTest() {
        colaboradorPJ = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedColaboradorPJ != null) {
            colaboradorPJRepository.delete(insertedColaboradorPJ);
            insertedColaboradorPJ = null;
        }
    }

    @Test
    @Transactional
    void createColaboradorPJ() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ColaboradorPJ
        ColaboradorPJDTO colaboradorPJDTO = colaboradorPJMapper.toDto(colaboradorPJ);
        var returnedColaboradorPJDTO = om.readValue(
            restColaboradorPJMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(colaboradorPJDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ColaboradorPJDTO.class
        );

        // Validate the ColaboradorPJ in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedColaboradorPJ = colaboradorPJMapper.toEntity(returnedColaboradorPJDTO);
        assertColaboradorPJUpdatableFieldsEquals(returnedColaboradorPJ, getPersistedColaboradorPJ(returnedColaboradorPJ));

        insertedColaboradorPJ = returnedColaboradorPJ;
    }

    @Test
    @Transactional
    void createColaboradorPJWithExistingId() throws Exception {
        // Create the ColaboradorPJ with an existing ID
        colaboradorPJ.setId(1L);
        ColaboradorPJDTO colaboradorPJDTO = colaboradorPJMapper.toDto(colaboradorPJ);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restColaboradorPJMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(colaboradorPJDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ColaboradorPJ in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        colaboradorPJ.setAtivo(null);

        // Create the ColaboradorPJ, which fails.
        ColaboradorPJDTO colaboradorPJDTO = colaboradorPJMapper.toDto(colaboradorPJ);

        restColaboradorPJMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(colaboradorPJDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllColaboradorPJS() throws Exception {
        // Initialize the database
        insertedColaboradorPJ = colaboradorPJRepository.saveAndFlush(colaboradorPJ);

        // Get all the colaboradorPJList
        restColaboradorPJMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(colaboradorPJ.getId().intValue())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)))
            .andExpect(jsonPath("$.[*].departamento").value(hasItem(DEFAULT_DEPARTAMENTO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllColaboradorPJSWithEagerRelationshipsIsEnabled() throws Exception {
        when(colaboradorPJServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restColaboradorPJMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(colaboradorPJServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllColaboradorPJSWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(colaboradorPJServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restColaboradorPJMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(colaboradorPJRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getColaboradorPJ() throws Exception {
        // Initialize the database
        insertedColaboradorPJ = colaboradorPJRepository.saveAndFlush(colaboradorPJ);

        // Get the colaboradorPJ
        restColaboradorPJMockMvc
            .perform(get(ENTITY_API_URL_ID, colaboradorPJ.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(colaboradorPJ.getId().intValue()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO))
            .andExpect(jsonPath("$.departamento").value(DEFAULT_DEPARTAMENTO));
    }

    @Test
    @Transactional
    void getNonExistingColaboradorPJ() throws Exception {
        // Get the colaboradorPJ
        restColaboradorPJMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingColaboradorPJ() throws Exception {
        // Initialize the database
        insertedColaboradorPJ = colaboradorPJRepository.saveAndFlush(colaboradorPJ);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the colaboradorPJ
        ColaboradorPJ updatedColaboradorPJ = colaboradorPJRepository.findById(colaboradorPJ.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedColaboradorPJ are not directly saved in db
        em.detach(updatedColaboradorPJ);
        updatedColaboradorPJ.ativo(UPDATED_ATIVO).departamento(UPDATED_DEPARTAMENTO);
        ColaboradorPJDTO colaboradorPJDTO = colaboradorPJMapper.toDto(updatedColaboradorPJ);

        restColaboradorPJMockMvc
            .perform(
                put(ENTITY_API_URL_ID, colaboradorPJDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(colaboradorPJDTO))
            )
            .andExpect(status().isOk());

        // Validate the ColaboradorPJ in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedColaboradorPJToMatchAllProperties(updatedColaboradorPJ);
    }

    @Test
    @Transactional
    void putNonExistingColaboradorPJ() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        colaboradorPJ.setId(longCount.incrementAndGet());

        // Create the ColaboradorPJ
        ColaboradorPJDTO colaboradorPJDTO = colaboradorPJMapper.toDto(colaboradorPJ);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restColaboradorPJMockMvc
            .perform(
                put(ENTITY_API_URL_ID, colaboradorPJDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(colaboradorPJDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ColaboradorPJ in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchColaboradorPJ() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        colaboradorPJ.setId(longCount.incrementAndGet());

        // Create the ColaboradorPJ
        ColaboradorPJDTO colaboradorPJDTO = colaboradorPJMapper.toDto(colaboradorPJ);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColaboradorPJMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(colaboradorPJDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ColaboradorPJ in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamColaboradorPJ() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        colaboradorPJ.setId(longCount.incrementAndGet());

        // Create the ColaboradorPJ
        ColaboradorPJDTO colaboradorPJDTO = colaboradorPJMapper.toDto(colaboradorPJ);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColaboradorPJMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(colaboradorPJDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ColaboradorPJ in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateColaboradorPJWithPatch() throws Exception {
        // Initialize the database
        insertedColaboradorPJ = colaboradorPJRepository.saveAndFlush(colaboradorPJ);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the colaboradorPJ using partial update
        ColaboradorPJ partialUpdatedColaboradorPJ = new ColaboradorPJ();
        partialUpdatedColaboradorPJ.setId(colaboradorPJ.getId());

        restColaboradorPJMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedColaboradorPJ.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedColaboradorPJ))
            )
            .andExpect(status().isOk());

        // Validate the ColaboradorPJ in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertColaboradorPJUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedColaboradorPJ, colaboradorPJ),
            getPersistedColaboradorPJ(colaboradorPJ)
        );
    }

    @Test
    @Transactional
    void fullUpdateColaboradorPJWithPatch() throws Exception {
        // Initialize the database
        insertedColaboradorPJ = colaboradorPJRepository.saveAndFlush(colaboradorPJ);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the colaboradorPJ using partial update
        ColaboradorPJ partialUpdatedColaboradorPJ = new ColaboradorPJ();
        partialUpdatedColaboradorPJ.setId(colaboradorPJ.getId());

        partialUpdatedColaboradorPJ.ativo(UPDATED_ATIVO).departamento(UPDATED_DEPARTAMENTO);

        restColaboradorPJMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedColaboradorPJ.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedColaboradorPJ))
            )
            .andExpect(status().isOk());

        // Validate the ColaboradorPJ in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertColaboradorPJUpdatableFieldsEquals(partialUpdatedColaboradorPJ, getPersistedColaboradorPJ(partialUpdatedColaboradorPJ));
    }

    @Test
    @Transactional
    void patchNonExistingColaboradorPJ() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        colaboradorPJ.setId(longCount.incrementAndGet());

        // Create the ColaboradorPJ
        ColaboradorPJDTO colaboradorPJDTO = colaboradorPJMapper.toDto(colaboradorPJ);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restColaboradorPJMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, colaboradorPJDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(colaboradorPJDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ColaboradorPJ in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchColaboradorPJ() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        colaboradorPJ.setId(longCount.incrementAndGet());

        // Create the ColaboradorPJ
        ColaboradorPJDTO colaboradorPJDTO = colaboradorPJMapper.toDto(colaboradorPJ);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColaboradorPJMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(colaboradorPJDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ColaboradorPJ in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamColaboradorPJ() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        colaboradorPJ.setId(longCount.incrementAndGet());

        // Create the ColaboradorPJ
        ColaboradorPJDTO colaboradorPJDTO = colaboradorPJMapper.toDto(colaboradorPJ);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restColaboradorPJMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(colaboradorPJDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ColaboradorPJ in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteColaboradorPJ() throws Exception {
        // Initialize the database
        insertedColaboradorPJ = colaboradorPJRepository.saveAndFlush(colaboradorPJ);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the colaboradorPJ
        restColaboradorPJMockMvc
            .perform(delete(ENTITY_API_URL_ID, colaboradorPJ.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return colaboradorPJRepository.count();
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

    protected ColaboradorPJ getPersistedColaboradorPJ(ColaboradorPJ colaboradorPJ) {
        return colaboradorPJRepository.findById(colaboradorPJ.getId()).orElseThrow();
    }

    protected void assertPersistedColaboradorPJToMatchAllProperties(ColaboradorPJ expectedColaboradorPJ) {
        assertColaboradorPJAllPropertiesEquals(expectedColaboradorPJ, getPersistedColaboradorPJ(expectedColaboradorPJ));
    }

    protected void assertPersistedColaboradorPJToMatchUpdatableProperties(ColaboradorPJ expectedColaboradorPJ) {
        assertColaboradorPJAllUpdatablePropertiesEquals(expectedColaboradorPJ, getPersistedColaboradorPJ(expectedColaboradorPJ));
    }
}
