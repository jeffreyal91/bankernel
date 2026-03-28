package com.bankernel.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.TipoNegocio} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class TipoNegocioDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 100)
    private String nome;

    @NotNull
    private Boolean ativo;

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

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TipoNegocioDTO)) {
            return false;
        }

        TipoNegocioDTO tipoNegocioDTO = (TipoNegocioDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, tipoNegocioDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TipoNegocioDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            ", ativo='" + getAtivo() + "'" +
            "}";
    }
}
