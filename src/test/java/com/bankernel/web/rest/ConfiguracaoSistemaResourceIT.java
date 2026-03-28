package com.bankernel.web.rest;

import static com.bankernel.domain.ConfiguracaoSistemaAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.ConfiguracaoSistema;
import com.bankernel.domain.enumeration.EnumTipoConfiguracao;
import com.bankernel.repository.ConfiguracaoSistemaRepository;
import com.bankernel.service.dto.ConfiguracaoSistemaDTO;
import com.bankernel.service.mapper.ConfiguracaoSistemaMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ConfiguracaoSistemaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConfiguracaoSistemaResourceIT {

    private static final String DEFAULT_CHAVE = "AAAAAAAAAA";
    private static final String UPDATED_CHAVE = "BBBBBBBBBB";

    private static final String DEFAULT_VALOR = "AAAAAAAAAA";
    private static final String UPDATED_VALOR = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    private static final EnumTipoConfiguracao DEFAULT_TIPO = EnumTipoConfiguracao.STRING;
    private static final EnumTipoConfiguracao UPDATED_TIPO = EnumTipoConfiguracao.INTEGER;

    private static final String DEFAULT_MODULO = "AAAAAAAAAA";
    private static final String UPDATED_MODULO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVA = false;
    private static final Boolean UPDATED_ATIVA = true;

    private static final String ENTITY_API_URL = "/api/configuracao-sistemas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConfiguracaoSistemaRepository configuracaoSistemaRepository;

    @Autowired
    private ConfiguracaoSistemaMapper configuracaoSistemaMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfiguracaoSistemaMockMvc;

    private ConfiguracaoSistema configuracaoSistema;

    private ConfiguracaoSistema insertedConfiguracaoSistema;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfiguracaoSistema createEntity() {
        return new ConfiguracaoSistema()
            .chave(DEFAULT_CHAVE)
            .valor(DEFAULT_VALOR)
            .descricao(DEFAULT_DESCRICAO)
            .tipo(DEFAULT_TIPO)
            .modulo(DEFAULT_MODULO)
            .ativa(DEFAULT_ATIVA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfiguracaoSistema createUpdatedEntity() {
        return new ConfiguracaoSistema()
            .chave(UPDATED_CHAVE)
            .valor(UPDATED_VALOR)
            .descricao(UPDATED_DESCRICAO)
            .tipo(UPDATED_TIPO)
            .modulo(UPDATED_MODULO)
            .ativa(UPDATED_ATIVA);
    }

    @BeforeEach
    void initTest() {
        configuracaoSistema = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedConfiguracaoSistema != null) {
            configuracaoSistemaRepository.delete(insertedConfiguracaoSistema);
            insertedConfiguracaoSistema = null;
        }
    }

    @Test
    @Transactional
    void createConfiguracaoSistema() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ConfiguracaoSistema
        ConfiguracaoSistemaDTO configuracaoSistemaDTO = configuracaoSistemaMapper.toDto(configuracaoSistema);
        var returnedConfiguracaoSistemaDTO = om.readValue(
            restConfiguracaoSistemaMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configuracaoSistemaDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConfiguracaoSistemaDTO.class
        );

        // Validate the ConfiguracaoSistema in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConfiguracaoSistema = configuracaoSistemaMapper.toEntity(returnedConfiguracaoSistemaDTO);
        assertConfiguracaoSistemaUpdatableFieldsEquals(
            returnedConfiguracaoSistema,
            getPersistedConfiguracaoSistema(returnedConfiguracaoSistema)
        );

        insertedConfiguracaoSistema = returnedConfiguracaoSistema;
    }

    @Test
    @Transactional
    void createConfiguracaoSistemaWithExistingId() throws Exception {
        // Create the ConfiguracaoSistema with an existing ID
        configuracaoSistema.setId(1L);
        ConfiguracaoSistemaDTO configuracaoSistemaDTO = configuracaoSistemaMapper.toDto(configuracaoSistema);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfiguracaoSistemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configuracaoSistemaDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConfiguracaoSistema in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkChaveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        configuracaoSistema.setChave(null);

        // Create the ConfiguracaoSistema, which fails.
        ConfiguracaoSistemaDTO configuracaoSistemaDTO = configuracaoSistemaMapper.toDto(configuracaoSistema);

        restConfiguracaoSistemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configuracaoSistemaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValorIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        configuracaoSistema.setValor(null);

        // Create the ConfiguracaoSistema, which fails.
        ConfiguracaoSistemaDTO configuracaoSistemaDTO = configuracaoSistemaMapper.toDto(configuracaoSistema);

        restConfiguracaoSistemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configuracaoSistemaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        configuracaoSistema.setTipo(null);

        // Create the ConfiguracaoSistema, which fails.
        ConfiguracaoSistemaDTO configuracaoSistemaDTO = configuracaoSistemaMapper.toDto(configuracaoSistema);

        restConfiguracaoSistemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configuracaoSistemaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkModuloIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        configuracaoSistema.setModulo(null);

        // Create the ConfiguracaoSistema, which fails.
        ConfiguracaoSistemaDTO configuracaoSistemaDTO = configuracaoSistemaMapper.toDto(configuracaoSistema);

        restConfiguracaoSistemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configuracaoSistemaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAtivaIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        configuracaoSistema.setAtiva(null);

        // Create the ConfiguracaoSistema, which fails.
        ConfiguracaoSistemaDTO configuracaoSistemaDTO = configuracaoSistemaMapper.toDto(configuracaoSistema);

        restConfiguracaoSistemaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configuracaoSistemaDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConfiguracaoSistemas() throws Exception {
        // Initialize the database
        insertedConfiguracaoSistema = configuracaoSistemaRepository.saveAndFlush(configuracaoSistema);

        // Get all the configuracaoSistemaList
        restConfiguracaoSistemaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configuracaoSistema.getId().intValue())))
            .andExpect(jsonPath("$.[*].chave").value(hasItem(DEFAULT_CHAVE)))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR)))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO)))
            .andExpect(jsonPath("$.[*].tipo").value(hasItem(DEFAULT_TIPO.toString())))
            .andExpect(jsonPath("$.[*].modulo").value(hasItem(DEFAULT_MODULO)))
            .andExpect(jsonPath("$.[*].ativa").value(hasItem(DEFAULT_ATIVA)));
    }

    @Test
    @Transactional
    void getConfiguracaoSistema() throws Exception {
        // Initialize the database
        insertedConfiguracaoSistema = configuracaoSistemaRepository.saveAndFlush(configuracaoSistema);

        // Get the configuracaoSistema
        restConfiguracaoSistemaMockMvc
            .perform(get(ENTITY_API_URL_ID, configuracaoSistema.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configuracaoSistema.getId().intValue()))
            .andExpect(jsonPath("$.chave").value(DEFAULT_CHAVE))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO))
            .andExpect(jsonPath("$.tipo").value(DEFAULT_TIPO.toString()))
            .andExpect(jsonPath("$.modulo").value(DEFAULT_MODULO))
            .andExpect(jsonPath("$.ativa").value(DEFAULT_ATIVA));
    }

    @Test
    @Transactional
    void getNonExistingConfiguracaoSistema() throws Exception {
        // Get the configuracaoSistema
        restConfiguracaoSistemaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConfiguracaoSistema() throws Exception {
        // Initialize the database
        insertedConfiguracaoSistema = configuracaoSistemaRepository.saveAndFlush(configuracaoSistema);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the configuracaoSistema
        ConfiguracaoSistema updatedConfiguracaoSistema = configuracaoSistemaRepository.findById(configuracaoSistema.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConfiguracaoSistema are not directly saved in db
        em.detach(updatedConfiguracaoSistema);
        updatedConfiguracaoSistema
            .chave(UPDATED_CHAVE)
            .valor(UPDATED_VALOR)
            .descricao(UPDATED_DESCRICAO)
            .tipo(UPDATED_TIPO)
            .modulo(UPDATED_MODULO)
            .ativa(UPDATED_ATIVA);
        ConfiguracaoSistemaDTO configuracaoSistemaDTO = configuracaoSistemaMapper.toDto(updatedConfiguracaoSistema);

        restConfiguracaoSistemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configuracaoSistemaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(configuracaoSistemaDTO))
            )
            .andExpect(status().isOk());

        // Validate the ConfiguracaoSistema in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConfiguracaoSistemaToMatchAllProperties(updatedConfiguracaoSistema);
    }

    @Test
    @Transactional
    void putNonExistingConfiguracaoSistema() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoSistema.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoSistema
        ConfiguracaoSistemaDTO configuracaoSistemaDTO = configuracaoSistemaMapper.toDto(configuracaoSistema);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfiguracaoSistemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configuracaoSistemaDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(configuracaoSistemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfiguracaoSistema in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConfiguracaoSistema() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoSistema.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoSistema
        ConfiguracaoSistemaDTO configuracaoSistemaDTO = configuracaoSistemaMapper.toDto(configuracaoSistema);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfiguracaoSistemaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(configuracaoSistemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfiguracaoSistema in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConfiguracaoSistema() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoSistema.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoSistema
        ConfiguracaoSistemaDTO configuracaoSistemaDTO = configuracaoSistemaMapper.toDto(configuracaoSistema);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfiguracaoSistemaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(configuracaoSistemaDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfiguracaoSistema in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConfiguracaoSistemaWithPatch() throws Exception {
        // Initialize the database
        insertedConfiguracaoSistema = configuracaoSistemaRepository.saveAndFlush(configuracaoSistema);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the configuracaoSistema using partial update
        ConfiguracaoSistema partialUpdatedConfiguracaoSistema = new ConfiguracaoSistema();
        partialUpdatedConfiguracaoSistema.setId(configuracaoSistema.getId());

        partialUpdatedConfiguracaoSistema.chave(UPDATED_CHAVE).descricao(UPDATED_DESCRICAO).modulo(UPDATED_MODULO);

        restConfiguracaoSistemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfiguracaoSistema.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConfiguracaoSistema))
            )
            .andExpect(status().isOk());

        // Validate the ConfiguracaoSistema in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConfiguracaoSistemaUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConfiguracaoSistema, configuracaoSistema),
            getPersistedConfiguracaoSistema(configuracaoSistema)
        );
    }

    @Test
    @Transactional
    void fullUpdateConfiguracaoSistemaWithPatch() throws Exception {
        // Initialize the database
        insertedConfiguracaoSistema = configuracaoSistemaRepository.saveAndFlush(configuracaoSistema);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the configuracaoSistema using partial update
        ConfiguracaoSistema partialUpdatedConfiguracaoSistema = new ConfiguracaoSistema();
        partialUpdatedConfiguracaoSistema.setId(configuracaoSistema.getId());

        partialUpdatedConfiguracaoSistema
            .chave(UPDATED_CHAVE)
            .valor(UPDATED_VALOR)
            .descricao(UPDATED_DESCRICAO)
            .tipo(UPDATED_TIPO)
            .modulo(UPDATED_MODULO)
            .ativa(UPDATED_ATIVA);

        restConfiguracaoSistemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfiguracaoSistema.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConfiguracaoSistema))
            )
            .andExpect(status().isOk());

        // Validate the ConfiguracaoSistema in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConfiguracaoSistemaUpdatableFieldsEquals(
            partialUpdatedConfiguracaoSistema,
            getPersistedConfiguracaoSistema(partialUpdatedConfiguracaoSistema)
        );
    }

    @Test
    @Transactional
    void patchNonExistingConfiguracaoSistema() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoSistema.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoSistema
        ConfiguracaoSistemaDTO configuracaoSistemaDTO = configuracaoSistemaMapper.toDto(configuracaoSistema);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfiguracaoSistemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, configuracaoSistemaDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(configuracaoSistemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfiguracaoSistema in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConfiguracaoSistema() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoSistema.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoSistema
        ConfiguracaoSistemaDTO configuracaoSistemaDTO = configuracaoSistemaMapper.toDto(configuracaoSistema);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfiguracaoSistemaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(configuracaoSistemaDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfiguracaoSistema in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConfiguracaoSistema() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        configuracaoSistema.setId(longCount.incrementAndGet());

        // Create the ConfiguracaoSistema
        ConfiguracaoSistemaDTO configuracaoSistemaDTO = configuracaoSistemaMapper.toDto(configuracaoSistema);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfiguracaoSistemaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(configuracaoSistemaDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfiguracaoSistema in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConfiguracaoSistema() throws Exception {
        // Initialize the database
        insertedConfiguracaoSistema = configuracaoSistemaRepository.saveAndFlush(configuracaoSistema);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the configuracaoSistema
        restConfiguracaoSistemaMockMvc
            .perform(delete(ENTITY_API_URL_ID, configuracaoSistema.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return configuracaoSistemaRepository.count();
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

    protected ConfiguracaoSistema getPersistedConfiguracaoSistema(ConfiguracaoSistema configuracaoSistema) {
        return configuracaoSistemaRepository.findById(configuracaoSistema.getId()).orElseThrow();
    }

    protected void assertPersistedConfiguracaoSistemaToMatchAllProperties(ConfiguracaoSistema expectedConfiguracaoSistema) {
        assertConfiguracaoSistemaAllPropertiesEquals(
            expectedConfiguracaoSistema,
            getPersistedConfiguracaoSistema(expectedConfiguracaoSistema)
        );
    }

    protected void assertPersistedConfiguracaoSistemaToMatchUpdatableProperties(ConfiguracaoSistema expectedConfiguracaoSistema) {
        assertConfiguracaoSistemaAllUpdatablePropertiesEquals(
            expectedConfiguracaoSistema,
            getPersistedConfiguracaoSistema(expectedConfiguracaoSistema)
        );
    }
}
