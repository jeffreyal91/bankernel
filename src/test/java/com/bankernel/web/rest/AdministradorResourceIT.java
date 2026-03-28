package com.bankernel.web.rest;

import static com.bankernel.domain.AdministradorAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Administrador;
import com.bankernel.domain.User;
import com.bankernel.repository.AdministradorRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.AdministradorService;
import com.bankernel.service.dto.AdministradorDTO;
import com.bankernel.service.mapper.AdministradorMapper;
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
 * Integration tests for the {@link AdministradorResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AdministradorResourceIT {

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    private static final String ENTITY_API_URL = "/api/administradors";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AdministradorRepository administradorRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private AdministradorRepository administradorRepositoryMock;

    @Autowired
    private AdministradorMapper administradorMapper;

    @Mock
    private AdministradorService administradorServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdministradorMockMvc;

    private Administrador administrador;

    private Administrador insertedAdministrador;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrador createEntity(EntityManager em) {
        Administrador administrador = new Administrador().ativo(DEFAULT_ATIVO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        administrador.setUsuario(user);
        return administrador;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Administrador createUpdatedEntity(EntityManager em) {
        Administrador updatedAdministrador = new Administrador().ativo(UPDATED_ATIVO);
        // Add required entity
        User user = UserResourceIT.createEntity();
        em.persist(user);
        em.flush();
        updatedAdministrador.setUsuario(user);
        return updatedAdministrador;
    }

    @BeforeEach
    void initTest() {
        administrador = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedAdministrador != null) {
            administradorRepository.delete(insertedAdministrador);
            insertedAdministrador = null;
        }
    }

    @Test
    @Transactional
    void createAdministrador() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);
        var returnedAdministradorDTO = om.readValue(
            restAdministradorMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administradorDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AdministradorDTO.class
        );

        // Validate the Administrador in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAdministrador = administradorMapper.toEntity(returnedAdministradorDTO);
        assertAdministradorUpdatableFieldsEquals(returnedAdministrador, getPersistedAdministrador(returnedAdministrador));

        insertedAdministrador = returnedAdministrador;
    }

    @Test
    @Transactional
    void createAdministradorWithExistingId() throws Exception {
        // Create the Administrador with an existing ID
        administrador.setId(1L);
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdministradorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administradorDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAtivoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        administrador.setAtivo(null);

        // Create the Administrador, which fails.
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        restAdministradorMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administradorDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdministradors() throws Exception {
        // Initialize the database
        insertedAdministrador = administradorRepository.saveAndFlush(administrador);

        // Get all the administradorList
        restAdministradorMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(administrador.getId().intValue())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAdministradorsWithEagerRelationshipsIsEnabled() throws Exception {
        when(administradorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAdministradorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(administradorServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAdministradorsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(administradorServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAdministradorMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(administradorRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAdministrador() throws Exception {
        // Initialize the database
        insertedAdministrador = administradorRepository.saveAndFlush(administrador);

        // Get the administrador
        restAdministradorMockMvc
            .perform(get(ENTITY_API_URL_ID, administrador.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(administrador.getId().intValue()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO));
    }

    @Test
    @Transactional
    void getNonExistingAdministrador() throws Exception {
        // Get the administrador
        restAdministradorMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdministrador() throws Exception {
        // Initialize the database
        insertedAdministrador = administradorRepository.saveAndFlush(administrador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the administrador
        Administrador updatedAdministrador = administradorRepository.findById(administrador.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdministrador are not directly saved in db
        em.detach(updatedAdministrador);
        updatedAdministrador.ativo(UPDATED_ATIVO);
        AdministradorDTO administradorDTO = administradorMapper.toDto(updatedAdministrador);

        restAdministradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administradorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(administradorDTO))
            )
            .andExpect(status().isOk());

        // Validate the Administrador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAdministradorToMatchAllProperties(updatedAdministrador);
    }

    @Test
    @Transactional
    void putNonExistingAdministrador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrador.setId(longCount.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, administradorDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(administradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdministrador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrador.setId(longCount.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(administradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdministrador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrador.setId(longCount.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(administradorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdministradorWithPatch() throws Exception {
        // Initialize the database
        insertedAdministrador = administradorRepository.saveAndFlush(administrador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the administrador using partial update
        Administrador partialUpdatedAdministrador = new Administrador();
        partialUpdatedAdministrador.setId(administrador.getId());

        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrador.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdministrador))
            )
            .andExpect(status().isOk());

        // Validate the Administrador in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdministradorUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAdministrador, administrador),
            getPersistedAdministrador(administrador)
        );
    }

    @Test
    @Transactional
    void fullUpdateAdministradorWithPatch() throws Exception {
        // Initialize the database
        insertedAdministrador = administradorRepository.saveAndFlush(administrador);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the administrador using partial update
        Administrador partialUpdatedAdministrador = new Administrador();
        partialUpdatedAdministrador.setId(administrador.getId());

        partialUpdatedAdministrador.ativo(UPDATED_ATIVO);

        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdministrador.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdministrador))
            )
            .andExpect(status().isOk());

        // Validate the Administrador in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdministradorUpdatableFieldsEquals(partialUpdatedAdministrador, getPersistedAdministrador(partialUpdatedAdministrador));
    }

    @Test
    @Transactional
    void patchNonExistingAdministrador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrador.setId(longCount.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, administradorDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(administradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdministrador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrador.setId(longCount.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(administradorDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Administrador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdministrador() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        administrador.setId(longCount.incrementAndGet());

        // Create the Administrador
        AdministradorDTO administradorDTO = administradorMapper.toDto(administrador);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdministradorMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(administradorDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Administrador in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdministrador() throws Exception {
        // Initialize the database
        insertedAdministrador = administradorRepository.saveAndFlush(administrador);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the administrador
        restAdministradorMockMvc
            .perform(delete(ENTITY_API_URL_ID, administrador.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return administradorRepository.count();
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

    protected Administrador getPersistedAdministrador(Administrador administrador) {
        return administradorRepository.findById(administrador.getId()).orElseThrow();
    }

    protected void assertPersistedAdministradorToMatchAllProperties(Administrador expectedAdministrador) {
        assertAdministradorAllPropertiesEquals(expectedAdministrador, getPersistedAdministrador(expectedAdministrador));
    }

    protected void assertPersistedAdministradorToMatchUpdatableProperties(Administrador expectedAdministrador) {
        assertAdministradorAllUpdatablePropertiesEquals(expectedAdministrador, getPersistedAdministrador(expectedAdministrador));
    }
}
