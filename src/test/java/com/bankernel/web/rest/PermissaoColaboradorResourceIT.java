package com.bankernel.web.rest;

import static com.bankernel.domain.PermissaoColaboradorAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.ColaboradorPJ;
import com.bankernel.domain.PermissaoColaborador;
import com.bankernel.domain.enumeration.EnumTipoPermissao;
import com.bankernel.repository.PermissaoColaboradorRepository;
import com.bankernel.service.PermissaoColaboradorService;
import com.bankernel.service.dto.PermissaoColaboradorDTO;
import com.bankernel.service.mapper.PermissaoColaboradorMapper;
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
 * Integration tests for the {@link PermissaoColaboradorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class PermissaoColaboradorResourceIT {

    private static final EnumTipoPermissao DEFAULT_TIPO_PERMISSAO = EnumTipoPermissao.VISUALIZAR;
    private static final EnumTipoPermissao UPDATED_TIPO_PERMISSAO = EnumTipoPermissao.OPERAR;

    private static final String ENTITY_API_URL = "/api/permissao-colaboradors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PermissaoColaboradorRepository permissaoColaboradorRepository;

    @Mock
    private PermissaoColaboradorRepository permissaoColaboradorRepositoryMock;

    @Autowired
    private PermissaoColaboradorMapper permissaoColaboradorMapper;

    @Mock
    private PermissaoColaboradorService permissaoColaboradorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPermissaoColaboradorMockMvc;

    private PermissaoColaborador permissaoColaborador;

    private PermissaoColaborador insertedPermissaoColaborador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PermissaoColaborador createEntity(EntityManager em) {
        PermissaoColaborador permissaoColaborador = new PermissaoColaborador().tipoPermissao(DEFAULT_TIPO_PERMISSAO);
        // Add required entity
        ColaboradorPJ colaboradorPJ;
        if (TestUtil.findAll(em, ColaboradorPJ.class).isEmpty()) {
            colaboradorPJ = ColaboradorPJResourceIT.createEntity(em);
            em.persist(colaboradorPJ);
            em.flush();
        } else {
            colaboradorPJ = TestUtil.findAll(em, ColaboradorPJ.class).get(0);
        }
        permissaoColaborador.setColaborador(colaboradorPJ);
        return permissaoColaborador;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PermissaoColaborador createUpdatedEntity(EntityManager em) {
        PermissaoColaborador updatedPermissaoColaborador = new PermissaoColaborador().tipoPermissao(UPDATED_TIPO_PERMISSAO);
        // Add required entity
        ColaboradorPJ colaboradorPJ;
        if (TestUtil.findAll(em, ColaboradorPJ.class).isEmpty()) {
            colaboradorPJ = ColaboradorPJResourceIT.createUpdatedEntity(em);
            em.persist(colaboradorPJ);
            em.flush();
        } else {
            colaboradorPJ = TestUtil.findAll(em, ColaboradorPJ.class).get(0);
        }
        updatedPermissaoColaborador.setColaborador(colaboradorPJ);
        return updatedPermissaoColaborador;
    }

    @BeforeEach
    void initTest() {
        permissaoColaborador = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedPermissaoColaborador != null) {
            permissaoColaboradorRepository.delete(insertedPermissaoColaborador);
            insertedPermissaoColaborador = null;
        }
    }

    @Test
    @Transactional
    void createPermissaoColaborador() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PermissaoColaborador
        PermissaoColaboradorDTO permissaoColaboradorDTO = permissaoColaboradorMapper.toDto(permissaoColaborador);
        var returnedPermissaoColaboradorDTO = om.readValue(
            restPermissaoColaboradorMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(permissaoColaboradorDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PermissaoColaboradorDTO.class
        );

        // Validate the PermissaoColaborador in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPermissaoColaborador = permissaoColaboradorMapper.toEntity(returnedPermissaoColaboradorDTO);
        assertPermissaoColaboradorUpdatableFieldsEquals(
            returnedPermissaoColaborador,
            getPersistedPermissaoColaborador(returnedPermissaoColaborador)
        );

        insertedPermissaoColaborador = returnedPermissaoColaborador;
    }

    @Test
    @Transactional
    void createPermissaoColaboradorWithExistingId() throws Exception {
        // Create the PermissaoColaborador with an existing ID
        permissaoColaborador.setId(1L);
        PermissaoColaboradorDTO permissaoColaboradorDTO = permissaoColaboradorMapper.toDto(permissaoColaborador);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermissaoColaboradorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(permissaoColaboradorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PermissaoColaborador in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTipoPermissaoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        permissaoColaborador.setTipoPermissao(null);

        // Create the PermissaoColaborador, which fails.
        PermissaoColaboradorDTO permissaoColaboradorDTO = permissaoColaboradorMapper.toDto(permissaoColaborador);

        restPermissaoColaboradorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(permissaoColaboradorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPermissaoColaboradors() throws Exception {
        // Initialize the database
        insertedPermissaoColaborador = permissaoColaboradorRepository.saveAndFlush(permissaoColaborador);

        // Get all the permissaoColaboradorList
        restPermissaoColaboradorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissaoColaborador.getId().intValue())))
            .andExpect(jsonPath("$.[*].tipoPermissao").value(hasItem(DEFAULT_TIPO_PERMISSAO.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPermissaoColaboradorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(permissaoColaboradorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPermissaoColaboradorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(permissaoColaboradorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllPermissaoColaboradorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(permissaoColaboradorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restPermissaoColaboradorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(permissaoColaboradorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getPermissaoColaborador() throws Exception {
        // Initialize the database
        insertedPermissaoColaborador = permissaoColaboradorRepository.saveAndFlush(permissaoColaborador);

        // Get the permissaoColaborador
        restPermissaoColaboradorMockMvc
            .perform(get(ENTITY_API_URL_ID, permissaoColaborador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(permissaoColaborador.getId().intValue()))
            .andExpect(jsonPath("$.tipoPermissao").value(DEFAULT_TIPO_PERMISSAO.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPermissaoColaborador() throws Exception {
        // Get the permissaoColaborador
        restPermissaoColaboradorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPermissaoColaborador() throws Exception {
        // Initialize the database
        insertedPermissaoColaborador = permissaoColaboradorRepository.saveAndFlush(permissaoColaborador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the permissaoColaborador
        PermissaoColaborador updatedPermissaoColaborador = permissaoColaboradorRepository
            .findById(permissaoColaborador.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedPermissaoColaborador are not directly saved in db
        em.detach(updatedPermissaoColaborador);
        updatedPermissaoColaborador.tipoPermissao(UPDATED_TIPO_PERMISSAO);
        PermissaoColaboradorDTO permissaoColaboradorDTO = permissaoColaboradorMapper.toDto(updatedPermissaoColaborador);

        restPermissaoColaboradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissaoColaboradorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(permissaoColaboradorDTO))
            )
            .andExpect(status().isOk());

        // Validate the PermissaoColaborador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPermissaoColaboradorToMatchAllProperties(updatedPermissaoColaborador);
    }

    @Test
    @Transactional
    void putNonExistingPermissaoColaborador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissaoColaborador.setId(longCount.incrementAndGet());

        // Create the PermissaoColaborador
        PermissaoColaboradorDTO permissaoColaboradorDTO = permissaoColaboradorMapper.toDto(permissaoColaborador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissaoColaboradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissaoColaboradorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(permissaoColaboradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissaoColaborador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPermissaoColaborador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissaoColaborador.setId(longCount.incrementAndGet());

        // Create the PermissaoColaborador
        PermissaoColaboradorDTO permissaoColaboradorDTO = permissaoColaboradorMapper.toDto(permissaoColaborador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissaoColaboradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(permissaoColaboradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissaoColaborador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPermissaoColaborador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissaoColaborador.setId(longCount.incrementAndGet());

        // Create the PermissaoColaborador
        PermissaoColaboradorDTO permissaoColaboradorDTO = permissaoColaboradorMapper.toDto(permissaoColaborador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissaoColaboradorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(permissaoColaboradorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PermissaoColaborador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePermissaoColaboradorWithPatch() throws Exception {
        // Initialize the database
        insertedPermissaoColaborador = permissaoColaboradorRepository.saveAndFlush(permissaoColaborador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the permissaoColaborador using partial update
        PermissaoColaborador partialUpdatedPermissaoColaborador = new PermissaoColaborador();
        partialUpdatedPermissaoColaborador.setId(permissaoColaborador.getId());

        partialUpdatedPermissaoColaborador.tipoPermissao(UPDATED_TIPO_PERMISSAO);

        restPermissaoColaboradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermissaoColaborador.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPermissaoColaborador))
            )
            .andExpect(status().isOk());

        // Validate the PermissaoColaborador in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPermissaoColaboradorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPermissaoColaborador, permissaoColaborador),
            getPersistedPermissaoColaborador(permissaoColaborador)
        );
    }

    @Test
    @Transactional
    void fullUpdatePermissaoColaboradorWithPatch() throws Exception {
        // Initialize the database
        insertedPermissaoColaborador = permissaoColaboradorRepository.saveAndFlush(permissaoColaborador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the permissaoColaborador using partial update
        PermissaoColaborador partialUpdatedPermissaoColaborador = new PermissaoColaborador();
        partialUpdatedPermissaoColaborador.setId(permissaoColaborador.getId());

        partialUpdatedPermissaoColaborador.tipoPermissao(UPDATED_TIPO_PERMISSAO);

        restPermissaoColaboradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermissaoColaborador.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPermissaoColaborador))
            )
            .andExpect(status().isOk());

        // Validate the PermissaoColaborador in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPermissaoColaboradorUpdatableFieldsEquals(
            partialUpdatedPermissaoColaborador,
            getPersistedPermissaoColaborador(partialUpdatedPermissaoColaborador)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPermissaoColaborador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissaoColaborador.setId(longCount.incrementAndGet());

        // Create the PermissaoColaborador
        PermissaoColaboradorDTO permissaoColaboradorDTO = permissaoColaboradorMapper.toDto(permissaoColaborador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissaoColaboradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, permissaoColaboradorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(permissaoColaboradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissaoColaborador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPermissaoColaborador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissaoColaborador.setId(longCount.incrementAndGet());

        // Create the PermissaoColaborador
        PermissaoColaboradorDTO permissaoColaboradorDTO = permissaoColaboradorMapper.toDto(permissaoColaborador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissaoColaboradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(permissaoColaboradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissaoColaborador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPermissaoColaborador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissaoColaborador.setId(longCount.incrementAndGet());

        // Create the PermissaoColaborador
        PermissaoColaboradorDTO permissaoColaboradorDTO = permissaoColaboradorMapper.toDto(permissaoColaborador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissaoColaboradorMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(permissaoColaboradorDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PermissaoColaborador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePermissaoColaborador() throws Exception {
        // Initialize the database
        insertedPermissaoColaborador = permissaoColaboradorRepository.saveAndFlush(permissaoColaborador);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the permissaoColaborador
        restPermissaoColaboradorMockMvc
            .perform(delete(ENTITY_API_URL_ID, permissaoColaborador.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return permissaoColaboradorRepository.count();
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

    protected PermissaoColaborador getPersistedPermissaoColaborador(PermissaoColaborador permissaoColaborador) {
        return permissaoColaboradorRepository.findById(permissaoColaborador.getId()).orElseThrow();
    }

    protected void assertPersistedPermissaoColaboradorToMatchAllProperties(PermissaoColaborador expectedPermissaoColaborador) {
        assertPermissaoColaboradorAllPropertiesEquals(
            expectedPermissaoColaborador,
            getPersistedPermissaoColaborador(expectedPermissaoColaborador)
        );
    }

    protected void assertPersistedPermissaoColaboradorToMatchUpdatableProperties(PermissaoColaborador expectedPermissaoColaborador) {
        assertPermissaoColaboradorAllUpdatablePropertiesEquals(
            expectedPermissaoColaborador,
            getPersistedPermissaoColaborador(expectedPermissaoColaborador)
        );
    }
}
