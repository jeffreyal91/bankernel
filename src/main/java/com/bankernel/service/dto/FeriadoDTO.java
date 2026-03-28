package com.bankernel.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.bankernel.domain.Feriado} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FeriadoDTO implements Serializable {

    private Long id;

    @NotNull
    private LocalDate data;

    @NotNull
    @Size(max = 200)
    private String descricao;

    @NotNull
    private Boolean ativo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
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
        if (!(o instanceof FeriadoDTO)) {
            return false;
        }

        FeriadoDTO feriadoDTO = (FeriadoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, feriadoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FeriadoDTO{" +
            "id=" + getId() +
            ", data='" + getData() + "'" +
            ", descricao='" + getDescricao() + "'" +
            ", ativo='" + getAtivo() + "'" +
            "}";
    }
}
