package com.bankernel.web.rest;

import static com.bankernel.domain.EnderecoAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static com.bankernel.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Endereco;
import com.bankernel.domain.enumeration.EnumTipoEndereco;
import com.bankernel.repository.EnderecoRepository;
import com.bankernel.repository.UserRepository;
import com.bankernel.service.EnderecoService;
import com.bankernel.service.dto.EnderecoDTO;
import com.bankernel.service.mapper.EnderecoMapper;
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
 * Integration tests for the {@link EnderecoResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class EnderecoResourceIT {

    private static final String DEFAULT_CEP = "AAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBB";

    private static final String DEFAULT_LOGRADOURO = "AAAAAAAAAA";
    private static final String UPDATED_LOGRADOURO = "BBBBBBBBBB";

    private static final String DEFAULT_NUMERO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO = "BBBBBBBBBB";

    private static final String DEFAULT_COMPLEMENTO = "AAAAAAAAAA";
    private static final String UPDATED_COMPLEMENTO = "BBBBBBBBBB";

    private static final String DEFAULT_BAIRRO = "AAAAAAAAAA";
    private static final String UPDATED_BAIRRO = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AA";
    private static final String UPDATED_ESTADO = "BB";

    private static final EnumTipoEndereco DEFAULT_TIPO_ENDERECO = EnumTipoEndereco.RESIDENCIAL;
    private static final EnumTipoEndereco UPDATED_TIPO_ENDERECO = EnumTipoEndereco.COMERCIAL;

    private static final Boolean DEFAULT_PRINCIPAL = false;
    private static final Boolean UPDATED_PRINCIPAL = true;

    private static final BigDecimal DEFAULT_LATITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LATITUDE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_LONGITUDE = new BigDecimal(1);
    private static final BigDecimal UPDATED_LONGITUDE = new BigDecimal(2);

    private static final String ENTITY_API_URL = "/api/enderecos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private EnderecoRepository enderecoRepositoryMock;

    @Autowired
    private EnderecoMapper enderecoMapper;

    @Mock
    private EnderecoService enderecoServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEnderecoMockMvc;

    private Endereco endereco;

    private Endereco insertedEndereco;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Endereco createEntity() {
        return new Endereco()
            .cep(DEFAULT_CEP)
            .logradouro(DEFAULT_LOGRADOURO)
            .numero(DEFAULT_NUMERO)
            .complemento(DEFAULT_COMPLEMENTO)
            .bairro(DEFAULT_BAIRRO)
            .cidade(DEFAULT_CIDADE)
            .estado(DEFAULT_ESTADO)
            .tipoEndereco(DEFAULT_TIPO_ENDERECO)
            .principal(DEFAULT_PRINCIPAL)
            .latitude(DEFAULT_LATITUDE)
            .longitude(DEFAULT_LONGITUDE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Endereco createUpdatedEntity() {
        return new Endereco()
            .cep(UPDATED_CEP)
            .logradouro(UPDATED_LOGRADOURO)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .estado(UPDATED_ESTADO)
            .tipoEndereco(UPDATED_TIPO_ENDERECO)
            .principal(UPDATED_PRINCIPAL)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);
    }

    @BeforeEach
    void initTest() {
        endereco = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEndereco != null) {
            enderecoRepository.delete(insertedEndereco);
            insertedEndereco = null;
        }
    }

    @Test
    @Transactional
    void createEndereco() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);
        var returnedEnderecoDTO = om.readValue(
            restEnderecoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enderecoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EnderecoDTO.class
        );

        // Validate the Endereco in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEndereco = enderecoMapper.toEntity(returnedEnderecoDTO);
        assertEnderecoUpdatableFieldsEquals(returnedEndereco, getPersistedEndereco(returnedEndereco));

        insertedEndereco = returnedEndereco;
    }

    @Test
    @Transactional
    void createEnderecoWithExistingId() throws Exception {
        // Create the Endereco with an existing ID
        endereco.setId(1L);
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCepIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        endereco.setCep(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLogradouroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        endereco.setLogradouro(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNumeroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        endereco.setNumero(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBairroIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        endereco.setBairro(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCidadeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        endereco.setCidade(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEstadoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        endereco.setEstado(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTipoEnderecoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        endereco.setTipoEndereco(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPrincipalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        endereco.setPrincipal(null);

        // Create the Endereco, which fails.
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        restEnderecoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enderecoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEnderecos() throws Exception {
        // Initialize the database
        insertedEndereco = enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList
        restEnderecoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endereco.getId().intValue())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP)))
            .andExpect(jsonPath("$.[*].logradouro").value(hasItem(DEFAULT_LOGRADOURO)))
            .andExpect(jsonPath("$.[*].numero").value(hasItem(DEFAULT_NUMERO)))
            .andExpect(jsonPath("$.[*].complemento").value(hasItem(DEFAULT_COMPLEMENTO)))
            .andExpect(jsonPath("$.[*].bairro").value(hasItem(DEFAULT_BAIRRO)))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE)))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO)))
            .andExpect(jsonPath("$.[*].tipoEndereco").value(hasItem(DEFAULT_TIPO_ENDERECO.toString())))
            .andExpect(jsonPath("$.[*].principal").value(hasItem(DEFAULT_PRINCIPAL)))
            .andExpect(jsonPath("$.[*].latitude").value(hasItem(sameNumber(DEFAULT_LATITUDE))))
            .andExpect(jsonPath("$.[*].longitude").value(hasItem(sameNumber(DEFAULT_LONGITUDE))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEnderecosWithEagerRelationshipsIsEnabled() throws Exception {
        when(enderecoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEnderecoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(enderecoServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllEnderecosWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(enderecoServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restEnderecoMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(enderecoRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getEndereco() throws Exception {
        // Initialize the database
        insertedEndereco = enderecoRepository.saveAndFlush(endereco);

        // Get the endereco
        restEnderecoMockMvc
            .perform(get(ENTITY_API_URL_ID, endereco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(endereco.getId().intValue()))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP))
            .andExpect(jsonPath("$.logradouro").value(DEFAULT_LOGRADOURO))
            .andExpect(jsonPath("$.numero").value(DEFAULT_NUMERO))
            .andExpect(jsonPath("$.complemento").value(DEFAULT_COMPLEMENTO))
            .andExpect(jsonPath("$.bairro").value(DEFAULT_BAIRRO))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO))
            .andExpect(jsonPath("$.tipoEndereco").value(DEFAULT_TIPO_ENDERECO.toString()))
            .andExpect(jsonPath("$.principal").value(DEFAULT_PRINCIPAL))
            .andExpect(jsonPath("$.latitude").value(sameNumber(DEFAULT_LATITUDE)))
            .andExpect(jsonPath("$.longitude").value(sameNumber(DEFAULT_LONGITUDE)));
    }

    @Test
    @Transactional
    void getNonExistingEndereco() throws Exception {
        // Get the endereco
        restEnderecoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEndereco() throws Exception {
        // Initialize the database
        insertedEndereco = enderecoRepository.saveAndFlush(endereco);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the endereco
        Endereco updatedEndereco = enderecoRepository.findById(endereco.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEndereco are not directly saved in db
        em.detach(updatedEndereco);
        updatedEndereco
            .cep(UPDATED_CEP)
            .logradouro(UPDATED_LOGRADOURO)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .estado(UPDATED_ESTADO)
            .tipoEndereco(UPDATED_TIPO_ENDERECO)
            .principal(UPDATED_PRINCIPAL)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(updatedEndereco);

        restEnderecoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enderecoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enderecoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Endereco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEnderecoToMatchAllProperties(updatedEndereco);
    }

    @Test
    @Transactional
    void putNonExistingEndereco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, enderecoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enderecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEndereco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(enderecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEndereco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(enderecoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Endereco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEnderecoWithPatch() throws Exception {
        // Initialize the database
        insertedEndereco = enderecoRepository.saveAndFlush(endereco);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the endereco using partial update
        Endereco partialUpdatedEndereco = new Endereco();
        partialUpdatedEndereco.setId(endereco.getId());

        partialUpdatedEndereco
            .cep(UPDATED_CEP)
            .logradouro(UPDATED_LOGRADOURO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .principal(UPDATED_PRINCIPAL)
            .latitude(UPDATED_LATITUDE);

        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEndereco.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEndereco))
            )
            .andExpect(status().isOk());

        // Validate the Endereco in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnderecoUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedEndereco, endereco), getPersistedEndereco(endereco));
    }

    @Test
    @Transactional
    void fullUpdateEnderecoWithPatch() throws Exception {
        // Initialize the database
        insertedEndereco = enderecoRepository.saveAndFlush(endereco);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the endereco using partial update
        Endereco partialUpdatedEndereco = new Endereco();
        partialUpdatedEndereco.setId(endereco.getId());

        partialUpdatedEndereco
            .cep(UPDATED_CEP)
            .logradouro(UPDATED_LOGRADOURO)
            .numero(UPDATED_NUMERO)
            .complemento(UPDATED_COMPLEMENTO)
            .bairro(UPDATED_BAIRRO)
            .cidade(UPDATED_CIDADE)
            .estado(UPDATED_ESTADO)
            .tipoEndereco(UPDATED_TIPO_ENDERECO)
            .principal(UPDATED_PRINCIPAL)
            .latitude(UPDATED_LATITUDE)
            .longitude(UPDATED_LONGITUDE);

        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEndereco.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEndereco))
            )
            .andExpect(status().isOk());

        // Validate the Endereco in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEnderecoUpdatableFieldsEquals(partialUpdatedEndereco, getPersistedEndereco(partialUpdatedEndereco));
    }

    @Test
    @Transactional
    void patchNonExistingEndereco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, enderecoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enderecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEndereco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(enderecoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Endereco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEndereco() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        endereco.setId(longCount.incrementAndGet());

        // Create the Endereco
        EnderecoDTO enderecoDTO = enderecoMapper.toDto(endereco);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEnderecoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(enderecoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Endereco in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEndereco() throws Exception {
        // Initialize the database
        insertedEndereco = enderecoRepository.saveAndFlush(endereco);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the endereco
        restEnderecoMockMvc
            .perform(delete(ENTITY_API_URL_ID, endereco.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return enderecoRepository.count();
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

    protected Endereco getPersistedEndereco(Endereco endereco) {
        return enderecoRepository.findById(endereco.getId()).orElseThrow();
    }

    protected void assertPersistedEnderecoToMatchAllProperties(Endereco expectedEndereco) {
        assertEnderecoAllPropertiesEquals(expectedEndereco, getPersistedEndereco(expectedEndereco));
    }

    protected void assertPersistedEnderecoToMatchUpdatableProperties(Endereco expectedEndereco) {
        assertEnderecoAllUpdatablePropertiesEquals(expectedEndereco, getPersistedEndereco(expectedEndereco));
    }
}
