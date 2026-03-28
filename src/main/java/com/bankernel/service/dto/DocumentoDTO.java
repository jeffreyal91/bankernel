package com.bankernel.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.Documento} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DocumentoDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 200)
    private String nome;

    @Size(max = 50)
    private String tipoArquivo;

    @Size(max = 500)
    private String endereco;

    private Long tamanho;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipoArquivo() {
        return tipoArquivo;
    }

    public void setTipoArquivo(String tipoArquivo) {
        this.tipoArquivo = tipoArquivo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Long getTamanho() {
        return tamanho;
    }

    public void setTamanho(Long tamanho) {
        this.tamanho = tamanho;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DocumentoDTO)) {
            return false;
        }

        DocumentoDTO documentoDTO = (DocumentoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, documentoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DocumentoDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", tipoArquivo='" + getTipoArquivo() + "'" +
            ", endereco='" + getEndereco() + "'" +
            ", tamanho=" + getTamanho() +
            "}";
    }
}
