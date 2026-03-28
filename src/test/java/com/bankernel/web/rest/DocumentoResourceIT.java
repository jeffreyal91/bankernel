package com.bankernel.web.rest;

import static com.bankernel.domain.DocumentoAsserts.*;
import static com.bankernel.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.bankernel.IntegrationTest;
import com.bankernel.domain.Documento;
import com.bankernel.repository.DocumentoRepository;
import com.bankernel.service.dto.DocumentoDTO;
import com.bankernel.service.mapper.DocumentoMapper;
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
 * Integration tests for the {@link DocumentoResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentoResourceIT {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_TIPO_ARQUIVO = "AAAAAAAAAA";
    private static final String UPDATED_TIPO_ARQUIVO = "BBBBBBBBBB";

    private static final String DEFAULT_ENDERECO = "AAAAAAAAAA";
    private static final String UPDATED_ENDERECO = "BBBBBBBBBB";

    private static final Long DEFAULT_TAMANHO = 1L;
    private static final Long UPDATED_TAMANHO = 2L;

    private static final String ENTITY_API_URL = "/api/documentos";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentoRepository documentoRepository;

    @Autowired
    private DocumentoMapper documentoMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentoMockMvc;

    private Documento documento;

    private Documento insertedDocumento;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Documento createEntity() {
        return new Documento().nome(DEFAULT_NOME).tipoArquivo(DEFAULT_TIPO_ARQUIVO).endereco(DEFAULT_ENDERECO).tamanho(DEFAULT_TAMANHO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Documento createUpdatedEntity() {
        return new Documento().nome(UPDATED_NOME).tipoArquivo(UPDATED_TIPO_ARQUIVO).endereco(UPDATED_ENDERECO).tamanho(UPDATED_TAMANHO);
    }

    @BeforeEach
    void initTest() {
        documento = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumento != null) {
            documentoRepository.delete(insertedDocumento);
            insertedDocumento = null;
        }
    }

    @Test
    @Transactional
    void createDocumento() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);
        var returnedDocumentoDTO = om.readValue(
            restDocumentoMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentoDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentoDTO.class
        );

        // Validate the Documento in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumento = documentoMapper.toEntity(returnedDocumentoDTO);
        assertDocumentoUpdatableFieldsEquals(returnedDocumento, getPersistedDocumento(returnedDocumento));

        insertedDocumento = returnedDocumento;
    }

    @Test
    @Transactional
    void createDocumentoWithExistingId() throws Exception {
        // Create the Documento with an existing ID
        documento.setId(1L);
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNomeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documento.setNome(null);

        // Create the Documento, which fails.
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        restDocumentoMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentoDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentos() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get all the documentoList
        restDocumentoMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documento.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME)))
            .andExpect(jsonPath("$.[*].tipoArquivo").value(hasItem(DEFAULT_TIPO_ARQUIVO)))
            .andExpect(jsonPath("$.[*].endereco").value(hasItem(DEFAULT_ENDERECO)))
            .andExpect(jsonPath("$.[*].tamanho").value(hasItem(DEFAULT_TAMANHO.intValue())));
    }

    @Test
    @Transactional
    void getDocumento() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        // Get the documento
        restDocumentoMockMvc
            .perform(get(ENTITY_API_URL_ID, documento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documento.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME))
            .andExpect(jsonPath("$.tipoArquivo").value(DEFAULT_TIPO_ARQUIVO))
            .andExpect(jsonPath("$.endereco").value(DEFAULT_ENDERECO))
            .andExpect(jsonPath("$.tamanho").value(DEFAULT_TAMANHO.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingDocumento() throws Exception {
        // Get the documento
        restDocumentoMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumento() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documento
        Documento updatedDocumento = documentoRepository.findById(documento.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumento are not directly saved in db
        em.detach(updatedDocumento);
        updatedDocumento.nome(UPDATED_NOME).tipoArquivo(UPDATED_TIPO_ARQUIVO).endereco(UPDATED_ENDERECO).tamanho(UPDATED_TAMANHO);
        DocumentoDTO documentoDTO = documentoMapper.toDto(updatedDocumento);

        restDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentoDTO))
            )
            .andExpect(status().isOk());

        // Validate the Documento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentoToMatchAllProperties(updatedDocumento);
    }

    @Test
    @Transactional
    void putNonExistingDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documento.setId(longCount.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentoDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documento.setId(longCount.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documento.setId(longCount.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Documento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentoWithPatch() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documento using partial update
        Documento partialUpdatedDocumento = new Documento();
        partialUpdatedDocumento.setId(documento.getId());

        partialUpdatedDocumento.tipoArquivo(UPDATED_TIPO_ARQUIVO).tamanho(UPDATED_TAMANHO);

        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumento))
            )
            .andExpect(status().isOk());

        // Validate the Documento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentoUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumento, documento),
            getPersistedDocumento(documento)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentoWithPatch() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documento using partial update
        Documento partialUpdatedDocumento = new Documento();
        partialUpdatedDocumento.setId(documento.getId());

        partialUpdatedDocumento.nome(UPDATED_NOME).tipoArquivo(UPDATED_TIPO_ARQUIVO).endereco(UPDATED_ENDERECO).tamanho(UPDATED_TAMANHO);

        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumento.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumento))
            )
            .andExpect(status().isOk());

        // Validate the Documento in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentoUpdatableFieldsEquals(partialUpdatedDocumento, getPersistedDocumento(partialUpdatedDocumento));
    }

    @Test
    @Transactional
    void patchNonExistingDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documento.setId(longCount.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentoDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documento.setId(longCount.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentoDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Documento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumento() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documento.setId(longCount.incrementAndGet());

        // Create the Documento
        DocumentoDTO documentoDTO = documentoMapper.toDto(documento);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentoMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentoDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Documento in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumento() throws Exception {
        // Initialize the database
        insertedDocumento = documentoRepository.saveAndFlush(documento);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documento
        restDocumentoMockMvc
            .perform(delete(ENTITY_API_URL_ID, documento.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentoRepository.count();
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

    protected Documento getPersistedDocumento(Documento documento) {
        return documentoRepository.findById(documento.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentoToMatchAllProperties(Documento expectedDocumento) {
        assertDocumentoAllPropertiesEquals(expectedDocumento, getPersistedDocumento(expectedDocumento));
    }

    protected void assertPersistedDocumentoToMatchUpdatableProperties(Documento expectedDocumento) {
        assertDocumentoAllUpdatablePropertiesEquals(expectedDocumento, getPersistedDocumento(expectedDocumento));
    }
}
